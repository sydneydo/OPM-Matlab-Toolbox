package gui.opdGraphics.dialogs;

import gui.Opcat2;

import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.JToggleButton;


public class UnfoldingDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JPanel jPanelUnfolding = null;

    private JPanel jPanelOptions = null;

    private JPanel jPanelButtons = null;

    private JButton jButtonCancel = null;

    private JButton jButtonOK = null;

    private JToggleButton jToggleButtonBringObjects = null;

    private JToggleButton jToggleButtonBringProcesses = null;

    private JToggleButton jToggleButtonBringRole = null;

    private JToggleButton jToggleButtonBringAllLevels = null;
    
    private boolean cancled = true ;
    private boolean ok = false ; 
    private boolean bringObjects = true ; 
    private boolean bringProcesses = true ; 
    private boolean bringRole = true ; 
    private boolean allLevels = true ; 

    /**
         * @param owner
         */
    public UnfoldingDialog(Frame owner) {
	super(owner);
	initialize();
    }

    /**
         * This method initializes this
         * 
         * @return void
         */
    private void initialize() {
	this.setSize(219, 213);
	this.setModal(true);
	this.setTitle("Unfolding Options");
	this.setContentPane(getJPanelUnfolding());
	this.setAlwaysOnTop(true);
	this.setLocationRelativeTo(Opcat2.getFrame());
    }

    /**
         * This method initializes jPanelUnfolding
         * 
         * @return javax.swing.JPanel
         */
    private JPanel getJPanelUnfolding() {
	if (jPanelUnfolding == null) {
	    jPanelUnfolding = new JPanel();
	    jPanelUnfolding.setLayout(new BorderLayout());
	    jPanelUnfolding.add(getJPanelOptions(), BorderLayout.CENTER);
	    jPanelUnfolding.add(getJPanelButtons(), BorderLayout.SOUTH);
	}
	return jPanelUnfolding;
    }

    /**
         * This method initializes jPanelOptions
         * 
         * @return javax.swing.JPanel
         */
    private JPanel getJPanelOptions() {
	if (jPanelOptions == null) {
	    GridLayout gridLayout = new GridLayout();
	    gridLayout.setColumns(1);
	    gridLayout.setHgap(0);
	    gridLayout.setRows(5);
	    gridLayout.setVgap(1);
	    jPanelOptions = new JPanel();
	    jPanelOptions.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	    jPanelOptions.setLayout(gridLayout);
	    jPanelOptions.add(getJToggleButtonBringObjects(), null);
	    jPanelOptions.add(getJToggleButtonBringProcesses(), null);
	    jPanelOptions.add(getJToggleButtonBringRole(), null);
	    jPanelOptions.add(getJToggleButtonBringAllLevels(), null);
	    jPanelOptions.add(Box.createVerticalBox());
	}
	return jPanelOptions;
    }

    /**
         * This method initializes jPanelButtons
         * 
         * @return javax.swing.JPanel
         */
    private JPanel getJPanelButtons() {
	if (jPanelButtons == null) {
	    jPanelButtons = new JPanel();
	    jPanelButtons.setLayout(new BoxLayout(getJPanelButtons(),
		    BoxLayout.X_AXIS));
	    jPanelButtons.setBorder(BorderFactory
		    .createEtchedBorder(EtchedBorder.RAISED));
	    jPanelButtons.add(Box.createHorizontalGlue());
	    jPanelButtons.add(getJButtonCancel(), null);
	    jPanelButtons.add(Box.createHorizontalGlue());
	    jPanelButtons.add(getJButtonOK(), null);
	    jPanelButtons.add(Box.createHorizontalGlue());
	}
	return jPanelButtons;
    }

    /**
         * This method initializes jButtonCancel
         * 
         * @return javax.swing.JButton
         */
    private JButton getJButtonCancel() {
	if (jButtonCancel == null) {
	    jButtonCancel = new JButton();
	    jButtonCancel.setText("Cancel");
	    jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent e) {
	    	cancled = true ; 
	    	ok = false ; 
	    	UnfoldingDialog.this.setVisible(false) ; 
	        }
	    });
	}
	return jButtonCancel;
    }

    /**
         * This method initializes jButtonOK
         * 
         * @return javax.swing.JButton
         */
    private JButton getJButtonOK() {
	if (jButtonOK == null) {
	    jButtonOK = new JButton();
	    jButtonOK.setText("OK");
	    jButtonOK.setMnemonic(KeyEvent.VK_UNDEFINED);
	    jButtonOK.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent e) {
		    	cancled = false ; 
		    	ok = true ; 
		    	UnfoldingDialog.this.setVisible(false) ; 
	        }
	    });
	}
	return jButtonOK;
    }

    /**
         * This method initializes jToggleButtonBringObjects
         * 
         * @return javax.swing.JToggleButton
         */
    private JToggleButton getJToggleButtonBringObjects() {
	if (jToggleButtonBringObjects == null) {
	    jToggleButtonBringObjects = new JToggleButton();
	    jToggleButtonBringObjects.setText("Bring Objects");
	    jToggleButtonBringObjects.setAlignmentX(Component.CENTER_ALIGNMENT);
	    jToggleButtonBringObjects.setPreferredSize(new Dimension(0, 0));
	    jToggleButtonBringObjects.setMaximumSize(null);
	    jToggleButtonBringObjects.addItemListener(new java.awt.event.ItemListener() {
	        public void itemStateChanged(java.awt.event.ItemEvent e) {
	    	if(jToggleButtonBringObjects.isSelected()) {
	    	jToggleButtonBringObjects.setText("Don't bring Objects");
	    	bringObjects = false ; 
	    	} else {
	    	jToggleButtonBringObjects.setText("Bring Objects");
	    	bringObjects = true ; 
	    	}
	        }
	    });
	}
	return jToggleButtonBringObjects;
    }

    /**
         * This method initializes jToggleButtonBringProcesses
         * 
         * @return javax.swing.JToggleButton
         */
    private JToggleButton getJToggleButtonBringProcesses() {
	if (jToggleButtonBringProcesses == null) {
	    jToggleButtonBringProcesses = new JToggleButton();
	    jToggleButtonBringProcesses.setText("Bring Processes");
	    jToggleButtonBringProcesses.setAlignmentX(Component.CENTER_ALIGNMENT);
	    jToggleButtonBringProcesses.setPreferredSize(new Dimension(0, 0));
	    jToggleButtonBringProcesses.setMaximumSize(null);
	    jToggleButtonBringProcesses.addItemListener(new java.awt.event.ItemListener() {
	        public void itemStateChanged(java.awt.event.ItemEvent e) {
	    	if(jToggleButtonBringProcesses.isSelected()) {
	    	jToggleButtonBringProcesses.setText("Don't bring Processes");
	    	bringProcesses = false ; 
	    	} else {
	    	jToggleButtonBringProcesses.setText("Bring Processes");
	    	bringProcesses = true ; 
	    	}
	        }
	    });
	}
	return jToggleButtonBringProcesses;
    }

    /**
         * This method initializes jToggleButtonBringRole
         * 
         * @return javax.swing.JToggleButton
         */
    private JToggleButton getJToggleButtonBringRole() {
	if (jToggleButtonBringRole == null) {
	    jToggleButtonBringRole = new JToggleButton();
	    jToggleButtonBringRole.setMnemonic(KeyEvent.VK_UNDEFINED);
	    jToggleButtonBringRole.setPreferredSize(new Dimension(0, 0));
	    jToggleButtonBringRole.setAlignmentX(Component.CENTER_ALIGNMENT);
	    jToggleButtonBringRole.setText("Bring Role related Things");
	    jToggleButtonBringRole.setMaximumSize(null);
	    jToggleButtonBringRole.addItemListener(new java.awt.event.ItemListener() {
	        public void itemStateChanged(java.awt.event.ItemEvent e) {
	    	if(jToggleButtonBringRole.isSelected()) {
	    	jToggleButtonBringRole.setText("Don't Bring Role related Things");
	    	bringRole = false ; 
	    	} else {
	    	jToggleButtonBringRole.setText("Bring Role related Things");
	    	bringRole = true ; 
	    	}
	        }
	    });
	}
	return jToggleButtonBringRole;
    }

    /**
         * This method initializes jToggleButtonBringAllLevels
         * 
         * @return javax.swing.JToggleButton
         */
    private JToggleButton getJToggleButtonBringAllLevels() {
	if (jToggleButtonBringAllLevels == null) {
	    jToggleButtonBringAllLevels = new JToggleButton();
	    jToggleButtonBringAllLevels.setText("Bring all levels");
	    jToggleButtonBringAllLevels
		    .addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent e) {
			    if (jToggleButtonBringAllLevels.isSelected()) {
				jToggleButtonBringAllLevels
					.setText("Bring one level");
				allLevels = false ; 
			    } else {
				jToggleButtonBringAllLevels
					.setText("Bring all levels");
				allLevels = false ; 
			    }
			}
		    });
	}
	return jToggleButtonBringAllLevels;
    }

    public boolean isAllLevels() {
        return allLevels;
    }

    public boolean isBringObjects() {
        return bringObjects;
    }

    public boolean isBringProcesses() {
        return bringProcesses;
    }

    public boolean isBringRole() {
        return bringRole;
    }

    public boolean isCancled() {
        return cancled;
    }

    public boolean isOk() {
        return ok;
    }

} // @jve:decl-index=0:visual-constraint="291,68"
