package gui.opdGraphics.popups;

import exportedAPI.opcatAPIx.IXCheckResult;
import gui.Opcat2;
import gui.actions.edit.CompleteLinksinOPD;
import gui.actions.edit.MetaColoringAction;
import gui.actions.edit.MetaHideAction;
import gui.actions.edit.MetaInsertAction;
import gui.checkModule.CheckModule;
import gui.checkModule.CheckResult;
import gui.controls.GuiControl;
import gui.dataProject.DataProject;
import gui.images.repository.RepositoryImages;
import gui.images.standard.StandardImages;
import gui.metaLibraries.logic.MetaLibrary;
import gui.metaLibraries.logic.Role;
import gui.opdProject.OpdProject;
import gui.opdProject.ProjectPropertiesDialog;
import gui.opmEntities.OpmProcess;
import gui.opmEntities.OpmThing;
import gui.util.OpcatLogger;
import gui.util.VerticalGridLayout;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

public class OpdPopup extends DefaultPopup {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	CompleteLinksinOPD completeLinksInOPDWithMissing = null;

	CompleteLinksinOPD completeLinksInOPDWithOutMissing = null;

	public OpdPopup(OpdProject prj, int x, int y) {
		super(prj, null);

		completeLinksInOPDWithMissing = new CompleteLinksinOPD(
				"Add Missing Things", StandardImages.COMPLETE_LINKS, true);

		completeLinksInOPDWithOutMissing = new CompleteLinksinOPD(
				"Do Not Add Missing Things",
				StandardImages.COMPLETE_LINKS_NO_THING, false);

		this.add(this.pasteAction);
		this.add(this.deleteAction);
		add(new JSeparator());
		JMenu completeLinks = new JMenu("Complete Links");
		completeLinks.setIcon(StandardImages.COMPLETE_LINKS);
		completeLinks.add(completeLinksInOPDWithOutMissing);
		completeLinks.add(completeLinksInOPDWithMissing);
		this.add(completeLinks);
		add(new JSeparator());

		JMenu levelColor = new JMenu("Meta Coloring");
		levelColor.setIcon(StandardImages.META_COLOR);
		levelColor
				.setToolTipText("Color things according to Meta Data associations");
		add(levelColor);
		/**
		 * TODO: move this to saved settings when we do that
		 */
		int menuItemInAColomn = 30;
		/**
		 * 
		 */
		levelColor.getPopupMenu().setLayout(
				new VerticalGridLayout(menuItemInAColomn, 0));

		JMenu implemMenu = new JMenu("Insert Tagged Things");
		implemMenu
				.setToolTipText("Insert Things connected to an External Thing");
		implemMenu.getPopupMenu().setLayout(
				new VerticalGridLayout(menuItemInAColomn, 0));
		implemMenu.setIcon(StandardImages.META_INSERT);
		JMenu hideMenu = new JMenu("Meta Hide");
		hideMenu.setIcon(StandardImages.META_HIDE);
		hideMenu.setToolTipText("Hide Things connected to External Data");
		hideMenu.getPopupMenu().setLayout(
				new VerticalGridLayout(menuItemInAColomn, 0));
		add(hideMenu);

		JMenu showMenu = new JMenu("Meta Show");
		showMenu.setIcon(StandardImages.META_SHOW);
		showMenu.setToolTipText("Show Things connected to External Data");
		showMenu.getPopupMenu().setLayout(
				new VerticalGridLayout(menuItemInAColomn, 0));
		add(showMenu);

		JMenuItem colorItem = new JMenuItem(new MetaColoringAction("Default",
				null, StandardImages.META_COLOR_DEF));
		levelColor.add(colorItem);

		JMenuItem showItem = new JMenuItem(new MetaHideAction("Show All", null,
				true, null));
		showMenu.add(showItem);

		JMenuItem hideItem = new JMenuItem(new MetaHideAction("Unhide All",
				null, false, null));
		hideMenu.add(hideItem);

		ArrayList<MetaLibrary> metas = new ArrayList<MetaLibrary>();
		metas.addAll(Collections.list(myProject.getMetaLibraries()));
		metas.addAll(Collections.list(myProject.getOpdMetaDataManager()
				.getMetaLibraries()));
		Iterator iter = metas.iterator();
		while (iter.hasNext()) {
			MetaLibrary meta = (MetaLibrary) iter.next();
			if (!meta.isPolicy()) {
				if (meta.getState() == MetaLibrary.STATE_LOADED) {
					if (meta.getProjectHolder() instanceof DataProject) {
						DataProject proj = (DataProject) meta
								.getProjectHolder();
						if (proj.hasColoringData()) {
							hideItem = new JMenuItem(new MetaHideAction(proj
									.getType(), meta, false,
									StandardImages.System_Icon));
							hideMenu.add(hideItem);

							showItem = new JMenuItem(new MetaHideAction(proj
									.getType(), meta, true,
									StandardImages.System_Icon));
							showMenu.add(showItem);

							colorItem = new JMenuItem(new MetaColoringAction(
									proj.getType(), meta,
									StandardImages.System_Icon));
							levelColor.add(colorItem);
						}
					} else {
						OpdProject proj = (OpdProject) meta.getProjectHolder();
						hideItem = new JMenuItem(new MetaHideAction(proj
								.getName(), meta, false,
								StandardImages.System_Icon));
						hideMenu.add(hideItem);

						showItem = new JMenuItem(new MetaHideAction(proj
								.getName(), meta, true,
								StandardImages.System_Icon));
						showMenu.add(showItem);

						colorItem = new JMenuItem(new MetaColoringAction(proj
								.getName(), meta, StandardImages.System_Icon));
						levelColor.add(colorItem);
					}
				}
			}
		}

		iter = Collections.list(
				myProject.getMetaLibraries(MetaLibrary.TYPE_POLICY)).iterator();
		ArrayList<Role> insertRolesThings = new ArrayList<Role>();

		while (iter.hasNext()) {
			MetaLibrary meta = (MetaLibrary) iter.next();
			if (!meta.isPolicy()) {
				if (meta.getState() == MetaLibrary.STATE_LOADED) {
					// check for connected roles
					try {
						Iterator rolesIter = meta.getRolesCollection()
								.iterator();
						JMenu metaMenu = new JMenu(meta.getName());
						metaMenu.setIcon(StandardImages.System_Icon);
						metaMenu.getPopupMenu().setLayout(
								new VerticalGridLayout(menuItemInAColomn, 0));
						while (rolesIter.hasNext()) {
							Role role = (Role) rolesIter.next();
							if (role.getThing() instanceof OpmThing) {
								insertRolesThings.add(role);
							}
						}
						Collections.sort(insertRolesThings);
						for (int i = 0; i < insertRolesThings.size(); i++) {
							Icon icon;
							if (insertRolesThings.get(i).getThing() instanceof OpmProcess) {
								icon = RepositoryImages.PROCESS;

							} else {
								icon = RepositoryImages.OBJECT;
							}
							JMenuItem roleItem = new JMenuItem(
									new MetaInsertAction(insertRolesThings
											.get(i), icon));
							metaMenu.add(roleItem);
						}
						implemMenu.add(metaMenu);
					} catch (Exception ex) {
						OpcatLogger.logError(ex);
					}
				}
			}
		}
		// this.add(new JSeparator());
		add(implemMenu);
		// add(processAction);
		// add(new JSeparator());
		// generateOPLAction.setEnabled(false);
		// add(generateOPLAction);
		this.add(new JSeparator());
		/**
		 * too long running time about 15 min to arrange a sd on the ABS
		 */
		// this.add(new AutoArrangeAction(prj.getCurrentOpd()));
		// this.add(new JSeparator());
		/**
		 * 
		 */
		this.add(this.propertiesAction);

		if (prj.getCurrentOpd().isViewZoomIn()) {
			this.add(new JSeparator());
			this.add(this.zoomOutAction);
		}
	}

