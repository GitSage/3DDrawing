package cs355.view;

import cs355.GUIFunctions;
import cs355.controller.Camera;
import cs355.model.drawing.*;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Triangle;
import cs355.model.drawing.Shape;
import cs355.model.image.FlexibleImage;
import cs355.model.scene.HouseModel;
import cs355.model.scene.Line3D;
import cs355.model.scene.WireFrame;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.Observable;

/**
 * Created by ben on 9/2/15.
 */
public class SimpleViewRefresher implements ViewRefresher{

    private CS355Drawing model;
    private Shape selected;
    private Color outlineColor;
    private Handle handle;
    private double zoomLevel;
    private double leftCoord;
    private double topCoord;

    private boolean model3D;
    private boolean displayBackground = true;

    private FlexibleImage imageCont;

    //private Drawer drawer;
    private WireFrame wireframe;
    private Camera camera;
    private ManualAffineTransform worldToView;
    private ManualAffineTransform viewToWorld;

    public SimpleViewRefresher(CS355Drawing model, FlexibleImage imageCont, double zoomLevel, double leftCorner, double topCorner){
        this.model = model;
        outlineColor = Color.RED;
        this.zoomLevel = zoomLevel;
        this.leftCoord = leftCorner;
        this.topCoord = topCorner;

        this.imageCont = imageCont;

        //drawer = new Drawer(this);
        wireframe = new HouseModel();
        camera = new Camera(0, 5, -20);
        worldToView = new ManualAffineTransform();
        viewToWorld = new ManualAffineTransform();
    }

    public void setZoomLevel(double z){
        this.zoomLevel = z;
    }

    public double getZoom(){
        return this.zoomLevel;
    }

    public Camera getCamera(){
        return camera;
    }

    public void setLeftCoord(double l){
        this.leftCoord = l;
        updateWorldToViewTransform();
    }

    public void setTopCoord(double t){
        this.topCoord = t;
        updateWorldToViewTransform();
    }

    public void toggleDraw3DMode(){
        this.model3D = !this.model3D;
    }

    private LineHandle[] lineHandles;

    public LineHandle[] getLineHandles() {
        return lineHandles;
    }

    public void setLineHandles(LineHandle[] lineHandles) {
        this.lineHandles = lineHandles;
    }

    public AffineTransform objectToView(Shape shape){
        AffineTransform o2v = new AffineTransform();
        AffineTransform o2w = new AffineTransform();
        AffineTransform w2v = new AffineTransform();

        // Mi = V Oi
        // First, convert object to world.
        double r = shape.getRotation();
        AffineTransform rotate = new AffineTransform(Math.cos(r), Math.sin(r), -Math.sin(r),  Math.cos(r), 0, 0);
        AffineTransform translate = new AffineTransform(1, 0, 0, 1, shape.getCenter().getX(), shape.getCenter().getY());
        o2w.concatenate(translate);
        o2w.concatenate(rotate);

        // Second, construct view matrix.
        AffineTransform translate2 = new AffineTransform(1, 0, 0, 1, -leftCoord, -topCoord);
        AffineTransform scale = new AffineTransform(zoomLevel, 0, 0, zoomLevel, 0, 0);
        w2v.concatenate(scale);
        w2v.concatenate(translate2);

        // Third, concatenate the two matrices.
        o2v.concatenate(w2v);
        o2v.concatenate(o2w);

        return o2v;
    }

    public AffineTransform objectToWorld(Shape shape){
        AffineTransform o2w = new AffineTransform();
        o2w.translate(shape.getCenter().getX(), shape.getCenter().getY());
        o2w.rotate(shape.getRotation());

        return o2w;
    }

    public AffineTransform viewToWorld(Point2D.Double pt){
        return null;
    }

    public Handle getHandle() {
        return handle;
    }

    public void setHandle(Handle handle) {
        this.handle = handle;
    }

    @Override
    public void refreshView(Graphics2D g2d) {
        System.out.println("Drawing image " + imageCont.getImage());
        if(displayBackground){
            g2d.drawImage(imageCont.getImage(), 0, 0, null);
        }
        drawAllShapesInCollection(g2d, model.getShapes());
        drawAllShapesInCollection(g2d, model.getShapesInProgress());
        drawSelected(g2d);

        if(model3D){
            drawWireFrame(g2d);
        }
    }

