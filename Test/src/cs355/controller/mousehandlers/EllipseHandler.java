package cs355.controller.mousehandlers;

import cs355.model.shapes.Ellipse;
import cs355.controller.CS355Controller;

import java.awt.geom.Point2D;

public class EllipseHandler extends CanvasMouseInteractionHandler {
    private Ellipse activeEllipse;
    private Point2D start;

    public EllipseHandler(CS355Controller controller) {
        super(controller);
    }

    @Override
    public void down(Point2D start) {
        activeEllipse = new Ellipse();
        activeEllipse.setColor(controller.getColor());

        controller.getModel().add(activeEllipse);

        this.start = start;

        activeEllipse.setCenter(start);
    }

    @Override
    public void drag(Point2D end) {
        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();

        double width = Math.abs(dx);
        double height = Math.abs(dy);

        double x, y;
        if (dx >= 0) {
            x = start.getX() + width/2;
        } else {
            x = start.getX() - width/2;
        }

        if (dy >= 0) {
            y = start.getY() + height/2;
        } else {
            y = start.getY() - height/2;
        }

        activeEllipse.setCenter(x, y);
        activeEllipse.setWidth(width);
        activeEllipse.setHeight(height);

        refresh();
    }

    @Override
    public void up(Point2D p) {
        activeEllipse = null;
        start = null;

        refresh();
    }
}
