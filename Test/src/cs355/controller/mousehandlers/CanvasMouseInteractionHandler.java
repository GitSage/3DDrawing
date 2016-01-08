package cs355.controller.mousehandlers;

import cs355.GUIFunctions;
import cs355.controller.CS355Controller;
import cs355.model.CS355Model;

import java.awt.geom.Point2D;

public class CanvasMouseInteractionHandler {
    protected CS355Controller controller;
    protected CS355Model model;

    public CanvasMouseInteractionHandler(CS355Controller controller) {
        this.controller = controller;
        model = controller.getModel();
    }

    public void down(Point2D p) {}
    public void up(Point2D p) {}
    public void click(Point2D p) {}
    public void move(Point2D p) {}
    public void drag(Point2D p) {}

    public void unload() {}

    protected static void refresh() {
        GUIFunctions.refresh();
    }
}
