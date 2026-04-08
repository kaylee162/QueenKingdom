import implement.GraphAlgorithms;
import org.junit.Before;
import org.junit.Test;
import refactor.Edge;
import refactor.StaticGraph;
import refactor.Vertex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Basic student tests to check GraphAlgorithms. These tests are in
 * no way comprehensive, nor do they guarantee any kind of grade.
 *
 * @author CS 1332 TAs
 * @version 2.0
 */
public class GraphAlgorithmsStudentTests {

    private StaticGraph<Integer> graph1;
    private StaticGraph<Character> graph2;
    public static final int TIMEOUT = 200;

    @Before
    public void init() {
        graph1 = createGraph1();
        graph2 = createGraph2();
    }

    private StaticGraph<Integer> createGraph1() {
        Set<Vertex<Integer>> vertices = new HashSet<>();
        for (int i = 1; i <= 7; i++) {
            vertices.add(new Vertex<>(i));
        }

        Set<Edge<Integer>> edges = new LinkedHashSet<>();
        edges.add(new Edge<>(new Vertex<>(1), new Vertex<>(2), 0));
        edges.add(new Edge<>(new Vertex<>(1), new Vertex<>(3), 0));
        edges.add(new Edge<>(new Vertex<>(1), new Vertex<>(4), 0));
        edges.add(new Edge<>(new Vertex<>(3), new Vertex<>(5), 0));
        edges.add(new Edge<>(new Vertex<>(4), new Vertex<>(6), 0));
        edges.add(new Edge<>(new Vertex<>(5), new Vertex<>(4), 0));
        edges.add(new Edge<>(new Vertex<>(5), new Vertex<>(7), 0));
        edges.add(new Edge<>(new Vertex<>(7), new Vertex<>(6), 0));

        return Main.getMutableGraphInstance(vertices, edges);
    }

    private StaticGraph<Character> createGraph2() {
        Set<Vertex<Character>> vertices = new HashSet<>();
        for (int i = 65; i <= 70; i++) {
            vertices.add(new Vertex<Character>((char) i));
        }

        Set<Edge<Character>> edges = new LinkedHashSet<>();
        edges.add(new Edge<>(new Vertex<>('A'), new Vertex<>('B'), 7));
        edges.add(new Edge<>(new Vertex<>('A'), new Vertex<>('C'), 5));
        edges.add(new Edge<>(new Vertex<>('C'), new Vertex<>('D'), 2));
        edges.add(new Edge<>(new Vertex<>('A'), new Vertex<>('D'), 4));
        edges.add(new Edge<>(new Vertex<>('D'), new Vertex<>('E'), 1));
        edges.add(new Edge<>(new Vertex<>('B'), new Vertex<>('E'), 3));
        edges.add(new Edge<>(new Vertex<>('B'), new Vertex<>('F'), 8));
        edges.add(new Edge<>(new Vertex<>('E'), new Vertex<>('F'), 6));

        return Main.getMutableGraphInstance(vertices, edges);
    }

    @Test(timeout = TIMEOUT)
    public void testBFS() {
        List<Vertex<Integer>> bfsActual = GraphAlgorithms.bfs(
                new Vertex<>(1), graph1);

        List<Vertex<Integer>> bfsExpected = new LinkedList<>();
        bfsExpected.add(new Vertex<>(1));
        bfsExpected.add(new Vertex<>(2));
        bfsExpected.add(new Vertex<>(3));
        bfsExpected.add(new Vertex<>(4));
        bfsExpected.add(new Vertex<>(5));
        bfsExpected.add(new Vertex<>(6));
        bfsExpected.add(new Vertex<>(7));

        assertEquals(bfsExpected, bfsActual);
    }

    @Test(timeout = TIMEOUT)
    public void testDFS() {
        List<Vertex<Integer>> dfsActual = GraphAlgorithms.dfs(
                new Vertex<>(5), graph1);

        List<Vertex<Integer>> dfsExpected = new LinkedList<>();
        dfsExpected.add(new Vertex<>(5));
        dfsExpected.add(new Vertex<>(3));
        dfsExpected.add(new Vertex<>(1));
        dfsExpected.add(new Vertex<>(2));
        dfsExpected.add(new Vertex<>(4));
        dfsExpected.add(new Vertex<>(6));
        dfsExpected.add(new Vertex<>(7));

        assertEquals(dfsExpected, dfsActual);
    }

    @Test(timeout = TIMEOUT)
    public void testDijkstras() {
        Map<Vertex<Character>, Integer> dijkActual = GraphAlgorithms.dijkstras(
                new Vertex<>('D'), graph2);
        Map<Vertex<Character>, Integer> dijkExpected = new HashMap<>();
        dijkExpected.put(new Vertex<>('A'), 4);
        dijkExpected.put(new Vertex<>('B'), 4);
        dijkExpected.put(new Vertex<>('C'), 2);
        dijkExpected.put(new Vertex<>('D'), 0);
        dijkExpected.put(new Vertex<>('E'), 1);
        dijkExpected.put(new Vertex<>('F'), 7);

        assertEquals(dijkExpected, dijkActual);
    }

    @Test(timeout = TIMEOUT)
    public void testPrims() {
        Set<Edge<Character>> mstActual = GraphAlgorithms.prims(
                new Vertex<>('A'), graph2);
        Set<Edge<Character>> mstExpected = new HashSet<>();
        mstExpected.add(new Edge<>(new Vertex<>('C'), new Vertex<>('D'), 2));
        mstExpected.add(new Edge<>(new Vertex<>('A'), new Vertex<>('D'), 4));
        mstExpected.add(new Edge<>(new Vertex<>('D'), new Vertex<>('E'), 1));
        mstExpected.add(new Edge<>(new Vertex<>('B'), new Vertex<>('E'), 3));
        mstExpected.add(new Edge<>(new Vertex<>('E'), new Vertex<>('F'), 6));

        assertEquals(mstExpected, mstActual);
    }

    @Test(timeout = TIMEOUT)
    public void testKruskals() {
        Set<Edge<Character>> mstActual = GraphAlgorithms.kruskals(
                graph2);

        Set<Edge<Character>> mstExpected = new HashSet<>();
        mstExpected.add(new Edge<>(new Vertex<>('C'), new Vertex<>('D'), 2));
        mstExpected.add(new Edge<>(new Vertex<>('A'), new Vertex<>('D'), 4));
        mstExpected.add(new Edge<>(new Vertex<>('D'), new Vertex<>('E'), 1));
        mstExpected.add(new Edge<>(new Vertex<>('B'), new Vertex<>('E'), 3));
        mstExpected.add(new Edge<>(new Vertex<>('E'), new Vertex<>('F'), 6));

        assertEquals(mstExpected, mstActual);
    }
}