package cs355.controller;

import cs355.GUIFunctions;
import cs355.model.SimpleModel;
import cs355.model.drawing.Line;
import cs355.model.drawing.LineHandle;
import cs355.model.drawing.Shape;
import cs355.model.image.FlexibleImage;
import cs355.view.SimpleViewRefresher;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ben on 9/2/15.
 */
public class SimpleController implements CS355Controller {

    private SimpleModel model;
    private List<cs355.model.drawing.Shape> shapes;
    private SimpleViewRefresher view;
    private double deltaX;
    private double deltaY;
    private boolean translating;
    private Shape rotatingShape;
    private LineHandle lineHandle;

    private FlexibleImage image;

    // lab 3
    private double zoomLevel; // 1 = 512x512 at initialization
    private double topCoord;
    private double leftCoord;

    private ButtonEnum currButton;
    private ShapeFactory shapeFactory;

    public SimpleController(SimpleModel model, SimpleViewRefresher view, FlexibleImage image, double zoomLevel,
                            double topCoord, double leftCoord){
        this.model = model;
        this.shapes = model.getShapes();
        this.view = view;
        this.shapeFactory = new ShapeFactory(model);
        this.currButton = ButtonEnum.SELECT;

        this.zoomLevel = zoomLevel;
        this.topCoord = topCoord;
        this.leftCoord = leftCoord;

        this.image = image;
    }

    @Override
    public void colorButtonHit(Color c) {
        shapeFactory.setColor(c);
        if(view.getSelected() != null){
            view.getSelected().setColor(c);
            model.quickNotify();
        }
        GUIFunctions.changeSelectedColor(c);
    }

    @Override
    public void lineButtonHit() {
        GUIFunctions.printf("Place line first point");

        this.currButton = ButtonEnum.LINE;
        shapeFactory.setShapeType(ButtonEnum.LINE);
    }

    @Override
    public void squareButtonHit() {
        GUIFunctions.printf("Place square first corner point");

        this.currButton = ButtonEnum.SQUARE;
        shapeFactory.setShapeType(ButtonEnum.SQUARE);

    }

    @Override
    public void rectangleButtonHit() {
        GUIFunctions.printf("Place rectangle first corner point");

        this.currButton = ButtonEnum.RECTANGLE;
        shapeFactory.setShapeType(ButtonEnum.RECTANGLE);

    }

    @Override
    public void circleButtonHit() {
        GUIFunctions.printf("Place circle center point");
        this.currButton = ButtonEnum.CIRCLE;
        shapeFactory.setShapeType(ButtonEnum.CIRCLE);
    }

    @Override
    public void ellipseButtonHit() {
        GUIFunctions.printf("Place ellipse center point");
        this.currButton = ButtonEnum.ELLIPSE;
        shapeFactory.setShapeType(ButtonEnum.ELLIPSE);
    }

    @Override
    public void triangleButtonHit() {
        GUIFunctions.printf("Place triangle first point");
        this.currButton = ButtonEnum.TRIANGLE;
        shapeFactory.setShapeType(ButtonEnum.TRIANGLE);
    }

    @Override
    public void selectButtonHit() {
        GUIFunctions.printf("Select a shape");
        this.currButton = ButtonEnum.SELECT;
    }

    @Override
    public void zoomInButtonHit() {
        double oldSize = zoomLevel * 512;
        if(zoomLevel < 4){
            if(zoomLevel >= 1){
                zoomLevel += 1.0;
            }
            else{
                zoomLevel += 1.0/4;
            }
        }

        // set new size of knobs
        GUIFunctions.setHScrollBarKnob((int)(1/zoomLevel * 512));
        GUIFunctions.setVScrollBarKnob((int)(1/zoomLevel * 512));

        // set new positions of knobs
        double newSize = zoomLevel * 512;
        leftCoord += (newSize - oldSize) / 2;
        topCoord += (newSize - oldSize) / 2;
        GUIFunctions.setHScrollBarPosit((int)leftCoord);
        GUIFunctions.setVScrollBarPosit((int)topCoord);

        // update the view
        view.setZoomLevel(zoomLevel);
        view.setLeftCoord(leftCoord);
        view.setTopCoord(topCoord);

        GUIFunctions.setZoomText(zoomLevel);
        model.quickNotify();
    }

