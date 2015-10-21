package extensionTools.Testing;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import exportedAPI.opcatAPIx.IXSystem;
import gui.Opcat2;
import gui.util.HtmlPanel;
import gui.util.OpcatException;

class TestingGUIPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -12749095803587578L;

	IXSystem mySys;

	TestingSystem aSys = null;

	private final static String fileSeparator = System
			.getProperty("file.separator");

	// JSplitPane jSplitPane1 = new JSplitPane();
	// BorderLayout borderLayout1 = new BorderLayout();
	JPanel jPanelButtons = new JPanel();

	// JPanel jPanel2 = new JPanel();
	JButton runButton = new JButton();

	JButton stopButton = new JButton();

	JButton pauseButton = new JButton();

	JButton continueButton = new JButton();

	JButton forwardButton = new JButton();

	JButton backwardButton = new JButton();

	JButton activateButton = new JButton();

	JButton deactivateButton = new JButton();

	JButton settingsButton = new JButton();

	JButton help = new JButton();

	JPanel jPanelRun = new JPanel();

	JPanel jPanelPause = new JPanel();

	JPanel jPanelManual = new JPanel();

	JPanel jPanelStep = new JPanel();

	GridLayout gridLayout1 = new GridLayout();

	Border border1;

	TitledBorder titledBorder1;

	TitledBorder titledBorder2;

	TitledBorder titledBorder3;

	TitledBorder titledBorder4;

	JTextField jTextFieldForwardSteps = new JTextField();

	JTextField jTextFieldBackward = new JTextField();

	JSplitPane jSplitPane1 = new JSplitPane();

	BoxLayout boxLayout1 = new BoxLayout(this.jPanelRun, BoxLayout.Y_AXIS);

	BoxLayout boxLayout2 = new BoxLayout(this.jPanelPause, BoxLayout.Y_AXIS);

	BoxLayout boxLayout3 = new BoxLayout(this.jPanelManual, BoxLayout.Y_AXIS);

	BoxLayout boxLayout4 = new BoxLayout(this.jPanelStep, BoxLayout.Y_AXIS);

	JPanel jPanel1 = new JPanel();

	TestingStatusBar jLabelTestingStatus = new TestingStatusBar();

	GridLayout gridLayout3 = new GridLayout();

	GridLayout gridLayout2 = new GridLayout();

	JPanel jPanel2 = new JPanel();

	// ImageIcon AgentIcon = new ImageIcon(getResource("agent.gif"));
	// this.setIconImage(AgentIcon.getImage());

	public TestingGUIPanel(IXSystem sys) {
		this.mySys = sys;
		this.aSys = new TestingSystem(this.mySys, this.jLabelTestingStatus);
		try {
			this.jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		TestingSettings.getInstance().loadDefaultSettings();
	}

	private void jbInit() throws Exception {
		this.titledBorder1 = new TitledBorder("");
		this.titledBorder2 = new TitledBorder("");
		this.titledBorder3 = new TitledBorder("");
		this.titledBorder4 = new TitledBorder("");

		// run button
		this.runButton.setMaximumSize(new Dimension(50, 27));
		this.runButton.setMinimumSize(new Dimension(30, 27));
		this.runButton.setPreferredSize(new Dimension(30, 27));
		this.runButton.setToolTipText("Run");
		this.runButton.setMargin(new Insets(2, 2, 2, 2));
		this.runButton.setText("Run");
		this.runButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TestingGUIPanel.this.runButton_actionPerformed(e);
			}
		});

		// stop button
		this.stopButton.setEnabled(false);
		this.stopButton.setMaximumSize(new Dimension(91, 27));
		this.stopButton.setMinimumSize(new Dimension(30, 27));
		this.stopButton.setPreferredSize(new Dimension(30, 27));
		this.stopButton.setToolTipText("Stop");
		this.stopButton.setMargin(new Insets(2, 2, 2, 2));
		this.stopButton.setText("Stop");
		this.stopButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TestingGUIPanel.this.stopButton_actionPerformed(e);
			}
		});

		// pause button
		this.pauseButton.setEnabled(false);
		this.pauseButton.setMaximumSize(new Dimension(91, 27));
		this.pauseButton.setMinimumSize(new Dimension(91, 27));
		this.pauseButton.setPreferredSize(new Dimension(30, 27));
		this.pauseButton.setToolTipText("Pause");
		this.pauseButton.setMargin(new Insets(2, 2, 2, 2));
		this.pauseButton.setText("Pause");
		this.pauseButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TestingGUIPanel.this.pauseButton_actionPerformed(e);
			}
		});

		// continue button
		this.continueButton.setEnabled(false);
		this.continueButton.setMaximumSize(new Dimension(91, 27));
		this.continueButton.setMinimumSize(new Dimension(91, 27));
		this.continueButton.setPreferredSize(new Dimension(30, 27));
		this.continueButton.setToolTipText("Continue");
		this.continueButton.setMargin(new Insets(2, 2, 2, 2));
		this.continueButton.setText("Continue");
		this.continueButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TestingGUIPanel.this.continueButton_actionPerformed(e);
			}
		});

		// forward button
		this.forwardButton.setEnabled(false);
		this.forwardButton.setMinimumSize(new Dimension(30, 27));
		this.forwardButton.setPreferredSize(new Dimension(30, 27));
		this.forwardButton.setToolTipText("Forward");
		this.forwardButton.setMargin(new Insets(0, 0, 0, 0));
		this.forwardButton.setMnemonic('0');
		this.forwardButton.setText("Forward");
		this.forwardButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TestingGUIPanel.this.forwardButton_actionPerformed(e);
			}
		});

		// backward button
		this.backwardButton.setEnabled(false);
		this.backwardButton.setMinimumSize(new Dimension(65, 27));
		this.backwardButton.setPreferredSize(new Dimension(30, 27));
		this.backwardButton.setToolTipText("Backward");
		this.backwardButton.setMargin(new Insets(2, 2, 2, 2));
		this.backwardButton.setMnemonic('0');
		this.backwardButton.setText("Backward");
		this.backwardButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TestingGUIPanel.this.backwardButton_actionPerformed(e);
			}
		});

		// activate button
		this.activateButton.setEnabled(false);
		this.activateButton.setMaximumSize(new Dimension(91, 27));
		this.activateButton.setMinimumSize(new Dimension(91, 27));
		this.activateButton.setPreferredSize(new Dimension(30, 27));
		this.activateButton.setToolTipText("Activate");
		this.activateButton.setMargin(new Insets(2, 2, 2, 2));
		this.activateButton.setText("Activate");
		this.activateButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TestingGUIPanel.this.activateButton_actionPerformed(e);
			}
		});

		// deactivate button
		this.deactivateButton.setEnabled(false);
		this.deactivateButton.setPreferredSize(new Dimension(30, 27));
		this.deactivateButton.setToolTipText("Deactivate");
		this.deactivateButton.setMargin(new Insets(2, 2, 2, 2));
		this.deactivateButton.setText("Deactivate");
		this.deactivateButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TestingGUIPanel.this.deactivateButton_actionPerformed(e);
			}
		});

		// settings button
		this.settingsButton.setBorder(null);
		this.settingsButton.setMaximumSize(new Dimension(30, 27));
		this.settingsButton.setMinimumSize(new Dimension(91, 27));
		this.settingsButton.setPreferredSize(new Dimension(60, 27));
		this.settingsButton.setToolTipText("Settings");
		this.settingsButton.setMargin(new Insets(2, 2, 2, 2));
		this.settingsButton.setText("Settings");
		this.settingsButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TestingGUIPanel.this.settingsButton_actionPerformed(e);
			}
		});

		this.help.setBorder(null);
		this.help.setMaximumSize(new Dimension(30, 27));
		this.help.setMinimumSize(new Dimension(91, 27));
		this.help.setPreferredSize(new Dimension(60, 27));
		this.help.setToolTipText("Help");
		this.help.setMargin(new Insets(2, 2, 2, 2));
		this.help.setText("Help");
		this.help.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TestingGUIPanel.this.help_actionPerformed(e);
			}
		});

		this.jPanelRun.setBorder(this.titledBorder1);
		this.jPanelRun.setPreferredSize(new Dimension(100, 30));
		this.jPanelPause.setBorder(this.titledBorder2);
		this.jPanelPause.setPreferredSize(new Dimension(100, 30));
		this.jPanelManual.setBorder(this.titledBorder3);
		this.jPanelManual.setPreferredSize(new Dimension(152, 30));
		this.jPanelStep.setBorder(this.titledBorder4);
		this.jPanelStep.setPreferredSize(new Dimension(340, 30));
		this.jTextFieldForwardSteps.setBorder(BorderFactory
				.createLoweredBevelBorder());
		this.jTextFieldForwardSteps.setPreferredSize(new Dimension(18, 21));
		this.jTextFieldForwardSteps.setToolTipText("");
		this.jTextFieldForwardSteps.setText("1");
		this.jTextFieldForwardSteps
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						TestingGUIPanel.this.jTextFieldForwardSteps_actionPerformed(e);
					}
				});
		this.jTextFieldBackward.setBorder(BorderFactory.createLoweredBevelBorder());
		this.jTextFieldBackward.setPreferredSize(new Dimension(18, 21));
		this.jTextFieldBackward.setToolTipText("");
		this.jTextFieldBackward.setText("1");
		this.jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.jPanelButtons.setLayout(this.gridLayout1);
		this.setLayout(this.gridLayout2);
		this.jLabelTestingStatus.setPreferredSize(new Dimension(41, 17));
		this.jPanel1.setLayout(this.gridLayout3);

		// run
		this.jPanelButtons.setPreferredSize(new Dimension(1700, 30));
		this.jPanel2.setPreferredSize(new Dimension(304, 30));
		this.jPanelRun.add(this.runButton, null);
		this.jPanelRun.add(this.stopButton, null);
		this.jPanelButtons.add(this.jPanelRun, null);
		this.jPanelButtons.add(this.jPanelPause, null);

		// pause
		this.jPanelPause.add(this.pauseButton, null);
		this.jPanelPause.add(this.continueButton, null);
		this.jPanelButtons.add(this.jPanelStep, null);

		// step
		this.jPanelStep.add(this.forwardButton, null);
		this.jPanelStep.add(this.jTextFieldForwardSteps, null);
		this.jPanelStep.add(this.backwardButton, null);
		this.jPanelStep.add(this.jTextFieldBackward, null);
		this.jPanelButtons.add(this.jPanelManual, null);

		// manual
		this.jPanelManual.add(this.activateButton, null);
		this.jPanelManual.add(this.deactivateButton, null);
		this.jPanelButtons.add(this.jPanel2, null);
		this.jPanel2.add(this.settingsButton, null);
		this.jPanelButtons.add(this.help, null);

		this.jSplitPane1.add(this.jPanel1, JSplitPane.BOTTOM);
		this.jPanel1.add(this.jLabelTestingStatus, null);
		this.jSplitPane1.add(this.jPanelButtons, JSplitPane.TOP);
		this.add(this.jSplitPane1, null);

	}

	void stopButton_actionPerformed(ActionEvent e) {
		this.aSys.testingStop();
		// disable all options except run and settings
		this.forwardButton.setEnabled(false);
		this.backwardButton.setEnabled(false);
		this.activateButton.setEnabled(false);
		this.deactivateButton.setEnabled(false);
		this.stopButton.setEnabled(false);
		this.pauseButton.setEnabled(false);
		this.continueButton.setEnabled(false);
		this.settingsButton.setEnabled(true);
		this.help.setEnabled(true);
		this.runButton.setEnabled(true);
	}

	void runButton_actionPerformed(ActionEvent e) {
		this.aSys.testingStart();
		// enable all options except run and settings
		// enable forward and backward only in step by step mode
		if (TestingSettings.getInstance().isSTEP_BY_STEP_MODE()) {
			this.forwardButton.setEnabled(true);
			this.backwardButton.setEnabled(true);
		} 
		this.activateButton.setEnabled(true);
		this.deactivateButton.setEnabled(true);
		this.stopButton.setEnabled(true);
		this.pauseButton.setEnabled(true);
		this.continueButton.setEnabled(true);
		this.settingsButton.setEnabled(false);
		this.runButton.setEnabled(false);

	}

	void pauseButton_actionPerformed(ActionEvent e) {
		this.aSys.testingPause();
		this.forwardButton.setEnabled(true);
		this.backwardButton.setEnabled(true);
	}

	void continueButton_actionPerformed(ActionEvent e) {
		this.aSys.testingContinue();
	}

	void forwardButton_actionPerformed(ActionEvent e) {
		long numberOfSteps = 1;
		try {
			numberOfSteps = java.lang.Long.valueOf(
					this.jTextFieldForwardSteps.getText()).longValue();
		} catch (NumberFormatException exc) {
			JOptionPane.showMessageDialog(this,
					"Illegal number of steps for forward", "Forward Failed",
					JOptionPane.ERROR_MESSAGE);
			this.jTextFieldForwardSteps.requestFocus();
			return;
		}
		this.aSys.testingForward(numberOfSteps);
	}

	void backwardButton_actionPerformed(ActionEvent e) {
		long numberOfSteps = 1;
		try {
			numberOfSteps = java.lang.Long
					.valueOf(this.jTextFieldBackward.getText()).longValue();
		} catch (NumberFormatException exc) {
			JOptionPane.showMessageDialog(this,
					"Illegal number of steps for backward", "Backward Failed",
					JOptionPane.ERROR_MESSAGE);
			this.jTextFieldBackward.requestFocus();
			return;
		}
		this.aSys.testingBackward(numberOfSteps);
	}

	void activateButton_actionPerformed(ActionEvent e) {
		this.aSys.testingActivate();
	}

	void deactivateButton_actionPerformed(ActionEvent e) {
		this.aSys.testingDeactivate();
	}

	void help_actionPerformed(ActionEvent e) {

		Thread runner = new Thread() {
			public void run() {
				JFrame helpWindow;

				helpWindow = new JFrame("Opcat II Testing Help");
				try {
					// helpWindow.setIconImage(logoIcon.getImage());
					helpWindow.getContentPane().add(
							new HtmlPanel("file:"
									+ System.getProperty("user.dir")
									+ fileSeparator + "help" + fileSeparator
									+ "help" + fileSeparator + "index.html"));
				} catch (OpcatException e) {
					JOptionPane.showMessageDialog(Opcat2.getFrame(),
							"Help files were not found", "Help error",
							JOptionPane.ERROR_MESSAGE);
				}

				Dimension screenSize = Toolkit.getDefaultToolkit()
						.getScreenSize();

				helpWindow.setBounds(0, 0,
						(int) (screenSize.getWidth() * 7 / 8),
						(int) (screenSize.getHeight() * 7 / 8));
				helpWindow
						.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				helpWindow.setVisible(true);

			}
		};
		runner.start();

	}

	void settingsButton_actionPerformed(ActionEvent e) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		TestingSettingsWindow settingsWindow = new TestingSettingsWindow();
		boolean packFrame = false;
		// Validate frames that have preset sizes
		// Pack frames that have useful preferred size info, e.g. from their
		// layout
		if (packFrame) {
			settingsWindow.pack();
		} else {
			settingsWindow.validate();
		}
		// Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = settingsWindow.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		settingsWindow.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		settingsWindow.setVisible(true);
	}

	void jTextFieldForwardSteps_actionPerformed(ActionEvent e) {

	}

}