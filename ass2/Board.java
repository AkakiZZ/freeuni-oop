// Board.java

import java.util.Arrays;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private int maxHeight;
	private boolean[][] grid;
	private int[] widths;
	private int[] heights;
	private boolean DEBUG = true;
	boolean committed;

	private boolean[][] backUpGrid;
	private int[] backUpWidths;
	private int[] backUpHeights;
	private int backUpMaxHeight;
	// Here a few trivial methods are provided:

	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		maxHeight = 0;
		backUpMaxHeight = 0;
		grid = new boolean[width][height];
		widths = new int[height];
		heights = new int[width];
		committed = true;
		backUpGrid = new boolean[width][height];
		backUpWidths = new int[height];
		backUpHeights = new int[width];
	}


	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}


	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}


	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() { return maxHeight; }


	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			int[] exceptedHeights = new int[width];
			int[] exceptedWidths = new int[height];
			int exceptedMaxHeight = 0;

			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[0].length; j++) {
					if (grid[i][j]) exceptedWidths[j] += 1;
					if (grid[i][j]) exceptedHeights[i] = j + 1;
					if (exceptedHeights[i] > exceptedMaxHeight) exceptedMaxHeight = exceptedHeights[i];
				}
			}
			checkHeights(exceptedHeights);
			checkWidths(exceptedWidths);
			checkMaxHeight(exceptedMaxHeight);
		}
	}

	protected void checkHeights(int[] exceptedHeights) {
		if (!Arrays.equals(exceptedHeights, heights))
			throw new RuntimeException("Heights array isn't sane");
	}

	protected void checkWidths(int[] exceptedWidths) {
		if (!Arrays.equals(exceptedWidths, widths))
			throw new RuntimeException("Withs array isn't sane");
	}

	protected void checkMaxHeight(int exceptedMaxHeight) {
		if (exceptedMaxHeight != maxHeight)
			throw new RuntimeException("Max height is incorrect");
	}

	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.

	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int[] skirt = piece.getSkirt();
		int res = 0;
		for (int i = x; i < x + piece.getWidth(); i++) {
			int height = heights[i] - skirt[i - x];
			if (height > 0 && height > res) res = height;
		}
		return res;
	}


	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x];
	}


	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return widths[y];
	}


	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if (!inBounds(x, y)) return true;
		return grid[x][y];
	}

	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;

	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.

	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");

		int result = PLACE_OK;
		committed = false;
		save();

		for(TPoint p : piece.getBody()) {

			int px = x + p.x;
			int py = y + p.y;

			if (!inBounds(px, py))
				return PLACE_OUT_BOUNDS;

			if (getGrid(px, py))
				return PLACE_BAD;

			grid[px][py] = true;

			if (++widths[py] == width)
				result = PLACE_ROW_FILLED;

			if (heights[px] < py + 1)
				heights[px] = py + 1;

			if (getMaxHeight() < heights[px])
				maxHeight = heights[px];
		}
		sanityCheck();
		return result;
	}


	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		committed = false;
		int rowsCleared = 0;
		for (int i = 0; i < getMaxHeight(); i++) {
			if (widths[i] == width) {
				clearRow(i);
				rowsCleared++;
				i--;
			}
		}
		sanityCheck();
		return rowsCleared;
	}

	/**
	 * Clears a row
	 */
	private void clearRow(int y) {
		for (int i = 0; i < grid.length; i++) {
			System.arraycopy(grid[i], y + 1, grid[i], y, height - y - 1);
			int h = 0;
			for (int j = 0; j < getMaxHeight(); j++) {
				if (grid[i][j]) h = j + 1;
			}
			heights[i] = h;
			if (h > maxHeight) maxHeight = h;
		}
		System.arraycopy(widths, y + 1, widths, y, height - y - 1);
		maxHeight--;
	}


	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if (committed) return;

		committed = true;

		int[] tempWidths = widths;
		widths = backUpWidths;
		backUpWidths = tempWidths;

		int[] tempHeights = heights;
		heights = backUpHeights;
		backUpHeights = tempHeights;

		boolean[][] tempGrid = grid;
		grid = backUpGrid;
		backUpGrid = tempGrid;

		maxHeight = backUpMaxHeight;

		sanityCheck();
	}


	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}

	/**
	 * Returns if p(x,y) is in bounds of the board
	 */
	private boolean inBounds(int x, int y) {
		return x < width && y < height && x >= 0 && y >= 0;
	}

	/**
	 * Saves committed data
	 */
	private void save() {
		for (int i = 0; i < width; i++) {
			System.arraycopy(grid[i], 0, backUpGrid[i], 0, height);
		}

		System.arraycopy(heights, 0, backUpHeights, 0, width);
		System.arraycopy(widths, 0, backUpWidths, 0, height);

		backUpMaxHeight = maxHeight;
	}


	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}

}


