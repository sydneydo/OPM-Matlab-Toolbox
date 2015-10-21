package gui.actions.svn;

import gui.controls.FileControl;
import gui.controls.GuiControl;
import gui.util.OpcatFile;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

import modelControl.OpcatMCManager;

public class SvnAddAction extends SvnAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OpcatFile file;

	public SvnAddAction(OpcatFile file, String name, Icon icon) {
		super(name, icon);
		this.file = file;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		OpcatMCManager.getInstance(true).addEntry(file);

		if (FileControl.getInstance().getCurrentProject() != null) {
			if (file.getPath().equalsIgnoreCase(
					FileControl.getInstance().getCurrentProject().getPath())) {
				FileControl.getInstance().getCurrentProject().setMcURL(
						OpcatMCManager.getInstance(true).getFileURL(file));
			}
		}
		GuiControl.getInstance().getRepository().getModelsView()
				.repaintKeepOpen();
	}

}
