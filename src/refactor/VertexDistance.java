package refactor;

/**
 * Record class to store a vertex in a graph and an integer associated with
 * it representing the distance to this vertex from some other vertex.
 *
 * @apiNote DO NOT MODIFY THIS FILE!
 * @version 2.0
 * @author CS 1332 TAs
 *
 * @param vertex the vertex to be stored
 * @param distance the integer representing the distance to this vertex
 * @param <T> the type of data held within the vertex
 */
public record VertexDistance<T>(Vertex<T> vertex, int distance) implements Comparable<VertexDistance<? super T>> {
    @Override
    public int compareTo(VertexDistance<? super T> o) {
        if (distance == o.distance) {
            return vertex.data().toString().compareTo(o.vertex.data().toString());
        }
        return distance - o.distance;
    }

    @Override
    public String toString() {
        return "Pair with vertex " + vertex + " and distance " + distance;
    }
}
