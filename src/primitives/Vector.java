package primitives;
public class Vector extends Point {
    //constructor
public Vector(double x, double y, double z) {
   //לשאול את לאיעזר לגבי זריקת חריגה בבנאי זה
    super(x, y, z);
}
public Vector (Double3 xyz) {
    super(xyz);


}
public Vector add(Vector vec)
{
    return new Vector(xyz.add(vec.xyz));
}
public Vector scale(double num)
{
    return new Vector(xyz.scale(num));
}
public double dotProduct(Vector vec)
{
    return vec.xyz.d1*xyz.d1+vec.xyz.d2*xyz.d2+vec.xyz.d3* xyz.d3;
}
public Vector crossProduct(Vector vec)
{
    return new Vector(xyz.d2*vec.xyz.d3-xyz.d3*vec.xyz.d2,xyz.d3*vec.xyz.d1-xyz.d1*vec.xyz.d3, xyz.d1*vec.xyz.d2- xyz.d2*vec.xyz.d1);
}
public double lengthSquared()
{
    return this.dotProduct(this);
}
public double length()
{
    return Math.sqrt(lengthSquared());
}
public Vector normalize()
{
    if(Util.isZero(length()))
        throw new IllegalArgumentException("A vector 0 was received");
    return  this.scale(1/length());
}

}
