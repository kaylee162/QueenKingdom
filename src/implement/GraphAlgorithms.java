package implement;
// lowgurtgeniuenly its time to cook yall 

import refactor.DisjointSet;
import refactor.Edge;
import refactor.StaticGraph;
import refactor.Vertex;
import refactor.VertexDistance;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * @author Kaylee Henry
 * @version 1.0
 * @userid khenry61
 * @GTID 904065531
 * <br>
 * <p>
 * Collaborators: None
 * <p>
 * Resources: None
 * <p>
 * <br>
 * By typing 'I agree' below, you are agreeing that this is your
 * own work and that you are responsible for the contents of all
 * submitted files. If this is left blank, this project will lose
 * points.
 *<p>
 *<br>
 * Agree Here: I agree  
 * 
 * ------------------------------------------------
 * 
 * High-level overview:
 * This class implements the core graph algorithms that are used later in QueenKingdom
 *
 * Our super swag toolkit and what they do:
 * - BFS: used to find the shortest path in an unweighted graph by exploring outward in layers (level by levle)
 *   which is great for traversal and connectivity checks
 * - DFS: used to fully explore a graph by going as deep as possible before backtracking, 
 *   which is great for traversal and connectivity checks
 * - Dijkstra (aka D): used to find the shortest weighted path from one starting vertex to every 
 *   other reachable vertex (when edge weights are nonnegative)
 * - Prim's: used to build a mst by growing one connected tree outward 
 *   from a starting vertex using the cheapest edges
 *   Krushkal's: used to build a mst by repeatedly choosing the globally cheapest valid edge
 *   without creating cycles
 */
public class GraphAlgorithms {

    /**
     * Breadth-First Search (BFS) aka BFFs :)
     *
     * BFS's algorithm explores the graph outward in "layers" from the start node.
     *    (i.e. vertices that rae one edge away from the start are explored before
     *    vertices that are two edges away, etc)
     * The runtime is O(V + E) because each vertex is visited once,
     * and each edge is examined at most once.
     *
     * @param <T> the data type stored in the vertices
     * @param start the starting vertex for the traversal
     * @param graph the graph to traverse
     * @return a list of vertices in the order they are visited
     */
    public static <T> List<Vertex<T>> bfs(Vertex<T> start, StaticGraph<T> graph) {
        validateStartAndGraph(start, graph);

        List<Vertex<T>> visitedOrder = new ArrayList<>(); // this will be the final traversal order
        Set<Vertex<T>> visited = new HashSet<>(); // this will keep track of where we've gone to prevent revisiting
        Queue<Vertex<T>> queue = new ArrayDeque<>(); // this is our frontier / the TO-DO list

        visited.add(start);
        queue.add(start); 

        // this is the main loop where BFS can happen 
        // we keep going until there are no more vertices to explore (i.e. the queue is empty)
        while (!queue.isEmpty()) {
            Vertex<T> current = queue.remove(); 
            visitedOrder.add(current); 

            for (VertexDistance<T> neighbor : graph.getNeighbors(current)) {
                Vertex<T> next = neighbor.vertex();

                if (!visited.contains(next)) {
                    visited.add(next); 
                    queue.add(next); 
                }
            }
        }

        return visitedOrder;
    }

    /**
     * Depth-First Search (DFS)
     *
     * DFS's algorithm goes as deep as possible before backtracking
     * The runtime is O(V + E) because each vertex is visited once,
     * and each edge is examined at most once.
     *
     * @param <T> the data type stored in the vertices
     * @param start the starting vertex for the traversal
     * @param graph the graph to traverse
     * @return a list of vertices in the order they are visited
     */
    public static <T> List<Vertex<T>> dfs(Vertex<T> start, StaticGraph<T> graph) {
        validateStartAndGraph(start, graph);

        List<Vertex<T>> visitedOrder = new ArrayList<>(); // this will store the order we visited nodes in
        
        // this will track which nodes we've already seen so we don't get stuck in a cycle (cuz that would be bad)
        Set<Vertex<T>> visited = new HashSet<>();

        // i used a recursive helper method to keep the main method clean and focused on validation and setup,
        // but the actual depth-first behavior happens in the helper method below
        dfs(start, graph, visited, visitedOrder);
        return visitedOrder;
    }

