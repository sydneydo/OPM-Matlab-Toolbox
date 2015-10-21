package gui.images.standard;
import javax.swing.ImageIcon;

public class StandardImages
{


    private final static String basePath = "/gui/images/standard/";//Images.opmPath;
    
    
    
    
    // ********** Standard Images ***************

    public final static ImageIcon NEW = loadImage("new.png");
    public final static ImageIcon EMPTY = loadImage("empty.png");
    public final static ImageIcon OPEN = loadImage("open.png");
    public final static ImageIcon CLOSE = loadImage("close.png");
    public final static ImageIcon SAVE = loadImage("save.png");
    public final static ImageIcon SAVE_AS = loadImage("save-as.png");
    
    public final static ImageIcon META_CONNECT = loadImage("connect.png");
    public final static ImageIcon META_DISCONNECT = loadImage("disconnect.png");
    public final static ImageIcon CUT = loadImage("cut.png");
    public final static ImageIcon COPY = loadImage("copy.png");
    public final static ImageIcon CLONE = loadImage("clone.gif");
    public final static ImageIcon COPYFORMAT = loadImage("copyformat.png");
    public final static ImageIcon System_Icon = loadImage("system.png");
    public final static ImageIcon REPORT = loadImage("report.png");
    public final static ImageIcon PASTE = loadImage("paste.png");
    public final static ImageIcon UNDO = loadImage("undo.png");
    public final static ImageIcon REDO = loadImage("redo.png");
    public final static ImageIcon DELETE = loadImage("delete.png");
    public final static ImageIcon REQ = loadImage("req.png");
    public final static ImageIcon REFRESH = loadImage("refresh.png");
    public final static ImageIcon VALIDATION_Menu = loadImage("validation.png");
    public final static ImageIcon RENUMBER_OPDS = loadImage("renumber.png");
    public final static ImageIcon META_COLOR_DEF = loadImage("meta_color_def.png");
    public final static ImageIcon META_COLOR = loadImage("meta_color.png");
    public final static ImageIcon META_HIDE = loadImage("meta_hide.png");
    public final static ImageIcon META_SHOW = loadImage("meta_show.png");
    public final static ImageIcon META_INSERT = loadImage("meta_insert.png");
    public final static ImageIcon GRID_PANEL_CLOSE = loadImage("grid_panel_close.png");
    public final static ImageIcon GRID_PANEL_CLEAR = loadImage("grid_panel_clear.png");
    public final static ImageIcon FIT_TO_SIZE = loadImage("fit_to_size.png");
    public final static ImageIcon COMPLETE_LINKS = loadImage("complete_links.png");
    public final static ImageIcon COMPLETE_LINKS_NO_THING = loadImage("complete_links_no_things.png");
    
    public final static ImageIcon BACK = loadImage("back.png");
    public final static ImageIcon UP = loadImage("up.png");
    public final static ImageIcon DOWN = loadImage("down.png");
    public final static ImageIcon FORWARD = loadImage("forward.png");
    
    
    public final static ImageIcon ALIGN_VERTICAL_BOTTOM = loadImage("align-vertical-bottom.png"); 
    public final static ImageIcon ALIGN_VERTICAL_CENTER = loadImage("align-horizontal-center.png"); 
    public final static ImageIcon ALIGN_VERTICAL_TOP = loadImage("align-vertical-top.png"); 
    public final static ImageIcon ALIGN_HORIZONTAL_CENTER = loadImage("align-vertical-center.png"); 
    

    public final static ImageIcon HIDE_TOOLS = loadImage("showHideTools.gif");
    public final static ImageIcon ZOOM_IN = loadImage("zoomin.png");
    public final static ImageIcon ZOOM_OUT = loadImage("zoomout.png");
    public final static ImageIcon EXIT = loadImage("exit.png");

    public final static ImageIcon POINTER = loadImage("pointer.png");
    public final static ImageIcon PEN = loadImage("pen2.png");         /*****************************HIOTeam****************/
    public final static ImageIcon PENCIL = loadImage("pencil.png");         /*****************************HIOTeam****************/

    public final static ImageIcon HAND = loadImage("hand.png");
    public final static ImageIcon ANIMATE = loadImage("animate.png");
    
    public final static ImageIcon SHOW_APPR = loadImage("show_app.png");

    public final static ImageIcon CHECKED = loadImage("checked.gif");
    public final static ImageIcon UNCHECKED = loadImage("unchecked.gif");

    public final static ImageIcon PRINT = loadImage("print.png");
    public final static ImageIcon PREVIEW_CLOSE = loadImage("previewClose.gif");
    public final static ImageIcon PAGE_SETUP = loadImage("pageSetup.gif");
    public final static ImageIcon PRINTER_SETUP = loadImage("printerSetup.gif");
    public final static ImageIcon PRINT_PREVIEW = loadImage("preview.png");

    public final static ImageIcon PROPERTIES = loadImage("properties.png");
    public final static ImageIcon HELPBOOK = loadImage("helpbook.png");
    public final static ImageIcon DATAFORM = loadImage("dataform.png");
    public final static ImageIcon LIBRARIES = loadImage("libs.png");
    
    
    public final static ImageIcon SEARCH = loadImage("search.png");    
    
    
	/**
	 * An icon for representing validation.
	 * @author Eran Toch
	 */
	  public final static ImageIcon VALIDATION = loadImage("validate.png");
	  
	  /**
	   * An icon for representing meta-libraries
	   */
	  public final static ImageIcon Policies = loadImage("metalibs.png");
	  public final static ImageIcon Classification = loadImage("classification.png");
	  
	  /**
	   * An icon for representing import
	   */
	  public final static ImageIcon IMPORT = loadImage("import.png");

    protected static ImageIcon loadImage(String file)
    {
        try
        {
            return new ImageIcon(StandardImages.class.getResource(basePath+file), "test");
        }
        catch (Exception e)
        {
            System.err.println("cannot load standard image "+file);

            return null;
        }
    }

}
