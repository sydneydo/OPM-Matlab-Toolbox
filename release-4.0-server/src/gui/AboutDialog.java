package gui;

import gui.images.misc.MiscImages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;

public class AboutDialog extends JDialog {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 5126942890128221557L;

	/**
	 * 
	 */
	 

	private static String htmlText = "<html><br>"
			+ "<font color='navy' face='Tahoma' size=-1><b>Object-Process Methodology:</b><font>"
			+ "<font color='navy' face='Tahoma' size=-1><ul>"
			+ "<li>Dov Dori</li>" + "</ul><font>" +

			"<font color='navy' face='Tahoma' size=-1><b>Design:</b><font>"
			+ "<font color='navy' face='Tahoma' size=-1><ul>"
			+ "<li>Dov Dori</li>" + "<li>Iris Berger</li>"
			+ "<li>Arnon Sturm</li>" + "<li>Eran Toch</li>" + "</ul><font>"
			+ "</html>";

	JPanel jPanel1 = new JPanel();

	JTabbedPane jTabbedPane1 = new JTabbedPane();

	JPanel jPanel2 = new JPanel();

	JPanel jPanel3 = new JPanel();

	JButton closeButton = new JButton();

	JLabel jLabel13 = new JLabel(MiscImages.SPLASH);

	JPanel jPanel4 = new JPanel();

	JLabel jLabel16 = new JLabel(MiscImages.LOGO_BIG_ICON);

	JLabel jLabel17 = new JLabel();

	JLabel jLabel18 = new JLabel();

	JLabel licenseLabel = new JLabel();

	JScrollPane jScrollPane1 = new JScrollPane();

	Border border1;

	JEditorPane infoPane = new JEditorPane();

	public AboutDialog() {
		super(Opcat2.getFrame(), "About OPCAT", true);
		try {
			this.jbInit();
			this.setSize(482, 353);
			int pWidth = Opcat2.getFrame().getWidth();
			int pHeight = Opcat2.getFrame().getHeight();

			this.setLocation(Math.abs(pWidth / 2 - this.getWidth() / 2), Math.abs(pHeight
					/ 2 - this.getHeight() / 2));

			KeyListener kListener = new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						AboutDialog.this.closeButton.doClick();
						return;
					}
				}
			};

			this.addKeyListener(kListener);

			this.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.border1 = BorderFactory.createCompoundBorder(BorderFactory
				.createEtchedBorder(Color.white, new Color(142, 142, 142)),
				BorderFactory.createEmptyBorder(0, 10, 0, 0));
		this.jPanel1.setLayout(null);
		this.jTabbedPane1.setBounds(new Rectangle(3, 2, 457, 265));
		this.closeButton.setText("Close");
		this.closeButton.setBounds(new Rectangle(198, 274, 102, 28));
		this.closeButton
				.addActionListener(new AboutDialog_closeButton_actionAdapter(
						this));
		this.jPanel3.setLayout(null);
		this.jLabel13.setBounds(new Rectangle(-2, 0, 468, 238));
		this.jPanel2.setLayout(null);
		this.setResizable(false);
		this.jPanel4.setLayout(null);
		this.jLabel16.setBounds(new Rectangle(21, 87, 65, 42));
		this.jLabel17.setFont(new java.awt.Font("Dialog", 1, 15));
		this.jLabel17.setForeground(new Color(0, 0, 128));
		this.jLabel17.setToolTipText("");
		this.jLabel17.setText("Version:       VERSION");
		this.jLabel17.setBounds(new Rectangle(20, 39, 450, 32));
		this.jLabel18.setFont(new java.awt.Font("Dialog", 1, 15));
		this.jLabel18.setForeground(new Color(0, 0, 128));
		this.jLabel18.setText("Date:  CompileDate");
		this.jLabel18.setBounds(new Rectangle(20, 70, 450, 32));

		this.licenseLabel.setFont(new java.awt.Font("Dialog", 1, 15));
		this.licenseLabel.setForeground(new Color(0, 0, 128));
		String licenseTitle = "";
		try {
			licenseTitle = Opcat2.getLicense().getLicenseDescription();
		} catch (NullPointerException npe) {
		}
		this.licenseLabel.setText(licenseTitle);
		this.licenseLabel.setBounds(new Rectangle(20, 101, 350, 32));

		String copyright = "OPCAT Systems. All rights reserved (c) 2005-2008";
		JLabel copyrightLabel = new JLabel();
		copyrightLabel.setFont(new java.awt.Font("Dialog", 1, 15));
		copyrightLabel.setForeground(new Color(0, 0, 128));
		copyrightLabel.setText(copyright);
		copyrightLabel.setBounds(new Rectangle(20, 130, 450, 32));

		this.jScrollPane1.setBorder(this.border1);
		this.jScrollPane1.setBounds(new Rectangle(14, 9, 442, 215));
		this.infoPane.setContentType("text/html");
		this.infoPane.setText(htmlText);
		this.infoPane.setCaretPosition(0);
		this.infoPane.setBackground(this.jPanel3.getBackground());
		this.infoPane.setEditable(false);
		this.getContentPane().add(this.jPanel1, BorderLayout.CENTER);
		this.jPanel1.add(this.jTabbedPane1, null);
		this.jTabbedPane1.add(this.jPanel2, "About");
		this.jPanel2.add(this.jLabel13, null);
		// jTabbedPane1.add(jPanel3, "Info");
		this.jPanel3.add(this.jScrollPane1, null);
		this.jScrollPane1.getViewport().add(this.infoPane, null);
		this.jTabbedPane1.add(this.jPanel4, "Version");
		this.jPanel4.add(this.jLabel17, null);
		this.jPanel4.add(this.jLabel18, null);
		// jPanel4.add(jLabel16, null);
		this.jPanel4.add(this.licenseLabel, null);
		this.jPanel4.add(copyrightLabel, null);
		this.jPanel1.add(this.closeButton, null);
	}

	void closeButton_actionPerformed(ActionEvent e) {
		this.dispose();
	}
}

class AboutDialog_closeButton_actionAdapter implements
		java.awt.event.ActionListener {
	AboutDialog adaptee;

	AboutDialog_closeButton_actionAdapter(AboutDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		this.adaptee.closeButton_actionPerformed(e);
	}
}