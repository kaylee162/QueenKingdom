package refactor;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An interface representing an undirected graph, with a vertex set,
 * an edge set, and an adjacency list.
 *
 * @apiNote DO NOT MODIFY THIS FILE!
 * @version 1.0
 * @author CS 1332 TAs
 *
 * @param <T> the type of data held within the vertices
 */
public interface StaticGraph<T> {
    /**
     * Gets the vertex set of this graph.
     *
     * @return the vertex set of this graph
     * @implSpec the returned set must not allow modifications to the underlying graph.
     */
    Set<Vertex<T>> getVertices();

    /**
     * Gets the edge set of this graph.
     *
     * @return the edge set of this graph
     * @implSpec the returned set must not allow modifications to the underlying graph.
     */
    Set<Edge<T>> getEdges();

    /**
     * Gets the adjacency list of this graph.
     *
     * @return the adjacency list of this graph
     */
    Map<Vertex<T>, List<VertexDistance<T>>> getAdjList();

    /**
     * Gets all adjacent vertices of and their distances from a given vertex.
     * See PDF for exception handling.
     *
     * @param vertex the vertex to get neighbors of
     * @return a list of all neighboring vertices and their distances
     */
    List<VertexDistance<T>> getNeighbors(Vertex<T> vertex);

    /**
     * Gets the number of vertices in the graph.
     *
     * @return the number of vertices in the graph
     */
    int getVertexCount();

    /**
     * Gets the number of edges in the graph.
     *
     * @return the number of edges in the graph
     */
    int getEdgeCount();

    /**
     * Gets whether the vertex is in the graph.
     *
     * @param vertex the vertex to check
     * @return whether the vertex is in the graph
     */
    boolean containsVertex(Vertex<T> vertex);

    /**
     * Gets whether the edge is in the graph.
     *
     * @param edge the edge to check
     * @return whether the edge is in the graph
     */
    boolean containsEdge(Edge<T> edge);
}
