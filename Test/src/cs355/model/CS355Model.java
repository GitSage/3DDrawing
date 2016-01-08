package cs355.model;

import cs355.model.shapes.Shape;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class CS355Model implements Iterable<Shape> {
    private Deque<Shape> shapes = new ArrayDeque<>();
    private Shape selection;

    public Image image;

    public void add(Shape shape) {
        shapes.add(shape);
    }

    public Shape firstShapeUnderPoint(Point2D p) {
        for (Shape shape : this.reverse()) {
            AffineTransform worldToObj = shape.fromWorldTransform();
            Point2D objCoord = new Point2D.Double();
            worldToObj.transform(p, objCoord);

            if (shape.contains(objCoord)) {
                return shape;
            }
        }

        return null;
    }

    @Override
    public Iterator<Shape> iterator() {
        return shapes.iterator();
    }

    public Iterable<Shape> reverse() {
        return new Iterable<Shape>() {
            @Override
            public Iterator<Shape> iterator() {
                return shapes.descendingIterator();
            }
        };
    }

    public Shape getSelection() {
        return selection;
    }

    public void setSelection(Shape selection) {
        this.selection = selection;
    }

    public void setImage(int width, int height, int[] pixels) {
        image = new Image(width, height, pixels);
    }
}
