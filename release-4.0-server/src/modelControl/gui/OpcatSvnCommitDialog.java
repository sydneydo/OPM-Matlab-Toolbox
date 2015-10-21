package modelControl.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class OpcatSvnCommitDialog extends javax.swing.JDialog implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton jButton2;
	private JTextPane jTextPane1;

	public JTextPane getTextPane() {
		return jTextPane1;
	}

	private JPanel jPanel1;
	private JButton jButton1;

	private boolean cancled = true;

	private JSeparator jSeparator4;
	private JSeparator jSeparator3;
	private JSeparator jSeparator2;
	private JSeparator jSeparator1;
	private JCheckBox jCheckBox1;
	private JPanel jPanel2;

	/**
	 * Auto-generated main method to display this JDialog
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				OpcatSvnCommitDialog inst = new OpcatSvnCommitDialog(frame);
				inst.setVisible(true);
			}
		});
	}

	public OpcatSvnCommitDialog(JFrame frame) {
		super(frame);
		initGUI();
	}

	private void initGUI() {
		try {
			BorderLayout thisLayout = new BorderLayout();
			getContentPane().setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(401, 322));
			this.setTitle("Commit");
			{
				jTextPane1 = new JTextPane();
				getContentPane().add(jTextPane1, BorderLayout.NORTH);
				jTextPane1.setText("Enter Commit note");
				jTextPane1.selectAll();
				jTextPane1.setPreferredSize(new java.awt.Dimension(391, 149));
				jTextPane1.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));

			}
			{
				jPanel1 = new JPanel();
				FlowLayout jPanel1Layout = new FlowLayout();
				jPanel1Layout.setHgap(50);
				jPanel1Layout.setVgap(15);
				getContentPane().add(jPanel1, BorderLayout.SOUTH);
				jPanel1.setLayout(jPanel1Layout);
				jPanel1.setPreferredSize(new java.awt.Dimension(391, 74));
				jPanel1.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));

				{
					jButton2 = new JButton();
					jPanel1.add(jButton2);
					jButton2.setText("Commit");
					jButton2.setPreferredSize(new java.awt.Dimension(128, 44));
					jButton2.setBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false));
					jButton2.addActionListener(this);
				}
				{
					jButton1 = new JButton();
					jPanel1.add(jButton1);
					jButton1.setText("Cancel");
					jButton1.setPreferredSize(new java.awt.Dimension(128, 44));
					jButton1.setBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false));
					jButton1.addActionListener(this);
				}
			}
			{
				jPanel2 = new JPanel();
				FlowLayout jPanel2Layout = new FlowLayout();
				jPanel2Layout.setVgap(10);
				jPanel2.setLayout(jPanel2Layout);
				getContentPane().add(jPanel2, BorderLayout.CENTER);
				jPanel2.setPreferredSize(new java.awt.Dimension(391, 59));
				jPanel2.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));

				{
					jSeparator3 = new JSeparator();
					jPanel2.add(jSeparator3);

					jCheckBox1 = new JCheckBox();
					jPanel2.add(jCheckBox1);
					jCheckBox1.setText("Unlock after Commit ?");
					jCheckBox1.setBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false));
					jCheckBox1
							.setPreferredSize(new java.awt.Dimension(162, 27));
				}
			}
			this.setSize(401, 322);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton event = (JButton) e.getSource();

		if (event.getText().equalsIgnoreCase("commit")) {
			cancled = false;

		}

		if (event.getText().equalsIgnoreCase("cancel")) {
			cancled = true;
		}

		setVisible(false);

	}

	public String getCommitMessage() {
		return jTextPane1.getText();
	}

	public boolean isUnlockAfterCommit() {
		return jCheckBox1.isSelected();
	}

	public boolean isCancled() {
		return cancled;
	}

}