    /**
     * The recursive DFS helper
     *
     * This is where the actual depth-first behavior happens.
     * Each call visits one vertex, records it, and recursively explores
     * each unvisited neighbor in adjacency-list order
     *
     * @param <T> the data type stored in the vertices
     * @param curr the current vertex being explored
     * @param g the graph being traversed
     * @param vSet the set of already visited vertices
     * @param list the traversal order being built
     */
    private static <T> void dfs(Vertex<T> curr, StaticGraph<T> g,
                                Set<Vertex<T>> vSet, List<Vertex<T>> list) {

        vSet.add(curr); 
        list.add(curr);

        // this is the main DFS loop where we recursively explore neighbors
        // we go as deep as possible down one path before backtracking to explore other paths
        for (VertexDistance<T> neighbor : g.getNeighbors(curr)) {
            Vertex<T> next = neighbor.vertex();

            // so we only recurse if we haven't seen this neighbor 
            //  - to avoid cycles and infinite recursion (bc that would be bad)
            if (!vSet.contains(next)) {
                dfs(next, g, vSet, list);
            }
        }
    }

    /**
     * D's Algorithm
     * 
     * D's algorithm computes the shortest-path distances from the start vertex
     * to every reachable vertex in the graph, leaving unreachable
     * vertices at Integer.MAX_VALUE.
     * This assumes that all edge weights are non-negative
     *
     * @param <T> the data type stored in the vertices
     * @param start the starting vertex
     * @param graph the graph on which to run Dijkstra's algorithm
     * @return a map from each vertex to its shortest distance from start
     */
    public static <T> Map<Vertex<T>, Integer> dijkstras(Vertex<T> start,
                                                        StaticGraph<T> graph) {
        validateStartAndGraph(start, graph);

        // this map will store the best-known distance to each vertex from the start
        Map<Vertex<T>, Integer> distances = new HashMap<>();

        // Initialize all distances to "infinity" bc we haven't found any paths yet 
        for (Vertex<T> vertex : graph.getVertices()) {
            distances.put(vertex, Integer.MAX_VALUE);
        }
        distances.put(start, 0);

        // use a min-heap ordered by smallest distance bc we always want to explore the closest vertex next
        PriorityQueue<VertexDistance<T>> pq = new PriorityQueue<>();
        pq.add(new VertexDistance<>(start, 0));

        // the strat here with D's: keep exploring the closest vertex until we've exhausted all reachable vertices
        while (!pq.isEmpty()) {
            VertexDistance<T> currentPair = pq.remove();
            Vertex<T> current = currentPair.vertex();
            int currentDistance = currentPair.distance();

            // skip outdated entries (for optimization ofc)
            if (currentDistance > distances.get(current)) {
                continue;
            }

            // then explore neighbors and update paths if there better
            for (VertexDistance<T> neighbor : graph.getNeighbors(current)) {
                Vertex<T> next = neighbor.vertex();
                int newDistance = currentDistance + neighbor.distance();

                // we found a better path to next vertex so update the distance and add to the 
                // priority queue for future exploration
                if (newDistance < distances.get(next)) {
                    distances.put(next, newDistance);
                    pq.add(new VertexDistance<>(next, newDistance));
                }
            }
        }

        return distances;
    }

    /**
     * Prim's algorithm for constructing a minimum spanning tree (mst)
     * of a connected graph.
     *
     * This function grows an MST by repeatedly choosing the cheapest edge
     * that connects a visited vertex to an unvisited vertex.
     *
     * @param <T> the data type stored in the vertices
     * @param start the starting vertex for Prim's algorithm
     * @param graph the graph on which to build the MST
     * @return the minimum spanning tree as a set of edges, or null if the graph
     *         is disconnected
     */
    public static <T> Set<Edge<T>> prims(Vertex<T> start, StaticGraph<T> graph) {
        validateStartAndGraph(start, graph);

        Set<Edge<T>> mst = new LinkedHashSet<>(); // this will preserve the insertion order
        Set<Vertex<T>> visited = new HashSet<>(); // this tracks which vertices are already in the growing MST
        
        // and this will store the candidate edges to add (ordered by weight, so well use a pq)
        PriorityQueue<Edge<T>> pq = new PriorityQueue<>(); 

        visited.add(start);
        addOutgoingEdges(start, graph, visited, pq); // seed initial edges

        // this is the main loop where Prim's can happen
        // we will keep adding edges until we've included all vertices in the mst or no edges remain
        while (!pq.isEmpty() && visited.size() < graph.getVertexCount()) {
            Edge<T> edge = pq.remove(); 

            // Determine which vertex is the "next" one to visit (aka the one we haven't seen yet)
            Vertex<T> next = null;
            boolean uVisited = visited.contains(edge.u()); 
            boolean vVisited = visited.contains(edge.v()); 

            // if exactly one vertex is visited, then the other vertex is the next one to add
            if (uVisited && !vVisited) {
                next = edge.v();
            } else if (!uVisited && vVisited) { 
                // if v is visited and u is not, then u is the next vertex to add
                next = edge.u();
            } else { 
                // if both vertices are visited, then this edge would create a cycle so skip it 
                continue; 
            }

            mst.add(edge); 
            visited.add(next); 
            addOutgoingEdges(next, graph, visited, pq);
        }

        // if not all vertices were reached then graph must be disconnected
        if (visited.size() != graph.getVertexCount()) {
            return null;
        }

        return mst;
    }

