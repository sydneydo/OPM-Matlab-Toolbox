package gui.opdProject.consistency.adding.rules;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;

import util.Configuration;

import gui.Opcat2;
import gui.opdGraphics.opdBaseComponents.OpdConnectionEdge;
import gui.opdProject.OpdProject;
import gui.opdProject.consistency.ConsistencyAbstractChecker;
import gui.opdProject.consistency.ConsistencyAction;
import gui.opdProject.consistency.ConsistencyCheckerInterface;
import gui.opdProject.consistency.ConsistencyOptions;
import gui.opdProject.consistency.ConsistencyResult;
import gui.opmEntities.OpmProceduralLink;
import gui.projectStructure.Instance;
import gui.projectStructure.LinkInstance;
import gui.projectStructure.ThingEntry;

public class LinkAddition extends ConsistencyAbstractChecker implements
	ConsistencyCheckerInterface {

    private boolean stop = false;

    public LinkAddition(ConsistencyOptions options, ConsistencyResult results) {
	super(options, results);
    }

    public boolean isStoping() {
	return stop;
    }

    public void check() {

	Iterator addedEnum = this.getMyOptions().getInstences();

	while (addedEnum.hasNext()) {
	    Instance addedIns = (Instance) addedEnum.next();
	    Object entry = addedIns.getEntry().getLogicalEntity();
	    if ((entry instanceof OpmProceduralLink)) {

		LinkInstance li = (LinkInstance) addedIns;
		OpmProceduralLink addedLink = (OpmProceduralLink) li.getEntry()
			.getLogicalEntity();

		if (addedLink != null) {

		    ArrayList opds = new ArrayList();
		    Instance locSourceInstance = null;
		    Instance locDestInstance = null;
		    // boolean isSourceMainEntityinOPD = false ;
		    // isSourceMainEntityinOPD =
		    // (this.getOpdByID(sourceID).getMainEntry().getId() ==
		    // sourceID);
		    Enumeration sourceEnum = li.getSourceInstance().getEntry()
			    .getInstances();
		    Enumeration destEnum;
		    while (sourceEnum.hasMoreElements()) {
			destEnum = li.getDestinationInstance().getEntry()
				.getInstances();
			locSourceInstance = (Instance) sourceEnum.nextElement();
			// debug.Print(locSourceInstance,
			// locSourceInstance.getKey().toString(),"1") ;
			long sourceID = locSourceInstance.getKey().getOpdId();
			while (destEnum.hasMoreElements()) {
			    locDestInstance = (Instance) destEnum.nextElement();
			    // debug.Print(locDestInstance, "dest - " +
			    // locDestInstance.getKey().toString(),"1") ;
			    long destID = locDestInstance.getKey().getOpdId();

			    if ((destID != OpdProject.CLIPBOARD_ID)
				    && (sourceID != OpdProject.CLIPBOARD_ID)) {
				if (destID == sourceID) {
				    long[] locOPDArray = new long[2];
				    locOPDArray[0] = sourceID;
				    locOPDArray[1] = destID;
				    opds.add(locOPDArray);
				}
			    }
			}
		    }

		    ThingEntry srcentry = null;
		    ThingEntry destentry = null;
		    boolean hasSrcZoomIn = false;
		    boolean hasDestZoomIn = false;
		    boolean consistencyFail = true;
		    boolean consistencySrcFail = true;
		    boolean consistencyDestFail = true;
		    OpdConnectionEdge destination = li.getDestination();
		    OpdConnectionEdge source = li.getSource();

		    if (destination.getEntry() instanceof ThingEntry) {
			destentry = (ThingEntry) destination.getEntry();
			hasDestZoomIn = (destentry.getZoomedInOpd() != null);
		    }
		    if ((source.getEntry() instanceof ThingEntry)) {
			srcentry = (ThingEntry) source.getEntry();
			hasSrcZoomIn = (srcentry.getZoomedInOpd() != null);
		    }

		    if ((srcentry != null) || (destentry != null)) {
			for (int i = 0; i < opds.size(); i++) {
			    long[] array = (long[]) opds.get(i);
			    long locSourceID = array[0];
			    long locDestID = array[1];
			    if ((srcentry != null) && (hasSrcZoomIn)) {
				consistencySrcFail = !((locSourceID == srcentry
					.getZoomedInOpd().getOpdId()));
			    }
			    if ((destentry != null) && (hasDestZoomIn)) {
				consistencyDestFail = !(locDestID == destentry
					.getZoomedInOpd().getOpdId());
			    }

			    consistencyFail = (consistencyDestFail && consistencySrcFail)
				    || (!hasSrcZoomIn && !hasDestZoomIn);

			    if (!consistencyFail)
				break;
			}

			// if we got to here then no result link was found
			// so we add a dummy action just to tell deploy that
			// it's not consistsent
			if (consistencyFail && (hasSrcZoomIn || hasDestZoomIn)) {
			    Instance[] insArray = new Instance[1];
			    insArray[0] = addedIns;
			    ConsistencyAction action = new ConsistencyAction(
				    ConsistencyAction.LINK_ADDITION, li
					    .getSourceInstance().getOpd(), li,
				    insArray);
			    this.getResults().setAction(action);
			    stop = true;
			    return;
			}

			/**
                         * here we can continue the check for more links.
                         */

		    }

		}
	    }
	}
    }

    public void deploy(ConsistencyResult checkResult) {
	Vector results = checkResult.getActions();
	for (int i = 0; i < results.size(); i++) {
	    ConsistencyAction action = (ConsistencyAction) results.get(i);
	    if (action.getMyType() == ConsistencyAction.LINK_ADDITION) {
		LinkInstance li = (LinkInstance) action.getMySourceInstance();

		if (stop) {
		    String copyMsg = "";
		    if (li.getDestination().getEntry() instanceof ThingEntry) {
			copyMsg = "Destination";
		    }
		    if ((li.getSource().getEntry() instanceof ThingEntry)) {
			copyMsg = "Source";
		    }
		    JOptionPane
			    .showMessageDialog(
				    this.getMyOptions().getProject()
					    .getMainFrame(),
				    copyMsg
					    + " is not found "
					    + "in the in-zoom OPD"
					    + "\n"
					    + "copy it to the in-zoom OPD before adding this link",
				    "OPCAT II error", JOptionPane.ERROR_MESSAGE);
		    Opcat2.getCurrentProject().deleteLink(li);
		    return;
		} else {

		}
	    }
	}
    }

    public boolean isDeploy() {
	String str = Configuration.getInstance().getProperty(
		"show_link_addition_rule");
	if ((str != null) && (str.equalsIgnoreCase("yes"))) {
	    return true;
	} else {
	    return false;
	}
    }

}
