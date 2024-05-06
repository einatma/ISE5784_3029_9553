public class Point {
    protected Double3 xyz;
   Point(Double3 xyz) {
        this.xyz = xyz;
    }
    public Point(double x, double y, double z) {
        this.xyz = new Double3(x,y,z);
    }
    /** Zero triad (0,0,0) */
    public static final Point ZERO = new Point(Double3.ZERO);

    /** One's triad (1,1,1) */
    public static final Point ONE  = new Point(Double3.ONE);

    @Override
    public String toString() {
        return xyz.toString();
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other &&
             other.xyz.equals(this.xyz));
    }
    public Point add(Vector v) {
        return new Point(this.xyz.add(v.xyz));
    }
    public Vector subtract(Point p) {
        if (p.xyz.subtract(this.xyz).equals(Double3.ZERO))
             throw new IllegalArgumentException("A vector 0 was received");
        return new Vector(p.xyz.subtract(this.xyz));
    }
    public double distanceSquared(Point p) {

    }
}