    public void setSelected(Shape selected){
        this.selected = selected;
        if(selected != null){
            if(selected instanceof Line){
                this.handle = null;
                this.lineHandles = new LineHandle[2];
                lineHandles[0] = new LineHandle(((Line)selected).getStart());
                lineHandles[1] = new LineHandle(((Line)selected).getEnd());
            }
            else{
                this.lineHandles = null;
                this.handle = new Handle(selected);
            }
        }
        else{
            this.lineHandles = null;
            this.handle = null;
        }
    }

    public Shape getSelected(){
        return selected;
    }

    private void drawSelected(Graphics2D g2d){
        // draw the rotation handle
        if(selected != null){
            // if it's a line, draw both handles
            if(selected instanceof Line){
                Point2D.Double start = ((Line) selected).getStart();
                Point2D.Double end = ((Line) selected).getEnd();
                drawHandleOutline(g2d, start.getX(), start.getY(), Handle.RADIUS, Handle.RADIUS, 0);
                drawHandleOutline(g2d, end.getX(), end.getY(), Handle.RADIUS, Handle.RADIUS, 0);
            }
            else{ // if it's not a line, draw just one rotation handle
                Point2D.Double center = handle.getCenter();
                // draw the handle
                drawHandleOutline(g2d, center.getX(), center.getY(), Handle.RADIUS, Handle.RADIUS, 0);
            }
        }

        if(selected instanceof Square){
            Square s = (Square) selected;
            drawSquareOutline(g2d, s);
        }
        else if(selected instanceof Rectangle){
            Rectangle s = (Rectangle) selected;
            drawRectOutline(g2d, s);
        }
        else if(selected instanceof Circle){
            Circle s = (Circle) selected;
            drawCircleOutline(g2d, s);
        }
        else if(selected instanceof Ellipse){
            Ellipse s = (Ellipse) selected;
            drawOvalOutline(g2d, s);
        }
        else if(selected instanceof Triangle){
            drawTriangleOutline(g2d, (Triangle) selected);
        }
    }

    private void drawAllShapesInCollection(Graphics2D g2d, Collection<Shape> coll){
        for(cs355.model.drawing.Shape s : coll){
            g2d.setColor(s.getColor());

            if(s instanceof Circle){
                drawCircle(g2d, (Circle) s);
            }
            else if(s instanceof Ellipse){
                drawEllipse(g2d, (Ellipse) s);
            }
            else if(s instanceof Line){
                Line line = (Line) s;
                g2d.setTransform(objectToView(line));

                int startx = (int)line.getStart().getX();
                int starty = (int)line.getStart().getY();
                int endx = (int)line.getEnd().getX();
                int endy = (int)line.getEnd().getY();

                g2d.drawLine(0, 0, endx - startx, endy - starty);
            }
            else if(s instanceof Rectangle){
                drawRect(g2d, (Rectangle) s);
            }
            else if(s instanceof Square){
                drawSquare(g2d, (Square)s);
            }
            else if(s instanceof Triangle){
                drawTriangle(g2d, (Triangle) s);
            }
        }
    }

    private AffineTransform getAffTrans(double x, double y, double r){
        AffineTransform o2w = new AffineTransform();
        o2w.translate(x, y);
        o2w.rotate(r);
        return o2w;
    }

    private void drawSquare(Graphics2D g2d, Square s){
        g2d.setTransform(objectToView(s));
        g2d.fillRect((int)-s.getSize()/2, (int)-s.getSize()/2, (int)s.getSize(), (int)s.getSize());
    }

    private void drawRect(Graphics2D g2d, cs355.model.drawing.Rectangle r){
        g2d.setTransform(objectToView(r));
        g2d.fillRect((int) -r.getWidth() / 2, (int) -r.getHeight() / 2, (int) r.getWidth(), (int) r.getHeight());
    }

    private void drawCircle(Graphics2D g2d, Circle c){
        double size = c.getRadius();
        g2d.setTransform(objectToView(c));
        g2d.fillOval((int) -size, (int) -size, (int) size * 2, (int) size * 2);
    }

    private void drawEllipse(Graphics2D g2d, Ellipse e){
        g2d.setTransform(objectToView(e));
        g2d.fillOval((int) -e.getWidth()/2, (int) -e.getHeight()/2, (int) e.getWidth(), (int) e.getHeight());
    }

