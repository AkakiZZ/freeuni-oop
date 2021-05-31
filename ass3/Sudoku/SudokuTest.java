import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SudokuTest {
    private Sudoku sudokuEasy;
    private Sudoku sudokuHard;
    private Sudoku sudokuMedium;
    private Sudoku sudokuChangedHardGrid;
    private Sudoku sudokuWithManySols;

    private static boolean isValidSolution(int board[][]) {
        for (int[] ints : board) {
            HashSet<Integer> rowNumbers = new HashSet<>();
            for (int j = 0; j < ints.length; j++) {
                rowNumbers.add(ints[j]);
            }
            if (rowNumbers.size() != 9) return false;
        }
        for (int i = 0; i < board.length; i++) {
            HashSet<Integer> columnNumbers = new HashSet<>();
            for (int j = 0; j < board[i].length; j++) {
                columnNumbers.add(board[j][i]);
            }
            if (columnNumbers.size() != 9) return false;
        }

        for (int i = 0; i < board.length; i+=3) {
            for (int j = 0; j < board[i].length; j+=3) {
                HashSet<Integer> squareNumbers = new HashSet<>();
                for (int k = i; k < i + 3; k++) {
                    for (int m = j; m < j + 3; m++) {
                        squareNumbers.add(board[k][m]);
                    }
                }
                if (squareNumbers.size() != 9) return false;
            }
        }
        return true;
    }

    private static final String easyBoardString =
            "1 6 4 0 0 0 0 0 2 \n" +
	        "2 0 0 4 0 3 9 1 0 \n" +
            "0 0 5 0 8 0 4 0 7 \n" +
            "0 9 0 0 0 6 5 0 0 \n" +
            "5 0 0 1 0 2 0 0 8 \n" +
            "0 0 8 9 0 0 0 3 0 \n" +
            "8 0 9 0 4 0 2 0 0 \n" +
            "0 7 3 5 0 9 0 0 1 \n" +
            "4 0 0 0 0 0 6 7 9 \n";


    @BeforeAll
    public void setup() {
        sudokuEasy = new Sudoku(Sudoku.easyGrid);
        sudokuHard = new Sudoku(Sudoku.hardGrid);
        sudokuMedium = new Sudoku(Sudoku.mediumGrid);
        Sudoku.main(null);
        sudokuChangedHardGrid = new Sudoku(
                "3 0 0 0 0 0 0 8 0" +
                "0 0 1 0 9 3 0 0 0" +
                "0 4 0 7 8 0 0 0 3" +
                "0 9 3 8 0 0 0 1 2" +
                "0 0 0 0 4 0 0 0 0" +
                "5 2 0 0 0 6 7 9 0" +
                "6 0 0 0 2 1 0 4 0" +
                "0 0 0 5 3 0 9 0 0" +
                "0 3 0 0 0 0 0 5 1");
        sudokuWithManySols = new Sudoku(
                "3 0 0 0 0 0 0 8 0" +
                        "0 0 1 0 9 3 0 0 0" +
                        "0 4 0 7 8 0 0 0 3" +
                        "0 9 3 8 0 0 0 1 2" +
                        "0 0 0 0 4 0 0 0 0" +
                        "5 2 0 0 0 6 7 9 0" +
                        "6 0 0 0 2 1 0 0 0" +
                        "0 0 0 5 3 0 0 0 0" +
                        "0 3 0 0 0 0 0 0 0");
    }

    @Test
    public void testSolutionSolution() {
        assertEquals(1, sudokuHard.solve());
        assertEquals(1, sudokuMedium.solve());
        assertEquals(6, sudokuChangedHardGrid.solve());
        assertEquals(100, sudokuWithManySols.solve());
        assertTrue(isValidSolution(Sudoku.textToGrid(sudokuHard.getSolutionText())));
        assertTrue(isValidSolution(Sudoku.textToGrid(sudokuMedium.getSolutionText())));
        assertTrue(isValidSolution(Sudoku.textToGrid(sudokuChangedHardGrid.getSolutionText())));
        assertTrue(isValidSolution(Sudoku.textToGrid(sudokuWithManySols.getSolutionText())));
    }

    @Test
    public void testBoardToString() {
        assertEquals(easyBoardString, sudokuEasy.toString());
    }

    @Test
    public void testException() {
        assertThrows(RuntimeException.class, () -> Sudoku.textToGrid("1 2"));
    }
}
