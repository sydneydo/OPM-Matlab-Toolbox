package extensionTools.search;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox; 
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JTextArea;
import javax.swing.JRadioButton;
import exportedAPI.opcatAPIx.IXSystem;
import javax.swing.JDialog;

/** 
 * a singleton which shows the dialog form of the search feature. 
 * it is invoked by 
 * <b>	JDialog searchDialog = null ; 
 *		searchDialog = SearchDialog.getInstance(opcatSystem) ; 
 *		searchDialog.setVisible(true) ;
 *</b> 
 * @author raanan
 *
 */
public class SearchDialog {

	private static JPanel jPanel = null;
	private static JCheckBox jCheckBoxforObjects = null;
	private static JCheckBox jCheckBoxforStates = null;
	private static JCheckBox jCheckBoxforProcesses = null;
	private static JPanel jPanel1 = null;
	private static JCheckBox jCheckBoxinName = null;
	private static JCheckBox jCheckBoxinDescription = null;
	private static JPanel jPanel2 = null;
	private static JPanel jPanel3 = null;
	private static JTextArea jTextArea = null;
	private static JPanel jPanel4 = null;
	private static JButton jSearchButton = null;
	private static JPanel jPanel5 = null;
	private static JRadioButton jRadioButtonalgoInString = null;
	private static JRadioButton jRadioButton1 = null;
	private static JDialog jDialog = null;  //  @jve:decl-index=0:visual-constraint="160,15"
	private static JPanel jContentPane1 = null;
	private static JDialog instance = null; 
	
	private  SearchDialog () {
		//its a singleton
	}
	
