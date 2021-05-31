import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	private final int[][] originalBoard;
	private int[][] board;
	private int[][] solution;
	private final List<Spot> spots;
	private int numSolutions;
	private long timeSpent;

	// Provided grid data for main/testing
	// The instance variable strategy is up to you.

	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");


	private static final int SIZE = 9; // size of the whole 9x9 puzzle
	private static final int PART = 3;
	private static final int MAX_SOLUTIONS = 100;
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}

	private static String boardToString(int[][] arr) {
		if (arr == null) throw new NullPointerException("Solution doesn't exist");
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++)
				result.append(arr[i][j]).append(" ");
			result.append("\n");
		}

		return result.toString();
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);

		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}
	
	
	

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		originalBoard = ints;
		board = new int[SIZE][SIZE];
		spots = new ArrayList<>();
		numSolutions = 0;
		for (int i = 0; i < SIZE; i++) {
			board[i] = Arrays.copyOf(ints[i], SIZE);
			for (int j = 0; j < SIZE; j++) {
				if (originalBoard[i][j] == 0) spots.add(new Spot(i, j));
			}
		}
		Collections.sort(spots);
	}

	public Sudoku(String text) {
		this(textToGrid(text));
	}
	
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		numSolutions = 0;
		solution = null;
		long start = System.currentTimeMillis();
		solveHelper(0);
		long end = System.currentTimeMillis();
		timeSpent = end - start;
		return numSolutions;
	}
	
	public String getSolutionText() {
		return boardToString(solution);
	}

	public long getElapsed() {
		return timeSpent;
	}

	public String toString() {
		return boardToString(originalBoard);
	}

	private void solveHelper(int index) {
		if (numSolutions == MAX_SOLUTIONS)
			return;

		if (index == spots.size()) {
			if (numSolutions == 0) {
				solution = new int[SIZE][SIZE];
				for (int i = 0; i < SIZE; i++) {
					solution[i] = Arrays.copyOf(board[i], SIZE);
				}
			}
			numSolutions++;
			return;
		}

		Spot currentSpot = spots.get(index);
		List<Integer> validValues = currentSpot.getValidNumbers();

		for (int value : validValues) {
			currentSpot.setValue(value);
			solveHelper(index + 1);
			currentSpot.setValue(0);
		}
	}

	private class Spot implements Comparable<Spot>{
		private final int row;
		private final int col;
		private int value;
		private List<Integer> possibleNumbers = getValidNumbers();

		public Spot(int row, int col) {
			this.row = row;
			this.col = col;
		}

		public void setValue(int value) {
			this.value = value;
			board[row][col] = value;
		}

		public List<Integer> getValidNumbers() {
			Set<Integer> possibleNumbers = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
			checkRowCol(possibleNumbers);
			checkSquare(possibleNumbers);
			return new ArrayList<>(possibleNumbers);
		}

		private void checkRowCol(Set<Integer> possibleNumbers) {
			for (int i = 0; i < SIZE; i++) {
				possibleNumbers.remove(board[row][i]);
				possibleNumbers.remove(board[i][col]);
			}
		}

		private void checkSquare(Set<Integer> possibleNumbers) {
			int firstSquareX = (row / PART) * PART;
			int firstSquareY = (col / PART) * PART;
			for (int i = 0; i < PART; i++) {
				for (int j = 0; j < PART; j++) {
					possibleNumbers.remove(board[firstSquareX + i][firstSquareY + j]);
				}
			}
		}

		@Override
		public int compareTo(Spot o) {
			return this.getValidNumbers().size() - o.getValidNumbers().size();
		}
	}

}
