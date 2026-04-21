package refactor;
// she is a princess and she don't need no prince

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * High level overview:
 * This is a concrete mutable graph implementation that is backed by an adj list.
 *
 * This graph is:
 *     Simple: meaning it has no self-loops, and at most one edge between a pair of vertices
 *     Weighted: menaing that each edge stores an integer weight
 *     Undirected: meaning that every edge appears in both endpoint neighbor lists
 *
 * The inherited vertices and edges sets come from MutableGraph.
 * And this class adds the adj list representation
 *
 * @param <T> the data type stored inside each vertex
 */
public class Princess<T> extends MutableGraph<T> {

    /**
     * This is the adj-list representation of the graph
     *
     * Each vertex maps to a list of nieghboring vertices paired with the
     * weight of the edge connecting them.
     *
     * Every vertex in the graph must appear as a key in this map, even if
     * it has no edges and therefore has an empty neighbor list.
     */
    private final Map<Vertex<T>, List<VertexDistance<T>>> adjList;

    /**
     * This constructs a new mutable graph from the given vertex and edge sets.
     *      
     * After validation, the constructor builds the ajd list so that
     * every vertex is present as a key and every undirected edge is inserted
     * into both endpoint neighbor lists
     * 
     * the runtime of this constructor is O(V + E), bc we have to iterate through every vertex to add it 
     * to the adj list and then we have to iterate through every edge to add it to the adj list as well
     *
     * @param vertices the initial set of vertices
     * @param edges the initial set of edges
     * @throws IllegalArgumentException if the sets are null, contain null
     * vertices/edges, contain self-loops, or if an edge references a vertex
     * not contained in the vertex set
     */
    public Princess(Set<Vertex<T>> vertices, Set<Edge<T>> edges) {
        super(vertices, edges);

        // i used a hash map here bc it has O(1) average-case lookup time for getNeighbors and add/remove edge operations
        adjList = new HashMap<>(); 

        // validate the vertex first before we start building the adj list
        for (Vertex<T> vertex : this.vertices) {
            if (vertex == null) {
                throw new IllegalArgumentException("Vertex set cannot contain null");
            }
            adjList.put(vertex, new ArrayList<>()); // put it in the adj list 
        }

        // then validate the edges and build the adj list at the same time
        for (Edge<T> edge : this.edges) {
            validateEdgeForConstructor(edge);

            // bc the graph is undirected, each edge must appear twice (u2v & v2u)
            addNeighbor(edge.u(), edge.v(), edge.weight());
            addNeighbor(edge.v(), edge.u(), edge.weight());
        }
    }

    /**
     * This returns the ajd list of the graph.
     *
     * This is an O(V + E) op since we have to make a deep copy of the entire adj list
     * @return an unmodifiable view of the adj list
     */
    @Override
    public Map<Vertex<T>, List<VertexDistance<T>>> getAdjList() {
        Map<Vertex<T>, List<VertexDistance<T>>> copy = new HashMap<>();

        // make a copy of the adj list so that external callers can't modify it 
        for (Map.Entry<Vertex<T>, List<VertexDistance<T>>> entry : adjList.entrySet()) {
            copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }

        return copy;
    }

    /**
     * this returnrs the neighbors of a given vertex 
     *
     * This is an O(1) lookup in the adj map bc we can directly access the 
     * vertex's neighbor list without iterating through the graph :)
     *
     * @param vertex the vertex whose neighbors should be returned
     * @return an unmodifiable view of that vertex's neighbor list
     * @throws IllegalArgumentException if the vertex is null or not in the graph
     */
    @Override
    public List<VertexDistance<T>> getNeighbors(Vertex<T> vertex) {
        if (vertex == null || !vertices.contains(vertex)) {
            throw new IllegalArgumentException("Vertex must be non-null and exist in the graph");  
        }

        return Collections.unmodifiableList(adjList.get(vertex));
    }

    /**
     * this adds a vertex to our graph
     *
     * The new vertex begins with no incident edges, so it receives an empty
     * neighbor list in the adj map.
     *
     * @param vertex the vertex to add
     * @throws IllegalArgumentException if the vertex is null or already exists
     */
    @Override
    public void addVertex(Vertex<T> vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("Vertex cannot be null");
        }
        if (vertices.contains(vertex)) {
            throw new IllegalArgumentException("Vertex already exists in the graph");
        }

