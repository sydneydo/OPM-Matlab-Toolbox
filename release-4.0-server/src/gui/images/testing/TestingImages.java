package gui.images.testing;
import javax.swing.ImageIcon;

public class TestingImages
{

    // ********** Testing Images ***************
	private final static String basePath = "/gui/images/testing/";//Images.opmPath;
    public final static ImageIcon PLAY = loadImage("an_play.png");
    public final static ImageIcon BACKWARD = loadImage("an_backward.png");
    public final static ImageIcon FORWARD = loadImage("an_forward.png");
    public final static ImageIcon PAUSE = loadImage("an_pause.png");
    public final static ImageIcon CLOSE = loadImage("an_close.png");
    public final static ImageIcon ACTIVATE = loadImage("an_activate.png");
    public final static ImageIcon DEACTIVATE = loadImage("an_deactivate.png");
    public final static ImageIcon STOP = loadImage("an_stop.png");
    public final static ImageIcon SETTINGS = loadImage("an_settings.png");
    public final static ImageIcon ZOOM_IN = loadImage("an_zoomin.png");
    public final static ImageIcon ZOOM_OUT = loadImage("an_zoomout.png");

    private static ImageIcon loadImage(String file)
    {
        try
        {
            return new ImageIcon(TestingImages.class.getResource(basePath+file), "test");
        }
        catch (Exception e)
        {
            System.err.println("cannot load testing image "+file);

            return null;
        }
    }

}
