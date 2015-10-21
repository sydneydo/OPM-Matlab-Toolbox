package gui.opdGraphics;

import exportedAPI.OpdKey; //import extensionTools.hio.CursorFactory;
//import extensionTools.hio.DrawAppNew;
import gui.Opcat2;
import gui.controls.GuiControl;
import gui.license.ILicense;
import gui.opdGraphics.dialogs.ObjectPropertiesDialog;
import gui.opdGraphics.dialogs.ProcessPropertiesDialog;
import gui.opdGraphics.lines.LinkingLine;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;
import gui.opdGraphics.opdBaseComponents.OpdObject;
import gui.opdGraphics.opdBaseComponents.OpdProcess;
import gui.opdGraphics.popups.OpdPopup;
import gui.opdProject.GenericTable;
import gui.opdProject.IllegalPassException;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.opdProject.Selection;
import gui.opdProject.StateMachine;
import gui.opdProject.consistency.ConsistencyAction;
import gui.opdProject.consistency.ConsistencyAbstractChecker;
import gui.opdProject.consistency.ConsistencyFactory;
import gui.opdProject.consistency.ConsistencyResult;
import gui.opmEntities.OpmObject;
import gui.opmEntities.OpmProcess;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ObjectInstance;
import gui.projectStructure.ProcessEntry;
import gui.projectStructure.ProcessInstance;
import gui.undo.UndoableAddObject;
import gui.undo.UndoableAddProcess;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Enumeration;
import javax.swing.JDesktopPane;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;

/******************HIOTeam*********************/
/**
 * <p>
 */

