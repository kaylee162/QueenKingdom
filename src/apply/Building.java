package apply;

/**
 * Record class representing a Building in WaddleWorks.
 *
 * @param name the unique identifier of the Building
 * @param closest the closest {@link Intersection} adjacent to the Building
 */
public record Building(String name, Intersection closest) {

    /**
     * Constructor overload for a default Building with 
     * a null closest Intersection.
     *
     * @param name the unique identifier of the Building
     */
    public Building(String name) {
        this(name, null);
    }
    @Override
    public String toString() {
        return closest == null ? name : name + "(" + closest.id() + ")";
    }
}
