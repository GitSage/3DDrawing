package CS355.LWJGL;

/**
 *
 * @author Brennan Smith
 */
public class CS355LWJGL 
{
    
    public static void main(String[] args)
  {
    CS355.LWJGL.LWJGLSandbox main = null;
    try 
    {
      main = new LWJGLSandbox();
      CS355LWJGLController cont = new StudentLWJGLController();
      main.create(cont);
      main.run();
    }
    catch(Exception ex) 
    {
      ex.printStackTrace();
    }
    finally {
      if(main != null) 
      {
        main.destroy();
      }
    }
  }
}
