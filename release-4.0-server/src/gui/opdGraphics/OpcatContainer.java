package gui.opdGraphics;

import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;
import gui.opdProject.Selection;

import java.awt.Container;
import java.util.Enumeration;

public interface OpcatContainer
{
	public Container getContainer();
	public BaseGraphicComponent[] graphicalSelectionComponents();
	public Enumeration getGraphicalSelection();
	public Selection getSelection();
	public SelectionRectangle getSelectionRectangle();
}
