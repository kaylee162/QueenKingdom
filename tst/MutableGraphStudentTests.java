import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import refactor.Edge;
import refactor.MutableGraph;
import refactor.Vertex;
import refactor.VertexDistance;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Basic student tests to check your MutableGraph implementation. These tests 
 * are in no way comprehensive, nor do they guarantee any kind of grade.
 *
 * @author CS 1332 TAs
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MutableGraphStudentTests {
    private static final int TIMEOUT = 200;

    private MutableGraph<String> mutableGraph;
    private Set<Vertex<String>> vertices;
    private Set<Edge<String>> edges;
    private Vertex<String> a;
    private Vertex<String> b;
    private Vertex<String> c;

    @Before
    public void setup() {
        a = new Vertex<>("A");
        b = new Vertex<>("B");
        c = new Vertex<>("C");
        vertices = Set.of(a, b, c);
        edges = Set.of(new Edge<>(a, b, 1), new Edge<>(b, c, 2));
        mutableGraph = Main.getMutableGraphInstance(vertices, edges);
    }

    @Test(timeout = TIMEOUT)
    public void testSetup() {
        assertEquals(vertices, mutableGraph.getVertices());
        assertEquals(edges, mutableGraph.getEdges());
    }

    @Test(timeout = TIMEOUT)
    public void testAdjList() {
        Map<Vertex<String>, List<VertexDistance<String>>> expected = new HashMap<>();
        expected.put(a, new ArrayList<>(List.of(new VertexDistance<>(b, 1))));
        expected.put(b, new ArrayList<>(List.of(new VertexDistance<>(a, 1), new VertexDistance<>(c, 2))));
        expected.put(c, new ArrayList<>(List.of(new VertexDistance<>(b, 2))));

        assertEquals(expected.size(), mutableGraph.getAdjList().size());
        assertEquals(expected.keySet(), mutableGraph.getAdjList().keySet());
        for (Vertex<String> vertex : expected.keySet()) {
            Collections.sort(expected.get(vertex));
            Collections.sort(mutableGraph.getAdjList().get(vertex));
            assertEquals(expected.get(vertex), mutableGraph.getAdjList().get(vertex));
        }
    }
}
