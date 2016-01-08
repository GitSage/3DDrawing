package cs355.view;

import cs355.GUIFunctions;
import cs355.HouseModel;
import cs355.ManualAffineTransform;
import cs355.WireFrame;
import cs355.controller.Camera;
import cs355.model.CS355Model;
import cs355.model.shapes.Shape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class ViewRefresher implements cs355.ViewRefresher {
    private CS355Model model;
    private Drawer drawer = new Drawer(this);

    public AffineTransform worldToView = new ManualAffineTransform();
    public AffineTransform viewToWorld = new ManualAffineTransform();
    double zoom = 1;

    double scrollX = 0;
    double scrollY = 0;

    double canvasWidth = 2048;
    double canvasHeight = 2048;

    public boolean drawImage = true;

    public boolean draw3DHouse = false;
    public Camera camera = new Camera(0, 5, -20);

    public ViewRefresher(CS355Model model) {
        this.model = model;
        updateWorldToViewTransform();
    }

    public void zoomIn() {
        if (zoom >= 4) return;

        Point2D center = new Point2D.Double(canvasWidth/2, canvasHeight/2);
        viewToWorld.transform(center, center);

        zoom *= 2;
        updateScrollBars();
        updateWorldToViewTransform();

        GUIFunctions.setHScrollBarPosit((int) center.getX());
        GUIFunctions.setVScrollBarPosit((int) center.getY());

        GUIFunctions.refresh();
    }

    public void zoomOut() {
        if (zoom <= 0.25) return;

        Point2D center = new Point2D.Double(canvasWidth/2, canvasHeight/2);
        viewToWorld.transform(center, center);

        zoom *= 0.5;
        updateScrollBars();
        updateWorldToViewTransform();

        GUIFunctions.setHScrollBarPosit((int) center.getX());
        GUIFunctions.setVScrollBarPosit((int) center.getY());

        GUIFunctions.refresh();
    }

    public void updateScrollBars() {
        System.out.println("Setting scroll bars");
        GUIFunctions.setHScrollBarMax((int) canvasWidth);
        GUIFunctions.setHScrollBarKnob((int) (canvasWidth/zoom));

        GUIFunctions.setVScrollBarMax((int) canvasHeight);
        GUIFunctions.setVScrollBarKnob((int) (canvasHeight/zoom));
    }

    public void updateWorldToViewTransform() {
        worldToView.setToIdentity();
        worldToView.translate(-scrollX, -scrollY);
        worldToView.scale(zoom, zoom);

        viewToWorld.setToIdentity();
        viewToWorld.scale(1/zoom, 1/zoom);
        viewToWorld.translate(scrollX, scrollY);
    }

    public void scrollHTo(int value) {
        scrollX = value;
        updateWorldToViewTransform();
        GUIFunctions.refresh();
    }

    public void scrollVTo(int value) {
        scrollY = value;
        updateWorldToViewTransform();
        GUIFunctions.refresh();
    }

    WireFrame house = new HouseModel();
    @Override
    public void refreshView(Graphics2D g2d) {
        drawer.setG(g2d);

        if (model.image != null && drawImage) {
            g2d.setTransform(worldToView);
            BufferedImage bi = model.image.getBufferedImage();
            g2d.drawImage(bi, null, (2048-bi.getWidth())/2, (2048-bi.getHeight())/2);
        }

        for (Shape shape : model) {
            drawer.draw(shape);
        }

        Shape selected = model.getSelection();
        if (selected != null) {
            drawer.drawSelectionOutlineAndHandles(selected);
        }

        if (draw3DHouse) {
            drawer.drawWireFrame(house);
        }
    }
}
