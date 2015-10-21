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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import exportedAPI.opcatAPI.ISystem;
import extensionTools.Documents.Doc.Document;

/**
 * DocGen Class extends the standard JDialog, allows user to select
 * the contents of the document he is interested in.
 * All the choices of the user regarding the fields of the General Info, OPD,
 * OPL and Elements Dictionary are done in this screen.
 * Also allows opening one of the templates avaliable or saving the selection
 * as a template.
 * Parallel to the TempEdit class (for templates).
 * @author		Olga Tavrovsky
 * @author		Anna Levit
 */
public class DocGen extends JDialog {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/**
	 * 
	 */
	 
ISystem mySys;
  File myDir = new File("Templates");
  JPanel contentPane;
  JTabbedPane TPMain = new JTabbedPane();
  JPanel GI = new JPanel();
  JPanel OPDOPL = new JPanel();
  JPanel DD = new JPanel();
  TitledBorder titledBorder1;
  JTabbedPane TPDD = new JTabbedPane();
  JPanel DDObjects = new JPanel();
  JPanel DDProcesses = new JPanel();
  JPanel DDRelations = new JPanel();
  JPanel DDLinks = new JPanel();
  JPanel ButtonsPanel = new JPanel();
  JButton Cancel = new JButton();
  JButton Generate = new JButton();
  JPanel GILeft = new JPanel();
  JCheckBox GIClient = new JCheckBox();
  JCheckBox GIOverview = new JCheckBox();
  JCheckBox GIFuture = new JCheckBox();
  JCheckBox GIUsers = new JCheckBox();
  JCheckBox GIGoals = new JCheckBox();
  JCheckBox GICurrent = new JCheckBox();
  JPanel OPL = new JPanel();
  JPanel DDObjMain = new JPanel();
  JCheckBox DDObjOrigin = new JCheckBox();
  JCheckBox DDObjDesc = new JCheckBox();
  JCheckBox DDObjType = new JCheckBox();
  JCheckBox DDObjStates = new JCheckBox();
  JCheckBox DDObjScope = new JCheckBox();
  JCheckBox DDObjEssence = new JCheckBox();
  JPanel OPD = new JPanel();
  JRadioButton OPDByName = new JRadioButton();
  JRadioButton OPDUntil = new JRadioButton();
  JRadioButton OPDAll = new JRadioButton();
  JRadioButton OPDNone = new JRadioButton();
  ButtonGroup BG_OPD = new ButtonGroup();
  ButtonGroup BG_OPL = new ButtonGroup();
  JCheckBox DDObjIndex = new JCheckBox();
  JCheckBox DDObjInitVal = new JCheckBox();
  JPanel DDObjState = new JPanel();
  JCheckBox DDObjStateDescr = new JCheckBox();
  JCheckBox DDObjStateInitial = new JCheckBox();
  Border border1;
  TitledBorder titledBorder2;
  Border border2;
  TitledBorder titledBorder3;
  Border border3;
  TitledBorder titledBorder4;
  Border border4;
  TitledBorder titledBorder5;
  JPanel DDProcMain = new JPanel();
  Border border5;
  TitledBorder titledBorder6;
  JCheckBox DDProcEssence = new JCheckBox();
  JCheckBox DDProcOrigin = new JCheckBox();
  JCheckBox DDProcDescr = new JCheckBox();
  JCheckBox DDProcActTime = new JCheckBox();
  JCheckBox DDProcScope = new JCheckBox();
  JCheckBox DDProcBody = new JCheckBox();
  Border border6;
  TitledBorder titledBorder7;
  JPanel DDRelMain = new JPanel();
  Border border7;
  TitledBorder titledBorder8;
  JCheckBox DDRelAggreg = new JCheckBox();
  JCheckBox DDRelFeature = new JCheckBox();
  JCheckBox DDRelGeneral = new JCheckBox();
  JCheckBox DDRelClassif = new JCheckBox();
  JCheckBox DDRelUni = new JCheckBox();
  JCheckBox DDRelBi = new JCheckBox();
  Border border8;
  TitledBorder titledBorder9;
  JPanel DDLinkMain = new JPanel();
  JCheckBox DDLinkInvoc = new JCheckBox();
  JCheckBox DDLinkInstrEv = new JCheckBox();
  JCheckBox DDLinkCondition = new JCheckBox();
  JCheckBox DDLinkInstr = new JCheckBox();
  JCheckBox DDLinkEvent = new JCheckBox();
  JCheckBox DDLinkRes = new JCheckBox();
  JCheckBox DDLinkEffect = new JCheckBox();
  JCheckBox DDLinkAgent = new JCheckBox();
  JCheckBox DDLinkException = new JCheckBox();
  Border border9;
  TitledBorder titledBorder10;
  JCheckBox GIIssues = new JCheckBox();
  JCheckBox GIInputs = new JCheckBox();
  JCheckBox GIHard = new JCheckBox();
  JCheckBox GIProblems = new JCheckBox();
  JCheckBox GIBusiness = new JCheckBox();
  JCheckBox GIOper = new JCheckBox();
  Border border10;
  TitledBorder titledBorder11;
  JTextField OPLUntilText = new JTextField();
  JTextField OPDUntilText = new JTextField();
  JRadioButton OPLUntil = new JRadioButton();
  JRadioButton OPLAll = new JRadioButton();
  JRadioButton OPLNone = new JRadioButton();
  JRadioButton OPLByName = new JRadioButton();
  JRadioButton OPLandOPD = new JRadioButton();
  JCheckBox DDLinkPropCond = new JCheckBox();
  JPanel DDLinkProp = new JPanel();
  JCheckBox DDLinkPropPath = new JCheckBox();
  JCheckBox DDLinkPropReacTime = new JCheckBox();
  JCheckBox DDObjStateTime = new JCheckBox();
  JPanel DFromTempPanel = new JPanel();
  JRadioButton DTempRadio = new JRadioButton();
  JComboBox DTCombo = new JComboBox();
  JPanel DTSavePanel = new JPanel();
  JButton MakeTemp = new JButton();
  JTextField DTSaveText = new JTextField();
  Border border11;
  TitledBorder titledBorder12;
  Border border12;
  TitledBorder titledBorder13;
  Border border13;
  TitledBorder titledBorder14;
  JButton DTSaveOK = new JButton();
  JButton DTSaveCancel = new JButton();
  Border border14;
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
  JLabel DTSaveL = new JLabel();

/**
 * Construct the frame
 * @param sys of type ISystem
 */
  public DocGen(ISystem sys) {
    super(sys.getMainFrame(), true);
    this.setTitle("Generate a new Document");
    this.mySys = sys;
    this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      this.jbInit();
    }//of try
    catch(Exception e) {
      e.printStackTrace();
    }
    //Esc and Enter Keys active
    KeyListener kListener = new KeyAdapter(){
      public void keyReleased(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
          DocGen.this.Cancel.doClick();
          return;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER){
          DocGen.this.Generate.doClick();
          return;
        }
      }
    };
    this.addKeyListener(kListener);
    //set the screen in the center
    int fX = sys.getMainFrame().getX();
    int fY = sys.getMainFrame().getY();
    int pWidth = sys.getMainFrame().getWidth();
    int pHeight = sys.getMainFrame().getHeight();
    this.setLocation(fX + Math.abs(pWidth/2-this.getWidth()/2), fY + Math.abs(pHeight/2-this.getHeight()/2));
  }

