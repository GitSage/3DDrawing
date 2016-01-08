package cs355.model.drawing;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Created by ben on 10/4/15.
 */
public class Handle extends Shape {

    private Shape s;
    public static final double HEIGHTABOVESHAPE = 15;
    public static final double RADIUS = 7;

    public Handle(Shape s){
        super(Color.RED, s.getCenter());
        this.s = s;
    }

    @Override
    public Point2D.Double getCenter(){
        Point2D.Double pointAbove = s.getPointAbove(HEIGHTABOVESHAPE);
        Point2D.Double finalPoint = new Point2D.Double();

        // First translate, then rotate
        AffineTransform w2o = new AffineTransform();
        w2o.rotate(s.getRotation(), s.getCenter().getX(), s.getCenter().getY());
        w2o.translate(0, -s.getCenter().getY() + pointAbove.getY());

        w2o.transform(s.getCenter(), finalPoint);
        return finalPoint;
    }

    @Override
    public boolean pointInShape(Point2D.Double pt, double tolerance) {
        AffineTransform w2o = new AffineTransform();
        w2o.rotate(-s.getRotation());
        Point2D.Double center = getCenter();
        w2o.translate(-center.getX(), -center.getY());
        Point2D.Double transPt = new Point2D.Double(pt.getX(), pt.getY());
        w2o.transform(pt, transPt);

        return Math.abs(transPt.distance(new Point2D.Double(0, 0))) < RADIUS;
    }

    @Override
    public Point2D.Double getPointAbove(double dist) {
        return null;
    }
}
