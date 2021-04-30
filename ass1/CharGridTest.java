
// Test cases for CharGrid -- a few basic tests are provided.

import junit.framework.TestCase;

public class CharGridTest extends TestCase {

	public void testCharArea1() {
		char[][] grid = new char[][] {
				{'a', 'y', ' '},
				{'x', 'a', 'z'},
			};
		
		CharGrid cg = new CharGrid(grid);
				
		assertEquals(4, cg.charArea('a'));
		assertEquals(1, cg.charArea('z'));
	}
	
	
	public void testCharArea2() {
		char[][] grid = new char[][] {
				{'c', 'a', ' '},
				{'b', ' ', 'b'},
				{' ', 'k', 'a'}
		};
		
		CharGrid cg = new CharGrid(grid);
		
		assertEquals(6, cg.charArea('a'));
		assertEquals(3, cg.charArea('b'));
		assertEquals(1, cg.charArea('c'));
		assertEquals(9, cg.charArea(' '));
		assertEquals(1, cg.charArea('k'));

	}
	
	public void testCharArea3() {
		char[][] grid = new char[][] {
				{'c', 'c', 'c'},
				{'b', 'a', 'b'},
				{'z', 'z', 'a'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(0, cg.charArea('y'));
		assertEquals(3, cg.charArea('c'));
		assertEquals(2, cg.charArea('z'));
	}

	public void testCharArea4() {
		char[][] grid = new char[][] {
				{'c', 'c', 'c'},
				{'b', 'a', 'b'},
				{'z', 'z', 'c'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(9, cg.charArea('c'));
		assertEquals(2, cg.charArea('z'));
	}


	public void testCountPlus0() {
		char[][] grid = new char[][] {
				{' '}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(0, cg.countPlus());
	}

	public void testCountPlus1() {
		char[][] grid = new char[][] {
				{' ', 'c', ' '},
				{'c', 'c', 'c'},
				{' ', 'c', ' '}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(1, cg.countPlus());
	}

	public void testCountPlus2() {
		char[][] grid = new char[][] {
				{' ', 'c', ' '},
				{'c', ' ', 'c'},
				{' ', 'c', ' '}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(0, cg.countPlus());
	}

	public void testCountPlus3() {
		char[][] grid = new char[][] {
				{' ', 'c', ' ', ' '},
				{'c', 'c', 'c', ' '},
				{' ', 'c', 'c', 'c'},
				{' ', ' ', 'c', ' '},
				{' ', ' ', ' ', ' '}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(2, cg.countPlus());
	}
	
	
}
