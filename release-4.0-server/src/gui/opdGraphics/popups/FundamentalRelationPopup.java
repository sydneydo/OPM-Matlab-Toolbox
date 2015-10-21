package gui.opdGraphics.popups;

import gui.images.standard.StandardImages;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;
import gui.opdProject.OpdProject;
import gui.projectStructure.FundamentalRelationInstance;
import gui.projectStructure.Instance;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JSeparator;

public class FundamentalRelationPopup extends DefaultPopup {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	FundamentalRelationInstance fundamentalRelationInstance ; 
	
	public FundamentalRelationPopup(OpdProject prj, Instance inst) {
		super(prj, inst);
		fundamentalRelationInstance = (FundamentalRelationInstance) inst ; 

		this.copyAction.setEnabled(false);
		this.add(this.copyAction);
		// cutAction.setEnabled(false);
		// add(cutAction);

		this.add(this.deleteAction);
		this.add(new JSeparator());
		this.add(this.propertiesAction);
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
			fundamentalRelationInstance.getGraphicalRelationRepresentation().getRelation()
					.callPropertiesDialog(BaseGraphicComponent.SHOW_ALL_TABS,
							BaseGraphicComponent.SHOW_ALL_BUTTONS);
			FundamentalRelationPopup.this.myProject.getCurrentOpd().getDrawingArea().repaint();
		}
	};
}