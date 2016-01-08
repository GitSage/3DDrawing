package CS355.LWJGL;


//You might notice a lot of imports here.
//You are probably wondering why I didn't just import org.lwjgl.opengl.GL11.*
//Well, I did it as a hint to you.
//OpenGL has a lot of commands, and it can be kind of intimidating.
//This is a list of all the commands I used when I implemented my project.
//Therefore, if a command appears in this list, you probably need it.
//If it doesn't appear in this list, you probably don't.
//Of course, your milage may vary. Don't feel restricted by this list of imports.
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.util.Iterator;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3d;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.util.glu.GLU.gluPerspective;

/**
 *
 * @author Brennan Smith
 */
public class StudentLWJGLController implements CS355LWJGLController 
{
  
  //This is a model of a house.
  //It has a single method that returns an iterator full of Line3Ds.
  //A "Line3D" is a wrapper class around two Point2Ds.
  //It should all be fairly intuitive if you look at those classes.
  //If not, I apologize.
  private WireFrame model = new HouseModel();
    private CameraDescription cam = new CameraDescription();
    private boolean isOrtho;

  //This method is called to "resize" the viewport to match the screen.
  //When you first start, have it be in perspective mode.
  @Override
  public void resizeGL() 
  {
      int width = Display.getDisplayMode().getWidth();
      int height = Display.getDisplayMode().getHeight();

      glViewport(0, 0, width, height); // Reset The Current Viewport
      glMatrixMode(GL_PROJECTION); // Select The Projection Matrix
      glLoadIdentity(); // Reset The Projection Matrix
      gluPerspective(45.0f, ((float) width / (float) height), 0.1f, 100.0f); // Calculate The Aspect Ratio Of The Window
      glMatrixMode(GL_MODELVIEW); // Select The Modelview Matrix
      glLoadIdentity(); // Reset The Modelview Matrix

  }

    @Override
    public void update() 
    {
        
    }

    //This is called every frame, and should be responsible for keyboard updates.
    //An example keyboard event is captured below.
    //The "Keyboard" static class should contain everything you need to finish
    // this up.
    @Override
    public void updateKeyboard() 
    {
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){ // move left
            cam.x += 1;
            System.out.println("Left");
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_D)) // move right
        {
            cam.x -= 1;
            System.out.println("Right");
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_W)) // move forward
        {
            cam.z += 1;
            System.out.println("Forward");
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_S)) // move backward
        {
            cam.z -= 1;
            System.out.println("Back");
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_Q)) // turn left
        {
            cam.x_rot -= 1;
            System.out.println("Turn left");
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_E)) // turn right
        {
            cam.x_rot += 1;
            System.out.println("Turn right");
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_R)) // move up
        {
            cam.y -= 1;
            System.out.println("Up");
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_F)) // move down
        {
            cam.y += 1;
            System.out.println("Down");
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_H)) // return to home position and orientation
        {
            cam.reset();
            System.out.println("Home");
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_O)) // switch to orthographic projection
        {
            isOrtho = true;
            cam.reset();
            System.out.println("Orthographic");
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_P)) // switch to perspective projection
        {
            isOrtho = false;
            cam.reset();
            System.out.println("Perspective");
        }
    }

    //This method is the one that actually draws to the screen.
    @Override
    public void render() 
    {
        int width = Display.getDisplayMode().getWidth();
        int height = Display.getDisplayMode().getHeight();
        float aspect = (float)width/(float)height;

        //This clears the screen.
        glClear(GL_COLOR_BUFFER_BIT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity(); // reset the view
        if(isOrtho){
            glOrtho(-18, 18, -18, 18, 0.1f, 1000f);
        }
        else{
            gluPerspective(60, aspect, 0.1f, 9999999);
        }
        //glMatrixMode(GL_MODELVIEW);


        //Do your drawing here.
        glTranslatef(cam.x, cam.y, cam.z);
        glRotatef(cam.x_rot, 0, 1, 0);

        glColor3f(1, 0, 0);

        drawHouse();
        glTranslatef(15, 0, 0);
        drawHouse();
        glTranslatef(15, 0, 0);
        drawHouse();
        glTranslatef(15, 0, 0);
        drawHouse();
        glTranslatef(15, 0, 0);
        drawHouse();

        // draw the other side of the street
        glTranslatef(-60, 0, 30);
        glRotatef(180, 0, 1, 0);
        drawHouse();
        glTranslatef(-15, 0, 0);
        drawHouse();
        glTranslatef(-15, 0, 0);
        drawHouse();
        glTranslatef(-15, 0, 0);
        drawHouse();
        glTranslatef(-15, 0, 0);
        drawHouse();
    }

    private void drawHouse(){
        glPushMatrix();
        glBegin(GL_LINES);


        Iterator<Line3D> lines = model.getLines();
        while(lines.hasNext()){
            Line3D line = lines.next();
            glVertex3d(line.start.x, line.start.y, line.start.z);
            glVertex3d(line.end.x, line.end.y, line.end.z);
        }
        glEnd();
        glPopMatrix();
    }
    
}