    /**
     * The helper for Prim's algorithm
     *
     * This function adds all edges leaving a given vertex to the priority queue,
     * but only if they connect to unvisited vertices.
     *
     * @param <T> the data type stored in the vertices
     * @param vertex the vertex whose outgoing edges are being considered
     * @param graph the graph containing the adjacency information
     * @param visited the set of vertices already included in the MST
     * @param pq the priority queue of candidate edges
     */
    private static <T> void addOutgoingEdges(Vertex<T> vertex, StaticGraph<T> graph,
                                             Set<Vertex<T>> visited,
                                             PriorityQueue<Edge<T>> pq) {

        // only consider edges that lead to unvisited vertices to avoid cycles                            
        for (VertexDistance<T> neighbor : graph.getNeighbors(vertex)) {
            Vertex<T> next = neighbor.vertex();

            if (!visited.contains(next)) {
                pq.add(new Edge<>(vertex, next, neighbor.distance())); // candidate edge
            }
        }
    }

    /**
     * Kruskal's algorithm for a minimum spanning tree (mst)
     *
     * Krushkal's algorthim processes edges in increasing weight order and greedily adds
     * an edge when it does not create a cycle
     *
     * @param <T> the data type stored in the vertices
     * @param graph the graph on which to build the MST
     * @return the minimum spanning tree as a set of edges, or null if the graph
     *         is disconnected
     */
    public static <T> Set<Edge<T>> kruskals(StaticGraph<T> graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null.");
        }

        Set<Edge<T>> mst = new LinkedHashSet<>();
        PriorityQueue<Edge<T>> pq = new PriorityQueue<>(graph.getEdges()); 
        
        // I used a Disjoint Set here to track which vertices belong to the same connected component
        // so edges that would create cycles can be skipped
        DisjointSet<Vertex<T>> disjointSet = new DisjointSet<>(); 

        for (Vertex<T> vertex : graph.getVertices()) {
            disjointSet.find(vertex);
        }

        // the strat here with Kruskal's: keep adding the cheapest edge that connects two different 
        // components until we've added enough edges for a spanning tree or we run out of edges
        while (!pq.isEmpty() && mst.size() < graph.getVertexCount() - 1) {
            Edge<T> edge = pq.remove();

            Vertex<T> uRoot = disjointSet.find(edge.u()); 
            Vertex<T> vRoot = disjointSet.find(edge.v()); 

            // only add the edge if it connects two different components
            if (!uRoot.equals(vRoot)) {
                mst.add(edge);
                // merge the components with union
                disjointSet.union(edge.u(), edge.v()); 
            }
        }

        // If we didn’t get enough edges, then graph wasn’t fully connected
        if (mst.size() != graph.getVertexCount() - 1) {
            return null;
        }

        return mst;
    }

    /**
     * A shared validation helper.
     * lowkey not a lot we have to check, but it's still helpful to have a common
     * validation method to avoid code duplication and ensure consistency across algorithms
     *
     * Validates that the start vertex and graph are non-null and that
     * the start vertex exists in the graph.
     * 
     * @param <T> the data type stored in the vertices
     * @param start the starting vertex to validate
     * @param graph the graph to validate against
     */
    private static <T> void validateStartAndGraph(Vertex<T> start,
                                                  StaticGraph<T> graph) {
        // Check for null inputs bc we cant have any of those                                           
        if (start == null || graph == null) {
            throw new IllegalArgumentException("Start vertex and graph cannot be null.");
        }

        // Ensure the start actually exists in the graph, bc if it didn't that'd be bad
        if (!graph.containsVertex(start)) {
            throw new IllegalArgumentException("Start vertex must exist in the graph.");
        }
    }
}