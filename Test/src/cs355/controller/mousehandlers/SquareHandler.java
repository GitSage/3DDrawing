package cs355.controller.mousehandlers;

import cs355.model.shapes.Square;
import cs355.controller.CS355Controller;

import java.awt.geom.Point2D;

public class SquareHandler extends CanvasMouseInteractionHandler {
    private Square activeSquare;
    private Point2D start;

    public SquareHandler(CS355Controller controller) {
        super(controller);
    }

    @Override
    public void down(Point2D start) {
        activeSquare = new Square();
        activeSquare.setColor(controller.getColor());

        controller.getModel().add(activeSquare);

        this.start = start;

        activeSquare.setCenter(start);
        activeSquare.setSize(0);
    }

    @Override
    public void drag(Point2D end) {
        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();

        double width = Math.abs(dx);
        double height = Math.abs(dy);

        double size = Math.min(width, height);

        double x, y;
        if (dx >= 0) {
            x = start.getX() + size/2;
        } else {
            x = start.getX() - size/2;
        }

        if (dy >= 0) {
            y = start.getY() + size/2;
        } else {
            y = start.getY() - size/2;
        }

        activeSquare.setCenter(x, y);
        activeSquare.setSize(size);

        refresh();
    }

    @Override
    public void up(Point2D p) {
        activeSquare = null;
        start = null;

        refresh();
    }
}
