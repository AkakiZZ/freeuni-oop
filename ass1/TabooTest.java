// TabooTest.java
// Taboo class tests -- nothing provided.

import java.util.*;

import junit.framework.TestCase;

public class TabooTest extends TestCase {
    public void testNoFollow1() {
        List<String> rules = Arrays.asList("a", "c", "a", "b");
        Taboo<String> taboo = new Taboo<>(rules);
        Set<String> set1 = new HashSet<>();
        set1.add("b");
        set1.add("c");
        Set<String> set2 = new HashSet<>();
        set2.add("a");
        assertEquals(set1, taboo.noFollow("a"));
        assertEquals(set2, taboo.noFollow("c"));
    }

    public void testNoFollow2() {
        List<String> rules = Arrays.asList("a", "c", "a", "k", "a", "z", "a");
        Taboo<String> taboo = new Taboo<>(rules);
        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();
        set1.add("c");
        set1.add("k");
        set1.add("z");
        set2.add("a");
        assertEquals(set1, taboo.noFollow("a"));
        assertEquals(set2, taboo.noFollow("z"));
    }

    public void testReduce1() {
        List<String> rules = new ArrayList<>(Arrays.asList("a", "c", "a", "b"));
        List<String> list = new ArrayList<>(Arrays.asList("a", "c", "b", "x", "c", "a"));
        Taboo<String> taboo = new Taboo<>(rules);
        taboo.reduce(list);
        assertEquals(Arrays.asList("a", "x", "c"), list);
    }

    public void testReduce2() {
        List<String> rules = new ArrayList<>(Arrays.asList("a", "c"));
        List<String> list = new ArrayList<>(Arrays.asList("a", "c", "b", "x", "c", "a"));
        Taboo<String> taboo = new Taboo<>(rules);
        taboo.reduce(list);
        assertEquals(Arrays.asList("a", "b", "x", "c", "a"), list);
    }
}
