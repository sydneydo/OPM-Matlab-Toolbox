package gui.repository.rpopups;

import gui.images.standard.StandardImages;
import gui.opdProject.OpdProject;
import gui.repository.BaseView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class RDefaultPopup extends JPopupMenu implements ActionListener,
		ItemListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	protected OpdProject myProject;

	protected BaseView view;

	protected TreePath selectedPath;
	
	protected DefaultTreeModel treeModel;

	protected Object userObject;

	public RDefaultPopup(BaseView view, OpdProject prj) {
		// super(((ThingInstance)(prj.getCurrentOpd().getSelectedItem())).getThing().getEntity().getEntityName());
		// setSelectionModel(new MySingleSelectionModel());
		this.myProject = prj;
		this.view = view;
		this.selectedPath = view.getSelectionPath();
		this.treeModel = (DefaultTreeModel) view.getModel();
		this.userObject = ((DefaultMutableTreeNode) this.selectedPath
				.getLastPathComponent()).getUserObject();
	}

	Action expandAction = new AbstractAction("Expand", StandardImages.EMPTY) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public void actionPerformed(ActionEvent e) {
			RDefaultPopup.this.view.expandPath(RDefaultPopup.this.selectedPath);
		}
	};

	Action collapseAction = new AbstractAction("Collapse", StandardImages.EMPTY) {

		/**
		 * 
		 */
		private static final long serialVersionUID = -28111996587881578L;

		public void actionPerformed(ActionEvent e) {
			RDefaultPopup.this.view
					.collapsePath(RDefaultPopup.this.selectedPath);
		}
	};

	protected void addCollapseExpand() {
		if (!this.treeModel.isLeaf(this.selectedPath.getLastPathComponent())) {
			this.add(new JSeparator());
			if (this.view.isExpanded(this.selectedPath)) {
				this.add(this.collapseAction);
			} else // collapsed
			{
				this.add(this.expandAction);
			}
		}
	}

	public void actionPerformed(ActionEvent arg0) {

	}

	public void itemStateChanged(ItemEvent arg0) {

	}

}