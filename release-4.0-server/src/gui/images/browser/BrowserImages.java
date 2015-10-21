package gui.images.browser;
import javax.swing.ImageIcon;

public class BrowserImages
{
    private final static String basePath = "/gui/images/browser/";//Images.opmPath;

    // ********** Browser Images ***************

    public final static ImageIcon BACK = loadImage("back.gif");
    public final static ImageIcon FORWARD = loadImage("forward.gif");
    public final static ImageIcon GO = loadImage("go.gif");
    public final static ImageIcon SEARCH = loadImage("search.gif");
    public final static ImageIcon STOP = loadImage("stop.gif");
    protected static ImageIcon loadImage(String file)
    {
        try
        {
            return new ImageIcon(BrowserImages.class.getResource(basePath+file), "test");
        }
        catch (Exception e)
        {
            System.err.println("cannot load browser image "+file);

            return null;
        }
    }

}
