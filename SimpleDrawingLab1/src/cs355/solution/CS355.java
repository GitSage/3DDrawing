package cs355.solution;

import cs355.GUIFunctions;
import cs355.controller.CS355Controller;
import cs355.controller.SimpleController;
import cs355.model.SimpleModel;
import cs355.model.drawing.CS355Drawing;
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

        CS355Drawing model = new SimpleModel();
        ViewRefresher view = new SimpleViewRefresher(model);
        CS355Controller controller = new SimpleController(model, view);

        model.addObserver(view);

        // Fill in the parameters below with your controller and view.
		GUIFunctions.createCS355Frame(controller, view);
        GUIFunctions.changeSelectedColor(Color.WHITE);


        GUIFunctions.refresh();
	}
}
