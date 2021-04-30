import java.util.*;

public class Appearances {
	
	/**
	 * Returns the number of elements that appear the same number
	 * of times in both collections. Static method. (see handout).
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
		HashMap<T, Integer> map1 = new HashMap<>();
		HashMap<T, Integer> map2 = new HashMap<>();
		for (T elem : a) {
			if (map1.containsKey(elem)) {
				map1.put(elem, map1.get(elem) + 1);
			} else {
				map1.put(elem, 1);
			}
		}
		for (T elem : b) {
			if (map1.containsKey(elem)) {
				map1.put(elem, map1.get(elem) - 1);
			}
		}
		int count = 0;
		for (T elem : map1.keySet()) {
			if (map1.get(elem) == 0)
				count++;
		}
 		return count;
	}
	
}
