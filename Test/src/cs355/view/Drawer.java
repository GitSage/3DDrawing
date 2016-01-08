package cs355.view;

import cs355.*;
import cs355.model.shapes.*;
import cs355.model.shapes.Shape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Drawer {
    private ViewRefresher view;
    private Graphics2D g;

    private AffineTransform objToWorld;
    private AffineTransform worldToView;
    private AffineTransform objToView;

    public Drawer(ViewRefresher view) {
        this.view = view;
    }

    public void draw(Line line, boolean outline) {
        g.setColor(line.getColor());

        Point2D start = line.getStart();
        Point2D end = line.getEnd();

        if (outline) {
            Point2D s = new Point2D.Double();
            Point2D e = new Point2D.Double();

            objToView.transform(start, s);
            objToView.transform(end, e);

            g.setColor(Color.CYAN);
            g.fillOval((int) s.getX() - 3, (int) s.getY() - 3, 7, 7);
            g.fillOval((int) e.getX() - 3, (int) e.getY() - 3, 7, 7);
        } else {
            g.drawLine(
                (int) start.getX(),
                (int) start.getY(),
                (int) end.getX(),
                (int) end.getY()
            );
        }
    }

    public void draw(Circle circle, boolean outline) {
        g.setColor(circle.getColor());

        double radius = circle.getRadius();

        if (outline) {
            radius *= view.zoom;

            g.setColor(Color.CYAN);
            g.drawOval((int) -radius, (int) -radius, (int) (radius * 2), (int) (radius * 2));
        } else {
            g.fillOval((int) -radius, (int) -radius, (int) (radius * 2), (int) (radius * 2));
        }
    }

    public void draw(Ellipse ellipse, boolean outline) {
        g.setColor(ellipse.getColor());

        double width = ellipse.getWidth();
        double height = ellipse.getHeight();

        if (outline) {
            width *= view.zoom;
            height *= view.zoom;

            g.setColor(Color.CYAN);
            g.drawOval(
                    (int) (-width / 2),
                    (int) (-height / 2),
                    (int) width,
                    (int) height
            );
        } else {
            g.fillOval(
                (int) (-width / 2),
                (int) (-height / 2),
                (int) width,
                (int) height
            );
        }
    }

    public void draw(cs355.model.shapes.Rectangle rectangle, boolean outline) {
        g.setColor(rectangle.getColor());

        double width = rectangle.getWidth();
        double height = rectangle.getHeight();

        if (outline) {
            width *= view.zoom;
            height *= view.zoom;

            g.setColor(Color.CYAN);
            g.drawRect((int) (-width / 2), (int) (-height / 2), (int) (width), (int) (height));
        } else {
            g.fillRect(
                (int) (-width / 2),
                (int) (-height / 2),
                (int) width,
                (int) height
            );
        }
    }

    public void draw(Square square, boolean outline) {
        g.setColor(square.getColor());

        double size = square.getSize();

        if (outline) {
            size *= view.zoom;

            g.setColor(Color.CYAN);
            g.drawRect(
                    (int) (-size / 2),
                    (int) (-size / 2),
                    (int) size,
                    (int) size
            );
        } else {
            g.fillRect(
                (int) (-size / 2),
                (int) (-size / 2),
                (int) size,
                (int) size
            );
        }
    }

    public void draw(Triangle triangle, boolean outline) {
        g.setColor(triangle.getColor());

        Point2D c1 = triangle.getCorner1();
        Point2D c2 = triangle.getCorner2();
        Point2D c3 = triangle.getCorner3();

        int[] x = {
            (int) (c1.getX()),
            (int) (c2.getX()),
            (int) (c3.getX())
        };

        int[] y = {
            (int) (c1.getY()),
            (int) (c2.getY()),
            (int) (c3.getY())
        };

        if (outline) {
            g.setColor(Color.CYAN);

            x[0] *= view.zoom;
            x[1] *= view.zoom;
            x[2] *= view.zoom;

            y[0] *= view.zoom;
            y[1] *= view.zoom;
            y[2] *= view.zoom;

            g.drawPolygon(x, y, 3);

            g.fillOval((int) (c1.getX()*view.zoom)-3, (int) (c1.getY()*view.zoom)-3, 7, 7);
            g.fillOval((int) (c2.getX()*view.zoom)-3, (int) (c2.getY()*view.zoom)-3, 7, 7);
            g.fillOval((int) (c3.getX()*view.zoom)-3, (int) (c3.getY()*view.zoom)-3, 7, 7);
        } else {
            g.fillPolygon(x, y, 3);
        }
    }

    public void draw(Shape shape) {
        draw(shape, false);
    }

    public void drawSelectionOutlineAndHandles(Shape shape) {
        draw(shape, true);

        double dx = shape.getWidth()/2;
        double dy = shape.getHeight()/2;

        // Rotation handle
        Point2D rotationHandlePoint = new Point2D.Double(0, -dy-17);
        objToView.transform(rotationHandlePoint, rotationHandlePoint);
        g.fillOval((int) rotationHandlePoint.getX()-3, (int) rotationHandlePoint.getY()-3, 7, 7);

        // Bounding box corner handles (but not for lines and triangles)
        if (shape instanceof Line || shape instanceof Triangle) return;


        Point2D topLeft = new Point2D.Double(-dx, -dy);
        Point2D topRight = new Point2D.Double(-dx,  dy);
        Point2D bottomLeft = new Point2D.Double( dx, -dy);
        Point2D bottomRight = new Point2D.Double( dx,  dy);

        objToView.transform(topLeft, topLeft);
        objToView.transform(topRight, topRight);
        objToView.transform(bottomLeft, bottomLeft);
        objToView.transform(bottomRight, bottomRight);

        g.fillOval((int) topLeft.getX() - 3, (int) topLeft.getY() - 3, 7, 7);
        g.fillOval((int) topRight.getX()-3, (int) topRight.getY()-3, 7, 7);
        g.fillOval((int) bottomLeft.getX()-3, (int) bottomLeft.getY()-3, 7, 7);
        g.fillOval((int) bottomRight.getX()-3, (int) bottomRight.getY()-3, 7, 7);
    }

    public void draw(cs355.model.shapes.Shape shape, boolean outline) {
        objToWorld = shape.toWorldTransform();
        worldToView = view.worldToView;

        objToView = (AffineTransform) objToWorld.clone();
        objToView.preConcatenate(worldToView);

        if (!outline) g.setTransform(objToView);
        else {
            Point2D center = new Point2D.Double(0, 0);

            objToView.transform(center, center);

            AffineTransform t = new ManualAffineTransform();
            t.translate(center.getX(), center.getY());
            t.rotate(shape.getRotation());

            g.setTransform(t);
        }

        if (shape instanceof Line) {
            draw((Line) shape, outline);
        } else if (shape instanceof Ellipse) {
            draw((Ellipse) shape, outline);
        } else if (shape instanceof Circle) {
            draw((Circle) shape, outline);
        } else if (shape instanceof cs355.model.shapes.Rectangle) {
            draw((cs355.model.shapes.Rectangle) shape, outline);
        } else if (shape instanceof Square) {
            draw((Square) shape, outline);
        } else if (shape instanceof Triangle) {
            draw((Triangle) shape, outline);
        }

        g.setTransform(new ManualAffineTransform());
    }

    public void drawWireFrame(WireFrame model) {
        Matrix4 perspectiveMatrix = view.camera.getPerspectiveMatrix();

        g.setTransform(view.worldToView);
        g.setColor(Color.WHITE);

        for (Line3D line : model) {
            Vector4 start = new Vector4(line.start.x, line.start.y, line.start.z, 1);
            Vector4 end = new Vector4(line.end.x, line.end.y, line.end.z, 1);

            start.apply(perspectiveMatrix);
            end.apply(perspectiveMatrix);

            if (
                start.y < -start.w && end.y < -end.w ||
                start.y > start.w && end.y > end.w ||
                start.x < -start.w && end.x < -end.w ||
                start.x > start.w && end.x > end.w ||
                start.z < -start.w && end.z < -end.w ||
                start.z > start.w && end.z > end.w
            ) {
                continue;
            }

            start.divideW();
            end.divideW();

            g.drawLine((int) (start.x*-1024) + 1024, (int) (start.y*-1024) + 1024, (int) (end.x*-1024) + 1024, (int) (end.y*-1024) + 1024);
        }
    }

    public void setG(Graphics2D g) {
        this.g = g;
    }
}