/**
 * Component initialization
 */
  private void jbInit(){
    int counter=0;
    this.DTCombo.addItem("None");
    if (this.myDir.exists()) {
      File[] contents = this.myDir.listFiles();
      while (counter < contents.length) {
        this.DTCombo.addItem(contents[counter].getName());
        counter++;
      }
    }

    this.contentPane = (JPanel) this.getContentPane();
    this.titledBorder1 = new TitledBorder("");
    this.border1 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    this.titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"General Information Fields");
    this.border2 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    this.titledBorder3 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"OPL Levels");
    this.border3 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    this.titledBorder4 = new TitledBorder(this.border3,"State Properties");
    this.border4 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    this.titledBorder5 = new TitledBorder(this.border4,"Object Properties");
    this.border5 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    this.titledBorder6 = new TitledBorder(this.border5,"Process Properties");
    this.border6 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    this.titledBorder7 = new TitledBorder(this.border6,"Process Properties");
    this.border7 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    this.titledBorder8 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Relation Types");
    this.border8 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    this.titledBorder9 = new TitledBorder(this.border8,"Link Properties:");
    this.border9 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    this.titledBorder10 = new TitledBorder(this.border9,"Link Types");
    this.border10 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    this.titledBorder11 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"OPD Levels");
    this.border11 = BorderFactory.createLineBorder(Color.white,1);
    this.titledBorder12 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"Save as template");
    this.border12 = BorderFactory.createEmptyBorder();
    this.titledBorder13 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"Generate Document");
    this.border13 = BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.white,Color.white,new Color(134, 134, 134),new Color(93, 93, 93));
    this.titledBorder14 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"From Template");
    this.border14 = BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134));
    this.contentPane.setLayout(null);
    this.setResizable(false);
    this.setSize(new Dimension(580, 477));
    this.setTitle("New Document Generation");
    this.GI.setLayout(null);
    this.OPDOPL.setLayout(null);
    this.DD.setLayout(null);
    this.TPDD.setBackground(new Color(204, 204, 204));
    this.TPDD.setBounds(new Rectangle(-5, 0, 566, 264));
    this.DDObjects.setLayout(null);
    this.DDProcesses.setLayout(null);
    this.DDRelations.setLayout(null);
    this.DDLinks.setLayout(null);
    this.TPMain.setBackground(new Color(204, 204, 204));
    this.TPMain.setBounds(new Rectangle(5, 51, 563, 284));
    this.ButtonsPanel.setBorder(this.titledBorder13);
    this.ButtonsPanel.setBounds(new Rectangle(4, 339, 567, 52));
    this.ButtonsPanel.setLayout(null);
    this.Cancel.setBounds(new Rectangle(425, 17, 124, 27));
    this.Cancel.setBorder(BorderFactory.createEtchedBorder());
    this.Cancel.setToolTipText("");
    this.Cancel.setText("Cancel");
    this.Cancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.Cancel_actionPerformed(e);
      }
    });
    this.Generate.setBounds(new Rectangle(147, 17, 124, 27));
    this.Generate.setBorder(BorderFactory.createEtchedBorder());
    this.Generate.setText("Generate");
    this.Generate.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.Generate_actionPerformed(e);

      }
    });
    this.GILeft.setBorder(this.titledBorder2);
    this.GILeft.setBounds(new Rectangle(12, 17, 535, 232));
    this.GILeft.setLayout(null);
    this.GIClient.setBorder(BorderFactory.createEtchedBorder());
    this.GIClient.setText("Client");
    this.GIClient.setBounds(new Rectangle(9, 30, 90, 25));
    this.GIOverview.setBorder(null);
    this.GIOverview.setText("System Overview");
    this.GIOverview.setBounds(new Rectangle(9, 58, 117, 25));
    this.GIFuture.setBorder(BorderFactory.createEtchedBorder());
    this.GIFuture.setText("Future Goals");
    this.GIFuture.setBounds(new Rectangle(9, 115, 100, 25));
    this.GIUsers.setBorder(BorderFactory.createEtchedBorder());
    this.GIUsers.setText("Possible Users for the System");
    this.GIUsers.setBounds(new Rectangle(9, 171, 199, 25));
    this.GIGoals.setBorder(BorderFactory.createEtchedBorder());
    this.GIGoals.setText("Goals and Objectives of the Project");
    this.GIGoals.setBounds(new Rectangle(9, 143, 219, 25));
    this.GICurrent.setBorder(BorderFactory.createEtchedBorder());
    this.GICurrent.setText("The Current State");
    this.GICurrent.setBounds(new Rectangle(9, 86, 136, 25));
    this.OPL.setBorder(this.titledBorder3);
    this.OPL.setBounds(new Rectangle(298, 21, 209, 200));
    this.OPL.setLayout(null);
    this.DDObjMain.setBorder(this.titledBorder5);
    this.DDObjMain.setBounds(new Rectangle(19, 17, 531, 205));
    this.DDObjMain.setLayout(null);
    this.DDObjOrigin.setText("Origin");
    this.DDObjOrigin.setBounds(new Rectangle(14, 100, 58, 25));
    this.DDObjDesc.setText("Description ");
    this.DDObjDesc.setBounds(new Rectangle(14, 64, 91, 25));
    this.DDObjType.setText("Object Type ");
    this.DDObjType.setBounds(new Rectangle(14, 27, 92, 25));
    this.DDObjStates.setText("States");
    this.DDObjStates.setBounds(new Rectangle(175, 134, 60, 25));
    this.DDObjStates.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.DDObjStates_actionPerformed(e);
      }
    });
    this.DDObjScope.setText("Scope");
    this.DDObjScope.setBounds(new Rectangle(14, 134, 60, 25));
    this.DDObjEssence.setText("Essence");
    this.DDObjEssence.setBounds(new Rectangle(175, 27, 74, 25));
    this.OPD.setBorder(this.titledBorder11);
    this.OPD.setBounds(new Rectangle(48, 21, 209, 200));
    this.OPD.setLayout(null);
    this.OPDByName.setText("By Name");
    this.OPDByName.setBounds(new Rectangle(13, 97, 89, 25));
    this.OPDByName.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.OPDByName_actionPerformed(e);
      }
    });
    this.OPDUntil.setText("Until Level");
    this.OPDUntil.setBounds(new Rectangle(13, 65, 89, 25));
    this.OPDUntil.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.OPDUntil_actionPerformed(e);
      }
    });
    this.OPDAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.OPDAll_actionPerformed(e);
      }
    });
    this.OPDAll.setActionCommand("");
    this.OPDAll.setText("All Levels");
    this.OPDAll.setBounds(new Rectangle(13, 33, 89, 25));
    this.OPDNone.setText("None");
    this.OPDNone.setBounds(new Rectangle(13, 129, 89, 25));
    this.OPDNone.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.OPDNone_actionPerformed(e);
      }
    });
    this.DDObjIndex.setText("Index");
    this.DDObjIndex.setBounds(new Rectangle(175, 64, 54, 25));
    this.DDObjInitVal.setText("Initial Value");
    this.DDObjInitVal.setBounds(new Rectangle(175, 100, 88, 25));
    this.DDObjState.setBorder(this.titledBorder4);
    this.DDObjState.setBounds(new Rectangle(368, 31, 134, 127));
    this.DDObjState.setLayout(null);
    this.DDObjStateDescr.setEnabled(true);
    this.DDObjStateDescr.setText("Description");
    this.DDObjStateDescr.setBounds(new Rectangle(16, 64, 88, 25));
    this.DDObjStateInitial.setEnabled(true);
    this.DDObjStateInitial.setText("Initial/Final");
    this.DDObjStateInitial.setBounds(new Rectangle(16, 34, 88, 25));
    this.DDProcMain.setBorder(this.titledBorder7);
    this.DDProcMain.setBounds(new Rectangle(19, 17, 531, 206));
    this.DDProcMain.setLayout(null);
    this.DDProcEssence.setText("Essence");
    this.DDProcEssence.setBounds(new Rectangle(14, 63, 92, 25));
    this.DDProcOrigin.setText("Origin");
    this.DDProcOrigin.setBounds(new Rectangle(14, 134, 92, 25));
    this.DDProcDescr.setText("Description");
    this.DDProcDescr.setBounds(new Rectangle(14, 27, 92, 25));
    this.DDProcActTime.setText("Activation time");
    this.DDProcActTime.setBounds(new Rectangle(176, 27, 103, 25));
    this.DDProcScope.setText("Scope");
    this.DDProcScope.setBounds(new Rectangle(14, 98, 92, 25));
    this.DDProcBody.setText("Body");
    this.DDProcBody.setBounds(new Rectangle(178, 63, 52, 25));
    this.DDRelMain.setBorder(this.titledBorder8);
    this.DDRelMain.setBounds(new Rectangle(19, 17, 531, 205));
    this.DDRelMain.setLayout(null);
    this.DDRelAggreg.setText("Aggregation-Participation Relations");
    this.DDRelAggreg.setBounds(new Rectangle(14, 27, 235, 25));
    this.DDRelFeature.setText("Featuring-Characterization Relations");
    this.DDRelFeature.setBounds(new Rectangle(14, 63, 235, 25));
    this.DDRelGeneral.setText("Generalization-Specification Relations");
    this.DDRelGeneral.setBounds(new Rectangle(14, 98, 235, 25));
    this.DDRelClassif.setText("Classification-Instantiation Relations");
    this.DDRelClassif.setBounds(new Rectangle(14, 134, 235, 25));
    this.DDRelUni.setText("Uni-Directional General Relations");
    this.DDRelUni.setBounds(new Rectangle(284, 27, 230, 25));
    this.DDRelBi.setText("Bi-directional General Relations");
    this.DDRelBi.setBounds(new Rectangle(284, 63, 230, 25));
    this.DDLinkMain.setBorder(this.titledBorder10);
    this.DDLinkMain.setBounds(new Rectangle(19, 17, 532, 205));
    this.DDLinkMain.setLayout(null);
    this.DDLinkInvoc.setText("Invocation Links");
    this.DDLinkInvoc.setBounds(new Rectangle(194, 59, 125, 25));
    this.DDLinkInvoc.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.DDLinkInvoc_actionPerformed(e);
      }
    });
    this.DDLinkInstrEv.setText("Instrument Event Links");
    this.DDLinkInstrEv.setBounds(new Rectangle(194, 91, 151, 25));
    this.DDLinkInstrEv.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.DDLinkInstrEv_actionPerformed(e);
      }
    });
    this.DDLinkCondition.setText("Condition Links");
    this.DDLinkCondition.setBounds(new Rectangle(194, 123, 132, 25));
    this.DDLinkCondition.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.DDLinkCondition_actionPerformed(e);
      }
    });
    this.DDLinkInstr.setText("Instrument Links");
    this.DDLinkInstr.setBounds(new Rectangle(14, 59, 173, 25));
    this.DDLinkInstr.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.DDLinkInstr_actionPerformed(e);
      }
    });
    this.DDLinkEvent.setText("Event Links");
    this.DDLinkEvent.setBounds(new Rectangle(14, 155, 173, 25));
    this.DDLinkEvent.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.DDLinkEvent_actionPerformed(e);
      }
    });
    this.DDLinkRes.setText("Result/Consumption Links");
    this.DDLinkRes.setBounds(new Rectangle(14, 91, 173, 25));
    this.DDLinkRes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.DDLinkRes_actionPerformed(e);
      }
    });
    this.DDLinkEffect.setText("Effect Links");
    this.DDLinkEffect.setBounds(new Rectangle(14, 123, 173, 25));
    this.DDLinkEffect.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.DDLinkEffect_actionPerformed(e);
      }
    });
    this.DDLinkAgent.setText("Agent Links");
    this.DDLinkAgent.setBounds(new Rectangle(14, 27, 173, 25));
    this.DDLinkAgent.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.DDLinkAgent_actionPerformed(e);
      }
    });
    this.DDLinkException.setText("Exception Links");
    this.DDLinkException.setBounds(new Rectangle(194, 27, 116, 25));
    this.DDLinkException.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.DDLinkException_actionPerformed(e);
      }
    });

    //General info fields
    this.GIIssues.setBorder(BorderFactory.createEtchedBorder());
    this.GIIssues.setText("Open Issues");
    this.GIIssues.setBounds(new Rectangle(249, 115, 96, 25));
    this.GIInputs.setBorder(BorderFactory.createEtchedBorder());
    this.GIInputs.setText("Inputs, Processing functionality and Outputs");
    this.GIInputs.setBounds(new Rectangle(249, 86, 269, 25));
    this.GIHard.setBorder(BorderFactory.createEtchedBorder());
    this.GIHard.setText("Hardware and Software Requirements");
    this.GIHard.setBounds(new Rectangle(249, 171, 236, 25));
    this.GIProblems.setBorder(BorderFactory.createEtchedBorder());
    this.GIProblems.setText("Problems");
    this.GIProblems.setBounds(new Rectangle(249, 143, 79, 25));
    this.GIBusiness.setBorder(BorderFactory.createEtchedBorder());
    this.GIBusiness.setText("Business or Program Constraints");
    this.GIBusiness.setBounds(new Rectangle(249, 58, 210, 25));
    this.GIOper.setBorder(BorderFactory.createEtchedBorder());
    this.GIOper.setText("Operation and Maintenance");
    this.GIOper.setBounds(new Rectangle(249, 30, 176, 25));
    //OPD OPL fields
    this.OPLUntilText.setBounds(new Rectangle(131, 58, 39, 25));
    this.OPLUntilText.setVisible(false);
    this.OPDUntilText.setVisible(false);
    this.OPDUntilText.setBounds(new Rectangle(110, 65, 38, 25));
    this.OPLUntil.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.OPLUntil_actionPerformed(e);
      }
    });
    this.OPLUntil.setBounds(new Rectangle(14, 58, 89, 25));
    this.OPLUntil.setText("Until Level");
    this.OPLAll.setBounds(new Rectangle(14, 27, 89, 25));
    this.OPLAll.setText("All Levels");
    this.OPLAll.setActionCommand("");
    this.OPLAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.OPLAll_actionPerformed(e);
      }
    });
    this.OPLNone.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.OPLNone_actionPerformed(e);
      }
    });
    this.OPLNone.setBounds(new Rectangle(14, 121, 89, 25));
    this.OPLNone.setText("None");
    this.OPLByName.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.OPLByName_actionPerformed(e);
      }
    });
    this.OPLByName.setBounds(new Rectangle(14, 90, 89, 25));
    this.OPLByName.setText("By Name");
    this.OPLandOPD.setText("According to OPD");
    this.OPLandOPD.setBounds(new Rectangle(14, 152, 128, 25));
    this.OPLandOPD.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.OPLandOPD_actionPerformed(e);
      }
    });
    //Link properties fields
    this.DDLinkPropCond.setText("Condition");
    this.DDLinkPropCond.setBounds(new Rectangle(20, 27, 78, 25));
    this.DDLinkProp.setBorder(this.titledBorder9);
    this.DDLinkProp.setBounds(new Rectangle(377, 23, 130, 137));
    this.DDLinkProp.setLayout(null);
    this.DDLinkProp.setVisible(false);
    this.DDLinkPropPath.setText("Path");
    this.DDLinkPropPath.setBounds(new Rectangle(20, 59, 50, 25));
    this.DDLinkPropReacTime.setText("Reaction time");
    this.DDLinkPropReacTime.setBounds(new Rectangle(20, 91, 101, 25));
    this.DDObjStateTime.setText("Activation time");
    this.DDObjStateTime.setBounds(new Rectangle(16, 94, 109, 25));

    this.DFromTempPanel.setBorder(this.titledBorder14);
    this.DFromTempPanel.setBounds(new Rectangle(4, 4, 566, 47));
    this.DFromTempPanel.setLayout(null);
    this.DTempRadio.setText("From Template");
    this.DTempRadio.setBounds(new Rectangle(68, 15, 142, 25));
    this.DTempRadio.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.DTempRadio_actionPerformed(e);
      }
    });
    this.DTCombo.setBounds(new Rectangle(212, 17, 125, 21));
    this.DTCombo.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.DTCombo_actionPerformed(e);
      }
    });
    this.DTSavePanel.setBorder(this.titledBorder12);
    this.DTSavePanel.setBounds(new Rectangle(4, 391, 567, 54));
    this.DTSavePanel.setLayout(null);
    this.MakeTemp.setBounds(new Rectangle(286, 17, 124, 27));
    this.MakeTemp.setBorder(BorderFactory.createEtchedBorder());
    this.MakeTemp.setText("Save as Template");
    this.MakeTemp.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.MakeTemp_actionPerformed(e);
      }
    });
    this.DTSaveText.setBounds(new Rectangle(178, 20, 187, 26));
    this.DTSaveOK.setBounds(new Rectangle(382, 19, 79, 27));
    this.DTSaveOK.setBorder(this.border14);
    this.DTSaveOK.setText("OK");
    this.DTSaveOK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.DTSaveOK_actionPerformed(e);
      }
    });
    this.DTSaveCancel.setBounds(new Rectangle(469, 19, 79, 27));
    this.DTSaveCancel.setBorder(this.border14);
    this.DTSaveCancel.setText("Cancel");
    this.DTSaveCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.DTSaveCancel_actionPerformed(e);
      }
    });
    this.SelAll.setBounds(new Rectangle(293, 197, 114, 27));
    this.SelAll.setText("Select All");
    this.SelAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.SelAll_actionPerformed(e);
      }
    });
    this.UnSelAll.setBounds(new Rectangle(413, 197, 114, 27));
    this.UnSelAll.setText("Unselect All");
    this.UnSelAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.UnSelAll_actionPerformed(e);
      }
    });
    this.ObjSelAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.ObjSelAll_actionPerformed(e);
      }
    });
    this.ObjSelAll.setText("Select All");
    this.ObjSelAll.setBounds(new Rectangle(289, 169, 114, 27));
    this.ObjUnSelAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.ObjUnSelAll_actionPerformed(e);
      }
    });
    this.ObjUnSelAll.setText("Unselect All");
    this.ObjUnSelAll.setBounds(new Rectangle(409, 169, 114, 27));
    this.ProcUnSelAll.setBounds(new Rectangle(407, 169, 114, 27));
    this.ProcUnSelAll.setText("Unselect All");
    this.ProcUnSelAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.ProcUnSelAll_actionPerformed(e);
      }
    });
    this.ProcSelAll.setBounds(new Rectangle(287, 169, 114, 27));
    this.ProcSelAll.setText("Select All");
    this.ProcSelAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.ProcSelAll_actionPerformed(e);
      }
    });
    this.LinkUnSelAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.LinkUnSelAll_actionPerformed(e);
      }
    });
    this.LinkUnSelAll.setText("Unselect All");
    this.LinkUnSelAll.setBounds(new Rectangle(410, 171, 114, 27));
    this.LinkSelAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.LinkSelAll_actionPerformed(e);
      }
    });
    this.LinkSelAll.setText("Select All");
    this.LinkSelAll.setBounds(new Rectangle(290, 171, 114, 27));
    this.RelSelAll.setBounds(new Rectangle(289, 170, 114, 27));
    this.RelSelAll.setText("Select All");
    this.RelSelAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.RelSelAll_actionPerformed(e);
      }
    });
    this.RelUnSelAll.setBounds(new Rectangle(409, 170, 114, 27));
    this.RelUnSelAll.setText("Unselect All");
    this.RelUnSelAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocGen.this.RelUnSelAll_actionPerformed(e);
      }
    });
    this.DTSaveL.setToolTipText("");
    this.DTSaveL.setText("Template name");
    this.DTSaveL.setBounds(new Rectangle(57, 19, 119, 26));
    this.GILeft.add(this.GICurrent, null);
    this.GILeft.add(this.GIOper, null);
    this.GILeft.add(this.GIClient, null);
    this.GILeft.add(this.GIOverview, null);
    this.GILeft.add(this.GIUsers, null);
    this.GILeft.add(this.GIGoals, null);
    this.GILeft.add(this.GIFuture, null);
    this.GILeft.add(this.GIBusiness, null);
    this.GILeft.add(this.GIInputs, null);
    this.GILeft.add(this.GIIssues, null);
    this.GILeft.add(this.GIProblems, null);
    this.GILeft.add(this.GIHard, null);
    this.GILeft.add(this.UnSelAll, null);
    this.GILeft.add(this.SelAll, null);
    this.contentPane.add(this.ButtonsPanel, null);
    this.DDProcMain.add(this.DDProcDescr, null);
    this.DDProcMain.add(this.DDProcEssence, null);
    this.DDProcMain.add(this.DDProcScope, null);
    this.DDProcMain.add(this.DDProcOrigin, null);
    this.DDProcMain.add(this.DDProcActTime, null);
    this.DDProcMain.add(this.DDProcBody, null);
    this.DDProcMain.add(this.ProcUnSelAll, null);
    this.DDProcMain.add(this.ProcSelAll, null);
    this.TPDD.add(this.DDObjects, "Objects");
    this.DDObjects.add(this.DDObjMain, null);
    this.TPDD.add(this.DDProcesses, "Processes");
    this.DDProcesses.add(this.DDProcMain, null);
    this.DDLinkMain.add(this.DDLinkAgent, null);
    this.DDLinkMain.add(this.DDLinkInstr, null);
    this.DDLinkMain.add(this.DDLinkRes, null);
    this.DDLinkMain.add(this.DDLinkEffect, null);
    this.DDLinkMain.add(this.DDLinkEvent, null);
    this.DDLinkMain.add(this.DDLinkException, null);
    this.DDLinkMain.add(this.DDLinkInvoc, null);
    this.DDLinkMain.add(this.DDLinkInstrEv, null);
    this.DDLinkMain.add(this.DDLinkCondition, null);
    this.DDLinkMain.add(this.DDLinkProp, null);
    this.DDLinkProp.add(this.DDLinkPropCond, null);
    this.DDLinkProp.add(this.DDLinkPropPath, null);
    this.DDLinkProp.add(this.DDLinkPropReacTime, null);
    this.DDLinkMain.add(this.LinkUnSelAll, null);
    this.DDLinkMain.add(this.LinkSelAll, null);
    this.TPDD.add(this.DDLinks, "Links");
    this.DDLinks.add(this.DDLinkMain, null);
    this.DDRelMain.add(this.DDRelUni, null);
    this.DDRelMain.add(this.DDRelBi, null);
    this.DDRelMain.add(this.DDRelAggreg, null);
    this.DDRelMain.add(this.DDRelFeature, null);
    this.DDRelMain.add(this.DDRelGeneral, null);
    this.DDRelMain.add(this.DDRelClassif, null);
    this.DDRelMain.add(this.RelUnSelAll, null);
    this.DDRelMain.add(this.RelSelAll, null);
    this.TPDD.add(this.DDRelations, "Relations");
    this.DDRelations.add(this.DDRelMain, null);
    this.contentPane.add(this.DFromTempPanel, null);
    this.OPL.add(this.OPLUntilText, null);
    this.OPL.add(this.OPLAll, null);
    this.OPL.add(this.OPLUntil, null);
    this.OPL.add(this.OPLByName, null);
    this.OPL.add(this.OPLNone, null);
    this.OPL.add(this.OPLandOPD, null);
    this.DD.add(this.TPDD, null);
    this.OPDOPL.add(this.OPD, null);
    this.DDObjMain.add(this.DDObjDesc, null);
    this.DDObjMain.add(this.DDObjOrigin, null);
    this.DDObjMain.add(this.DDObjScope, null);
    this.DDObjMain.add(this.DDObjEssence, null);
    this.DDObjMain.add(this.DDObjIndex, null);
    this.DDObjMain.add(this.DDObjInitVal, null);
    this.DDObjMain.add(this.DDObjType, null);
    this.DDObjMain.add(this.DDObjStates, null);
    this.DDObjMain.add(this.DDObjState, null);
    this.DDObjState.add(this.DDObjStateInitial, null);
    this.DDObjState.add(this.DDObjStateDescr, null);
    this.DDObjState.add(this.DDObjStateTime, null);
    this.DDObjMain.add(this.ObjUnSelAll, null);
    this.DDObjMain.add(this.ObjSelAll, null);
     this.GI.add(this.GILeft, null);
    this.BG_OPD.add(this.OPDAll);
    this.BG_OPD.add(this.OPDUntil);
    this.BG_OPD.add(this.OPDByName);
    this.BG_OPD.add(this.OPDNone);
    this.ButtonsPanel.add(this.Generate, null);
    this.ButtonsPanel.add(this.Cancel, null);
    this.ButtonsPanel.add(this.MakeTemp, null);
    this.contentPane.add(this.DTSavePanel, null);
    this.DTSavePanel.add(this.DTSaveText, null);
    this.DTSavePanel.add(this.DTSaveCancel, null);
    this.DTSavePanel.add(this.DTSaveOK, null);
    this.DTSavePanel.add(this.DTSaveL, null);
    this.OPD.add(this.OPDUntil, null);
    this.OPD.add(this.OPDAll, null);
    this.OPD.add(this.OPDByName, null);
    this.OPD.add(this.OPDNone, null);
    this.OPD.add(this.OPDUntilText, null);
    this.OPDOPL.add(this.OPL, null);
    this.BG_OPL.add(this.OPLAll);
    this.BG_OPL.add(this.OPLUntil);
    this.BG_OPL.add(this.OPLByName);
    this.BG_OPL.add(this.OPLNone);
    this.BG_OPL.add(this.OPLandOPD);
    this.TPMain.add(this.GI, "General Info");
    this.TPMain.add(this.OPDOPL, "OPD & OPL");
    this.TPMain.add(this.DD,  "Element Dictionary");
    this.DFromTempPanel.add(this.DTempRadio, null);
    this.DFromTempPanel.add(this.DTCombo, null);
    this.contentPane.add(this.TPMain, null);
    this.DDObjState.setVisible(false);
    this.DTCombo.setVisible(false);
    this.DTSavePanel.setVisible(false);
    this.OPDAll.setSelected(true);
    this.OPLandOPD.setSelected(true);
  }
  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      this.dispose();
      //System.exit(0);
    }
  }

