package gui.actions.file;

import gui.controls.FileControl;
import gui.controls.GuiControl;
import gui.util.OpcatLogger;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.tmatesoft.svn.core.wc.SVNRevision;

import modelControl.OpcatMCDirEntry;
import modelControl.OpcatMCManager;

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
public class NewProjectDialog extends javax.swing.JDialog implements
		ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ButtonGroup buttonGroup1;
	private JPanel jPanel1;
	private JButton jButton1;
	private JRadioButton jRadioButton2;
	private JRadioButton jRadioButton1;
	private JButton jButton2;
	private JLabel jLabel1;
	private JComboBox jComboBox1;
	private JPanel jPanel3;
	private JPanel jPanel2;
	ArrayList<OpcatMCDirEntry> entities = new ArrayList<OpcatMCDirEntry>();

	/**
	 * Auto-generated main method to display this JDialog
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				NewProjectDialog inst = new NewProjectDialog(frame);
				inst.setVisible(true);
			}
		});
	}

	public NewProjectDialog(JFrame frame) {
		super(frame);
		initGUI();
	}

	private void initGUI() {
		try {
			{
			}
			BorderLayout thisLayout = new BorderLayout();
			getContentPane().setLayout(thisLayout);
			this.setTitle("New Model");
			this.setModalityType(ModalityType.APPLICATION_MODAL);
			{
				jPanel1 = new JPanel();
				FlowLayout jPanel1Layout = new FlowLayout();
				jPanel1Layout.setHgap(60);
				jPanel1Layout.setVgap(10);
				jPanel1.setLayout(jPanel1Layout);
				getContentPane().add(jPanel1, BorderLayout.SOUTH);
				jPanel1.setPreferredSize(new java.awt.Dimension(384, 73));
				jPanel1.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				jPanel1.add(getJButton2());
				jPanel1.add(getJButton1());
				getContentPane().add(getJPanel3(), BorderLayout.CENTER);
				getContentPane().add(getJPanel2(), BorderLayout.NORTH);
			}
			getJRadioButton1().setSelected(true);
			getJComboBox1().setEnabled(false);
			this.setSize(394, 280);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			FlowLayout jPanel2Layout = new FlowLayout();
			jPanel2Layout.setHgap(50);
			jPanel2Layout.setVgap(20);
			jPanel2.setLayout(jPanel2Layout);
			jPanel2.setPreferredSize(new java.awt.Dimension(384, 76));
			jPanel2.setBorder(BorderFactory
					.createEtchedBorder(BevelBorder.LOWERED));
			getButtonGroup1().add(getJRadioButton1());
			getButtonGroup1().add(getJRadioButton2());
			jPanel2.add(getJRadioButton1());
			jPanel2.add(getJRadioButton2());

		}
		return jPanel2;
	}

	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			jPanel3 = new JPanel();
			FlowLayout jPanel3Layout = new FlowLayout();
			jPanel3Layout.setHgap(25);
			jPanel3Layout.setAlignment(FlowLayout.LEFT);
			jPanel3Layout.setVgap(30);
			jPanel3.setLayout(jPanel3Layout);
			jPanel3.setPreferredSize(new java.awt.Dimension(384, 127));
			jPanel3.setBorder(BorderFactory
					.createEtchedBorder(BevelBorder.LOWERED));
			jPanel3.add(getJLabel1());
			jPanel3.add(getJComboBox1());
		}
		return jPanel3;
	}

	private JComboBox getJComboBox1() {
		if (jComboBox1 == null) {
			ComboBoxModel jComboBox1Model = new DefaultComboBoxModel();
			jComboBox1 = new JComboBox();
			jComboBox1.setModel(jComboBox1Model);
			jComboBox1.setPreferredSize(new java.awt.Dimension(196, 33));
			File file = new File(OpcatMCManager.TEMPLATES_DIR);
			if (file.exists()) {
				try {
					LinkedList<OpcatMCDirEntry> templates = OpcatMCManager
							.getInstance(true).getFileListFromWorkingCopy(file,
									SVNRevision.WORKING);

					for (OpcatMCDirEntry template : templates) {
						int index = template.getName().lastIndexOf(".");
						jComboBox1.addItem(template.getName().substring(0,
								index));
						entities.add(template);
					}

				} catch (Exception e) {
					getJRadioButton2().setEnabled(false);
					getJComboBox1().setEnabled(false);
				}

			} else {
				getJRadioButton2().setEnabled(false);
				getJComboBox1().setEnabled(false);
			}
		}
		return jComboBox1;
	}

	private JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new JLabel();
			jLabel1.setText("Choose Template :");
			jLabel1.setPreferredSize(new java.awt.Dimension(124, 14));
		}
		return jLabel1;
	}

	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("Cancel");
			jButton1.setPreferredSize(new java.awt.Dimension(128, 44));
			jButton1.addActionListener(this);
		}
		return jButton1;
	}

	private JButton getJButton2() {
		if (jButton2 == null) {
			jButton2 = new JButton();
			jButton2.setText("OK");
			jButton2.setSize(128, 44);
			jButton2.setPreferredSize(new java.awt.Dimension(128, 44));
			jButton2.addActionListener(this);
		}
		return jButton2;
	}

	private ButtonGroup getButtonGroup1() {
		if (buttonGroup1 == null) {
			buttonGroup1 = new ButtonGroup();
		}
		return buttonGroup1;
	}

	private JRadioButton getJRadioButton1() {
		if (jRadioButton1 == null) {
			jRadioButton1 = new JRadioButton();
			jRadioButton1.setText("New");
			jRadioButton1.setPreferredSize(new java.awt.Dimension(51, 30));
			jRadioButton1.addActionListener(this);
		}
		return jRadioButton1;
	}

	private JRadioButton getJRadioButton2() {
		if (jRadioButton2 == null) {
			jRadioButton2 = new JRadioButton();
			jRadioButton2.setText("From Template");
			jRadioButton2.setPreferredSize(new java.awt.Dimension(115, 31));
			jRadioButton2.addActionListener(this);
		}
		return jRadioButton2;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		GuiControl.getInstance().setCursor4All(Cursor.WAIT_CURSOR);
		try {
			if (e.getSource() instanceof JButton) {
				setVisible(false);
				JButton event = (JButton) e.getSource();
				if (event.getText().equalsIgnoreCase("ok")) {
					if (getJRadioButton1().isSelected()) {
						FileControl.getInstance().createNewProject();
					}
					if (getJRadioButton2().isSelected()) {
						FileControl.getInstance().createNewProject(
								this.entities
										.get(jComboBox1.getSelectedIndex()),
								false);

					}
				}

			}

			if (e.getSource() instanceof JRadioButton) {
				JRadioButton radio = (JRadioButton) e.getSource();
				if (radio.getText().equalsIgnoreCase("new")) {
					this.getJComboBox1().setEnabled(false);
				} else {
					this.getJComboBox1().setEnabled(true);
				}
			}
		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
		GuiControl.getInstance().setCursor4All(Cursor.DEFAULT_CURSOR);

	}
}
