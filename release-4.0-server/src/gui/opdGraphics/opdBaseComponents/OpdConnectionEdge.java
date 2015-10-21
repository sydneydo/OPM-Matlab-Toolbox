package gui.opdGraphics.opdBaseComponents;

import exportedAPI.OpcatConstants;
import exportedAPI.OpdKey;
import exportedAPI.RelativeConnectionPoint;
import exportedAPI.opcatAPIx.IXCheckResult; //import extensionTools.hio.DrawAppNew;
import gui.Opcat2;
import gui.checkModule.CheckModule;
import gui.checkModule.CheckResult;
import gui.checkModule.CommandWrapper;
import gui.controls.GuiControl;
import gui.images.expose.ExposeImages;
import gui.opdGraphics.DrawingArea;
import gui.opdGraphics.GraphicsUtils;
import gui.opdGraphics.dialogs.ObjectPropertiesDialog;
import gui.opdGraphics.dialogs.ProcessPropertiesDialog;
import gui.opdGraphics.lines.LinkingLine;
import gui.opdProject.GenericTable;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.opdProject.Selection;
import gui.opdProject.StateMachine;
import gui.opdProject.consistency.ConsistencyAction;
import gui.opdProject.consistency.ConsistencyAbstractChecker;
import gui.opdProject.consistency.ConsistencyFactory;
import gui.opdProject.consistency.ConsistencyResult;
import gui.opmEntities.OpmConnectionEdge;
import gui.opmEntities.OpmObject;
import gui.opmEntities.OpmProcess;
import gui.opmEntities.OpmState;
import gui.projectStructure.Entry;
import gui.projectStructure.FundamentalRelationInstance;
import gui.projectStructure.GeneralRelationInstance;
import gui.projectStructure.Instance;
import gui.projectStructure.LinkInstance;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ObjectInstance;
import gui.projectStructure.ProcessEntry;
import gui.projectStructure.ProcessInstance;
import gui.undo.UndoableAddFundamentalRelation;
import gui.undo.UndoableAddGeneralRelation;
import gui.undo.UndoableAddLink;
import gui.undo.UndoableAddObject;
import gui.undo.UndoableAddProcess;
import gui.util.BrowserLauncher2;
import gui.util.OpcatLogger;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.media.jai.PlanarImage;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;

import util.Configuration;

/**
 * This is an absract class that represents all components that links or
 * relations can be connected to.
 */
