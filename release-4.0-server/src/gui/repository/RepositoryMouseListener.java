package gui.repository;

import gui.Opcat2;
import gui.controls.FileControl;
import gui.metaLibraries.logic.MetaLibrary;
import gui.opdGraphics.DrawingArea;
import gui.opdGraphics.opdBaseComponents.OpdThing;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.opdProject.StateMachine;
import gui.opmEntities.OpmEntity;
import gui.opmEntities.OpmThing;
import gui.projectStructure.ThingEntry;
import gui.projectStructure.ThingInstance;
import gui.repository.rpopups.RDirPopup;
import gui.repository.rpopups.REntryPopup;
import gui.repository.rpopups.RMetaPopup;
import gui.repository.rpopups.RMetaThingPopup;
import gui.repository.rpopups.ROpdPopup;
import gui.repository.rpopups.RProjectPopup;
import gui.repository.rpopups.RSVNPopup;
import gui.repository.rpopups.RScenariosPopup;
import gui.repository.rpopups.RScenarioPopup;
import gui.repository.rpopups.RThingPopup;
import gui.scenarios.Scenario;
import gui.scenarios.Scenarios;
import gui.util.OpcatFile;
import gui.util.OpcatLogger;
import gui.util.opcatGrid.GridPanel;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JPopupMenu;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import modelControl.OpcatMCDirEntry;

import org.tmatesoft.svn.core.SVNURL;

class RepositoryMouseListener extends MouseAdapter {
	BaseView eventSender;

	Repository repository;

	public RepositoryMouseListener(BaseView tree, Repository rep) {
		super();
		this.repository = rep;
		this.eventSender = tree;
	}