	public static JDialog getInstance (IXSystem system ) {
		if (instance == null ) {
			getJDialog().setLocationRelativeTo(system.getMainFrame()) ; 
			getJDialog().setAlwaysOnTop(true) ; 
			instance =  getJDialog() ; 
		} 
		return instance; 
		
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private static JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(getJPanel(), BoxLayout.Y_AXIS));
			jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search For", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12), new java.awt.Color(51,51,51)));
			jPanel.add(getJCheckBoxforStates(), null);
			jPanel.add(getJCheckBoxforProcesses(), null);
			jPanel.add(getJCheckBoxforObjects(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private static JCheckBox getJCheckBoxforObjects() {
		if (jCheckBoxforObjects == null) {
			jCheckBoxforObjects = new JCheckBox();
			jCheckBoxforObjects.setText("Objects");
			jCheckBoxforObjects.setSelected(true);
		}
		return jCheckBoxforObjects;
	}

	/**
	 * This method initializes jCheckBox1	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private static JCheckBox getJCheckBoxforStates() {
		if (jCheckBoxforStates == null) {
			jCheckBoxforStates = new JCheckBox();
			jCheckBoxforStates.setText("States");
		}
		return jCheckBoxforStates;
	}

	/**
	 * This method initializes jCheckBox2	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private static JCheckBox getJCheckBoxforProcesses() {
		if (jCheckBoxforProcesses == null) {
			jCheckBoxforProcesses = new JCheckBox();
			jCheckBoxforProcesses.setText("Processes");
			jCheckBoxforProcesses.setSelected(true);
		}
		return jCheckBoxforProcesses;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private static JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BoxLayout(getJPanel1(), BoxLayout.Y_AXIS));
			jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search In", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12), new java.awt.Color(51,51,51)));
			jPanel1.add(getJCheckBoxinName(), null);
			jPanel1.add(getJCheckBoxinDescription(), null);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jCheckBox3	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private static JCheckBox getJCheckBoxinName() {
		if (jCheckBoxinName == null) {
			jCheckBoxinName = new JCheckBox();
			jCheckBoxinName.setText("Name");
			jCheckBoxinName.setSelected(true);
		}
		return jCheckBoxinName;
	}

	/**
	 * This method initializes jCheckBox4	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private static JCheckBox getJCheckBoxinDescription() {
		if (jCheckBoxinDescription == null) {
			jCheckBoxinDescription = new JCheckBox();
			jCheckBoxinDescription.setText("Description");
		}
		return jCheckBoxinDescription;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private static JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.setLayout(new BorderLayout());
			jPanel2.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
			jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search Options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12), new java.awt.Color(51,51,51)));
			jPanel2.add(getJPanel(), java.awt.BorderLayout.WEST);
			jPanel2.add(getJPanel5(), java.awt.BorderLayout.EAST);
			jPanel2.add(getJPanel1(), java.awt.BorderLayout.CENTER);
		}
		return jPanel2;
	}

	/**
	 * This method initializes jPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private static JPanel getJPanel3() {
		if (jPanel3 == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(java.awt.FlowLayout.CENTER);
			jPanel3 = new JPanel();
			jPanel3.setLayout(flowLayout1);
			jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search Criteria", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12), new java.awt.Color(51,51,51)));
			jPanel3.add(getJTextArea(), null);
		}
		return jPanel3;
	}

	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private static JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			//jTextArea.setText("Enter Search String Here");
			jTextArea.setPreferredSize(new java.awt.Dimension(350,17));
		}
		return jTextArea;
	}

	/**
	 * This method initializes jPanel4	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private static JPanel getJPanel4() {
		if (jPanel4 == null) {
			jPanel4 = new JPanel();
			jPanel4.add(getJSearchButton(), null);
		}
		return jPanel4;
	}

	/**
	 * This method initializes jSearchButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private static JButton getJSearchButton() {
		if (jSearchButton == null) {
			jSearchButton = new JButton();
			jSearchButton.setText("Search");
			//jSearchButton.setAction(new SearchAction(this, opcat)) ;
			SearchAction action = new SearchAction("Search") ; 
			jSearchButton.addActionListener(action) ; 
		}
		return jSearchButton;
	}

	/**
	 * This method initializes jPanel5	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private static JPanel getJPanel5() {
		if (jPanel5 == null) {
			jPanel5 = new JPanel();
			jPanel5.setLayout(new BoxLayout(getJPanel5(), BoxLayout.Y_AXIS));
			jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search Using", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12), new java.awt.Color(51,51,51)));
			ButtonGroup group = new ButtonGroup();  
			group.add( getJRadioButton1());
			group.add( getJRadioButtonalgoInString()); 
			jPanel5.add(getJRadioButton1(), null);
			jPanel5.add(getJRadioButtonalgoInString(), null); 
		}
		return jPanel5;
	}

	/**
	 * This method initializes jRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private static JRadioButton getJRadioButtonalgoInString() {
		if (jRadioButtonalgoInString == null) {
			jRadioButtonalgoInString = new JRadioButton();
			jRadioButtonalgoInString.setText("In String Search");
			jRadioButtonalgoInString.setSelected(true);
		}
		return jRadioButtonalgoInString;
	}

	/**
	 * This method initializes jRadioButton1	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private static JRadioButton getJRadioButton1() {
		if (jRadioButton1 == null) {
			jRadioButton1 = new JRadioButton();
			jRadioButton1.setText("Reguler Expressions");
			jRadioButton1.setEnabled(false);
		}
		return jRadioButton1;
	}
	/** this is realy the options factory 
	 * 
	 * @return
	 */
	public static OptionsBase OptionsFactory() {
		
		OptionsBase options = null  ; 
		if (getJRadioButtonalgoInString().isSelected()) {
			options = new OptionsInString() ; 
			fillBaseOptions(options) ; 
			//fill here any specific options for in string			
		} else {
			//more algo's should define thier own options
		}
		
		return options ; 
	}
	
	private static void fillBaseOptions(OptionsBase options ) {
		options.forObjects = getJCheckBoxforObjects().isSelected() ; 
		options.forProcess = getJCheckBoxforProcesses().isSelected() ;
		options.forStates = getJCheckBoxforStates().isSelected() ; 
		options.inDescription = getJCheckBoxinDescription().isSelected() ; 
		options.inName = getJCheckBoxinName().isSelected() ; 
		options.setSearchText(getJTextArea().getText()) ; 
	}

	/**
	 * This method initializes jDialog	
	 * 	
	 * @return javax.swing.JDialog	
	 */
	private static JDialog getJDialog() {
		if (jDialog == null) {
			jDialog = new JDialog();
			jDialog.setSize(new java.awt.Dimension(377,316));
			jDialog.setTitle("Search Dialog");
			jDialog.setContentPane(getJContentPane1());
		}
		return jDialog;
	}

	/**
	 * This method initializes jContentPane1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private static JPanel getJContentPane1() {
		if (jContentPane1 == null) {
			jContentPane1 = new JPanel();
			jContentPane1.setLayout(new BorderLayout());
			jContentPane1.add(getJPanel3(), java.awt.BorderLayout.NORTH);
			jContentPane1.add(getJPanel4(), java.awt.BorderLayout.SOUTH);
			jContentPane1.add(getJPanel2(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane1;
	}

}  //  @jve:decl-index=0:visual-constraint="582,60"
