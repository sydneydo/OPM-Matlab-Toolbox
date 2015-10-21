package gui.repository.rpopups;

import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JSeparator;
import gui.actions.file.ReuseImportAction;
import gui.controls.GuiControl;
import gui.dataProject.DataProject;
import gui.dataProject.DataCreatorType;
import gui.images.standard.StandardImages;
import gui.metaLibraries.logic.MetaLibrary;
import gui.opdProject.OpdProject;
import gui.repository.BaseView;
import gui.repository.TestingView;
import gui.util.BrowserLauncher2;
import gui.util.OpcatException;
import gui.util.OpcatLogger;

public class RMetaPopup extends RDefaultPopup {

	/**
         * 
         */
	private static final long serialVersionUID = 1L;

	/**
         * 
         */

	MetaLibrary myMeta;

	public RMetaPopup(BaseView view, OpdProject prj) {
		super(view, prj);
		this.myMeta = (MetaLibrary) this.userObject;
		if (!(view instanceof TestingView))
			this.add(this.importAction);
		if (!(view instanceof TestingView))
			this.add(new JSeparator());
		this.add(this.openAction);
		if (!myMeta.isPolicy()) {
			this.add(this.showInGrid);
		}
		this.addCollapseExpand();
	}

	Action importAction = new AbstractAction("Import", StandardImages.IMPORT) {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			ReuseImportAction imp = new ReuseImportAction("Import Model",
					StandardImages.IMPORT, myMeta);
			try {
				imp.doImport(myMeta.getPath());
			} catch (OpcatException ex) {
				OpcatLogger.logError(ex);
			}
			myProject.setCanClose(false);
		}
	};

	Action openAction = new AbstractAction("Open", StandardImages.OPEN) {
		private static final long serialVersionUID = 8446841991771412186L;

		public void actionPerformed(ActionEvent e) {
			try {
				URL url = new URL("file", "", myMeta.getPath());
				BrowserLauncher2.openURL(url.toExternalForm());
				// FileControl.getInstance().replaceLoadedProject((OpdProject)
				// myMeta.getProjectHolder(), false) ;
			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			}
		}
	};

	Action showInGrid = new AbstractAction("Show in Grid", StandardImages.OPEN) {

		/**
         * 
         */
		private static final long serialVersionUID = -1774127394237777292L;

		public void actionPerformed(ActionEvent e) {
			try {
				DataProject metaData = new DataProject(myMeta
						.getProjectHolder(), new DataCreatorType(
						DataCreatorType.DATA_TYPE_OPCAT_PROJECT, DataCreatorType.REFERENCE_TYPE_TEMPLATE_FILE));
				GuiControl file = GuiControl.getInstance();
				file.showMetaData(myMeta, metaData);
				// metadata.
			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			}
		}
	};

}