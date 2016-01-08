package cs355.controller;

import cs355.GUIFunctions;
import cs355.model.CS355Model;
import cs355.controller.mousehandlers.*;
import cs355.view.ViewRefresher;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Iterator;

public class CS355Controller implements cs355.CS355Controller {
    public MouseListener mouseListener;
    public MouseMotionListener mouseMotionListener;

    private CanvasMouseInteractionHandler mouseInteractionHandler = new CanvasMouseInteractionHandler(this);

    private CS355Model model;
    private ViewRefresher view;
    private Color color = Color.blue;

    public CS355Controller(CS355Model model, ViewRefresher view) {
        this.model = model;
        this.view = view;

        createMouseListener();
        createMouseMotionListener();
    }

    public CS355Model getModel() {
        return model;
    }
    public ViewRefresher getView() { return view; }

    public void setMouseInteractionHandler(CanvasMouseInteractionHandler mouseInteractionHandler) {
        if (this.mouseInteractionHandler != null) {
            this.mouseInteractionHandler.unload();
        }
        this.mouseInteractionHandler = mouseInteractionHandler;
    }

    @Override
    public void colorButtonHit(Color c) {
        color = c;
        GUIFunctions.changeSelectedColor(c);
        if (model.getSelection() != null) {
            model.getSelection().setColor(c);
            GUIFunctions.refresh();
        }
    }

    @Override
    public void lineButtonHit() {
        setMouseInteractionHandler(new LineHandler(this));
    }

    @Override
    public void triangleButtonHit() {
        setMouseInteractionHandler(new TriangleHandler(this));
    }

    @Override
    public void squareButtonHit() {
        setMouseInteractionHandler(new SquareHandler(this));
    }

    @Override
    public void rectangleButtonHit() {
        setMouseInteractionHandler(new RectangleHandler(this));
    }

    @Override
    public void circleButtonHit() {
        setMouseInteractionHandler(new CircleHandler(this));
    }

    @Override
    public void ellipseButtonHit() {
        setMouseInteractionHandler(new EllipseHandler(this));
    }

    @Override
    public void selectButtonHit() {
        setMouseInteractionHandler(new SelectHandler(this));
    }

    @Override
    public void zoomInButtonHit() {
        view.zoomIn();
    }

    @Override
    public void zoomOutButtonHit() {
        view.zoomOut();
    }

    @Override
    public void hScrollbarChanged(int value) {
        view.scrollHTo(value);
    }

    @Override
    public void vScrollbarChanged(int value) {
        view.scrollVTo(value);
    }

    @Override
    public void toggle3DModelDisplay() {
        view.draw3DHouse = !view.draw3DHouse;
        GUIFunctions.refresh();
    }

    @Override
    public void keyPressed(Iterator<Integer> iterator) {
        while (iterator.hasNext()) {
            char key = (char) ((int) iterator.next());

            if (key == 'W') {
                view.camera.z += 1 * Math.cos(Math.toRadians(view.camera.angle));
                view.camera.x += 1 * Math.sin(Math.toRadians(view.camera.angle));
            }

            if (key == 'S') {
                view.camera.z -= 1 * Math.cos(Math.toRadians(view.camera.angle));
                view.camera.x -= 1 * Math.sin(Math.toRadians(view.camera.angle));
            }

            if (key == 'A') {
                view.camera.z += 1 * Math.cos(Math.toRadians(view.camera.angle+90));
                view.camera.x += 1 * Math.sin(Math.toRadians(view.camera.angle+90));
            }

            if (key == 'D') {
                view.camera.z -= 1 * Math.cos(Math.toRadians(view.camera.angle+90));
                view.camera.x -= 1 * Math.sin(Math.toRadians(view.camera.angle+90));
            }

            if (key == 'Q') {
                view.camera.angle += 1;
                if (view.camera.angle > 360) {
                    view.camera.angle -= 360;
                }
            }

            if (key == 'E') {
                view.camera.angle -= 1;
                if (view.camera.angle < 0) {
                    view.camera.angle += 360;
                }
            }

            if (key == 'R') {
                view.camera.y += 1;
            }

            if (key == 'F') {
                view.camera.y -= 1;
            }
        }

        GUIFunctions.refresh();
    }

    @Override
    public void doEdgeDetection() {
        model.image.detectEdges();
        GUIFunctions.refresh();
    }

    @Override
    public void doSharpen() {
        model.image.sharpen();
        GUIFunctions.refresh();
    }

    @Override
    public void doMedianBlur() {
        model.image.medianFilter();
        GUIFunctions.refresh();
    }

    @Override
    public void doUniformBlur() {
        model.image.uniformBlur();
        GUIFunctions.refresh();
    }

    @Override
    public void doChangeContrast(int contrastAmountNum) {
        model.image.adjustContrast(contrastAmountNum);
        GUIFunctions.refresh();
    }

    @Override
    public void doChangeBrightness(int brightnessAmountNum) {
        model.image.adjustBrightness(brightnessAmountNum);
        GUIFunctions.refresh();
    }

    @Override
    public void doLoadImage(BufferedImage i) {
        Raster r = i.getRaster();

        int width = i.getWidth();
        int height = i.getHeight();
        int[] pixels = r.getPixels(0, 0, width, height, (int[]) null);

        model.setImage(width, height, pixels);

        GUIFunctions.refresh();
    }

    @Override
    public void toggleBackgroundDisplay() {
        view.drawImage = !view.drawImage;
        GUIFunctions.refresh();
    }

    private void createMouseListener() {
        mouseListener = new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point2D p = new Point2D.Double();
                p.setLocation(e.getPoint());
                view.viewToWorld.transform(p, p);
                mouseInteractionHandler.down(p);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point2D p = new Point2D.Double();
                p.setLocation(e.getPoint());
                view.viewToWorld.transform(p, p);
                mouseInteractionHandler.up(p);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                Point2D p = new Point2D.Double();
                p.setLocation(e.getPoint());
                view.viewToWorld.transform(p, p);
                mouseInteractionHandler.click(p);
            }

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        };
    }

    private void createMouseMotionListener() {
        mouseMotionListener = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point2D p = new Point2D.Double();
                p.setLocation(e.getPoint());
                view.viewToWorld.transform(p, p);
                mouseInteractionHandler.drag(p);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Point2D p = new Point2D.Double();
                p.setLocation(e.getPoint());
                view.viewToWorld.transform(p, p);
                mouseInteractionHandler.move(p);
            }
        };
    }

    public Color getColor() {
        return color;
    }
}