/**
 * Checks if the state properties panel should be visible.
 * @param e ActionEvent on ObjStates
 */
  void DDObjStates_actionPerformed(ActionEvent e) {
    if (this.DDObjStates.isSelected()) {
      this.DDObjState.setVisible(true);
    }
    if (this.DDObjStates.isSelected()==false) {
      this.DDObjState.setVisible(false);
    }
  }
/**
 * In case that All OPD levels is selected
 * makes the text field for OPD level invisible.
 * @param e ActionEvent on OPDAll
 */
  void OPDAll_actionPerformed(ActionEvent e) {
    if (this.OPDAll.isSelected()) {
      this.OPDUntilText.setVisible(false);
    }
  }
/**
 * In case that No OPD levels is selected
 * makes the text field for OPD level invisible.
 * @param e ActionEvent on OPDNone
 */
  void OPDNone_actionPerformed(ActionEvent e) {
    if (this.OPDNone.isSelected()) {
      this.OPDUntilText.setVisible(false);
    }
  }
/**
 * In case that OPD Until is selected
 * makes the text field for OPD level visible.
 * @param e ActionEvent on OPDUntil
 */
 void OPDUntil_actionPerformed(ActionEvent e) {
    if (this.OPDUntil.isSelected()) {
      this.OPDUntilText.setVisible(true);
    }
 }
