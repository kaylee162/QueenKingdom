import refactor.DisjointSet;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import refactor.Vertex;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Basic student tests to check DisjointSet. These tests are in
 * no way comprehensive, nor do they guarantee any kind of grade.
 *
 * @author CS 1332 TAs
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DisjointSetStudentTests {
    private static final int TIMEOUT = 200;

    private DisjointSet<Vertex<String>> disjointSet;
    private Vertex<String> a;
    private Vertex<String> b;
    private Vertex<String> c;

    @Before
    public void setup() {
        a = new Vertex<>("A");
        b = new Vertex<>("B");
        c = new Vertex<>("C");
        disjointSet = new DisjointSet<>();
        disjointSet.find(a);
        disjointSet.find(b);
        disjointSet.find(c);
        disjointSet.union(a, b);
    }

    @Test(timeout = TIMEOUT)
    public void testSetup() {
        assertEquals(a, disjointSet.find(a));
        assertEquals(a, disjointSet.find(b));
        assertEquals(c, disjointSet.find(c));
    }

    @Test(timeout = TIMEOUT)
    public void testRoots() {
        assertEquals(Set.of(a, c), disjointSet.getRoots());
    }
}
