package refactor;

/**
 * Record class representing an undirected edge between two vertices.
 *
 * @apiNote DO NOT MODIFY THIS FILE!
 * @version 2.0
 * @author CS 1332 TAs
 *
 * @param u the start vertex of the edge
 * @param v the end vertex of the edge
 * @param weight the weight value of the edge
 * @param <T> the type of data held within the vertices
 */
public record Edge<T>(Vertex<T> u, Vertex<T> v, int weight) implements Comparable<Edge<? super T>> {
    @Override
    public boolean equals(Object obj) {
        return ((obj instanceof Edge<?> e) && ((u.equals(e.u) && v.equals(e.v)) || (u.equals(e.v) && v.equals(e.u))));
    }

    @Override
    public int hashCode() {
        return u.hashCode() + v.hashCode();
    }

    @Override
    public int compareTo(Edge<? super T> e) {
        return weight - e.weight();
    }

    @Override
    public String toString() {
        return "Edge between " + u + " and " + v + " with weight " + weight;
    }
}