/**
 * In case that OPD By name is selected
 * makes the text field for OPD level invisible.
 * @param e ActionEvent on OPD by Name
 */
  void OPDByName_actionPerformed(ActionEvent e) {
    if (this.OPDByName.isSelected()) {
      this.OPDUntilText.setVisible(false);
    }
  }
/**
 * In case that OPL Until is selected
 * makes the text field for OPL level visible.
 * @param e ActionEvent on OPL Until
 */
  void OPLUntil_actionPerformed(ActionEvent e) {
    if (this.OPLUntil.isSelected()) {
		this.OPLUntilText.setVisible(true);
	}
  }
/**
 * In case that All OPL Levels is selected
 * makes the text field for OPL level invisible.
 * @param e ActionEvent on All OPL Level
 */
  void OPLAll_actionPerformed(ActionEvent e) {
    if (this.OPLAll.isSelected()) {
		this.OPLUntilText.setVisible(false);
	}
  }
/**
 * In case that No OPL is selected
 * makes the text field for OPL level invisible.
 * @param e ActionEvent on No OPL
 */
  void OPLNone_actionPerformed(ActionEvent e) {
    if (this.OPLNone.isSelected()) {
		this.OPLUntilText.setVisible(false);
	}
  }
/**
 * In case that OPL By name is selected
 * makes the text field for OPL level invisible.
 * @param e ActionEvent on OPL By name
 */
  void OPLByName_actionPerformed(ActionEvent e) {
    if (this.OPLByName.isSelected()) {
		this.OPLUntilText.setVisible(false);
	}
  }
/**
 * In case that OPL according to OPD is selected
 * makes the text field for OPL level invisible.
 * @param e ActionEvent on OPL according to OPD
 */
  void OPLandOPD_actionPerformed(ActionEvent e) {
  if (this.OPLandOPD.isSelected()) {
	this.OPLUntilText.setVisible(false);
}
  }
/**
 * For any change made to the Agent Link,
 * checks if link properties panel should be visible
 * and sets it respectively.
 * @param e ActionEvent on Agent Link
 */
  void DDLinkAgent_actionPerformed(ActionEvent e) {
    if (this.DDLinkAgent.isSelected()) {
		this.DDLinkProp.setVisible(true);
	}
    if ((this.DDLinkAgent.isSelected()==false)&&(this.DDLinkInstr.isSelected()==false)&&(this.DDLinkRes.isSelected()==false)&&(this.DDLinkEffect.isSelected()==false)&&(this.DDLinkEvent.isSelected()==false)&&(this.DDLinkException.isSelected()==false)&&(this.DDLinkInvoc.isSelected()==false)&&(this.DDLinkInstrEv.isSelected()==false)&&(this.DDLinkCondition.isSelected()==false)) {
		this.DDLinkProp.setVisible(false);
	}
  }
