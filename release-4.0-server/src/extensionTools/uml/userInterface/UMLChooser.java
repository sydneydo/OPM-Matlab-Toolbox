package extensionTools.uml.userInterface;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.ISystem;
import extensionTools.uml.umlDiagrams.Activity_d;
import extensionTools.uml.umlDiagrams.DiagramsCreator;
import extensionTools.uml.umlDiagrams.Statechart_d;
import extensionTools.uml.umlDiagrams.sequence_d;
import gui.util.OpcatException;
import gui.util.OpcatLogger;

/**
 * Creates the screen for the UML diagrams generating.
 */
public class UMLChooser extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	ISystem mySys;

	JPanel UMLMain;

	Border border1;

	TitledBorder titledBorder1;

	Border border2;

	JList USeqList;

	JList UStateList;

	TitledBorder titledBorder2;

	Border border3;

	TitledBorder titledBorder3;

	ButtonGroup UUBGroup = new ButtonGroup();

	Border border4;

	TitledBorder titledBorder4;

	Border border5;

	TitledBorder titledBorder5;

	ButtonGroup UClassBGroup = new ButtonGroup();

	Border border6;

	TitledBorder titledBorder6;

	ButtonGroup UDepBGroup = new ButtonGroup();

	Border border7;

	TitledBorder titledBorder7;

	ButtonGroup UMLActGroup = new ButtonGroup();

	JPanel UmlP = new JPanel();

	JPanel UseCaseP = new JPanel();

	Border border8;

	TitledBorder titledBorder8;

	JRadioButton UUAll = new JRadioButton();

	JRadioButton UUUntil = new JRadioButton();

	JRadioButton UURoot = new JRadioButton();

	JRadioButton UUNone = new JRadioButton();

	JPanel ClassP = new JPanel();

	Border border9;

	TitledBorder titledBorder9;

	JRadioButton UClassYes = new JRadioButton();

	JRadioButton UClassNo = new JRadioButton();

	JPanel SequenceP = new JPanel();

	Border border10;

	TitledBorder titledBorder10;

	JScrollPane USeqScroll = new JScrollPane(this.USeqList);

	JPanel StatechartP = new JPanel();

	Border border11;

	TitledBorder titledBorder11;

	JScrollPane UStateScroll = new JScrollPane();

	JPanel DeploymentP = new JPanel();

	Border border12;

	TitledBorder titledBorder12;

	JRadioButton UDepNo = new JRadioButton();

	JRadioButton UDepYes = new JRadioButton();

	JPanel ActivityP = new JPanel();

	Border border13;

	TitledBorder titledBorder13;

	JTextField UMLActTopText = new JTextField();

	JLabel UMLActTopLable = new JLabel();

	JRadioButton UMLActTop = new JRadioButton();

	JRadioButton UMLActElse = new JRadioButton();

	JScrollPane UMLActScroll = new JScrollPane();

	JButton UMLCancel = new JButton();

	JButton UMLGenerate = new JButton();

	JTextField UUUntilText = new JTextField();

	JRadioButton UMLActNone = new JRadioButton();

	JList UMLActList = new JList();

	JRadioButton UMLActListAll = new JRadioButton();

	JRadioButton UMLActListUntil = new JRadioButton();

	JTextField UMLActListNum = new JTextField();

	JLabel UMLActListLabel = new JLabel();

	ButtonGroup UMLAct2 = new ButtonGroup();

	JRadioButton UStateYes = new JRadioButton();

	JRadioButton UStateNo = new JRadioButton();

	JRadioButton USeqYes = new JRadioButton();

	JRadioButton USeqNo = new JRadioButton();

	ButtonGroup USeqGroup = new ButtonGroup();

	ButtonGroup UStateGroup = new ButtonGroup();

	Vector v_proc = new Vector(1, 1);

	Vector v_obj = new Vector(1, 1);

	Vector v_act = new Vector(1, 1);

	/**
	 * Construct the frame
	 * @param ISystem sys
	 */
	public UMLChooser(ISystem sys) {
		super(sys.getMainFrame(), true);
		this.mySys = sys;
		this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			this.jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		KeyListener kListener = new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					UMLChooser.this.UMLCancel.doClick();
					return;
				}

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					UMLChooser.this.UMLGenerate.doClick();
					return;
				}

			}
		};

		this.addKeyListener(kListener);

		int fX = sys.getMainFrame().getX();
		int fY = sys.getMainFrame().getY();
		int pWidth = sys.getMainFrame().getWidth();
		int pHeight = sys.getMainFrame().getHeight();

		this.setLocation(fX + Math.abs(pWidth / 2 - this.getWidth() / 2), fY
				+ Math.abs(pHeight / 2 - this.getHeight() / 2));

	}

	/**
	 *Component initialization
	 */
	private void jbInit() throws Exception {
		int i = 0;

		//-------------activity list------------------------
		Activity_d act_d = new Activity_d();
		act_d.GetAll_InZoomedProcess(this.mySys.getISystemStructure(),
				this.v_act);
		String data2[] = new String[this.v_act.size()];
		while (i < this.v_act.size()) {
			data2[i] = ((IEntry) this.v_act.get(i)).getName()
					.replace('\n', ' ');
			i++;
		}
		this.UMLActList = new JList(data2);
		//-------------state list--------------------------
		Statechart_d state_d = new Statechart_d();
		state_d.getObjectsForList(this.mySys.getISystemStructure(), this.v_obj);
		String data1[] = new String[this.v_obj.size()];
		i = 0;
		while (i < this.v_obj.size()) {
			data1[i] = ((IEntry) this.v_obj.get(i)).getName()
					.replace('\n', ' ');
			i++;
		}
		this.UStateList = new JList(data1);
		//---------------------Sequence list-------------------------
		sequence_d seq_d = new sequence_d();
		seq_d.getProcForList(this.mySys.getISystemStructure(), this.v_proc);
		String data[] = new String[this.v_proc.size()];
		i = 0;
		while (i < this.v_proc.size()) {
			data[i] = ((IEntry) this.v_proc.get(i)).getName()
					.replace('\n', ' ');
			i++;
		}
		this.USeqList = new JList(data);

		this.UMLMain = (JPanel) this.getContentPane();
		this.border1 = BorderFactory.createEtchedBorder(Color.white, new Color(
				148, 145, 140));
		this.titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(148, 145, 140)),
				"Please select Processes for Sequence Diagrams");
		this.border2 = BorderFactory.createEtchedBorder(Color.white, new Color(
				148, 145, 140));
		this.titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(148, 145, 140)),
				"Please select Objects for Statechart Diagrams");
		this.border3 = BorderFactory.createEtchedBorder(Color.white, new Color(
				148, 145, 140));
		this.titledBorder3 = new TitledBorder(this.border3,
				"Please select levels of OPD for UseCase Diagram");
		this.border4 = BorderFactory.createEmptyBorder();
		this.titledBorder4 = new TitledBorder(this.border4,
				"Generate Class Diagram?");
		this.border5 = BorderFactory.createEtchedBorder(Color.white, new Color(
				142, 142, 142));
		this.titledBorder5 = new TitledBorder(this.border5,
				"Generate Class Diagram?");
		this.border6 = new EtchedBorder(EtchedBorder.RAISED, Color.white,
				new Color(142, 142, 142));
		this.titledBorder6 = new TitledBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(142, 142, 142)),
				"Generate Deployment Diagram?");
		this.border7 = BorderFactory.createEtchedBorder(Color.white, new Color(
				142, 142, 142));
		this.titledBorder7 = new TitledBorder(this.border7,
				"Please select Processes and levels for Activity Diagrm");
		this.border8 = BorderFactory.createEtchedBorder(Color.white, new Color(
				142, 142, 142));
		this.titledBorder8 = new TitledBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(142, 142, 142)), "Use Case Diagram");
		this.border9 = BorderFactory.createEtchedBorder(Color.white, new Color(
				142, 142, 142));
		this.titledBorder9 = new TitledBorder(this.border9, "Class Diagram");
		this.border10 = BorderFactory.createEtchedBorder(Color.white,
				new Color(142, 142, 142));
		this.titledBorder10 = new TitledBorder(this.border10,
				"Sequence Diagrams");
		this.border11 = new EtchedBorder(EtchedBorder.RAISED, Color.white,
				new Color(142, 142, 142));
		this.titledBorder11 = new TitledBorder(BorderFactory
				.createEtchedBorder(Color.white, new Color(142, 142, 142)),
				"Statechart Diagrams");
		this.border12 = BorderFactory.createEtchedBorder(Color.white,
				new Color(142, 142, 142));
		this.titledBorder12 = new TitledBorder(this.border12,
				"Deployment Diagram");
		this.border13 = BorderFactory.createEtchedBorder(Color.white,
				new Color(142, 142, 142));
		this.titledBorder13 = new TitledBorder(this.border13,
				"Activity Diagrams");
		this.UMLMain.setLayout(null);
		this.setResizable(false);
		this.setSize(new Dimension(485, 542));
		this.setTitle("OPM to UML");

		this.UmlP.setBounds(new Rectangle(4, 4, 510, 488));
		this.UmlP.setLayout(null);
		this.UseCaseP.setBorder(this.titledBorder8);
		this.UseCaseP.setBounds(new Rectangle(3, 0, 212, 154));
		this.UseCaseP.setLayout(null);
		this.UUAll.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UMLChooser.this.UUAll_actionPerformed(e);
			}
		});
		this.UUAll.setBounds(new Rectangle(13, 53, 104, 25));
		this.UUAll.setText("All OPD Levels");
		this.UUUntil.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UMLChooser.this.UUUntil_actionPerformed(e);
			}
		});
		this.UUUntil.setBounds(new Rectangle(13, 86, 104, 25));
		this.UUUntil.setText("Until Level");
		this.UURoot.setSelected(true);
		this.UURoot.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UMLChooser.this.UURoot_actionPerformed(e);
			}
		});
		this.UURoot.setBounds(new Rectangle(13, 21, 125, 25));
		this.UURoot.setText("Root Level Only");
		this.UUNone.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UMLChooser.this.UUNone_actionPerformed(e);
			}
		});
		this.UUNone.setBounds(new Rectangle(13, 118, 125, 25));
		this.UUNone.setText("None");
		this.ClassP.setBorder(this.titledBorder9);
		this.ClassP.setBounds(new Rectangle(224, 0, 245, 82));
		this.ClassP.setLayout(null);
		this.UClassYes.setSelected(true);
		this.UClassYes.setBounds(new Rectangle(12, 19, 53, 25));
		this.UClassYes.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UMLChooser.this.UClassYes_actionPerformed(e);
			}
		});
		this.UClassYes.setText("Yes");
		this.UClassNo.setToolTipText("");
		this.UClassNo.setBounds(new Rectangle(12, 51, 57, 25));
		this.UClassNo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UMLChooser.this.UClassNo_actionPerformed(e);
			}
		});
		this.UClassNo.setText("No");
		this.SequenceP.setBorder(this.titledBorder10);
		this.SequenceP.setBounds(new Rectangle(3, 151, 212, 152));
		this.SequenceP.setLayout(null);
		this.USeqScroll.setBounds(new Rectangle(65, 23, 124, 112));
		this.StatechartP.setBorder(this.titledBorder11);
		this.StatechartP.setBounds(new Rectangle(3, 301, 212, 149));
		this.StatechartP.setLayout(null);
		this.UStateScroll.setBounds(new Rectangle(65, 28, 124, 106));
		this.DeploymentP.setBorder(this.titledBorder12);
		this.DeploymentP.setBounds(new Rectangle(224, 81, 245, 83));
		this.DeploymentP.setLayout(null);
		this.UDepNo.setBounds(new Rectangle(15, 54, 65, 25));
		this.UDepNo.setText("No");
		this.UDepNo.setToolTipText("");
		this.UDepYes.setBounds(new Rectangle(15, 22, 78, 25));
		this.UDepYes.setText("Yes");
		this.UDepNo.setSelected(true);
		this.ActivityP.setBorder(this.titledBorder13);
		this.ActivityP.setBounds(new Rectangle(224, 163, 245, 287));
		this.ActivityP.setLayout(null);
		this.UMLActTopText.setToolTipText("");
		this.UMLActTopText.setText("1");
		this.UMLActTopText.setBounds(new Rectangle(152, 51, 31, 25));
		this.UMLActTopLable.setText("levels");
		this.UMLActTopLable.setBounds(new Rectangle(186, 52, 48, 25));
		this.UMLActTop.setText("From Top Level down");
		this.UMLActTop.setBounds(new Rectangle(10, 52, 142, 25));
		this.UMLActTop.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UMLChooser.this.UMLActTop_actionPerformed(e);
			}
		});
		this.UMLActNone.setSelected(true);
		this.UMLActElse.setText("By Processes");
		this.UMLActElse.setBounds(new Rectangle(10, 84, 101, 25));
		this.UMLActElse.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UMLChooser.this.UMLActElse_actionPerformed(e);
			}
		});
		this.UMLActScroll.setBounds(new Rectangle(15, 14, 282, 141));
		this.UMLActScroll.setBounds(new Rectangle(53, 110, 167, 104));
		this.UMLActScroll.setEnabled(false);
		this.UMLCancel.setBounds(new Rectangle(357, 453, 112, 27));
		this.UMLCancel.setText("Cancel");
		this.UMLCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UMLChooser.this.UMLCancel_actionPerformed(e);
			}
		});
		this.UMLGenerate.setBounds(new Rectangle(224, 453, 112, 27));
		this.UMLGenerate.setText("Generate");
		this.UMLGenerate.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UMLChooser.this.UMLGenerate_actionPerformed(e);
			}
		});
		this.UUUntilText.setBounds(new Rectangle(124, 86, 46, 25));
		this.UMLActNone.setText("None");
		this.UMLActNone.setBounds(new Rectangle(10, 23, 103, 25));
		this.UMLActNone.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UMLChooser.this.UMLActNone_actionPerformed(e);
			}
		});
		this.UMLActListAll.setText("All levels down");
		this.UMLActListAll.setBounds(new Rectangle(55, 218, 103, 25));
		this.UMLActListAll
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						UMLChooser.this.UMLActListAll_actionPerformed(e);
					}
				});
		this.UMLActListUntil.setBounds(new Rectangle(55, 240, 17, 25));
		this.UMLActListUntil
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						UMLChooser.this.UMLActListUntil_actionPerformed(e);
					}
				});
		this.UMLActListNum.setBounds(new Rectangle(74, 243, 32, 21));
		this.UMLActListNum
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						UMLChooser.this.UMLActListNum_actionPerformed(e);
					}
				});
		this.UMLActListLabel.setText("levels down");
		this.UMLActListLabel.setBounds(new Rectangle(119, 245, 85, 17));
		this.UStateYes.setText("Yes");
		this.UStateYes.setBounds(new Rectangle(13, 31, 47, 25));
		this.UStateYes.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UMLChooser.this.UStateYes_actionPerformed(e);
			}
		});
		this.UStateNo.setSelected(true);
		this.UStateNo.setText("No");
		this.UStateNo.setBounds(new Rectangle(13, 63, 44, 25));
		this.UStateNo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UMLChooser.this.UStateNo_actionPerformed(e);
			}
		});
		this.UStateNo.setToolTipText("");
		this.USeqNo.setSelected(true);
		this.USeqYes.setBounds(new Rectangle(11, 29, 47, 25));
		this.USeqYes.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UMLChooser.this.USeqYes_actionPerformed(e);
			}
		});
		this.USeqYes.setText("Yes");
		this.USeqNo.setToolTipText("");
		this.USeqNo.setBounds(new Rectangle(11, 61, 44, 25));
		this.USeqNo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UMLChooser.this.USeqNo_actionPerformed(e);
			}
		});
		this.USeqNo.setText("No");
		this.UMLMain.add(this.UmlP, null);
		this.UseCaseP.add(this.UURoot, null);
		this.UseCaseP.add(this.UUNone, null);
		this.UseCaseP.add(this.UUUntil, null);
		this.UseCaseP.add(this.UUAll, null);
		this.UseCaseP.add(this.UUUntilText, null);
		this.ClassP.add(this.UClassYes, null);
		this.ClassP.add(this.UClassNo, null);
		this.DeploymentP.add(this.UDepYes, null);
		this.DeploymentP.add(this.UDepNo, null);
		this.UmlP.add(this.UMLCancel, null);
		this.UmlP.add(this.UMLGenerate, null);
		this.UmlP.add(this.ActivityP, null);
		this.UmlP.add(this.SequenceP, null);
		this.ActivityP.add(this.UMLActTop, null);
		this.ActivityP.add(this.UMLActNone, null);
		this.ActivityP.add(this.UMLActElse, null);
		this.ActivityP.add(this.UMLActTopText, null);
		this.ActivityP.add(this.UMLActTopLable, null);
		this.ActivityP.add(this.UMLActScroll, null);
		this.ActivityP.add(this.UMLActListAll, null);
		this.ActivityP.add(this.UMLActListUntil, null);
		this.ActivityP.add(this.UMLActListNum, null);
		this.ActivityP.add(this.UMLActListLabel, null);
		this.UmlP.add(this.DeploymentP, null);
		this.UMLActScroll.getViewport().add(this.UMLActList, null);
		this.UmlP.add(this.UseCaseP, null);
		this.UUUntilText.setVisible(false);
		this.UUBGroup.add(this.UURoot);
		this.UUBGroup.add(this.UUAll);
		this.UUBGroup.add(this.UUUntil);
		this.UUBGroup.add(this.UUNone);
		this.UClassBGroup.add(this.UClassYes);
		this.UClassBGroup.add(this.UClassNo);
		this.UDepBGroup.add(this.UDepYes);
		this.UDepBGroup.add(this.UDepNo);
		this.UMLActGroup.add(this.UMLActTop);
		this.UMLActGroup.add(this.UMLActElse);
		this.UMLActGroup.add(this.UMLActNone);
		this.UMLActTopText.setEnabled(false);
		this.UMLActTopLable.setEnabled(false);
		this.UMLAct2.add(this.UMLActListAll);
		this.UMLAct2.add(this.UMLActListUntil);
		this.StatechartP.add(this.UStateYes, null);
		this.StatechartP.add(this.UStateNo, null);
		this.StatechartP.add(this.UStateScroll, null);
		this.UStateScroll.getViewport().add(this.UStateList, null);
		this.SequenceP.add(this.USeqScroll, null);
		this.SequenceP.add(this.USeqYes, null);
		this.SequenceP.add(this.USeqNo, null);
		this.UmlP.add(this.StatechartP, null);
		this.USeqScroll.getViewport().add(this.USeqList, null);
		this.UmlP.add(this.ClassP, null);
		this.USeqScroll.setVisible(false);
		this.UStateScroll.setVisible(false);
		this.UMLActListAll.setEnabled(false);
		this.UMLActListAll.setSelected(true);
		this.UMLActListUntil.setEnabled(false);
		this.UMLActListNum.setEnabled(false);
		this.UMLActListLabel.setEnabled(false);
		this.USeqGroup.add(this.USeqYes);
		this.USeqGroup.add(this.USeqNo);
		this.UStateGroup.add(this.UStateYes);
		this.UStateGroup.add(this.UStateNo);
		this.UMLActList.setEnabled(false);
	}

	//Overridden so we can exit when window is closed
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.dispose();
		}
	}

	void UMLActTop_actionPerformed(ActionEvent e) {
		if (this.UMLActTop.isSelected()) {
			this.UMLActScroll.setEnabled(false);
			this.UMLActTopText.setEnabled(true);
			this.UMLActTopLable.setEnabled(true);
			this.UMLActListAll.setEnabled(false);
			this.UMLActListUntil.setEnabled(false);
			this.UMLActListNum.setEnabled(false);
			this.UMLActListLabel.setEnabled(false);
			this.UMLActList.setEnabled(false);
			this.UMLActScroll.setEnabled(false);
		}
	}

	void UMLActElse_actionPerformed(ActionEvent e) {
		if (this.UMLActElse.isSelected()) {
			this.UMLActScroll.setEnabled(true);
			this.UMLActListAll.setEnabled(true);
			this.UMLActList.setEnabled(true);
			this.UMLActListUntil.setEnabled(true);
			this.UMLActListLabel.setEnabled(true);
			this.UMLActTopText.setEnabled(false);
			this.UMLActTopLable.setEnabled(false);
		}
	}

	void UURoot_actionPerformed(ActionEvent e) {
		if (this.UURoot.isSelected()) {
			this.UUUntilText.setVisible(false);
		}

	}

	void UUAll_actionPerformed(ActionEvent e) {
		if (this.UUAll.isSelected()) {
			this.UUUntilText.setVisible(false);
		}

	}

	void UUUntil_actionPerformed(ActionEvent e) {
		if (this.UUUntil.isSelected()) {
			this.UUUntilText.setVisible(true);
		}

	}

	void UUNone_actionPerformed(ActionEvent e) {
		if (this.UUNone.isSelected()) {
			this.UUUntilText.setVisible(false);
		}

	}

	/**
	 * User chose to generate the UML diagrams.
	 */
	private void UMLGenerate_actionPerformed(ActionEvent e) {
		boolean Uflag = false;
		try {

			int input;
			boolean Sflag = false;
			boolean Aflag = false;
			boolean Seqflag = false;

			if (this.UUUntil.isSelected()) {
				Uflag = true;
				input = Integer.parseInt(this.UUUntilText.getText());
				if (input < 1) {
					input = Integer.parseInt("jdkfh");
				}
			}
			if ((this.UMLActListUntil.isSelected())
					&& (this.UMLActElse.isSelected())) {
				input = Integer.parseInt(this.UMLActListNum.getText());
				if (input < 1) {
					input = Integer.parseInt("jdkfh");
				}
			}
			if (this.UMLActTop.isSelected()) {
				input = Integer.parseInt(this.UMLActTopText.getText());
				if (input < 1) {
					input = Integer.parseInt("jdkfh");
				}
			}

			if ((this.UStateYes.isSelected()) && (!this.UClassYes.isSelected())) {
				JOptionPane
						.showMessageDialog(
								this,
								"To create Statechart Diagram you need to choose:\n Class Diagram : Yes",
								"Error", JOptionPane.ERROR_MESSAGE);
				Sflag = true;
			} else if (this.UStateYes.isSelected()) {
				int selected[] = this.UStateList.getSelectedIndices();
				if (selected.length <= 0) {
					JOptionPane
							.showMessageDialog(
									this,
									"Illegal input in statechart. Please select object.",
									"ERROR", JOptionPane.ERROR_MESSAGE);
					Sflag = true;
				}
			}

			if ((this.UMLActElse.isSelected()) && (!Sflag)) {
				int selected[] = this.UMLActList.getSelectedIndices();
				if (selected.length <= 0) {
					JOptionPane
							.showMessageDialog(
									this,
									"Illegal input in activity. Please select process.",
									"ERROR", JOptionPane.ERROR_MESSAGE);
					Aflag = true;
				}
			}

			if ((this.USeqYes.isSelected())
					&& ((!this.UUAll.isSelected()) || (!this.UClassYes
							.isSelected())) && (!Sflag) && (!Aflag)) {
				JOptionPane
						.showMessageDialog(
								this,
								"To create Sequence Diagram you need to choose:\n Class Diagram : Yes\n Use Case Diagram : All OPD Levels",
								"Error", JOptionPane.ERROR_MESSAGE);
				Seqflag = true;
			} else if (this.USeqYes.isSelected() && (!Sflag) && (!Aflag)) {
				int selected[] = this.USeqList.getSelectedIndices();
				if (selected.length <= 0) {
					JOptionPane.showMessageDialog(this,
							"Illigal input in sequence.Please select process.",
							"ERROR", JOptionPane.ERROR_MESSAGE);
					Seqflag = true;
				}
			}

			if (!Aflag && !Sflag && !Seqflag) {
				String filename = "";
				UMLSaver uml_save = new UMLSaver();
				int returnVal = uml_save.UMLSaver.showSaveDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					filename = uml_save.UMLSaver.getSelectedFile().getPath();
					String name = uml_save.UMLSaver.getSelectedFile().getName();
					StringTokenizer st = new StringTokenizer(filename, ".",
							false);
					filename = st.nextToken() + ".xmi";
					File myDir = uml_save.UMLSaver.getCurrentDirectory();
					boolean isFileExist = false; 
					int i = 0;
					if (myDir.exists()) {
						File[] contents = myDir.listFiles();
						while (i < contents.length) {
							if (contents[i].getName().compareTo(name) == 0) {
								isFileExist = true;
							}
							i++;
						}//end of while
					}//end of if
					DiagramsCreator xmiCreator = new DiagramsCreator();
					UserDesicion desicion = new UserDesicion();
					desicion.UClassYes = UClassYes.isSelected();
					desicion.UURoot = UURoot.isSelected();
					desicion.UUAll = UUAll.isSelected();
					desicion.UUNone = UUNone.isSelected();
					desicion.UUUntil = UUUntil.isSelected();
					
					desicion.UStateYes = UStateYes.isSelected();
					desicion.USeqYes = USeqYes.isSelected();
					desicion.UMLActTop = UMLActTop.isSelected();
					desicion.UMLActElse = UMLActElse.isSelected();
					desicion.UMLActListAll = UMLActListAll.isSelected();
					desicion.UDepYes = UDepYes.isSelected();

					desicion.UMLActTopText = UMLActTopText.getText();
					desicion.UMLActListNum = UMLActListNum.getText();
					desicion.UUUntilText = UUUntilText.getText();
					desicion.USeqList = USeqList.getSelectedIndices();
					desicion.UStateList = UStateList.getSelectedIndices();
					desicion.UMLActList = UMLActList.getSelectedIndices();
					
					desicion.v_proc = v_proc;
					desicion.v_obj = v_obj;
					desicion.v_act = v_act;

					int retval = JOptionPane.NO_OPTION;
					if (isFileExist) {
						retval = JOptionPane
								.showConfirmDialog(
										this,
										"A file with this name already exists. Do you want to overwrite it?",
										"Overwrite Document",
										JOptionPane.YES_NO_OPTION);
					}
					if (!isFileExist || retval == JOptionPane.YES_OPTION)	{
							if ((this.UClassYes.isSelected())
									|| (this.UUNone.isSelected() == false)
									|| (this.UStateYes.isSelected())
									|| (this.UDepYes.isSelected())
									|| (this.UMLActNone.isSelected() == false)
									|| (this.USeqYes.isSelected())) {
								try {
									xmiCreator.generateXmiFile(this.mySys, filename, desicion);
								} catch (OpcatException er) {
									JOptionPane.showMessageDialog(this,
											"An error occured while generating the XMI file.\n " +
											"Please review the log file for details.",
											"XMI Generation Error", JOptionPane.ERROR_MESSAGE);
									OpcatLogger.logError(er);
								}
								this.dispose();
							}//end of if
						}//end of if
					}//end of if
				}//end of if
		}//end of try
		catch (NumberFormatException ne) {
			if (Uflag) {
				JOptionPane
						.showMessageDialog(
								this,
								"Illegal input in usecase: level is not valid.\n Please insert an integer.",
								"ERROR", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane
						.showMessageDialog(
								this,
								"Illegal input in activity: level is not valid.\n Please insert an integer.",
								"ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}//end of catch
	}//end of func

	void UMLCancel_actionPerformed(ActionEvent e) {
		this.dispose();
	}

	void UMLActNone_actionPerformed(ActionEvent e) {
		if (this.UMLActNone.isSelected()) {
			this.UMLActTopText.setEnabled(false);
			this.UMLActTopLable.setEnabled(false);
			this.UMLActScroll.setEnabled(false);
			this.UMLActListAll.setEnabled(false);
			this.UMLActListUntil.setEnabled(false);
			this.UMLActListNum.setEnabled(false);
			this.UMLActListLabel.setEnabled(false);
			this.UMLActList.setEnabled(false);
		}
	}

	void USeqYes_actionPerformed(ActionEvent e) {
		if (this.USeqYes.isSelected()) {
			this.USeqScroll.setVisible(true);
		}
		if (!this.USeqYes.isSelected()) {
			this.USeqScroll.setVisible(false);
		}
	}

	void UStateYes_actionPerformed(ActionEvent e) {
		if (this.UStateYes.isSelected()) {
			this.UStateScroll.setVisible(true);
		}
		if (!this.UStateYes.isSelected()) {
			this.UStateScroll.setVisible(false);
		}
	}

	void USeqNo_actionPerformed(ActionEvent e) {
		if (!this.USeqNo.isSelected()) {
			this.USeqScroll.setVisible(true);
		}
		if (this.USeqNo.isSelected()) {
			this.USeqScroll.setVisible(false);
		}
	}

	void UStateNo_actionPerformed(ActionEvent e) {
		if (this.UStateNo.isSelected()) {
			this.UStateScroll.setVisible(false);
		}
		if (!this.UStateNo.isSelected()) {
			this.UStateScroll.setVisible(true);
		}
	}

	void UMLActListAll_actionPerformed(ActionEvent e) {
		if (this.UMLActListAll.isSelected()) {
			this.UMLActListNum.setEnabled(false);
		}
		if (!this.UMLActListAll.isSelected()) {
			this.UMLActListNum.setEnabled(true);
		}
	}

	void UMLActListUntil_actionPerformed(ActionEvent e) {
		if (this.UMLActListUntil.isSelected()) {
			this.UMLActListNum.setEnabled(true);
		}
		if (!this.UMLActListUntil.isSelected()) {
			this.UMLActListNum.setEnabled(false);
		}

	}

	void UMLActListNum_actionPerformed(ActionEvent e) {

	}

	void UClassNo_actionPerformed(ActionEvent e) {
	}

	void UClassYes_actionPerformed(ActionEvent e) {
		if (this.UClassYes.isSelected()) {
			this.UStateYes.setEnabled(true);
			this.UStateNo.setEnabled(true);
			this.StatechartP.setEnabled(true);
			if (this.UStateYes.isSelected()) {
				this.UStateScroll.setVisible(true);
			}
		}
	}

}