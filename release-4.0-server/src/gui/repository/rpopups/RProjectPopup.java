package gui.repository.rpopups;

import gui.Opcat2;
import gui.actions.reports.ProjectSummaryAction;
import gui.images.standard.StandardImages;
import gui.opdProject.OpdProject;
import gui.opdProject.ProjectPropertiesDialog;
import gui.repository.BaseView;
import gui.repository.Repository;
import gui.util.opcatGrid.GridPanel;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JSeparator;

public class RProjectPopup extends RDefaultPopup {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	OpdProject myProject;

	Repository repo;

	Object[] row = { "", "", "", "", "", "" };

	Object[] tag = { "", "" };

	GridPanel results = null;

	public RProjectPopup(BaseView view, Repository rep, OpdProject prj) {
		super(view, prj);

		this.myProject = (OpdProject) this.userObject;
		this.repo = rep;
		this.add(new JSeparator());
		this.add(this.propertiesAction);
		this.add(new JSeparator());
		this.add(this.SummaryAction);
		this.add(new JSeparator());

		this.add(this.RefrashAction);

		this.addCollapseExpand();

	}

	Action propertiesAction = new AbstractAction("Project Properties",
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
					RProjectPopup.this.myProject, Opcat2.getFrame(),
					"Project Properties");
			ppd.showDialog();
		}
	};

	Action SummaryAction = new AbstractAction("Project Summary",
			StandardImages.REPORT) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public void actionPerformed(ActionEvent e) {
			// print the project grid. includeing summaries ?

			ProjectSummaryAction summary = new ProjectSummaryAction();
			summary.actionPerformed(null);
		}
	};

	Action RefrashAction = new AbstractAction("Refresh View",
			StandardImages.EMPTY) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public void actionPerformed(ActionEvent e) {
			repo.rebuildTrees(true);
		}
	};

}