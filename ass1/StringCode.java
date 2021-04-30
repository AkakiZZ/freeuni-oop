import java.util.HashSet;
import java.util.Set;

// CS108 HW1 -- String static methods

public class StringCode {

	/**
	 * Given a string, returns the length of the largest run.
	 * A a run is a series of adajcent chars that are the same.
	 * @param str
	 * @return max run length
	 */
	public static int maxRun(String str) {
		int max_count = 0;
		for (int i = 0; i < str.length(); i++) {
			int count = 0;
			char curr = str.charAt(i);
			for (int j = i; j < str.length(); j++) {
				if (curr != str.charAt(j)) break;
				count++;
			}
			if (count > max_count) max_count = count;
		}
		return max_count;
	}

	
	/**
	 * Given a string, for each digit in the original string,
	 * replaces the digit with that many occurrences of the character
	 * following. So the string "a3tx2z" yields "attttxzzz".
	 * @param str
	 * @return blown up string
	 */
	public static String blowup(String str) {
		StringBuilder res = new StringBuilder("");
		if (str.length() == 0) return str;
		for (int i = 0; i < str.length() - 1; i++) {
			if (Character.isDigit(str.charAt(i))) {
				for (int j = 0; j < (str.charAt(i) - '0'); j++) {
					res.append(str.charAt(i + 1));
				}
			} else {
				res.append(str.charAt(i));
			}
		}
		if (!Character.isDigit(str.charAt(str.length() - 1))) {
			res.append(str.charAt(str.length() - 1));
		}
		return res.toString();
	}
	
	/**
	 * Given 2 strings, consider all the substrings within them
	 * of length len. Returns true if there are any such substrings
	 * which appear in both strings.
	 * Compute this in linear time using a HashSet. Len will be 1 or more.
	 */
	public static boolean stringIntersect(String a, String b, int len) {
		if (a.length() < len || b.length() < len) {
			throw new IllegalArgumentException("Len is greater than size of string");
		}
		HashSet<String> hs = new HashSet<>();
		for (int i = 0; i < a.length() - len + 1; i++) {
			hs.add(a.substring(i, i + len));
		}
		for (int i = 0; i < b.length() - len + 1; i++) {
			if (hs.contains(b.substring(i, i + len))) {
				return true;
			}
		}
		return false; // YOUR CODE HERE
	}
}
