//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.

public class TetrisGrid {

	boolean[][] grid;
	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		this.grid = grid;
	}

	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
		for (int i = 0; i < grid[0].length; i++) {
			if (isFilled(i)) {
				deleteRow(i);
				i--;
			}
		}
	}

	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		return grid;
	}

	private void deleteRow(int NthRow) {
		for (int i = NthRow; i < grid[0].length - 1; i++) {
			for (int j = 0; j < grid.length; j++) {
				grid[j][i] = grid[j][i + 1];
			}
		}
		for (int j = 0; j < grid.length; j++) {
			grid[j][grid[0].length - 1] = false;
		}
	}

	private boolean isFilled(int NthRow) {
		boolean res = true;
		for (int j = 0; j < grid.length; j++) {
			res = res && grid[j][NthRow];
		}
		return res;
	}
}
