package apply;
// she is a queen and this is her kingdom and she will slay the day away 

import implement.GraphAlgorithms;
import refactor.Edge;
import refactor.MutableGraph;
import refactor.StaticGraph;
import refactor.Vertex;
import refactor.VertexDistance;
import refactor.DisjointSet;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import refactor.Princess;

/**
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
 * <p>
 * <br>
 * Agree Here: I agree
 *
 * ------------------------------------------------
 *
 * High-level overview:
 * This class acts kinda like the main "city manager" 
 * It keeps track of two different graph systems at once:
 *
 * 1. Roads graph
 *    - vertices are intersections
 *    - used for neighborhoods, travel, and routing
 *
 * 2. Grid graph
 *    - vertices are buildings
 *    - used for wire placement, power-site analysis, and mst consloidatoin
 *
 * The road graph uese a disjoint set to track connected components / neighborhoods so we can efficiently 
 * update and query neighborhood membership as roads are added
 */
public class QueenKingdom implements StaticWaddleWorks {

    private final MutableGraph<Intersection> roads; // this is the road network between intersections
    private final MutableGraph<Building> grid;      // this is the electrical grid between buildings
    private final DisjointSet<Intersection> roadNeighborhoods; // this tracks connected road components

    /**
     * This function creates a new Waddleworks implemention
     * The constructr sets up the two graphs, validates that the initial data
     * is reasonable, and preloads the disjoint set so that the road neighborhoods
     * already match the starting road graph.
     *
     * @param roads the initial road graph
     * @param grid the initial electrical grid graph
     */
    public QueenKingdom(MutableGraph<Intersection> roads,
                 MutableGraph<Building> grid) {
        if (roads == null || grid == null) {
            throw new IllegalArgumentException("Graphs cannot be null");
        }

        // make defensive copies so outside code cannot mutate
        this.roads = new Princess<>(roads.getVertices(), roads.getEdges());
        this.grid = new Princess<>(grid.getVertices(), grid.getEdges());
        
        // i used a disjoint set here to track the road neighborhoods because it has very efficient 
        // union and find operations, which are exactly what we need to keep the neighborhood information 
        // up to date as roads are added and to quickly determine which neighborhood an intersection belongs
        //  to when grouping them by connections.
        this.roadNeighborhoods = new DisjointSet<>(); 

        validateInitialGridBuildings();

        if (!isConnected(this.grid)) {
            throw new IllegalArgumentException("Initial grid must be fully connected");
        }

        // for each vertex in the roads graph, add its data to the disjoint set
        for (Vertex<Intersection> vertex : this.roads.getVertices()) {
            roadNeighborhoods.find(vertex.data());
        }

        // for each edge in the roads graph, union the two endpoints in the disjoint set 
        // so the neighborhoods are correct from the start
        for (Edge<Intersection> edge : this.roads.getEdges()) {
            roadNeighborhoods.union(edge.u().data(), edge.v().data());
        }
    }
    /**
     * This function adds a new road to the kingdom and returns whether it merged 
     * two separate neighborhoods
     * This is done in O(1) time by checking the disjoint set parents of the two endpoints 
     * before we union them
     *
     * @param a the first intersection
     * @param b the second intersection
     * @param duration the time it takes to travel this road
     * @return true if adding this road merges two separate neighborhoods, false otherwise
     */

