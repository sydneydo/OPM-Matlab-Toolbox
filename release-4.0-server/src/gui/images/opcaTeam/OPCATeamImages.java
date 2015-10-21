package gui.images.opcaTeam;
import javax.swing.ImageIcon;

public class OPCATeamImages
{
    private final static String basePath = "/gui/images/opcaTeam/";//Images.opmPath;

    // ********** Standard Images ***************

    //OPCATeam
    public final static ImageIcon PEOPLE = loadImage("people.gif");
    public final static ImageIcon PEOPLEsmall = loadImage("peopleSmall.jpg");
    public final static ImageIcon PEOPLEVerySmall = loadImage("peopleVerySmall.jpg");
    public final static ImageIcon MEMBER_CONNECTED = loadImage("user.jpg");
    public final static ImageIcon MEMBERSTREE = loadImage("group.jpg");
    public final static ImageIcon MEMBER_NOT_CONNECTED = loadImage("userNotConnected.jpg");
    public final static ImageIcon CONTROL_PANEL = loadImage("controlPanel.jpg");
    public final static ImageIcon DOCUMENT = loadImage("edit.jpg");
    public final static ImageIcon LOGIN = loadImage("login.jpg");
    public final static ImageIcon LOGOUT = loadImage("logout.jpg");
    public final static ImageIcon SESSION = loadImage("session.jpg");
    public final static ImageIcon MODEL = loadImage("model.jpg");
    public final static ImageIcon USER = loadImage("myUser.jpg");
    public final static ImageIcon COMMIT = loadImage("commit.jpg");
    public final static ImageIcon WORKGROUP = loadImage("workgroup.jpg");
    public final static ImageIcon PERMITHANDLE = loadImage("permitHandle.jpg");
    public final static ImageIcon GETTOKEN = loadImage("token.jpg");
    public final static ImageIcon RETURNTOKEN = loadImage("returnToken.jpg");
    public final static ImageIcon SAVE = loadImage("save.jpg");

    protected static ImageIcon loadImage(String file)
    {
        try
        {
            return new ImageIcon(OPCATeamImages.class.getResource(basePath+file), "test");
        }
        catch (Exception e)
        {
            System.err.println("cannot load misc image "+file);

            return null;
        }
    }

}
