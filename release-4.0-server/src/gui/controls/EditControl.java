package gui.controls;

import java.awt.Point;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.undo.CannotRedoException;

import gui.Opcat2;
import gui.opdGraphics.GraphicsUtils;
import gui.opdGraphics.opdBaseComponents.OpdThing;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.opdProject.StateMachine;
import gui.projectStructure.Entry;
import gui.projectStructure.Instance;
import gui.projectStructure.ThingInstance;
import gui.util.OpcatLogger;

/**
 * Provides methods for controling editing actions.
 * 
 * @author Eran Toch
 */
public class EditControl {

	public final static int CUT = 300;

	public final static String CUT_PENDING_MSG = "Finish the Pending Cut operation first";

	/**
	 * The single instance of this class.
	 */
	private static EditControl instance = null;

	private Opcat2 myOpcat = null;

	//private GuiControl myGuiControl = GuiControl.getInstance();

	/**
	 * A private constructor, exists only to defeat instantiation.
	 * 
	 */
	private EditControl() {
	}

	/**
	 * Returns the singleton instance of the class. Initiaits it if it does not
	 * exist already, or creates a new one if it does.
	 * 
	 * @return
	 */
	public static EditControl getInstance() {
		if (instance == null) {
			instance = new EditControl();
		}
		return instance;
	}

	/**
	 * Sets a reference to Opcat. This method must be called before other
	 * methods could be called.
	 * 
	 * @param opcat
	 */
	public void setOpcat(Opcat2 opcat) {
		this.myOpcat = opcat;
	}

	public OpdProject getCurrentProject() {
		if (this.myOpcat == null) {
			OpcatLogger.logError("EditControl was not initiated by Opcat2");
		}
		return Opcat2.getCurrentProject();
	}

	/**
	 * Sets the CHECKED image on the <code>setEnableDraggingButton</code> menu
	 * item.
	 * 
	 * @param doEnable
	 *            If <code>true</code>, the option would be displayed as
	 *            <code>CHECKED</code>. Otherwise, it would be displayed as
	 *            <code>EMPTY</code>.
	 */
	public void enableDragging(boolean doEnable) {
		// Merge Remarked by Eran Toch
		// myOpcat.enableDragging(doEnable);
	}

	/**
	 * 
	 * @return Whether a project is currently open. Uses
	 *         {@link	Opcat2#isProjectWasOpened()}in order to retrieve an
	 *         answer.
	 */
	public boolean isProjectOpened() {
		FileControl file = FileControl.getInstance();
		return file.isProjectOpened();
	}

	/**
	 * Updates a logical change in the diagram refreshes the OPl and the
	 * repository.
	 */
	public void updateLogicalStructureChange() {
		Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
	}
	
	public void updateLogicalStructureChange(Opd opd) {
		Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
	}	

	/**
	 * Performs an undo operation - calls
	 * <code>Opcat2.getUndoManager().undo()</code>, handle errors, toggles
	 * the undo/redo buttons and updates the structural change.
	 */
	public void undo() {
		try {
			Opcat2.getUndoManager().undo();
		} catch (CannotRedoException cre) {
			OpcatLogger.logError(cre);
		}
		this.myOpcat.toggleUndoButtons(true);
		this.updateLogicalStructureChange();
	}

	/**
	 * Performs a redo operation. Similarly to the undo operation, it calls
	 * <code>Opcat2.getUndoManager().redo()</code>, toggles the undo/redo
	 * buttons and updates the structural change.
	 * 
	 */
	public void redo() {
		try {
			Opcat2.getUndoManager().redo();
		} catch (CannotRedoException cre) {
			OpcatLogger.logError(cre);
		}
		this.myOpcat.toggleUndoButtons(false);
		this.updateLogicalStructureChange();
	}

	/**
	 * a Function to warn the user about a Pending CUT OP.
	 * 
	 * @return
	 */
	public boolean IsCutPending() {
		return (StateMachine.getCurrentState() == StateMachine.EDIT_CUT);
	}

	/**
	 * a Function to warn about a pending edit OP. to warn the user before doing
	 * something which will kill the Edit OP
	 * 
	 * @return
	 */
	public boolean IsEditPending() {
		return (StateMachine.getCurrentState() == StateMachine.EDIT_CUT);
	}

	/**
	 * Paste from the clipBoard to the input Opd. If no Opd is input then the
	 * CurrentOpd is used. 
	 * 
	 */
	public void paste(Opd to) {

		if (!this.isProjectOpened()) {
			return;
		}

		int x;
		int y;

		Opd opd = null;
		Entry entry = null;
		Instance instance = null;
		OpdThing thing = null;

		if (GraphicsUtils.getLastMouseEvent() == null) {
			x = 100 ; 
			y = 100 ; 			

		} else {
			x = GraphicsUtils.getLastMouseEvent().getX();
			y = GraphicsUtils.getLastMouseEvent().getY();
		}

		Point showPoint = new Point(x, y);
		x = (int) showPoint.getX();
		y = (int) showPoint.getY();	
		
		if (to == null) {		
			opd = this.getCurrentProject().getCurrentOpd();
		} else {
			opd = to;
		}
		
		try {
			/**
			 * TODO: this check should go into th echeckmoudole package.
			 */
			instance = opd.getSelectedItem();
			entry = instance.getEntry();
			thing = ((ThingInstance) entry.getInstance(instance.getKey()))
					.getThing();
			if (!(thing.isZoomedIn())) {
				JOptionPane.showMessageDialog(Opcat2.getFrame(),
						"Can not Paste inside an un-zoomed thing",
						"Opcat2 - Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} catch (Exception e) {
			thing = null;
		}

		if (thing == null) {
			this.getCurrentProject()._copy(this.getCurrentProject().getClipBoard(), to,
					x, y, to.getDrawingArea(), true);
		} else {
			this.getCurrentProject()._copy(this.getCurrentProject().getClipBoard(), to,
					x, y, thing, true);

		}

		this.updateLogicalStructureChange();
		opd.getDrawingArea().repaint();

		StateMachine.reset(true);
		StateMachine.reset();
	}

	/**
	 * Copy input {@link Opd OPD} to ClipBoard. If source Opd is null the
	 * CurrentOpd is used
	 */
	public void copy(Opd from) {
		if (this.isProjectOpened()) {
			if (from == null) {
				this.getCurrentProject().copy();
			} else {
				this.getCurrentProject().copy(from);
			}
		}
	}

	/**
	 * remove Selection from {@link OpdProject The Current Project} if globel is
	 * False the Selection removed from Current Opd Only
	 * 
	 * @param globelDESelction
	 *            true if to remove selection from all Project
	 */
	public void removeSelection(boolean globelDESelction) {
		Opd opd = null;
		if (globelDESelction) {
			Iterator iter = Collections.list(
					this.getCurrentProject().getSystemStructure().getAllOpds())
					.iterator();
			while (iter.hasNext()) {
				opd = (Opd) iter.next();
				opd.removeSelection();
			}
		} else {
			this.getCurrentProject().removeSelection();
		}
		// Iterator iter =
	}
}