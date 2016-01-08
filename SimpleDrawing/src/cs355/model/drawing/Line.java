package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * Add your line code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Line extends Shape {

	// The ending point of the line.
	private Point2D.Double end;

	/**
	 * Basic constructor that sets all fields.
	 * @param color the color for the new shape.
	 * @param start the starting point.
	 * @param end the ending point.
	 */
	public Line(Color color, Point2D.Double start, Point2D.Double end) {

		// Initialize the superclass.
		super(color, start);

		// Set the field.
		this.end = end;
	}

	/**
	 * Getter for this Line's ending point.
	 * @return the ending point as a Java point.
	 */
	public Point2D.Double getEnd() {
		return end;
	}

	/**
	 * Setter for this Line's ending point.
	 * @param end the new ending point for the Line.
	 */
	public void setEnd(Point2D.Double end) {
		this.end.setLocation(end);
	}

    public Point2D.Double getStart(){
        return getCenter(); }

    public void setStart(Point2D.Double start){ this.setCenter(start); }

	/**
	 * Add your code to do an intersection test
	 * here. You <i>will</i> need the tolerance.
	 * @param pt = the point to test against.
	 * @param tolerance = the allowable tolerance.
	 * @return true if pt is in the shape,
	 *		   false otherwise.
	 */
	@Override
	public boolean pointInShape(Point2D.Double pt, double tolerance) {
        return lineToPointDistance2D(pt) < tolerance;
	}

    @Override
    public Point2D.Double getPointAbove(double dist) {
        return null;
    }

    // Compute the dot product AB . AC
    private double dotProduct(Point2D.Double pointA, Point2D.Double pointB, Point2D.Double pointC)
    {
        Point2D.Double AB = new Point2D.Double(pointB.getX() - pointA.getX(), pointB.getY() - pointA.getY());
        Point2D.Double BC = new Point2D.Double(pointC.getX() - pointB.getX(), pointC.getY() - pointB.getY());
        double dot = AB.getX() * BC.getX() + AB.getY() * BC.getY();
        return dot;
    }

    //Compute the distance from AB to C
    private double lineToPointDistance2D(Point2D.Double pointC)
    {
        Point2D.Double start = getStart();
        Point2D.Double end = new Point2D.Double(getEnd().getX(), getEnd().getY());

        // get the cross product
        Point2D.Double AB = new Point2D.Double(end.getX() - start.getX(), end.getY() - start.getY());
        Point2D.Double AC = new Point2D.Double(pointC.getX() - start.getX(), pointC.getY() - start.getY());
        double cross = AB.getX() * AC.getY() - AB.getY() * AC.getX();

        double dist = cross / start.distance(end);

        // check for endpoints
        double dot1 = dotProduct(start, end, pointC);
        if (dot1 > 0){
            return end.distance(pointC);
        }

        double dot2 = dotProduct(end, start, pointC);
        if (dot2 > 0){
            return start.distance(pointC);
        }

        return Math.abs(dist);
    }


}