    private void drawTriangle(Graphics2D g2d, Triangle t){
        g2d.setTransform(objectToView(t));
        int[] xPoints = {(int)t.getA().getX(), (int)t.getB().getX(), (int)t.getC().getX()};
        int[] yPoints = {(int)t.getA().getY(), (int)t.getB().getY(), (int)t.getC().getY()};
        g2d.fillPolygon(xPoints, yPoints, 3);
    }

    private void drawSquareOutline(Graphics2D g2d, Square s){
        g2d.setTransform(objectToView(s));
        g2d.drawRect((int) -s.getSize() / 2, (int) -s.getSize() / 2, (int) s.getSize(), (int) s.getSize());
    }

    private void drawRectOutline(Graphics2D g2d, Rectangle r){
        g2d.setColor(outlineColor);
        g2d.setTransform(objectToView(r));
        g2d.drawRect((int) -r.getWidth() / 2, (int) -r.getHeight() / 2, (int) r.getWidth(), (int) r.getHeight());
    }

    private void drawCircleOutline(Graphics2D g2d, Circle c){
        double size = c.getRadius();
        g2d.setTransform(objectToView(c));
        g2d.drawOval((int) -size, (int) -size, (int) size * 2, (int) size * 2);
    }

    private void drawOvalOutline(Graphics2D g2d, Ellipse e){
        g2d.setTransform(objectToView(e));
        g2d.drawOval((int) -e.getWidth() / 2, (int) -e.getHeight() / 2, (int) e.getWidth(), (int) e.getHeight());
    }

    private void drawTriangleOutline(Graphics2D g2d, Triangle t){
        g2d.setColor(outlineColor);
        g2d.setTransform(objectToView(t));
        int[] xPoints = {(int)t.getA().getX(), (int)t.getB().getX(), (int)t.getC().getX()};
        int[] yPoints = {(int)t.getA().getY(), (int)t.getB().getY(), (int)t.getC().getY()};
        g2d.drawPolygon(xPoints, yPoints, 3);
    }

    private void drawHandleOutline(Graphics2D g2d, double cx, double cy, double width, double height, double rotation){
        g2d.setColor(outlineColor);
        Ellipse handle = new Ellipse(outlineColor, new Point2D.Double(cx, cy), width, height);
        g2d.setTransform(objectToView(handle));
        g2d.drawOval((int) -(width/zoomLevel), (int) -(height/zoomLevel), (int) (width/zoomLevel) * 2, (int) (height/zoomLevel) * 2);
    }

    public void drawWireFrame(Graphics2D g2d){
        Matrix4 perspectiveMatrix = camera.getPerspectiveMatrix();

        g2d.setTransform(worldToView);
        g2d.setColor(Color.WHITE);

        Iterator<Line3D> lines = wireframe.getLines();
        while (lines.hasNext()) {
            System.out.println("Drawing line");
            Line3D line = lines.next();
            Vector4 start = new Vector4(line.start.x, line.start.y, line.start.z, 1);
            Vector4 end = new Vector4(line.end.x, line.end.y, line.end.z, 1);

            start.apply(perspectiveMatrix);
            end.apply(perspectiveMatrix);

            if (start.y < -start.w && end.y < -end.w ||
                    start.y > start.w && end.y > end.w ||
                    start.x < -start.w && end.x < -end.w ||
                    start.x > start.w && end.x > end.w ||
                    start.z < -start.w && end.z < -end.w ||
                    start.z > start.w && end.z > end.w) {
                continue;
            }

            start.divideW();
            end.divideW();

            g2d.drawLine((int) (start.x*-1024) + 1024, (int) (start.y*-1024) + 1024, (int) (end.x*-1024) + 1024, (int) (end.y*-1024) + 1024);
        }
    }

    public void updateWorldToViewTransform() {
        worldToView.setToIdentity();
        worldToView.translate(-leftCoord, -topCoord);
        worldToView.scale(zoomLevel, zoomLevel);

        viewToWorld.setToIdentity();
        viewToWorld.scale(1/zoomLevel, 1/zoomLevel);
        viewToWorld.translate(leftCoord, topCoord);
    }

    @Override
    public void update(Observable observable, Object o) {
        GUIFunctions.refresh();
    }

    public void toggleBackgroundDisplay() {
        displayBackground = !displayBackground;
    }
}
