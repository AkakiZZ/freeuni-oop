// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

import java.security.KeyPair;
import java.util.ArrayList;

public class CharGrid {
	private char[][] grid;

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}
	
	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		int min_i_index = Integer.MAX_VALUE;
		int max_i_index = -1;
		int min_j_index = Integer.MAX_VALUE;
		int max_j_index = -1;
		boolean occurred = false;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j] == ch) {
					if (i < min_i_index) min_i_index = i;
					if (i > max_i_index) max_i_index = i;
					if (j < min_j_index) min_j_index = j;
					if (j > max_j_index) max_j_index = j;
					occurred = true;
				}
			}
		}
		if (!occurred) return 0;
		return (max_i_index - min_i_index + 1) * (max_j_index - min_j_index + 1);
	}

	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */
	public int countPlus() {
		int res = 0;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (isPlus(i, j)) res++;
			}
		}
		return res; // YOUR CODE HERE
	}

	private boolean isPlus(int i, int j) {
		boolean res = true;
		int length = armLength(i, j, 0);
		for (int k = 0; k < 4; k++) {
			res = res && (length == armLength(i, j, k)) && length != 0;
		}
		return res;
	}
	//0 up, 1 right, 2 down, else left
	private int armLength(int i, int j, int direction) {
		int res = 0;
		char startChar = grid[i][j];
		while (true) {
			if (direction == 0) {
				if (inBounds(i - 1, j) && grid[i - 1][j] == startChar) {
					res++;
					i--;
				} else {
					break;
				}
			} else if (direction == 1) {
				if (inBounds(i, j + 1) && grid[i][j + 1] == startChar) {
					res++;
					j++;
				} else {
					break;
				}
			} else if (direction == 2) {
				if (inBounds(i + 1, j) && grid[i + 1][j] == startChar) {
					res++;
					i++;
				} else {
					break;
				}
			} else {
				if (inBounds(i, j - 1) && grid[i][j - 1] == startChar) {
					res++;
					j--;
				} else {
					break;
				}
			}
		}
		return res;
	}

	private boolean inBounds(int i, int j) {
		return i >=0 && i < grid.length && j >= 0 && j < grid[i].length;
	}
}