	public void mouseReleased(MouseEvent e) {
		if (StateMachine.isAnimated() || StateMachine.isAnimated()) {
			return;
		}

		if (e.isPopupTrigger() || e.isMetaDown()) {
			TreePath selPath = this.eventSender.getPathForLocation(e.getX(), e
					.getY());
			if (selPath == null) {
				return;
			}
			this.eventSender.setSelectionPath(selPath);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) (selPath
					.getLastPathComponent());
			if (node != null) {
				Object obj = node.getUserObject();
				if (obj instanceof Opd) {
					JPopupMenu jpm = new ROpdPopup(this.eventSender,
							this.repository.getProject());
					jpm.show(this.eventSender, e.getX(), e.getY());
				}
				if ((obj instanceof ThingInstance)
						&& (eventSender.getType() != Repository.MetaVIEW)) {
					JPopupMenu jpm = new RThingPopup(this.eventSender,
							this.repository.getProject());
					jpm.show(this.eventSender, e.getX(), e.getY());
				}
				// Meta View
				if ((obj instanceof ThingEntry)
						&& (eventSender.getType() == Repository.MetaVIEW)) {
					JPopupMenu jpm = new RMetaThingPopup(this.eventSender,
							this.repository.getProject());
					jpm.show(this.eventSender, e.getX(), e.getY());
				}

				if ((obj instanceof MetaLibrary)
						&& ((eventSender.getType() == Repository.MetaVIEW) || (eventSender
								.getType() == Repository.TestingVIEW))) {
					JPopupMenu jpm = new RMetaPopup(this.eventSender,
							this.repository.getProject());
					jpm.show(this.eventSender, e.getX(), e.getY());
				}
				// END Meta View

				if ((obj instanceof OpmEntity)
						&& (eventSender.getType() != Repository.MetaVIEW)) {
					JPopupMenu jpm = new REntryPopup(this.eventSender,
							this.repository.getProject());
					jpm.show(this.eventSender, e.getX(), e.getY());
				}
				if (obj instanceof OpdProject) {
					JPopupMenu jpm = new RProjectPopup(this.eventSender,
							this.repository, this.repository.getProject());
					jpm.show(this.eventSender, e.getX(), e.getY());
				}

				if (obj instanceof Scenario) {
					JPopupMenu jpm = new RScenarioPopup(this.eventSender,
							this.repository.getProject());
					jpm.show(this.eventSender, e.getX(), e.getY());
				}

				if (obj instanceof Scenarios) {
					JPopupMenu jpm = new RScenariosPopup(this.eventSender,
							this.repository.getProject());
					jpm.show(this.eventSender, e.getX(), e.getY());
				}

				if (obj instanceof SVNURL) {
					JPopupMenu jpm = new RSVNPopup(this.eventSender,
							this.repository.getProject());
					jpm.show(this.eventSender, e.getX(), e.getY());
				}

				if (obj instanceof OpcatMCDirEntry) {
					JPopupMenu jpm = new RSVNPopup(this.eventSender,
							this.repository.getProject());
					jpm.show(this.eventSender, e.getX(), e.getY());
				}

				if ((obj instanceof OpcatFile)
						|| ((obj instanceof String) && (obj.toString()
								.equalsIgnoreCase("Models")))) {
					JPopupMenu jpm = new RDirPopup(this.eventSender,
							this.repository.getProject(), e);
					jpm.show(this.eventSender, e.getX(), e.getY());
				}

			}
		}
	}

	public void mouseClicked(MouseEvent e) {

		TreePath selPath = this.eventSender.getPathForLocation(e.getX(), e
				.getY());
		if (selPath == null) {
			return;
		}
		this.eventSender.setSelectionPath(selPath);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) (selPath
				.getLastPathComponent());

		if (e.getClickCount() == 2) {
			if (node != null) {
				Object obj = node.getUserObject();
				if (obj instanceof Opd) {
					this.showOpd((Opd) obj);
				}

				if (obj instanceof ThingInstance) {
					ThingInstance inst = ((ThingInstance) obj);
					long opdId = inst.getThing().getOpdId();
					Opd myOpd = this.repository.getProject()
							.getComponentsStructure().getOpd(opdId);
					OpdProject myProject = this.repository.getProject();
					this.showOpd(myOpd);
					myProject.removeSelection();
					myProject.addSelection(inst, true);
					inst.getThing().setSelected(true);

					// center view to Thing
					this.centerView(myOpd, inst);

				}

				if (obj instanceof OpmThing) {
					Opcat2.getCurrentProject().getSystemStructure().getEntry(
							((OpmThing) obj).getId()).ShowInstances();
				}

				if (obj instanceof MetaLibrary) {
					try {
						MetaLibrary meta = (MetaLibrary) obj;
						FileControl.getInstance().runNewOPCAT(
								new File(meta.getPath()));
					} catch (Exception ex) {
						OpcatLogger.logError(ex);
					}
				}

				if (obj instanceof OpcatFile) {
					OpcatFile file = (OpcatFile) obj;
					if (file.isDirectory()) {
						if (eventSender.isExpanded(selPath)) {
							eventSender.collapsePath(selPath);
						} else {
							eventSender.expandPath(selPath);
						}
					} else {
						try {
							GridPanel.RemoveALLPanels();

							FileControl.getInstance()
									.loadFileWithOutFilesPrompt(file.getPath());

							if (FileControl.getInstance().getCurrentProject() != null)
								FileControl.getInstance().getCurrentProject()
										.setCanClose(false);

						} catch (Exception ex) {
							OpcatLogger.logError(ex);
						}
					}
				}

				if (obj instanceof OpcatMCDirEntry) {
					repository.getSVNView().dblClicked(e);
				}
			}
		}
		// here we need to color the selected cell background.
	}

	private void centerView(Opd myOpd, ThingInstance inst) {
		DrawingArea da = myOpd.getDrawingArea();
		JViewport jv = (JViewport) da.getParent();
		OpdThing thing = inst.getThing();
		Dimension visiblePort = jv.getExtentSize();

		int newX = (thing.getX() + thing.getWidth() / 2);
		int newY = (thing.getY() + thing.getHeight() / 2);

		Point thingCenter = SwingUtilities.convertPoint(thing.getParent(),
				newX, newY, da);

		newX = (int) (thingCenter.getX() - visiblePort.getWidth() / 2);
		newY = (int) (thingCenter.getY() - visiblePort.getHeight() / 2);

		int maxX = da.getWidth() - jv.getWidth();
		int maxY = da.getHeight() - jv.getHeight();

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

	private void showOpd(Opd myOpd) {
		// myOpd.setVisible(true);
		repository.getProject().showOPD(myOpd.getOpdId());
		// try {
		// myOpd.getOpdContainer().setSelected(true);
		// myOpd.getOpdContainer().setMaximum(true);
		// } catch (java.beans.PropertyVetoException pve) {
		// pve.printStackTrace();
		// }
	}
}
