package gui.actions.edit;

import gui.Opcat2;
import gui.images.standard.StandardImages;
import gui.opdProject.Opd;

import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.w3c.dom.events.EventException;

/**
 * Carries out a copy operation.
 * 
 * @author Eran Toch
 */
public class CopyAction extends EditAction {

    /**
         * 
         */

    /**
         * 
         */
    private static final long serialVersionUID = 1L;

    private Opd toOpd = null;

    /**
         * @param name
         * @param icon
         */
    public CopyAction(String name, Icon icon) {
	super(name, icon);
	toOpd = null;
    }

    /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
    public void actionPerformed(ActionEvent arg0) {
	try {
	    super.actionPerformed(arg0);
	} catch (EventException e) {
	    JOptionPane.showMessageDialog(this.gui.getFrame(), e.getMessage()
		    .toString(), "Message", JOptionPane.ERROR_MESSAGE);
	    return;
	}

	MetaHideAction hide = new MetaHideAction("XXX", null, false,
		StandardImages.System_Icon);
	hide.actionPerformed(null);

	MetaColoringAction color = new MetaColoringAction("xxx", null,
		StandardImages.META_COLOR_DEF);
	color.actionPerformed(null);

	if (toOpd == null) {
	    this.edit.getCurrentProject().copy();
	} else {
	    this.edit.getCurrentProject().clearClipBoard();
	    String message = this.edit.getCurrentProject()._copy(this.edit.getCurrentProject()
		    .getCurrentOpd(), toOpd, 0, 0, toOpd.getDrawingArea(),
		    false);
	    if (message != null) {
		JOptionPane.showMessageDialog(Opcat2.getFrame(), message,
			"Opcat2 - Message", JOptionPane.ERROR_MESSAGE);
	    }
	}
    }

    public void setToOpd(Opd toOpd) {
	this.toOpd = toOpd;
    }
}
