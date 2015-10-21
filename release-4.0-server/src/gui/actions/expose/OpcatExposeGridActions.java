package gui.actions.expose;

import expose.OpcatExposeEntity;
import expose.OpcatExposeKey;
import expose.OpcatExposeList;
import expose.gui.OpcatExposeShowUsersAction;
import extensionTools.search.OptionsIsPointer;
import extensionTools.search.SearchAction;
import gui.Opcat2;
import gui.actions.OpcatAction;
import gui.controls.FileControl;
import gui.projectStructure.Instance;
import gui.projectStructure.ThingInstance;
import gui.util.OpcatLogger;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import messages.OpcatMessage;
import messages.OpcatMessagesManager;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_OP;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_SEVIRITY;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_SUBSYSTEMS;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_SYSTEMS;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_TYPE;
import user.OpcatUser;

public class OpcatExposeGridActions extends OpcatAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OpcatExposeGridActions(OpcatExposeList exposed) {
		super();
	}

	private void handleUseExport() {
		OpcatExposeEntity entity = OpcatExposeList.getInstance()
				.getSelectedEntity();
		if (entity == null) {
			return;
		} else {
			Instance ins = FileControl.getInstance().getCurrentProject()
					.getCurrentOpd().getSelectedItem();

			if (ins != null) {
				if (ins.getOpd().getMainInstance() != null) {
					if ((ins.getOpd().getMainInstance().getKey()
							.getEntityInOpdId() != ins.getKey()
							.getEntityInOpdId())) {
						JOptionPane.showMessageDialog(Opcat2.getFrame(),
								"Can not insert Expose into selected Instance");
						return;
					}

				} else {
					JOptionPane.showMessageDialog(Opcat2.getFrame(),
							"Can not insert Expose into selected Instance");
					return;
				}
			}
			if ((ins instanceof ThingInstance) || (ins == null)) {
				ThingInstance thing = (ThingInstance) ins;
				OpcatExposeKey key;
				try {
					key = new OpcatExposeKey(new File(FileControl.getInstance()
							.getCurrentProject().getPath()), entity.getKey()
							.getOpmEntityID(), false);
				} catch (Exception e) {
					OpcatLogger.logError(e);
					return;
				}

				if (entity.getKey().equals(key) && !entity.getKey().isPrivate()) {
					// tring to use entity from same model.
					JOptionPane.showMessageDialog(Opcat2.getFrame(),
							"Use \"Private Expose\" for in-model Things");
					return;

				}
				// if
				// (!FileControl.getInstance().getCurrentProject().isCanClose())
				// {
				// JOptionPane.showMessageDialog(Opcat2.getFrame(),
				// "Save the model before using exposed things");
				// return;
				// }
				FileControl.getInstance().getCurrentProject()
						.getExposeManager().useExpose(
								thing,
								FileControl.getInstance().getCurrentProject()
										.getCurrentOpd(), entity, 100, 100,
								false);
				FileControl.getInstance().getCurrentProject()
						.setCanClose(false);
			} else {
				JOptionPane
						.showMessageDialog(
								Opcat2.getFrame(),
								"Please select destination Objet/Process or un-select all for insertion into Opd");
				return;
			}
		}

	}

	private void handleLocalReport() {
		try {

			OpcatExposeEntity entity = OpcatExposeList.getInstance()
					.getSelectedEntity();
			if (entity == null) {
				return;
			}

			OptionsIsPointer options = new OptionsIsPointer(entity);
			SearchAction search = new SearchAction(null, options, entity
					.getOpmEntityName()
					+ " usage", FileControl.getInstance().getCurrentProject());
			search.show(true);

		} catch (Exception e1) {
			OpcatLogger.logError(e1);
		}
	}

	private void handleGlobalReport() {
		try {
			OpcatExposeEntity entity = OpcatExposeList.getInstance()
					.getSelectedEntity();

			if (entity != null) {
				OpcatExposeShowUsersAction report = new OpcatExposeShowUsersAction(
						"Show public usage", null, entity.getKey(), entity
								.getOpmEntityName());
				report.actionPerformed(null);

			}

		} catch (Exception e) {
			OpcatLogger.logError(e);
		}
	}

	private void handleRefrash() {
		try {
			ArrayList<OpcatExposeEntity> entities;
			entities = FileControl.getInstance().getCurrentProject()
					.getExposeManager().getExposedList(null, true);
			OpcatExposeList.getInstance().refresh(entities);
			OpcatExposeList.getInstance().show(true);
		} catch (Exception e1) {
			OpcatLogger.logError(e1);
		}
	}

	public void handleRegulerMessage() {
		try {
			OpcatExposeEntity entity = OpcatExposeList.getInstance()
					.getSelectedEntity();

			String message = JOptionPane
					.showInputDialog("Expose Change Needed");
			if (message != null) {
				OPCAT_MESSAGES_OP op = OPCAT_MESSAGES_OP.EXPOSE_INTERFACE_CHANGE_REQUEST;
				String subject = entity.getOpmEntityName() + "@"
						+ entity.getKey().getModelDecodedURI();
				OPCAT_MESSAGES_TYPE type = OPCAT_MESSAGES_TYPE.ENTITY;
				OPCAT_MESSAGES_SUBSYSTEMS subsystem = OPCAT_MESSAGES_SUBSYSTEMS.EXPOSE;
				OPCAT_MESSAGES_SEVIRITY sevi = OPCAT_MESSAGES_SEVIRITY.WARNING;
				OPCAT_MESSAGES_SYSTEMS system = OPCAT_MESSAGES_SYSTEMS.MODELS;

				String username = null;
				try {
					username = OpcatUser.getCurrentUser().getName();
					if (username == null) {
						username = entity.getOpmEntityName() + "@"
								+ entity.getKey().getModelDecodedURI();
					}
				} catch (Exception ex) {

				}
				OpcatMessage opcatMessage = new OpcatMessage(username,
						OpcatUser.getCurrentUser().getName(), type, type,
						subsystem, subsystem, system, sevi, op, subject,
						message);
				OpcatMessagesManager.getInstance().sendMessage(opcatMessage);
			}
		} catch (Exception e1) {
			OpcatLogger.logError(e1);
		}
	}

	public void handleActionNeededMessage() {

		try {
			OpcatExposeEntity entity = OpcatExposeList.getInstance()
					.getSelectedEntity();

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

				String username = null;
				try {
					username = OpcatUser.getCurrentUser().getName();
					if (username == null) {
						username = entity.getOpmEntityName() + "@"
								+ entity.getKey().getModelDecodedURI();
					}
				} catch (Exception ex) {

				}
				OpcatMessage opcatMessage = new OpcatMessage(username,
						OpcatUser.getCurrentUser().getName(), type, type,
						subsystem, subsystem, system, sevi, op, subject,
						message);
				OpcatMessagesManager.getInstance().sendMessage(opcatMessage);
			}
		} catch (Exception e1) {
			OpcatLogger.logError(e1);
		}

	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		if (e.getSource() instanceof JButton) {

			JButton butten = (JButton) e.getSource();

			if (butten.getText().equalsIgnoreCase(OpcatExposeList.refresh_text)) {
				handleRefrash();
			}

			if (butten.getText().equalsIgnoreCase(
					OpcatExposeList.useExpose_text)) {
				handleUseExport();
			}
		}
		if (e.getSource() instanceof JMenuItem) {
			JMenuItem item = (JMenuItem) e.getSource();
			if (item.getText().equalsIgnoreCase(
					OpcatExposeList.usageReport_text)) {
				handleLocalReport();
			}

			item = (JMenuItem) e.getSource();
			if (item.getText().equalsIgnoreCase(
					OpcatExposeList.usageGlobalReport_text)) {
				handleGlobalReport();
			}
			if (item.getText().equalsIgnoreCase(OpcatExposeList.useExpose_text)) {
				handleUseExport();
			}

			if (item.getText().equalsIgnoreCase(
					OpcatExposeList.interface_change_text)) {
				handleActionNeededMessage();
			}

			if (item.getText().equalsIgnoreCase(
					OpcatExposeList.non_interface_change_text)) {
				handleRegulerMessage();
			}
		}

	}
}
