package gui.repository.rpopups;

import gui.Opcat2;
import gui.controls.FileControl;
import gui.images.standard.StandardImages;
import gui.metaLibraries.logic.MetaLibrary;
import gui.metaLibraries.logic.Role;
import gui.opdProject.OpdProject;
import gui.projectStructure.ConnectionEdgeInstance;
import gui.projectStructure.Instance;
import gui.projectStructure.ThingEntry;
import gui.projectStructure.ThingInstance;
import gui.repository.BaseView;
import gui.util.OpcatLogger;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JSeparator;
import javax.swing.tree.DefaultMutableTreeNode;

public class RMetaThingPopup extends RDefaultPopup {

	private static final long serialVersionUID = 1L;

	ThingEntry myEntry;

	MetaLibrary meta;

	public RMetaThingPopup(BaseView view, OpdProject prj) {
		super(view, prj);
		this.myEntry = (ThingEntry) this.userObject;
		this.meta = (MetaLibrary) ((DefaultMutableTreeNode) this.selectedPath
				.getParentPath().getLastPathComponent()).getUserObject();
		this.add(new JSeparator());
		this.add(this.openAction);
		// this.add(this.propertiesAction);
		this.add(new JSeparator());
		this.add(this.copyAction);
		this.addCollapseExpand();
	}

	Action copyAction = new AbstractAction("Insert", StandardImages.COPY) {

		private static final long serialVersionUID = -9011119202463736832L;

		public void actionPerformed(ActionEvent e) {

			myProject.clearClipBoard();

			try {
				Role role = new Role(myEntry.getId(), meta);
				ConnectionEdgeInstance addedProcessInstance;
				Instance selected = Opcat2.getCurrentProject().getCurrentOpd()
						.getSelectedItem();
				ThingInstance mainInstance = Opcat2.getCurrentProject()
						.getCurrentOpd().getMainInstance();

				if ((mainInstance != null)
						&& (selected != null)
						&& (selected instanceof ThingInstance)
						&& (selected.getKey().getEntityInOpdId() == mainInstance
								.getKey().getEntityInOpdId())) {
					addedProcessInstance = MetaLibrary.insertConnectionEdge(
							role, myProject.getCurrentOpd(), true, null,
							mainInstance, false);

				} else {
					addedProcessInstance = MetaLibrary.insertConnectionEdge(
							role, myProject.getCurrentOpd(), true, null, null,
							false);
				}

				addedProcessInstance.setTemplateInstance(true);

				addedProcessInstance.getOpd().getDrawingArea().repaint();
			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			}

			myProject.setCanClose(false);

		}
	};

	Action propertiesAction = new AbstractAction("Properties",
			StandardImages.PROPERTIES) {

		private static final long serialVersionUID = 4806047327580173904L;

		public void actionPerformed(ActionEvent e) {

		}
	};

	Action openAction = new AbstractAction("Show", StandardImages.PROPERTIES) {

		private static final long serialVersionUID = 4806047327580173904L;

		public void actionPerformed(ActionEvent e) {
			FileControl.getInstance().runNewOPCAT(new File(meta.getPath()),
					myEntry.getId());
		}
	};

}