    @Override
    public void zoomOutButtonHit() {
        double oldSize = zoomLevel * 512;
        if(zoomLevel > 1.0/4){
            if(zoomLevel > 1){
                zoomLevel -= 1.0;
            }
            else{
                zoomLevel -= 1.0/4;
            }
        }

        // set new size of knob

        GUIFunctions.setHScrollBarKnob((int)(1/zoomLevel * 512));
        GUIFunctions.setVScrollBarKnob((int)(1/zoomLevel * 512));

        // set new position of top left corner (and knobs)
        double newSize = zoomLevel * 512;
        leftCoord += (newSize - oldSize) / 2;
        topCoord += (newSize - oldSize) / 2;

        if(leftCoord <= 0){
            leftCoord = 0;
        }
        if(topCoord <= 0){
            topCoord = 0;
        }
        if(leftCoord + zoomLevel * 512 > 2024){
            leftCoord = 2024 - zoomLevel * 512;
        }
        if(topCoord + zoomLevel * 512 > 2024){
            topCoord = 2024 - zoomLevel * 512;
        }
        GUIFunctions.setHScrollBarPosit((int)leftCoord);
        GUIFunctions.setVScrollBarPosit((int) topCoord);

        // update the view
        view.setZoomLevel(zoomLevel);
        view.setLeftCoord(leftCoord);
        view.setTopCoord(topCoord);

        GUIFunctions.setZoomText(zoomLevel);
        model.quickNotify();
    }

    @Override
    public void hScrollbarChanged(int value) {
        leftCoord = value;
        view.setLeftCoord(value);

        model.quickNotify();
    }

    @Override
    public void vScrollbarChanged(int value) {
        topCoord = value;
        view.setTopCoord(value);
        model.quickNotify();
    }

    @Override
    public void openScene(File file) {

    }

    @Override
    public void toggle3DModelDisplay() {
        view.toggleDraw3DMode();
        GUIFunctions.refresh();
    }

    @Override
    public void keyPressed(Iterator<Integer> iterator) {

    }

    @Override
    public void openImage(File file) {
        System.out.println("Loading image " + file.getAbsolutePath());
        image.open(file);
        GUIFunctions.refresh();
    }

    @Override
    public void saveImage(File file) {
        image.save(file);
    }

    @Override
    public void toggleBackgroundDisplay() {
        view.toggleBackgroundDisplay();
        GUIFunctions.refresh();
    }

    @Override
    public void saveDrawing(File file) {
        model.save(file);
        GUIFunctions.printf("Saved drawing as " + file.getName());
    }

    @Override
    public void openDrawing(File file) {
        model.open(file);
        view.setSelected(null);
        model.quickNotify();
        GUIFunctions.printf("Opened drawing " + file.getName());
    }

    @Override
    public void doDeleteShape() {
        // sanity check
        if(view.getSelected() == null){
            return;
        }
        // get the number of the shape
        List<Shape> shapes = model.getShapes();
        int index = shapes.indexOf(view.getSelected());

        shapes.remove(index);
        view.setSelected(null);
        model.quickNotify();
    }

    @Override
    public void doEdgeDetection() {
        image.edgeDetection();
    }

    @Override
    public void doSharpen() {
        image.sharpen();
    }

    @Override
    public void doMedianBlur() {
        image.medianBlur();
    }

    @Override
    public void doUniformBlur() {
        image.uniformBlur();
    }

    @Override
    public void doGrayscale() {
        image.grayscale();
    }

