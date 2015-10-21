package gui.images.expose;

import java.awt.image.BufferedImage;
import gui.images.browser.BrowserImages;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ExposeImages {

	private final static String basePath = "/gui/images/expose/";

	public final static BufferedImage exposed_public = loadImage("EPB.png");
	public final static BufferedImage exposed_private = loadImage("EPR.png");
	public final static BufferedImage exposed_public_private = loadImage("E-PB-PR.png");

	public final static BufferedImage exposed_public_changed = loadImage("C-EPB.png");
	public final static BufferedImage exposed_private_changed = loadImage("C-EPR.png");
	public final static BufferedImage exposed_public_private_changed = loadImage("C-E-PB-PR.png");

	public final static BufferedImage public_pointer_private_exposed = loadImage("UPB-EPR.png");
	public final static BufferedImage public_pointer_public_exposed = loadImage("UPB-EPB.png");
	public final static BufferedImage public_pointer_private_exposed_public_exposed = loadImage("UPB-EPB-EPR.png");
	public final static BufferedImage public_pointer_private_exposed_changed = loadImage("C-UPB-EPR.png");
	public final static BufferedImage public_pointer_public_exposed_changed = loadImage("C-UPB-EPB.png");
	public final static BufferedImage public_pointer_private_exposed_public_exposed_changed = loadImage("C-UPB-EPB-EPR.png");
	public final static BufferedImage public_pointer_not_exposed = loadImage("UPB.png");

	public final static BufferedImage private_pointer_private_exposed = loadImage("UPR-EPR.png");
	public final static BufferedImage private_pointer_public_exposed = loadImage("UPR-EPB.png");
	public final static BufferedImage private_pointer_private_exposed_public_exposed = loadImage("UPR-EPB-EPR.png");
	public final static BufferedImage private_pointer_private_exposed_changed = loadImage("C-UPR-EPR.png");
	public final static BufferedImage private_pointer_public_exposed_changed = loadImage("C-UPR-EPB.png");
	public final static BufferedImage private_pointer_private_exposed_public_exposed_changed = loadImage("C-UPR-EPB-EPR.png");
	public final static BufferedImage private_pointer_not_exposed = loadImage("UPR.png");

	public final static BufferedImage template_pointer_private_exposed = loadImage("UT-EPR.png");
	public final static BufferedImage template_pointer_public_exposed = loadImage("UT-EPB.png");
	public final static BufferedImage template_pointer_private_exposed_public_exposed = loadImage("UT-EPB-EPR.png");
	public final static BufferedImage template_pointer_private_exposed_changed = loadImage("C-UT-EPR.png");
	public final static BufferedImage template_pointer_public_exposed_changed = loadImage("C-UT-EPB.png");
	public final static BufferedImage template_pointer_private_exposed_public_exposed_changed = loadImage("C-UT-EPB-EPR.png");
	public final static BufferedImage template_pointer_not_exposed = loadImage("UT.png");

	public final static BufferedImage unknown_pointer_private_exposed = loadImage("UINT-EPR.png");
	public final static BufferedImage unknown_pointer_public_exposed = loadImage("UINT-EPB.png");
	public final static BufferedImage unknown_pointer_private_exposed_public_exposed = loadImage("UINT-EPB-EPR.png");
	public final static BufferedImage unknown_pointer_private_exposed_changed = loadImage("C-UINT-EPR.png");
	public final static BufferedImage unknown_pointer_public_exposed_changed = loadImage("C-UINT-EPB.png");
	public final static BufferedImage unknown_pointer_private_exposed_public_exposed_changed = loadImage("C-UINT-EPB-EPR.png");
	public final static BufferedImage unknown_pointer_not_exposed = loadImage("UINT.png");

	public final static ImageIcon expose_list = getIcon("expose-list.png");

	protected static BufferedImage loadImage(String file) {
		try {
			return ImageIO
					.read(ExposeImages.class.getResource(basePath + file));
		} catch (Exception e) {
			System.err.println("cannot load browser image " + file);

			return null;
		}
	}

	protected static ImageIcon getIcon(String file) {
		try {
			return new ImageIcon(BrowserImages.class.getResource(basePath
					+ file), "test");
		} catch (Exception e) {
			System.err.println("cannot load browser image " + file);

			return null;
		}
	}

}
