import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;

public class SudokuGrid extends JPanel {
    private JTextField[][] cells;
    private int gridSize;

    public SudokuGrid(int size) {
        this.gridSize = size;
        setLayout(new GridLayout(size, size));
        cells = new JTextField[size][size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                cells[row][col] = new JTextField();
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                add(cells[row][col]);
                setCellBorder(row, col);
            }
        }
    }

    private void setCellBorder(int row, int col) {
        int sqrt = (int) Math.sqrt(gridSize);
        int top = (row % sqrt == 0) ? 3 : 1;
        int left = (col % sqrt == 0) ? 3 : 1;
        int bottom = (row == gridSize - 1) ? 3 : 1;
        int right = (col == gridSize - 1) ? 3 : 1;

        Border border = BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK);
        cells[row][col].setBorder(border);
    }

    public void populateGrid(int[][] puzzle) {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (puzzle[row][col] != 0) {
                    cells[row][col].setText(String.valueOf(puzzle[row][col]));
                    cells[row][col].setEditable(false);
                } else {
                    cells[row][col].setText("");
                    cells[row][col].setEditable(true);
                }
            }
        }
    }

    public void displaySolution(int[][] solution) {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                cells[row][col].setText(String.valueOf(solution[row][col]));
                cells[row][col].setEditable(false);
            }
        }
    }

    public int[][] getGridValues() {
        int[][] values = new int[gridSize][gridSize];
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                try {
                    values[row][col] = Integer.parseInt(cells[row][col].getText());
                } catch (NumberFormatException e) {
                    values[row][col] = 0;
                }
            }
        }
        return values;
    }
}
