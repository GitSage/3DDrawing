package cs355.controller.mousehandlers;

import cs355.model.shapes.Circle;
import cs355.controller.CS355Controller;

import java.awt.geom.Point2D;

public class CircleHandler extends CanvasMouseInteractionHandler {
    private Circle activeCircle;
    private Point2D start;

    public CircleHandler(CS355Controller controller) {
        super(controller);
    }

    @Override
    public void down(Point2D start) {
        activeCircle = new Circle();
        activeCircle.setColor(controller.getColor());

        model.add(activeCircle);

        this.start = start;

        activeCircle.setCenter(start);
        activeCircle.setRadius(0);
    }

    @Override
    public void drag(Point2D end) {
        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();

        double width = Math.abs(dx);
        double height = Math.abs(dy);

        double diameter = Math.min(width, height);
        double radius = diameter/2;

        double x, y;

        if (dx >= 0) {
            x = start.getX() + radius;
        } else {
            x = start.getX() - radius;
        }

        if (dy >= 0) {
            y = start.getY() + radius;
        } else {
            y = start.getY() - radius;
        }

        activeCircle.setCenter(x, y);
        activeCircle.setRadius(radius);

        refresh();
    }

    @Override
    public void up(Point2D p) {
        activeCircle = null;
        start = null;
        refresh();
    }
}
