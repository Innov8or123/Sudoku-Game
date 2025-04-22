import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuGame extends JFrame {
    private SudokuGrid gridPanel;
    private SudokuGenerator generator;
    private int[][] solution;
    private int[][] puzzle;
    private int gridSize;
    private Timer timer;
    private JLabel timerLabel;
    private int timeRemaining;

    public SudokuGame() {
        // Setup main window
        setTitle("Sudoku Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);

        // Initialize Sudoku generator
        generator = new SudokuGenerator();

        // Show difficulty selection screen
        showDifficultySelection();

        setVisible(true);
    }

    // Show the difficulty selection screen
    private void showDifficultySelection() {
        getContentPane().removeAll(); // Clear previous content

        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new GridLayout(3, 1));

        JButton easyButton = new JButton("Easy (4x4)");
        JButton mediumButton = new JButton("Medium (9x9)");
        JButton hardButton = new JButton("Hard (16x16)");

        levelPanel.add(easyButton);
        levelPanel.add(mediumButton);
        levelPanel.add(hardButton);

        // Action listeners for difficulty levels
        easyButton.addActionListener(e -> startNewGame(4, 5 * 60));  // 5 minutes for Easy
        mediumButton.addActionListener(e -> startNewGame(9, 15 * 60)); // 15 minutes for Medium
        hardButton.addActionListener(e -> startNewGame(16, 25 * 60));  // 25 minutes for Hard

        add(levelPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    // Start a new Sudoku game with a timer for the selected difficulty level
    private void startNewGame(int size, int timeLimit) {
        gridSize = size;
        solution = generator.generateSudokuSolution(size);
        puzzle = generator.generateSudokuPuzzle(solution, size);
        timeRemaining = timeLimit; // Set the time limit

        getContentPane().removeAll(); // Clear previous content

        // Create new grid
        gridPanel = new SudokuGrid(size);
        gridPanel.populateGrid(puzzle);
        add(gridPanel, BorderLayout.CENTER);

        // Timer display
        timerLabel = new JLabel("Time Remaining: " + formatTime(timeRemaining), JLabel.CENTER);
        add(timerLabel, BorderLayout.NORTH);

        // Start the timer
        startTimer();

        // Add buttons panel
        JPanel buttonPanel = new JPanel();
        JButton checkButton = new JButton("Check Solution");
        JButton displaySolutionButton = new JButton("Display Solution");
        JButton backButton = new JButton("Back to Difficulty Selection");

        buttonPanel.add(checkButton);
        buttonPanel.add(displaySolutionButton);
        buttonPanel.add(backButton);

        // Add action listener for checking the solution
        checkButton.addActionListener(new CheckSolutionAction());
        // Add action listener for displaying the solution
        displaySolutionButton.addActionListener(e -> {
            if (timer != null) {
                timer.stop(); // Stop the timer when showing the solution
            }
            gridPanel.displaySolution(solution);
            JOptionPane.showMessageDialog(null, "Solution is displayed.");
        });
        // Add action listener for going back to difficulty selection
        backButton.addActionListener(e -> {
            if (timer != null) {
                timer.stop(); // Stop the timer when going back
            }
            showDifficultySelection();
        });

        add(buttonPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    // Start the game timer
    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                timerLabel.setText("Time Remaining: " + formatTime(timeRemaining));

                if (timeRemaining <= 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(null, "Time's up! Game over ;)");
                    showSolutionCompleteScreen();
                }
            }
        });
        timer.start();
    }

    // Format the time in minutes and seconds
    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    // Action to check if the player's solution is correct
    private class CheckSolutionAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int[][] userValues = gridPanel.getGridValues();

            if (isGridValid(userValues)) {
                if (SudokuSolver.isSolutionCorrect(userValues, solution, gridSize)) {
                    if (timer != null) {
                        timer.stop(); // Stop the timer when the solution is correct
                    }
                    JOptionPane.showMessageDialog(null, "Congratulations! You solved the puzzle.");
                    gridPanel.displaySolution(solution);
                    showSolutionCompleteScreen();
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect solution. Try again.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please fill in all the cells with valid numbers.");
            }
        }

        private boolean isGridValid(int[][] grid) {
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    int value = grid[i][j];
                    if (value < 0 || value > gridSize || value == 0) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    // Show the solution complete screen with a back button
    private void showSolutionCompleteScreen() {
        getContentPane().removeAll();

        JPanel panel = new JPanel(new BorderLayout());
        JLabel messageLabel = new JLabel("Solution is displayed. You can go back to select another difficulty.", JLabel.CENTER);
        panel.add(messageLabel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Difficulty Selection");
        backButton.addActionListener(e -> showDifficultySelection());
        panel.add(backButton, BorderLayout.SOUTH);

        add(panel);

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuGame::new);
    }
}
