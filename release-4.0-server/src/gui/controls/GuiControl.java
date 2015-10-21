package gui.controls;

import gui.Opcat2;
import gui.dataProject.DataProject;
import gui.metaLibraries.logic.MetaLibrary;
import gui.opdProject.Opd;
import gui.opdProject.StateMachine;
import gui.repository.Repository;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.util.Enumeration;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import net.java.swingfx.waitwithstyle.PerformanceInfiniteProgressPanel;

/**
 * 
 * @author Eran Toch
 */
public class GuiControl {

	private static GuiControl instance = null;
	private Opcat2 myOpcat = null;

	protected GuiControl() {
		// Exists only to defeat instantiation.
	}

	public boolean isShowFileConsole() {
		return myOpcat.getShowFileConsoleState();
	}

	public boolean isShowAdminConsole() {
		return myOpcat.getShowAdminConsoleState();
	}

	public boolean isShowMessagesConsole() {
		return myOpcat.getShowMessagesConsoleState();
	}

	public static GuiControl getInstance() {
		if (instance == null) {
			instance = new GuiControl();
		}
		return instance;
	}

	public void setOpcat(Opcat2 opcat) {
		this.myOpcat = opcat;
	}

	public Opd getClipBoard() {
		if (Opcat2.getCurrentProject() != null) {
			return Opcat2.getCurrentProject().getClipBoard();
		} else {
			return null;
		}
	}

	public JTabbedPane getExtensionToolsPane() {
		return this.myOpcat.getExtensionToolsPane();
	}

	public boolean IsProjectOpen() {
		return this.myOpcat.isProjectOpened();
	}

	public void setCursor4All(final int cursorType) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Opcat2.getFrame().setCursor(
						Cursor.getPredefinedCursor(cursorType));
				Opcat2.getOplPane().setCursor(
						Cursor.getPredefinedCursor(cursorType));

				if (Opcat2.getCurrentProject() != null) {
					for (Enumeration e = Opcat2.getCurrentProject()
							.getComponentsStructure().getOpds(); e
							.hasMoreElements();) {
						Opd tempOpd = (Opd) e.nextElement();
						tempOpd.getDrawingArea().setCursor(
								Cursor.getPredefinedCursor(cursorType));
					}
				}
			}
		});

	}

	public void startWaitingMode(String message, boolean indeterminate) {

		JProgressBar pBar = new JProgressBar();
		if (indeterminate) {
			pBar.setIndeterminate(true);
		}
		this.showWaitMessage(message, pBar);
		StateMachine.setWaiting(true);
		this.setCursor4All(Cursor.WAIT_CURSOR);

	}

	public void stopWaitingMode() {
		this.hideWaitMessage();
		StateMachine.reset();
		StateMachine.setWaiting(false);
		this.setCursor4All(Cursor.DEFAULT_CURSOR);
	}

	public void showWaitMessage(String message, JProgressBar pBar) {
		this.myOpcat.setWaitScreen(new JWindow(Opcat2.getFrame()));
		this.handleWaitScreen(this.myOpcat.getWaitScreen(), message, pBar);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GuiControl.this.myOpcat.getWaitScreen().setVisible(true);
			}
		});

	}

	public void handleWaitScreen(JWindow theScreen, String message,
			JProgressBar pBar) {
		// theScreen = new JWindow(myFrame);
		JLabel msgLabel = new JLabel(message);
		msgLabel.setBorder(new EmptyBorder(25, 30, 25, 30));
		msgLabel.setForeground(new Color(0, 51, 102));
		msgLabel.setFont(new Font("Arial", Font.BOLD, 16));

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(msgLabel, BorderLayout.CENTER);
		panel.add(pBar, BorderLayout.SOUTH);

		panel.setBorder(new LineBorder(new Color(0, 51, 102), 1, true));
		panel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		theScreen.getContentPane().setLayout(new BorderLayout());
		theScreen.getContentPane().add(panel, BorderLayout.CENTER);
		pBar.setForeground(new Color(7, 65, 122));
		pBar.setBorder(new LineBorder(new Color(0, 51, 102)));
		pBar.setFont(new Font("Arial", Font.BOLD, 14));

		theScreen.pack();
		theScreen.setLocation(Opcat2.getFrame().getWidth() / 2
				- theScreen.getSize().width / 2, Opcat2.getFrame().getHeight()
				/ 2 - theScreen.getSize().height / 2);
	}

	public void hideWaitMessage() {
		if (this.myOpcat.getWaitScreen() == null) {
			return;
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GuiControl.this.myOpcat.getWaitScreen().dispose();
			}
		});

	}

	public JWindow getMetaWaitMessage() {
		return GuiControl.this.myOpcat.getMetaWaitScreen();
	}

	public void showMetaWaitMessage(String message, JProgressBar pBar) {
		if (pBar != null) {
			this.myOpcat.setMetaWaitScreen(new JWindow(Opcat2.getFrame()));
			this.handleWaitScreen(this.myOpcat.getMetaWaitScreen(), message,
					pBar);
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GuiControl.this.myOpcat.getMetaWaitScreen().setVisible(true);
			}
		});
	}

	public void hideMetaWaitMessage() {
		if (this.myOpcat.getMetaWaitScreen() == null) {
			return;
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GuiControl.this.myOpcat.getMetaWaitScreen().dispose();
			}
		});

	}

	public JFrame getFrame() {
		return Opcat2.getFrame();
	}

	public JDesktopPane getDesktop() {
		return this.myOpcat.getDesktop();
	}

	public Repository getRepository() {
		return myOpcat.getRepository();
	}

	public void showMetaData(MetaLibrary ont, DataProject metaData) {
		myOpcat.showMetaData(ont, metaData);
	}

	public PerformanceInfiniteProgressPanel getGlassPane() {
		return Opcat2.getGlassPane();
	}

	public static class GlassPaneStarter implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Opcat2.getGlassPane().setVisible(true);
			Opcat2.getGlassPane().start();
		}

	}

	public static class GlassPaneStoper implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Opcat2.getGlassPane().setVisible(false);
			Opcat2.getGlassPane().stop();
		}

	}

	public static void AddPanelToExtensionToolsPanel(JPanel panel) {
		String tabName = panel.getName();
		GuiControl gui = GuiControl.getInstance();
		JTabbedPane tabbedPane = gui.getExtensionToolsPane();

		for (int i = 0; i < tabbedPane.getTabCount(); i++) {

			if (tabbedPane.getTitleAt(i).equals(tabName)) {
				tabbedPane.setSelectedIndex(i);
				return;
			}

		}
		tabbedPane.add(tabName, panel);
		tabbedPane.setSelectedComponent(panel);

	}

	public static void RemovePanelFromExtensionToolsPanel(JPanel panel) {
		String tabName = panel.getName();
		GuiControl gui = GuiControl.getInstance();
		JTabbedPane tabbedPane = gui.getExtensionToolsPane();

		for (int i = 0; i < tabbedPane.getTabCount(); i++) {

			if (tabbedPane.getTitleAt(i).equals(tabName)) {
				tabbedPane.remove(i);
				return;
			}

		}
	}
}
