package geometries;
//לשאול את אליעזר אם היא מחלקה אבסטרקטית
/**
 * Represents a radial geometry in 3D space, defined by a radius.
 */
public abstract class RadialGeometry implements Geometry {
    /** The radius of the radial geometry. */
    final double radius;
    /**
     * Constructs a new RadialGeometry object with the specified radius.
     *
     * @param r The radius of the radial geometry.
     */
    public RadialGeometry(double r)
    {
        radius=r;
    }
}