/**
 * For any change made to the Instrument Link,
 * checks if link properties panel should be visible
 * and sets it respectively.
 * @param e ActionEvent on Instrument Link
 */
  void DDLinkInstr_actionPerformed(ActionEvent e) {
    if (this.DDLinkInstr.isSelected()) {
		this.DDLinkProp.setVisible(true);
	}
    if ((this.DDLinkAgent.isSelected()==false)&&(this.DDLinkInstr.isSelected()==false)&&(this.DDLinkRes.isSelected()==false)&&(this.DDLinkEffect.isSelected()==false)&&(this.DDLinkEvent.isSelected()==false)&&(this.DDLinkException.isSelected()==false)&&(this.DDLinkInvoc.isSelected()==false)&&(this.DDLinkInstrEv.isSelected()==false)&&(this.DDLinkCondition.isSelected()==false)) {
		this.DDLinkProp.setVisible(false);
	}
  }
/**
 * For any change made to the Result Link,
 * checks if link properties panel should be visible
 * and sets it respectively.
 * @param e ActionEvent on Result Link
 */
  void DDLinkRes_actionPerformed(ActionEvent e) {
    if (this.DDLinkRes.isSelected()) {
		this.DDLinkProp.setVisible(true);
	}
    if ((this.DDLinkAgent.isSelected()==false)&&(this.DDLinkInstr.isSelected()==false)&&(this.DDLinkRes.isSelected()==false)&&(this.DDLinkEffect.isSelected()==false)&&(this.DDLinkEvent.isSelected()==false)&&(this.DDLinkException.isSelected()==false)&&(this.DDLinkInvoc.isSelected()==false)&&(this.DDLinkInstrEv.isSelected()==false)&&(this.DDLinkCondition.isSelected()==false)) {
		this.DDLinkProp.setVisible(false);
	}
  }

/**
 * For any change made to the Effect Link,
 * checks if link properties panel should be visible
 * and sets it respectively.
 * @param e ActionEvent on Effect Link
 */
  void DDLinkEffect_actionPerformed(ActionEvent e) {
    if (this.DDLinkEffect.isSelected()) {
		this.DDLinkProp.setVisible(true);
	}
    if ((this.DDLinkAgent.isSelected()==false)&&(this.DDLinkInstr.isSelected()==false)&&(this.DDLinkRes.isSelected()==false)&&(this.DDLinkEffect.isSelected()==false)&&(this.DDLinkEvent.isSelected()==false)&&(this.DDLinkException.isSelected()==false)&&(this.DDLinkInvoc.isSelected()==false)&&(this.DDLinkInstrEv.isSelected()==false)&&(this.DDLinkCondition.isSelected()==false)) {
		this.DDLinkProp.setVisible(false);
	}
  }

/**
 * For any change made to the Event Link,
 * checks if link properties panel should be visible
 * and sets it respectively.
 * @param e ActionEvent on Event Link
 */
  void DDLinkEvent_actionPerformed(ActionEvent e) {
    if (this.DDLinkEvent.isSelected()) {
		this.DDLinkProp.setVisible(true);
	}
    if ((this.DDLinkAgent.isSelected()==false)&&(this.DDLinkInstr.isSelected()==false)&&(this.DDLinkRes.isSelected()==false)&&(this.DDLinkEffect.isSelected()==false)&&(this.DDLinkEvent.isSelected()==false)&&(this.DDLinkException.isSelected()==false)&&(this.DDLinkInvoc.isSelected()==false)&&(this.DDLinkInstrEv.isSelected()==false)&&(this.DDLinkCondition.isSelected()==false)) {
		this.DDLinkProp.setVisible(false);
	}
  }

/**
 * For any change made to the Exception Link,
 * checks if link properties panel should be visible
 * and sets it respectively.
 * @param e ActionEvent on Exception Link
 */
  void DDLinkException_actionPerformed(ActionEvent e) {
    if (this.DDLinkException.isSelected()) {
		this.DDLinkProp.setVisible(true);
	}
    if ((this.DDLinkAgent.isSelected()==false)&&(this.DDLinkInstr.isSelected()==false)&&(this.DDLinkRes.isSelected()==false)&&(this.DDLinkEffect.isSelected()==false)&&(this.DDLinkEvent.isSelected()==false)&&(this.DDLinkException.isSelected()==false)&&(this.DDLinkInvoc.isSelected()==false)&&(this.DDLinkInstrEv.isSelected()==false)&&(this.DDLinkCondition.isSelected()==false)) {
		this.DDLinkProp.setVisible(false);
	}
  }

/**
 * For any change made to the Invocation Link,
 * checks if link properties panel should be visible
 * and sets it respectively.
 * @param e ActionEvent on Invocation Link
 */
  void DDLinkInvoc_actionPerformed(ActionEvent e) {
    if (this.DDLinkInvoc.isSelected()) {
		this.DDLinkProp.setVisible(true);
	}
    if ((this.DDLinkAgent.isSelected()==false)&&(this.DDLinkInstr.isSelected()==false)&&(this.DDLinkRes.isSelected()==false)&&(this.DDLinkEffect.isSelected()==false)&&(this.DDLinkEvent.isSelected()==false)&&(this.DDLinkException.isSelected()==false)&&(this.DDLinkInvoc.isSelected()==false)&&(this.DDLinkInstrEv.isSelected()==false)&&(this.DDLinkCondition.isSelected()==false)) {
		this.DDLinkProp.setVisible(false);
	}
  }

/**
 * For any change made to the Instrument Event Link,
 * checks if link properties panel should be visible
 * and sets it respectively.
 * @param e ActionEvent on Instrument Event Link
 */
  void DDLinkInstrEv_actionPerformed(ActionEvent e) {
    if (this.DDLinkInstrEv.isSelected()) {
		this.DDLinkProp.setVisible(true);
	}
    if ((this.DDLinkAgent.isSelected()==false)&&(this.DDLinkInstr.isSelected()==false)&&(this.DDLinkRes.isSelected()==false)&&(this.DDLinkEffect.isSelected()==false)&&(this.DDLinkEvent.isSelected()==false)&&(this.DDLinkException.isSelected()==false)&&(this.DDLinkInvoc.isSelected()==false)&&(this.DDLinkInstrEv.isSelected()==false)&&(this.DDLinkCondition.isSelected()==false)) {
		this.DDLinkProp.setVisible(false);
	}
  }

/**
 * For any change made to the Condition Link,
 * checks if link properties panel should be visible
 * and sets it respectively.
 * @param e ActionEvent on Condition Link
 */
  void DDLinkCondition_actionPerformed(ActionEvent e) {
    if (this.DDLinkCondition.isSelected()) {
		this.DDLinkProp.setVisible(true);
	}
    if ((this.DDLinkAgent.isSelected()==false)&&(this.DDLinkInstr.isSelected()==false)&&(this.DDLinkRes.isSelected()==false)&&(this.DDLinkEffect.isSelected()==false)&&(this.DDLinkEvent.isSelected()==false)&&(this.DDLinkException.isSelected()==false)&&(this.DDLinkInvoc.isSelected()==false)&&(this.DDLinkInstrEv.isSelected()==false)&&(this.DDLinkCondition.isSelected()==false)) {
		this.DDLinkProp.setVisible(false);
	}
  }

/**
 * Checks the input to be valid.<BR>
 * In Case the input is valid, creates a new document by DocCreate Funciton.
 * Checks if OPD and/or OPL are to be chosen by name.<BR>
 * If they are: opens the choosing window, else
 * opens the save window and calls for print function in "doc".
 * @param e ActionEvent on Generate button
 */
  void Generate_actionPerformed(ActionEvent e) {
    try {

      //if OPD untill is selected -> check that there is a valid level number
      if (this.OPDUntil.isSelected()) {
        int opd_text = Integer.parseInt(this.OPDUntilText.getText());
        if (opd_text <1) {
			opd_text = Integer.parseInt("jdkfh");
		}
      }//of if
      //if OPL untill is selected -> check that there is a valid level number
      if (this.OPLUntil.isSelected()) {
        int opl_text = Integer.parseInt(this.OPLUntilText.getText());
        if (opl_text <1) {
			opl_text = Integer.parseInt("jdkfh");
		}
      }//of if

      Document doc = this.DocCreate();//create a new document
      //in case the user wants to choose the diagrams or OPL by name
      //show the chooser window
      if ( (this.OPDByName.isSelected())||(this.OPLByName.isSelected())){
        int type = 0;
        if (this.OPDByName.isSelected()) {
			type = 0;//only OPDlist
		}
        if (this.OPLByName.isSelected()) {
			type = 1;//only OPLlist
		}
        if ((this.OPLByName.isSelected())&&(this.OPDByName.isSelected())) {
			type =2;//both OPD and OPL lists
		}
        DocOPDByName opdlist = new DocOPDByName(this.mySys, type, doc, this);
        opdlist.setVisible(true);
      }// of if
      //if the user is not interested in choosing OPD or OPL by name
      else {
        String filename="";
        DocSave doc_save = new DocSave(this); //show the window for choosing
                                              //a name for the document
        int returnVal = doc_save.SDFileChooser.showSaveDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
          String name = doc_save.SDFileChooser.getSelectedFile().getName();
          filename = doc_save.SDFileChooser.getSelectedFile().getPath();
          StringTokenizer st = new StringTokenizer(filename, ".", false);
          filename = st.nextToken()+".htm";
          File myDir = doc_save.SDFileChooser.getCurrentDirectory();
          int flag = 0; //0 - no such file yet
          int i=0;
          //checks if a file with this name already exists
          if (myDir.exists()) {
            File[] contents = myDir.listFiles();
            while (i < contents.length) {
              if (contents[i].getName().compareTo(name)==0) {
				flag = 1;
			}
              i++;
            }//of while
          }//of if
          //if there is a file with this name
          //ask if the user is interested in overwriting the file
          //if YES -> print the document
          if (flag==1){
            int retval = JOptionPane.showConfirmDialog( this, "A document with this name already exists. Do you want to overwrite it?", "Overwrite Document", JOptionPane.YES_NO_OPTION);
            if (retval== JOptionPane.YES_OPTION){
              try{
                //make the file
                doc.PrintDocument(this.mySys,filename);
                //close this window
                this.dispose();
              }// of try
              catch(IOException er){
                System.out.println("There was a problem printing the file.");
              }// of catch
            }//of if
          }//of if
          //if there is no file with this name
          else {
            try{
              //make the file
              doc.PrintDocument(this.mySys,filename);
              //close this window
              this.dispose();
            }// of try
            catch(IOException er){
              System.out.println("There was a problem printing the file.");
            }// of catch
          }//of else
        }//of if
      }//of else
    }//of try
    //if there was an invalid level number
    catch (NumberFormatException ne) {
      JOptionPane.showMessageDialog(this, "Illegal input: level is not valid. Please insert an integer.", "ERROR", JOptionPane.ERROR_MESSAGE);
    }//of catch
  }

