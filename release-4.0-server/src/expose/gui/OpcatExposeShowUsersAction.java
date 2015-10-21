package expose.gui;

import expose.OpcatExposeConstants;
import expose.OpcatExposeEntity;
import expose.OpcatExposeKey;
import expose.OpcatExposeManager;
import expose.OpcatExposeUser;
import gui.Opcat2;
import gui.actions.OpcatAction;
import gui.controls.FileControl;
import gui.projectStructure.Entry;
import gui.util.OpcatLogger;
import gui.util.opcatGrid.GridPanel;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import messages.OpcatMessage;
import messages.OpcatMessagesManager;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_OP;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_SEVIRITY;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_SUBSYSTEMS;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_SYSTEMS;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_TYPE;
import modelControl.OpcatMCManager;
import user.OpcatUser;

public class OpcatExposeShowUsersAction extends OpcatAction {

	GridPanel myPanel;
	OpcatExposeKey key;
	ArrayList<String> cols;

	static final String messages_menu_text = "Messages";
	static final String interface_change_text = "Interface need Changes";
	static final String non_interface_change_text = "Non Interface changes";
	static final String realese_text = "Release request";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OpcatExposeShowUsersAction(String name, Icon icon, Entry myEntry) {
		super(name, icon);

		try {
			this.key = new OpcatExposeKey(new File(myEntry.getMyProject()
					.getPath()), myEntry.getId(), false);
		} catch (Exception e) {
			OpcatLogger.logError(e);
		}
		cols = new ArrayList<String>();
		cols.add("Expose ID");
		cols.add("Model");
		cols.add("MC Path");
		cols.add("Last User");

		myPanel = new GridPanel(cols);
		myPanel.setTabName(myEntry.getName() + " users ");
		myPanel.getGrid().addMouseListener(new MouseListner(myPanel));
	}

