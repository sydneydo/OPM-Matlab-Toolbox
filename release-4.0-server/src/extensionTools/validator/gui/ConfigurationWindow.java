package extensionTools.validator.gui;

import extensionTools.validator.algorithm.AlgorithmConfiguration;
import gui.Opcat2;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * This class is a dialog box for <code>ProcessPropertiesDialog</code>, allowing
 * the user to edit the process name if it does not end with "ing".
 * The dialog box allow the user to skip the message the next time the situation
 * occurs.
 * @author Eran Toch
 */
public class ConfigurationWindow extends JDialog implements ComponentListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2201583947608117623L;
	private JPanel mess, p1; // tmp panels
	private JButton okButton, cancelButton;
	private JCheckBox do_is_a;
	private JCheckBox links_as_m;
	
	public final int CANCEL = 0;
	public final int OK = 1;
	public final int OK_AND_DONT_ASK_AGAIN = 2;
	/**
	 *  Constructor:
	 *  @param <code>parent</code> -- parent frame, Opcat2 application window or <code>null</code>
	 *  @param <code>pState OpdState</code> -- the OPD State to show properties dialog for.
	 */
	public ConfigurationWindow(String name)
	{
		super(Opcat2.getFrame(), "Process Name", true);
		this.addComponentListener(this);

		Container baseCont = this.getContentPane();
		baseCont.setLayout(new BorderLayout(0, 0));

		// Building the message (and checkbox) panel
		this.mess = new JPanel();
		this.mess.setLayout(new BoxLayout(this.mess, BoxLayout.Y_AXIS));
		this.mess.add(Box.createRigidArea(new Dimension(0,10)));
		this.mess.add(Box.createRigidArea(new Dimension(0,5)));
		this.do_is_a = new JCheckBox("Things inherit constraints from generalized things");
		this.do_is_a.setSelected(AlgorithmConfiguration.DO_IS_A);
		this.mess.add(this.do_is_a);
		this.mess.add(Box.createRigidArea(new Dimension(0,5)));

		this.links_as_m = new JCheckBox("The default cardinality of links (i.e. result) is \"many\"");
		this.links_as_m.setSelected(AlgorithmConfiguration.LINKS_AS_M);
		this.mess.add(this.links_as_m);
		
		this.mess.add(Box.createRigidArea(new Dimension(0,5)));

		//Building the buttons panel
		this.p1 = new JPanel();
		this.p1.setLayout(new BoxLayout(this.p1, BoxLayout.X_AXIS));
		this.p1.add(Box.createHorizontalGlue());
		//Building cancel button
		this.cancelButton = new JButton("Cancel");
		this.cancelButton.addActionListener(new CancelListener());
		this.p1.add(this.cancelButton);
		this.p1.add(Box.createRigidArea(new Dimension(10, 0)));

		//Building OK button
		this.okButton = new JButton("OK");
		this.okButton.addActionListener(new OkListener());
		this.getRootPane().setDefaultButton(this.okButton);
		this.p1.add(this.okButton);

		//Building a basic panel that holds the other two
		JPanel space = new JPanel(new BorderLayout(0, 0));
		space.add(this.mess, BorderLayout.CENTER);
		space.add(this.p1, BorderLayout.SOUTH);

		//Building a panel for the spaces to the right and to the left
		JPanel xlay = new JPanel();
		xlay.setLayout(new BoxLayout(xlay, BoxLayout.X_AXIS));
		xlay.add(Box.createRigidArea(new Dimension(10,0)));
		xlay.add(space);
		xlay.add(Box.createRigidArea(new Dimension(10,0)));

		//Building a panel for the spaces to the top and to the buttom
		JPanel ylay = new JPanel();
		ylay.setLayout(new BoxLayout(ylay, BoxLayout.Y_AXIS));
		ylay.add(Box.createRigidArea(new Dimension(0,10)));
		ylay.add(xlay);
		ylay.add(Box.createRigidArea(new Dimension(0,10)));

		//Adding all of it to the content pane
		baseCont.add(ylay, BorderLayout.CENTER);
		this.pack();
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		int fX = Opcat2.getFrame().getX();
		int fY = Opcat2.getFrame().getY();
		int pWidth = Opcat2.getFrame().getWidth();
		int pHeight = Opcat2.getFrame().getHeight();

		this.setLocation(fX + Math.abs(pWidth/2-this.getWidth()/2), fY + Math.abs(pHeight/2-this.getHeight()/2));
		this.setResizable(false);
	}

	/**
	 * Showing the dialog, and returning the output
	 * @return <code>ProcessNameDialog.CANCEL</code> if the user choose to edit
	 * the name again, <code>ProcessNameDialog.OK</code> if the user choose to
	 * let the name be and to repeat the message the next time.
	 * <code>ProcessNameDialog.OK_AND_DONT_ASK_AGAIN</code> if the user choose to
	 * let the name be and not to show it the next time. The last one is the
	 * default.
	 */
	public void showDialog()
	{
		this.setVisible(true);
	}

	public void componentShown(ComponentEvent e) {}
	public void componentHidden(ComponentEvent e){}
	public void componentMoved(ComponentEvent e){}
	public void componentResized(ComponentEvent e){}

	/**
	 * Action Listener for OK. Checks if the user wanted the message to be shown
	 * the next time or not, and return the value accodingly. See <code>showDialog</code>
	 * to see the details.
	 */
	class OkListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			AlgorithmConfiguration.DO_IS_A = ConfigurationWindow.this.do_is_a.isSelected();
			AlgorithmConfiguration.LINKS_AS_M = ConfigurationWindow.this.links_as_m.isSelected();
			(ConfigurationWindow.this).dispose();
			return;
		}
	}

	/**
	 * Action listener for Cancel.
	 */
	class CancelListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			(ConfigurationWindow.this).dispose();
		}
	}
}



