public class SudokuSolver {
    public static boolean isSolutionCorrect(int[][] userSolution, int[][] correctSolution, int size) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (userSolution[row][col] != correctSolution[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }
}
