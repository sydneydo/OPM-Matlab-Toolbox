package gui.actions.edit;

import gui.Opcat2;
import gui.controls.EditControl;
import gui.controls.FileControl;
import gui.images.standard.StandardImages;
import gui.opdGraphics.GraphicsUtils;
import gui.opdProject.Opd;
import gui.opdProject.StateMachine;
import gui.opdGraphics.opdBaseComponents.OpdThing;
import gui.projectStructure.Entry;
import gui.projectStructure.Instance;
import gui.projectStructure.ThingInstance;
import gui.util.OpcatLogger;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.w3c.dom.events.EventException;

/**
 * Carries out a copy operation.
 * 
 * @author Eran Toch
 */
public class PasteAction extends EditAction {

    /**
         * 
         */
    private static final long serialVersionUID = 1L;

    /**
         * 
         */

    public PasteAction(String name, Icon icon) {
	super(name, icon);
    }

    /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
    public void actionPerformed(ActionEvent arg0) {

	this.edit = EditControl.getInstance();

	if (this.edit.IsCutPending()) {
	    // ask the user as there is no undo for this
	    int cancel = JOptionPane.showConfirmDialog(Opcat2.getFrame(),
		    "Are you sure, This action can not be undone",
		    "Opcat2 - Message", JOptionPane.YES_NO_OPTION);
	    if (cancel == JOptionPane.NO_OPTION) {
		return;
	    }
	}

	try {
	    super.actionPerformed(arg0);
	} catch (EventException e) {
	    JOptionPane.showMessageDialog(this.gui.getFrame(), e.getMessage()
		    .toString(), "Message", JOptionPane.ERROR_MESSAGE);
	    return;
	}

	MetaHideAction hide = new MetaHideAction("XXX", null, false, false, StandardImages.System_Icon);
	hide.actionPerformed(null);

	MetaColoringAction color = new MetaColoringAction("xxx", null, false, StandardImages.META_COLOR_DEF);
	color.actionPerformed(null);

	Thread runner = new Thread() {
	    public void run() {
		//PasteAction.this.gui.startWaitingMode(
		//	"Paste from Clipboard...", true);
		try {
		    PasteAction.this.doPaste();
		} catch (Exception e) {
		    OpcatLogger.logError(e);
		    FileControl file = FileControl.getInstance();
		    JOptionPane.showMessageDialog(file.getCurrentProject()
			    .getMainFrame(),
			    "Paste had failed becasue of the following error:\n"
				    + e.getMessage(), "Message",
			    JOptionPane.ERROR_MESSAGE);
		} 
	    }

	};
	runner.start();

    }

    private void doPaste() {

	gui.getGlassPane().setVisible(true);
	gui.getGlassPane().start();

	int x;
	int y;

	Opd opd = null;
	Entry entry = null;
	Instance instance = null;
	OpdThing thing = null;

	if (GraphicsUtils.getLastMouseEvent() == null) {
	    x = 100;
	    y = 100;

	} else {
	    x = GraphicsUtils.getLastMouseEvent().getX();
	    y = GraphicsUtils.getLastMouseEvent().getY();
	}

	Point showPoint = new Point(x, y);
	x = (int) showPoint.getX();
	y = (int) showPoint.getY();

	opd = this.edit.getCurrentProject().getCurrentOpd();
	try {
	    /**
                 * TODO: this check should go into th echeckmoudole package.
                 */
	    instance = opd.getSelectedItem();
	    entry = instance.getEntry();
	    thing = ((ThingInstance) entry.getInstance(instance.getKey()))
		    .getThing();
	    if (!(thing.isZoomedIn())) {
		JOptionPane.showMessageDialog(Opcat2.getFrame(),
			"Can not Paste inside an un-zoomed thing",
			"Opcat2 - Error", JOptionPane.ERROR_MESSAGE);
		gui.getGlassPane().stop();
		return;
	    }
	} catch (Exception e) {
	    thing = null;
	}

	if (thing == null) {
	    this.edit.getCurrentProject().paste(x, y, opd.getDrawingArea());
	} else {
	    this.edit.getCurrentProject().paste(x, y, thing);

	}

	// this.edit.updateLogicalStructureChange();
	// opd.getDrawingArea().repaint();
	edit.getCurrentProject().deselectAll();
	StateMachine.reset(true);
	StateMachine.reset();
	gui.getRepository().rebuildTrees(true);

	gui.getGlassPane().stop();

    }
}
