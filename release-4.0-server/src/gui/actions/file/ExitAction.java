package gui.actions.file;

import gui.controls.GuiControl;

import java.awt.event.ActionEvent;
import javax.swing.Icon;

/**
 * Exits from Opcat, after asking the user to save the current project and
 * performing other cleaning tasks.
 * 
 * @author Eran Toch
 */
public class ExitAction extends FileAction {

    /**
         * 
         */
    private static final long serialVersionUID = 1L;

    /**
         * 
         */

    public ExitAction(String name, Icon icon) {
	super(name, icon);
    }

    public void actionPerformed(ActionEvent e) {
	GuiControl gui = GuiControl.getInstance();
	gui.getGlassPane().setVisible(true);
	gui.getGlassPane().start();
	this.file._exit();
	gui.getGlassPane().stop();
    }
}
