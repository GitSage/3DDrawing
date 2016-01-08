package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Add your triangle code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Triangle extends Shape {

	// The three points of the triangle.
	private Point2D.Double a;
	private Point2D.Double b;
	private Point2D.Double c;

	/**
	 * Basic constructor that sets all fields.
	 * @param color the color for the new shape.
	 * @param center the center of the new shape.
	 * @param a the first point, relative to the center.
	 * @param b the second point, relative to the center.
	 * @param c the third point, relative to the center.
	 */
	public Triangle(Color color, Point2D.Double center, Point2D.Double a,
					Point2D.Double b, Point2D.Double c)
	{

		// Initialize the superclass.
		super(color, center);

		// Set fields.
		this.a = a;
		this.b = b;
		this.c = c;
	}

	/**
	 * Getter for the first point.
	 * @return the first point as a Java point.
	 */
	public Point2D.Double getA() {
		return a;
	}

	/**
	 * Setter for the first point.
	 * @param a the new first point.
	 */
	public void setA(Point2D.Double a) {
		this.a = a;
	}

	/**
	 * Getter for the second point.
	 * @return the second point as a Java point.
	 */
	public Point2D.Double getB() {
		return b;
	}

	/**
	 * Setter for the second point.
	 * @param b the new second point.
	 */
	public void setB(Point2D.Double b) {
		this.b = b;
	}

	/**
	 * Getter for the third point.
	 * @return the third point as a Java point.
	 */
	public Point2D.Double getC() {
		return c;
	}

	/**
	 * Setter for the third point.
	 * @param c the new third point.
	 */
	public void setC(Point2D.Double c) {
		this.c = c;
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

        Point2D.Double ia = new Point2D.Double(a.getX(), a.getY());
        Point2D.Double ib = new Point2D.Double(b.getX(), b.getY());
        Point2D.Double ic = new Point2D.Double(c.getX(), c.getY());

        double first = dotProduct(subtract(transPt, ia), orth(subtract(ib, ia)));
        double second = dotProduct(subtract(transPt, ib), orth(subtract(ic, ib)));
        double third = dotProduct(subtract(transPt, ic), orth(subtract(ia, ic)));

        return first < 0 && second < 0 && third < 0;
	}

    public double dotProduct(Point2D.Double p1, Point2D.Double p2){
        return p1.getX() * p2.getX() + p1.getY() * p2.getY();
    }

    public Point2D.Double subtract(Point2D.Double p1, Point2D.Double p2){
        return new Point2D.Double(p1.getX() - p2.getX(), p1.getY() - p2.getY());
    }

    public Point2D.Double orth(Point2D.Double p){
        return new Point2D.Double(-p.getY(), p.getX());
    }

    @Override
    public Point2D.Double getPointAbove(double dist) {

        double y = Math.min(getCenter().getY() + getA().getY(), getCenter().getY() + getB().getY());
        y = Math.min(y, getCenter().getY() + getC().getY());
        return new Point2D.Double(getCenter().getX(), y - dist);
    }

}
