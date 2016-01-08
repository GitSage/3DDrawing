package cs355.controller.mousehandlers;

import cs355.model.shapes.Rectangle;
import cs355.controller.CS355Controller;

import java.awt.geom.Point2D;

public class RectangleHandler extends CanvasMouseInteractionHandler {
    private Rectangle activeRectangle;
    private Point2D start;

    public RectangleHandler(CS355Controller controller) {
        super(controller);
    }

    @Override
    public void down(Point2D start) {
        activeRectangle = new Rectangle();
        activeRectangle.setColor(controller.getColor());

        controller.getModel().add(activeRectangle);

        this.start = start;

        activeRectangle.setCenter(start);
        activeRectangle.setWidth(0);
        activeRectangle.setHeight(0);
    }

    @Override
    public void drag(Point2D end) {
        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();

        double width = Math.abs(dx);
        double height = Math.abs(dy);

        double x = (start.getX() + end.getX()) / 2;
        double y = (start.getY() + end.getY()) / 2;

        activeRectangle.setCenter(x, y);
        activeRectangle.setWidth(width);
        activeRectangle.setHeight(height);

        refresh();
    }

    @Override
    public void up(Point2D p) {
        activeRectangle = null;
        start = null;

        refresh();
    }
}
