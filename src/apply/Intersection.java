package apply;

/**
 * Record class representing an Intersection in WaddleWorks.
 *
 * @param id the unique identifier of the Intersection
 * @param type the special type of the Intersection, if applicable
 */
public record Intersection(String id, Type type) {

    /**
     * Constructor overload for a default Intersection.
     *
     * @param id the unique identifier of the Intersection
     */
    public Intersection(String id) {
        this(id, Type.NORMAL);
    }

    /**
     * Enum storing the different classes of Intersections.
     */
    public enum Type {
        NORMAL,
        TOLL,
        BRIDGE,
        TUNNEL,
        ROUNDABOUT,
        HIGHWAY
    }

    @Override
    public String toString() {
        return type == Type.NORMAL ? id : id + "(" + type + ")";
    }
}
