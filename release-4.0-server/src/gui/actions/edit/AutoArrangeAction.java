package gui.actions.edit;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.IdentityHashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.w3c.dom.events.EventException;

import extensionTools.opcatLayoutManager.autoArrange.AutoArranger;

import gui.controls.EditControl;
import gui.images.standard.StandardImages;
import gui.opdGraphics.lines.OpdLine;
import gui.opdProject.Opd;
import gui.projectStructure.ConnectionEdgeInstance;
import gui.projectStructure.FundamentalRelationInstance;
import gui.projectStructure.GeneralRelationInstance;
import gui.projectStructure.GraphicalRelationRepresentation;
import gui.projectStructure.Instance;
import gui.projectStructure.LinkInstance;
import gui.projectStructure.StateInstance;

public class AutoArrangeAction extends EditAction {

    /**
         * 
         */
    private static final long serialVersionUID = 1L;

    private Opd myOpd = null;

    public AutoArrangeAction(Opd opd) {
	super("Auto Arrange", StandardImages.EMPTY);
	myOpd = opd;
    }

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

	AutoArranger.arrange(getList());
	gui.getGlassPane().stop();

    }

    private ArrayList getList() {

	// this "identity map" will store all GraphicalRelationRepresentation's
	// already encountered (to avoid adding them more than once)
	IdentityHashMap<GraphicalRelationRepresentation, Object> graphRelReprMap = new IdentityHashMap<GraphicalRelationRepresentation, Object>();

	OpdLine[] lineArray = new OpdLine[5];

	// collect all relevant schema elements (i.e. all elements belonging
	// to the *current* schema
	ArrayList arrangedEntitlesList = new ArrayList();
	Enumeration<Instance> instEnum = EditControl.getInstance()
		.getCurrentProject().getSystemStructure().getInstancesInOpd(
			myOpd);

	while (instEnum.hasMoreElements()) {

	    Instance o = instEnum.nextElement();

	    // add the appropriate "aranged entities" (based on their type)
	    // to the list
	    if (o instanceof ConnectionEdgeInstance) {
		// states are handled as connection edges of lines, there is
		// no need to check states separately from their parent objects
		if (!(o instanceof StateInstance)) {
		    // a ConnectionEdgeInstance - add its "ConnectionEdge"
		    arrangedEntitlesList.add(((ConnectionEdgeInstance) o)
			    .getConnectionEdge());
		}
	    } else if (o instanceof LinkInstance) {
		// a LinkInstance - add all "DraggerPoint"s as nodes and
		// "OpdLine"s as lines
		LinkInstance li = (LinkInstance) o;

		arrangedEntitlesList.addAll(li.getLine().getPointsVector());
		arrangedEntitlesList.addAll(li.getLine().getLinesVector());
	    } else if (o instanceof FundamentalRelationInstance) {
		FundamentalRelationInstance fri = (FundamentalRelationInstance) o;
		// store the GraphicalRelationRepresentation - they will all
		// be taken care of later on
		graphRelReprMap.put(fri.getGraphicalRelationRepresentation(),
			null);

		// store the lines from this FundamentalRelationInstance
		int i, j = 0;
		for (i = 0; i < lineArray.length; i++) {
		    if (fri.getLine(i) == null)
			break;

		    if ((j == 0)
			    || (fri.getLine(i).getEdge1() != lineArray[j - 1]
				    .getEdge1())
			    || (fri.getLine(i).getEdge2() != lineArray[j - 1]
				    .getEdge2()))
			lineArray[j++] = fri.getLine(i);
		}

		if (j > 0) {
		    OpdLine[] oneLineArray = new OpdLine[j];
		    System.arraycopy(lineArray, 0, oneLineArray, 0, i);

		    arrangedEntitlesList.add(oneLineArray);
		}
	    } else if (o instanceof GeneralRelationInstance) {
		GeneralRelationInstance gri = (GeneralRelationInstance) o;

		arrangedEntitlesList.addAll(gri.getLine().getPointsVector());
		arrangedEntitlesList.addAll(gri.getLine().getLinesVector());
	    } else {
		throw new RuntimeException("unknown instance in scheme: "
			+ o.getClass());
	    }
	}

	// finally, add the GraphicalRelationRepresentation's - objects
	// together with the lines
	Iterator grrIter = graphRelReprMap.keySet().iterator();
	while (grrIter.hasNext()) {
	    GraphicalRelationRepresentation grr = (GraphicalRelationRepresentation) grrIter
		    .next();

	    // add the "triangle" itself
	    arrangedEntitlesList.add(grr.getRelation());

	    // add the lines from this GraphicalRelationRepresentation
	    int i, j = 0;
	    for (i = 0; i < lineArray.length; i++) {
		if (grr.getLine(i) == null)
		    break;

		if ((j == 0)
			|| (grr.getLine(i).getEdge1() != lineArray[j - 1]
				.getEdge1())
			|| (grr.getLine(i).getEdge2() != lineArray[j - 1]
				.getEdge2()))
		    lineArray[j++] = grr.getLine(i);
	    }

	    if (j > 0) {
		OpdLine[] oneLineArray = new OpdLine[j];
		System.arraycopy(lineArray, 0, oneLineArray, 0, i);

		arrangedEntitlesList.add(oneLineArray);
	    }
	}

	return arrangedEntitlesList;
    }

}