    @Override
    public void doChangeContrast(int contrastAmountNum) {
        image.contrast(contrastAmountNum);
        GUIFunctions.refresh();
    }

    @Override
    public void doChangeBrightness(int brightnessAmountNum) {
        image.brightness(brightnessAmountNum);
        GUIFunctions.refresh();
    }

    @Override
    public void doMoveForward() {
        // sanity check
        if(view.getSelected() == null){
            return;
        }
        // get the number of the shape
        List<Shape> shapes = model.getShapes();
        int index = shapes.indexOf(view.getSelected());

        // if it's already on top, do nothing
        if(index == shapes.size() - 1){
            return;
        }

        // swap the two items
        Shape temp = shapes.get(index + 1);
        shapes.set(index + 1, view.getSelected());
        shapes.set(index, temp);
        model.quickNotify();
    }

    @Override
    public void doMoveBackward() {
        // sanity check
        if(view.getSelected() == null){
            return;
        }
        // get the number of the shape
        List<Shape> shapes = model.getShapes();
        int index = shapes.indexOf(view.getSelected());

        // if it's already on the bottom, do nothing
        if(index == 0){
            return;
        }

        // swap the two items
        Shape temp = shapes.get(index - 1);
        shapes.set(index - 1, view.getSelected());
        shapes.set(index, temp);
        model.quickNotify();
    }

    @Override
    public void doSendToFront() {
        // sanity check
        if(view.getSelected() == null){
            return;
        }
        // get the number of the shape
        List<Shape> shapes = model.getShapes();
        int index = shapes.indexOf(view.getSelected());

        // swap the two items
        Shape temp = shapes.get(shapes.size()-1);
        shapes.set(shapes.size()-1, view.getSelected());
        shapes.set(index, temp);
        model.quickNotify();
    }

