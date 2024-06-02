package primitives;

import static primitives.Util.isZero;

/**
 * Represents a ray in 3D space, defined by its starting point (head) and direction.
 *
 * @author Hadar Cohen-213953029 and Einat Mazuz -324019553
 */
public class Ray {
    /**
     * The starting point (head) of the ray.
     */
    private final Point head;
    /**
     * The direction vector of the ray, normalized.
     */
    private final Vector direction;

    /**
     * Constructs a new Ray object with the specified starting point and direction.
     *
     * @param p   The starting point (head) of the ray.
     * @param vec The direction vector of the ray.
     */
    public Ray(Point p, Vector vec) {
        head = p;
        direction = vec.normalize();
    }

    /**
     * Returns the starting point (head) of the ray.
     *
     * @return The starting point (head) of the ray.
     */
    public Point getHead() {
        return head;
    }

    /**
     * Returns the direction vector of the ray.
     *
     * @return The direction vector of the ray.
     */
    public Vector getDirection() {
        return direction;
    }


    public Point addToHead(double t) {
        if(isZero(t))
            return head;
        return head.add(direction.scale(t));
    }
}
