package gui.actions.svn;

import gui.Opcat2;
import gui.controls.FileControl;
import gui.controls.GuiControl;
import gui.util.OpcatFile;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JOptionPane;

public class SvnAddLocalDirectoryAction extends SvnAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OpcatFile file;

	public SvnAddLocalDirectoryAction(OpcatFile file, String name, Icon icon) {
		super(name, icon);
		this.file = file;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		File dir = null;
		String msg = "Enter name";
		while ((msg != null) && ((dir == null) || (dir.exists()))) {
			String title = "Enter Name";
			if (dir != null) {
				title = msg + " Exists";
			}
			msg = JOptionPane.showInputDialog(Opcat2.getFrame(), title, msg);
			dir = new File(file.getPath() + FileControl.fileSeparator + msg);

		}

		if (msg != null) {
			dir.mkdirs();
			GuiControl.getInstance().getRepository().repaint();
		}
		GuiControl.getInstance().getRepository().getModelsView().repaintKeepOpen(); 
		//

		// JOptionPane.showConfirmDialog(Opcat2.getFrame(),
		// "Not Implemented Yet");
	}

}
