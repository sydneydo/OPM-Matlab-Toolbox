package gui.actions.file;

import gui.Opcat2;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

/**
 * Loads a file from the <code>previous files</code> list. The class
 * constructor recieves the name of the file, and sends it to the opening
 * method.
 * 
 * @see gui.controls.file#loadOpxFile
 * @author Eran Toch
 */
public class LoadPrevious extends FileAction {
	 

	/**
	 * 
	 */
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Stores the designated file name.
	 */
	private String fileName = "";

	/**
	 * The consructor recieves a file name, in addition to the regular
	 * parameters passed to the <code>AbstractAction</code> constructors.
	 * 
	 * @param fileName
	 *            The name (full path) of the file that would be opened when
	 *            this action will be performed.
	 * 
	 */
	public LoadPrevious(String name, Icon icon, String fileName) {
		super(name, icon);
		this.fileName = fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		if (!this.file.handleOpenedSystem()) {
			return;
		}
		Thread runner = new Thread() {
			public void run() {
				Opcat2.setLoadedfilename(LoadPrevious.this.fileName) ; 
				LoadPrevious.this.file.loadOpxFile(LoadPrevious.this.fileName, true);
			}
		};

		runner.start();

	}

}
