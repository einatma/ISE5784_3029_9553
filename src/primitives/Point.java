package primitives;
public class Point {
    protected Double3 xyz;
   Point(Double3 xyz) {
        this.xyz = xyz;
    }
    //constructor
    public Point(double x, double y, double z) {
        this.xyz = new Double3(x,y,z);
    }
    /** Zero triad (0,0,0) */
    public static final Point ZERO = new Point(Double3.ZERO);

    /** One's triad (1,1,1) */
    public static final Point ONE  = new Point(Double3.ONE);

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

    public Point add(Vector v) {
        return new Point(this.xyz.add(v.xyz));
    }

    //substract points to get vector
    public Vector subtract(Point p) {
        if (Util.isZero(p.distanceSquared(this)))
             throw new IllegalArgumentException("A vector 0 was received");
        return new Vector(this.xyz.subtract(p.xyz));
    }
    public double distanceSquared(Point other)
    {
        double dx = xyz.d1 - other.xyz.d1;
        double dy = xyz.d2 - other.xyz.d2;
        double dz = xyz.d3 - other.xyz.d3;
        return dx*dx + dy*dy + dz*dz;
    }
    public double distance(Point other)
    {
        return Math.sqrt(distanceSquared(other));
    }




}
