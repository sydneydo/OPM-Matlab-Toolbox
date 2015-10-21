package gui.undo;

import gui.opdProject.GenericTable;
import gui.opdProject.OpdProject;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.undo.AbstractUndoableEdit;

public class UndoableMoveResizeComponent extends AbstractUndoableEdit {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	private JComponent myComponent;

	private Point initialLocation, finalLocation;

	private Dimension initialSize, finalSize;

	double originalSize;

	OpdProject myProject;

	public UndoableMoveResizeComponent(JComponent component, Point iLocation,
			Dimension iSize, Point fLocation, Dimension fSize,
			OpdProject myProject) {
		this.myProject = myProject;
		this.myComponent = component;
		this.initialLocation = iLocation;
		this.initialSize = iSize;
		this.finalLocation = fLocation;
		this.finalSize = fSize;
		GenericTable config = myProject.getConfiguration();
		this.originalSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();

	}

	public String getPresentationName() {
		return "Move/Resize Component";
	}

	public void undo() {
		super.undo();

		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double factor = currentSize / this.originalSize;

		this.myComponent.setLocation((int) Math.round(this.initialLocation.getX()
				* factor), (int) Math.round(this.initialLocation.getY() * factor));
		this.myComponent.setSize((int) Math.round(this.initialSize.getWidth() * factor),
				(int) Math.round(this.initialSize.getHeight() * factor));
	}

	public void redo() {
		super.redo();

		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double factor = currentSize / this.originalSize;

		this.myComponent.setLocation(
				(int) Math.round(this.finalLocation.getX() * factor), (int) Math
						.round(this.finalLocation.getY() * factor));
		this.myComponent.setSize((int) Math.round(this.finalSize.getWidth() * factor),
				(int) Math.round(this.finalSize.getHeight() * factor));
	}
}
