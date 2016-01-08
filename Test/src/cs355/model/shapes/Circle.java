package cs355.model.shapes;

import java.awt.geom.Point2D;

public class Circle extends Shape {
    private double radius;

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public boolean contains(Point2D point) {
        double x = point.getX(), y = point.getY(), r2 = radius*radius;
        return (
                (x*x)/(r2) + (y*y)/(r2)
        ) <= 1;
    }

    @Override
    public double getHeight() {
        return radius*2;
    }

    @Override
    public double getWidth() {
        return radius*2;
    }

    @Override
    public void setHeight(double height) {
        radius = height/2;
    }

    @Override
    public void setWidth(double width) {
        super.setWidth(width);
    }
}
