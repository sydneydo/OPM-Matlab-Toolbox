package extensionTools.opltoopd;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * Represents the GUI of OPLtoOPD window
 */
public class OPLComboBox extends JDialog {
	 

	/**
	 * 
	 */
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates the main dialog and assigns it with controller.
	 * 
	 * @param aOPLComboBoxController
	 *            The main controller of the application.
	 */
	public OPLComboBox(OPLComboBoxController aOPLComboBoxController) {
		super(aOPLComboBoxController.getSystem().getMainFrame(), true);
		this.oPLComboBoxController = aOPLComboBoxController;
		this.setContentPane(this.createMainPanel());
		this.addListeners();
		this.pack();

		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		int fX = aOPLComboBoxController.getSystem().getMainFrame().getX();
		int fY = aOPLComboBoxController.getSystem().getMainFrame().getY();
		int pWidth = aOPLComboBoxController.getSystem().getMainFrame()
				.getWidth();
		int pHeight = aOPLComboBoxController.getSystem().getMainFrame()
				.getHeight();

		this.setLocation(fX + Math.abs(pWidth / 2 - this.getWidth() / 2), fY
				+ Math.abs(pHeight / 2 - this.getHeight() / 2));

		this.setResizable(false);
		this.setTitle("Add new OPL sentence");
	}

	private JPanel ComboBoxPanel() {

		JLabel patternLabel2 = new JLabel();
		JLabel patternLabel1 = new JLabel();
		patternLabel1
				.setText("<html><center><font color='navy' face='Tahoma' size='4'><b>&nbsp;&nbsp;&nbsp; -----Add new OPL sentence----- &nbsp;&nbsp;&nbsp;</b></font></center></html>");
		patternLabel2
				.setText("<html><center><font color='navy' face='Tahoma' size='4'><b>&nbsp;&nbsp;&nbsp; ------------------------------------- &nbsp;&nbsp;&nbsp;</b></font></center></html>");
		this.patternList = new JComboBox(this.patternExamples);

		this.currentPattern = this.patternExamples[0];
		this.patternList.setEditable(false);
		this.patternList.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel patternPanel = new JPanel();
		patternPanel.setLayout(new BoxLayout(patternPanel, BoxLayout.Y_AXIS));
		patternPanel.add(this.patternList);
		patternPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		return patternPanel;
	}

	private JPanel ResultPanel() {
		this.result.setForeground(Color.black);
		this.result.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createLineBorder(Color.black), BorderFactory
				.createEmptyBorder(5, 5, 5, 5)));
		JPanel resultPanel = new JPanel();
		resultPanel.setLayout(new GridLayout(0, 1));
		resultPanel.add(this.result);
		return resultPanel;
	}

	private JPanel createButtonsPanel() {
		this.buttonsPanel = new UpdateCancelButtonsPanel();
		this.buttonsPanel.setLayout(new BoxLayout(this.buttonsPanel, BoxLayout.X_AXIS));
		this.buttonsPanel.setBorder(BorderFactory.createCompoundBorder());
		return this.buttonsPanel;
	}

	private JPanel createMainPanel() {
		this.mainPanel = new JPanel();
		this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
		this.mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		this.mainPanel.add(this.ComboBoxPanel());

		this.mainPanel.add(Box.createRigidArea(new Dimension(10, 10)));
		this.mainPanel.add(this.ResultPanel());
		this.mainPanel.add(Box.createRigidArea(new Dimension(10, 10)));
		this.mainPanel.add(this.createButtonsPanel());

		return this.mainPanel;

	}

	/**
	 * Returns the <code>OPLtoOPD window</code> of this system.
	 */
	public JPanel getPanel() {
		return this.mainPanel;
	}

	private void addListeners() {
		KeyListener kListener = new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					OPLComboBox.this.oPLComboBoxController.addButtonPressed();
					return;
				}

				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					OPLComboBox.this.dispose();
					return;
				}

			}
		};
		this.addKeyListener(kListener);

		ComponentAdapter cListener = new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				OPLComboBox.this.result.requestFocus();
			}
		};

		this.addComponentListener(cListener);

		this.patternList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OPLComboBox.this.oPLComboBoxController.comboChused(e);
			}
		});
		this.buttonsPanel.getAddButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OPLComboBox.this.oPLComboBoxController.addButtonPressed();
			}
		});
		this.buttonsPanel.getExitButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OPLComboBox.this.dispose();
				return;
			}
		});

		this.buttonsPanel.getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OPLComboBox.this.oPLComboBoxController.cancelButtonPressed();
			}
		});
		this.buttonsPanel.getUpdateButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OPLComboBox.this.oPLComboBoxController.updateButtonPressed();
			}
		});
	}

	JComboBox patternList;

	JPanel mainPanel;

	public JTextField result = new JTextField();

	public UpdateCancelButtonsPanel buttonsPanel;

	private OPLComboBoxController oPLComboBoxController;

	String currentPattern;

	public String[] patternExamples = { "(Add new sentence)" };
}
