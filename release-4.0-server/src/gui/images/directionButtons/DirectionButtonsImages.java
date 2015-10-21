package gui.images.directionButtons;
import javax.swing.ImageIcon;

public class DirectionButtonsImages
{
    private final static String basePath = "/gui/images/directionButtons/";//Images.opmPath;

    // ********** Misc Images ***************

    public final static ImageIcon BL = loadImage("bl.gif");
    public final static ImageIcon BLDN = loadImage("bldn.gif");
    public final static ImageIcon BM = loadImage("bm.gif");
    public final static ImageIcon BMDN = loadImage("bmdn.gif");
    public final static ImageIcon BR = loadImage("br.gif");
    public final static ImageIcon BRDN = loadImage("brdn.gif");
    public final static ImageIcon C = loadImage("c.gif");
    public final static ImageIcon CDN = loadImage("cdn.gif");
    public final static ImageIcon ML = loadImage("ml.gif");
    public final static ImageIcon MLDN = loadImage("mldn.gif");
    public final static ImageIcon MR = loadImage("mr.gif");

    public final static ImageIcon MRDN = loadImage("mrdn.gif");
    public final static ImageIcon TL = loadImage("tl.gif");
    public final static ImageIcon TLDN = loadImage("tldn.gif");
    public final static ImageIcon TM = loadImage("tm.gif");
    public final static ImageIcon TMDN = loadImage("tmdn.gif");
    public final static ImageIcon TR = loadImage("tr.gif");
    public final static ImageIcon TRDN = loadImage("trdn.gif");


    protected static ImageIcon loadImage(String file)
    {
        try
        {
            return new ImageIcon(DirectionButtonsImages.class.getResource(basePath+file), "test");
        }
        catch (Exception e)
        {
            System.err.println("cannot load direction buttons image "+file);

            return null;
        }
    }

}
