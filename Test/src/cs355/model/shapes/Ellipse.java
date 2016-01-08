package cs355.model.shapes;

import java.awt.geom.Point2D;

public class Ellipse extends Shape {
    private double width = 0;
    private double height = 0;

    @Override
    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public boolean contains(Point2D point) {
        double x = point.getX(), y = point.getY(), rx = width/2.0, ry = height/2.0;
        return (
                (x*x)/(rx*rx) + (y*y)/(ry*ry)
        ) <= 1;
    }
}
