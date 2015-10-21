package gui.actions.file;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

import extensionTools.sqlGeneration.gui.startForm;

public class GenerateSQL extends FileAction {

	private static final long serialVersionUID = 5332772446749038992L;

	public GenerateSQL(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {

		startForm sql = new startForm("SQL GEneration");
		sql.start();

	}
}
