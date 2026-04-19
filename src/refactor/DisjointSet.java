package refactor;
// alright divas its time to find and union our way to an A :)

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
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
 * This is a class to store a DisjointSet data structure. This data structure has two
 * main functions: find and union. 
 *    - find will look for the root (parent) of a DisjointSet. Calling find on two different T data will 
 *      check if those two are
 *      part of the same set. 
 *    - union will join two sets together if not already 
 */
public class DisjointSet<T> {

    /**
     * Maps each data item to its node in the union-find forest.
     */
    private final Map<T, DisjointSetNode<T>> disjointSet;

    /**
     * Tracks the current representative/root data values for every disjoint set.
     *
     * This backing set is what allows getRoots() to run in O(1), since the
     * method can simply return an unmodifiable view of this already-maintained
     * set instead of recomputing the roots each time
     */
    private final Set<T> roots;

    /**
     * Initializes the disjoint sets by instantiating a HashMap and HashSet to store the data and roots (respectively) 
     */
    public DisjointSet() {
        disjointSet = new HashMap<>();
        roots = new HashSet<>();
    }

    /**
     * Finds the root node of the disjoint set containing data.
     * and puts the data in the disjoint sets if it does not already exist.
     *
     * @param data the data to search for
     * @return the disjoint set's root data
     */
    public T find(T data) {
        // rf this data has never appeared before, create a brand-new singleton set
        // A brand-new node is its own parent, so it also is the root of its own set :)
        if (!disjointSet.containsKey(data)) {
            disjointSet.put(data, new DisjointSetNode<>(data));
            roots.add(data);
        }

        // fnid the root node and return the root's data.
        return find(disjointSet.get(data)).getData();
    }

    /**
     * Recursively finds the root of the DisjointSetNode. 
     * Performs path compression such that all DisjointSetNodes along the path to the root
     * will all directly point to the root.
     *
     * @param curr the current DisjointSetNode to find the root of
     * @return the root of the current node
     */
    private DisjointSetNode<T> find(DisjointSetNode<T> curr) {
        DisjointSetNode<T> parent = curr.getParent();
        if (parent == curr) { // if the parent is itself, then this is the root, so return it
            return curr;
        } else { // else, we gotta recurisvely find the root
            parent = find(curr.getParent());
            // then do path compression by setting the current node's parent to the root we found
            curr.setParent(parent); 
            return parent;
        }
    }

    /**
     * Attempts to join the two data into the same set by pointing the parent
     * of one set to the parent of another set.
     *
     * @param first The first data to find the parent of
     * @param second The second data to find the parent of
     */
    public void union(T first, T second) {
        // first, make sure both elements exist in the structure
        find(first);
        find(second);

        // then actually do the union of the two sets by finding their nodes and calling the private union 
        // helper function
        union(disjointSet.get(first), disjointSet.get(second));
    }

    /**
     * This is where the work is done for union(). This method finds the
     * roots of both passed in nodes and checks if they are the same root.
     * If they are not the same root, then the root with the least rank will point
     * to the node with higher rank using the merge by rank
     *
     * @param first The first DisjointSetNode to find the parent of
     * @param second The second DisjointSetNode to find the parent of
     */
    private void union(DisjointSetNode<T> first, DisjointSetNode<T> second) {
        // Find the current roots of each node's set.
        DisjointSetNode<T> firstParent = find(first);
        DisjointSetNode<T> secondParent = find(second);

        // if they already share the same root, they are already in the same set,
        // so there is nothing to merge and the roots set should not change.
        if (firstParent != secondParent) {
            if (firstParent.getRank() < secondParent.getRank()) {
                // firstParent loses root status and becomes a child of secondParent.
                firstParent.setParent(secondParent);
                roots.remove(firstParent.getData());
            } else {
                // secondParent loses root status and becomes a child of firstParent.
                secondParent.setParent(firstParent);
                roots.remove(secondParent.getData());

                // If the ranks were equal, the chosen root's rank increases by 1.
                if (firstParent.getRank() == secondParent.getRank()) {
                    firstParent.setRank(firstParent.getRank() + 1);
                }
            }
        }
    }

    /**
     * Gets the set of all representative vertices (roots) in the disjoint set.
     *
     * @return the set of all roots in the disjoint set.
     * runs in O(1) since we maintain the roots in a separate set that is updated during union operations, 
     * so we can just return an unmodifiable view of that set without needing to recompute the roots each time
     */
    public Set<T> getRoots() {
        return Collections.unmodifiableSet(roots);
    }
}