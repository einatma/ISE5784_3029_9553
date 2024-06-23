package lighting;

import primitives.*;

public class PointLight extends Light implements LightSource{

    protected Point position;
    private double kC=1d;
    private double kL=0d;
    private double kQ=0d;

    public PointLight(Color I, Point position) {
        super(I);
        this.position = position;
    }

    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }

    public Color getIntensity(Point p) {
        double disSquared = position.distanceSquared(p);
        double dis = position.distance(p);
        return getIntensity().scale(1/(kC + kL * dis + kQ * disSquared));
    }

    public Vector getL(Point p) {
        if (p.equals(position)) {
            return null;
        }
        return p.subtract(position).normalize();
    }

    @Override
    public double getDistance(Point p) {
        return position.distance(p);
    }


}