	Action propertiesAction = new AbstractAction("Properties",
			StandardImages.PROPERTIES) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public void actionPerformed(ActionEvent e) {
			ProjectPropertiesDialog ppd = new ProjectPropertiesDialog(
					OpdPopup.this.myProject, Opcat2.getFrame(),
					"Project Properties");
			ppd.showDialog();
			OpdPopup.this.myProject.getCurrentOpd().getDrawingArea().repaint();
			GuiControl gui = GuiControl.getInstance();
			gui.getRepository().rebuildTrees(true);
		}
	};
	Action zoomOutAction = new AbstractAction("Normal Size",
			StandardImages.ZOOM_OUT) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public void actionPerformed(ActionEvent e) {
			OpdPopup.this.myProject.getCurrentOpd().setViewZoomIn(false);
		}
	};

	Action deleteAction = new AbstractAction("Delete OPD",
			StandardImages.DELETE) {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1284971987046026420L;

		public void actionPerformed(ActionEvent e) {
			// JOptionPane.showMessageDialog(Opcat2.getFrame(), "This option
			// not
			// impemented yet", "Opcat2 - tmp message",
			// JOptionPane.INFORMATION_MESSAGE);

			try {

				GuiControl gui = GuiControl.getInstance();
				gui.getGlassPane().setVisible(true);
				gui.getGlassPane().start();
				CheckResult cr = CheckModule.checkOpdDeletion(
						OpdPopup.this.myProject.getCurrentOpd(),
						OpdPopup.this.myProject);

				if (cr.getResult() == IXCheckResult.WRONG) {
					JOptionPane.showMessageDialog(Opcat2.getFrame(), cr
							.getMessage(), "Opcat2 - Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (cr.getResult() == IXCheckResult.WARNING) {
					int retValue = JOptionPane.showConfirmDialog(Opcat2
							.getFrame(), cr.getMessage()
							+ "\n Do you want to continue?",
							"Opcat2 - Warning", JOptionPane.YES_NO_OPTION);

					if (retValue == JOptionPane.NO_OPTION) {
						return;
					}
				}

				myProject.getExposeManager().startCompundChange();
				OpdPopup.this.myProject.deleteOpd(OpdPopup.this.myProject
						.getCurrentOpd());
				myProject.getExposeManager().endCompundChange();

				gui.getGlassPane().stop();
				// myProject.getCurrentOpd().getDrawingArea().repaint();
			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			} finally {
				myProject.getExposeManager().endCompundChange();
				GuiControl.getInstance().getGlassPane().stop();
			}
		}

	};

}