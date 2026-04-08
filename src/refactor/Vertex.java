package refactor;

/**
 * Record class representing a vertex.
 *
 * @apiNote DO NOT MODIFY THIS FILE!
 * @version 2.0
 * @author CS 1332 TAs
 *
 * @param data the object that is stored in the Vertex
 * @param <T> the type of data held within the vertex
 */
public record Vertex<T>(T data) {
    @Override
    public String toString() {
        return "(" + data + ")";
    }
}
