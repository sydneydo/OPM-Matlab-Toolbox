package modelControl.gui;

import gui.Opcat2;
import gui.images.misc.MiscImages;
import gui.opdGraphics.SplashLabel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

public class OpcatSvnProgressReporter extends JWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel splashLabel = null;
	JProgressBar jpb = null;

	public OpcatSvnProgressReporter() {

		Calendar rightNow = new GregorianCalendar();
		int year = rightNow.get(Calendar.YEAR);
		if (year < 2004) {
			year = 2004;
		}
		String copyrightString = " OPCAT Systems, All rights reserved (c) 2005-2007 \n";
		this.splashLabel = new SplashLabel(copyrightString, MiscImages.SPLASH
				.getImage());
		this.splashLabel.setPreferredSize(new Dimension(370, 253));
		this.splashLabel.setForeground(new Color(44, 71, 148));
		this.splashLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.splashLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		this.getContentPane().setLayout(new BorderLayout());
		JPanel bottomPanel = new JPanel(new BorderLayout());
		this.getContentPane().add(this.splashLabel, BorderLayout.CENTER);
		this.jpb = new JProgressBar();
		this.jpb.setForeground(new Color(37, 119, 197));
		this.jpb.setString("Loading... Please wait");
		this.jpb.setStringPainted(true);
		this.jpb.setMinimum(0);
		this.jpb.setMaximum(18);
		this.jpb.setValue(0);
		this.jpb.setIndeterminate(true);
		bottomPanel.add(this.jpb, BorderLayout.CENTER);
		JLabel licenseLabel = new JLabel(Opcat2.getLicense()
				.getLicenseCaption());
		licenseLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bottomPanel.add(licenseLabel, BorderLayout.SOUTH);
		this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		this.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screenSize.width / 2 - this.getSize().width / 2,
				screenSize.height / 2 - this.getSize().height / 2);
	}

	public void setMessage(String msg) {
		this.jpb.setString(msg);
	}
}
