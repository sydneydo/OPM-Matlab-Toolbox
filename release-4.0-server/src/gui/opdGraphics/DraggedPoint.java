package gui.opdGraphics;

import exportedAPI.OpcatConstants;
import exportedAPI.RelativeConnectionPoint;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;
import gui.opdProject.OpdProject;
import gui.opdProject.StateMachine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

public class DraggedPoint extends BaseGraphicComponent {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	public DraggedPoint(OpdProject project) {
		super(project);

		this.setSize(8, 8);
	}

	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Object AntiAlias = RenderingHints.VALUE_ANTIALIAS_ON;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, AntiAlias);

		if (this.isSelected()) {
			g2.setColor(Color.white);
			g2.fillRect(this.getWidth() / 2 - 2, this.getHeight() / 2 - 2, 4, 4);
			g2.setColor(Color.black);
			g2.drawRect(this.getWidth() / 2 - 2, this.getHeight() / 2 - 2, 4, 4);

		}
		// else
		// {
		// g2.setColor(Color.black);
		// g2.drawOval(0,0,getWidth()-1,getHeight()-1);
		// }

	}

	public void setAbsolutesLocation(int x, int y) {
		super.setLocation(x, y);
	}

	public void callPropertiesDialog(int showTabs, int showButtons) {
	}

	public void callShowUrl() {
	}

	public boolean inMove(int pX, int pY) {
		if (this.contains(pX, pY)) {
			return true;
		}

		return false;
	}

	public boolean inResize(int pX, int pY) {
		return false;
	}

	public int whichBorder(int pX, int pY) {
		return OpcatConstants.NOT_IN_BORDER;
	}

	public boolean isInAdjacentArea(int x, int y) {
		return this.contains(x, y);
	}

	public Point getAbsoluteConnectionPoint(RelativeConnectionPoint pPoint) {
		return new Point(this.getWidth() / 2, this.getHeight() / 2);
	}

	public String toString() {
		return " X=" + this.getX() + " Y=" + this.getY() + ";";
	}

	public void showPopupMenu(int pX, int pY) {
	}

	protected void addObjects2Move() {
	}

	public void mousePressed(MouseEvent e) {
		if (StateMachine.isWaiting()) {
			return;
		}

		this.myProject.addSelection(this, true);
		super.mousePressed(e);
	}

}