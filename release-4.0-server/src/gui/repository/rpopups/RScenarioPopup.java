package gui.repository.rpopups;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

import gui.Opcat2;
import gui.controls.FileControl;
import gui.controls.GuiControl;
import gui.images.standard.StandardImages;
import gui.metaLibraries.logic.MetaLibrary;
import gui.opdProject.OpdProject;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.StateEntry;
import gui.repository.BaseView;
import gui.scenarios.Scenario;
import gui.scenarios.ScenarioSettingsDialog;
import gui.scenarios.ScenarioUtils;
import gui.scenarios.TestingScenariosPanel;

public class RScenarioPopup extends RDefaultPopup {

	Scenario scen;

	public RScenarioPopup(BaseView view, OpdProject prj) {
		super(view, prj);

		this.add(this.addTestingSettings);
		this.add(new JSeparator());
		this.add(this.editScenrio);
		this.add(new JSeparator());
		this.add(this.loadScenrio);
		this.add(new JSeparator());
		this.add(this.deleteScenrio);

		scen = (Scenario) userObject;
	}

	/**
	 * 
	 */

	Action loadScenrio = new AbstractAction("Load", StandardImages.EMPTY) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public void actionPerformed(ActionEvent e) {
			HashMap initialSce = (HashMap) scen.getInitialObjectsHash();
			HashMap finalSce = (HashMap) scen.getFinalObjectsHash();
			boolean noInitial = false;
			boolean noFinal = false;

			Iterator keys = initialSce.keySet().iterator();

			if (!keys.hasNext())
				noInitial = true;
			while (keys.hasNext()) {
				Long objID = (Long) keys.next();
				ObjectEntry ent = (ObjectEntry) myProject.getSystemStructure()
						.getEntry(objID.longValue());
				if (ent != null) {
					Iterator iter = Collections.list(ent.getStates())
							.iterator();
					while (iter.hasNext()) {
						StateEntry state = (StateEntry) iter.next();
						if (state.getId() == ((Long) initialSce.get(objID))
								.longValue()) {
							state.setInitial(true);
						} else {
							state.setInitial(false);
						}
					}
				} else {
					initialSce.remove(objID);
					keys = initialSce.keySet().iterator();
				}
			}

			keys = finalSce.keySet().iterator();
			if (!keys.hasNext())
				noFinal = true;
			while (keys.hasNext()) {
				Long objID = (Long) keys.next();
				ObjectEntry ent = (ObjectEntry) myProject.getSystemStructure()
						.getEntry(objID.longValue());
				if (ent != null) {
					Iterator iter = Collections.list(ent.getStates())
							.iterator();
					while (iter.hasNext()) {
						StateEntry state = (StateEntry) iter.next();
						if (state.getId() == ((Long) finalSce.get(objID))
								.longValue()) {
							state.setFinal(true);
						} else {
							state.setFinal(false);
						}
					}
				} else {
					finalSce.remove(objID);
					keys = finalSce.keySet().iterator();
				}
			}

			Iterator objectIter = myProject.getSystemStructure()
					.GetObjectEntries();
			while (objectIter.hasNext()) {
				ObjectEntry ent = (ObjectEntry) objectIter.next();
				if (ent.hasStates()) {
					Iterator states = Collections.list(ent.getStates())
							.iterator();
					while (states.hasNext()) {
						StateEntry sta = (StateEntry) states.next();

						if (noInitial) {
							sta.setInitial(false);
						}

						if (noFinal) {
							sta.setFinal(false);
						}
					}
				}
			}
			Opcat2.getFrame().repaint();

			view.rebuildTree(myProject.getSystemStructure(), myProject);
		}
	};

	Action addTestingSettings = new AbstractAction("Settings",
			StandardImages.EMPTY) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public void actionPerformed(ActionEvent e) {

			FileControl file = FileControl.getInstance();
			OpdProject currentProject = file.getCurrentProject();

			ScenarioSettingsDialog libsLoad = new ScenarioSettingsDialog(scen
					.getSettings(), currentProject, Opcat2.getFrame());

			if (libsLoad.showDialog()) {
				file.refreshMetaLibraries(scen.getSettings(), libsLoad
						.getUpdatedLibs());
			}

			for (Enumeration i = scen.getSettings().getMetaLibraries(); i
					.hasMoreElements();) {
				MetaLibrary meta = (MetaLibrary) i.nextElement();
				try {
					if (myProject.getMetaManager().getMetaLibrary(
							meta.getID()) == null) {
						//myProject.getMetaManager().addMetaLibrary(meta);
					}					
				} catch (Exception ex) {
					//
				}
			}

			view.rebuildTree(myProject.getSystemStructure(), myProject);
			
			GuiControl.getInstance().hideMetaWaitMessage() ; 
		}
	};

	private static final long serialVersionUID = 1L;

	Action editScenrio = new AbstractAction("Edit", StandardImages.EMPTY) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public void actionPerformed(ActionEvent e) {

			TestingScenariosPanel panel = ScenarioUtils.getScenaioPanel(scen, null);
			panel.AddToExtensionToolsPanel();

			view.rebuildTree(myProject.getSystemStructure(), myProject);
		}
	};

	Action deleteScenrio = new AbstractAction("Delete", StandardImages.EMPTY) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public void actionPerformed(ActionEvent e) {
			int sure = JOptionPane.showConfirmDialog(Opcat2.getFrame(),
					"Delete this scenario ?", "OPCAT II",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

			if (sure == JOptionPane.YES_OPTION) {
				myProject.getScen().remove(scen.getId());
				view.rebuildTree(myProject.getSystemStructure(), myProject);
			}
		}
	};

}