    @Override
    public void doSendtoBack() {
        // sanity check
        if(view.getSelected() == null){
            return;
        }
        // get the number of the shape
        List<Shape> shapes = model.getShapes();
        int index = shapes.indexOf(view.getSelected());

        // swap the two items
        Shape temp = shapes.get(0);
        shapes.set(0, view.getSelected());
        shapes.set(index, temp);
        model.quickNotify();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        switch(currButton){
            case TRIANGLE:
                Shape result = shapeFactory.click(mouseEventToPoint2D(mouseEvent.getPoint()));
                if(result != null){ // triangle has been completed. Add it to the list of shapes.
                    model.addShape(result);
                }
                break;
            case SELECT:
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        Point2D.Double pt = mouseEventToPoint2D(mouseEvent.getPoint());

        // Ignore mouse press event for triangle
        switch(currButton){
            case SELECT:
                // select the shape on top and inform the View which shape is selected
                boolean foundShape = false;

                // check if we have selected a rotation handle
                Shape handle = view.getHandle();
                if(handle != null && handle.pointInShape(mouseEventToPoint2D(mouseEvent.getPoint()), 4)){
                    GUIFunctions.printf("Selected rotation handle.");
                    rotatingShape = view.getSelected();
                    foundShape = true;
                }

                // check if we have selected a line handle
                if(!foundShape ){
                    LineHandle[] lineHandles = view.getLineHandles();
                    if(lineHandles != null){
                        if(lineHandles[0].pointInShape(pt, 0)){
                            lineHandle = lineHandles[0];
                            GUIFunctions.printf("Selected first line handle");
                            foundShape = true;
                        }
                        else if(lineHandles[1].pointInShape(pt, 0)){
                            lineHandle = lineHandles[1];
                            GUIFunctions.printf("Selected second line handle");
                            foundShape = true;
                        }
                    }
                }

                // check if we have clicked over the currently selected shape
                if(view.getSelected() != null && view.getSelected().pointInShape(pt, 4)){
                    foundShape = true;
                }

                if(!foundShape){
                    // check if we have selected a shape
                    shapes = model.getShapes();
                    for(int i = shapes.size()-1; i >= 0; i--){
                        Shape shape = shapes.get(i);
                        if(shape.pointInShape(pt, 4 / zoomLevel)){
                            rotatingShape = null;
                            GUIFunctions.printf("Selected shape " + i);
                            GUIFunctions.changeSelectedColor(shape.getColor());
                            view.setSelected(shape);
                            foundShape = true;
                            break;
                        }
                    }
                }

                if(!foundShape){
                    view.setSelected(null);
                    GUIFunctions.printf("No shape selected");
                }
                model.quickNotify();
                break;

            case TRIANGLE:
                break;

            default:
                shapeFactory.press(pt);
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        // Ignore mouse release event for triangle
        rotatingShape = null;
        lineHandle = null;
        switch(currButton){
            case TRIANGLE:
                break;
            case SELECT:
                translating = false;
            case CIRCLE:
            case ELLIPSE:
            case RECTANGLE:
            case SQUARE:
            case LINE:
                shapeFactory.release(mouseEventToPoint2D(mouseEvent.getPoint()));
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        Point2D.Double pt = mouseEventToPoint2D(mouseEvent.getPoint());
        switch(currButton){
            case SELECT:
                Shape s = view.getSelected();
                // if a rotation handle is pressed
                if(rotatingShape != null){
                    // find the closest point on the circle around the object
                    Point2D.Double circlePoint = s.getClosestPointToCircle(pt);

                    // get the angle of this point from vertical
                    double deltaX = circlePoint.getX() - s.getCenter().getX();
                    double deltaY = circlePoint.getY() - s.getCenter().getY();
                    double angle = Math.PI - Math.atan2(deltaX, deltaY);
                    GUIFunctions.printf("New angle: " + angle);
                    s.setRotation(angle);
                }
                // if a line handle is selected
                else if(lineHandle != null){
                    lineHandle.setCenter(pt);
                }
                else{  // no handle was selected, we are translating
                    if(s == null){
                        break;
                    }
                    Point2D p = mouseEventToPoint2D(mouseEvent.getPoint());
                    if(!translating){ // record the initial distance between the point and the object's center
                        deltaX = s.getCenter().getX() - p.getX();
                        deltaY = s.getCenter().getY() - p.getY();
                    }
                    translating = true; // initial distance recorded

                    // calculate new distance
                    double newX = p.getX() + deltaX;
                    double newY = p.getY() + deltaY;
                    double saveCenterX = s.getCenter().getX();
                    double saveCenterY = s.getCenter().getY();
                    Point2D.Double newStart = new Point2D.Double(newX, newY);
                    s.setCenter(newStart);

                    if(s instanceof Line){
                        Line l = (Line) s;
                        double dx = l.getEnd().getX() - saveCenterX;
                        double dy = l.getEnd().getY() - saveCenterY;
                        Point2D.Double newEnd = new Point2D.Double(dx + newX, dy + newY);
                        l.setEnd(newEnd);
                    }
                }
                model.quickNotify();
                break;
            case CIRCLE:
            case ELLIPSE:
            case RECTANGLE:
            case SQUARE:
            case LINE:
                shapeFactory.drag(mouseEventToPoint2D(mouseEvent.getPoint()));
                break;
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }

    private Point2D.Double mouseEventToPoint2D(Point p){
        Point2D.Double pt = new Point2D.Double(p.getX(), p.getY());

        // Convert from view to world
        AffineTransform v2w = new AffineTransform();
        AffineTransform scale = new AffineTransform(1/zoomLevel, 0, 0, 1/zoomLevel, 0, 0);
        AffineTransform translate = new AffineTransform(1, 0, 0, 1, leftCoord, topCoord);
        v2w.concatenate(translate);
        v2w.concatenate(scale);

        v2w.transform(pt, pt);

        return pt;
    }
}
