package primitives;
/**
 * Represents a point in 3D space using Cartesian coordinates (x, y, z).
 * @author Hadar Cohen-213953029 and Einat Mazuz -324019553
 */
public class Point {
    /**The coordinates of the point's location */
    protected final Double3 xyz;
    /**
     * Constructs a new Point object with the specified coordinates.
     *
     * @param xyz The Cartesian coordinates of the point as a Double3 object.
     * */
   Point(Double3 xyz) {
        this.xyz = xyz;
    }
    public static final Point ZERO = new Point(Double3.ZERO);

    /**
     * Constructs a new Point object with the specified Cartesian coordinates.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     * @param z The z-coordinate of the point.
     */
    public Point(double x, double y, double z) {
        this( new Double3(x,y,z));
    }
    /**
     * Zero triad (0,0,0).
     */
    @Override
    public String toString() {
        return "Point {" +xyz + '}';
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other &&
             other.xyz.equals(this.xyz));
    }
    /**
     * Adds a vector to the point, returning a new point.
     *
     * @param v The vector to add.
     * @return A new point that is the result of adding the vector to this point.
     */
    public Point add(Vector v) {
        return new Point(this.xyz.add(v.xyz));
    }
    /**
     * Subtracts a point from this point to get a vector.
     *
     * @param p The point to subtract.
     * @return A new vector that is the result of subtracting the given point from this point.
     * @throws IllegalArgumentException If the distance between the points is zero.
     */
    public Vector subtract(Point p) {
        if (Util.isZero(p.distanceSquared(this)))
             throw new IllegalArgumentException("A vector 0 was received");
        return new Vector(this.xyz.subtract(p.xyz));

    }
    /**
     * Calculates the square of the Euclidean distance between this point and another point.
     *
     * @param other The other point.
     * @return The square of the Euclidean distance between this point and the other point.
     */
    public double distanceSquared(Point other)
    {
        double dx = xyz.d1 - other.xyz.d1;
        double dy = xyz.d2 - other.xyz.d2;
        double dz = xyz.d3 - other.xyz.d3;
        return dx*dx + dy*dy + dz*dz;
    }
    /**
     * Calculates the Euclidean distance between this point and another point.
     *
     * @param other The other point.
     * @return The Euclidean distance between this point and the other point.
     */
    public double distance(Point other)
    {
        return Math.sqrt(distanceSquared(other));
    }
}
