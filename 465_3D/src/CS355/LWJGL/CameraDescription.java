package CS355.LWJGL;

/**
 * Created by benja_000 on 11/12/2015.
 */
public class CameraDescription {
	public float x;
	public float y;
	public float z;
	public float x_rot;

	public CameraDescription(){
		reset();
	}

	public void reset(){
		x = -15;
		y = -5;
		z = -30;
		x_rot = 90;
	}
}
