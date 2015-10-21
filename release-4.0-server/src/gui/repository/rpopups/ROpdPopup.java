package gui.repository.rpopups;

import exportedAPI.opcatAPIx.IXCheckResult;
import gui.Opcat2;
import gui.checkModule.CheckModule;
import gui.checkModule.CheckResult;
import gui.controls.GuiControl;
import gui.dataProject.DataProject;
import gui.dataProject.DataCreatorType;
import gui.images.repository.RepositoryImages;
import gui.images.standard.StandardImages;
import gui.metaLibraries.logic.MetaLibrary;
import gui.opdGraphics.popups.OpdPopup;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.repository.BaseView;
import gui.util.OpcatLogger;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

public class ROpdPopup extends RDefaultPopup {

	/**
         * 
         */
	private static final long serialVersionUID = 1L;

	/**
         * 
         */

	Opd myOpd;

	public ROpdPopup(BaseView view, OpdProject prj) {
		super(view, prj);
		this.myOpd = (Opd) this.userObject;
		this.add(this.showAction);
		this.add(new JSeparator());
		// String header = myOpd.getName().substring(0,
		// myOpd.getName().indexOf("-"));
		if (myOpd.isView()) {
			this.add(this.renameAction);
			// renameAction.setEnabled(false) ;
		}

		this.add(this.deleteAction);
		this.add(new JSeparator());
		this.add(this.showInGrid);
		// add(new JSeparator());
		// add(generateOPLAction);
		this.addCollapseExpand();
	}

	Action showAction = new AbstractAction("Show", RepositoryImages.OPD) {

		/**
         * 
         */
		private static final long serialVersionUID = 1L;

		/**
         * 
         */

		public void actionPerformed(ActionEvent e) {
			// ROpdPopup.this.myOpd.setVisible(true);
			myProject.showOPD(myOpd.getOpdId());
			// try {
			// ROpdPopup.this.myOpd.getOpdContainer().setSelected(true);
			// ROpdPopup.this.myOpd.getOpdContainer().setMaximum(true);
			// } catch (java.beans.PropertyVetoException pve) {
			// pve.printStackTrace();
			// }
		}
	};

	Action deleteAction = new AbstractAction("Delete", StandardImages.DELETE) {

		/**
         * 
         */
		private static final long serialVersionUID = 8446841991771412186L;

		public void actionPerformed(ActionEvent e) {

			try {
				GuiControl gui = GuiControl.getInstance();
				gui.getGlassPane().setVisible(true);
				gui.getGlassPane().start();
				// JOptionPane.showMessageDialog(Opcat2.getFrame(), "This option
				// not
				// impemented yet", "Opcat2 - tmp message",
				// JOptionPane.INFORMATION_MESSAGE);
				CheckResult cr = CheckModule.checkOpdDeletion(
						ROpdPopup.this.myOpd, ROpdPopup.this.myProject);

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
				ROpdPopup.this.myProject.deleteOpd(ROpdPopup.this.myOpd);
				// gui.getRepository().rebuildTrees(true);
				// myProject.setCanClose(false);
				myProject.getExposeManager().endCompundChange();
				gui.getGlassPane().stop();
			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			} finally {
				myProject.getExposeManager().endCompundChange();
				GuiControl.getInstance().getGlassPane().stop();
			}
		}
	};

	Action renameAction = new AbstractAction("Rename", StandardImages.EMPTY) {

		/**
         * 
         */
		private static final long serialVersionUID = -1;

		public void actionPerformed(ActionEvent e) {

			String header = myOpd.getName().substring(0,
					myOpd.getName().indexOf("-"));

			String ans = JOptionPane.showInputDialog(Opcat2.getFrame(),
					"Rename OPD", myOpd.getName().substring(
							myOpd.getName().indexOf("-") + 2));

			if ((ans != null) && (ans.trim() != null)
					&& (!ans.trim().equalsIgnoreCase(""))) {
				myOpd.setName(header + "- " + ans);
			}

			Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
			myProject.getCurrentOpd().getDrawingArea().repaint();
			GuiControl.getInstance().getRepository().rebuildTrees(true);

			myProject.setCanClose(false);
		}
	};

	// Action pasteAction = new AbstractAction("Paste",
	// StandardImages.PASTE){
	// public void actionPerformed(ActionEvent e){
	// myProject.paste(x, y,myProject.getCurrentOpd().getDrawingArea());
	// myProject.getCurrentOpd().getDrawingArea().repaint();
	// }
	// };

	Action showInGrid = new AbstractAction("Show in Grid", StandardImages.OPEN) {

		/**
         * 
         */
		private static final long serialVersionUID = -1774127394237777292L;

		public void actionPerformed(ActionEvent e) {
			try {
				Object[] source;
				source = new Object[2];

				source[0] = myProject;
				source[1] = new Long(myOpd.getOpdId());
				DataProject metaData = new DataProject(source,
						new DataCreatorType(
								DataCreatorType.DATA_TYPE_OPCAT_OPD,
								DataCreatorType.REFERENCE_TYPE_PRIVATE_FILE));

				MetaLibrary meta = new MetaLibrary(
						MetaLibrary.TYPE_CLASSIFICATION, source,
						DataCreatorType.DATA_TYPE_OPCAT_OPD,
						DataCreatorType.REFERENCE_TYPE_PRIVATE_FILE);

				MetaLibrary oldOpdMete = myProject.getOpdMetaDataManager()
						.findByReferenceID(meta.getID());

				if (oldOpdMete != null) {
					myProject.getOpdMetaDataManager().removeMetaLibrary(
							oldOpdMete.getPath());
				}

				myProject.getOpdMetaDataManager().addMetaLibrary(meta);

				meta.load();

				myProject.getOpdMetaDataManager().refreshRoles(myProject);

				GuiControl file = GuiControl.getInstance();
				file.showMetaData(meta, metaData);
				// metadata.
			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			}
			myProject.showOPD(myOpd.getOpdId());
		}
	};

}