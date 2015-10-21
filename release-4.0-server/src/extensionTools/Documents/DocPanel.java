package extensionTools.Documents;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import exportedAPI.opcatAPI.IInstance;
import exportedAPI.opcatAPI.ISystem;
import exportedAPI.opcatAPI.ISystemStructure;
import extensionTools.Documents.UserInterface.DocSel;
import extensionTools.Documents.UserInterface.HandleTemp;


public class DocPanel extends JPanel
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/**
	 * 
	 */
	 
ISystem mySys;
  Border border1;
  TitledBorder titledBorder1;
  String[] data = {"Template1", "Template2", "Template3", "Template4","Template5","Template6","Template7","Template8","Template9","Template10"};
  Border border2;
  TitledBorder titledBorder2;
  Border border3;
  TitledBorder titledBorder3;
  Border border4;
  TitledBorder titledBorder4;
  JPanel DocDocPanel = new JPanel();
  JButton DocNew = new JButton();
  JButton TempNew = new JButton();
  GridLayout gridLayout1 = new GridLayout();

	public DocPanel(ISystem sys)
	{
		this.mySys = sys;
		try {
			this.jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void jbInit() throws Exception {
    this.border1 = BorderFactory.createEtchedBorder(Color.white,new Color(142, 142, 142));
    this.titledBorder1 = new TitledBorder(this.border1,"First try of UML");

    this.border2 = BorderFactory.createEtchedBorder(Color.white,new Color(142, 142, 142));
    this.titledBorder2 = new TitledBorder(this.border2,"Please select a template:");
    this.border3 = BorderFactory.createEtchedBorder(Color.white,new Color(142, 142, 142));
    this.titledBorder3 = new TitledBorder(this.border3,"Handle Document:");
    this.border4 = BorderFactory.createEtchedBorder(Color.white,new Color(142, 142, 142));
    this.titledBorder4 = new TitledBorder(this.border4,"Handle Template:");
    this.setLayout(this.gridLayout1);

    this.DocDocPanel.setLayout(null);
    this.DocNew.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocPanel.this.DocNew_actionPerformed(e);
      }
    });
    this.DocNew.setBounds(new Rectangle(21, 8, 155, 30));
    this.DocNew.setText("Handle Document      ");
    this.TempNew.setBounds(new Rectangle(182, 8, 155, 30));
    this.TempNew.setText("Handle Template");
    this.TempNew.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocPanel.this.TempNew_actionPerformed(e);
      }
    });
    this.add(this.DocDocPanel, null);
    this.DocDocPanel.add(this.DocNew, null);
    this.DocDocPanel.add(this.TempNew, null);

	}

	class InstanceRepresenter
	{
		private IInstance myInst;
		private ISystemStructure myStruct;
		InstanceRepresenter(IInstance inst, ISystemStructure struct)
		{
			this.myInst = inst;
			this.myStruct = struct;
		}
		public String toString()
		{
			return this.myInst.getIEntry().getName()+ " - " +this.myStruct.getIOpd(this.myInst.getKey().getOpdId()).getName();
		}

		public IInstance getIInstance()
		{
			return this.myInst;
		}
	}


  void DocNew_actionPerformed(ActionEvent e) {
      DocSel new_doc = new DocSel(this.mySys);
      new_doc.setVisible(true);
  }

  void TempNew_actionPerformed(ActionEvent e) {
      HandleTemp ht=new HandleTemp(this.mySys);
      ht.setVisible(true);
  }

}