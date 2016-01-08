package cs355.model.shapes;

import cs355.ManualAffineTransform;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public abstract class Shape {
    protected Color color;
    protected Point2D center = new Point2D.Double();
    protected double rotation = 0;

    public Color getColor() {
        return color;
    }

    public Shape setColor(Color c) {
        color = c;
        return this;
    }

    public Point2D getCenter() {
        return center;
    }

    public void setCenter(Point2D p) {
        center.setLocation(p);
    }

    public void setCenter(double x, double y) {
        center.setLocation(x, y);
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public boolean contains(Point2D point) { return false; }

    public abstract double getHeight();

    public double getWidth() { return 0; }

    public void setHeight(double height) {}

    public void setWidth(double width) {}

    public AffineTransform fromWorldTransform() {
        AffineTransform worldToObj = new ManualAffineTransform();
        worldToObj.rotate(-this.getRotation());
        worldToObj.translate(-center.getX(), -center.getY());
        return worldToObj;
    }

    public AffineTransform toWorldTransform() {
        AffineTransform objToWorld = new ManualAffineTransform();
        objToWorld.translate(center.getX(), center.getY());
        objToWorld.rotate(this.getRotation());
        return objToWorld;
    }
}
