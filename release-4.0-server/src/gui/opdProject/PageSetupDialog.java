package gui.opdProject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.print.Paper;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class PageSetupDialog extends JDialog {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	private JCheckBox projectName = new JCheckBox();

	private JCheckBox printTime = new JCheckBox();

	private JCheckBox authorName = new JCheckBox();

	private JCheckBox pageNum = new JCheckBox();

	private JCheckBox center = new JCheckBox();

	private JCheckBox fitToPage1 = new JCheckBox();

	protected JSlider marginsSlider = new JSlider();

	protected JLabel inchText = new JLabel();

	private JPanel topPanel = new JPanel();

	private JButton okButton = new JButton();

	private JButton cancelButton = new JButton();

	private JPanel jPanel1 = new JPanel();

	private TitledBorder titledBorder1;

	private JPanel jPanel2 = new JPanel();

	private JPanel jPanel3 = new JPanel();

	private PageSetupData printProp;

	private JCheckBox opdName = new JCheckBox();

	private JRadioButton centerView = new JRadioButton();

	private JRadioButton fitToPage = new JRadioButton();

	private ButtonGroup fitOrCenter = new ButtonGroup();

	private JRadioButton noAlignment = new JRadioButton();

	TitledBorder titledBorder2;

	TitledBorder titledBorder3;

	Border border2;

	TitledBorder titledBorder4;

	Border border3;

	TitledBorder titledBorder5;

	public PageSetupDialog(JFrame owner, String title, PageSetupData pp) {
		super(owner, title, true);
		this.printProp = pp;
		try {
			this.jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(142, 142, 142)), "Margin Width");
		this.titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(142, 142, 142)), "Margin Width");
		this.titledBorder3 = new TitledBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(142, 142, 142)), "Margin Width");
		this.border2 = BorderFactory.createEtchedBorder(Color.white, new Color(142,
				142, 142));
		this.titledBorder4 = new TitledBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(142, 142, 142)), "System Info");
		this.border3 = BorderFactory.createEtchedBorder(Color.white, new Color(142,
				142, 142));
		this.titledBorder5 = new TitledBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(142, 142, 142)), "Printing Alignment");
		this.projectName.setToolTipText("Print Project Name");
		this.projectName.setSelected(this.printProp.isPrjName());
		this.projectName.setText("System Name");
		this.projectName.setBounds(new Rectangle(17, 28, 117, 25));
		this.getContentPane().setLayout(null);
		this.printTime.setBounds(new Rectangle(17, 109, 113, 25));
		this.printTime.setSelected(this.printProp.isPrintTime());
		this.printTime.setText("Printing Time");
		this.authorName.setBounds(new Rectangle(17, 82, 117, 25));
		this.authorName.setText("Author Name");
		this.authorName.setSelected(this.printProp.isPrjCreator());
		this.pageNum.setBounds(new Rectangle(17, 136, 115, 25));
		this.pageNum.setText("Page Number");
		this.pageNum.setSelected(this.printProp.isPageNumber());
		this.center.setSelected(this.printProp.isCenterView());
		this.center.setText("Center View");
		this.center.setBounds(new Rectangle(19, 103, 87, 25));
		this.center.setVisible(false);
		this.fitToPage1.setSelected(this.printProp.isFitToPage());
		this.fitToPage1.setText("Fit To Page");
		this.fitToPage1.setBounds(new Rectangle(19, 133, 87, 25));
		this.fitToPage1.setVisible(false);
		// fitToPage.setEnabled(false);
		this.marginsSlider.setMajorTickSpacing(8);
		this.marginsSlider.setMaximum(72);
		this.marginsSlider.setMinimum(16);
		this.marginsSlider.setMinorTickSpacing(4);
		this.marginsSlider.setPaintLabels(true);
		this.marginsSlider.setPaintTicks(true);
		this.marginsSlider.createStandardLabels(8);
		this.marginsSlider.setForeground(Color.black);
		this.marginsSlider.setToolTipText("Adjust Margins Size");
		this.marginsSlider.setVerifyInputWhenFocusTarget(false);
		this.marginsSlider.setBounds(new Rectangle(6, 16, 242, 45));
		// marginsSlider.addMouseMotionListener(new
		// PrintDialog_marginsSlider_mouseMotionAdapter(this));
		this.marginsSlider.addChangeListener(new SliderChanged());
		this.marginsSlider.setLayout(null);
		this.marginsSlider.setValue(this.printProp.getMarginWidth());
		this.inchText.setForeground(Color.black);
		this.inchText.setText(this.marginsSlider.getValue() + "/72 inch");
		this.inchText.setBounds(new Rectangle(255, 28, 71, 20));
		this.topPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		this.topPanel.setMinimumSize(new Dimension(1, 1));
		this.topPanel.setPreferredSize(new Dimension(1, 1));
		this.topPanel.setVerifyInputWhenFocusTarget(false);
		this.topPanel.setBounds(new Rectangle(9, 14, 380, 276));
		this.topPanel.setLayout(null);
		this.okButton.setText("OK");
		this.okButton.setBounds(new Rectangle(209, 303, 84, 26));
		this.okButton.addActionListener(new PageSetupDialog_okButton_actionAdapter(
				this));
		this.cancelButton.setBounds(new Rectangle(301, 303, 84, 26));
		this.cancelButton
				.addActionListener(new PageSetupDialog_cancelButton_actionAdapter(
						this));
		this.cancelButton.setText("Cancel");
		this.jPanel1.setBorder(this.titledBorder4);
		this.jPanel1.setBounds(new Rectangle(20, 20, 159, 167));
		this.jPanel1.setLayout(null);
		this.jPanel2.setBorder(this.titledBorder5);
		this.jPanel2.setBounds(new Rectangle(203, 20, 159, 165));
		this.jPanel2.setLayout(null);
		this.jPanel3.setBorder(this.titledBorder1);
		this.jPanel3.setBounds(new Rectangle(20, 195, 343, 66));
		this.jPanel3.setLayout(null);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setModal(true);
		this.setTitle("");
		this.setResizable(false);
		// this.setTitle("Printing Properties");
		this.opdName.setText("OPD name");
		this.opdName.setBounds(new Rectangle(17, 55, 112, 24));
		this.opdName.setSelected(this.printProp.isOpdName());
		this.opdName.addActionListener(new PageSetupDialog_opdName_actionAdapter(
				this));
		this.centerView.setSelected(this.printProp.isCenterView());
		this.centerView.setText("Center View");
		this.centerView.setBounds(new Rectangle(18, 63, 107, 24));
		this.fitToPage.setText("Fit to Page");
		this.fitToPage.setBounds(new Rectangle(18, 96, 100, 24));
		this.fitToPage.setSelected(this.printProp.isFitToPage());
		this.noAlignment.setText("Do not Align");
		this.noAlignment.setBounds(new Rectangle(18, 31, 105, 24));
		this.noAlignment.setSelected(this.printProp.isDoNotAlign());
		this.fitOrCenter.add(this.fitToPage);
		this.fitOrCenter.add(this.centerView);
		this.fitOrCenter.add(this.noAlignment);
		this.getContentPane().add(this.topPanel, null);
		this.topPanel.add(this.jPanel1, null);
		this.jPanel1.add(this.authorName, null);
		this.jPanel1.add(this.projectName, null);
		this.jPanel1.add(this.opdName, null);
		this.jPanel1.add(this.printTime, null);
		this.jPanel1.add(this.pageNum, null);
		this.topPanel.add(this.jPanel2, null);
		this.jPanel2.add(this.center, null);
		this.jPanel2.add(this.fitToPage1, null);
		this.jPanel2.add(this.centerView, null);
		this.jPanel2.add(this.fitToPage, null);
		this.jPanel2.add(this.noAlignment, null);
		this.topPanel.add(this.jPanel3, null);
		this.jPanel3.add(this.marginsSlider, null);
		this.jPanel3.add(this.inchText, null);
		this.getContentPane().add(this.cancelButton, null);
		this.getContentPane().add(this.okButton, null);

		// this.pack();
		this.setBounds(0, 0, 402, 370);
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}

	void marginsSlider_mouseDragged(MouseEvent e) {
		this.inchText.setText(this.marginsSlider.getValue() + "/72 inch");
	}

	private void _updatePageFormat() {
		int marginWidth = this.marginsSlider.getValue();
		double h = this.printProp.getWidth();
		double w = this.printProp.getHeight();
		Paper p = this.printProp.getPaper();
		p.setImageableArea(marginWidth, marginWidth, w - marginWidth * 2, h
				- marginWidth * 2);
		this.printProp.setPaper(p);
		this.printProp.setMarginWidth(marginWidth);
		// printProp.setCenterView(center.isSelected());
		this.printProp.setCenterView(this.centerView.isSelected());
		// printProp.setFitToPage(fitToPage1.isSelected());
		this.printProp.setFitToPage(this.fitToPage.isSelected());
		this.printProp.setDoNotAlign(this.noAlignment.isSelected());
		this.printProp.setPageNumber(this.pageNum.isSelected());
		this.printProp.setPrintTime(this.printTime.isSelected());
		this.printProp.setPrjCreator(this.authorName.isSelected());
		this.printProp.setPrjName(this.projectName.isSelected());
		this.printProp.setOpdName(this.opdName.isSelected());
		this.printProp.setApplyed(true);
		this.dispose();
	}

	Action sliderDraggedAction = new AbstractAction() {
		 

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		 

		public void actionPerformed(ActionEvent e) {
			(PageSetupDialog.this).inchText.setText(PageSetupDialog.this.marginsSlider.getValue()
					+ "/72 inch");
		}
	};

	class SliderChanged implements ChangeListener {
		public SliderChanged() {
			// Empty stub
		}

		public void stateChanged(ChangeEvent e) {
			(PageSetupDialog.this).inchText.setText(PageSetupDialog.this.marginsSlider.getValue()
					+ "/72 inch");
		}

	}

	void opdName_actionPerformed(ActionEvent e) {
		// empty stub
	}

	void okButton_actionPerformed(ActionEvent e) {
		this._updatePageFormat();
		this.dispose();
	}

	void cancelButton_actionPerformed(ActionEvent e) {
		this.printProp.setApplyed(false);
		this.dispose();
	}

}

class PrintDialog_marginsSlider_mouseMotionAdapter extends
		java.awt.event.MouseMotionAdapter {
	PageSetupDialog adaptee;

	PrintDialog_marginsSlider_mouseMotionAdapter(PageSetupDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void mouseDragged(MouseEvent e) {
		this.adaptee.marginsSlider_mouseDragged(e);
	}
}

class PageSetupDialog_opdName_actionAdapter implements
		java.awt.event.ActionListener {
	PageSetupDialog adaptee;

	PageSetupDialog_opdName_actionAdapter(PageSetupDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		this.adaptee.opdName_actionPerformed(e);
	}
}

class PageSetupDialog_okButton_actionAdapter implements
		java.awt.event.ActionListener {
	PageSetupDialog adaptee;

	PageSetupDialog_okButton_actionAdapter(PageSetupDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		this.adaptee.okButton_actionPerformed(e);
	}
}

class PageSetupDialog_cancelButton_actionAdapter implements
		java.awt.event.ActionListener {
	PageSetupDialog adaptee;

	PageSetupDialog_cancelButton_actionAdapter(PageSetupDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		this.adaptee.cancelButton_actionPerformed(e);
	}
}