public abstract class OpdConnectionEdge extends OpdBaseComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4228898496409498642L;

	protected static final double SELECTION_OFFSET = 3.2;

	public static final double FRAMES_PER_SECOND = 8.0;

	/**
	 * The reference to entry of given component in <a href =
	 * "../projectStructure/MainStructure.html" >
	 * <code>projectStructure.MainStructure</code></a>
	 */
	protected Entry myEntry;

	private boolean iconVisiable = true;

	/**
	 * The reference to entity of given component in <a href =
	 * "../projectStructure/MainStructure.html" >
	 * <code>projectStructure.MainStructure</code></a>
	 */
	protected OpmConnectionEdge myEntity;

	/**
	 * <strong>No default constructor for this class.</strong><br>
	 * <p>
	 * This is an abstruct class and cannot be instantinated directely. It calls
	 * a superclass <a href = "OpdBaseComponent.html">
	 * <code>OpdBaseComponent</code></a>, all subclasses have to call
	 * <code>super()</code> in their constructors to initialize the defaults and
	 * set parameters given as arguments
	 * </p>
	 * 
	 * @param <code>long pOpdId</code> uniqe identifier of the OPD this
	 *        component is added to.
	 * @param <code>long pOpdId</code> uniqe identifier of the component within
	 *        OPD it is added to.
	 * @param <code>OpdProject pProject</code> project this component is added
	 *        to.
	 */
	private Color beforeEventBorderColor;

	private String beforeEventToolTip;

	private boolean colorChanged = false;

	private boolean dragged;

	private DrawingArea dArea;

	private LinkingLine lLine;

	private boolean animated;

	private boolean animatedUp;

	private boolean animatedDown;

	private boolean paused;

	private javax.swing.Timer testingTimer;

	private long startTime;

	private long totalTime;

	private Color originalColor;

	// private final static Color ANIMATION_COLOR = Color.yellow;
	private Color testingColor;

	private long performedTime;

	// protected DrawAppNew drawing = gui.Opcat2.drawing;

	/** *************hioTeam************** */

	public OpdConnectionEdge(long pOpdId, long pEntityInOpdId,
			OpdProject pProject) {

		super(pOpdId, pEntityInOpdId, pProject);
		this.animated = false;
		this.animatedUp = false;
		this.animatedDown = false;

	}

	/**
	 * @return The reference to entry of given component in <a href =
	 *         "../projectStructure/MainStructure.html" >
	 *         <code>projectStructure.MainStructure</code></a>
	 */

	public OpmConnectionEdge getEntity() {
		return this.myEntity;
	}

	/**
	 * @return The reference to entity of given component in <a href =
	 *         "../projectStructure/MainStructure.html" >
	 *         <code>projectStructure.MainStructure</code></a>
	 */
	public Entry getEntry() {
		return this.myEntry;
	}

	public void mousePressed(MouseEvent e) {
		if (StateMachine.isWaiting()) {
			return;
		}

		this.dragged = false;

		this.dArea = this.myProject.getCurrentOpd().getDrawingArea();
		this.lLine = this.dArea.getLinkingLine();

		switch (StateMachine.getCurrentState()) {
		// Selecting the thing
		case StateMachine.USUAL_SELECT:
		case StateMachine.EDIT_CUT:
		case StateMachine.USUAL_MOVE: {
			super.mousePressed(e);
			Instance myInstance = getInstance();
			if (e.isShiftDown()) {
				if (myInstance.isSelected()) {
					this.myProject.removeSelection(myInstance);
				} else {
					this.myProject.addSelection(myInstance, !e.isShiftDown());
				}
			} else {
				if (myInstance.isSelected()) {
					this.myProject.addSelection(myInstance, false);
				} else {
					this.myProject.addSelection(myInstance, true);
				}
			}
			this.repaint();
			return;
		}

		case StateMachine.ADD_RELATION: {
			this.lLine.setVisible(true);
			this.drawLine(e);
			break;
		}

		case StateMachine.ADD_GENERAL_RELATION: {
			this.lLine.setVisible(true);
			this.drawLine(e);
			break;
		}

		case StateMachine.ADD_LINK: {
			this.lLine.setVisible(true);
			this.drawLine(e);
			if ((StateMachine.getDesiredComponent() == OpcatConstants.CONSUMPTION_LINK)
					&& (this instanceof OpdProcess)) {
				StateMachine.setDesiredComponent(OpcatConstants.RESULT_LINK);
			}
			break;
		}
		}

	}

	protected void _drawIcon(Graphics2D g2, Shape shape) {

		boolean showIcons = Boolean.parseBoolean(Configuration.getInstance()
				.getProperty("show_icons"));

		if (!showIcons)
			return;

		if (!isIconVisible()) {
			return;
		}

		PlanarImage planerImage = null;
		BufferedImage buf = null;
		if (myEntry.isRoleIconSet() || myEntry.isIconSet()) {
			if (myEntry.isRoleIconSet()) {
				planerImage = myEntry.getRoleIcon();
			} else {
				planerImage = myEntry.getIcon();
			}
			buf = planerImage.getAsBufferedImage();
		}

		if (getInstance() != null) {

			boolean isChanged = myEntity.isExposedChanged();

			boolean probablyTemplate = myInstance.isTemplateInstance();

			boolean usedUnknown = !myInstance.isPointerInstance()
					&& !probablyTemplate
					&& (myInstance.getEntry().getLogicalEntity()
							.getRolesManager().getRolesCollection().size() > 0);

			if (myEntity.isExposed() && !myInstance.isPointerInstance()
					&& !probablyTemplate) {
				// exposed no pointer
				if (isChanged) {
					if (myEntity.isPrivateExposed()
							&& myEntity.isPublicExposed()) {
						buf = ExposeImages.exposed_public_private_changed;
					} else if (myEntity.isPrivateExposed()) {
						buf = ExposeImages.exposed_private_changed;
					} else if (myEntity.isPublicExposed()) {
						buf = ExposeImages.exposed_public_changed;
					}

				} else {
					if (myEntity.isPrivateExposed()
							&& myEntity.isPublicExposed()) {
						buf = ExposeImages.exposed_public_private;
					} else if (myEntity.isPrivateExposed()) {
						buf = ExposeImages.exposed_private;
					} else if (myEntity.isPublicExposed()) {
						buf = ExposeImages.exposed_public;
					}
				}
			} else if (myEntity.isExposed() && myInstance.isPointerInstance()
					&& myInstance.getPointer().isPrivate() && !probablyTemplate) {
				// private pointer, and exposed
				if (isChanged) {
					if (myEntity.isPrivateExposed()
							&& myEntity.isPublicExposed()) {
						buf = ExposeImages.private_pointer_private_exposed_public_exposed_changed;
					} else if (myEntity.isPrivateExposed()) {
						buf = ExposeImages.private_pointer_private_exposed_changed;
					} else if (myEntity.isPublicExposed()) {
						buf = ExposeImages.private_pointer_public_exposed_changed;
					}

				} else {
					if (myEntity.isPrivateExposed()
							&& myEntity.isPublicExposed()) {
						buf = ExposeImages.private_pointer_private_exposed_public_exposed;
					} else if (myEntity.isPrivateExposed()) {
						buf = ExposeImages.private_pointer_private_exposed;
					} else if (myEntity.isPublicExposed()) {
						buf = ExposeImages.private_pointer_public_exposed;
					}
				}
			} else if (myEntity.isExposed() && myInstance.isPointerInstance()
					&& !myInstance.getPointer().isPrivate()
					&& !probablyTemplate) {
				// public pointer, and exposed
				if (isChanged) {
					if (myEntity.isPrivateExposed()
							&& myEntity.isPublicExposed()) {
						buf = ExposeImages.public_pointer_private_exposed_public_exposed_changed;
					} else if (myEntity.isPrivateExposed()) {
						buf = ExposeImages.public_pointer_private_exposed_changed;
					} else if (myEntity.isPublicExposed()) {
						buf = ExposeImages.public_pointer_public_exposed_changed;
					}

				} else {
					if (myEntity.isPrivateExposed()
							&& myEntity.isPublicExposed()) {
						buf = ExposeImages.public_pointer_private_exposed_public_exposed;
					} else if (myEntity.isPrivateExposed()) {
						buf = ExposeImages.public_pointer_private_exposed;
					} else if (myEntity.isPublicExposed()) {
						buf = ExposeImages.public_pointer_public_exposed;
					}
				}
			} else if (!myEntity.isExposed() && myInstance.isPointerInstance()
					&& myInstance.getPointer().isPrivate() && !probablyTemplate) {
				// private pointer, not exposed
				buf = ExposeImages.private_pointer_not_exposed;
			} else if (!myEntity.isExposed() && myInstance.isPointerInstance()
					&& !myInstance.getPointer().isPrivate()
					&& !probablyTemplate) {
				// public pointer, not exposed
				buf = ExposeImages.public_pointer_not_exposed;
			} else if (probablyTemplate) {
				// template
				if (myEntity.isExposed()) {
					// exposed template
					if (isChanged) {
						if (myEntity.isPrivateExposed()
								&& myEntity.isPublicExposed()) {
							buf = ExposeImages.template_pointer_private_exposed_public_exposed_changed;
						} else if (myEntity.isPrivateExposed()) {
							buf = ExposeImages.template_pointer_private_exposed_changed;
						} else if (myEntity.isPublicExposed()) {
							buf = ExposeImages.template_pointer_public_exposed_changed;
						}

					} else {
						if (myEntity.isPrivateExposed()
								&& myEntity.isPublicExposed()) {
							buf = ExposeImages.template_pointer_private_exposed_public_exposed;
						} else if (myEntity.isPrivateExposed()) {
							buf = ExposeImages.template_pointer_private_exposed;
						} else if (myEntity.isPublicExposed()) {
							buf = ExposeImages.template_pointer_public_exposed;
						}
					}
				} else {
					// not exposed template
					buf = ExposeImages.template_pointer_not_exposed;
				}

			} else if (usedUnknown) {
				if (myEntity.isExposed()) {
					if (isChanged) {
						if (myEntity.isPrivateExposed()
								&& myEntity.isPublicExposed()) {
							buf = ExposeImages.unknown_pointer_private_exposed_public_exposed_changed;
						} else if (myEntity.isPrivateExposed()) {
							buf = ExposeImages.unknown_pointer_private_exposed_changed;
						} else if (myEntity.isPublicExposed()) {
							buf = ExposeImages.unknown_pointer_public_exposed_changed;
						}

					} else {
						if (myEntity.isPrivateExposed()
								&& myEntity.isPublicExposed()) {
							buf = ExposeImages.unknown_pointer_private_exposed_public_exposed;
						} else if (myEntity.isPrivateExposed()) {
							buf = ExposeImages.unknown_pointer_private_exposed;
						} else if (myEntity.isPublicExposed()) {
							buf = ExposeImages.unknown_pointer_public_exposed;
						}
					}
				} else {
					buf = ExposeImages.unknown_pointer_not_exposed;
				}

			}

		}
		if (buf != null) {

			Rectangle2D rec = new Rectangle(getSize());
			TexturePaint texPaint = new TexturePaint(buf, rec);
			g2.setPaint(texPaint);

			float alpha = Float.valueOf(Configuration.getInstance()
					.getProperty("thing_icon_AlphaComposite"));

			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					alpha));
			g2.fill(shape);
		}
		return;
	}

	private void drawLine(MouseEvent e) {

		Point ePoint = SwingUtilities.convertPoint(this, e.getX(), e.getY(),
				this.dArea);

		int eX = (int) ePoint.getX() - 1;
		int eY = (int) ePoint.getY() - 1;

		Point sPoint = SwingUtilities.convertPoint(this.getParent(), this
				.getX(), this.getY(), this.dArea);

		int sX = (int) sPoint.getX() + this.getWidth() / 2;
		int sY = (int) sPoint.getY() + this.getHeight() / 2;

		int lWidth = Math.abs(eX - sX) + 2;
		int lHeight = Math.abs(eY - sY) + 2;

		this.lLine.setSize(lWidth, lHeight);

		if ((eX <= sX) && (eY <= sY)) {
			this.lLine.setLeftDirection(true);
			this.lLine.setLocation(eX, eY);
			return;
		}

		if ((eX <= sX) && (eY >= sY)) {
			this.lLine.setLeftDirection(false);
			this.lLine.setLocation(eX, sY);
			return;
		}

		if ((eX >= sX) && (eY >= sY)) {
			this.lLine.setLeftDirection(true);
			this.lLine.setLocation(sX, sY);
			return;
		}

		if ((eX >= sX) && (eY <= sY)) {
			this.lLine.setLeftDirection(false);
			this.lLine.setLocation(sX, eY);
			return;
		}

		return;
	}

	/**
	 * <code>MouseEvent.mouseReleased(MouseEvent e)</code> event handler
	 * Behavior depends on state of <a href =
	 * "../opdProject.OpdStateMachine.html"><code>OpdStateMachine</code></a>
	 * state. Also sets the mouse cursor.
	 */
	public void mouseReleased(MouseEvent e) {
		GraphicsUtils.setLastMouseEvent(e);
		if (StateMachine.isWaiting()) {
			return;
		}

		this.lLine.setVisible(false);

		if ((StateMachine.getCurrentState() == StateMachine.USUAL_SELECT)
				|| (StateMachine.getCurrentState() == StateMachine.USUAL_MOVE)
				|| (StateMachine.getCurrentState() == StateMachine.EDIT_CUT)) {
			super.mouseReleased(e);
			return;
		}

		switch (StateMachine.getCurrentState()) {
		case StateMachine.ADD_STATE: {
			return;
		}
		case StateMachine.ADD_PROCESS: {
			if (this.isZoomedIn()) {
				this.addProcess(e.getX(), e.getY());
			}
			return;
		}

		case StateMachine.ADD_OBJECT: {
			if (this.isZoomedIn()) {
				this.addObject(e.getX(), e.getY());
			}
			return;
		}
			// /** **************************HIOTeam************** */
			// case StateMachine.USUAL_DRAW: {
			//
			// if (SwingUtilities.isMiddleMouseButton(e)) {
			// return;
			// }
			// if (SwingUtilities.isRightMouseButton(e)) {
			// super.mouseReleased(e); // showPopupMenu(e.getX(), e.getY());
			// } else {
			// this.drawing.ourMouseReleased(e);
			// }
			// return;
			// }
			// /** **************************HIOTeam end************** */

		}

		int currState = StateMachine.getCurrentState();

		if ((currState == StateMachine.ADD_RELATION)
				|| (currState == StateMachine.ADD_GENERAL_RELATION)
				|| (currState == StateMachine.ADD_LINK))

		{
			Component tempComp = this.dArea.findComponentAt(SwingUtilities
					.convertPoint(this, e.getX(), e.getY(), this.dArea));

			if (!this.dragged || (tempComp instanceof DrawingArea)) {
				StateMachine.reset();
				this.setCursorForState(e);
				return;
			}

			if (!(tempComp instanceof OpdConnectionEdge)) {
				JOptionPane.showMessageDialog(null,
						"This is not allowed in OPM", "Opcat 2 - Error",
						JOptionPane.ERROR_MESSAGE);
				StateMachine.reset();
				this.setCursorForState(e);
				this.dArea.repaint();
				return;
			}

			OpdConnectionEdge target = (OpdConnectionEdge) tempComp;
			RelativeConnectionPoint sourcePoint = GraphicsUtils
					.calculateConnectionPoint(this, target);
			RelativeConnectionPoint targetPoint = GraphicsUtils
					.calculateConnectionPoint(target, this);

			OpdKey sourceKey = new OpdKey(this.getOpdId(), this
					.getEntityInOpdId());
			OpdKey targetKey = new OpdKey(target.getOpdId(), target
					.getEntityInOpdId());
			CommandWrapper cw = new CommandWrapper(this.getEntity().getId(),
					sourceKey, target.getEntity().getId(), targetKey,
					StateMachine.getDesiredComponent(), this.myProject);

			CheckResult cr = CheckModule.checkCommand(cw);

			if (cr.getResult() == IXCheckResult.WRONG) {
				JOptionPane.showMessageDialog(Opcat2.getFrame(), cr
						.getMessage(), "Opcat2 - Error",
						JOptionPane.ERROR_MESSAGE);
				StateMachine.reset();
				this.setCursorForState(e);
				this.dArea.repaint();
				return;
			}

			if (cr.getResult() == IXCheckResult.WARNING) {
				int retValue = JOptionPane.showConfirmDialog(Opcat2.getFrame(),
						cr.getMessage() + "\n Do you want to continue?",
						"Opcat2 - Warning", JOptionPane.YES_NO_OPTION);

				if (retValue == JOptionPane.NO_OPTION) {
					StateMachine.reset();
					this.setCursorForState(e);
					this.dArea.repaint();
					return;
				}
			}

			switch (StateMachine.getCurrentState()) {

			case StateMachine.ADD_RELATION: {
				try {

					if (this.getParent() == target.getParent()) {
						this.tempContainer = (JLayeredPane) target.getParent();
					} else {
						this.tempContainer = this.myProject.getCurrentOpd()
								.getDrawingArea();
					}

					sourcePoint = new RelativeConnectionPoint(
							OpcatConstants.S_BORDER, 0.5);
					targetPoint = new RelativeConnectionPoint(
							OpcatConstants.N_BORDER, 0.5);

					FundamentalRelationInstance fr = this.myProject
							.addRelation(this, sourcePoint, target,
									targetPoint, StateMachine
											.getDesiredComponent(),
									this.tempContainer, -1, -1);

					Opcat2.getUndoManager().undoableEditHappened(
							new UndoableEditEvent(this.myProject,
									new UndoableAddFundamentalRelation(
											this.myProject, fr)));

					Selection selection = Opcat2.getCurrentProject()
							.getCurrentOpd().getSelection();

					ArrayList goodSelection = new ArrayList();
					Iterator iter = Collections.list(
							selection.getSelectedItems()).iterator();
					while (iter.hasNext()) {
						Object obj = iter.next();
						if (obj instanceof Instance) {
							BaseGraphicComponent comp = (BaseGraphicComponent) ((Instance) obj)
									.getGraphicComponents()[0];
							if ((comp instanceof OpdObject)
									|| (comp instanceof OpdProcess)) {
								if (!comp.equals(this) && !comp.equals(target)) {
									goodSelection.add(obj);
								}
							}
						}
					}

					if (goodSelection.size() > 0) {
						int ret = JOptionPane.showConfirmDialog(Opcat2
								.getFrame(), "Continue with All selected ?",
								"OPCAT II", JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.INFORMATION_MESSAGE, null);
						if (ret == JOptionPane.OK_OPTION) {
							iter = goodSelection.iterator();
							while (iter.hasNext()) {
								Instance newTarget = (Instance) iter.next();

								cw = new CommandWrapper(this.getEntity()
										.getId(), sourceKey, newTarget
										.getEntry().getId(),
										newTarget.getKey(), StateMachine
												.getDesiredComponent(),
										this.myProject);

								cr = CheckModule.checkCommand(cw);

								if (cr.getResult() == IXCheckResult.WRONG) {
									JOptionPane.showMessageDialog(Opcat2
											.getFrame(), cr.getMessage(),
											"Opcat2 - Error",
											JOptionPane.ERROR_MESSAGE);
									StateMachine.reset();
									this.setCursorForState(e);
									this.dArea.repaint();
									return;
								}

								if (cr.getResult() == IXCheckResult.WARNING) {
									int retValue = JOptionPane
											.showConfirmDialog(
													Opcat2.getFrame(),
													cr.getMessage()
															+ "\n Do you want to continue?",
													"Opcat2 - Warning",
													JOptionPane.YES_NO_OPTION);

									if (retValue == JOptionPane.NO_OPTION) {
										StateMachine.reset();
										this.setCursorForState(e);
										this.dArea.repaint();
										return;
									}
								}

								fr = this.myProject.addRelation(this,
										sourcePoint,
										(OpdConnectionEdge) newTarget
												.getGraphicComponents()[0],
										targetPoint, StateMachine
												.getDesiredComponent(),
										this.tempContainer, -1, -1);

								Opcat2
										.getUndoManager()
										.undoableEditHappened(
												new UndoableEditEvent(
														this.myProject,
														new UndoableAddFundamentalRelation(
																this.myProject,
																fr)));

							}
						}
					}

					Opcat2.setUndoEnabled(Opcat2.getUndoManager().canUndo());
					Opcat2.setRedoEnabled(Opcat2.getUndoManager().canRedo());
					this.myProject.removeSelection();
					this.myProject.addSelection(fr, true);
					Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);

					StateMachine.reset();
				} catch (Exception e1) {
					System.out
							.println("OpdConnectionEdge.java : Exception ADD_RELATION "
									+ e1);
					e1.printStackTrace();
				}
				break;
			}

			case StateMachine.ADD_GENERAL_RELATION: {
				try {
					if (this.getParent() == target.getParent()) {
						this.tempContainer = (JLayeredPane) target.getParent();
					} else {
						this.tempContainer = this.myProject.getCurrentOpd()
								.getDrawingArea();
					}

					GeneralRelationInstance gr = this.myProject
							.addGeneralRelation(this, sourcePoint, target,
									targetPoint, this.tempContainer,
									StateMachine.getDesiredComponent(), -1, -1);

					Opcat2.getUndoManager().undoableEditHappened(
							new UndoableEditEvent(this.myProject,
									new UndoableAddGeneralRelation(
											this.myProject, gr)));
					Opcat2.setUndoEnabled(Opcat2.getUndoManager().canUndo());
					Opcat2.setRedoEnabled(Opcat2.getUndoManager().canRedo());
					this.myProject.removeSelection();
					this.myProject.addSelection(gr, true);
					Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
					StateMachine.reset();
				} catch (Exception e1) {
					System.out.println("Exception ADD_GENERAL_RELATION " + e1);
				}
				break;
			}

			case StateMachine.ADD_LINK: {
				try {

					Container tempContainer = null;
					if (this.getParent() == target.getParent()) {
						tempContainer = (JLayeredPane) target.getParent();
					} else {
						tempContainer = this.myProject.getCurrentOpd()
								.getDrawingArea();
					}

					LinkInstance li = this.myProject
							.addLinks/* addLinkConsistent */(this, sourcePoint,
									target, targetPoint, tempContainer,
									StateMachine.getDesiredComponent(), -1, -1);
					ConsistencyResult checkResult = new ConsistencyResult();
					ConsistencyAbstractChecker checker = (ConsistencyAbstractChecker) (new ConsistencyFactory(
							li, this.myProject, ConsistencyAction.ADDITION,
							checkResult)).create();
					checker.check();
					if (!checkResult.isConsistent()) {
						checker.deploy(checkResult);
					}

					ArrayList opds = getConsistencyAddingOPDs(li);
					if (opds.size() > 0) {

						Enumeration sourceEnum;
						Enumeration destEnum;
						Instance locSourceInstance = null;
						Instance locDestInstance = null;

						Opd opd = myProject.getCurrentOpd();
						Object[] options = { "Yes", "No", "Stop" };
						GuiControl gui = GuiControl.getInstance();

						Object[] possibilities = { "One", "All", "Step by Step" };
						String s = (String) JOptionPane.showInputDialog(gui
								.getFrame(), "Adding - ", "Opcat II",
								JOptionPane.QUESTION_MESSAGE, null,
								possibilities, "One");
						boolean ask = false;
						if (s == null) {
							s = "";
						}
						if (s.equalsIgnoreCase("Step by Step")) {
							ask = true;
						}
						if (s.equalsIgnoreCase("Step by Step")
								|| s.equalsIgnoreCase("All")) {
							int n;
							sourceEnum = li.getSourceInstance().getEntry()
									.getInstances();
							boolean cancel = false;
							OpdConnectionEdge locSourceConnectionEdge = null;
							OpdConnectionEdge locDestConnectionEdge = null;
							while (sourceEnum.hasMoreElements() && !cancel) {
								destEnum = li.getDestinationInstance()
										.getEntry().getInstances();
								locSourceInstance = (Instance) sourceEnum
										.nextElement();
								locSourceConnectionEdge = (OpdConnectionEdge) locSourceInstance
										.getGraphicComponents()[0];
								while (destEnum.hasMoreElements() && !cancel) {
									locDestInstance = (Instance) destEnum
											.nextElement();
									locDestConnectionEdge = (OpdConnectionEdge) locDestInstance
											.getGraphicComponents()[0];
									myProject.showOPD(locSourceInstance
											.getKey().getOpdId());
									if (myProject.getCurrentOpd().isContaining(
											locSourceInstance)
											&& myProject.getCurrentOpd()
													.isContaining(
															locDestInstance)
											&& !myProject
													.getCurrentOpd()
													.isConnected(
															locSourceInstance,
															locDestInstance, li)) {
										tempContainer = null;
										if (locSourceConnectionEdge.getParent() == locDestConnectionEdge
												.getParent()) {
											tempContainer = locDestConnectionEdge
													.getParent();
										} else {
											tempContainer = locDestInstance
													.getOpd().getDrawingArea();
										}
										myProject.getCurrentOpd()
												.removeSelection();
										locDestInstance.setSelected(true);
										locSourceInstance.setSelected(true);
										// ask if to add link here ?
										if (ask) {
											n = JOptionPane
													.showOptionDialog(
															gui.getFrame(),
															"Add - ",
															"OPCAT II - Message",
															JOptionPane.YES_NO_CANCEL_OPTION,
															JOptionPane.QUESTION_MESSAGE,
															null, // don't
															// use
															// a
															// custom
															// Icon
															options, // the
															// titles
															// of
															// buttons
															options[0]); // default
											// button
											// title
										} else {
											n = JOptionPane.OK_OPTION;
										}

										switch (n) {
										case JOptionPane.OK_OPTION:

											cw = new CommandWrapper(
													locSourceConnectionEdge
															.getEntity()
															.getId(),
													locSourceConnectionEdge
															.getInstance()
															.getKey(),
													locDestConnectionEdge
															.getEntity()
															.getId(),
													locDestConnectionEdge
															.getInstance()
															.getKey(),
													StateMachine
															.getDesiredComponent(),
													this.myProject);

											cr = CheckModule.checkCommand(cw);

											if (cr.getResult() == IXCheckResult.WRONG) {
												JOptionPane
														.showMessageDialog(
																Opcat2
																		.getFrame(),
																cr.getMessage(),
																"Opcat2 - Error",
																JOptionPane.ERROR_MESSAGE);
												StateMachine.reset();
												this.setCursorForState(e);
												this.dArea.repaint();
												continue;
											}

											if (cr.getResult() == IXCheckResult.WARNING) {
												int retValue = JOptionPane
														.showConfirmDialog(
																Opcat2
																		.getFrame(),
																cr.getMessage()
																		+ "\n Do you want to continue?",
																"Opcat2 - Warning",
																JOptionPane.YES_NO_OPTION);

												if (retValue == JOptionPane.NO_OPTION) {
													StateMachine.reset();
													this.setCursorForState(e);
													this.dArea.repaint();
													continue;
												}
											}

											li = myProject
													.addLinks/* addLinkConsistent */(
															locSourceConnectionEdge,
															new RelativeConnectionPoint(
																	locSourceConnectionEdge
																			.getX(),
																	locSourceConnectionEdge
																			.getY()),
															locDestConnectionEdge,
															new RelativeConnectionPoint(
																	locDestConnectionEdge
																			.getX(),
																	locDestConnectionEdge
																			.getY()),
															tempContainer,
															StateMachine
																	.getDesiredComponent(),
															-1, -1);

											checkResult = new ConsistencyResult();
											checker = (ConsistencyAbstractChecker) (new ConsistencyFactory(
													li, this.myProject,
													ConsistencyAction.ADDITION,
													checkResult)).create();
											checker.check();
											if (!checkResult.isConsistent()) {
												checker.deploy(checkResult);
											}
											break;
										case JOptionPane.NO_OPTION:
											break;
										case JOptionPane.CANCEL_OPTION:
											cancel = true;
											break;
										}
										li.setSelected(false);
										locDestInstance.setSelected(false);
										locSourceInstance.setSelected(false);
										myProject.getCurrentOpd()
												.removeSelection();

										Opcat2
												.getUndoManager()
												.undoableEditHappened(
														new UndoableEditEvent(
																this,
																new UndoableAddLink(
																		myProject,
																		li)));
										Opcat2.setUndoEnabled(Opcat2
												.getUndoManager().canUndo());
										Opcat2.setRedoEnabled(Opcat2
												.getUndoManager().canRedo());

										// removeSelection();
										// addSelection(li, true);
										Opcat2
												.updateStructureChange(Opcat2.LOGICAL_CHANGE);
										myProject.showOPD(opd.getOpdId());
									}
								}
							}
						}

					}

					StateMachine.reset();
				} catch (Exception e1) {
					System.out
							.println("Exception ADD_LINK at OpdConnection edge "
									+ e1);
					e1.printStackTrace();
				}
				break;
			}
			}
			this.setCursorForState(e);
		}
	}

	private ArrayList getConsistencyAddingOPDs(LinkInstance li) {

		// find if there are more links to add
		ArrayList opds = new ArrayList();

		// if li still exists in the system
		Enumeration opdEnum = myProject.getComponentsStructure().getLinksInOpd(
				li.getOpd());
		boolean linkExists = false;
		while (opdEnum.hasMoreElements() && !linkExists) {
			LinkInstance link = (LinkInstance) opdEnum.nextElement();
			if (link.getKey().getEntityInOpdId() == li.getKey()
					.getEntityInOpdId()) {
				linkExists = true;
				break;
			}
		}

		if (!linkExists)
			return opds;

		boolean foundMoreOPD = false;
		Instance locSourceInstance = null;
		Instance locDestInstance = null;
		OpdConnectionEdge source = li.getSource();
		OpdConnectionEdge destination = li.getDestination();

		OpdKey addedSource = source.getInstance().getKey();
		OpdKey addedDest = destination.getInstance().getKey();

		// boolean isSourceMainEntityinOPD = false ;
		// isSourceMainEntityinOPD =
		// (this.getOpdByID(sourceID).getMainEntry().getId() == sourceID);
		Enumeration sourceEnum = li.getSourceInstance().getEntry()
				.getInstances();
		Enumeration destEnum;
		while (sourceEnum.hasMoreElements()) {
			destEnum = li.getDestinationInstance().getEntry().getInstances();
			locSourceInstance = (Instance) sourceEnum.nextElement();
			// debug.Print(locSourceInstance,
			// locSourceInstance.getKey().toString(),"1") ;
			long sourceID = locSourceInstance.getKey().getOpdId();
			while (destEnum.hasMoreElements()) {
				locDestInstance = (Instance) destEnum.nextElement();
				// debug.Print(locDestInstance, "dest - " +
				// locDestInstance.getKey().toString(),"1") ;
				long destID = locDestInstance.getKey().getOpdId();

				boolean locFound = false;

				if ((destID != OpdProject.CLIPBOARD_ID)
						&& (sourceID != OpdProject.CLIPBOARD_ID)) {
					locFound = !(addedDest.toString()
							.equalsIgnoreCase(locDestInstance.getKey()
									.toString()));

					locFound = locFound
							&& !(addedSource.toString()
									.equalsIgnoreCase(locSourceInstance
											.getKey().toString()));
					locFound = locFound && (destID == sourceID)
							&& (destID != myProject.getCurrentOpd().getOpdId());

					foundMoreOPD = foundMoreOPD || locFound;

					if (locFound) {
						long[] locOPDArray = new long[2];
						locOPDArray[0] = sourceID;
						locOPDArray[1] = destID;
						opds.add(locOPDArray);
					}
				}
			}
		}

		return opds;
	}

	public void mouseDragged(MouseEvent e) {
		if (StateMachine.isWaiting() || StateMachine.isAnimated()) {
			return;
		}

		this.dragged = true;
		switch (StateMachine.getCurrentState()) {
		case StateMachine.USUAL_SELECT:
		case StateMachine.USUAL_MOVE: {
			super.mouseDragged(e);
			return;
		}
		case StateMachine.ADD_RELATION:
		case StateMachine.ADD_GENERAL_RELATION:
		case StateMachine.ADD_LINK:
			this.drawLine(e);
			break;

		// /** ******************hioTeam*****start********** */
		// case StateMachine.USUAL_DRAW: {
		//
		// if (SwingUtilities.isRightMouseButton(e)
		// || SwingUtilities.isMiddleMouseButton(e)) {
		// return;
		// }
		// Point eventPoint = SwingUtilities.convertPoint(this, e.getX(), e
		// .getY(), this.myProject.getCurrentOpd().getDrawingArea());
		// // convert the point to drawingArea coordinates
		// this.drawing.ourMouseDragged(eventPoint);
		// return;
		// }
		// /** ******************hioTeam end*************** */

		}

		return;
	}

	/**
	 * Draws selection, just draws small squares in corners and at the middle of
	 * the edges.
	 */
	public void drawSelection(Graphics2D g) {
		Stroke oldStroke = g.getStroke();

		g.setStroke(new BasicStroke());

		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		double factor = currentSize / normalSize;
		int selOffset = Math.round((float) (SELECTION_OFFSET * factor));
		int dSelOffset = 2 * selOffset;

		g.setColor(Color.white);
		g.fillRect(0, 0, dSelOffset, dSelOffset);
		g.fillRect(this.getActualWidth() / 2 - selOffset, 0, dSelOffset,
				dSelOffset);
		g.fillRect(this.getActualWidth() - 1 - selOffset * 2, 0, dSelOffset,
				dSelOffset);
		g.fillRect(0, this.getActualHeight() / 2 - selOffset, dSelOffset,
				dSelOffset);
		g.fillRect(0, this.getActualHeight() - 1 - dSelOffset, dSelOffset,
				dSelOffset);
		g.fillRect(this.getActualWidth() / 2 - selOffset, this
				.getActualHeight()
				- 1 - dSelOffset, dSelOffset, dSelOffset);
		g.fillRect(this.getActualWidth() - 1 - dSelOffset, this
				.getActualHeight()
				- 1 - dSelOffset, dSelOffset, dSelOffset);
		g.fillRect(this.getActualWidth() - 1 - dSelOffset, this
				.getActualHeight()
				/ 2 - selOffset, dSelOffset, dSelOffset);

		g.setColor(Color.black);
		g.drawRect(0, 0, dSelOffset, dSelOffset);
		g.drawRect(this.getActualWidth() / 2 - selOffset, 0, dSelOffset,
				dSelOffset);
		g.drawRect(this.getActualWidth() - 1 - dSelOffset, 0, dSelOffset,
				dSelOffset);
		g.drawRect(0, this.getActualHeight() / 2 - selOffset, dSelOffset,
				dSelOffset);
		g.drawRect(0, this.getActualHeight() - 1 - dSelOffset, dSelOffset,
				dSelOffset);
		g.drawRect(this.getActualWidth() / 2 - selOffset, this
				.getActualHeight()
				- 1 - dSelOffset, dSelOffset, dSelOffset);
		g.drawRect(this.getActualWidth() - 1 - dSelOffset, this
				.getActualHeight()
				- 1 - dSelOffset, dSelOffset, dSelOffset);
		g.drawRect(this.getActualWidth() - 1 - dSelOffset, this
				.getActualHeight()
				/ 2 - selOffset, dSelOffset, dSelOffset);

		g.setStroke(oldStroke);
	}

	public abstract void fitToContent();

	public abstract Dimension getActualSize();

	private void addObject(int x, int y) {
		OpmObject sampleObjOpm = new OpmObject(0, "");
		ObjectEntry sampleObjEntry = new ObjectEntry(sampleObjOpm,
				this.myProject);
		ObjectInstance sampleObjInstance = new ObjectInstance(sampleObjEntry,
				new OpdKey(0, 0), null, this.myProject, false);
		OpdObject sampleObjOpd = (OpdObject) sampleObjInstance
				.getConnectionEdge();
		ObjectPropertiesDialog od = new ObjectPropertiesDialog(sampleObjOpd,
				sampleObjEntry, this.myProject, true);

		if (od.showDialog()) {
			ObjectInstance oi = this.myProject.addObject(x, y, this, -1, -1,
					false);

			OpmObject newObject = (OpmObject) oi.getEntry().getLogicalEntity();
			newObject.copyPropsFrom(sampleObjOpm);
			oi.copyPropsFrom(sampleObjInstance);
			oi.update();
			oi.getConnectionEdge().fitToContent();

			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			Opcat2.getUndoManager().undoableEditHappened(
					new UndoableEditEvent(this.myProject,
							new UndoableAddObject(this.myProject, oi)));
			Opcat2.setUndoEnabled(Opcat2.getUndoManager().canUndo());
			Opcat2.setRedoEnabled(Opcat2.getUndoManager().canRedo());
			this.myProject.removeSelection();
			this.myProject.addSelection(oi, true);
			Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
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
		return;
	}

	private void addProcess(int x, int y) {
		OpmProcess sampleProcOpm = new OpmProcess(0, "");
		ProcessEntry sampleProcEntry = new ProcessEntry(sampleProcOpm,
				this.myProject);
		ProcessInstance sampleProcInstance = new ProcessInstance(
				sampleProcEntry, new OpdKey(0, 0), null, this.myProject);
		OpdProcess sampleProcOpd = (OpdProcess) sampleProcInstance
				.getConnectionEdge();

		ProcessPropertiesDialog pd = new ProcessPropertiesDialog(sampleProcOpd,
				sampleProcEntry, this.myProject, true);

		if (pd.showDialog()) {
			ProcessInstance pi = this.myProject.addProcess(x, y, this, -1, -1);

			OpmProcess newProcess = (OpmProcess) pi.getEntry()
					.getLogicalEntity();
			newProcess.copyPropsFrom(sampleProcOpm);
			pi.copyPropsFrom(sampleProcInstance);
			pi.update();
			pi.getConnectionEdge().fitToContent();

			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			Opcat2.getUndoManager().undoableEditHappened(
					new UndoableEditEvent(this.myProject,
							new UndoableAddProcess(this.myProject, pi)));
			Opcat2.setUndoEnabled(Opcat2.getUndoManager().canUndo());
			Opcat2.setRedoEnabled(Opcat2.getUndoManager().canRedo());
			this.myProject.removeSelection();
			this.myProject.addSelection(pi, true);
			Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);

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
		return;
	}

	public synchronized boolean isAnimated() {
		return this.animated;
	}

	public synchronized boolean isAnimatedUp() {
		return this.animatedUp;
	}

	public synchronized boolean isAnimatedDown() {
		return this.animatedDown;
	}

	public synchronized void pauseAnimaition() {
		if (!(this.animatedDown || this.animatedUp) || this.paused) {
			return;
		}

		this.paused = true;
		this.testingTimer.stop();
		this.performedTime = (new GregorianCalendar()).getTime().getTime()
				- this.startTime;
	}

	public synchronized void continueAnimaition() {
		if (!(this.animatedDown || this.animatedUp) || !this.paused) {
			return;
		}

		this.startTime = (new GregorianCalendar()).getTime().getTime()
				- this.performedTime;
		this.testingTimer.start();
		this.paused = false;
	}

	public synchronized void animateUrl(long time) {
		// a part of animating is showing the URL
		String url = this.getEntity().getUrl().toLowerCase();
		if (url != null) {
			if (!url.equalsIgnoreCase("none")
					&& (url.endsWith("jpg") || url.endsWith("bmp")
							|| url.endsWith("gif") || url.endsWith("jpeg")
							|| url.endsWith("mpa") || url.endsWith("avi")
							|| url.endsWith("qt") || url.endsWith("mp4")
							|| url.endsWith("mp3") || url.endsWith("mpg")
							|| url.endsWith("mpeg") || url.endsWith("wmv")
							|| url.endsWith("wma") || url.endsWith("amr"))) {
				try {
					BrowserLauncher2.openURL(this.getEntity().getUrl());
				} catch (Exception e) {
					OpcatLogger.logError(e);
				}
			}
		}
	}

	public synchronized void animate(long time) {
		if (this.animated) {
			if (time == 0) {
				this.animatedDown = false;
				this.animatedUp = false;
				this.stopColorChangeImmediately(this.testingColor);
			}
			return;
		}

		this.animated = true;

		this.testingColor = this.muchBrighter(this.getBorderColor());
		this.originalColor = this.getBackgroundColor();

		if (this.isZoomedIn()) {
			this.testingColor = this.muchBrighter(this.testingColor);
		}

		this.testingTimer = new javax.swing.Timer(
				(int) (1000 / FRAMES_PER_SECOND), this.change2testingColor);

		if (time == 0) {
			this.setBackgroundColor(this.testingColor);
			this.repaint();
			return;
		}

		this.animatedUp = true;
		this.startTime = (new GregorianCalendar()).getTime().getTime();
		this.totalTime = time;
		this.testingTimer.setInitialDelay(0);
		this.testingTimer.start();
	}

	public synchronized void stopTesting(long time) {
		if (!this.animated) {
			return;
		}

		if (this.paused) {
			this.paused = false;
		}

		this.testingTimer.stop();

		if (time == 0) {
			this.setBackgroundColor(this.originalColor);
			this.repaint();
			this.animated = false;
			this.animatedDown = false;
			this.animatedUp = false;
			return;
		}

		this.animatedDown = true;
		this.animatedUp = false;
		this.testingTimer = new javax.swing.Timer(
				(int) (1000 / FRAMES_PER_SECOND), this.change2originalColor);
		this.startTime = (new GregorianCalendar()).getTime().getTime();
		this.totalTime = time;
		this.testingColor = this.getBackgroundColor();
		this.testingTimer.setInitialDelay(0);
		this.testingTimer.start();
	}

	public synchronized long getRemainedTestingTime() {
		if (!this.animatedDown && !this.animatedUp) {
			return 0;
		}

		if (this.paused) {
			return this.totalTime - this.performedTime;
		}

		return this.totalTime + this.startTime
				- (new GregorianCalendar()).getTime().getTime();
	}

	public synchronized long getTotalTestingTime() {
		if (!this.animatedDown && !this.animatedUp) {
			return 0;
		}

		return this.totalTime;
	}

	private void stopColorChangeImmediately(Color targetColor) {
		this.testingTimer.stop();
		this.setBackgroundColor(targetColor);
		this.repaint();
	}

	Action change2testingColor = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public synchronized void actionPerformed(java.awt.event.ActionEvent e) {

			long timeLeft = OpdConnectionEdge.this.totalTime
					+ OpdConnectionEdge.this.startTime
					- (new GregorianCalendar()).getTime().getTime();
			;

			if (timeLeft <= 0) {
				OpdConnectionEdge.this
						.stopColorChangeImmediately(OpdConnectionEdge.this.testingColor);
				OpdConnectionEdge.this.animatedUp = false;
				return;
			}

			double param = 1.0 - (double) (timeLeft)
					/ (double) (OpdConnectionEdge.this.totalTime);
			OpdConnectionEdge.this
					.setBackgroundColor(new Color(
							OpdConnectionEdge.this.originalColor.getRed()
									+ (int) ((OpdConnectionEdge.this.testingColor
											.getRed() - OpdConnectionEdge.this.originalColor
											.getRed()) * param),
							OpdConnectionEdge.this.originalColor.getGreen()
									+ (int) ((OpdConnectionEdge.this.testingColor
											.getGreen() - OpdConnectionEdge.this.originalColor
											.getGreen()) * param),
							OpdConnectionEdge.this.originalColor.getBlue()
									+ (int) ((OpdConnectionEdge.this.testingColor
											.getBlue() - OpdConnectionEdge.this.originalColor
											.getBlue()) * param)));
			OpdConnectionEdge.this.repaint();
		}
	};

	Action change2originalColor = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 904738836425500647L;

		public synchronized void actionPerformed(java.awt.event.ActionEvent e) {

			long timeLeft = OpdConnectionEdge.this.totalTime
					+ OpdConnectionEdge.this.startTime
					- (new GregorianCalendar()).getTime().getTime();

			if (timeLeft <= 0) {
				OpdConnectionEdge.this
						.stopColorChangeImmediately(OpdConnectionEdge.this.originalColor);
				OpdConnectionEdge.this.animated = false;
				OpdConnectionEdge.this.animatedDown = false;

				OpdConnectionEdge edge = OpdConnectionEdge.this;
				if (edge instanceof OpdObject) {
					OpdObject opdObject = (OpdObject) edge;
					// System.out.println("Thing - " + edge.getName());
					// System.out.println("Object Color "
					// + opdObject.getBackgroundColor());
					// System.out.println("orign Color " +
					// edge.originalColor);
					Component comp[] = opdObject.getComponents();
					opdObject.getEntry().getInstances().nextElement();
					for (int i = 0; i < comp.length; i++) {
						if (comp[i] instanceof OpdState) {
							OpdState opdState = (OpdState) comp[i];
							if (((OpmState) opdState.getEntity()).isFinal()) {
								opdState.animate(0);
							}
						}
					}
				}

				return;
			}

			double param = 1.0 - (double) (timeLeft)
					/ (double) (OpdConnectionEdge.this.totalTime);
			OpdConnectionEdge.this
					.setBackgroundColor(new Color(
							OpdConnectionEdge.this.testingColor.getRed()
									+ (int) ((OpdConnectionEdge.this.originalColor
											.getRed() - OpdConnectionEdge.this.testingColor
											.getRed()) * param),
							OpdConnectionEdge.this.testingColor.getGreen()
									+ (int) ((OpdConnectionEdge.this.originalColor
											.getGreen() - OpdConnectionEdge.this.testingColor
											.getGreen()) * param),
							OpdConnectionEdge.this.testingColor.getBlue()
									+ (int) ((OpdConnectionEdge.this.originalColor
											.getBlue() - OpdConnectionEdge.this.testingColor
											.getBlue()) * param)));
			OpdConnectionEdge.this.repaint();

		}
	};

	private Color muchBrighter(Color origColor) {
		int r = origColor.getRed();
		int g = origColor.getGreen();
		int b = origColor.getBlue();

		return new Color(r + (255 - r) / 2, g + (255 - g) / 2, b + (255 - b)
				/ 2);
	}

	public void mouseClicked(MouseEvent e) {
		if (this.colorChanged) {
			this.setToolTipText(this.beforeEventToolTip);
			this.setBorderColor(this.beforeEventBorderColor);
			this.colorChanged = false;
		}
		this.repaint();
		super.mouseClicked(e);
		if (e.isControlDown()) {
			this.myEntry.ShowUrl();
			return;
		}
	}

	public void mouseExited(MouseEvent e) {
		if (this.colorChanged) {
			this.setToolTipText(this.beforeEventToolTip);
			this.setBorderColor(this.beforeEventBorderColor);
			this.colorChanged = false;
		}
		this.repaint();
	}

	public void mouseEntered(MouseEvent e) {

		super.mouseEntered(e);
		if (this.myEntry.hasURL()) {
			this.beforeEventBorderColor = this.getBorderColor();
			this.beforeEventToolTip = this.getToolTipText();
			this.setToolTipText("Press Ctrl+Left-Mouse to activate URL");
			this.setBorderColor((Color) this.myProject.getConfiguration()
					.getProperty("UrlColor"));
			this.colorChanged = true;
			this.repaint();
		}
	}

	public void setAnimated(boolean animated) {
		this.animated = animated;
	}

	public boolean isIconVisible() {
		return iconVisiable;
	}

	public void setIconVisible(boolean iconVisible) {
		this.iconVisiable = iconVisible;
	}

}