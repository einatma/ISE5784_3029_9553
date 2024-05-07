package geometries;
import primitives.*;
/**
 * Represents a tube in 3D space, defined by its central axis and radius.
 */
public class Tube extends RadialGeometry{
    /** The central axis of the tube. */
    final Ray axis;
    /**
     * Constructs a new Tube object with the specified central axis and radius.
     *
     * @param a The central axis of the tube.
     * @param r The radius of the tube.
     */
    public Tube(Ray a, double r)
    {
        super(r);
        axis=a;
    }

    @Override
    public Vector getNormal(Point p) {
        return null;
    }
}
