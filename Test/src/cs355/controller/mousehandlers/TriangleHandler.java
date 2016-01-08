package cs355.controller.mousehandlers;

import cs355.model.shapes.Triangle;
import cs355.controller.CS355Controller;

import java.awt.geom.Point2D;

public class TriangleHandler extends CanvasMouseInteractionHandler {
    private Point2D c1;
    private Point2D c2;
    private Point2D c3;

    public TriangleHandler(CS355Controller controller) {
        super(controller);
    }

    @Override
    public void click(Point2D point) {
        if (c1 == null) {
            c1 = point;
            return;
        } else if (c2 == null) {
            c2 = point;
            return;
        } else if (c3 == null) {
            c3 = point;
        }

        Triangle t = new Triangle(c1, c2, c3);
        t.setColor(controller.getColor());
        model.add(t);

        refresh();

        c1 = c2 = c3 = null;
    }
}
