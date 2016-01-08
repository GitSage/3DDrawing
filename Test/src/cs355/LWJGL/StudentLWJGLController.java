package cs355.LWJGL;


//You might notice a lot of imports here.
//You are probably wondering why I didn't just import org.lwjgl.opengl.GL11.*
//Well, I did it as a hint to you.
//OpenGL has a lot of commands, and it can be kind of intimidating.
//This is a list of all the commands I used when I implemented my project.
//Therefore, if a command appears in this list, you probably need it.
//If it doesn't appear in this list, you probably don't.
//Of course, your milage may vary. Don't feel restricted by this list of imports.
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3d;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class StudentLWJGLController implements CS355LWJGLController
{

    //This is a model of a house.
    //It has a single method that returns an iterator full of Line3Ds.
    //A "Line3D" is a wrapper class around two Point2Ds.
    //It should all be fairly intuitive if you look at those classes.
    //If not, I apologize.
    private WireFrame model = new HouseModel();

    private Vector3f position = new Vector3f(0, -5, -20);
    private float angle = 0;

    //This method is called to "resize" the viewport to match the screen.
    //When you first start, have it be in perspective mode.
    @Override
    public void resizeGL()
    {
        switchToPerspective();
    }

    private void switchToPerspective() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        //             fov  aspect  near  far
        gluPerspective(60,  4/3,    1,    100);
    }

    private void switchToOrtho() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        glOrtho(-12, 12, -12, 12, -100, 100);
    }

    @Override
    public void update() { }

    //This is called every frame, and should be responsible for keyboard updates.
    //An example keyboard event is captured below.
    //The "Keyboard" static class should contain everything you need to finish
    // this up.
    @Override
    public void updateKeyboard()
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            position.z += 0.1 * Math.cos(Math.toRadians(angle));
            position.x -= 0.1 * Math.sin(Math.toRadians(angle));
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.z -= 0.1 * Math.cos(Math.toRadians(angle));
            position.x += 0.1 * Math.sin(Math.toRadians(angle));
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            position.z -= 0.1 * Math.cos(Math.toRadians(angle+90));
            position.x += 0.1 * Math.sin(Math.toRadians(angle+90));
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            position.z += 0.1 * Math.cos(Math.toRadians(angle+90));
            position.x -= 0.1 * Math.sin(Math.toRadians(angle+90));
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            angle -= 1;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            angle += 1;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
            position.y -= 0.3;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
            position.y += 0.3;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
            switchToOrtho();
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
            switchToPerspective();
        }
    }

    //This method is the one that actually draws to the screen.
    @Override
    public void render()
    {
        glClear(GL_COLOR_BUFFER_BIT);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glRotatef(angle, 0, 1, 0);
        glTranslatef(position.x, position.y, position.z);

        glColor3f(1, 1, 1);

        for (Line3D line : model) {
            glBegin(GL_LINE_STRIP);
            glVertex3d(line.start.x, line.start.y, line.start.z);
            glVertex3d(line.end.x, line.end.y, line.end.z);
            glEnd();
        }
    }

}
