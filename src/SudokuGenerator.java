import java.util.Random;

public class SudokuGenerator {
    private Random random = new Random();

    public int[][] generateSudokuSolution(int size) {
        int[][] grid = new int[size][size];
        fillGrid(grid, size);
        return grid;
    }

    private boolean fillGrid(int[][] grid, int size) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (grid[row][col] == 0) {
                    for (int num = 1; num <= size; num++) {
                        if (isValid(grid, row, col, num, size)) {
                            grid[row][col] = num;
                            if (fillGrid(grid, size)) {
                                return true;
                            } else {
                                grid[row][col] = 0; // backtrack
                            }
                        }
                    }
                    return false; // no number is valid here
                }
            }
        }
        return true; // solution found
    }

    private boolean isValid(int[][] grid, int row, int col, int num, int size) {
        for (int x = 0; x < size; x++) {
            if (grid[row][x] == num || grid[x][col] == num) {
                return false;
            }
        }
        int sqrt = (int) Math.sqrt(size);
        int boxRowStart = row - row % sqrt;
        int boxColStart = col - col % sqrt;
        for (int r = boxRowStart; r < boxRowStart + sqrt; r++) {
            for (int d = boxColStart; d < boxColStart + sqrt; d++) {
                if (grid[r][d] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    public int[][] generateSudokuPuzzle(int[][] solution, int size) {
        int[][] puzzle = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(solution[i], 0, puzzle[i], 0, size);
        }

        int cluesToKeep = (int) (size * size * 0.4); // keep 40% of the numbers
        while (cluesToKeep > 0) {
            int row = random.nextInt(size);
            int col = random.nextInt(size);
            if (puzzle[row][col] != 0) {
                puzzle[row][col] = 0;
                cluesToKeep--;
            }
        }

        return puzzle;
    }
}
