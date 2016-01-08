package cs355.solution;

import cs355.GUIFunctions;
import cs355.controller.CS355Controller;
import cs355.controller.SimpleController;
import cs355.model.SimpleModel;
import cs355.model.drawing.CS355Drawing;
import cs355.model.image.FlexibleImage;
import cs355.view.SimpleViewRefresher;
import cs355.view.ViewRefresher;

import java.awt.*;

/**
 * This is the main class. The program starts here.
 * Make you add code below to initialize your model,
 * view, and controller and give them to the app.
 */
public class CS355 {


	/**
	 * This is where it starts.
	 * @param args = the command line arguments
	 */
	public static void main(String[] args) {
        FlexibleImage img = new FlexibleImage();
        CS355Drawing model = new SimpleModel();
        double zoomLevel = 1;
        double leftCoord = 0;
        double topCoord = 0;
        ViewRefresher view = new SimpleViewRefresher(model, img, zoomLevel, leftCoord, topCoord);
        CS355Controller controller = new SimpleController((SimpleModel) model, (SimpleViewRefresher)view, img, zoomLevel,
                leftCoord, topCoord);


        model.addObserver(view);

        // Fill in the parameters below with your controller and view.
		GUIFunctions.createCS355Frame(controller, view);
        GUIFunctions.changeSelectedColor(Color.WHITE);
        int knobWidth = 512; // default viewing area size
        GUIFunctions.setVScrollBarKnob(knobWidth);
        GUIFunctions.setHScrollBarKnob(knobWidth);

        GUIFunctions.refresh();
	}
}
