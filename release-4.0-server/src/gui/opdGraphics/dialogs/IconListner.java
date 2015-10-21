package gui.opdGraphics.dialogs;

import gui.Opcat2;
import gui.projectStructure.Entry;
import gui.util.CustomFileFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

public class IconListner implements ActionListener {
	
	Entry myEntry ; 
	OpcatDialog myDialog ; 
	
	public IconListner(Entry entry , OpcatDialog entryDialog) {
		myDialog = entryDialog ; 
		myEntry = entry ; 
	}
	
	
	public void actionPerformed(ActionEvent e) {
		JFileChooser myFileChooser = new JFileChooser();
		if (myEntry.isIconSet()) {
			myFileChooser.setSelectedFile(new File(myEntry.getIconPath()));

		} else {
			myFileChooser.setSelectedFile(new File(""));
		}

		myFileChooser.resetChoosableFileFilters();
		myFileChooser.removeChoosableFileFilter(myFileChooser
				.getAcceptAllFileFilter());

		CustomFileFilter myFilter;

		String[] exts = { "jpeg", "jpg", "png", "bmp" };
		myFilter = new CustomFileFilter(exts, "Image Files");
		myFileChooser.addChoosableFileFilter(myFilter);

		int retVal = myFileChooser.showDialog(Opcat2.getFrame(), "Load");

		if (retVal != JFileChooser.APPROVE_OPTION) {
			return;
		} else {
			myDialog.getIconPath().setText(myFileChooser
					.getSelectedFile().getPath());
		}
		return;
	}
}
