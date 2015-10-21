package gui.views.gui;

import java.awt.event.ActionEvent;

import gui.controls.FileControl;
import gui.controls.GuiControl;
import gui.opdProject.Opd;
import gui.projectStructure.ThingInstance;
import gui.util.OpcatLogger;
import gui.views.OpcatViewSet;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

public class ComponentInViewMenuAction extends JMenuItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int CREATE_UNFOLD_OPD = 1;
	public static final int CREATE_NEW_OPD_WiTH_THING = 2;

	/**
	 * if view name is not found in viewSet then a new view will be created
	 * 
	 * @param comp
	 * @param viewName
	 */

	public ComponentInViewMenuAction(int action, ThingInstance thingInstance,
			String viewName) {

		super();

		if (action == CREATE_UNFOLD_OPD) {
			this.setAction(new UnfoldAction(thingInstance, viewName));
		}

		if (action == CREATE_NEW_OPD_WiTH_THING) {
			this.setAction(new NewOPDAction(thingInstance, viewName));
		}
	}

	private class UnfoldAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		UnfoldAction(ThingInstance instance, String viewName) {
			super(viewName);
		}

		@Override
		public void actionPerformed(ActionEvent e) {

		}

	}

	private class NewOPDAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private String viewName = null ; 

		NewOPDAction(ThingInstance instance, String viewName) {
			super(viewName);
			this.viewName = viewName ; 
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			try {

				GuiControl gui = GuiControl.getInstance();
				gui.getGlassPane().setVisible(true);
				gui.getGlassPane().start();

				Opd opd = FileControl.getInstance().getCurrentProject().unfolding();
				OpcatViewSet viewset = OpcatViewSet.getInstance();

				if (!viewset.containsKey(viewName)) {
					viewset.addView(viewName) ; 
					viewset.getView(viewName).addComponent(viewName, opd) ; //.addComponent(viewName, opd) ; 
				}

				gui.getGlassPane().stop();

			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			}
		}

	}

}