        vertices.add(vertex); 
        adjList.put(vertex, new ArrayList<>()); 
    }

    /**
     * This rmeoves a vertex from the graph.
     *
     * Removing a vertex also removes every edge inicident to that vertex.
     * That means we gotta:
     *     1. remove the vertex from the vertex set
     *     2. remove all matching edges from the edge set
     *     3. remove the vertex from every neighbor list in the adj map
     *     4. remove the vertex's own ajd list entry
     *     5. remove the vertex from the graph's vertex set
     *
     * @param vertex the vertex to remove
     * @throws IllegalArgumentException if the vertex is null or not in the graph
     */
    @Override
    public void removeVertex(Vertex<T> vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("Vertex cannot be null");
        }
        if (!vertices.contains(vertex)) {
            throw new IllegalArgumentException("Vertex does not exist in the graph");
        }

        // make a temp list to avoid modifying the edge set while iterating over it, which would be bad
        List<Edge<T>> edgesToRemove = new ArrayList<>();

        // 1. we have to check every edge to see if it's incident to the vertex we want to remove, 
        // which is O(E)
        for (Edge<T> edge : edges) {
            if (edge.u().equals(vertex) || edge.v().equals(vertex)) {
                edgesToRemove.add(edge);
            }
        }

        // 2. for each edge we found that was incident to the vertex, remove it from the edge set
        for (Edge<T> edge : edgesToRemove) {
            edges.remove(edge);
        }

        // 3.Remove the vertex from every other vertex's neighbor list.
        for (Vertex<T> other : vertices) {
            if (!other.equals(vertex)) {
                removeNeighbor(other, vertex); 
            }
        }

        // 4. Remove the vertex's own adj list entry.
        adjList.remove(vertex);

        // 5. Finally remove the vertex from the graph's vertex set.
        vertices.remove(vertex);
    }

    /**
     * this adds an undirected edge to the graph
     *
     * If either endpoint vertex does not already exist in the graph,
     * it is added automatically.
     * Because the graph is undirected, the edge is stored once in the
     * edge set but represented twice in the adj list (u2v & v2u)
     *
     * @param edge the edge to add
     * @throws IllegalArgumentException if the edge is null, already exists,
     * or is a self-loop
     */
    @Override
    public void addEdge(Edge<T> edge) {
        validateEdgeForMutation(edge); 

        if (edges.contains(edge)) {
            throw new IllegalArgumentException("Edge already exists in the graph");
        }

        if (!vertices.contains(edge.u())) {
            addVertex(edge.u());
        }
        if (!vertices.contains(edge.v())) {
            addVertex(edge.v());
        }

        edges.add(edge);

        addNeighbor(edge.u(), edge.v(), edge.weight());
        addNeighbor(edge.v(), edge.u(), edge.weight());
    }

    /**
     * this remves an edge from the grahp.
     *
     * bc the graph is undirected, the edge must be removed from both
     * endpoint neighbor lists as well as from the graph's edge set
     *
     * @param edge the edge to remove
     * @throws IllegalArgumentException if the edge is null or not in the graph
     */
    @Override
    public void removeEdge(Edge<T> edge) {
        if (edge == null) {
            throw new IllegalArgumentException("Edge cannot be null");
        }
        if (!edges.contains(edge)) {
            throw new IllegalArgumentException("Edge does not exist in the graph");
        }

        edges.remove(edge);
        removeNeighbor(edge.u(), edge.v());
        removeNeighbor(edge.v(), edge.u());
    }

    /**
     * this validates an edge while the constructor is building the initial graph.
     * lowkgurtgeninely there's a lot to check for the edge, so just make a sep function for that
     *
     * @param edge the edge to validate
     * @throws IllegalArgumentException if the edge is null, has a null endpoint,
     * references a vertex not in the graph, or is a self-loop
     */
    private void validateEdgeForConstructor(Edge<T> edge) {
        if (edge == null) {
            throw new IllegalArgumentException("Edge set cannot contain null");
        }
        if (edge.u() == null || edge.v() == null) {
            throw new IllegalArgumentException("Edge endpoints cannot be null");
        }
        if (edge.u().equals(edge.v())) {
            throw new IllegalArgumentException("Self-loops are not allowed");
        }
        if (!vertices.contains(edge.u()) || !vertices.contains(edge.v())) {
            throw new IllegalArgumentException("Every edge endpoint must be in the vertex set");
        }
    }

    /**
     * Validates an edge for add/remove mutation operations.
     * again, theres lowkey a lot to check for the edge, so just make a sep function for that too
     *
     * @param edge the edge to validate
     * @throws IllegalArgumentException if the edge is null, has a null endpoint,
     * or is a self-loop
     */
    private void validateEdgeForMutation(Edge<T> edge) {
        if (edge == null) {
            throw new IllegalArgumentException("Edge cannot be null");
        }
        if (edge.u() == null || edge.v() == null) {
            throw new IllegalArgumentException("Edge endpoints cannot be null");
        }
        if (edge.u().equals(edge.v())) {
            throw new IllegalArgumentException("Self-loops are not allowed");
        }
    }

    /**
     * this adds one neighbor entry to the ajd list.
     *
     * This method only inserts one direction (for undirected behavior,
     * callers must invoke it for both endpoint directions)
     *
     * @param from the source vertex whose neighbor list will be updated
     * @param to the neighboring vertex to add
     * @param weight the edge weight to store
     */
    private void addNeighbor(Vertex<T> from, Vertex<T> to, int weight) {
        adjList.get(from).add(new VertexDistance<>(to, weight));
    }

    /**
     * this removes a single neighbor entry from the adj list.
     *
     * This removes the to vertex from the from vertex's
     * neighbor list, regardless of the stored edge weight
     *
     * @param from the vertex whose neighbor list should be edited
     * @param to the neighbor vertex to remove
     */
    private void removeNeighbor(Vertex<T> from, Vertex<T> to) {
        List<VertexDistance<T>> neighbors = adjList.get(from);
        neighbors.removeIf(vertexDistance -> vertexDistance.vertex().equals(to));
    }
}
// periodddddd
// and thats the way it is