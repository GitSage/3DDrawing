package cs355.LWJGL;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Brennan Smith
 */
public class WireFrame implements Iterable<Line3D>
{
    public Iterator<Line3D> getLines()
    {
        return new ArrayList<Line3D>().iterator();
    }

    @Override
    public Iterator iterator() {
        return getLines();
    }
}