/**
  * Cancel creating a document
  * @param e ActionEvent on Cancel button
  */
  void Cancel_actionPerformed(ActionEvent e) {
    this.dispose();
  }

/**
 * Checks for legal input and sets the "save as template" panel visible.
 * @param e ActionEvent on MakeTemp button
 */
  void MakeTemp_actionPerformed(ActionEvent e) {
    try {
      //if OPD untill is selected -> check that the level is a legal number
      if (this.OPDUntil.isSelected()){
        int opd_text = Integer.parseInt(this.OPDUntilText.getText());
	  if (opd_text<1) {
		opd_text=Integer.parseInt("jsak");
	}
      }
      //if OPL untill is selected -> check that the level is a legal number
      if (this.OPLUntil.isSelected()) {
        int opl_text = Integer.parseInt(this.OPLUntilText.getText());
	  if (opl_text<1) {
		opl_text=Integer.parseInt("jsak");
	}
      }
      this.DTSavePanel.setVisible(true);
    }//of try
    catch (NumberFormatException ne) {
      JOptionPane.showMessageDialog(this, "Illegal input: level is not valid. Please insert an integer.", "ERROR", JOptionPane.ERROR_MESSAGE);
    }//of catch
  }

/**
 * Enables choosing a template from a list of avaliable templates.
 * @param e ActionEvent on From Template radio box
 */
  void DTempRadio_actionPerformed(ActionEvent e) {
    if (this.DTempRadio.isSelected()) {
		this.DTCombo.setVisible(true);
	}
    if (!this.DTempRadio.isSelected()) {
		this.DTCombo.setVisible(false);
	}
  }

/**
 * Cancel saving as template. Hides the "Save as template" panel.
 * @param e ActionEvent on save button in save as template paenl
 */
  void DTSaveCancel_actionPerformed(ActionEvent e) {
    this.DTSavePanel.setVisible(false); //hide the "save as template" panel
  }

/**
 * Saves the selection as a template and hides the "Save as template" panel.
 * @param e ActionEvent on save button on main panel
 */
  void DTSaveOK_actionPerformed(ActionEvent e) {

    try{
     String temp=this.DTSaveText.getText(); //the name of the template
     //if a name for the template was not inserted
     if (temp.compareTo("")==0) {
       JOptionPane.showMessageDialog(this, "Please insert a name for the template.", "ERROR", JOptionPane.ERROR_MESSAGE);
     }//of if
     //if a name for the template was inserted
     else{
      int flag = 0; //a flag indicating if there is a template with this name
                    //0 - no such file yet
      int i=0;
      //if there is a directory for templates already
      if (this.myDir.exists()) {
        File[] contents = this.myDir.listFiles();
        //check all the files in the templates directory
        //if there is a template with this name
        //set flag to 1
        while (i < contents.length) {
          if (contents[i].getName().compareTo(temp)==0) {
			flag = 1;
		}
          i++;
        }//of while
      }//of if
      //if there is a template with this name already
      //ask if the user wants to overwrite that template
      //if YES -> overwrite, else do nothing
      if (flag == 1){
        int retval=JOptionPane.showConfirmDialog( this, "A template with this name already exists. Do you want to overwrite it?", "Overwrite Template", JOptionPane.YES_NO_OPTION);
        if (retval== JOptionPane.YES_OPTION){
          Document doc = this.DocCreate();//create a new document
          doc.CreateTemplate(this.mySys, temp);//save the document as template
          this.DTSavePanel.setVisible(false);//hide the "save as template" panel
        }//of if
      }//of if
      //if there isn't a template with this name already
      //save the selection as a template
      else {
        Document doc = this.DocCreate();//create a new document
        doc.CreateTemplate(this.mySys, temp);//save it as template
        this.DTSavePanel.setVisible(false);//hide the "save as template" panel
        this.DTCombo.addItem(temp);//add the name of the new template to the combo
                              //with the list of available templates
      }//of else
     }//of else
    this.DTSaveText.setText("");
    }//of try
    catch(IOException er){
       System.out.println("Wasn't able to save the template");
    }//of catch
  }//of DTSaveOK_actionPerformed

/**
 * Takes the selected fields from the screen, creates a new document
 * and marks the user selection in the document.
 * @return doc of type Document, with the relevent choices
 */
  Document DocCreate (){

    Document doc = new Document();
    boolean[] obj_array =new boolean[11];//an array for object fields
    boolean[] proc_array =new boolean[6];//an array for process fields
    boolean[] rel_array =new boolean[6];//an array for relation fields
    boolean[] link_array =new boolean[12];//an array for link fields
    boolean[] opd_array  =new boolean[7];//an array for opdopl fields
    boolean[] gi_array = new boolean[12];//an array for general info fields

    //for fields of object, check if the field is selected on the screen -
    //if selected -> set TRUE in the array, else FALSE
    obj_array[0] = this.DDObjType.isSelected();
    obj_array[1] = this.DDObjDesc.isSelected();
    obj_array[2] = this.DDObjInitVal.isSelected();
    obj_array[3] = this.DDObjEssence.isSelected();
    obj_array[4] = this.DDObjIndex.isSelected();
    obj_array[5] = this.DDObjScope.isSelected();
    obj_array[6] = this.DDObjOrigin.isSelected();
    obj_array[7] = this.DDObjStates.isSelected();
    obj_array[8] = this.DDObjStateDescr.isSelected();
    obj_array[9] = this.DDObjStateInitial.isSelected();
    obj_array[10] = this.DDObjStateTime.isSelected();
    //for fields of process, check if the field is selected on the screen -
    //if selected -> set TRUE in the array, else FALSE
    proc_array[0] = this.DDProcDescr.isSelected();
    proc_array[1] = this.DDProcEssence.isSelected();
    proc_array[2] = this.DDProcOrigin.isSelected();
    proc_array[3] = this.DDProcScope.isSelected();
    proc_array[4] = this.DDProcBody.isSelected();
    proc_array[5] = this.DDProcActTime.isSelected();
    //for fields of relation, check if the field is selected on the screen -
    //if selected -> set TRUE in the array, else FALSE
    rel_array[0] = this.DDRelAggreg.isSelected();
    rel_array[1] = this.DDRelClassif.isSelected();
    rel_array[2] = this.DDRelFeature.isSelected();
    rel_array[3] = this.DDRelGeneral.isSelected();
    rel_array[4] = this.DDRelBi.isSelected();
    rel_array[5] = this.DDRelUni.isSelected();
    //for fields of link, check if the field is selected on the screen -
    //if selected -> set TRUE in the array, else FALSE
    link_array[0] = this.DDLinkAgent.isSelected();
    link_array[1] = this.DDLinkCondition.isSelected();
    link_array[2] = this.DDLinkEffect.isSelected();
    link_array[3] = this.DDLinkEvent.isSelected();
    link_array[4] = this.DDLinkException.isSelected();
    link_array[5] = this.DDLinkInstrEv.isSelected();
    link_array[6] = this.DDLinkInvoc.isSelected();
    link_array[7] = this.DDLinkRes.isSelected();
    link_array[8] = this.DDLinkInstr.isSelected();
    link_array[9] = this.DDLinkPropCond.isSelected();
    link_array[10] = this.DDLinkPropPath.isSelected();
    link_array[11] = this.DDLinkPropReacTime.isSelected();
    //for fields of opd and opl, check if the field is selected on the screen -
    //if selected -> set TRUE in the array, else FALSE
    opd_array[0] = this.OPDAll.isSelected();
    opd_array[1] = this.OPDByName.isSelected();
    opd_array[2] = this.OPDNone.isSelected();
    opd_array[3] = this.OPLAll.isSelected();
    opd_array[4] = this.OPLByName.isSelected();
    opd_array[5] = this.OPLNone.isSelected();
    opd_array[6] = this.OPLandOPD.isSelected();
    //for fields of general info, check if the field is selected on the screen -
    //if selected -> set TRUE in the array, else FALSE
    gi_array[0] = this.GIClient.isSelected();
    gi_array[1] = this.GIOverview.isSelected();
    gi_array[2] = this.GICurrent.isSelected();
    gi_array[3] = this.GIGoals.isSelected();
    gi_array[4] = this.GIBusiness.isSelected();
    gi_array[5] = this.GIFuture.isSelected();
    gi_array[6] = this.GIHard.isSelected();
    gi_array[7] = this.GIInputs.isSelected();
    gi_array[8] = this.GIIssues.isSelected();
    gi_array[9] = this.GIProblems.isSelected();
    gi_array[10] = this.GIUsers.isSelected();
    gi_array[11] = this.GIOper.isSelected();

    String OPDUntilT,OPLUntilT; //strings for the untill level
    //if OPD until is selected -> put level chosen in the string
    if (this.OPDUntil.isSelected()) {
		OPDUntilT=this.OPDUntilText.getText();
		//else -> put -1
	} else {
		OPDUntilT="-1";
	}
    //if OPL until is selected -> put level chosen in the string
    if (this.OPLUntil.isSelected()) {
		OPLUntilT=this.OPLUntilText.getText();
		//else -> put -1
	} else {
		OPLUntilT="-1";
	}
    //save the selection in the document created by init functions
    doc.DocInfo.Data.Proc.ProcInit(proc_array);
    doc.DocInfo.Data.Obj.ObjInit(obj_array);
    doc.DocInfo.Data.Link.LinkInit(link_array);
    doc.DocInfo.Data.Rel.RelInit(rel_array);
    doc.DocInfo.GI.GIInit(gi_array);
    doc.DocInfo.opdopl.OPDOPLInit(opd_array,OPDUntilT,OPLUntilT);
    return doc;//return the document created
  }

