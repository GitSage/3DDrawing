package cs355.controller.mousehandlers;

import cs355.model.shapes.Line;
import cs355.controller.CS355Controller;

import java.awt.geom.Point2D;

public class LineHandler extends CanvasMouseInteractionHandler {
    private Line activeLine;

    public LineHandler(CS355Controller controller) {
        super(controller);
    }

    @Override
    public void down(Point2D start) {
        activeLine = new Line();
        activeLine.setColor(controller.getColor());

        controller.getModel().add(activeLine);

        activeLine.setStart(start);
        activeLine.setEnd(start);
    }

    @Override
    public void drag(Point2D end) {
        activeLine.setEnd(end);
        refresh();
    }

    @Override
    public void up(Point2D p) {
        activeLine = null;
        refresh();
    }
}
