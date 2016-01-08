package cs355.model;

import cs355.model.drawing.CS355Drawing;
import cs355.model.drawing.Shape;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ben on 9/2/15.
 */
public class SimpleModel extends CS355Drawing {

    private List<Shape> shapes;
    private List<Shape> shapesInProgress;

    public SimpleModel(){
        this.shapes = new ArrayList<Shape>();
        this.shapesInProgress = new ArrayList<Shape>();
    }

    public List<Shape> getShapesInProgress(){
        return shapesInProgress;
    }

    public void addShapeInProgress(Shape s){
        quickNotify();
        shapesInProgress.add(s);
    }

    public void clearShapesInProgress(){
        quickNotify();
        shapesInProgress.clear();
    }

    public void quickNotify(){
        setChanged();
        notifyObservers();
    }


    @Override
    public Shape getShape(int index) {
        return null;
    }

    @Override
    public int addShape(Shape s) {
        if(s != null){
            shapes.add(s);
            quickNotify();
        }

        return 0;
    }

    @Override
    public void deleteShape(int index) {

    }

    @Override
    public void moveToFront(int index) {

    }

    @Override
    public void movetoBack(int index) {

    }

    @Override
    public void moveForward(int index) {

    }

    @Override
    public void moveBackward(int index) {

    }

    @Override
    public List<Shape> getShapes() {
        return shapes;
    }

    @Override
    public List<Shape> getShapesReversed() {
        return null;
    }

    @Override
    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
        quickNotify();
    }
}
