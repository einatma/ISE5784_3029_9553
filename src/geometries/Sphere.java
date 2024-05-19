package geometries;

import primitives.*;

public class Sphere extends RadialGeometry {
    private final Point center;

    public Sphere(Point c, double r) {
        super(r);
        center = c;
    }
@Override
    public Vector getNormal(Point p)
    {
        return p.subtract(center).normalize();
    }
}
