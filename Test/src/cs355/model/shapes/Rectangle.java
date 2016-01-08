package cs355.model.shapes;

import java.awt.geom.Point2D;

public class Rectangle extends Shape {
    private double height;
    private double width;

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public boolean contains(Point2D point) {
        double x = point.getX(), y = point.getY();
        return (x >= -1 * (width / 2)) &&
                (x <= width / 2) &&
                (y >= -1 * (height / 2)) &&
                (y <= height / 2);
    }
}
