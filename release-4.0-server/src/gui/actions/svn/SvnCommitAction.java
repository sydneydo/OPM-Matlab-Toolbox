package gui.actions.svn;

import database.OpcatDatabaseConnection;
import expose.OpcatExposeError;
import expose.OpcatExposeList;
import expose.OpcatExposeManager;
import expose.OpcatExposeConstants.OPCAT_EXPOSE_OP;
import gui.Opcat2;
import gui.actions.expose.OpcatExposeChange;
import gui.controls.FileControl;
import gui.controls.GuiControl;
import gui.images.svn.SvnImages;
import gui.metaLibraries.logic.MetaLibrary;
import gui.opdProject.OpdProject;
import gui.opx.Loader;
import gui.opx.Saver;
import gui.util.OpcatFile;
import gui.util.OpcatLogger;

import java.awt.event.ActionEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import javax.swing.Icon;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import org.tmatesoft.svn.core.wc.SVNStatus;

import util.FileUtils;

import modelControl.OpcatMCManager;
import modelControl.gui.OpcatSvnCommitDialog;

public class SvnCommitAction extends SvnAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OpcatFile root;

	public SvnCommitAction(OpcatFile file, String name, Icon icon) {
		super(name, icon);
		this.root = file;
	}

	public String commit(boolean unLockAfter, String commitMessage) {

		try {

			if ((FileControl.getInstance().getCurrentProject() == null)
					|| FileControl.getInstance().getCurrentProject()
							.isCanClose()) {

				if (!OpcatMCManager.getInstance().forceConnection()) {
					return "Could not connect to MC";
				}

				ArrayList<File> rawFiles = FileUtils.getFileListFlat(root);

				ArrayList<File> approvedForCommit = new ArrayList<File>();

				for (File raw : rawFiles) {
					SVNStatus status = null;
					try {
						status = OpcatMCManager.getInstance().getStatus(raw,
								false);
					} catch (Exception ex) {

					}
					if (status != null) {
						approvedForCommit.add(raw);
					}

				}

				if (approvedForCommit.size() != 0) {

					for (File file : approvedForCommit) {

						ArrayList<OpcatExposeChange> origChanges = new ArrayList<OpcatExposeChange>();

						try {
							OpcatDatabaseConnection.getInstance()
									.getConnection().setAutoCommit(false);

							if (file.isFile()) {

								InputStream is = null;
								Loader ld = new gui.opx.Loader(file.getPath());
								if (file.getName().endsWith(".opz")) {
									is = new GZIPInputStream(
											new FileInputStream(file), 4096);
								} else {
									is = new BufferedInputStream(
											new FileInputStream(file), 4096);
								}

								if (is == null) {
									String message = "File " + file.getName()
											+ " is not an opcat model \n";
									message = message
											+ "Continue committing other files ?";

									int ret = JOptionPane.showOptionDialog(
											Opcat2.getFrame(), message,
											"OPCAT II",
											JOptionPane.YES_NO_OPTION,
											JOptionPane.WARNING_MESSAGE, null,
											null, null);
									if (ret != JOptionPane.YES_OPTION) {
										cleanupFail();
										return message;
									} else {
										continue;
									}
								}

								OpdProject project = null;
								ArrayList<OpcatExposeError> results = new ArrayList<OpcatExposeError>();

								project = ld.load(new JDesktopPane(), is, null);

								if (project == null) {
									String message = "File " + file.getName()
											+ " : Error loading file  \n";
									message = message
											+ "Continue committing other files ?";

									int ret = JOptionPane.showOptionDialog(
											Opcat2.getFrame(), message,
											"OPCAT II",
											JOptionPane.YES_NO_OPTION,
											JOptionPane.WARNING_MESSAGE, null,
											null, null);
									if (ret != JOptionPane.YES_OPTION) {
										cleanupFail();
										return message;
									} else {
										continue;
									}
								}

								origChanges = project.getExposeManager()
										.getLatestExposedChangeMap();

								results = new ArrayList<OpcatExposeError>();
								results = project.getExposeManager()
										.commitExpose();

								if (results.size() == 0) {
									boolean usageOK = false;
									usageOK = project.getExposeManager()
											.commitUsage();

									if (usageOK) {

										project.getExposeManager()
												.clearExposeLocalChanges();

										if (FileControl.getInstance()
												.getCurrentProject() != null) {
											if (FileControl
													.getInstance()
													.getCurrentProject()
													.getGlobalID()
													.equalsIgnoreCase(
															project
																	.getGlobalID())) {
												FileControl
														.getInstance()
														.getCurrentProject()
														.getExposeManager()
														.clearExposeLocalChanges(
																project);
											}
										}

										if (file.exists()) {
											Saver save = new gui.opx.Saver();
											save.save(project, null, null);
										}

										boolean commitMCOK = OpcatMCManager
												.getInstance().doCommit(
														new OpcatFile(project
																.getPath(),
																true),
														commitMessage,
														unLockAfter, false);

										if (commitMCOK) {
											OpcatDatabaseConnection
													.getInstance()
													.getConnection().commit();

										} else {
											rollback(file, project, origChanges);

											String message = "File "
													+ file.getName()
													+ " failed while committing to MC: \n\n";
											message = message
													+ "Continue committing other files ?";

											int ret = JOptionPane
													.showOptionDialog(
															Opcat2.getFrame(),
															message,
															"OPCAT II",
															JOptionPane.YES_NO_OPTION,
															JOptionPane.WARNING_MESSAGE,
															null, null, null);
											if (ret != JOptionPane.YES_OPTION) {
												cleanupFail();
												return message;
											}
										}
									} else {
										rollback(file, project, origChanges);

										String message = "File "
												+ file.getName()
												+ " failed while committing expose usage: \n\n";
										message = message
												+ "Continue committing other files ?";

										int ret = JOptionPane.showOptionDialog(
												Opcat2.getFrame(), message,
												"OPCAT II",
												JOptionPane.YES_NO_OPTION,
												JOptionPane.WARNING_MESSAGE,
												null, null, null);
										if (ret != JOptionPane.YES_OPTION) {
											cleanupFail();
											return message;
										}
									}

								} else {
									rollback(file, project, origChanges);

									String message = "File "
											+ file.getName()
											+ " failed while committing expose :\n\n";
									for (OpcatExposeError entry : results) {

										message = message
												+ entry.getErrorCouserName()
												+ ": "
												+ entry.getErrorMessage()
												+ "\n";

									}
									message = message
											+ "\nContinue committing other files ?";
									int ret = JOptionPane.showOptionDialog(
											Opcat2.getFrame(), message,
											"OPCAT II",
											JOptionPane.YES_NO_OPTION,
											JOptionPane.WARNING_MESSAGE, null,
											null, null);
									if (ret != JOptionPane.YES_OPTION) {
										cleanupFail();
										return message;
									}
								}
							}
						} catch (Exception ex) {
							OpcatDatabaseConnection.getInstance()
									.getConnection().rollback();
							OpcatLogger.logError(ex, false);
							String message = "Failed committing on unknown error.\n";
							message = message
									+ "\nContinue committing other files ?";

							int ret = JOptionPane.showOptionDialog(Opcat2
									.getFrame(), message, "OPCAT II",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.WARNING_MESSAGE, null, null,
									null);
							if (ret != JOptionPane.YES_OPTION) {
								cleanupFail();
								return message;
							}
						}
					}
				} else {
					JOptionPane.showMessageDialog(Opcat2.getFrame(),
							"Nothing to commit");
				}
			} else {
				JOptionPane
						.showMessageDialog(Opcat2.getFrame(),
								"Please save the project before committing, Commit cancelled");
			}

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		} finally {
			cleanupFail();
		}
		return null;

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		super.actionPerformed(e);

		Opcat2.getGlassPane().setVisible(true);
		Opcat2.getGlassPane().start();

		try {
			OpcatSvnCommitDialog commitDialog = new OpcatSvnCommitDialog(Opcat2
					.getFrame());
			commitDialog.setIconImage(SvnImages.ACTION_COMMIT.getImage());
			commitDialog.setLocationRelativeTo(Opcat2.getFrame());
			commitDialog.setModal(true);
			commitDialog.setVisible(true);

			String msg = commitDialog.getCommitMessage();

			if (!(commitDialog.isCancled())) {
				String re = commit(commitDialog.isUnlockAfterCommit(), msg);
				if (re != null) {
					OpcatLogger.logMessage(re);
				}
			}
		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		} finally {
			cleanupFail();
		}
	}

	private void cleanupFail() {
		try {

			try {
				if (FileControl.getInstance().getCurrentProject() != null) {
					OpcatExposeList.getInstance().refresh(
							FileControl.getInstance().getCurrentProject()
									.getExposeManager().getExposedList(null,
											true));
				}
			} catch (Exception ex) {

			}

			try {
				OpcatDatabaseConnection.getInstance().getConnection()
						.rollback();
			} catch (SQLException e) {
				// OpcatLogger.logError(e);
			}
			try {
				OpcatDatabaseConnection.getInstance().getConnection()
						.setAutoCommit(true);
			} catch (SQLException e) {
				// OpcatLogger.logError(e);

			}
			GuiControl.getInstance().getRepository().getModelsView()
					.repaintKeepOpen();
		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		} finally {
			Opcat2.getGlassPane().setVisible(false);
			Opcat2.getGlassPane().stop();
		}

	}

	private void rollback(File file, OpdProject project,
			ArrayList<OpcatExposeChange> origChanges) throws SQLException {

		OpcatDatabaseConnection.getInstance().getConnection().rollback();

		project.getExposeManager().setLatestExposedChangeMap(origChanges);

		if (file.exists()) {
			Saver save = new gui.opx.Saver();
			save.save(project, null, null);
		}

		if (FileControl.getInstance().getCurrentProject() != null) {
			if (FileControl.getInstance().getCurrentProject().getGlobalID()
					.equalsIgnoreCase(project.getGlobalID())) {
				FileControl.getInstance().getCurrentProject()
						.getExposeManager().setLatestExposedChangeMap(
								origChanges);
				// FileControl.getInstance().getCurrentProject()
				// .getExposeManager().clearExposeLocalChanges();
			}
		}

	}
}
