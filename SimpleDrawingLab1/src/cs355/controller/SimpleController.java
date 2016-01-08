package cs355.controller;

import cs355.GUIFunctions;
import cs355.model.SimpleModel;
import cs355.model.drawing.*;
import cs355.model.drawing.Shape;
import cs355.view.ViewRefresher;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ben on 9/2/15.
 */
public class SimpleController implements CS355Controller {

    private CS355Drawing model;
    private List<cs355.model.drawing.Shape> shapes;
    private ViewRefresher view;

    private ShapeEnum currButton;
    private ShapeFactory shapeFactory;

    public SimpleController(CS355Drawing model, ViewRefresher view){
        this.model = model;
        this.shapes = model.getShapes();
        this.view = view;
        this.shapeFactory = new ShapeFactory((SimpleModel)model);
    }

    @Override
    public void colorButtonHit(Color c) {
        shapeFactory.setColor(c);
        GUIFunctions.changeSelectedColor(c);
    }

    @Override
    public void lineButtonHit() {
        GUIFunctions.printf("Place line first point");

        this.currButton = ShapeEnum.LINE;
        shapeFactory.setShapeType(ShapeEnum.LINE);
    }

    @Override
    public void squareButtonHit() {
        GUIFunctions.printf("Place square first corner point");

        this.currButton = ShapeEnum.SQUARE;
        shapeFactory.setShapeType(ShapeEnum.SQUARE);

    }

    @Override
    public void rectangleButtonHit() {
        GUIFunctions.printf("Place rectangle first corner point");

        this.currButton = ShapeEnum.RECTANGLE;
        shapeFactory.setShapeType(ShapeEnum.RECTANGLE);

    }

    @Override
    public void circleButtonHit() {
        GUIFunctions.printf("Place circle center point");
        this.currButton = ShapeEnum.CIRCLE;
        shapeFactory.setShapeType(ShapeEnum.CIRCLE);

    }

    @Override
    public void ellipseButtonHit() {
        GUIFunctions.printf("Place ellipse center point");
        this.currButton = ShapeEnum.ELLIPSE;
        shapeFactory.setShapeType(ShapeEnum.ELLIPSE);
    }

    @Override
    public void triangleButtonHit() {
        GUIFunctions.printf("Place triangle first point");
        this.currButton = ShapeEnum.TRIANGLE;
        shapeFactory.setShapeType(ShapeEnum.TRIANGLE);
    }

    @Override
    public void selectButtonHit() {

    }

    @Override
    public void zoomInButtonHit() {

    }

    @Override
    public void zoomOutButtonHit() {

    }

    @Override
    public void hScrollbarChanged(int value) {

    }

    @Override
    public void vScrollbarChanged(int value) {

    }

    @Override
    public void openScene(File file) {

    }

    @Override
    public void toggle3DModelDisplay() {

    }

    @Override
    public void keyPressed(Iterator<Integer> iterator) {

    }

    @Override
    public void openImage(File file) {

    }

    @Override
    public void saveImage(File file) {
    }

    @Override
    public void toggleBackgroundDisplay() {

    }

    @Override
    public void saveDrawing(File file) {
        model.save(file);
    }

    @Override
    public void openDrawing(File file) {
        model.open(file);
    }

    @Override
    public void doDeleteShape() {

    }

    @Override
    public void doEdgeDetection() {

    }

    @Override
    public void doSharpen() {

    }

    @Override
    public void doMedianBlur() {

    }

    @Override
    public void doUniformBlur() {

    }

    @Override
    public void doGrayscale() {

    }

    @Override
    public void doChangeContrast(int contrastAmountNum) {

    }

    @Override
    public void doChangeBrightness(int brightnessAmountNum) {

    }

    @Override
    public void doMoveForward() {

    }

    @Override
    public void doMoveBackward() {

    }

    @Override
    public void doSendToFront() {

    }

    @Override
    public void doSendtoBack() {

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if(currButton == ShapeEnum.TRIANGLE){
            Shape result = shapeFactory.click(pointToDouble(mouseEvent.getPoint()));
            if(result != null){ // triangle has been completed. Add it to the list of shapes.
                model.addShape(result);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        // Ignore mouse press event for triangle
        if(currButton == ShapeEnum.TRIANGLE){
            return;
        }
        else{
            shapeFactory.press(pointToDouble(mouseEvent.getPoint()));
        }

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        // Ignore mouse release event for triangle
        if(currButton == ShapeEnum.TRIANGLE){
            return;
        } else{
            shapeFactory.release(pointToDouble(mouseEvent.getPoint()));
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
        shapeFactory.drag(pointToDouble(mouseEvent.getPoint()));
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }

    private Point2D.Double pointToDouble(Point p){
        return new Point2D.Double( p.getX(), p.getY());
    }
}
