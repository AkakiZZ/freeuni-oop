import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BoardTest {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated, square, stickRotated;

	@BeforeAll
	public void setUp() {
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();

		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();

		square = new Piece(Piece.SQUARE_STR);
		stickRotated = new Piece(Piece.STICK_STR).computeNextRotation();
	}

	// Check the basic width/height/max after the one placement
	@Test
	public void testSample1() {
		b = new Board(3, 6);
		b.place(pyr1, 0, 0);
		b.commit();
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}

	// Place sRotated into the board, then check some measures
	@Test
	public void testSample2() {
		b = new Board(3, 6);
		pyr1 = new Piece(Piece.PYRAMID_STR);
		b.place(pyr1, 0, 0);
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
		b.commit();
	}

	@Test
	public void testBasicMethods() {
		b = new Board(3, 6);
		b.place(pyr1, 0, 0);
		assertEquals(3, b.getWidth());
		assertEquals(6, b.getHeight());
		assertEquals(2, b.getMaxHeight());
		assertTrue(b.getGrid(0, 0));
	}

	@Test
	public void testBadPlace() {
		b = new Board(4, 6);
		assertEquals(Board.PLACE_OUT_BOUNDS, b.place(sRotated, -1, 0));
		b.undo();
		assertEquals(Board.PLACE_OK, b.place(pyr1, 0, 0));
		b.commit();
		assertEquals(Board.PLACE_BAD, b.place(pyr1, 0, 0));
		b.undo();
		b.commit();
	}

	@Test
	public void testClearRows() {
		b = new Board(4, 6);
		assertEquals(Board.PLACE_ROW_FILLED, b.place(stickRotated, 0, 0));
		assertEquals(1, b.clearRows());
		assertEquals(0, b.getRowWidth(0));
		b.commit();
		assertFalse(b.getGrid(0, 0));
		assertEquals(Board.PLACE_OK, b.place(square, 2, 0));
		b.commit();
		assertEquals(Board.PLACE_ROW_FILLED, b.place(square, 0, 0));
		b.clearRows();
		b.undo();
		assertEquals(2, b.getRowWidth(0));
		assertEquals(2, b.getRowWidth(1));
		b.commit();
	}

	@Test
	public void testDropHeight() {
		b = new Board(4, 6);
		b.place(square, 0, 0);
		b.commit();
		b.place(square, 2, 0);
		b.commit();
		assertEquals(2, b.dropHeight(pyr1, 0));
		b.place(pyr1, 0, 2);
		b.commit();
		assertEquals(4, b.dropHeight(pyr1, 1));
	}

	@Test
	public void testToString() {
		b = new Board(3, 6);
		b.place(pyr1, 0, 0);
		assertEquals("|   |\n|   |\n|   |\n|   |\n| + |\n|+++|\n-----", b.toString());
		b.commit();
		b.place(pyr1, 0, 2);
		b.commit();
		assertEquals("|   |\n|   |\n| + |\n|+++|\n| + |\n|+++|\n-----", b.toString());
	}

	@Test
	public void testExceptions() {
		b = new Board(3, 6);
		b.place(pyr1, 0, 0);
		b.commit();
		assertThrows(RuntimeException.class, () -> { b.checkHeights(new int[] {0, 0, 0});});
		assertThrows(RuntimeException.class, () -> { b.checkWidths(new int[] {0, 0, 0});});
		assertThrows(RuntimeException.class, () -> { b.checkMaxHeight(0);});
	}

}
