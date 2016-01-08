package cs355.model.drawing;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by ben on 10/5/15.
 */
public class LineHandle extends Shape {

    private Point2D.Double end;
    public static final double RADIUS = 12;

    public LineHandle(Point2D.Double end){
        super(Color.RED, end);
        this.end = end;
    }

    @Override
    public void setCenter(Point2D.Double center){
        end.setLocation(center);
    }

    public Point2D.Double getCenter() {
        return end;
    }


    @Override
    public boolean pointInShape(Point2D.Double pt, double tolerance) {
        return Math.abs(pt.distance(end)) < RADIUS;
    }

    @Override
    public Point2D.Double getPointAbove(double dist) {
        return null;
    }
}
