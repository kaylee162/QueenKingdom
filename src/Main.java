import apply.Building;
import apply.Intersection;
import apply.StaticWaddleWorks;
import refactor.Edge;
import refactor.MutableGraph;
import refactor.Vertex;

import java.util.Set;

/**
 * Entry point for accessing your project 3 files.
 *
 * @author YOUR NAME HERE
 * @version 1.0
 * @userid YOUR USER ID HERE (i.e. gburdell3)
 * @GTID YOUR GT ID HERE (i.e. 900000000)
 * <br>
 * <p>
 * Collaborators: LIST ALL COLLABORATORS YOU WORKED WITH HERE
 * <p>
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 * <p>
 * <br>
 * By typing 'I agree' below, you are agreeing that this is your
 * own work and that you are responsible for the contents of all
 * submitted files. If this is left blank, this project will lose
 * points.
 *<p>
 *<br>
 * Agree Here: REPLACE THIS TEXT
 */
public class Main {

    /**
     * Creates and returns a new instance of your class extending
     * {@link MutableGraph}.
     *
     * @param vertices the vertex set
     * @param edges the edge set
     * @param <T> the type of vertex data in the graph
     * @return a new {@link MutableGraph} instance
     * @throws IllegalArgumentException if any of the arguments are null, or
     * if the vertex set doesn't contain all the vertices.
     */
    public static <T> MutableGraph<T> getMutableGraphInstance(Set<Vertex<T>> vertices,
                                                              Set<Edge<T>> edges) {
        // Replace the line below
        throw new UnsupportedOperationException("Instantiate your class here!");
    }

    /**
     * Creates and returns a new instance of your class extending
     * {@link StaticWaddleWorks}.
     *
     * @param roads the initial graph of the road network
     * @param grid the initial graph of the electrical grid network
     * @return a new {@link StaticWaddleWorks} instance
     */
    public static StaticWaddleWorks getWaddleWorksInstance(MutableGraph<Intersection> roads,
                                                           MutableGraph<Building> grid) {
        // Replace the line below
        throw new UnsupportedOperationException("Instantiate your class here!");
    }
}