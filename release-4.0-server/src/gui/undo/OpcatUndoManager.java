package gui.undo;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoManager;

/**
 * A specialized undo manager for Opcat - allows to decide if a project can be
 * closed without data loss. Wehn a change happens in the system, each call for
 * <code>undoableEditHappened</code> is captured, and a variable
 * <code>noDataLoss</code> is set to false. When a save operation happens in
 * the system, the method <code>dataSaved()</code> can be called in order to
 * indicate that the project can be closed without data loss.
 * 
 * @author Eran Toch Created: 03/06/2004
 */
public class OpcatUndoManager extends UndoManager {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	 
	private boolean noDataLoss = true;

	/**
	 * Before redirecting the call to <code>super</code>, the method sets
	 * <code>noDataLoss</code> to false.
	 */
	public void undoableEditHappened(UndoableEditEvent e) {
		this.noDataLoss = false;
		super.undoableEditHappened(e);
	}

	/**
	 * The method is called whenever the project is saved and no data loss is
	 * expected if no changes were to happen to the system.
	 */
	public void dataSaved() {
		this.noDataLoss = true;
	}

	/**
	 * Indicated if it is possible to close the project without data loss. The
	 * method checks if <code>noDataLoss</code> is true <b>or</b> if
	 * <code>canUndo</code> returns false. If any of them is <codE>true</code>,
	 * the method returns <code>true</code>.
	 * 
	 * @return <code>true</code> if the project can be be closed without data
	 *         loss. <code>false</code> otherwise.
	 */
	public boolean canCloseProject() {
		if (this.noDataLoss || !this.canUndo()) {
			return true;
		}
		return false;
	}
}
