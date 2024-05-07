package primitives;

public class Ray {

    private Point head;
    private Vector direction;
     public Ray (Point p, Vector vec)
     {
         head=p;
         direction=vec.normalize();
     }

}
