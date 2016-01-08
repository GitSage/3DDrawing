package cs355.view;

import cs355.GUIFunctions;
import cs355.model.drawing.*;
import cs355.model.drawing.Shape;

import java.awt.*;
import java.util.Collection;
import java.util.Observable;

/**
 * Created by ben on 9/2/15.
 */
public class SimpleViewRefresher implements ViewRefresher{

    private CS355Drawing model;

    public SimpleViewRefresher(CS355Drawing model){
        this.model = model;
    }

    @Override
    public void refreshView(Graphics2D g2d) {
        drawAllShapesInCollection(g2d, model.getShapes());
        drawAllShapesInCollection(g2d, model.getShapesInProgress());
    }

    private void drawAllShapesInCollection(Graphics2D g2d, Collection<Shape> coll){
        for(cs355.model.drawing.Shape s : coll){
            g2d.setColor(s.getColor());

            if(s instanceof Circle){
                Circle circle = (Circle) s;
                int topLeftX = (int)(circle.getCenter().getX() - (circle.getRadius()));
                int topLeftY = (int)(circle.getCenter().getY() - (circle.getRadius()));

                g2d.fillOval(topLeftX,
                        topLeftY,
                        (int)circle.getRadius()*2,
                        (int)circle.getRadius()*2);
            }
            else if(s instanceof Ellipse){
                Ellipse ell = (Ellipse) s;
                int topLeftX = (int)(ell.getCenter().getX() - (ell.getWidth() / 2));
                int topLeftY = (int)(ell.getCenter().getY() - (ell.getHeight() / 2));

                g2d.fillOval(topLeftX,
                        topLeftY,
                        (int)ell.getWidth(),
                        (int)ell.getHeight());
            }
            else if(s instanceof Line){
                Line line = (Line) s;
                g2d.drawLine((int) line.getStart().getX(),
                        (int) line.getStart().getY(),
                        (int) line.getEnd().getX(),
                        (int) line.getEnd().getY());
            }
            else if(s instanceof cs355.model.drawing.Rectangle){
                cs355.model.drawing.Rectangle rect = (cs355.model.drawing.Rectangle) s;
                g2d.fillRect((int) rect.getUpperLeft().getX(),
                        (int) rect.getUpperLeft().getY(),
                        (int) rect.getWidth(),
                        (int) rect.getHeight());
            }
            else if(s instanceof Square){
                Square square = (cs355.model.drawing.Square) s;
                g2d.fillRect((int) square.getUpperLeft().getX(),
                        (int) square.getUpperLeft().getY(),
                        (int) square.getSize(),
                        (int) square.getSize());
            }
            else if(s instanceof Triangle){
                Triangle tri = (cs355.model.drawing.Triangle) s;
                int[] xPoints = {(int)tri.getA().getX(), (int)tri.getB().getX(), (int)tri.getC().getX()};
                int[] yPoints = {(int)tri.getA().getY(), (int)tri.getB().getY(), (int)tri.getC().getY()};

                g2d.fillPolygon(xPoints, yPoints, 3);
            }
            else{
                System.out.println("WARNING: Trying to draw object but not found!");
            }
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        GUIFunctions.refresh();
    }
}
