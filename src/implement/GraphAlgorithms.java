package implement;

import refactor.Edge;
import refactor.StaticGraph;
import refactor.Vertex;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Your implementation of various different graph algorithms.
 */
public class GraphAlgorithms {
    /**
     * Performs a breadth-first search (BFS) on the input graph, starting at
     * the parameterized starting vertex.
     * <p>
     * When exploring a vertex, explore in the order of neighbors returned by
     * the adjacency list. Failure to do so may cause you to lose points.
     *
     * @param <T>   the generic typing of the data
     * @param start the vertex to begin the BFS on
     * @param graph the graph to search through
     * @return list of vertices in visited order
     * @throws IllegalArgumentException if any input is null, or if start
     *                                  doesn't exist in the graph
     */
    public static <T> List<Vertex<T>> bfs(Vertex<T> start, StaticGraph<T> graph) {
        // Remove this line when you implement the method
        throw new UnsupportedOperationException("Unimplemented");
    }

    /**
     * Performs a depth-first search (DFS) on the input graph, starting at
     * the parameterized starting vertex.
     * <p>
     * When exploring a vertex, explore in the order of neighbors returned by
     * the adjacency list. Failure to do so may cause you to lose points.
     *
     * @param <T>   the generic typing of the data
     * @param start the vertex to begin the DFS on
     * @param graph the graph to search through
     * @return list of vertices in visited order
     * @throws IllegalArgumentException if any input is null, or if start
     *                                  doesn't exist in the graph
     * @implSpec You MUST implement this method recursively, or else you will
     * lose all points for this method.
     */
    public static <T> List<Vertex<T>> dfs(Vertex<T> start, StaticGraph<T> graph) {
        // Remove this line when you implement the method
        throw new UnsupportedOperationException("Unimplemented");
    }

    /**
     * Recursive helper method for DFS.
     * 
     * @param <T>   the generic typing of the data
     * @param curr  the current vertex being explored
     * @param g     the graph being searched through
     * @param vSet  the set of vertices that have already been visited
     * @param list  the list of vertices in visited order
     */
    private static <T> void dfs(Vertex<T> curr, StaticGraph<T> g, Set<Vertex<T>> vSet, List<Vertex<T>> list) {
        // Remove this line when you implement the method
        throw new UnsupportedOperationException("Unimplemented");
    }

    /**
     * Finds the single-source shortest distance between the start vertex and
     * all vertices given a weighted graph (you may assume non-negative edge
     * weights).
     * <p>
     * Return a map of the shortest distances such that the key of each entry
     * is a node in the graph and the value for the key is the shortest distance
     * to that node from start, or {@link Integer#MAX_VALUE} (representing
     * infinity) if no path exists.
     *
     * @param <T>   the generic typing of the data
     * @param start the vertex to begin the Dijkstra's on (source)
     * @param graph the graph we are applying Dijkstra's to
     * @return a map of the shortest distances from start to every
     * other node in the graph
     * @throws IllegalArgumentException if any input is null, or if start
     *                                  doesn't exist in the graph.
     */
    public static <T> Map<Vertex<T>, Integer> dijkstras(Vertex<T> start,
                                                        StaticGraph<T> graph) {
        // Remove this line when you implement the method
        throw new UnsupportedOperationException("Unimplemented");
    }

    /**
     * Runs Prim's algorithm on the given graph and returns the Minimum
     * Spanning Tree (MST) in the form of a set of Edges. If the graph is
     * disconnected and therefore no valid MST exists, return null.
     * <p>
     * You may assume that the passed in graph is undirected.
     * <p>
     * @param <T> the generic typing of the data
     * @param start the vertex to begin Prims on
     * @param graph the graph we are applying Prims to
     * @return the MST of the graph or null if there is no valid MST
     * @throws IllegalArgumentException if any input is null, or if start
     *                                  doesn't exist in the graph.
     */
    public static <T> Set<Edge<T>> prims(Vertex<T> start, StaticGraph<T> graph) {
        // Remove this line when you implement the method
        throw new UnsupportedOperationException("Unimplemented");
    }

    /**
     * Runs Kruskal's algorithm on the given graph and returns the Minimal
     * Spanning Tree (MST) in the form of a set of Edges. If the graph is
     * disconnected and therefore no valid MST exists, return null.
     * <p>
     * You may assume that the passed in graph is undirected.
     * <p>
     * @param <T>   the generic typing of the data
     * @param graph the graph we are applying Kruskals to
     * @return the MST of the graph or null if there is no valid MST
     * @throws IllegalArgumentException if any input is null
     */
    public static <T> Set<Edge<T>> kruskals(StaticGraph<T> graph) {
        // Remove this line when you implement the method
        throw new UnsupportedOperationException("Unimplemented");
    }

}
