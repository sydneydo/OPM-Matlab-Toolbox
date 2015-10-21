package gui.images.repository;
import javax.swing.ImageIcon;

public class RepositoryImages
{

    private final static String basePath = "/gui/images/repository/";//Images.opmPath;
    // ********** Repository Images ***************

    public final static ImageIcon OBJECT = loadImage("objectRM.gif");
    public final static ImageIcon OBJECT_MID = loadImage("objectRM_MID.png");
    public final static ImageIcon PROCESS = loadImage("processRM.gif");
    public final static ImageIcon PROCESS_MID = loadImage("processRM_MID.png");
    public final static ImageIcon OPD = loadImage("opdRM.png");
    public final static ImageIcon OPD_OPEN = loadImage("opdRM_open.png");
//    public final static ImageIcon PROJECT = loadImage("ProjectRM.gif");
//    public final static ImageIcon OPCAT_PROJECTS = loadImage("opcatProjectsRM.gif");
    public final static ImageIcon TREE = loadImage("tree.gif");
    public final static ImageIcon BOOK = loadImage("book.gif");
    public final static ImageIcon FOLDER_CLOSE = loadImage("folder_close.png");
    public final static ImageIcon FOLDER_OPEN = loadImage("folder_open.png");
    public final static ImageIcon FOLDER_EMPTY = loadImage("folder-empty.png");

    protected static ImageIcon loadImage(String file)
    {
        try
        {
            return new ImageIcon(RepositoryImages.class.getResource(basePath+file), "test");
        }
        catch (Exception e)
        {
            System.err.println("cannot load image "+file);

            return null;
        }
    }

}
