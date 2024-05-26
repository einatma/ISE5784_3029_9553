package primitives;

import static primitives.Util.isZero;

/**
 * Represents a vector in 3D space, defined by its components (x, y, z).
 * @author Hadar Cohen-213953029 and Einat Mazuz -324019553
 */
public class Vector extends Point {
    /**
     * Constructs a new Vector object with the specified components.
     *
     * @param x The x-component of the vector.
     * @param y The y-component of the vector.
     * @param z The z-component of the vector.
     * @throws IllegalArgumentException If the vector has zero length.
     */
    public Vector(double x, double y, double z) {
       this(new Double3(x,y,z));
    }
    /**
     * Constructs a new Vector object with the specified components.
     *
     * @param xyz The components of the vector as a Double3 object.
     * @throws IllegalArgumentException If the vector has zero length.
     */
    public Vector (Double3 xyz) {
         super(xyz);
         if(xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("A vector 0 was received");
    }
    /**
     * Adds another vector to this vector, returning a new vector.
     *
     * @param vec The vector to add.
     * @return A new vector that is the result of adding the given vector to this vector.
     * @throws IllegalArgumentException If the resulting vector has zero length.
     */
    public Vector add(Vector vec)
    {
        return new Vector( xyz.add(vec.xyz));
    }
    /**
     * Scales this vector by a scalar value, returning a new vector.
     *
     * @param num The scalar value to scale the vector by.
     * @return A new vector that is the result of scaling this vector by the given scalar value.
     * @throws IllegalArgumentException If the resulting vector has zero length.
     */
    public Vector scale(double num)
    {
       return new Vector(xyz.scale(num));
    }
    /**
     * Calculates the dot product of this vector with another vector.
     *
     * @param vec The other vector.
     * @return The dot product of this vector with the other vector.
     */
    public double dotProduct(Vector vec)
    {
        return vec.xyz.d1*xyz.d1+vec.xyz.d2*xyz.d2+vec.xyz.d3* xyz.d3;
    }
    /**
     * Calculates the cross product of this vector with another vector.
     *
     * @param vec The other vector.
     * @return A new vector that is the cross product of this vector with the other vector.
     * @throws IllegalArgumentException If the resulting vector is the zero vector.
     */
    public Vector crossProduct(Vector vec)
    {
       double d1 = xyz.d2*vec.xyz.d3-xyz.d3*vec.xyz.d2;
       double d2 = xyz.d3*vec.xyz.d1-xyz.d1*vec.xyz.d3;
       double d3 =  xyz.d1*vec.xyz.d2- xyz.d2*vec.xyz.d1;
       if (d1==d2 && d2==d3 && d3==0)
            throw new IllegalArgumentException("A vector 0 was received");
       return  new Vector(d1,d2,d3);
    }
    /**
     * Calculates the square of the length (magnitude) of this vector.
     *
     * @return The square of the length of this vector.
     */
    public double lengthSquared()
    {
        return this.dotProduct(this);
    }
    /**
     * Calculates the length (magnitude) of this vector.
     *
     * @return The length of this vector.
     */
    public double length()
    {
        return Math.sqrt(lengthSquared());
    }
    /**
     * Normalizes this vector to have a length of 1, returning a new vector.
     *
     * @return A new vector that is the normalized form of this vector.
     * @throws IllegalArgumentException If this vector is the zero vector.
     */
    public Vector normalize()
    {
        if(isZero(length()))
            throw new IllegalArgumentException("A vector 0 was received");
        return  this.scale(1/length());
    }
}
