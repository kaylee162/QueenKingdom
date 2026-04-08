package apply;

import refactor.StaticGraph;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface for your custom {@code WaddleWorks} implementation.
 * <p>
 * This API will function as a city planning app, performing
 * simulations of accessible road networks and an electrical grid.
 * More details are provided in the PDF.
 *
 * @apiNote DO NOT MODIFY THIS FILE!
 * @implNote {@code B} refers to the number of Buildings, {@code W}
 * refers to the number of wire segments, {@code I} refers to the
 * number of Intersections, and {@code R} refers to the number of
 * roads.
 * @version 1.0
 * @author CS 1332 TAs
 */
public interface StaticWaddleWorks {

    /**
     * Adds a road to the road network.
     * <p>
     * If the added road connects two neighborhoods,
     * then return a true. Otherwise, return false.
     *
     * @param a the Intersection at one end of the road
     * @param b the Intersection at the other end of the road
     * @param duration the time it takes to traverse the road
     * @return whether the edge connects two neighborhoods
     * @implSpec {@code O(1)} runtime
     */
    boolean addRoad(Intersection a, Intersection b, int duration);

    /**
     * Adds a wire to the electrical grid.
     *
     * @param a the Building at one end of the wire
     * @param b the Building at the other end of the wire
     * @param length the length of the wire
     * @implSpec {@code O(1)} runtime
     */
    void addWire(Building a, Building b, int length);

    /**
     * Gets the number of neighborhoods in the road network.
     * <p>
     * A group of Intersections is considered to be in the same
     * neighborhood if each Intersection can reach every other
     * Intersection.
     *
     * @return the number of neighborhoods
     * @implSpec {@code O(1)} runtime
     */
    int getNeighborhoodCount();

    /**
     * Gets a view of certain Intersections organized by neighborhood,
     * specifically Intersections with a number of adjacent roads in an 
     * inclusive range [i, j].
     * <p>
     * This should return a map where the keys are the neighborhoods'
     * representative Intersections, and the values are a set of all
     * Intersections in the same neighborhood as the representative
     * Intersection that have a number of adjacent roads in the range.
     *
     * @param i the lower bound of the number adjacent of roads to consider
     * @param j the upper bound of the number adjacent of roads to consider
     * @return a curated map of the road network organized by neighborhoods
     * @implSpec
     * <p> {@code O(|R|+|I|)} runtime
     */
    Map<Intersection, Set<Intersection>> getNeighborhoodsByConnections(int i, int j);

    /**
     * Gets the minimum number of roads to travel between two Buildings.
     * If no path exists between the two Buildings, return -1.
     *
     * @param from the start Building
     * @param to the end Building
     * @return the minimum number of roads between the Buildings
     * @implSpec {@code O(|I|+|R|)} runtime
     */
    int getMinBetween(Building from, Building to);

    /**
     * Calculates the shortest duration path between two Buildings that
     * avoids certain types of Intersections. Prioritize avoiding the
     * undesirable Intersections, even if that means taking a longer path.
     * <p>
     * See {@link Intersection.Type} for details on what can be
     * constituted an avoided type of Intersection. If a path avoiding
     * these types is not possible, the path should include the minimum
     * number of avoided types.
     * <p>
     * The route should be constructed from source to destination,
     * including all Intersections along the way.
     *
     * @param from the start Building
     * @param to the end Building
     * @param avoid the set of Intersection types to avoid when possible
     * @return the list of Intersections to travel to, in order
     * @implSpec
     * <li> {@code O(|R|log(|R|))} runtime
     * <li> <b>No space complexity requirement</b>
     */
    List<Intersection> calculateRoute(Building from, Building to, Set<Intersection.Type> avoid);

    /**
     * Given a list of {@code k} candidate Buildings, select the one
     * that would be most "central" to the grid as a power site.
     * <p>
     * The most central power site is one that minimizes the average
     * distance to any arbitrary Building in the electrical grid graph.
     *
     * @param candidates the list of Building candidates
     * @return the best candidate to be a power site
     * @implSpec {@code O(k(|W|log(|W|)))} runtime
     */
    Building getBestPowerSite(List<Building> candidates);

    /**
     * Consolidates the electrical grid to the minimum total length of
     * wire in the grid by removing all unnecessary wiring.
     *
     * @return the fraction of wire that was removed
     * @implSpec {@code O(|W|log(|W|))} runtime
     */
    double consolidateGrid();

    /**
     * Gets the current state of the road network.
     *
     * @return the current state of the road network
     */
    StaticGraph<Intersection> getRoads();

    /**
     * Gets the current state of the electrical grid.
     *
     * @return the current state of the electrical grid
     */
    StaticGraph<Building> getGrid();
}