/**
 * The function is showing the choices selected in the document d on the screen
 * @param d Object of type Document
 */
  void TempToScreen(Document d){

    Document doc=d;
    //for fields of object, check if the field is selected in document -
    //if selected -> set selected on screen, else unselect
    this.DDObjType.setSelected(doc.DocInfo.Data.Obj.getType());
    this.DDObjDesc.setSelected(doc.DocInfo.Data.Obj.getDesc());
    this.DDObjInitVal.setSelected(doc.DocInfo.Data.Obj.getInValue());
    this.DDObjEssence.setSelected(doc.DocInfo.Data.Obj.getEssence());
    this.DDObjIndex.setSelected(doc.DocInfo.Data.Obj.getIndex());
    this.DDObjScope.setSelected(doc.DocInfo.Data.Obj.getScope());
    this.DDObjOrigin.setSelected(doc.DocInfo.Data.Obj.getOrigin());
    this.DDObjStates.setSelected(doc.DocInfo.Data.Obj.getStates());
    this.DDObjStateDescr.setSelected(doc.DocInfo.Data.Obj.getStateDesc());
    this.DDObjStateInitial.setSelected(doc.DocInfo.Data.Obj.getStateInitial());
    this.DDObjStateTime.setSelected(doc.DocInfo.Data.Obj.getStateTime());
    //in case the states field for objects is selected ->
    //set visible the pannel with state properties
    if (this.DDObjStates.isSelected()) {
		this.DDObjState.setVisible(true);
	} else {
		this.DDObjState.setVisible(false);
	}

    //for fields of process, check if the field is selected in document -
    //if selected -> set selected on screen, else unselect
    this.DDProcDescr.setSelected(doc.DocInfo.Data.Proc.getDesc());
    this.DDProcEssence.setSelected(doc.DocInfo.Data.Proc.getEssence());
    this.DDProcOrigin.setSelected(doc.DocInfo.Data.Proc.getOrigin());
    this.DDProcScope.setSelected(doc.DocInfo.Data.Proc.getScope());
    this.DDProcBody.setSelected(doc.DocInfo.Data.Proc.getBody());
    this.DDProcActTime.setSelected(doc.DocInfo.Data.Proc.getActTime());

    //for fields of relations, check if the field is selected in document -
    //if selected -> set selected on screen, else unselect
    this.DDRelAggreg.setSelected(doc.DocInfo.Data.Rel.getAgPar());
    this.DDRelClassif.setSelected(doc.DocInfo.Data.Rel.getClassInst());
    this.DDRelFeature.setSelected(doc.DocInfo.Data.Rel.getFeChar());
    this.DDRelGeneral.setSelected(doc.DocInfo.Data.Rel.getGenSpec());
    this.DDRelBi.setSelected(doc.DocInfo.Data.Rel.getBiDir());
    this.DDRelUni.setSelected(doc.DocInfo.Data.Rel.getUniDir());

    //for fields of links, check if the field is selected in document -
    //if selected -> set selected on screen, else unselect
    this.DDLinkAgent.setSelected(doc.DocInfo.Data.Link.getAgent());
    this.DDLinkCondition.setSelected(doc.DocInfo.Data.Link.getCondition());
    this.DDLinkEffect.setSelected(doc.DocInfo.Data.Link.getEffect());
    this.DDLinkEvent.setSelected(doc.DocInfo.Data.Link.getEvent());
    this.DDLinkException.setSelected(doc.DocInfo.Data.Link.getException());
    this.DDLinkInstrEv.setSelected(doc.DocInfo.Data.Link.getInstEvent());
    this.DDLinkInvoc.setSelected(doc.DocInfo.Data.Link.getInvocation());
    this.DDLinkRes.setSelected(doc.DocInfo.Data.Link.getResCons());
    this.DDLinkInstr.setSelected(doc.DocInfo.Data.Link.getInstrument());
    this.DDLinkPropCond.setSelected(doc.DocInfo.Data.Link.getCond());
    this.DDLinkPropPath.setSelected(doc.DocInfo.Data.Link.getPath());
    this.DDLinkPropReacTime.setSelected(doc.DocInfo.Data.Link.getReactTime());
    //if at least one of the link types is selected - >
    //set the panel with link properties visible, else hide it
    if((this.DDLinkAgent.isSelected()==false)&&(this.DDLinkInstr.isSelected()==false)&&(this.DDLinkRes.isSelected()==false)&&(this.DDLinkEffect.isSelected()==false)&&(this.DDLinkEvent.isSelected()==false)&&(this.DDLinkException.isSelected()==false)&&(this.DDLinkInvoc.isSelected()==false)&&(this.DDLinkInstrEv.isSelected()==false)&&(this.DDLinkCondition.isSelected()==false)) {
		this.DDLinkProp.setVisible(false);
	} else {
		this.DDLinkProp.setVisible(true);
	}

    //for fields of OPD and OPL, check if the field is selected in document -
    //if selected -> set selected on screen, else unselect
    this.OPDAll.setSelected(doc.DocInfo.opdopl.getOPDAll());
    this.OPDByName.setSelected(doc.DocInfo.opdopl.getOPDByName());
    this.OPDNone.setSelected(doc.DocInfo.opdopl.getOPDNone());
    this.OPLAll.setSelected(doc.DocInfo.opdopl.getOPLAll());
    this.OPLByName.setSelected(doc.DocInfo.opdopl.getOPLByName());
    this.OPLNone.setSelected(doc.DocInfo.opdopl.getOPLNone());
    this.OPLandOPD.setSelected(doc.DocInfo.opdopl.getOPLAccording());

    //for fields of General Info, check if the field is selected in document -
    //if selected -> set selected on screen, else unselect
    this.GIClient.setSelected(doc.DocInfo.GI.getClient());
    this.GIOverview.setSelected(doc.DocInfo.GI.getOverview());
    this.GICurrent.setSelected(doc.DocInfo.GI.getCurrent());
    this.GIGoals.setSelected(doc.DocInfo.GI.getGoals());
    this.GIBusiness.setSelected(doc.DocInfo.GI.getBusiness());
    this.GIFuture.setSelected(doc.DocInfo.GI.getFuture());
    this.GIHard.setSelected(doc.DocInfo.GI.getHard());
    this.GIInputs.setSelected(doc.DocInfo.GI.getInputs());
    this.GIIssues.setSelected(doc.DocInfo.GI.getIssues());
    this.GIProblems.setSelected(doc.DocInfo.GI.getProblems());
    this.GIUsers.setSelected(doc.DocInfo.GI.getUsers());
    this.GIOper.setSelected(doc.DocInfo.GI.getOper());

    //if OPD untill was not selected in the document ->
    //hide text field of the level, unselect the field
    if(doc.DocInfo.opdopl.getOPDUntil().compareTo("-1")==0){
      this.OPDUntil.setSelected(false);
      this.OPDUntilText.setVisible(false);
    }
    //else, if OPD untill is selected ->
    //select the relevant field, show the text field for the level,
    //and put the level from the document in the text field
    else{
      this.OPDUntil.setSelected(true);
      this.OPDUntilText.setVisible(true);
      this.OPDUntilText.setText(doc.DocInfo.opdopl.getOPDUntil());
    }
    //if OPL untill was not selected in the document ->
    //hide text field of the level, unselect the field
    if(doc.DocInfo.opdopl.getOPLUntil().compareTo("-1")==0){
      this.OPLUntil.setSelected(false);
      this.OPLUntilText.setVisible(false);
    }
    //else, if OPL untill is selected ->
    //select the relevant field, show the text field for the level,
    //and put the level from the document in the text field
    else{
      this.OPLUntil.setSelected(true);
      this.OPLUntilText.setVisible(true);
      this.OPLUntilText.setText(doc.DocInfo.opdopl.getOPLUntil());
    }
  }

