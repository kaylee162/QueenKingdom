import apply.Building;
import apply.Intersection;
import apply.StaticWaddleWorks;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import refactor.Edge;
import refactor.MutableGraph;
import refactor.StaticGraph;
import refactor.Vertex;

import java.util.*;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WaddleWorksStudentTests {
    private static final int TIMEOUT = 200;

    private StaticWaddleWorks waddleWorks;

    private Vertex<Intersection> i1, i2, i3;
    private Vertex<Building> b1, b2, b3, b4;
    private Set<Edge<Building>> gridEdges;
    private Set<Vertex<Building>> gridVertices;
    private Set<Vertex<Intersection>> roadVertices;
    private Set<Edge<Intersection>> roadEdges;


    @Before
    public void setup() {
        i1 = new Vertex<>(new Intersection("A"));
        i2 = new Vertex<>(new Intersection("B"));
        i3 = new Vertex<>(new Intersection("C"));

        roadVertices = Set.of(i1, i2, i3);
        roadEdges = new HashSet<>();

        roadEdges.add(new Edge<>(i1, i2, 5));
        roadEdges.add(new Edge<>(i2, i3, 3));
        roadEdges.add(new Edge<>(i1, i3, 7));

        MutableGraph<Intersection> roads = Main.getMutableGraphInstance(roadVertices, roadEdges);

        b1 = new Vertex<>(new Building("B1", i1.data()));
        b2 = new Vertex<>(new Building("B2", i2.data()));
        b3 = new Vertex<>(new Building("B3", i3.data()));
        b4 = new Vertex<>(new Building("B4", i1.data()));

        gridVertices = Set.of(b1, b2, b3, b4);
        gridEdges = new HashSet<>();

        gridEdges.add(new Edge<>(b1, b2, 10));
        gridEdges.add(new Edge<>(b2, b3, 5));
        gridEdges.add(new Edge<>(b3, b4, 7));
        gridEdges.add(new Edge<>(b1, b4, 12));

        MutableGraph<Building> grid = Main.getMutableGraphInstance(gridVertices, gridEdges);
        waddleWorks = Main.getWaddleWorksInstance(roads, grid);
    }

    @Test(timeout = TIMEOUT)
    public void testStandardInit() {
        StaticGraph<Intersection> roadsGraph = waddleWorks.getRoads();
        StaticGraph<Building> gridGraph = waddleWorks.getGrid();

        assertEquals(roadVertices, roadsGraph.getVertices());
        assertEquals(roadEdges, roadsGraph.getEdges());
        assertEquals(gridVertices, gridGraph.getVertices());
        assertEquals(gridEdges, gridGraph.getEdges());
    }

    @Test(timeout = TIMEOUT)
    public void testConsolidateGrid() {
        System.out.println(waddleWorks.getGrid().getEdges());
        assertEquals(12. / 34, waddleWorks.consolidateGrid(), 0.001);

        Set<Edge<Building>> edges = new HashSet<>(gridEdges);
        edges.removeIf(o -> o.weight() == 12);
        assertEquals(edges, waddleWorks.getGrid().getEdges());
    }

    @Test(timeout = TIMEOUT)
    public void testCalculateRoute() {
        Intersection i4 = new Intersection("D"), i5 = new Intersection("E", Intersection.Type.HIGHWAY), i6 = new Intersection("F");
        Building b5 = new Building("B5", i6);
        waddleWorks.addRoad(i1.data(), i4, 4);
        waddleWorks.addRoad(i4, i5, 1);
        waddleWorks.addRoad(i5, i6, 1);
        waddleWorks.addRoad(i4, i6, 10);
        waddleWorks.addWire(b4.data(), b5, 10);
        assertEquals(List.of(i3.data(), i1.data(), i4, i6), waddleWorks.calculateRoute(b3.data(), b5, Set.of(Intersection.Type.HIGHWAY)));
    }

    @Test(timeout = TIMEOUT)
    public void testNeighbors() {
        Intersection i4 = new Intersection("D"), i5 = new Intersection("E", Intersection.Type.HIGHWAY);
        waddleWorks.addRoad(i4, i5, 5);
        assertEquals(2, waddleWorks.getNeighborhoodCount());
        Map<Intersection, Set<Intersection>> map = waddleWorks.getNeighborhoodsByConnections(0,    2);
        assertEquals(2, map.size());
        List<Set<Intersection>> expected = new ArrayList<>(List.of(Set.of(i1.data(), i2.data(), i3.data()), Set.of(i4, i5)));

        for (Map.Entry<Intersection, Set<Intersection>> entry : map.entrySet()) {
            assertTrue(entry.getValue().contains(entry.getKey()));
            assertTrue(expected.contains(entry.getValue()));
            expected.remove(entry.getValue());
        }
    }

    @Test(timeout = TIMEOUT)
    public void testMinIntersections() {
        Intersection i4 = new Intersection("D"), i5 = new Intersection("E", Intersection.Type.HIGHWAY), i6 = new Intersection("F");
        Building b5 = new Building("B5", i6);
        waddleWorks.addRoad(i1.data(), i4, 4);
        waddleWorks.addRoad(i4, i5, 1);
        waddleWorks.addRoad(i5, i6, 1);
        assertFalse(waddleWorks.addRoad(i4, i6, 10));
        waddleWorks.addWire(b4.data(), b5, 10);
        assertEquals(3, waddleWorks.getMinBetween(b3.data(), b5));
    }

    @Test(timeout = TIMEOUT)
    public void testBestPowerSite() {
        Building b5 = new Building("B5", i3.data());
        waddleWorks.addWire(b4.data(), b5, 10);
        assertEquals(b1.data(), waddleWorks.getBestPowerSite(List.of(b1.data(), b5)));
    }

}