public class DrawingArea extends JDesktopPane implements MouseListener,
		MouseMotionListener, Scrollable, OpcatContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	protected OpdProject myProject;

	private int m_XDifference, m_YDifference;

	private LinkingLine lLine;

	private int startSelX, startSelY;

	Container c;

	private SelectionRectangle selRect;

	private Opd myOpd;

	// private DrawAppNew drawing = gui.Opcat2.drawing;

	/** ****************HIOTeam******************** */

	public DrawingArea(OpdProject pProject, Opd opd) {
		super();
		this.setDoubleBuffered(true);
		this.myProject = pProject;
		this.myOpd = opd;
		this.setLayout(null);
		GenericTable config = this.myProject.getConfiguration();
		this.setBackground((Color) config.getProperty("BackgroundColor"));

		this.lLine = new LinkingLine();
		this.lLine.setVisible(false);

		this.add(this.lLine, JLayeredPane.DRAG_LAYER);

		this.selRect = new SelectionRectangle(this.myProject);
		this.selRect.setVisible(true);
		this.add(this.selRect, JLayeredPane.DRAG_LAYER);

		// selected = new Hashtable();
		// nonselected = new Hashtable();
		// setBorder(BorderFactory.createEtchedBorder());
	}

	// we overwrite this function to avoid sun internal exception
	public void printComponent(Graphics g) {
		if (this.getComponentCount() == 1) {
			// g.setColor(new Color(230, 230, 230));
			// g.fillRect(0, 0, getWidth(), getHeight());
			return;
		} else {
			super.printComponent(g);
		}
	}

	public void mouseClicked(MouseEvent e) {

		if (e.getClickCount() == 2) {
			myOpd.setViewZoomIn(false);
		}

		if (StateMachine.isWaiting()) {
			return;
		}
		// Event evt ;
		// keyDown(evt , )
	}

	public void mouseDragged(MouseEvent e) {

		if (StateMachine.isWaiting()) {
			return;
		}

		if (StateMachine.getCurrentState() == StateMachine.USUAL_SELECT) {
			try {
				StateMachine.go(StateMachine.IN_SELECTION, 0);
			} catch (IllegalPassException ipe) {
				ipe.printStackTrace();
			}

			int minX = Math.min(this.startSelX, e.getX());
			int minY = Math.min(this.startSelY, e.getY());
			int maxX = Math.max(this.startSelX, e.getX());
			int maxY = Math.max(this.startSelY, e.getY());
			this.getSelection().initSelection(this, null);
			this.selRect.setLocation(minX, minY);
			this.selRect.setSize(maxX - minX, maxY - minY);
			this.getSelection().inSelection();
			return;
		}

		// /** **************HIOTeam Start *********** */
		// if (StateMachine.getCurrentState() == StateMachine.USUAL_DRAW) {
		// if (SwingUtilities.isRightMouseButton(e)
		// || SwingUtilities.isMiddleMouseButton(e)) {
		// return;
		// }
		// Point eventPoint = new Point(e.getPoint());
		// //this.drawing.ourMouseDragged(eventPoint);
		// return;
		// }
		// /** **************HIOTeam End*********** */

		if (StateMachine.getCurrentState() == StateMachine.IN_SELECTION) {
			int minX = Math.min(this.startSelX, e.getX());
			int minY = Math.min(this.startSelY, e.getY());
			int maxX = Math.max(this.startSelX, e.getX());
			int maxY = Math.max(this.startSelY, e.getY());
			this.selRect.setLocation(minX, minY);
			this.selRect.setSize(maxX - minX, maxY - minY);
			this.myOpd.getSelection().inSelection();
			return;
		}

		// }
		// else if(Opcat2.getDrawingAreaOnMouseDragAction().equals("move"))
		// {
		if (StateMachine.getCurrentState() == StateMachine.USUAL_MOVE) {
			this.c = this.getParent();
			if (this.c instanceof JViewport) {
				JViewport jv = (JViewport) this.c;
				Point p = jv.getViewPosition();
				int newX = p.x - (e.getX() - this.m_XDifference);
				int newY = p.y - (e.getY() - this.m_YDifference);
				int maxX = this.getWidth() - jv.getWidth();
				int maxY = this.getHeight() - jv.getHeight();
				if (newX < 0) {
					newX = 0;
				}
				if (newX > maxX) {
					newX = maxX;
				}
				if (newY < 0) {
					newY = 0;
				}
				if (newY > maxY) {
					newY = maxY;
				}
				jv.setViewPosition(new Point(newX, newY));
			}
			return;
		}
	}

	public void mousePressed(MouseEvent e) {
		if (StateMachine.isWaiting()) {
			return;
		}

		if (StateMachine.getCurrentState() == StateMachine.USUAL_MOVE) {
			this.myProject.removeSelection();
			this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			this.m_XDifference = e.getX();
			this.m_YDifference = e.getY();
			return;
		}

		if ((StateMachine.getCurrentState() == StateMachine.USUAL_SELECT)
				|| (StateMachine.getCurrentState() == StateMachine.EDIT_CUT)) {
			this.myProject.removeSelection();
			this.startSelX = e.getX();
			this.startSelY = e.getY();
			this.selRect.setVisible(true);
			return;
		}

		// /** ****start*****HIOTeam******************** */
		// if (StateMachine.getCurrentState() == StateMachine.USUAL_DRAW) {
		// if (SwingUtilities.isRightMouseButton(e)
		// || SwingUtilities.isMiddleMouseButton(e)) {
		// this.myProject.removeSelection();
		// this.startSelX = e.getX();
		// this.startSelY = e.getY();
		// this.selRect.setVisible(true);
		// return;
		// }
		// //if (this.drawing == null) {
		// // this.drawing = gui.Opcat2.drawing;
		// //}
		// Point eventPoint = new Point(e.getPoint());
		// //this.drawing.ourMousePressed(eventPoint);
		// }
		// /** **end**********HIOTeam******************** */

	}

	public void mouseMoved(MouseEvent e) {
	}

	public LinkingLine getLinkingLine() {
		return this.lLine;
	}

	public void mouseReleased(MouseEvent e) {
		ILicense license = Opcat2.getLicense();
		GraphicsUtils.setLastMouseEvent(e);

		if (StateMachine.isWaiting()) {
			return;
		}

		if ((e.isPopupTrigger() || e.isMetaDown())
				&& !StateMachine.isAnimated()) {
			this.showPopupMenu(e.getX(), e.getY());
			return;
		}

		if (e.isControlDown()) {
			this.getSelection().finishSelection();
			this.selRect.setVisible(false);

			int neededWidth = selRect.getWidth() + 1;
			int currentWidth = getWidth();

			int neededHeight = selRect.getHeight();
			int currentHeight = getHeight();

			if (neededHeight == 0)
				return;

			myOpd.setViewZoomIn(true);
			int widthFactor = currentWidth / neededWidth;
			int heightFactor = currentHeight / neededHeight;
			int factor = 0;
			if (widthFactor > heightFactor) {
				factor = widthFactor;
			} else {
				factor = heightFactor;
			}

			myOpd.viewZoomIn(widthFactor, heightFactor);

			JViewport jv = (JViewport) getParent();
			jv.setViewPosition(new Point(selRect.getX() * widthFactor, selRect
					.getY()
					* heightFactor));
			// myProject.getConfiguration().setProperty("CurrentSize", factor);
			// myOpd.getOpdContainer().setGlassPane(new JPanel()) ;
			// myOpd.getOpdContainer().getGlassPane().setVisible(true);

		}

		switch (StateMachine.getCurrentState()) {
		/** *********HIOTeam******************** */
		case StateMachine.USUAL_DRAW:
			if (SwingUtilities.isMiddleMouseButton(e)) {
				return;
			}
			if (SwingUtilities.isRightMouseButton(e)) {
				this.showPopupMenu(e.getX(), e.getY());
				return;
			}
			// this.drawing.ourMouseReleased(e);
			return;
			/** *********HIOTeam end******************** */
		case StateMachine.IN_SELECTION: {
			this.getSelection().finishSelection();
			this.selRect.setVisible(false);
			// add code for selection of entities
			this.selRect.setSize(0, 0);
			try {
				StateMachine.go(StateMachine.USUAL_SELECT, 0);
			} catch (IllegalPassException ipe) {
				ipe.printStackTrace();
			}
			break;
		}
		case StateMachine.ADD_PROCESS: {
			if (!license.getPolicyOfficer().canAddThing(this.myProject)) {
				this.actUponLicenseViolation();
				return;
			}
			OpmProcess sampleProcOpm = new OpmProcess(0, "");
			ProcessEntry sampleProcEntry = new ProcessEntry(sampleProcOpm,
					this.myProject);
			ProcessInstance sampleProcInstance = new ProcessInstance(
					sampleProcEntry, new OpdKey(0, 0), null, this.myProject);
			OpdProcess sampleProcOpd = (OpdProcess) sampleProcInstance
					.getConnectionEdge();

			ProcessPropertiesDialog pd = new ProcessPropertiesDialog(
					sampleProcOpd, sampleProcEntry, this.myProject, true);

			if (pd.showDialog()) {
				ProcessInstance pi = this.myProject.addProcess(e.getX(), e
						.getY(), this, -1, -1);

				OpmProcess newProcess = (OpmProcess) pi.getEntry()
						.getLogicalEntity();
				newProcess.copyPropsFrom(sampleProcOpm);
				pi.copyPropsFrom(sampleProcInstance);
				pi.update();
				pi.getConnectionEdge().fitToContent();
				pi.getOpd().refit(pi.getOpd().getDrawingArea());

				this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				Opcat2.getUndoManager().undoableEditHappened(
						new UndoableEditEvent(this.myProject,
								new UndoableAddProcess(this.myProject, pi)));
				Opcat2.setUndoEnabled(Opcat2.getUndoManager().canUndo());
				Opcat2.setRedoEnabled(Opcat2.getUndoManager().canRedo());
				this.myProject.removeSelection();
				this.myProject.addSelection(pi, true);

				if (pd.isAddHelpON()) {
					ConsistencyResult checkResult = new ConsistencyResult();
					ConsistencyAbstractChecker checker = (ConsistencyAbstractChecker) (new ConsistencyFactory(
							pi, this.myProject, ConsistencyAction.ADDITION,
							checkResult)).create();
					checker.check();
					if (!checkResult.isConsistent()) {
						checker.deploy(checkResult);
					}
				}
			}

			StateMachine.reset();
			Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
			break;
		}
		case StateMachine.ADD_OBJECT: {
			if (!license.getPolicyOfficer().canAddThing(this.myProject)) {
				this.actUponLicenseViolation();
				return;
			}
			OpmObject sampleObjOpm = new OpmObject(0, "");
			ObjectEntry sampleObjEntry = new ObjectEntry(sampleObjOpm,
					this.myProject);
			ObjectInstance sampleObjInstance = new ObjectInstance(
					sampleObjEntry, new OpdKey(0, 0), null, this.myProject,
					false);
			OpdObject sampleObjOpd = (OpdObject) sampleObjInstance
					.getConnectionEdge();

			ObjectPropertiesDialog od = new ObjectPropertiesDialog(
					sampleObjOpd, sampleObjEntry, this.myProject, true);

			if (od.showDialog()) {
				ObjectInstance oi = this.myProject.addObject(e.getX(),
						e.getY(), this, -1, -1, false);
				OpmObject newObject = (OpmObject) oi.getEntry()
						.getLogicalEntity();
				newObject.copyPropsFrom(sampleObjOpm);
				oi.copyPropsFrom(sampleObjInstance);
				oi.update();
				if (((OpdObject) oi.getConnectionEdge()).isStatesAutoarrange())
					oi.getConnectionEdge().fitToContent();
				oi.getOpd().refit(oi.getOpd().getDrawingArea());

				this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				Opcat2.getUndoManager().undoableEditHappened(
						new UndoableEditEvent(this.myProject,
								new UndoableAddObject(this.myProject, oi)));
				Opcat2.setUndoEnabled(Opcat2.getUndoManager().canUndo());
				Opcat2.setRedoEnabled(Opcat2.getUndoManager().canRedo());
				this.myProject.removeSelection();
				this.myProject.addSelection(oi, true);
				if (od.isAddHelpON()) {
					ConsistencyResult checkResult = new ConsistencyResult();
					ConsistencyAbstractChecker checker = (ConsistencyAbstractChecker) (new ConsistencyFactory(
							oi, this.myProject, ConsistencyAction.ADDITION,
							checkResult)).create();
					checker.check();
					if (!checkResult.isConsistent()) {
						checker.deploy(checkResult);
					}
				}

			}

			StateMachine.reset();
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);

			GuiControl gui = GuiControl.getInstance();
			gui.getRepository().rebuildTrees(true);
			break;
		}

		default: {
			StateMachine.reset();
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			break;
		}
		}

	}

	public void mouseEntered(MouseEvent e) {
		if (StateMachine.isWaiting()) {
			return;
		}

		switch (StateMachine.getCurrentState()) {
		case StateMachine.USUAL_SELECT:
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			break;
		case StateMachine.EDIT_CUT:
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			break;
		// /** ***********************HIOTeam************** */
		// case StateMachine.USUAL_DRAW:
		// //this.setCursor(CursorFactory.getDrawCursor());
		//
		// break;
		// /** ***********************HIOTeam End************** */
		case StateMachine.USUAL_MOVE:
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			break;
		case StateMachine.ADD_PROCESS:
		case StateMachine.ADD_OBJECT:
			this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			break;
		case StateMachine.ADD_LINK:
		case StateMachine.ADD_RELATION:
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			break;
		}
	}

	public void mouseExited(MouseEvent e) {
	}

	public Dimension getPreferredScrollableViewportSize() {
		return this.getPreferredSize();
	}

	public int getScrollableBlockIncrement(Rectangle r, int orientation,
			int direction) {
		Dimension size;

		size = this.getPreferredSize();
		if (orientation == SwingConstants.VERTICAL) {
			return (int) (size.getHeight() / 10);
		} else {
			return (int) (size.getWidth() / 10);
		}

	}

	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	public int getScrollableUnitIncrement(Rectangle r, int orientation,
			int direction) {
		Dimension size;

		size = this.getPreferredSize();
		if (orientation == SwingConstants.VERTICAL) {
			return (int) (size.getHeight() / 50);
		} else {
			return (int) (size.getWidth() / 50);
		}

	}

	protected void showPopupMenu(int x, int y) {
		JPopupMenu jpm = new OpdPopup(this.myProject, x, y);
		// Point showPoint = GraphicsUtils.findPlace4Menu(this, new Point(x, y),
		// jpm.getPreferredSize());
		// jpm.show(this, (int) showPoint.getX(), (int) showPoint.getY());
		jpm.show(this, x, y);
	}

	public Container getContainer() {
		return this;
	}

	public BaseGraphicComponent[] graphicalSelectionComponents() {
		return this.myOpd.getSelection().graphicalSelectionComponents();
	}

	public Enumeration getGraphicalSelection() {
		return null;
	}

	public Selection getSelection() {
		return this.myOpd.getSelection();
	}

	public SelectionRectangle getSelectionRectangle() {
		return this.selRect;
	}

	/**
	 * A set of actions which happens if the user violated the license terms.
	 * For instance, setting the cursor back to default and showing an error
	 * message to the user.
	 */
	protected void actUponLicenseViolation() {
		StateMachine.reset();
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		Opcat2.showMessage("License Error", Opcat2.getLicense()
				.getLicenseError(), JOptionPane.ERROR_MESSAGE);
	}
}