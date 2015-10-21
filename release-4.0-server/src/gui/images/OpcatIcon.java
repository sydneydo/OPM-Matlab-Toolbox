package gui.images;

import javax.swing.ImageIcon;

public class OpcatIcon extends ImageIcon implements Comparable<OpcatIcon> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ImageIcon myIcon;
	private String name;

	public OpcatIcon(ImageIcon icon, String name) {
		super();
		this.myIcon = icon;
		this.name = name;
	}

	public ImageIcon getMyIcon() {
		return myIcon;
	}

	public String getName() {
		return name;
	}

	@Override
	public int compareTo(OpcatIcon o) {
		return this.name.compareTo(o.getName());
	}
}
