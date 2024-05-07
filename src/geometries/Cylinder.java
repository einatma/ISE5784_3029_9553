package geometries;
import primitives.*;
//Hey Einat
/**
 * Represents a vector in 3D space, defined by its components (x, y, z).
 * @author Hadar Cohen and Einat Mazuz
 */
public class Cylinder extends  Tube{
    /** The height of the cylinder. */
    private double height;
    /**
     * Constructs a new Cylinder object with the specified height, axis, and radius.
     *
     * @param h The height of the cylinder.
     * @param a The axis (ray) of the cylinder.
     * @param r The radius of the cylinder.
     */
    public Cylinder (double h, Ray a, double r)
    {
        super(a,r);
        height = h;
    }

    @Override
    public Vector getNormal(Point p) {
        return super.getNormal(p);
    }
}