    @Override
    public boolean addRoad(Intersection a, Intersection b, int duration) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Intersections cannot be null");
        }
        if (duration < 0) {
            throw new IllegalArgumentException("Road duration cannot be negative");
        }
        if (a.equals(b)) {
            throw new IllegalArgumentException("Roads cannot be self-loops");
        }

        // wrap the raw data in Vertex objects so they can be used in the graph
        Vertex<Intersection> vertexA = new Vertex<>(a);
        Vertex<Intersection> vertexB = new Vertex<>(b);

        // dup roads should return false
        if (roads.containsEdge(new Edge<>(vertexA, vertexB, duration))) {
            return false;
        }

        // check if these vertices already exist in the graph so we can determine the return value 
        // before we modify any state
        boolean aAlreadyExists = roads.containsVertex(vertexA);
        boolean bAlreadyExists = roads.containsVertex(vertexB);

        boolean mergedNeighborhoods = false;

        if (aAlreadyExists && bAlreadyExists) {
            Intersection rootA = roadNeighborhoods.find(a);
            Intersection rootB = roadNeighborhoods.find(b);
            mergedNeighborhoods = !rootA.equals(rootB);
        }

        // now that the return value has been decided, actually add the road :)
        roads.addEdge(new Edge<>(vertexA, vertexB, duration));

        // keep the disjoint set in sync with the road graph
        roadNeighborhoods.union(a, b);

        return mergedNeighborhoods;
    }

    /**
     * This function adds a new wire to the grid and validates that the operation 
     * doesn't violate any constraints.
     * This is done in O(1) time since we can directly check the existence of the buildings 
     * and their closest intersections in the graph
     * 
     * @param a the first intersection
     * @param b the second intersection
     * @param length the length of the wire being added
     * @return the set of root intersections

     */
    @Override
    public void addWire(Building a, Building b, int length) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Buildings cannot be null");
        }
        if (length < 0) {
            throw new IllegalArgumentException("Wire length cannot be negative");
        }

        validateBuildingClosestIntersection(a);
        validateBuildingClosestIntersection(b);

        // no self loops
        if (a.equals(b)) {
            throw new IllegalArgumentException("Wires cannot be self-loops");
        }

        // check if the buildings already exist in the grid. rf not, we'll add them as new vertices
        Vertex<Building> vertexA = new Vertex<>(a);
        Vertex<Building> vertexB = new Vertex<>(b);
        boolean hasA = grid.getVertices().contains(vertexA);
        boolean hasB = grid.getVertices().contains(vertexB);

        // If both buildings are brand new and the grid already has vertices,
        // this single wire would create a separate disconnected component, which is bad
        if (grid.getVertexCount() > 0 && !hasA && !hasB) {
            throw new IllegalArgumentException("Operation would disconnect the grid");
        }

        // finally add the edge to the grid graph
        grid.addEdge(new Edge<>(vertexA, vertexB, length));
    }

    /**
     * This function returns the number of road neighborhoods in the kingdom, which is just the number 
     * of distinct roots in the disjoint set since each root represents a separate connected component of the road graph
     * 
     * This is an O(1) operation since we can directly access the size of the
     * 
     * @return the number of road neighborhoods in the kingdom
     */
    @Override
    public int getNeighborhoodCount() {
        return roadNeighborhoods.getRoots().size();
    }

    /**
     * This function returns a map of all neighborhoods and their members that have a number of connections within the given bounds
     * It uses the disjoint set to quickly determine which neighborhood each intersection belongs to, 
     * and it iterates through all the vertices once to group them 
     * 
     * This whole thing is a O(V + E) since we have to check every vertex and edge at least once to build the neighborhood groups.
     */
    @Override
    public Map<Intersection, Set<Intersection>> getNeighborhoodsByConnections(int i, int j) {
        if (i < 0 || j < 0 || i > j) {
            throw new IllegalArgumentException("Bounds must be non-negative and ordered");
        }

        // i used a hashmap here because it has O(1) lookup time for grouping intersections by their neighborhood roots
        Map<Intersection, Set<Intersection>> grouped = new HashMap<>();

        // include every neighborhood up front, even if its filtered set ends up empty.
        for (Intersection root : roadNeighborhoods.getRoots()) {
            grouped.put(root, new HashSet<>());
        }

        // for each vertex in the roads graph, check its degree and add it to the correct neighborhood group 
        // if it's within the bounds
        for (Vertex<Intersection> vertex : roads.getVertices()) {
            int degree = roads.getNeighbors(vertex).size();

            if (degree >= i && degree <= j) {
                Intersection root = roadNeighborhoods.find(vertex.data());
                grouped.get(root).add(vertex.data());
            }
        }

        return grouped;
    }

    /**
     * this function calculates the min numebr of roads that must be traveled to get from one building to another 
     * by running a BFS on the road graph starting from the closest intersection of the source building and ending 
     * at the closest intersection of the target building.
     * 
     * i used BFS here because it finds the shortest path in terms of number of edges (roads) in an unweighted graph, 
     * which is exactly what we need for this problem. 
     * D's would be overkill since we don't have weighted edges for this particular query.
     * 
     * @param from the starting building
     * @param to the destination building
     * @return the minimum number of roads that must be traveled to get from the source building to the target building
     * or -1 if no route exists
     */
    @Override
    public int getMinBetween(Building from, Building to) {
        validateGridBuildingForTraversal(from);
        validateGridBuildingForTraversal(to);

        Vertex<Intersection> start = new Vertex<>(from.closest());
        Vertex<Intersection> target = new Vertex<>(to.closest());

        Queue<Vertex<Intersection>> queue = new ArrayDeque<>();
        
        // i made a hashmap here to track the distance (in terms of number of roads) from the start vertex 
        // to each vertex we explore during BFS. This allows us to return the correct distance as soon as 
        // we reach the target vertex.
        Map<Vertex<Intersection>, Integer> distanceByRoadCount = new HashMap<>();
       
        queue.add(start); 
        distanceByRoadCount.put(start, 0); // zero roads taken at the beginning

        // shoudl just call BFS here instead of reinventing it bc this is pretty much the standard BFS template with an added distance map to track how many roads we've taken to reach each vertex. We return as soon as we reach the target vertex, which guarantees it's the shortest path in terms of road count.
        while (!queue.isEmpty()) {
            Vertex<Intersection> current = queue.remove();

            if (current.equals(target)) {
                return distanceByRoadCount.get(current);
            }

            for (VertexDistance<Intersection> neighbor : roads.getNeighbors(current)) {
                Vertex<Intersection> next = neighbor.vertex();

                if (!distanceByRoadCount.containsKey(next)) {
                    distanceByRoadCount.put(next, distanceByRoadCount.get(current) + 1);
                    queue.add(next); // so we add it to the queue to explore its neighbors later
                }
            }
        }

        return -1;
    }

    /**
     * This function calculates the shortest route between two buildings, avoiding specified intersection types
     * 
     * The runtime of this function is O(V log V) 
     * 
     * @param from the starting building
     * @param to the destination building
     * @param avoid the set of intersection types to avoid
     * @return a list of intersections representing the shortest route, or an empty list if no route exists
     */
    @Override
    public List<Intersection> calculateRoute(Building from, Building to,
                                             Set<Intersection.Type> avoid) {
        if (avoid == null) {
            throw new IllegalArgumentException("Avoid set cannot be null");
        }

        validateGridBuildingForTraversal(from);
        validateGridBuildingForTraversal(to);

        // make vertices for the closest intersections of the two buildings so we can run a 
        // modified D's on the roads graph
        Vertex<Intersection> start = new Vertex<>(from.closest());
        Vertex<Intersection> target = new Vertex<>(to.closest());

        // i made a hashmap here bc it allows us to efficiently update and compare routes as we explore the graph
        Map<Vertex<Intersection>, Integer> notGood = new HashMap<>();
        Map<Vertex<Intersection>, Integer> duration = new HashMap<>();
        Map<Vertex<Intersection>, Vertex<Intersection>> previous = new HashMap<>();
        
        // and this hashset tracks which vertices have been finalized with optimal routes so we can skip stale queue entries
        Set<Vertex<Intersection>> finalized = new HashSet<>(); 

        // Start by treating every vertex as unreachable / infinitely costly.
        for (Vertex<Intersection> vertex : roads.getVertices()) {
            notGood.put(vertex, Integer.MAX_VALUE);
            duration.put(vertex, Integer.MAX_VALUE);
        }

        notGood.put(start, 0);
        duration.put(start, 0);

        // Priority order:
        //   - fewer avoided/bad intersections first,
        //   - then lower total travel time,
        //   - then string order as a stable final tiebreaker.
        Comparator<RouteState> routeOrder = Comparator.comparingInt(RouteState::badSeen)
                .thenComparingInt(RouteState::totalDuration)
                .thenComparing(state -> state.vertex().data().toString());

        PriorityQueue<RouteState> pq = new PriorityQueue<>(routeOrder);

        // add a RouteState to the pq every time we find a better path to a vertex, 
        pq.add(new RouteState(start, 0, 0));

        // should prob implement D's here since we have two cost metrics (bad intersections and duration) 
        // and we want to find the optimal route based on those. The priority queue will help us always expand the most promising route next, and the notGood and duration maps will help us keep track of the best known cost to reach each vertex so we can skip stale routes in the pq that are no longer optimal.
        while (!pq.isEmpty()) {
            RouteState state = pq.remove();
            Vertex<Intersection> current = state.vertex();

            boolean wrongBadCount = state.badSeen() != notGood.get(current);
            boolean wrongDuration = state.totalDuration() != duration.get(current);

            if (wrongBadCount || wrongDuration) {
                continue;
            }

            // Once finalized, this vertex has already been processed optimally.
            if (!finalized.add(current)) {
                continue;
            }

            if (current.equals(target)) {
                break; // best route to target has been found
            }

            // Explore neighbors and update their best-known cost if this path is better
            for (VertexDistance<Intersection> neighbor : roads.getNeighbors(current)) {
                Vertex<Intersection> next = neighbor.vertex();

                int nextBadCount = state.badSeen()
                    + (avoid.contains(next.data().type()) ? 1 : 0);
                int nextDuration = state.totalDuration() + neighbor.distance();

                int currentBestBad = notGood.get(next);
                int currentBestDuration = duration.get(next);

                boolean betterPath = false;

                // path is better if it has fewer bad intersections, or the same number of bad 
                // intersections but shorter duration
                if (nextBadCount < currentBestBad) {
                    betterPath = true;
                } else if (nextBadCount == currentBestBad
                        && nextDuration < currentBestDuration) {
                    betterPath = true;
                }

                // if this path to the neighbor is better than any previously known path, update the cost and \
                // add a new state to the priority queue
                if (betterPath) {
                    notGood.put(next, nextBadCount);
                    duration.put(next, nextDuration);
                    previous.put(next, current);
                    pq.add(new RouteState(next, nextBadCount, nextDuration));
                }
            }
        }

        // if the target is still unreachable after the search, return null cuz no route exitsts
        if (duration.get(target) == Integer.MAX_VALUE) {
            return null;
        }

        // reconstruct the route by walking backward through the previous map 
        List<Intersection> route = new ArrayList<>();
        Vertex<Intersection> current = target;
        while (current != null) {
            route.add(0, current.data()); 
            current = previous.get(current); 
        }

        return route;
    }

    /*
     * This function finds the best location for a power station among a list of candidate buildings.
     * It calculates the average shortest-path distance from each candidate to all reachable buildings.
     * The candidate with the smallest average distance is returned
     * 
     * @param candidates the list of candidate buildings to consider
     * @return the building that is the best power site based on average distance to all reachable buildings
     */
    @Override
    public Building getBestPowerSite(List<Building> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            throw new IllegalArgumentException("Candidate list cannot be null or empty");
        }

        Building best = null;
        double bestAvg = Double.MAX_VALUE;

        // for each candidate, we run D's to find the shortest distance from that candidate to every other building in the grid
        // then calc the avg of those distnnces (ignoring unreachable buildings) and keep track of the candidate with the 
        // lowest avg distance
        for (Building candidate : candidates) {
            if (candidate == null || !grid.getVertices().contains(new Vertex<>(candidate))) {
                throw new IllegalArgumentException("Every candidate must exist in the grid");
            }

            Map<Vertex<Building>, Integer> distances =
                    GraphAlgorithms.dijkstras(new Vertex<>(candidate), grid);

            long total = 0;
            int count = 0;

            // for each vertex in the grid, we check the distance from the candidate to that vertex 
            // rf it's reachable (aka the distance != Integer.MAX_VALUE), then we add it to the total and 
            // increment the count of reachable buildings
            for (Vertex<Building> vertex : distances.keySet()) {
                int distance = distances.get(vertex);

                if (distance != Integer.MAX_VALUE) {
                    total += distance;
                    count++;
                }
            }

            // calc the avg distance to all reachable buildings from this candidate
            double average;
            if (count == 0) {
                average = Double.MAX_VALUE;
            } else {
                average = (double) total / count;
            }

            // seed the first valid candidate, then keep the smallest avg
            if (best == null || average < bestAvg) {
                bestAvg = average;
                best = candidate;
            }
        }

        return best;
    }

    /*
     * This function consolidates the electrical grid by removing unnecessary wires while maintaining connectivity
     * It does this by finding a mst of the grid graph, which represents the subset of wires needed to keep all buildings 
     * connected with min total wire length
     * then it removes any wires that aren't part of the mst, which consolidates the grid and eliminates redundant wiring
     * 
     * this is lowgurtgeninely kinda like evict in Dale
     * 
     * @return the fraction of wire length that was eliminated
     */
    @Override
    public double consolidateGrid() {
        int originalTotalLength = 0;

        // first we gotta calculate the total length of all the wires currently in the grid so we can compare 
        // it to the total length of the mst later
        for (Edge<Building> edge : grid.getEdges()) {
            originalTotalLength += edge.weight();
        }

        if (originalTotalLength == 0) {
            return 0.0; // theres nothing to consolidate
        }

        Set<Edge<Building>> mst = GraphAlgorithms.kruskals(grid);
        if (mst == null) {
            return 0.0; // disconnected graph means no valid mst
        }

        int keptLength = 0;
        
        // Sum up the total length of the wires we need to keep based on the mst
        for (Edge<Building> edge : mst) {
            keptLength += edge.weight();
        }

        // this arraylist holds the edges we need to remove from the grid after we determine which ones are in the mst
        List<Edge<Building>> toRemove = new ArrayList<>();

        // any edge that isn't in the mst is redundant and can be removed to consolidate the grid, 
        // so we check every edge in the grid and mark it for removal if it's not in the mst
        for (Edge<Building> edge : grid.getEdges()) {
            boolean keepEdge = mst.contains(edge);

            if (!keepEdge) {
                toRemove.add(edge);
            }
        }

        // actually remove the redundant edges from the grid graph
        for (Edge<Building> edge : toRemove) {
            grid.removeEdge(edge);
        }

        // retunr the fraction of wire length that was eliminated.
        return (double) (originalTotalLength - keptLength) / originalTotalLength;
    }

    /**
     * Returns the road graph of the kingdom
     *
     * @return the road graph
     */
    @Override
    public StaticGraph<Intersection> getRoads() {
        return roads; 
    }
    
    /**
     * Retunrs the electrical grid graph of the kingdom
     *
     * @return the grid graph
     */
    @Override
    public StaticGraph<Building> getGrid() {
        return grid;
    }

    /**
     * Ensures every building already present in the initial electrical grid
     * references a closest intersection that actually exists in the road graph
     */
    private void validateInitialGridBuildings() {
        for (Vertex<Building> vertex : grid.getVertices()) {
            validateBuildingClosestIntersection(vertex.data());
        }
    }

    /**
     * Validates that a building's closest intersection, when present, belongs
     * to the road graph.
     *
     * @param building the building to validate
     */
    private void validateBuildingClosestIntersection(Building building) {
        if (building.closest() != null
            && !roads.getVertices().contains(new Vertex<>(building.closest()))) {
            throw new IllegalArgumentException("Closest intersection must exist in the road graph");
        }
    }

    /**
     * Validation helper for path-based road operations.
     *
     * A building must:
     * - not be null
     * - exist in the grid
     * - have a closest intersection
     * - have that closest intersection present in the roads graph
     *
     * @param building the building being checked
     */
    private void validateGridBuildingForTraversal(Building building) {
        // all of these conditions are necessary to ensure the building is properly connected to the road graph so 
        // pathfinding operations can run correctly. If any of them were false, we might end up with null pointer 
        // exceptions or basically disconnected components, which legit would be bad
        if (building == null) {
            throw new IllegalArgumentException("Building cannot be null");
        }
        if (!grid.getVertices().contains(new Vertex<>(building))) {
            throw new IllegalArgumentException("Building must exist in the grid");
        }
        if (building.closest() == null) {
            throw new IllegalArgumentException("Building must have a closest intersection");
        }
        if (!roads.getVertices().contains(new Vertex<>(building.closest()))) {
            throw new IllegalArgumentException("Closest intersection must exist in the road graph");
        }
    }

    /**
     * This is a quick graph connectivity helper that i used during constructor validation
     *
     * It runs a BFS from an arbitrary start vertex and checks whether every
     * vertex in the graph becomes reachable.
     *
     * @param graph the graph to inspect
     * @param <T> the vertex data type
     * @return whether every vertex can be reached from an arbitrary start vertex
     */
    private static <T> boolean isConnected(StaticGraph<T> graph) {
        if (graph.getVertexCount() == 0) {
            return true;
        }

        Vertex<T> start = graph.getVertices().iterator().next();
        Set<Vertex<T>> visited = new HashSet<>();
        // i used a queue here to implement the BFS traversal of the graph. The queue allows us to explore vertices in the 
        // order they were discovered, which is essential for BFS to ensure we find the shortest path in terms of number of edges.
        Queue<Vertex<T>> queue = new ArrayDeque<>();

        visited.add(start);
        queue.add(start);

        // keep exploring vertices until there are no more reachable ones left. Each time we explore a vertex,
        // we add all of its unvisited neighbors to the queue to explore later.
        while (!queue.isEmpty()) {
            Vertex<T> current = queue.remove();

            // now we check all the neighbors of the current vertex and add them to the queue 
            // if we haven't visited them before
            for (VertexDistance<T> neighbor : graph.getNeighbors(current)) {
                Vertex<T> next = neighbor.vertex();

                if (!visited.contains(next)) {
                    visited.add(next);
                    queue.add(next);
                }
            }
        }

        return visited.size() == graph.getVertexCount();
    }

    /**
     * Small helper record used by calculateRoute.
     *
     * badSeen is the primary cost metric & 
     * totalDuration is the secondary tiebreaker
     */
    private record RouteState(Vertex<Intersection> vertex, int badSeen, int totalDuration) {
        
    }
}
// slay the day away