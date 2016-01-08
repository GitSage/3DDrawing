package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Add your rectangle code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Rectangle extends Shape {

	// The width of this shape.
	private double width;

	// The height of this shape.
	private double height;

	/**
	 * Basic constructor that sets all fields.
	 * @param color the color for the new shape.
	 * @param center the center of the new shape.
	 * @param width the width of the new shape.
	 * @param height the height of the new shape.
	 */
	public Rectangle(Color color, Point2D.Double center, double width, double height) {

		// Initialize the superclass.
		super(color, center);

		// Set fields.
		this.width = width;
		this.height = height;
	}

	/**
	 * Getter for this shape's width.
	 * @return this shape's width as a double.
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Setter for this shape's width.
	 * @param width the new width.
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * Getter for this shape's height.
	 * @return this shape's height as a double.
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Setter for this shape's height.
	 * @param height the new height.
	 */
	public void setHeight(double height) {
		this.height = height;
	}

    public Point2D.Double getUpperLeft(){
        return new Point2D.Double(getCenter().getX() - width/2, getCenter().getY() - height/2);
    }

	/**
	 * Add your code to do an intersection test
	 * here. You shouldn't need the tolerance.
	 * @param pt = the point to test against.
	 * @param tolerance = the allowable tolerance.
	 * @return true if pt is in the shape,
	 *		   false otherwise.
	 */
	@Override
	public boolean pointInShape(Point2D.Double pt, double tolerance) {
		AffineTransform w2o = new AffineTransform();
		w2o.rotate(-getRotation());
		w2o.translate(-center.getX(), -center.getY());
		Point2D.Double transPt = new Point2D.Double(pt.getX(), pt.getY());
		w2o.transform(pt, transPt);

        return transPt.getX() <= getWidth() / 2 &&
				transPt.getX() >= -getWidth() / 2 &&
				transPt.getY() <= getHeight() / 2 &&
				transPt.getY() >= -getHeight() / 2;
	}

    @Override
    public Point2D.Double getPointAbove(double dist) {
        return new Point2D.Double(getCenter().getX(), getCenter().getY() - getHeight()/2 - dist);
    }
}
