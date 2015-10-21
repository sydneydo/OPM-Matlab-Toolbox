package gui.opdGraphics;

import gui.opdProject.OpdProject;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

/**
 * this class is a variable in DrawingArea and OpdThing
 */

public class SelectionRectangle extends JComponent {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	public SelectionRectangle(OpdProject prj) {
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 5.0f, new float[] { 3 }, 0.0f));
		g2.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
	}
}
