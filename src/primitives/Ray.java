package primitives;
/**
 * Represents a ray in 3D space, defined by its starting point (head) and direction.
 * @author Hadar Cohen and Einat Mazuz */
public class Ray {
    /** The starting point (head) of the ray.*/
    private final Point head;
    /**The direction vector of the ray, normalized.*/
    private final Vector direction;
    /**
     * Constructs a new Ray object with the specified starting point and direction.
     *
     * @param p   The starting point (head) of the ray.
     * @param vec The direction vector of the ray.
     */
     public Ray (Point p, Vector vec)
     {
         head=p;
         direction=vec.normalize();
     }

}
