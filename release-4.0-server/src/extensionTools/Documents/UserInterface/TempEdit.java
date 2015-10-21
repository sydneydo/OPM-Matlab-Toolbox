package extensionTools.Documents.UserInterface;

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
import java.io.IOException;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import exportedAPI.opcatAPI.ISystem;
import extensionTools.Documents.Doc.Document;

/**
 * TempEdit Class extends the standard JDialog. Allows user to edit a template
 * or create a new one, by selecting the contents he is interested in. All the
 * choices of the user regarding the fields of the General Info, OPD, OPL and
 * Elements Dictionary are done in this screen. Parallel to the DocGen class
 * (for Documents).
 * 
 * @author Olga Tavrovsky
 * @author Anna Levit
 */

public class TempEdit extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	ISystem mySys;

	String filename;

	JPanel contentPane;

	JTabbedPane TTPMain = new JTabbedPane();

	JPanel TGI = new JPanel();

	JPanel TOPDOPL = new JPanel();

	JPanel TDD = new JPanel();

	TitledBorder titledBorder1;

	JTabbedPane TTPDD = new JTabbedPane();

	JPanel TDDObjects = new JPanel();

	JPanel TDDProcesses = new JPanel();

	JPanel TDDRelations = new JPanel();

	JPanel TDDLinks = new JPanel();

	JPanel TButtonsPanel = new JPanel();

	JButton TCancel = new JButton();

	JPanel TGILeft = new JPanel();

	JCheckBox TGIClient = new JCheckBox();

	JCheckBox TGIOverview = new JCheckBox();

	JCheckBox TGIFuture = new JCheckBox();

	JCheckBox TGIUsers = new JCheckBox();

	JCheckBox TGIGoals = new JCheckBox();

	JCheckBox TGICurrent = new JCheckBox();

	JPanel TOPL = new JPanel();

	JPanel TDDObjMain = new JPanel();

	JCheckBox TDDObjOrigin = new JCheckBox();

	JCheckBox TDDObjDesc = new JCheckBox();

	JCheckBox TDDObjType = new JCheckBox();

	JCheckBox TDDObjStates = new JCheckBox();

	JCheckBox TDDObjScope = new JCheckBox();

	JCheckBox TDDObjEssence = new JCheckBox();

	JPanel TOPD = new JPanel();

	JRadioButton TOPDByName = new JRadioButton();

	JRadioButton TOPDUntil = new JRadioButton();

	JRadioButton TOPDAll = new JRadioButton();

	JRadioButton TOPDNone = new JRadioButton();

	ButtonGroup BG_TOPD = new ButtonGroup();

	ButtonGroup BG_TOPL = new ButtonGroup();

	JCheckBox TDDObjIndex = new JCheckBox();

	JCheckBox TDDObjInitVal = new JCheckBox();

	JPanel TDDObjState = new JPanel();

	JCheckBox TDDObjStateDescr = new JCheckBox();

	JCheckBox TDDObjStateInitial = new JCheckBox();

	Border border1;

	TitledBorder titledBorder2;

	Border border2;

	TitledBorder titledBorder3;

	Border border3;

	TitledBorder titledBorder4;

	Border border4;

	TitledBorder titledBorder5;

	JPanel TDDProcMain = new JPanel();

	Border border5;

	TitledBorder titledBorder6;

	JCheckBox TDDProcEssence = new JCheckBox();

	JCheckBox TDDProcOrigin = new JCheckBox();

	JCheckBox TDDProcDescr = new JCheckBox();

	JCheckBox TDDProcActTime = new JCheckBox();

	JCheckBox TDDProcScope = new JCheckBox();

	JCheckBox TDDProcBody = new JCheckBox();

	Border border6;

	TitledBorder titledBorder7;

	JPanel TDDRelMain = new JPanel();

	Border border7;

	TitledBorder titledBorder8;

	JCheckBox TDDRelAggreg = new JCheckBox();

	JCheckBox TDDRelFeature = new JCheckBox();

	JCheckBox TDDRelGeneral = new JCheckBox();

	JCheckBox TDDRelClassif = new JCheckBox();

	JCheckBox TDDRelUni = new JCheckBox();

	JCheckBox TDDRelBi = new JCheckBox();

	Border border8;

	TitledBorder titledBorder9;

	JPanel TDDLinkMain = new JPanel();

	JCheckBox TDDLinkInvoc = new JCheckBox();

	JCheckBox TDDLinkInsrEv = new JCheckBox();

	JCheckBox TDDLinkCondition = new JCheckBox();

	JCheckBox TDDLinkInstr = new JCheckBox();

	JCheckBox TDDLinkEvent = new JCheckBox();

	JCheckBox TDDLinkRes = new JCheckBox();

	JCheckBox TDDLinkEffect = new JCheckBox();

	JCheckBox TDDLinkAgent = new JCheckBox();

	JCheckBox TDDLinkExeption = new JCheckBox();

	Border border9;

	TitledBorder titledBorder10;

	JCheckBox TGIIssues = new JCheckBox();

	JCheckBox TGIInputs = new JCheckBox();

	JCheckBox TGIHard = new JCheckBox();

	JCheckBox TGIProblems = new JCheckBox();

	JCheckBox TGIBusiness = new JCheckBox();

	JCheckBox TGIOper = new JCheckBox();

	Border border10;

	TitledBorder titledBorder11;

	JTextField TOPLUntilText = new JTextField();

	JTextField TOPDUntilText = new JTextField();

	JRadioButton TOPLUntil = new JRadioButton();

	JRadioButton TOPLAll = new JRadioButton();

	JRadioButton TOPLNone = new JRadioButton();

	JRadioButton TOPLByName = new JRadioButton();

	JRadioButton TOPLandOPD = new JRadioButton();

	JCheckBox TDDLinkPropCond = new JCheckBox();

	JPanel TDDLinkProp = new JPanel();

	JCheckBox TDDLinkPropPath = new JCheckBox();

	JCheckBox TDDLinkPropReacTime = new JCheckBox();

	JButton TSaveAs = new JButton();

	JCheckBox TDDObjStateTime = new JCheckBox();

	JPanel TNamePanel = new JPanel();

	JLabel TNameLable = new JLabel();

	JTextField TNameText = new JTextField();

	JDialog dad;

	JButton SelAll = new JButton();

	JButton UnSelAll = new JButton();

	JButton ObjSelAll = new JButton();

	JButton ObjUnSelAll = new JButton();

	JButton ProcUnSelAll = new JButton();

	JButton ProcSelAll = new JButton();

	JButton LinkUnSelAll = new JButton();

	JButton LinkSelAll = new JButton();

	JButton RelSelAll = new JButton();

	JButton RelUnSelAll = new JButton();

	/**
	 * Constructs the frame.
	 * 
	 * @param sys
	 *            of type ISystem
	 * @param file
	 *            of type String - the name of the template
	 * @param prev
	 *            of type JDialog - the previous screen
	 */
	public TempEdit(ISystem sys, String file, JDialog prev) {
		super(sys.getMainFrame(), true);
		this.dad = prev;
		this.mySys = sys;
		this.filename = file;
		this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			this.jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Esc and Enter Keys active
		KeyListener kListener = new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					TempEdit.this.TCancel.doClick();
					return;
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					TempEdit.this.TSaveAs.doClick();
					return;
				}
			}
		};
		this.addKeyListener(kListener);
		// set the screen in the center
		int fX = sys.getMainFrame().getX();
		int fY = sys.getMainFrame().getY();
		int pWidth = sys.getMainFrame().getWidth();
		int pHeight = sys.getMainFrame().getHeight();
		this.setLocation(fX + Math.abs(pWidth / 2 - this.getWidth() / 2), fY
				+ Math.abs(pHeight / 2 - this.getHeight() / 2));
	}

	/**
	 * Component initialization.
	 */
	private void jbInit() {
		this.contentPane = (JPanel) this.getContentPane();
		this.titledBorder1 = new TitledBorder("");
		this.border1 = BorderFactory.createEtchedBorder(Color.white, new Color(148,
				145, 140));
		this.titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(148, 145, 140)),
				"General Information Fields");
		this.border2 = BorderFactory.createEtchedBorder(Color.white, new Color(148,
				145, 140));
		this.titledBorder3 = new TitledBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(148, 145, 140)), "OPL Levels");
		this.border3 = BorderFactory.createEtchedBorder(Color.white, new Color(148,
				145, 140));
		this.titledBorder4 = new TitledBorder(this.border3, "State Properties");
		this.border4 = BorderFactory.createEtchedBorder(Color.white, new Color(148,
				145, 140));
		this.titledBorder5 = new TitledBorder(this.border4, "Object Properties");
		this.border5 = new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(
				148, 145, 140));
		this.titledBorder6 = new TitledBorder(this.border5, "Process Properties");
		this.border6 = BorderFactory.createEtchedBorder(Color.white, new Color(148,
				145, 140));
		this.titledBorder7 = new TitledBorder(this.border6, "Process Properties");
		this.border7 = BorderFactory.createEtchedBorder(Color.white, new Color(148,
				145, 140));
		this.titledBorder8 = new TitledBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(148, 145, 140)), "Relation Types");
		this.border8 = BorderFactory.createEtchedBorder(Color.white, new Color(148,
				145, 140));
		this.titledBorder9 = new TitledBorder(this.border8, "Link Properties:");
		this.border9 = BorderFactory.createEtchedBorder(Color.white, new Color(148,
				145, 140));
		this.titledBorder10 = new TitledBorder(this.border9, "Link Types");
		this.border10 = BorderFactory.createEtchedBorder(Color.white, new Color(148,
				145, 140));
		this.titledBorder11 = new TitledBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(148, 145, 140)), "OPD Levels");
		this.contentPane.setLayout(null);
		this.setResizable(false);
		this.setSize(new Dimension(580, 428));
		this.setTitle("Make/Edit Template");
		this.TGI.setLayout(null);
		this.TOPDOPL.setLayout(null);
		this.TDD.setLayout(null);
		this.TTPDD.setBackground(new Color(204, 204, 204));
		this.TTPDD.setBounds(new Rectangle(-5, 0, 566, 264));
		this.TDDObjects.setLayout(null);
		this.TDDProcesses.setLayout(null);
		this.TDDRelations.setLayout(null);
		this.TDDLinks.setLayout(null);
		this.TTPMain.setBackground(new Color(204, 204, 204));
		this.TTPMain.setBounds(new Rectangle(3, 42, 563, 291));
		this.TButtonsPanel.setBounds(new Rectangle(1, 338, 565, 50));
		this.TButtonsPanel.setLayout(null);
		this.TCancel.setBounds(new Rectangle(438, 1, 127, 27));
		this.TCancel.setBorder(BorderFactory.createEtchedBorder());
		this.TCancel.setToolTipText("");
		this.TCancel.setText("Cancel");
		this.TCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.TCancel_actionPerformed(e);
			}
		});
		this.TGILeft.setBorder(this.titledBorder2);
		this.TGILeft.setBounds(new Rectangle(12, 17, 536, 237));
		this.TGILeft.setLayout(null);
		this.TGIClient.setBorder(BorderFactory.createEtchedBorder());
		this.TGIClient.setText("Client");
		this.TGIClient.setBounds(new Rectangle(9, 30, 90, 25));
		this.TGIOverview.setBorder(null);
		this.TGIOverview.setText("System Overview");
		this.TGIOverview.setBounds(new Rectangle(9, 58, 117, 25));
		this.TGIFuture.setBorder(BorderFactory.createEtchedBorder());
		this.TGIFuture.setText("Future Goals");
		this.TGIFuture.setBounds(new Rectangle(9, 115, 100, 25));
		this.TGIUsers.setBorder(BorderFactory.createEtchedBorder());
		this.TGIUsers.setText("Possible Users for the System");
		this.TGIUsers.setBounds(new Rectangle(9, 171, 199, 25));
		this.TGIGoals.setBorder(BorderFactory.createEtchedBorder());
		this.TGIGoals.setText("Goals and Objectives of the Project");
		this.TGIGoals.setBounds(new Rectangle(9, 143, 219, 25));
		this.TGICurrent.setBorder(BorderFactory.createEtchedBorder());
		this.TGICurrent.setText("The Current State");
		this.TGICurrent.setBounds(new Rectangle(9, 86, 136, 25));
		this.TOPL.setBorder(this.titledBorder3);
		this.TOPL.setBounds(new Rectangle(298, 21, 209, 200));
		this.TOPL.setLayout(null);
		this.TDDObjMain.setBorder(this.titledBorder5);
		this.TDDObjMain.setBounds(new Rectangle(19, 17, 531, 212));
		this.TDDObjMain.setLayout(null);
		this.TDDObjOrigin.setText("Origin");
		this.TDDObjOrigin.setBounds(new Rectangle(14, 100, 58, 25));
		this.TDDObjDesc.setText("Description ");
		this.TDDObjDesc.setBounds(new Rectangle(14, 64, 91, 25));
		this.TDDObjType.setText("Object Type ");
		this.TDDObjType.setBounds(new Rectangle(14, 27, 92, 25));
		this.TDDObjStates.setText("States");
		this.TDDObjStates.setBounds(new Rectangle(175, 134, 60, 25));
		this.TDDObjStates.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.TDDObjStates_actionPerformed(e);
			}
		});
		this.TDDObjScope.setText("Scope");
		this.TDDObjScope.setBounds(new Rectangle(14, 134, 60, 25));
		this.TDDObjEssence.setText("Essence");
		this.TDDObjEssence.setBounds(new Rectangle(175, 27, 74, 25));
		this.TOPD.setBorder(this.titledBorder11);
		this.TOPD.setBounds(new Rectangle(48, 21, 209, 200));
		this.TOPD.setLayout(null);
		this.TOPDByName.setText("By Name");
		this.TOPDByName.setBounds(new Rectangle(13, 97, 89, 25));
		this.TOPDByName.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.TOPDByName_actionPerformed(e);
			}
		});
		this.TOPDUntil.setText("Until Level");
		this.TOPDUntil.setBounds(new Rectangle(13, 65, 89, 25));
		this.TOPDUntil.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.TOPDUntil_actionPerformed(e);
			}
		});
		this.TOPDAll.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.TOPDAll_actionPerformed(e);
			}
		});
		this.TOPDAll.setActionCommand("");
		this.TOPDAll.setText("All Levels");
		this.TOPDAll.setBounds(new Rectangle(13, 33, 89, 25));
		this.TOPDNone.setText("None");
		this.TOPDNone.setBounds(new Rectangle(13, 129, 89, 25));
		this.TOPDNone.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.TOPDNone_actionPerformed(e);
			}
		});
		this.TDDObjIndex.setText("Index");
		this.TDDObjIndex.setBounds(new Rectangle(175, 64, 54, 25));
		this.TDDObjInitVal.setText("Initial Value");
		this.TDDObjInitVal.setBounds(new Rectangle(175, 100, 88, 25));
		this.TDDObjState.setBorder(this.titledBorder4);
		this.TDDObjState.setBounds(new Rectangle(368, 31, 134, 127));
		this.TDDObjState.setLayout(null);
		this.TDDObjStateDescr.setEnabled(true);
		this.TDDObjStateDescr.setText("Description");
		this.TDDObjStateDescr.setBounds(new Rectangle(16, 65, 88, 25));
		this.TDDObjStateInitial.setEnabled(true);
		this.TDDObjStateInitial.setText("Initial/Final");
		this.TDDObjStateInitial.setBounds(new Rectangle(16, 34, 88, 25));
		this.TDDProcMain.setBorder(this.titledBorder7);
		this.TDDProcMain.setBounds(new Rectangle(19, 17, 531, 211));
		this.TDDProcMain.setLayout(null);
		this.TDDProcEssence.setText("Essence");
		this.TDDProcEssence.setBounds(new Rectangle(14, 63, 92, 25));
		this.TDDProcOrigin.setText("Origin");
		this.TDDProcOrigin.setBounds(new Rectangle(14, 134, 92, 25));
		this.TDDProcDescr.setText("Description");
		this.TDDProcDescr.setBounds(new Rectangle(14, 27, 92, 25));
		this.TDDProcActTime.setText("Activation time");
		this.TDDProcActTime.setBounds(new Rectangle(176, 27, 103, 25));
		this.TDDProcScope.setText("Scope");
		this.TDDProcScope.setBounds(new Rectangle(14, 98, 92, 25));
		this.TDDProcBody.setText("Body");
		this.TDDProcBody.setBounds(new Rectangle(178, 63, 52, 25));
		this.TDDRelMain.setBorder(this.titledBorder8);
		this.TDDRelMain.setBounds(new Rectangle(19, 17, 531, 211));
		this.TDDRelMain.setLayout(null);
		this.TDDRelAggreg.setText("Aggregation-Participation Relations");
		this.TDDRelAggreg.setBounds(new Rectangle(14, 27, 235, 25));
		this.TDDRelFeature.setText("Featuring-Characterization Relations");
		this.TDDRelFeature.setBounds(new Rectangle(14, 63, 235, 25));
		this.TDDRelGeneral.setText("Generalization-Specification Relations");
		this.TDDRelGeneral.setBounds(new Rectangle(14, 98, 235, 25));
		this.TDDRelClassif.setText("Classification-Instantiation Relations");
		this.TDDRelClassif.setBounds(new Rectangle(14, 134, 235, 25));
		this.TDDRelUni.setText("Uni-Directional General Relations");
		this.TDDRelUni.setBounds(new Rectangle(284, 27, 230, 25));
		this.TDDRelBi.setText("Bi-directional General Relations");
		this.TDDRelBi.setBounds(new Rectangle(284, 63, 230, 25));
		this.TDDLinkMain.setBorder(this.titledBorder10);
		this.TDDLinkMain.setBounds(new Rectangle(19, 17, 530, 211));
		this.TDDLinkMain.setLayout(null);
		this.TDDLinkInvoc.setText("Invocation Links");
		this.TDDLinkInvoc.setBounds(new Rectangle(194, 59, 125, 25));
		this.TDDLinkInvoc.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.TDDLinkInvoc_actionPerformed(e);
			}
		});
		this.TDDLinkInsrEv.setText("Instrument Event Links");
		this.TDDLinkInsrEv.setBounds(new Rectangle(194, 91, 151, 25));
		this.TDDLinkInsrEv.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.TDDLinkInsrEv_actionPerformed(e);
			}
		});
		this.TDDLinkCondition.setText("Condition Links");
		this.TDDLinkCondition.setBounds(new Rectangle(194, 123, 132, 25));
		this.TDDLinkCondition.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.TDDLinkCondition_actionPerformed(e);
			}
		});
		this.TDDLinkInstr.setText("Instrument Links");
		this.TDDLinkInstr.setBounds(new Rectangle(14, 59, 173, 25));
		this.TDDLinkInstr.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.TDDLinkInstr_actionPerformed(e);
			}
		});
		this.TDDLinkEvent.setText("Event Links");
		this.TDDLinkEvent.setBounds(new Rectangle(14, 155, 173, 25));
		this.TDDLinkEvent.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.TDDLinkEvent_actionPerformed(e);
			}
		});
		this.TDDLinkRes.setText("Result/Consumption Links");
		this.TDDLinkRes.setBounds(new Rectangle(14, 91, 173, 25));
		this.TDDLinkRes.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.TDDLinkRes_actionPerformed(e);
			}
		});
		this.TDDLinkEffect.setText("Effect Links");
		this.TDDLinkEffect.setBounds(new Rectangle(14, 123, 173, 25));
		this.TDDLinkEffect.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.TDDLinkEffect_actionPerformed(e);
			}
		});
		this.TDDLinkAgent.setText("Agent Links");
		this.TDDLinkAgent.setBounds(new Rectangle(14, 27, 173, 25));
		this.TDDLinkAgent.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.TDDLinkAgent_actionPerformed(e);
			}
		});
		this.TDDLinkExeption.setText("Exception Links");
		this.TDDLinkExeption.setBounds(new Rectangle(194, 27, 116, 25));
		this.TDDLinkExeption.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.TDDLinkExeption_actionPerformed(e);
			}
		});
		this.TGIIssues.setBorder(BorderFactory.createEtchedBorder());
		this.TGIIssues.setText("Open Issues");
		this.TGIIssues.setBounds(new Rectangle(249, 115, 96, 25));
		this.TGIInputs.setBorder(BorderFactory.createEtchedBorder());
		this.TGIInputs.setText("Inputs, Processing functionality and Outputs");
		this.TGIInputs.setBounds(new Rectangle(249, 86, 269, 25));
		this.TGIHard.setBorder(BorderFactory.createEtchedBorder());
		this.TGIHard.setText("Hardware and Software Requirements");
		this.TGIHard.setBounds(new Rectangle(249, 171, 236, 25));
		this.TGIProblems.setBorder(BorderFactory.createEtchedBorder());
		this.TGIProblems.setText("Problems");
		this.TGIProblems.setBounds(new Rectangle(249, 143, 79, 25));
		this.TGIBusiness.setBorder(BorderFactory.createEtchedBorder());
		this.TGIBusiness.setText("Business or Program Constraints");
		this.TGIBusiness.setBounds(new Rectangle(249, 58, 210, 25));
		this.TGIOper.setBorder(BorderFactory.createEtchedBorder());
		this.TGIOper.setText("Operation and Maintenance");
		this.TGIOper.setBounds(new Rectangle(249, 30, 176, 25));
		this.TOPLUntilText.setBounds(new Rectangle(131, 58, 39, 25));
		this.TOPLUntilText.setVisible(false);
		this.TOPDUntilText.setVisible(false);
		this.TOPDUntilText.setBounds(new Rectangle(110, 65, 38, 25));
		this.TOPLUntil.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.TOPLUntil_actionPerformed(e);
			}
		});
		this.TOPLUntil.setBounds(new Rectangle(14, 58, 89, 25));
		this.TOPLUntil.setText("Until Level");
		this.TOPLAll.setBounds(new Rectangle(14, 27, 89, 25));
		this.TOPLAll.setText("All Levels");
		this.TOPLAll.setActionCommand("");
		this.TOPLAll.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.TOPLAll_actionPerformed(e);
			}
		});
		this.TOPLNone.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.TOPLNone_actionPerformed(e);
			}
		});
		this.TOPLNone.setBounds(new Rectangle(14, 121, 89, 25));
		this.TOPLNone.setText("None");
		this.TOPLByName.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.TOPLByName_actionPerformed(e);
			}
		});
		this.TOPLByName.setBounds(new Rectangle(14, 90, 89, 25));
		this.TOPLByName.setText("By Name");
		this.TOPLandOPD.setText("According to OPD");
		this.TOPLandOPD.setBounds(new Rectangle(14, 152, 128, 25));
		this.TOPLandOPD.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.OPLandTOPD_actionPerformed(e);
			}
		});
		this.TDDLinkPropCond.setText("Condition");
		this.TDDLinkPropCond.setBounds(new Rectangle(20, 27, 78, 25));
		this.TDDLinkProp.setBorder(this.titledBorder9);
		this.TDDLinkProp.setBounds(new Rectangle(377, 23, 130, 137));
		this.TDDLinkProp.setLayout(null);
		this.TDDLinkProp.setVisible(false);
		this.TDDLinkPropPath.setText("Path");
		this.TDDLinkPropPath.setBounds(new Rectangle(20, 59, 50, 25));
		this.TDDLinkPropReacTime.setText("Reaction time");
		this.TDDLinkPropReacTime.setBounds(new Rectangle(20, 91, 101, 25));
		this.TSaveAs.setBounds(new Rectangle(297, 1, 127, 27));
		this.TSaveAs.setBorder(BorderFactory.createEtchedBorder());
		this.TSaveAs.setText("Save");
		this.TSaveAs.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.TSaveAs_actionPerformed(e);
			}
		});
		this.TDDObjStateTime.setText("Activation Time");
		this.TDDObjStateTime.setBounds(new Rectangle(16, 95, 111, 25));
		this.TNamePanel.setBounds(new Rectangle(0, 11, 573, 29));
		this.TNamePanel.setLayout(null);
		this.TNameLable.setText("Template Name");
		this.TNameLable.setBounds(new Rectangle(14, 6, 113, 17));
		this.TNameText.setBounds(new Rectangle(124, 4, 154, 21));
		this.SelAll.setBounds(new Rectangle(293, 203, 114, 27));
		this.SelAll.setText("Select All");
		this.SelAll.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.SelAll_actionPerformed(e);
			}
		});
		this.UnSelAll.setBounds(new Rectangle(413, 203, 114, 27));
		this.UnSelAll.setText("Unselect All");
		this.UnSelAll.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.UnSelAll_actionPerformed(e);
			}
		});
		this.ObjSelAll.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.ObjSelAll_actionPerformed(e);
			}
		});
		this.ObjSelAll.setText("Select All");
		this.ObjSelAll.setBounds(new Rectangle(289, 176, 114, 27));
		this.ObjUnSelAll.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.ObjUnSelAll_actionPerformed(e);
			}
		});
		this.ObjUnSelAll.setText("Unselect All");
		this.ObjUnSelAll.setBounds(new Rectangle(409, 176, 114, 27));
		this.ProcUnSelAll.setBounds(new Rectangle(409, 175, 114, 27));
		this.ProcUnSelAll.setText("Unselect All");
		this.ProcUnSelAll.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.ProcUnSelAll_actionPerformed(e);
			}
		});
		this.ProcSelAll.setBounds(new Rectangle(289, 175, 114, 27));
		this.ProcSelAll.setText("Select All");
		this.ProcSelAll.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.ProcSelAll_actionPerformed(e);
			}
		});
		this.LinkUnSelAll.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.LinkUnSelAll_actionPerformed(e);
			}
		});
		this.LinkUnSelAll.setText("Unselect All");
		this.LinkUnSelAll.setBounds(new Rectangle(405, 173, 114, 27));
		this.LinkSelAll.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.LinkSelAll_actionPerformed(e);
			}
		});
		this.LinkSelAll.setText("Select All");
		this.LinkSelAll.setBounds(new Rectangle(285, 173, 114, 27));
		this.RelSelAll.setBounds(new Rectangle(288, 175, 114, 27));
		this.RelSelAll.setText("Select All");
		this.RelSelAll.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.RelSelAll_actionPerformed(e);
			}
		});
		this.RelUnSelAll.setBounds(new Rectangle(408, 175, 114, 27));
		this.RelUnSelAll.setText("Unselect All");
		this.RelUnSelAll.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TempEdit.this.RelUnSelAll_actionPerformed(e);
			}
		});
		this.TButtonsPanel.add(this.TCancel, null);
		this.TButtonsPanel.add(this.TSaveAs, null);
		this.TDDLinkProp.add(this.TDDLinkPropCond, null);
		this.TDDLinkProp.add(this.TDDLinkPropPath, null);
		this.TDDLinkProp.add(this.TDDLinkPropReacTime, null);
		this.TDDLinkMain.add(this.LinkUnSelAll, null);
		this.TDDLinkMain.add(this.LinkSelAll, null);
		this.TDDRelations.add(this.TDDRelMain, null);
		this.TDDLinks.add(this.TDDLinkMain, null);
		this.TDDLinkMain.add(this.TDDLinkAgent, null);
		this.TDDLinkMain.add(this.TDDLinkInstr, null);
		this.TDDLinkMain.add(this.TDDLinkRes, null);
		this.TDDLinkMain.add(this.TDDLinkEffect, null);
		this.TDDLinkMain.add(this.TDDLinkEvent, null);
		this.TDDLinkMain.add(this.TDDLinkExeption, null);
		this.TDDLinkMain.add(this.TDDLinkInvoc, null);
		this.TDDLinkMain.add(this.TDDLinkInsrEv, null);
		this.TDDLinkMain.add(this.TDDLinkCondition, null);
		this.TDDLinkMain.add(this.TDDLinkProp, null);
		this.TDDRelMain.add(this.TDDRelUni, null);
		this.TDDRelMain.add(this.TDDRelBi, null);
		this.TDDRelMain.add(this.TDDRelAggreg, null);
		this.TDDRelMain.add(this.TDDRelFeature, null);
		this.TDDRelMain.add(this.TDDRelGeneral, null);
		this.TDDRelMain.add(this.TDDRelClassif, null);
		this.TDDRelMain.add(this.RelUnSelAll, null);
		this.TDDRelMain.add(this.RelSelAll, null);
		this.TDDProcesses.add(this.TDDProcMain, null);
		this.TDDProcMain.add(this.TDDProcDescr, null);
		this.TDDProcMain.add(this.TDDProcEssence, null);
		this.TDDProcMain.add(this.TDDProcScope, null);
		this.TDDProcMain.add(this.TDDProcOrigin, null);
		this.TDDProcMain.add(this.TDDProcActTime, null);
		this.TDDProcMain.add(this.TDDProcBody, null);
		this.TDDProcMain.add(this.ProcUnSelAll, null);
		this.TDDProcMain.add(this.ProcSelAll, null);
		this.TTPDD.add(this.TDDObjects, "Objects");
		this.TTPDD.add(this.TDDProcesses, "Processes");
		this.TTPDD.add(this.TDDLinks, "Links");
		this.TTPDD.add(this.TDDRelations, "Relations");
		this.TDDObjects.add(this.TDDObjMain, null);
		this.TDDObjState.add(this.TDDObjStateInitial, null);
		this.TDDObjState.add(this.TDDObjStateDescr, null);
		this.TDDObjState.add(this.TDDObjStateTime, null);
		this.TDDObjMain.add(this.ObjUnSelAll, null);
		this.TDDObjMain.add(this.ObjSelAll, null);
		this.TDDObjMain.add(this.TDDObjDesc, null);
		this.TDDObjMain.add(this.TDDObjOrigin, null);
		this.TDDObjMain.add(this.TDDObjScope, null);
		this.TDDObjMain.add(this.TDDObjEssence, null);
		this.TDDObjMain.add(this.TDDObjIndex, null);
		this.TDDObjMain.add(this.TDDObjInitVal, null);
		this.TDDObjMain.add(this.TDDObjType, null);
		this.TDDObjMain.add(this.TDDObjStates, null);
		this.TDDObjMain.add(this.TDDObjState, null);
		this.TOPL.add(this.TOPLUntilText, null);
		this.TOPL.add(this.TOPLAll, null);
		this.TOPL.add(this.TOPLUntil, null);
		this.TOPL.add(this.TOPLByName, null);
		this.TOPL.add(this.TOPLNone, null);
		this.TOPL.add(this.TOPLandOPD, null);
		this.TOPDOPL.add(this.TOPD, null);
		this.TOPDOPL.add(this.TOPL, null);
		this.TOPD.add(this.TOPDUntil, null);
		this.TOPD.add(this.TOPDAll, null);
		this.TOPD.add(this.TOPDByName, null);
		this.TOPD.add(this.TOPDNone, null);
		this.TOPD.add(this.TOPDUntilText, null);
		this.TDD.add(this.TTPDD, null);
		this.TGILeft.add(this.TGICurrent, null);
		this.TGILeft.add(this.TGIOper, null);
		this.TGILeft.add(this.TGIClient, null);
		this.TGILeft.add(this.TGIOverview, null);
		this.TGILeft.add(this.TGIUsers, null);
		this.TGILeft.add(this.TGIGoals, null);
		this.TGILeft.add(this.TGIFuture, null);
		this.TGILeft.add(this.TGIBusiness, null);
		this.TGILeft.add(this.TGIInputs, null);
		this.TGILeft.add(this.TGIIssues, null);
		this.TGILeft.add(this.TGIProblems, null);
		this.TGILeft.add(this.TGIHard, null);
		this.TGILeft.add(this.UnSelAll, null);
		this.TGILeft.add(this.SelAll, null);
		this.contentPane.add(this.TNamePanel, null);
		this.TGI.add(this.TGILeft, null);
		this.TNamePanel.add(this.TNameText, null);
		this.TNamePanel.add(this.TNameLable, null);
		this.contentPane.add(this.TButtonsPanel, null);
		this.contentPane.add(this.TTPMain, null);
		this.BG_TOPD.add(this.TOPDAll);
		this.BG_TOPD.add(this.TOPDUntil);
		this.BG_TOPD.add(this.TOPDByName);
		this.BG_TOPD.add(this.TOPDNone);
		this.BG_TOPD.add(this.TOPDUntil);
		this.BG_TOPD.add(this.TOPDAll);
		this.BG_TOPD.add(this.TOPDByName);
		this.BG_TOPD.add(this.TOPDNone);
		this.BG_TOPL.add(this.TOPLAll);
		this.BG_TOPL.add(this.TOPLUntil);
		this.BG_TOPL.add(this.TOPLByName);
		this.BG_TOPL.add(this.TOPLNone);
		this.BG_TOPL.add(this.TOPLandOPD);
		this.BG_TOPL.add(this.TOPLAll);
		this.BG_TOPL.add(this.TOPLUntil);
		this.BG_TOPL.add(this.TOPLByName);
		this.BG_TOPL.add(this.TOPLNone);
		this.BG_TOPL.add(this.TOPLandOPD);
		this.TTPMain.add(this.TGI, "General Info");
		this.TTPMain.add(this.TOPDOPL, "OPD & OPL");
		this.TTPMain.add(this.TDD, "Element Dictionary");
		this.TDDObjState.setVisible(false);
		this.TOPDAll.setSelected(true);
		this.TOPLandOPD.setSelected(true);
		this.setModal(true);
		// insert the name of the editted template in the field
		if (this.filename.compareTo("-1") != 0) {
			StringTokenizer st = new StringTokenizer(this.filename, ".", false);
			this.TNameText.setText(st.nextToken());
		} else {
			this.TNameText.setText("");
		}
	}

	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.dispose();
		}
	}

	/**
	 * Checks if the state properties panel should be visible.
	 * 
	 * @param e
	 *            ActionEvent on TDDObjStates
	 */
	void TDDObjStates_actionPerformed(ActionEvent e) {
		if (this.TDDObjStates.isSelected()) {
			this.TDDObjState.setVisible(true);
		}
		if (this.TDDObjStates.isSelected() == false) {
			this.TDDObjState.setVisible(false);
		}
	}

	/**
	 * If All OPD levels is selected hides the text field.
	 * 
	 * @param e
	 *            ActionEvent on TOPDAll
	 */
	void TOPDAll_actionPerformed(ActionEvent e) {
		if (this.TOPDAll.isSelected()) {
			this.TOPDUntilText.setVisible(false);
		}
	}

	/**
	 * If None OPD levels is selected hides the text field.
	 * 
	 * @param e
	 *            ActionEvent on TOPDNone
	 */
	void TOPDNone_actionPerformed(ActionEvent e) {
		if (this.TOPDNone.isSelected()) {
			this.TOPDUntilText.setVisible(false);
		}
	}

	/**
	 * If OPD until level is selected shows the text field.
	 * 
	 * @param e
	 *            ActionEvent on TOPDUntil
	 */
	void TOPDUntil_actionPerformed(ActionEvent e) {
		if (this.TOPDUntil.isSelected()) {
			this.TOPDUntilText.setVisible(true);
		}
	}

	/**
	 * If OPD by name is selected hides the text field.
	 * 
	 * @param e
	 *            ActionEvent on TOPDByName
	 */
	void TOPDByName_actionPerformed(ActionEvent e) {
		if (this.TOPDByName.isSelected()) {
			this.TOPDUntilText.setVisible(false);
		}
	}

	/**
	 * If OPL until level is selected shows the text field.
	 * 
	 * @param e
	 *            ActionEvent on TOPLUntil
	 */
	void TOPLUntil_actionPerformed(ActionEvent e) {
		if (this.TOPLUntil.isSelected()) {
			this.TOPLUntilText.setVisible(true);
		}
	}

	/**
	 * If All OPL levels is selected hides the text field.
	 * 
	 * @param e
	 *            ActionEvent on TOPLAll
	 */
	void TOPLAll_actionPerformed(ActionEvent e) {
		if (this.TOPLAll.isSelected()) {
			this.TOPLUntilText.setVisible(false);
		}
	}

	/**
	 * If None OPL levels is selected hides the text field.
	 * 
	 * @param e
	 *            ActionEvent on TOPLNone
	 */
	void TOPLNone_actionPerformed(ActionEvent e) {
		if (this.TOPLNone.isSelected()) {
			this.TOPLUntilText.setVisible(false);
		}
	}

	/**
	 * If OPL by name is selected hides the text field.
	 * 
	 * @param e
	 *            ActionEvent on TOPLByName
	 */

	void TOPLByName_actionPerformed(ActionEvent e) {
		if (this.TOPLByName.isSelected()) {
			this.TOPLUntilText.setVisible(false);
		}
	}

	/**
	 * If OPL according to OPD is selected hides the text field.
	 * 
	 * @param e
	 *            ActionEvent on TOPLandOPD
	 */

	void OPLandTOPD_actionPerformed(ActionEvent e) {
		if (this.TOPLandOPD.isSelected()) {
			this.TOPLUntilText.setVisible(false);
		}
	}

	/**
	 * For any change made to the Agent Link, checks if link properties panel
	 * should be visible and sets it respectively.
	 * 
	 * @param e
	 *            ActionEvent on Agent Link
	 */
	void TDDLinkAgent_actionPerformed(ActionEvent e) {
		if (this.TDDLinkAgent.isSelected()) {
			this.TDDLinkProp.setVisible(true);
		}
		if ((this.TDDLinkAgent.isSelected() == false)
				&& (this.TDDLinkInstr.isSelected() == false)
				&& (this.TDDLinkRes.isSelected() == false)
				&& (this.TDDLinkEffect.isSelected() == false)
				&& (this.TDDLinkEvent.isSelected() == false)
				&& (this.TDDLinkExeption.isSelected() == false)
				&& (this.TDDLinkInvoc.isSelected() == false)
				&& (this.TDDLinkInsrEv.isSelected() == false)
				&& (this.TDDLinkCondition.isSelected() == false)) {
			this.TDDLinkProp.setVisible(false);
		}
	}

	/**
	 * For any change made to the Instr Link, checks if link properties panel
	 * should be visible. and sets it respectively
	 * 
	 * @param e
	 *            ActionEvent on Instr Link
	 */
	void TDDLinkInstr_actionPerformed(ActionEvent e) {
		if (this.TDDLinkInstr.isSelected()) {
			this.TDDLinkProp.setVisible(true);
		}
		if ((this.TDDLinkAgent.isSelected() == false)
				&& (this.TDDLinkInstr.isSelected() == false)
				&& (this.TDDLinkRes.isSelected() == false)
				&& (this.TDDLinkEffect.isSelected() == false)
				&& (this.TDDLinkEvent.isSelected() == false)
				&& (this.TDDLinkExeption.isSelected() == false)
				&& (this.TDDLinkInvoc.isSelected() == false)
				&& (this.TDDLinkInsrEv.isSelected() == false)
				&& (this.TDDLinkCondition.isSelected() == false)) {
			this.TDDLinkProp.setVisible(false);
		}
	}

	/**
	 * For any change made to the Res Link, checks if link properties panel
	 * should be visible and sets it respectively.
	 * 
	 * @param e
	 *            ActionEvent on Res Link
	 */
	void TDDLinkRes_actionPerformed(ActionEvent e) {
		if (this.TDDLinkRes.isSelected()) {
			this.TDDLinkProp.setVisible(true);
		}
		if ((this.TDDLinkAgent.isSelected() == false)
				&& (this.TDDLinkInstr.isSelected() == false)
				&& (this.TDDLinkRes.isSelected() == false)
				&& (this.TDDLinkEffect.isSelected() == false)
				&& (this.TDDLinkEvent.isSelected() == false)
				&& (this.TDDLinkExeption.isSelected() == false)
				&& (this.TDDLinkInvoc.isSelected() == false)
				&& (this.TDDLinkInsrEv.isSelected() == false)
				&& (this.TDDLinkCondition.isSelected() == false)) {
			this.TDDLinkProp.setVisible(false);
		}
	}

	/**
	 * For any change made to the Effect Link, checks if link properties panel
	 * should be visible and sets it respectively.
	 * 
	 * @param e
	 *            ActionEvent on Effect Link
	 */
	void TDDLinkEffect_actionPerformed(ActionEvent e) {
		if (this.TDDLinkEffect.isSelected()) {
			this.TDDLinkProp.setVisible(true);
		}
		if ((this.TDDLinkAgent.isSelected() == false)
				&& (this.TDDLinkInstr.isSelected() == false)
				&& (this.TDDLinkRes.isSelected() == false)
				&& (this.TDDLinkEffect.isSelected() == false)
				&& (this.TDDLinkEvent.isSelected() == false)
				&& (this.TDDLinkExeption.isSelected() == false)
				&& (this.TDDLinkInvoc.isSelected() == false)
				&& (this.TDDLinkInsrEv.isSelected() == false)
				&& (this.TDDLinkCondition.isSelected() == false)) {
			this.TDDLinkProp.setVisible(false);
		}
	}

	/**
	 * For any change made to the Event Link, checks if link properties panel
	 * should be visible and sets it respectively.
	 * 
	 * @param e
	 *            ActionEvent on Event Link
	 */
	void TDDLinkEvent_actionPerformed(ActionEvent e) {
		if (this.TDDLinkEvent.isSelected()) {
			this.TDDLinkProp.setVisible(true);
		}
		if ((this.TDDLinkAgent.isSelected() == false)
				&& (this.TDDLinkInstr.isSelected() == false)
				&& (this.TDDLinkRes.isSelected() == false)
				&& (this.TDDLinkEffect.isSelected() == false)
				&& (this.TDDLinkEvent.isSelected() == false)
				&& (this.TDDLinkExeption.isSelected() == false)
				&& (this.TDDLinkInvoc.isSelected() == false)
				&& (this.TDDLinkInsrEv.isSelected() == false)
				&& (this.TDDLinkCondition.isSelected() == false)) {
			this.TDDLinkProp.setVisible(false);
		}
	}

	/**
	 * For any change made to the Exeption Link, checks if link properties panel
	 * should be visible and sets it respectively.
	 * 
	 * @param e
	 *            ActionEvent on Exeption Link
	 */
	void TDDLinkExeption_actionPerformed(ActionEvent e) {
		if (this.TDDLinkExeption.isSelected()) {
			this.TDDLinkProp.setVisible(true);
		}
		if ((this.TDDLinkAgent.isSelected() == false)
				&& (this.TDDLinkInstr.isSelected() == false)
				&& (this.TDDLinkRes.isSelected() == false)
				&& (this.TDDLinkEffect.isSelected() == false)
				&& (this.TDDLinkEvent.isSelected() == false)
				&& (this.TDDLinkExeption.isSelected() == false)
				&& (this.TDDLinkInvoc.isSelected() == false)
				&& (this.TDDLinkInsrEv.isSelected() == false)
				&& (this.TDDLinkCondition.isSelected() == false)) {
			this.TDDLinkProp.setVisible(false);
		}
	}

	/**
	 * For any change made to the Invoc Link, checks if link properties panel
	 * should be visible and sets it respectively.
	 * 
	 * @param e
	 *            ActionEvent on Invoc Link
	 */
	void TDDLinkInvoc_actionPerformed(ActionEvent e) {
		if (this.TDDLinkInvoc.isSelected()) {
			this.TDDLinkProp.setVisible(true);
		}
		if ((this.TDDLinkAgent.isSelected() == false)
				&& (this.TDDLinkInstr.isSelected() == false)
				&& (this.TDDLinkRes.isSelected() == false)
				&& (this.TDDLinkEffect.isSelected() == false)
				&& (this.TDDLinkEvent.isSelected() == false)
				&& (this.TDDLinkExeption.isSelected() == false)
				&& (this.TDDLinkInvoc.isSelected() == false)
				&& (this.TDDLinkInsrEv.isSelected() == false)
				&& (this.TDDLinkCondition.isSelected() == false)) {
			this.TDDLinkProp.setVisible(false);
		}
	}

	/**
	 * For any change made to the InsrEv Link, checks if link properties panel
	 * should be visible and sets it respectively.
	 * 
	 * @param e
	 *            ActionEvent on InsrEv Link
	 */
	void TDDLinkInsrEv_actionPerformed(ActionEvent e) {
		if (this.TDDLinkInsrEv.isSelected()) {
			this.TDDLinkProp.setVisible(true);
		}
		if ((this.TDDLinkAgent.isSelected() == false)
				&& (this.TDDLinkInstr.isSelected() == false)
				&& (this.TDDLinkRes.isSelected() == false)
				&& (this.TDDLinkEffect.isSelected() == false)
				&& (this.TDDLinkEvent.isSelected() == false)
				&& (this.TDDLinkExeption.isSelected() == false)
				&& (this.TDDLinkInvoc.isSelected() == false)
				&& (this.TDDLinkInsrEv.isSelected() == false)
				&& (this.TDDLinkCondition.isSelected() == false)) {
			this.TDDLinkProp.setVisible(false);
		}
	}

	/**
	 * For any change made to the Condition Link, checks if link properties
	 * panel should be visible and sets it respectively.
	 * 
	 * @param e
	 *            ActionEvent on Condition Link
	 */
	void TDDLinkCondition_actionPerformed(ActionEvent e) {
		if (this.TDDLinkCondition.isSelected()) {
			this.TDDLinkProp.setVisible(true);
		}
		if ((this.TDDLinkAgent.isSelected() == false)
				&& (this.TDDLinkInstr.isSelected() == false)
				&& (this.TDDLinkRes.isSelected() == false)
				&& (this.TDDLinkEffect.isSelected() == false)
				&& (this.TDDLinkEvent.isSelected() == false)
				&& (this.TDDLinkExeption.isSelected() == false)
				&& (this.TDDLinkInvoc.isSelected() == false)
				&& (this.TDDLinkInsrEv.isSelected() == false)
				&& (this.TDDLinkCondition.isSelected() == false)) {
			this.TDDLinkProp.setVisible(false);
		}
	}

	/**
	 * When Save button is pressed, checks the input and saves as template.
	 * 
	 * @param e
	 *            ActionEvent on TSaveAs button
	 */
	void TSaveAs_actionPerformed(ActionEvent e) {
		// check if the level is a valid integer
		try {
			if (this.TOPDUntil.isSelected()) {
				int opd_text = Integer.parseInt(this.TOPDUntilText.getText());
				if (opd_text < 1) {
					opd_text = Integer.parseInt("jdkf");
				}
			}
			if (this.TOPLUntil.isSelected()) {
				int opl_text = Integer.parseInt(this.TOPLUntilText.getText());
				if (opl_text < 1) {
					opl_text = Integer.parseInt("jdkf");
				}
			}
			try {
				String filename = this.TNameText.getText();
				// checks if a name for the template was inserted
				if (filename.compareTo("") == 0) {
					JOptionPane.showMessageDialog(this,
							"Please insert a name for the template.", "ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
				// if there is a name
				else {
					// checks if there already is a template with that name
					File myDir = new File("Templates");
					int flag = 0; // 0 - no such file yet
					int i = 0;
					if (myDir.exists()) {
						File[] contents = myDir.listFiles();
						while (i < contents.length) {
							if (contents[i].getName().compareTo(filename) == 0) {
								flag = 1;
							}
							i++;
						}
					}
					// if there is a template with this name
					if (flag == 1) {
						int retval = JOptionPane
								.showConfirmDialog(
										this,
										"A template with this name already exists. Do you want to overwrite it?",
										"Overwrite Template",
										JOptionPane.YES_NO_OPTION);
						// overwriting
						if (retval == JOptionPane.YES_OPTION) {
							Document doc = this.DocCreate();// create a document
							doc.CreateTemplate(this.mySys, filename);// save to
							// template file
							this.dad.dispose();// close previous screen
							this.dispose();// close this screen
						}
					}
					// if there is no such file yet
					else {
						Document doc = this.DocCreate();// create a document
						doc.CreateTemplate(this.mySys, filename);// save to template
						// file
						this.dad.dispose();// close the previous screen
						this.dispose();// clise this screen
					}
				}
			} catch (IOException er) {
				System.out.println("Problem saving the template");
			}
		} catch (NumberFormatException ne) {
			// illigal level number
			JOptionPane
					.showMessageDialog(
							this,
							"Illegal input: level is not valid. Please insert an integer.",
							"ERROR", JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * Takes the selected fields from the screen, creates a new document and
	 * marks the user selection in the document.
	 * 
	 * @return doc of type Document, with the relevent choices
	 */

	Document DocCreate() {
		Document doc = new Document();
		boolean[] obj_array = new boolean[11];
		boolean[] proc_array = new boolean[6];
		boolean[] rel_array = new boolean[6];
		boolean[] link_array = new boolean[12];
		boolean[] opd_array = new boolean[7];
		boolean[] gi_array = new boolean[12];

		obj_array[0] = this.TDDObjType.isSelected();
		obj_array[1] = this.TDDObjDesc.isSelected();
		obj_array[2] = this.TDDObjInitVal.isSelected();
		obj_array[3] = this.TDDObjEssence.isSelected();
		obj_array[4] = this.TDDObjIndex.isSelected();
		obj_array[5] = this.TDDObjScope.isSelected();
		obj_array[6] = this.TDDObjOrigin.isSelected();
		obj_array[7] = this.TDDObjStates.isSelected();
		obj_array[8] = this.TDDObjStateDescr.isSelected();
		obj_array[9] = this.TDDObjStateInitial.isSelected();
		obj_array[10] = this.TDDObjStateTime.isSelected();

		proc_array[0] = this.TDDProcDescr.isSelected();
		proc_array[1] = this.TDDProcEssence.isSelected();
		proc_array[2] = this.TDDProcOrigin.isSelected();
		proc_array[3] = this.TDDProcScope.isSelected();
		proc_array[4] = this.TDDProcBody.isSelected();
		proc_array[5] = this.TDDProcActTime.isSelected();

		rel_array[0] = this.TDDRelAggreg.isSelected();
		rel_array[1] = this.TDDRelClassif.isSelected();
		rel_array[2] = this.TDDRelFeature.isSelected();
		rel_array[3] = this.TDDRelGeneral.isSelected();
		rel_array[4] = this.TDDRelBi.isSelected();
		rel_array[5] = this.TDDRelUni.isSelected();

		link_array[0] = this.TDDLinkAgent.isSelected();
		link_array[1] = this.TDDLinkCondition.isSelected();
		link_array[2] = this.TDDLinkEffect.isSelected();
		link_array[3] = this.TDDLinkEvent.isSelected();
		link_array[4] = this.TDDLinkExeption.isSelected();
		link_array[5] = this.TDDLinkInsrEv.isSelected();
		link_array[6] = this.TDDLinkInvoc.isSelected();
		link_array[7] = this.TDDLinkRes.isSelected();
		link_array[8] = this.TDDLinkInstr.isSelected();
		link_array[9] = this.TDDLinkPropCond.isSelected();
		link_array[10] = this.TDDLinkPropPath.isSelected();
		link_array[11] = this.TDDLinkPropReacTime.isSelected();

		opd_array[0] = this.TOPDAll.isSelected();
		opd_array[1] = this.TOPDByName.isSelected();
		opd_array[2] = this.TOPDNone.isSelected();
		opd_array[3] = this.TOPLAll.isSelected();
		opd_array[4] = this.TOPLByName.isSelected();
		opd_array[5] = this.TOPLNone.isSelected();
		opd_array[6] = this.TOPLandOPD.isSelected();

		gi_array[0] = this.TGIClient.isSelected();
		gi_array[1] = this.TGIOverview.isSelected();
		gi_array[2] = this.TGICurrent.isSelected();
		gi_array[3] = this.TGIGoals.isSelected();
		gi_array[4] = this.TGIBusiness.isSelected();
		gi_array[5] = this.TGIFuture.isSelected();
		gi_array[6] = this.TGIHard.isSelected();
		gi_array[7] = this.TGIInputs.isSelected();
		gi_array[8] = this.TGIIssues.isSelected();
		gi_array[9] = this.TGIProblems.isSelected();
		gi_array[10] = this.TGIUsers.isSelected();
		gi_array[11] = this.TGIOper.isSelected();

		// untill OPD level
		String OPDUntilT, OPLUntilT;
		if (this.TOPDUntil.isSelected()) {
			OPDUntilT = this.TOPDUntilText.getText();
		} else {
			OPDUntilT = "-1";
		}
		// untill OPL level
		if (this.TOPLUntil.isSelected()) {
			OPLUntilT = this.TOPLUntilText.getText();
		} else {
			OPLUntilT = "-1";
		}
		// insert arrays to document
		doc.DocInfo.Data.Proc.ProcInit(proc_array);
		doc.DocInfo.Data.Obj.ObjInit(obj_array);
		doc.DocInfo.Data.Link.LinkInit(link_array);
		doc.DocInfo.Data.Rel.RelInit(rel_array);
		doc.DocInfo.GI.GIInit(gi_array);
		doc.DocInfo.opdopl.OPDOPLInit(opd_array, OPDUntilT, OPLUntilT);

		return doc;
	}

	/**
	 * Cancels editing template.
	 * 
	 * @param e
	 *            ActionEvent on Cancel button
	 */

	void TCancel_actionPerformed(ActionEvent e) {
		this.dispose();
	}

	/**
	 * Shows the choices selected in the document d on the screen
	 * 
	 * @param d
	 *            Object of type Document
	 */

	void TempToScreen(Document d) {

		Document doc = d;
		// for fields of object, check if the field is selected in document -
		// if selected -> set selected on screen, else unselect

		this.TDDObjType.setSelected(doc.DocInfo.Data.Obj.getType());
		this.TDDObjDesc.setSelected(doc.DocInfo.Data.Obj.getDesc());
		this.TDDObjInitVal.setSelected(doc.DocInfo.Data.Obj.getInValue());
		this.TDDObjEssence.setSelected(doc.DocInfo.Data.Obj.getEssence());
		this.TDDObjIndex.setSelected(doc.DocInfo.Data.Obj.getIndex());
		this.TDDObjScope.setSelected(doc.DocInfo.Data.Obj.getScope());
		this.TDDObjOrigin.setSelected(doc.DocInfo.Data.Obj.getOrigin());
		this.TDDObjStates.setSelected(doc.DocInfo.Data.Obj.getStates());
		this.TDDObjStateDescr.setSelected(doc.DocInfo.Data.Obj.getStateDesc());
		this.TDDObjStateInitial.setSelected(doc.DocInfo.Data.Obj.getStateInitial());
		this.TDDObjStateTime.setSelected(doc.DocInfo.Data.Obj.getStateTime());
		// in case the states field for objects is selected ->
		// set visible the pannel with state properties
		if (this.TDDObjStates.isSelected()) {
			this.TDDObjState.setVisible(true);
		} else {
			this.TDDObjState.setVisible(false);
		}
		// for fields of process, check if the field is selected in document -
		// if selected -> set selected on screen, else unselect
		this.TDDProcDescr.setSelected(doc.DocInfo.Data.Proc.getDesc());
		this.TDDProcEssence.setSelected(doc.DocInfo.Data.Proc.getEssence());
		this.TDDProcOrigin.setSelected(doc.DocInfo.Data.Proc.getOrigin());
		this.TDDProcScope.setSelected(doc.DocInfo.Data.Proc.getScope());
		this.TDDProcBody.setSelected(doc.DocInfo.Data.Proc.getBody());
		this.TDDProcActTime.setSelected(doc.DocInfo.Data.Proc.getActTime());

		// for fields of relations, check if the field is selected in document -
		// if selected -> set selected on screen, else unselect
		this.TDDRelAggreg.setSelected(doc.DocInfo.Data.Rel.getAgPar());
		this.TDDRelClassif.setSelected(doc.DocInfo.Data.Rel.getClassInst());
		this.TDDRelFeature.setSelected(doc.DocInfo.Data.Rel.getFeChar());
		this.TDDRelGeneral.setSelected(doc.DocInfo.Data.Rel.getGenSpec());
		this.TDDRelBi.setSelected(doc.DocInfo.Data.Rel.getBiDir());
		this.TDDRelUni.setSelected(doc.DocInfo.Data.Rel.getUniDir());

		// for fields of links, check if the field is selected in document -
		// if selected -> set selected on screen, else unselect
		this.TDDLinkAgent.setSelected(doc.DocInfo.Data.Link.getAgent());
		this.TDDLinkCondition.setSelected(doc.DocInfo.Data.Link.getCondition());
		this.TDDLinkEffect.setSelected(doc.DocInfo.Data.Link.getEffect());
		this.TDDLinkEvent.setSelected(doc.DocInfo.Data.Link.getEvent());
		this.TDDLinkExeption.setSelected(doc.DocInfo.Data.Link.getException());
		this.TDDLinkInsrEv.setSelected(doc.DocInfo.Data.Link.getInstEvent());
		this.TDDLinkInvoc.setSelected(doc.DocInfo.Data.Link.getInvocation());
		this.TDDLinkRes.setSelected(doc.DocInfo.Data.Link.getResCons());
		this.TDDLinkInstr.setSelected(doc.DocInfo.Data.Link.getInstrument());
		this.TDDLinkPropCond.setSelected(doc.DocInfo.Data.Link.getCond());
		this.TDDLinkPropPath.setSelected(doc.DocInfo.Data.Link.getPath());
		this.TDDLinkPropReacTime.setSelected(doc.DocInfo.Data.Link.getReactTime());
		// if at least one of the link types is selected - >
		// set the panel with link properties visible, else hide it
		if ((this.TDDLinkAgent.isSelected() == false)
				&& (this.TDDLinkInstr.isSelected() == false)
				&& (this.TDDLinkRes.isSelected() == false)
				&& (this.TDDLinkEffect.isSelected() == false)
				&& (this.TDDLinkEvent.isSelected() == false)
				&& (this.TDDLinkExeption.isSelected() == false)
				&& (this.TDDLinkInvoc.isSelected() == false)
				&& (this.TDDLinkInsrEv.isSelected() == false)
				&& (this.TDDLinkCondition.isSelected() == false)) {
			this.TDDLinkProp.setVisible(false);
		} else {
			this.TDDLinkProp.setVisible(true);
		}

		// for fields of OPD and OPL, check if the field is selected in document
		// -
		// if selected -> set selected on screen, else unselect
		this.TOPDAll.setSelected(doc.DocInfo.opdopl.getOPDAll());
		this.TOPDByName.setSelected(doc.DocInfo.opdopl.getOPDByName());
		this.TOPDNone.setSelected(doc.DocInfo.opdopl.getOPDNone());
		this.TOPLAll.setSelected(doc.DocInfo.opdopl.getOPLAll());
		this.TOPLByName.setSelected(doc.DocInfo.opdopl.getOPLByName());
		this.TOPLNone.setSelected(doc.DocInfo.opdopl.getOPLNone());
		this.TOPLandOPD.setSelected(doc.DocInfo.opdopl.getOPLAccording());

		// for fields of General Info, check if the field is selected in
		// document -
		// if selected -> set selected on screen, else unselect
		this.TGIClient.setSelected(doc.DocInfo.GI.getClient());
		this.TGIOverview.setSelected(doc.DocInfo.GI.getOverview());
		this.TGICurrent.setSelected(doc.DocInfo.GI.getCurrent());
		this.TGIGoals.setSelected(doc.DocInfo.GI.getGoals());
		this.TGIBusiness.setSelected(doc.DocInfo.GI.getBusiness());
		this.TGIFuture.setSelected(doc.DocInfo.GI.getFuture());
		this.TGIHard.setSelected(doc.DocInfo.GI.getHard());
		this.TGIInputs.setSelected(doc.DocInfo.GI.getInputs());
		this.TGIIssues.setSelected(doc.DocInfo.GI.getIssues());
		this.TGIProblems.setSelected(doc.DocInfo.GI.getProblems());
		this.TGIUsers.setSelected(doc.DocInfo.GI.getUsers());
		this.TGIOper.setSelected(doc.DocInfo.GI.getOper());
		// if OPD untill was not selected in the document ->
		// hide text field of the level, unselect the field
		if (doc.DocInfo.opdopl.getOPDUntil().compareTo("-1") == 0) {
			this.TOPDUntil.setSelected(false);
			this.TOPDUntilText.setVisible(false);
		}
		// else, if OPD untill is selected ->
		// select the relevant field, show the text field for the level,
		// and put the level from the document in the text field
		else {
			this.TOPDUntil.setSelected(true);
			this.TOPDUntilText.setVisible(true);
			this.TOPDUntilText.setText(doc.DocInfo.opdopl.getOPDUntil());
		}
		// if OPL untill was not selected in the document ->
		// hide text field of the level, unselect the field
		if (doc.DocInfo.opdopl.getOPLUntil().compareTo("-1") == 0) {
			this.TOPLUntil.setSelected(false);
			this.TOPLUntilText.setVisible(false);
		}
		// else, if OPL untill is selected ->
		// select the relevant field, show the text field for the level,
		// and put the level from the document in the text field
		else {
			this.TOPLUntil.setSelected(true);
			this.TOPLUntilText.setVisible(true);
			this.TOPLUntilText.setText(doc.DocInfo.opdopl.getOPLUntil());
		}

	}

	/**
	 * When the "Select All" button in the General Info panel is pressed marks
	 * all the related fields as selected.
	 * 
	 * @param e
	 *            ActionEvent on select all button for genInfo
	 */
	void SelAll_actionPerformed(ActionEvent e) {
		this.TGIBusiness.setSelected(true);
		this.TGIClient.setSelected(true);
		this.TGICurrent.setSelected(true);
		this.TGIFuture.setSelected(true);
		this.TGIGoals.setSelected(true);
		this.TGIHard.setSelected(true);
		this.TGIInputs.setSelected(true);
		this.TGIIssues.setSelected(true);
		this.TGIOper.setSelected(true);
		this.TGIOverview.setSelected(true);
		this.TGIProblems.setSelected(true);
		this.TGIUsers.setSelected(true);
	}

	/**
	 * When the "Unselect All" button in the General Info panel is pressed marks
	 * all the related fields as unselected.
	 * 
	 * @param e
	 *            ActionEvent on unselect all button for genInfo
	 */
	void UnSelAll_actionPerformed(ActionEvent e) {
		this.TGIBusiness.setSelected(false);
		this.TGIClient.setSelected(false);
		this.TGICurrent.setSelected(false);
		this.TGIFuture.setSelected(false);
		this.TGIGoals.setSelected(false);
		this.TGIHard.setSelected(false);
		this.TGIInputs.setSelected(false);
		this.TGIIssues.setSelected(false);
		this.TGIOper.setSelected(false);
		this.TGIOverview.setSelected(false);
		this.TGIProblems.setSelected(false);
		this.TGIUsers.setSelected(false);
	}

	/**
	 * When the "Select All" button in the Objects panel is pressed marks all
	 * the object related fields as selected.
	 * 
	 * @param e
	 *            ActionEvent on select all button for objects
	 */
	void ObjSelAll_actionPerformed(ActionEvent e) {
		this.TDDObjDesc.setSelected(true);
		this.TDDObjEssence.setSelected(true);
		this.TDDObjIndex.setSelected(true);
		this.TDDObjInitVal.setSelected(true);
		this.TDDObjOrigin.setSelected(true);
		this.TDDObjScope.setSelected(true);
		this.TDDObjStates.setSelected(true);
		this.TDDObjStateDescr.setSelected(true);
		this.TDDObjStateInitial.setSelected(true);
		this.TDDObjStateTime.setSelected(true);
		this.TDDObjType.setSelected(true);
		this.TDDObjState.setVisible(true);
	}

	/**
	 * When the "Unselect All" button in the Objects panel is pressed marks all
	 * the object related fields as unselected.
	 * 
	 * @param e
	 *            ActionEvent on unselect all button for objects
	 */
	void ObjUnSelAll_actionPerformed(ActionEvent e) {
		this.TDDObjDesc.setSelected(false);
		this.TDDObjEssence.setSelected(false);
		this.TDDObjIndex.setSelected(false);
		this.TDDObjInitVal.setSelected(false);
		this.TDDObjOrigin.setSelected(false);
		this.TDDObjScope.setSelected(false);
		this.TDDObjStates.setSelected(false);
		this.TDDObjStateDescr.setSelected(false);
		this.TDDObjStateInitial.setSelected(false);
		this.TDDObjStateTime.setSelected(false);
		this.TDDObjType.setSelected(false);
		this.TDDObjState.setVisible(false);
	}

	/**
	 * When the "Unselect All" button in the Processes panel is pressed marks
	 * all the process related fields as unselected.
	 * 
	 * @param e
	 *            ActionEvent on unselect all button for processes
	 */
	void ProcUnSelAll_actionPerformed(ActionEvent e) {
		this.TDDProcActTime.setSelected(false);
		this.TDDProcBody.setSelected(false);
		this.TDDProcDescr.setSelected(false);
		this.TDDProcEssence.setSelected(false);
		this.TDDProcOrigin.setSelected(false);
		this.TDDProcScope.setSelected(false);
	}

	/**
	 * When the "Select All" button in the Processes panel is pressed marks all
	 * the process related fields as selected.
	 * 
	 * @param e
	 *            ActionEvent on select all button for processes
	 */
	void ProcSelAll_actionPerformed(ActionEvent e) {
		this.TDDProcActTime.setSelected(true);
		this.TDDProcBody.setSelected(true);
		this.TDDProcDescr.setSelected(true);
		this.TDDProcEssence.setSelected(true);
		this.TDDProcOrigin.setSelected(true);
		this.TDDProcScope.setSelected(true);
	}

	/**
	 * When the "Unselect All" button in the Links panel is pressed marks all
	 * the link related fields as unselected.
	 * 
	 * @param e
	 *            ActionEvent on unselect all button for links
	 */
	void LinkUnSelAll_actionPerformed(ActionEvent e) {
		this.TDDLinkAgent.setSelected(false);
		this.TDDLinkCondition.setSelected(false);
		this.TDDLinkEffect.setSelected(false);
		this.TDDLinkEvent.setSelected(false);
		this.TDDLinkExeption.setSelected(false);
		this.TDDLinkInstr.setSelected(false);
		this.TDDLinkInsrEv.setSelected(false);
		this.TDDLinkInvoc.setSelected(false);
		this.TDDLinkPropCond.setSelected(false);
		this.TDDLinkPropPath.setSelected(false);
		this.TDDLinkPropReacTime.setSelected(false);
		this.TDDLinkRes.setSelected(false);
		this.TDDLinkProp.setVisible(false);
	}

	/**
	 * When the "Select All" button in the Links panel is pressed marks all the
	 * link related fields as selected.
	 * 
	 * @param e
	 *            ActionEvent on select all button for links
	 */
	void LinkSelAll_actionPerformed(ActionEvent e) {
		this.TDDLinkAgent.setSelected(true);
		this.TDDLinkCondition.setSelected(true);
		this.TDDLinkEffect.setSelected(true);
		this.TDDLinkEvent.setSelected(true);
		this.TDDLinkExeption.setSelected(true);
		this.TDDLinkInstr.setSelected(true);
		this.TDDLinkInsrEv.setSelected(true);
		this.TDDLinkInvoc.setSelected(true);
		this.TDDLinkPropCond.setSelected(true);
		this.TDDLinkPropPath.setSelected(true);
		this.TDDLinkPropReacTime.setSelected(true);
		this.TDDLinkRes.setSelected(true);
		this.TDDLinkProp.setVisible(true);

	}

	/**
	 * When the "Select All" button in the Relations panel is pressed marks all
	 * the relation related fields as selected.
	 * 
	 * @param e
	 *            ActionEvent on select all button for relations
	 */
	void RelSelAll_actionPerformed(ActionEvent e) {
		this.TDDRelAggreg.setSelected(true);
		this.TDDRelBi.setSelected(true);
		this.TDDRelClassif.setSelected(true);
		this.TDDRelFeature.setSelected(true);
		this.TDDRelGeneral.setSelected(true);
		this.TDDRelUni.setSelected(true);
	}

	/**
	 * When the "Unselect All" button in the Relations panel is pressed marks
	 * all the relation related fields as unselected.
	 * 
	 * @param e
	 *            ActionEvent on unselect all button for relations
	 */
	void RelUnSelAll_actionPerformed(ActionEvent e) {
		this.TDDRelAggreg.setSelected(false);
		this.TDDRelBi.setSelected(false);
		this.TDDRelClassif.setSelected(false);
		this.TDDRelFeature.setSelected(false);
		this.TDDRelGeneral.setSelected(false);
		this.TDDRelUni.setSelected(false);
	}// //////////end of TempToScreen

}