	public OpcatExposeShowUsersAction(String name, Icon icon,
			OpcatExposeKey key, String entityName) {
		super(name, icon);

		try {
			this.key = key;
		} catch (Exception e) {
			OpcatLogger.logError(e);
		}
		cols = new ArrayList<String>();
		cols.add("Expose ID");
		cols.add("Model");
		cols.add("MC Path");
		cols.add("Last User");

		myPanel = new GridPanel(cols);
		myPanel.setTabName(entityName + " users ");
		myPanel.getGrid().addMouseListener(new MouseListner(myPanel));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {

			super.actionPerformed(e);
			if (OpcatMCManager.isOnline()) {

				ArrayList<OpcatExposeUser> users = FileControl.getInstance()
						.getCurrentProject().getExposeManager().getExposeUsage(
								key, true, true);

				for (OpcatExposeUser user : users) {
					Object[] rowTag = new Object[2];
					rowTag[0] = user;
					rowTag[1] = key;

					Object[] row = new Object[cols.size()];

					long id = OpcatExposeManager.getExposeServerID(key);
					if (id == OpcatExposeConstants.EXPOSE_UNDEFINED_ID) {
						id = 0;
					}

					row[0] = id;
					row[1] = user.getModelName();
					row[2] = user.getModelPath();
					row[3] = user.getUserID();

					myPanel.getGrid().addRow(row, rowTag, false);

				}

				myPanel.AddToExtensionToolsPanel();

			} else {
				throw new Exception("Can not show Expose list while off-line");
			}
		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
	}

	private void rightClickEvent(GridPanel panel, MouseEvent e) {
		JPopupMenu popup = panel.getRMenu();

		if (panel.getGrid().getSelectedRow() >= 0) {

			Object[] tag = panel.getSelectedTag();
			OpcatExposeUser user = (OpcatExposeUser) tag[0];
			OpcatExposeKey key = (OpcatExposeKey) tag[1];

			JMenu messages = new JMenu(messages_menu_text);

			JMenuItem interface_message = new JMenuItem(interface_change_text);
			interface_message.addActionListener(new myAction(key, user));
			messages.add(interface_message);

			JMenuItem non_interface_message = new JMenuItem(
					non_interface_change_text);
			non_interface_message.addActionListener(new myAction(key, user));
			messages.add(non_interface_message);

			JMenuItem release_message = new JMenuItem(realese_text);
			release_message.addActionListener(new myAction(key, user));
			messages.add(release_message);

			popup.add(messages);
			popup.add(new JSeparator());
		}

		popup.show(e.getComponent(), e.getX(), e.getY());

	}

	public void handleRegulerMessage(OpcatExposeKey key, OpcatExposeUser user) {
		try {
			OpcatExposeEntity entity = OpcatExposeManager.getServerExpose(key);
			if (entity == null) {
				// private expose
				JOptionPane.showMessageDialog(Opcat2.getFrame(),
						"Can not send a message to a local entity");
				return;
			}
			
			String message = JOptionPane
					.showInputDialog("Expose Change Needed");
			if (message != null) {
				OPCAT_MESSAGES_OP op = OPCAT_MESSAGES_OP.EXPOSE_CHANGE_REQUEST;
				String subject = entity.getOpmEntityName() + "@"
						+ entity.getKey().getModelDecodedURI();
				OPCAT_MESSAGES_TYPE type = OPCAT_MESSAGES_TYPE.ENTITY;
				OPCAT_MESSAGES_SUBSYSTEMS subsystem = OPCAT_MESSAGES_SUBSYSTEMS.EXPOSE;
				OPCAT_MESSAGES_SEVIRITY sevi = OPCAT_MESSAGES_SEVIRITY.WARNING;
				OPCAT_MESSAGES_SYSTEMS system = OPCAT_MESSAGES_SYSTEMS.MODELS;

				String username = user.getUserID();

				OpcatMessage opcatMessage = new OpcatMessage(OpcatUser
						.getCurrentUser().getName(), username, type, type,
						subsystem, subsystem, system, sevi, op, subject,
						message);
				OpcatMessagesManager.getInstance().sendMessage(opcatMessage);
			}
		} catch (Exception e1) {
			OpcatLogger.logError(e1);
		}
	}

	public void handleActionNeededMessage(OpcatExposeKey key,
			OpcatExposeUser user) {

		try {
			OpcatExposeEntity entity = OpcatExposeManager.getServerExpose(key);
			if (entity == null) {
				// private expose
				JOptionPane.showMessageDialog(Opcat2.getFrame(),
						"Can not send a message to a local entity");
				return;
			}

			String message = JOptionPane
					.showInputDialog("Interface Change Needed");
			if (message != null) {
				OPCAT_MESSAGES_OP op = OPCAT_MESSAGES_OP.EXPOSE_INTERFACE_CHANGE_REQUEST;
				String subject = entity.getOpmEntityName() + "@"
						+ entity.getKey().getModelDecodedURI();
				OPCAT_MESSAGES_TYPE type = OPCAT_MESSAGES_TYPE.ENTITY;
				OPCAT_MESSAGES_SUBSYSTEMS subsystem = OPCAT_MESSAGES_SUBSYSTEMS.EXPOSE;
				OPCAT_MESSAGES_SEVIRITY sevi = OPCAT_MESSAGES_SEVIRITY.ACTION_NEEDED;
				OPCAT_MESSAGES_SYSTEMS system = OPCAT_MESSAGES_SYSTEMS.MODELS;

				String username = user.getUserID();

				OpcatMessage opcatMessage = new OpcatMessage(OpcatUser
						.getCurrentUser().getName(), username, type, type,
						subsystem, subsystem, system, sevi, op, subject,
						message);
				OpcatMessagesManager.getInstance().sendMessage(opcatMessage);
			}
		} catch (Exception e1) {
			OpcatLogger.logError(e1);
		}

	}

	public void handleRealeseMessage(OpcatExposeKey key, OpcatExposeUser user) {

		try {
			OpcatExposeEntity entity = OpcatExposeManager.getServerExpose(key);
			if (entity == null) {
				// private expose
				JOptionPane.showMessageDialog(Opcat2.getFrame(),
						"Can not send a message to a local entity");
				return;
			}

			String message = JOptionPane.showInputDialog("Expose Release");
			if (message != null) {
				OPCAT_MESSAGES_OP op = OPCAT_MESSAGES_OP.EXPOSE_RELEASE_REQUEST;
				String subject = entity.getOpmEntityName() + "@"
						+ entity.getKey().getModelDecodedURI();
				OPCAT_MESSAGES_TYPE type = OPCAT_MESSAGES_TYPE.ENTITY;
				OPCAT_MESSAGES_SUBSYSTEMS subsystem = OPCAT_MESSAGES_SUBSYSTEMS.EXPOSE;
				OPCAT_MESSAGES_SEVIRITY sevi = OPCAT_MESSAGES_SEVIRITY.ACTION_NEEDED;
				OPCAT_MESSAGES_SYSTEMS system = OPCAT_MESSAGES_SYSTEMS.MODELS;

				String username = user.getUserID();

				OpcatMessage opcatMessage = new OpcatMessage(OpcatUser
						.getCurrentUser().getName(), username, type, type,
						subsystem, subsystem, system, sevi, op, subject,
						message);
				OpcatMessagesManager.getInstance().sendMessage(opcatMessage);
			}
		} catch (Exception e1) {
			OpcatLogger.logError(e1);
		}

	}

	class MouseListner extends MouseAdapter {
		GridPanel panel;

		public MouseListner(GridPanel panel) {
			super();
			this.panel = panel;
		}

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				// showThings();
			}
			if (e.getButton() == MouseEvent.BUTTON3) {
				rightClickEvent(panel, e);
			}
		}
	}

	class myAction extends OpcatAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		OpcatExposeKey key;
		OpcatExposeUser user;

		public myAction(OpcatExposeKey key, OpcatExposeUser user) {
			this.key = key;
			this.user = user;
		}

		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);

			if (e.getSource() instanceof JMenuItem) {
				JMenuItem item = (JMenuItem) e.getSource();

				if (item.getText().equalsIgnoreCase(interface_change_text)) {
					handleActionNeededMessage(key, user);
				}

				if (item.getText().equalsIgnoreCase(non_interface_change_text)) {
					handleRegulerMessage(key, user);
				}

				if (item.getText().equalsIgnoreCase(realese_text)) {
					handleRealeseMessage(key, user);
				}

			}
		}

	}

}
