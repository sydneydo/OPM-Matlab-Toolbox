package gui.opdGraphics;
import java.awt.Container;
import java.awt.Point;

import exportedAPI.RelativeConnectionPoint;

/**
 *  Specifies common interface for all OPD graphic components excluding lines
 */
public interface Connectable
{
/**
 *  @return A point of connection according to <code>pSide</code> and <code>parem</code> arguments
 *  @param <code>pSide</code> -- the side of OPD graphic component.
 *  Possible values are defined as constants in file - OpdBaseComponent.
 *  @param <code>param</code> -- Float number between 0 and 1 that means
 *  which point (relatively) is connection point.
 *  In order to get absolute point of connection(in coordinates of connected object)
 *  just multiply the length of connection side by this number.
 */
	public Point getAbsoluteConnectionPoint(RelativeConnectionPoint cPoint);
	public int getX();
	public int getY();
	public int getActualWidth();
	public int getActualHeight();
    public boolean isInAdjacentArea(int x, int y);
    public Container getParent();
}
