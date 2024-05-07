package geometries;
import primitives.*;

public class Plane implements Geometry{
   final private Point q;
   final private Vector normal;
   public Plane(Point a, Point b, Point c)
   {
       q=a;
       normal=null;
   }
   public Plane(Point p, Vector n)
   {
       q=p;
       normal=n;
   }
   //לשאול את אליעזר
    public Vector getNormal(Point point) {
        return normal;
    }
    public Vector getNormal()
    {
        return normal;
    }


}