/**
 * When the user chooses the template to be opened from the list of
 *  templates in the combo box - findes the template and shows  the
 *  selected fields on the screen.
 * @param e ActionEvent on combo box with list of templates
 */
  void DTCombo_actionPerformed(ActionEvent e) {
    try {
      //if None template selected -> unselect all
      if (((String)this.DTCombo.getSelectedItem()).compareTo("None")==0){
        this.DDLinkAgent.setSelected(false);
        this.DDLinkCondition.setSelected(false);
        this.DDLinkEffect.setSelected(false);
        this.DDLinkEvent.setSelected(false);
        this.DDLinkException.setSelected(false);
        this.DDLinkInstr.setSelected(false);
        this.DDLinkInstrEv.setSelected(false);
        this.DDLinkInvoc.setSelected(false);
        this.DDLinkPropCond.setSelected(false);
        this.DDLinkPropPath.setSelected(false);
        this.DDLinkPropReacTime.setSelected(false);
        this.DDLinkRes.setSelected(false);
        this.DDLinkProp.setVisible(false);

        this.DDObjDesc.setSelected(false);
        this.DDObjEssence.setSelected(false);
        this.DDObjIndex.setSelected(false);
        this.DDObjInitVal.setSelected(false);
        this.DDObjOrigin.setSelected(false);
        this.DDObjScope.setSelected(false);
        this.DDObjStates.setSelected(false);
        this.DDObjStateDescr.setSelected(false);
        this.DDObjStateInitial.setSelected(false);
        this.DDObjStateTime.setSelected(false);
        this.DDObjType.setSelected(false);
        this.DDObjState.setVisible(false);

        this.DDProcActTime.setSelected(false);
        this.DDProcBody.setSelected(false);
        this.DDProcDescr.setSelected(false);
        this.DDProcEssence.setSelected(false);
        this.DDProcOrigin.setSelected(false);
        this.DDProcScope.setSelected(false);

        this.DDRelAggreg.setSelected(false);
        this.DDRelBi.setSelected(false);
        this.DDRelClassif.setSelected(false);
        this.DDRelFeature.setSelected(false);
        this.DDRelGeneral.setSelected(false);
        this.DDRelUni.setSelected(false);

        this.GIBusiness.setSelected(false);
        this.GIClient.setSelected(false);
        this.GICurrent.setSelected(false);
        this.GIFuture.setSelected(false);
        this.GIGoals.setSelected(false);
        this.GIHard.setSelected(false);
        this.GIInputs.setSelected(false);
        this.GIIssues.setSelected(false);
        this.GIOper.setSelected(false);
        this.GIOverview.setSelected(false);
        this.GIProblems.setSelected(false);
        this.GIUsers.setSelected(false);

        this.OPDAll.setSelected(true);
        this.OPDUntilText.setVisible(false);
        this.OPLandOPD.setSelected(true);
        this.OPLUntilText.setVisible(false);
      }
      else {
        Document doc = new Document(); //create a new document
        String filename = (String) this.DTCombo.getSelectedItem(); //get the template name
        FileInputStream file = new FileInputStream("Templates/" + filename); //find the file of the template
        doc.FromTemp(file); //load the selection from file to document
        this.TempToScreen(doc); //show the template on the screen
        file.close(); //close the file
      }
    }//of try
    catch(IOException er){
       System.out.println("There was a problem opening template");
    }//of catch
  }

/**
 * When the "Select All" button in the General Info panel is pressed
 * marks all the related fields as selected
 * @param e ActionEvent on select all button for genInfo
 */
  void SelAll_actionPerformed(ActionEvent e) {
      this.GIBusiness.setSelected(true);
      this.GIClient.setSelected(true);
      this.GICurrent.setSelected(true);
      this.GIFuture.setSelected(true);
      this.GIGoals.setSelected(true);
      this.GIHard.setSelected(true);
      this.GIInputs.setSelected(true);
      this.GIIssues.setSelected(true);
      this.GIOper.setSelected(true);
      this.GIOverview.setSelected(true);
      this.GIProblems.setSelected(true);
      this.GIUsers.setSelected(true);

  }

/**
 * When the "Unselect All" button in the General Info panel is pressed
 * marks all the related fields as unselected.
 * @param e ActionEvent on unselect all button for genInfo
 */
  void UnSelAll_actionPerformed(ActionEvent e) {
      this.GIBusiness.setSelected(false);
      this.GIClient.setSelected(false);
      this.GICurrent.setSelected(false);
      this.GIFuture.setSelected(false);
      this.GIGoals.setSelected(false);
      this.GIHard.setSelected(false);
      this.GIInputs.setSelected(false);
      this.GIIssues.setSelected(false);
      this.GIOper.setSelected(false);
      this.GIOverview.setSelected(false);
      this.GIProblems.setSelected(false);
      this.GIUsers.setSelected(false);
  }

/**
 *  When the "Select All" button in the Objects panel is pressed
 *  marks all the object related fields as selected.
 * @param e ActionEvent on select all button for objects
 */
  void ObjSelAll_actionPerformed(ActionEvent e) {
      this.DDObjDesc.setSelected(true);
      this.DDObjEssence.setSelected(true);
      this.DDObjIndex.setSelected(true);
      this.DDObjInitVal.setSelected(true);
      this.DDObjOrigin.setSelected(true);
      this.DDObjScope.setSelected(true);
      this.DDObjStates.setSelected(true);
      this.DDObjStateDescr.setSelected(true);
      this.DDObjStateInitial.setSelected(true);
      this.DDObjStateTime.setSelected(true);
      this.DDObjType.setSelected(true);
      this.DDObjState.setVisible(true);

  }

/**
 *  When the "Unselect All" button in the Objects panel is pressed
 *  marks all the object related fields as unselected.
 * @param e ActionEvent on unselect all button for objects
 */
  void ObjUnSelAll_actionPerformed(ActionEvent e) {
      this.DDObjDesc.setSelected(false);
      this.DDObjEssence.setSelected(false);
      this.DDObjIndex.setSelected(false);
      this.DDObjInitVal.setSelected(false);
      this.DDObjOrigin.setSelected(false);
      this.DDObjScope.setSelected(false);
      this.DDObjStates.setSelected(false);
      this.DDObjStateDescr.setSelected(false);
      this.DDObjStateInitial.setSelected(false);
      this.DDObjStateTime.setSelected(false);
      this.DDObjType.setSelected(false);
      this.DDObjState.setVisible(false);

  }

/**
 *  When the "Unselect All" button in the Processes panel is pressed
 *  marks all the process related fields as unselected.
 * @param e ActionEvent on unselect all button for processes
 */
  void ProcUnSelAll_actionPerformed(ActionEvent e) {
      this.DDProcActTime.setSelected(false);
      this.DDProcBody.setSelected(false);
      this.DDProcDescr.setSelected(false);
      this.DDProcEssence.setSelected(false);
      this.DDProcOrigin.setSelected(false);
      this.DDProcScope.setSelected(false);
  }

/**
 *  When the "Select All" button in the Processes panel is pressed
 *  marks all the process related fields as selected.
 * @param e ActionEvent on select all button for processes
 */
  void ProcSelAll_actionPerformed(ActionEvent e) {
      this.DDProcActTime.setSelected(true);
      this.DDProcBody.setSelected(true);
      this.DDProcDescr.setSelected(true);
      this.DDProcEssence.setSelected(true);
      this.DDProcOrigin.setSelected(true);
      this.DDProcScope.setSelected(true);
  }

/**
 *  When the "Unselect All" button in the Links panel is pressed
 *  marks all the link related fields as unselected.
 * @param e ActionEvent on unselect all button for links
 */
  void LinkUnSelAll_actionPerformed(ActionEvent e) {
      this.DDLinkAgent.setSelected(false);
      this.DDLinkCondition.setSelected(false);
      this.DDLinkEffect.setSelected(false);
      this.DDLinkEvent.setSelected(false);
      this.DDLinkException.setSelected(false);
      this.DDLinkInstr.setSelected(false);
      this.DDLinkInstrEv.setSelected(false);
      this.DDLinkInvoc.setSelected(false);
      this.DDLinkPropCond.setSelected(false);
      this.DDLinkPropPath.setSelected(false);
      this.DDLinkPropReacTime.setSelected(false);
      this.DDLinkRes.setSelected(false);
      this.DDLinkProp.setVisible(false);

  }

/**
 *  When the "Select All" button in the Links panel is pressed
 *  marks all the link related fields as selected.
 * @param e ActionEvent on select all button for links
 */
  void LinkSelAll_actionPerformed(ActionEvent e) {
      this.DDLinkAgent.setSelected(true);
      this.DDLinkCondition.setSelected(true);
      this.DDLinkEffect.setSelected(true);
      this.DDLinkEvent.setSelected(true);
      this.DDLinkException.setSelected(true);
      this.DDLinkInstr.setSelected(true);
      this.DDLinkInstrEv.setSelected(true);
      this.DDLinkInvoc.setSelected(true);
      this.DDLinkPropCond.setSelected(true);
      this.DDLinkPropPath.setSelected(true);
      this.DDLinkPropReacTime.setSelected(true);
      this.DDLinkRes.setSelected(true);
      this.DDLinkProp.setVisible(true);

  }

/**
 *  When the "Select All" button in the Relations panel is pressed
 *  marks all the relation related fields as selected.
 * @param e ActionEvent on select all button for relations
 */
  void RelSelAll_actionPerformed(ActionEvent e) {
    this.DDRelAggreg.setSelected(true);
      this.DDRelBi.setSelected(true);
      this.DDRelClassif.setSelected(true);
      this.DDRelFeature.setSelected(true);
      this.DDRelGeneral.setSelected(true);
      this.DDRelUni.setSelected(true);
  }

/**
 *  When the "Unselect All" button in the Relations panel is pressed
 *  marks all the relation related fields as unselected.
 * @param e ActionEvent on unselect all button for relations
 */
  void RelUnSelAll_actionPerformed(ActionEvent e) {
      this.DDRelAggreg.setSelected(false);
      this.DDRelBi.setSelected(false);
      this.DDRelClassif.setSelected(false);
      this.DDRelFeature.setSelected(false);
      this.DDRelGeneral.setSelected(false);
      this.DDRelUni.setSelected(false);

  }
}
