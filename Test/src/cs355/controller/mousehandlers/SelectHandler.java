package cs355.controller.mousehandlers;

import cs355.GUIFunctions;
import cs355.ManualAffineTransform;
import cs355.model.shapes.*;
import cs355.controller.CS355Controller;
import cs355.model.shapes.Shape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class SelectHandler extends CanvasMouseInteractionHandler {
    private Point2D last;
    private Handle clickedHandle;

    public SelectHandler(CS355Controller controller) {
        super(controller);
    }

    @Override
    public void down(Point2D p) {
        last = p;

        clickedHandle = handleAtPoint(p);
        if (clickedHandle == Handle.NONE) {
            Shape selected = model.firstShapeUnderPoint(p);
            model.setSelection(selected);

            if (selected != null) {
                GUIFunctions.changeSelectedColor(selected.getColor());
            } else {
                GUIFunctions.changeSelectedColor(controller.getColor());
            }

            refresh();
        }
    }

    @Override
    public void drag(Point2D p) {
        Shape selected = model.getSelection();
        if (selected == null) return;
        clickedHandle.handleDrag(p, last, selected);
        last = p;
    }

    private Handle handleAtPoint(Point2D p) {
        Shape shape = model.getSelection();
        if (shape == null) return Handle.NONE;

        if (shape instanceof Line) {
            return handleAtPoint(p, (Line) shape);
        }

        AffineTransform worldToObj = shape.fromWorldTransform();
        Point2D objCoord = new Point2D.Double();
        worldToObj.transform(p, objCoord);

        if (shape instanceof Triangle) {
            Handle handle = triangleCornerHandleAtPoint(objCoord, (Triangle) shape);
            if (handle != Handle.NONE) return handle;
        } else {
            double dx = shape.getWidth()/2;
            double dy = shape.getHeight()/2;

            Point2D topLeft     = new Point2D.Double(-dx, -dy);
            Point2D topRight    = new Point2D.Double( dx, -dy);
            Point2D bottomLeft  = new Point2D.Double(-dx,  dy);
            Point2D bottomRight = new Point2D.Double( dx,  dy);

            if (
                near(objCoord, topLeft) ||
                near(objCoord, topRight) ||
                near(objCoord, bottomLeft) ||
                near(objCoord, bottomRight)
            ) {
                return Handle.BBOXCORNER;
            }
        }

        if (near(objCoord, new Point(0, (int) -shape.getHeight()/2 - 17))) {
            return Handle.ROTATE;
        }

        return Handle.NONE;
    }

    private Handle handleAtPoint(Point2D p, Line line) {
        if (near(p, line.getStart())) {
            return Handle.START;
        } else if (near(p, line.getEnd())) {
            return Handle.END;
        }

        return Handle.NONE;
    }

    private Handle triangleCornerHandleAtPoint(Point2D p, Triangle t) {
        Point2D c1 = t.getCorner1();
        Point2D c2 = t.getCorner2();
        Point2D c3 = t.getCorner3();

        if (near(p, c1)) {
            return Handle.CORNER1;
        }

        if (near(p, c2)) {
            return Handle.CORNER2;
        }

        if (near(p, c3)) {
            return Handle.CORNER3;
        }

        return Handle.NONE;
    }

    private boolean near(Point2D p1, Point2D p2) {
        AffineTransform worldToView = controller.getView().worldToView;

        Point2D _p1 = new Point2D.Double();
        Point2D _p2 = new Point2D.Double();

        worldToView.transform(p1, _p1);
        worldToView.transform(p2, _p2);

        return _p1.distanceSq(_p2) <= 16;
    }

    private static void squareize(Point2D p) {
        int xsign = (p.getX() > 0) ? 1 : -1;
        int ysign = (p.getY() > 0) ? 1 : -1;
        double min = Math.min(Math.abs(p.getX()), Math.abs(p.getY()));
        p.setLocation(xsign * min, ysign * min);
    }

    private enum Handle {
        BBOXCORNER {
            @Override
            public void handleDrag(Point2D p, Point2D prevPoint, Shape shape) {
                Point2D center = shape.getCenter();

                AffineTransform worldToObj = new ManualAffineTransform();
                worldToObj.rotate(-1 * shape.getRotation());
                worldToObj.translate(-1 * center.getX(), -1 * center.getY());

                Point2D newCorner = new Point2D.Double();
                worldToObj.transform(p, newCorner);

                if (shape instanceof Square || shape instanceof Circle) {
                    squareize(newCorner);
                }

                int xsign = (newCorner.getX() > 0) ? 1 : -1;
                int ysign = (newCorner.getY() > 0) ? 1 : -1;

                AffineTransform unRotate = new ManualAffineTransform();
                unRotate.rotate(shape.getRotation());

                Point2D oppositeCorner = new Point2D.Double(-xsign * shape.getWidth()/2, -ysign * shape.getHeight()/2);
                Point2D centerOffset = new Point2D.Double(
                    (newCorner.getX() + oppositeCorner.getX())/2,
                    (newCorner.getY() + oppositeCorner.getY())/2
                );
                unRotate.transform(centerOffset, centerOffset);
                shape.setCenter(
                        center.getX() + centerOffset.getX(),
                        center.getY() + centerOffset.getY()
                );
                shape.setWidth(Math.abs(oppositeCorner.getX() - newCorner.getX()));
                shape.setHeight(Math.abs(oppositeCorner.getY() - newCorner.getY()));

                refresh();
            }
        },
        CORNER1 {
            @Override
            public void handleDrag(Point2D newCorner, Point2D prevPoint, Shape shape) {
                Triangle t  = (Triangle) shape;
                AffineTransform worldToObj = shape.fromWorldTransform();
                worldToObj.transform(newCorner, newCorner);
                t.setCorner1(newCorner);
                refresh();
            }
        },
        CORNER2 {
            @Override
            public void handleDrag(Point2D newCorner, Point2D prevPoint, Shape shape) {
                Triangle t  = (Triangle) shape;
                AffineTransform worldToObj = shape.fromWorldTransform();
                worldToObj.transform(newCorner, newCorner);
                t.setCorner2(newCorner);
                refresh();
            }
        },
        CORNER3 {
            @Override
            public void handleDrag(Point2D newCorner, Point2D prevPoint, Shape shape) {
                Triangle t  = (Triangle) shape;
                AffineTransform worldToObj = shape.fromWorldTransform();
                worldToObj.transform(newCorner, newCorner);
                t.setCorner3(newCorner);
                refresh();
            }
        },
        START {
            @Override
            public void handleDrag(Point2D point, Point2D prevPoint, Shape shape) {
                Line line = (Line) shape;
                line.setStart(point);
                refresh();
            }
        },
        END {
            @Override
            public void handleDrag(Point2D point, Point2D prevPoint, Shape shape) {
                Line line = (Line) shape;
                line.setEnd(point);
                refresh();
            }
        },
        ROTATE {
            @Override
            public void handleDrag(Point2D point, Point2D prevPoint, Shape shape) {
                Point2D center = shape.getCenter();
                double dx = point.getX() - center.getX();
                double dy = point.getY() - center.getY();
                double angle = Math.atan2(dx, dy) + Math.PI;
                shape.setRotation(-angle);
                refresh();
            }
        },
        NONE {
            @Override
            public void handleDrag(Point2D point, Point2D prevPoint, Shape shape) {
                Point2D center = shape.getCenter();
                double dx = point.getX() - prevPoint.getX(), dy = point.getY() - prevPoint.getY();
                shape.setCenter(center.getX() + dx, center.getY() + dy);
                refresh();
            }
        };

        public abstract void handleDrag(Point2D point, Point2D prevPoint, Shape shape);
    }

    @Override
    public void unload() {
        model.setSelection(null);
        GUIFunctions.changeSelectedColor(controller.getColor());
        refresh();
    }
}
