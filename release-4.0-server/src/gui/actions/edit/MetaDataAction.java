package gui.actions.edit;

import gui.controls.FileControl;
import gui.controls.GuiControl;
import gui.dataProject.DataProject;
import gui.dataProject.MetaDataGridPanels;
import gui.metaLibraries.logic.MetaLibrary;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;

public class MetaDataAction extends AbstractAction{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 * 
		 */

		private FileControl file = FileControl.getInstance();

		private GuiControl gui = GuiControl.getInstance();

		public MetaDataAction(String name, Icon icon) {
			super(name, icon);
		}	
		
		public void actionPerformed(ActionEvent action) {

			if (!this.file.isProjectOpened()) {
				JOptionPane
						.showMessageDialog(this.gui.getFrame(),
								"No system is opened", "Message",
								JOptionPane.ERROR_MESSAGE);
				return;
			} 
			MetaDataGridPanels.getInstance().showMetaDataProjects() ; 

		}
		
		public void showMeta(MetaLibrary ont, DataProject metadata) {
			
			if (!this.file.isProjectOpened()) {
				JOptionPane
						.showMessageDialog(this.gui.getFrame(),
								"No system is opened", "Message",
								JOptionPane.ERROR_MESSAGE);
				return;
			}
			MetaDataGridPanels.getInstance().showMeta(ont, metadata) ; 			
			
		}

}
