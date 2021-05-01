import junit.framework.TestCase;

import java.util.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s, sRotated;
	protected void setUp() throws Exception {
		super.setUp();
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
	}
	
	// Here are some sample tests to get you started
	
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		
		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
	}
	
	
	// Test the skirt returned by a few pieces
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
	}

	public void testFastRotations() {
		Piece p = new Piece("0 0 0 1 1 0 1 1");
		Piece[] arr = Piece.getPieces();

		//Stick
		assertTrue(Arrays.equals(new int[] {0, 0, 0, 0}, arr[Piece.STICK].fastRotation().getSkirt()));
		assertTrue(arr[Piece.STICK].equals(arr[Piece.STICK].fastRotation().fastRotation()));

		//Square
		assertTrue(arr[Piece.SQUARE].fastRotation().equals(arr[Piece.SQUARE]));

		//L1
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, arr[Piece.L1].fastRotation().getSkirt()));
		assertTrue(Arrays.equals(new int[] {2, 0}, arr[Piece.L1].fastRotation().fastRotation().getSkirt()));
		assertTrue(arr[Piece.L1].fastRotation().fastRotation().fastRotation().fastRotation().equals(arr[Piece.L1]));

	}

	public void testException() {
		String badInput = "01 1 1";
		try {
			Piece p = new Piece("01 1 1");
			fail("Expected exception");
		} catch (RuntimeException expect) {
			assertEquals("Could not parse x,y string:" + badInput, expect.getMessage());
		}
	}
	
	
}