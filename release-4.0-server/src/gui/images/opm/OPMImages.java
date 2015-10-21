package gui.images.opm;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class OPMImages {
	private final static String basePath = "/gui/images/opm/";// Images.opmPath;
	// ********** OPM Images ***************

	public final static ImageIcon OBJECT = loadImage("object.gif");
	public final static ImageIcon STATE = loadImage("state.gif");
	public final static ImageIcon PROCESS = loadImage("process.gif");

	public final static ImageIcon AGGREGATION = loadImage("ap.gif");
	public final static ImageIcon EXHIBITION = loadImage("fc.gif");
	public final static ImageIcon GENERALIZATION = loadImage("gs.gif");
	public final static ImageIcon INSTANTIATION = loadImage("ci.gif");
	public final static ImageIcon UNI_DIRECTIONAL = loadImage("unidirectional.gif");
	public final static ImageIcon BI_DIRECTIONAL = loadImage("bidirectional.gif");

	public final static ImageIcon AGENT_LINK = loadImage("agentLink.gif");
	public final static ImageIcon INSTRUMENT_LINK = loadImage("instrumentLink.gif");
	public final static ImageIcon RESULT_LINK = loadImage("resultLink.gif");
	public final static ImageIcon EFFECT_LINK = loadImage("effectLink.gif");
	public final static ImageIcon INSTRUMENT_EVENT_LINK = loadImage("instrumentEventLink.gif");
	public final static ImageIcon CONSUMPTION_EVENT_LINK = loadImage("consumptionEventLink.gif");
	public final static ImageIcon CONDITION_LINK = loadImage("conditionLink.gif");
	public final static ImageIcon EXCEPTION_LINK = loadImage("exceptionLink.gif");
	public final static ImageIcon INVOCATION_LINK = loadImage("invocationLink.gif");

	protected static ImageIcon loadImage(String file) {
		try {
			return new ImageIcon(OPMImages.class.getResource(basePath + file),
					"test");
		} catch (Exception e) {
			System.err.println("cannot load opm image " + file);

			return null;
		}
	}

}
