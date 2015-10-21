package modelControl.gui;

import gui.actions.svn.SVNGridCheckOutButtonAction;
import gui.actions.svn.SvnShowEntryInTreeAction;
import gui.util.OpcatLogger;
import gui.util.opcatGrid.GridPanel;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.table.TableColumn;

import modelControl.OpcatMCDirEntry;
import modelControl.OpcatMCManager;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLock;
import org.tmatesoft.svn.core.SVNURL;

import categories.Categories;
import categories.CategoriesType;

import com.sciapp.renderers.NumberRenderer;

public class OpcatCheckoutGrid {

	GridPanel panel;
	SVNURL root;

	public OpcatCheckoutGrid(SVNURL root) {
		super();
		try {
			this.root = root;
			panel = this.InitPanel();
		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}

	}

	private GridPanel InitPanel() throws SVNException {
		ArrayList<String> cols = new ArrayList<String>();

		int numFixedColumns = 6;

		cols.add("Row");
		cols.add("Lock Owner");
		cols.add("Repository Path");
		cols.add("Name");
		cols.add("Author");
		cols.add("Revision");

		// get model categories from file or SVN ?
		// and add categories to grid columns
		cols.addAll(Categories.getNames(
				new CategoriesType(CategoriesType.MODEL)).values());

		GridPanel locPanel;
		locPanel = new GridPanel(cols);
		TableColumn column = locPanel.getGrid().getColumnModel().getColumn(0);
		column.setCellRenderer(new NumberRenderer());

		locPanel.setTabName("Repository Files Properties");

		LinkedList<OpcatMCDirEntry> files = OpcatMCManager.getInstance()
				.getFileListFromRepo(root);

		/**
		 * we have the repo tree, now create the grid with thnow show the grid
		 * on the left of the panel. every click should show the files the right
		 * grid.
		 */

		int i = 0;
		for (OpcatMCDirEntry entry : files) {
			i++;

			Object[] tag = new Object[2];
			Object[] row = new Object[locPanel.getGrid().getColumnCount()];

			// String filePath = FileControl.getInstance().getOPCATDirectory()
			// + FileControl.getInstance().fileSeparator
			// + entry.getRelativePath();
			//
			HashMap<String, String> props = Categories
					.getCategoriesNameValue(entry);

			row[0] = i;

			SVNLock lock = OpcatMCManager.getInstance().getEntryLock(entry);

			if (lock != null) {
				row[1] = lock.getOwner();
			} else {
				row[1] = "None";
			}
			row[2] = entry.getURL().getPath().replaceAll(entry.getName(), "")
					.replaceFirst(entry.getRepositoryRoot().getPath(), "");
			tag[1] = row[2];
			row[3] = entry.getName();
			row[4] = entry.getAuthor();
			row[5] = entry.getRevision();

			if (props != null) {
				for (String prop : props.keySet()) {
					int counter = 0;
					while ((counter < cols.size() - 1)
							&& !cols.get(counter).equalsIgnoreCase(prop)) {
						counter++;
					}
					if (counter != cols.size()) {
						row[counter + numFixedColumns] = props.get(prop);
					}

				}
			}

			tag[0] = entry;
			// tag[1] = " ";
			locPanel.getGrid().addRow(row, tag);
		}

		JButton checkOut = new JButton("Checkout");
		checkOut.addActionListener(new CheckOutButton());
		// locPanel.getButtonPane().add(checkOut);

//		JButton showInTree = new JButton("Show in Tree");
//		showInTree.addActionListener(new ShowInTree());
//		locPanel.getButtonPane().add(showInTree);

		// Grid myGrid = locPanel.getGrid();

		return locPanel;
	}

	public void show() {
		panel.AddToExtensionToolsPanel(true);
	}

	class CheckOutButton implements java.awt.event.ActionListener {

		public CheckOutButton() {

		}

		public void actionPerformed(ActionEvent e) {
			SVNGridCheckOutButtonAction action = new SVNGridCheckOutButtonAction(
					panel);
			action.actionPerformed(e);
		}
	}

	class ShowInTree implements java.awt.event.ActionListener {

		public ShowInTree() {

		}

		public void actionPerformed(ActionEvent e) {
			SvnShowEntryInTreeAction action = new SvnShowEntryInTreeAction(
					"Show in Browser", null, panel);
			action.actionPerformed(e);
		}
	}

}
