package gui.opdGraphics.popups;

import gui.projectStructure.OrInstance;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;



public class OrPopup  extends JPopupMenu
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/**
	 * 
	 */
	 
private OrInstance  myOr;

	public OrPopup(OrInstance myOr, int x, int y)
	{
		super();
		this.myOr = myOr;

		ButtonGroup myGroup = new ButtonGroup();
		JMenuItem temp;

		if (myOr.isOr())
		{
			temp = new JRadioButtonMenuItem("XOR", false);
		}
		else
		{
			temp = new JRadioButtonMenuItem("XOR", true);
		}

		temp.addActionListener(this.ChangeAction);
		myGroup.add(temp);
		this.add(temp);
		if (myOr.isOr())
		{
			temp = new JRadioButtonMenuItem("OR", true);
		}
		else
		{
			temp = new JRadioButtonMenuItem("OR", false);
		}

		temp.addActionListener(this.ChangeAction);
		myGroup.add(temp);
		this.add(temp);

	}

	ActionListener ChangeAction = new ActionListener(){
		public void actionPerformed(ActionEvent e){

			String command = e.getActionCommand();

			if (command.equals("XOR"))
			{
				OrPopup.this.myOr.setOr(false);
			}
			else
			{
				OrPopup.this.myOr.setOr(true);
			}

            OrPopup.this.myOr.update();
		}
	};

}