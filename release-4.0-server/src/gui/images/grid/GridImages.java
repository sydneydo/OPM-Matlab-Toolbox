package gui.images.grid;

import javax.swing.ImageIcon;

public class GridImages
{
    private final static String basePath = "/gui/images/grid/";//Images.opmPath;

    // ********** Browser Images ***************

    public final static ImageIcon FOLDER = loadImage("folder.gif");
    public final static ImageIcon FOLDEROPEN = loadImage("folderO.gif");
    public final static ImageIcon LEAF = loadImage("leaf.gif");
    protected static ImageIcon loadImage(String file)
    {
        try
        {
            return new ImageIcon(GridImages.class.getResource(basePath+file), "test");
        }
        catch (Exception e)
        {
            System.err.println("cannot load grid image "+file);

            return null;
        }
    }

}
