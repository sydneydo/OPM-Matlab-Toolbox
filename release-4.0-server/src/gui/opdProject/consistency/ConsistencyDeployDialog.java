package gui.opdProject.consistency;

import gui.images.standard.StandardImages;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConsistencyDeployDialog extends JDialog {

	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	private JButton jCancelButton = null;

	private JButton jOKButton = null;

	private JButton jNextButton = null;

	private boolean cancelPressed = false;

	private boolean nextPresed = false;

	private boolean okPressed = false;

	private boolean autoPressed = false;

	private boolean windowClosing = false;

	private JPanel jPanel1 = null;

	private JButton jAllButton = null;

	private JLabel jLabel = null;

	private JPanel jPanel2 = null;

	/**
	 * This method initializes
	 * 
	 */
	String title = "Consistency Deployment"; 
	public ConsistencyDeployDialog(String title) {
		super();
		this.title = title ; 
		this.initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setSize(new java.awt.Dimension(321, 116));
		this.setContentPane(this.getJPanel2());
		this.setTitle(title);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				ConsistencyDeployDialog.this.setWindowClosing(true); 
			}
		});

	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancelJButton() {
		if (this.jCancelButton == null) {
			this.jCancelButton = new JButton();
			this.jCancelButton.setText("Skip Rule");
			this.jCancelButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							ConsistencyDeployDialog.this.setCancelPressed(true); 
							ConsistencyDeployDialog.this.setVisible(false);
						}
					});
		}
		return this.jCancelButton;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getOKJButton() {
		if (this.jOKButton == null) {
			this.jOKButton = new JButton();
			this.jOKButton.setText("OK");
			this.jOKButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ConsistencyDeployDialog.this.setOkPressed(true); 
					ConsistencyDeployDialog.this.setVisible(false);
				}
			});
		}
		return this.jOKButton;
	}

	/**
	 * This method initializes jButton2
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getNextButton() {
		if (this.jNextButton == null) {
			this.jNextButton = new JButton();
			this.jNextButton.setText("Skip");
			this.jNextButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ConsistencyDeployDialog.this.setNextPresed(true); 
					ConsistencyDeployDialog.this.setVisible(false);
				}
			});
		}
		return this.jNextButton;
	}

	public boolean isCancelPressed() {
		return this.cancelPressed;
	}

	public void setCancelPressed(boolean cancelPressed) {
		this.cancelPressed = cancelPressed;
	}

	public boolean isNextPresed() {
		return this.nextPresed;
	}

	public void setNextPresed(boolean nextPresed) {
		this.nextPresed = nextPresed;
	}

	public boolean isOkPressed() {
		return this.okPressed;
	}

	public void setOkPressed(boolean okPressed) {
		this.okPressed = okPressed;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (this.jPanel1 == null) {
			this.jPanel1 = new JPanel();
			this.jPanel1.add(this.getOKJButton(), null);
			this.jPanel1.add(this.getNextButton(), null);
			this.jPanel1.add(this.getJAllButton(), null);
			this.jPanel1.add(this.getCancelJButton(), null);
		}
		return this.jPanel1;
	}

	/**
	 * This method initializes jAllButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJAllButton() {
		if (this.jAllButton == null) {
			this.jAllButton = new JButton();
			this.jAllButton.setText("Auto");
			this.jAllButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ConsistencyDeployDialog.this.setAutoPressed(true); 
					ConsistencyDeployDialog.this.setVisible(false);
				}
			});
		}
		return this.jAllButton;
	}

	public void setTextIcon(Icon icon) {
		this.jLabel.setIcon(icon);
	}

	public void setText(String text) {
		this.jLabel.setText(text);
	}

	public boolean isAutoPressed() {
		return this.autoPressed;
	}

	public void setAutoPressed(boolean autoPressed) {
		this.autoPressed = autoPressed;
	}

	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getJLabel() {
		if (this.jLabel == null) {
			this.jLabel = new JLabel();
			this.jLabel.setText("Place here checker label");
			this.jLabel.setIcon(StandardImages.DELETE);
		}
		return this.jLabel;
	}

	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (this.jPanel2 == null) {
			this.jPanel2 = new JPanel();
			this.jPanel2.add(this.getJLabel(), null);
			this.jPanel2.add(this.getJPanel1(), null);
		}
		return this.jPanel2;
	}

	public boolean isWindowClosing() {
		return this.windowClosing;
	}

	public void setWindowClosing(boolean windowClosing) {
		this.windowClosing = windowClosing;
	}

} // @jve:decl-index=0:visual-constraint="312,59"
