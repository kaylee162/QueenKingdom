package refactor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * An abstract class implementing part of {@link StaticGraph}, allowing
 * for mutating methods to occur.
 *
 * @apiNote DO NOT MODIFY THIS FILE!
 * @version 1.0
 * @author CS 1332 TAs
 *
 * @param <T> the type of data held within the vertices
 */
public abstract class MutableGraph<T> implements StaticGraph<T> {
    protected final Set<Vertex<T>> vertices;
    protected final Set<Edge<T>> edges;

    /**
     * Constructor for a MutableGraph
     * Takes in a set of vertices and edges and initializes the graph.
     */
    public MutableGraph(Set<Vertex<T>> vertices, Set<Edge<T>> edges) {
        if (vertices == null || edges == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        this.vertices = new HashSet<>(vertices);
        this.edges = new HashSet<>(edges);
    }

    @Override
    public Set<Vertex<T>> getVertices() {
        return Collections.unmodifiableSet(vertices);
    }

    @Override
    public Set<Edge<T>> getEdges() {
        return Collections.unmodifiableSet(edges);
    }

    @Override
    public int getVertexCount() {
        return vertices.size();
    }

    @Override
    public int getEdgeCount() {
        return edges.size();
    }

    @Override
    public boolean containsVertex(Vertex<T> vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("Vertex cannot be null");
        }
        return vertices.contains(vertex);
    }

    @Override
    public boolean containsEdge(Edge<T> edge) {
        if (edge == null) {
            throw new IllegalArgumentException("Edge cannot be null");
        }
        return edges.contains(edge);
    }

    /**
     * Adds a vertex to the graph.
     * See PDF for exception handling.
     *
     * @param vertex the vertex to add
     */
    public abstract void addVertex(Vertex<T> vertex);

    /**
     * Removes a vertex from the graph. It should also remove all adjacent edges.
     * See PDF for exception handling.
     *
     * @param vertex the vertex to remove
     */
    public abstract void removeVertex(Vertex<T> vertex);

    /**
     * Adds an edge to the graph. It should also add the vertices if needed.
     * See PDF for exception handling.
     * 
     * @param edge the edge to add
     */
    public abstract void addEdge(Edge<T> edge);

    /**
     * Removes an edge from the graph.
     * See PDF for exception handling.
     * 
     * @param edge the edge to remove
     */
    public abstract void removeEdge(Edge<T> edge);

}
