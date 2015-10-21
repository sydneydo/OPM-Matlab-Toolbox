package gui.actions.edit;

import gui.opdProject.Opd;
import gui.projectStructure.ConnectionEdgeEntry;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import org.w3c.dom.events.EventException;

public class CompleteLinksinOPD extends EditAction {

    private static final long serialVersionUID = 1L;

    /**
         * @param name
         * @param icon
         */
    
    private boolean addMissingThings = false ;  
    public CompleteLinksinOPD(String name, Icon icon, boolean addMissingThings) {
	super(name, icon);
	this.addMissingThings = addMissingThings ; 
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
	
	gui.getGlassPane().setVisible(true);
	gui.getGlassPane().start();

	completeLinks(true);
	completeLinks(false);

	gui.getGlassPane().stop() ; 
    }

    private void completeLinks(boolean isSource) {

	/**
         * get Things in OPD and connect all missing links.
         * 
         */
	/**
         * Algorithem - go over all the things in the Current OPD get all source
         * links and destination links. go over the things connected to those
         * links see if they are in this OPD. if yes then connect with the
         * connection if the link is not in the OPD.
         */

	long opdID = edit.getCurrentProject().getCurrentOpd().getOpdId();

	Opd opd = edit.getCurrentProject().getCurrentOpd();

	ArrayList<ConnectionEdgeEntry> entries = edit.getCurrentProject()
		.getSystemStructure().getConnetionEdgeEntriesInOpd(opdID);

	for (ConnectionEdgeEntry entry : entries) {
	    
	    entry.completeLinks(opd, isSource, addMissingThings,null) ; 

	}
    }
}
