package gui.opdProject.consistency.adding.rules;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import exportedAPI.OpdKey;
import gui.images.standard.StandardImages;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.opdProject.consistency.ConsistencyAction;
import gui.opdProject.consistency.ConsistencyAbstractChecker;
import gui.opdProject.consistency.ConsistencyCheckerInterface;
import gui.opdProject.consistency.ConsistencyDeployDialog;
import gui.opdProject.consistency.ConsistencyOptions;
import gui.opdProject.consistency.ConsistencyResult;
import gui.projectStructure.Entry;
import gui.projectStructure.Instance;
import gui.projectStructure.ProcessInstance;
import gui.projectStructure.ThingInstance;

/**
 * this rule is for adding a thing inside a zoomed in OPD but outside the main
 * entity of the OPD.
 * 
 * @author raanan
 * 
 */
public class InZommedInsideAddition extends ConsistencyAbstractChecker
	implements ConsistencyCheckerInterface {
    
    private boolean deploy = true;

    public InZommedInsideAddition(ConsistencyOptions options,
	    ConsistencyResult results) {
	super(options, results);
    }

    public boolean isStoping() {
	return false;
    }

    public void check() {

	OpdProject project = this.getMyOptions().getProject();

	Iterator addedEnum = this.getMyOptions().getInstences();
	while (addedEnum.hasNext()) {
	    Instance addedIns = (Instance) addedEnum.next();
	    // now search project
	    Entry entry = project.getCurrentOpd().getMainEntry();
	    if ((entry != null) && (addedIns instanceof ThingInstance)) {
		Enumeration mainEnum = entry.GetInZoomedIncludedInstances(
			project.getCurrentOpd()).elements();
		while (mainEnum.hasMoreElements()) {
		    entry = ((Instance) mainEnum.nextElement()).getEntry();
		    Enumeration insEnum = entry.getInstances();
		    while (insEnum.hasMoreElements()) {
			ThingInstance main = (ThingInstance) insEnum
				.nextElement();
			if (main == null) {
			    continue;
			}

			if (main.getOpd() == null) {
			    continue;
			}
			if ((main.getOpd().getOpdId() != OpdProject.CLIPBOARD_ID)) {
			    if (main instanceof ProcessInstance) {
				Opd mainOpd = main.getOpd();
				// there is a main instance in this opd and this
				// is not the OPD we started with
				if ((mainOpd.getMainInstance() != null)
					&& (mainOpd.getOpdId() != addedIns
						.getOpd().getOpdId())) {
				    // if the maininstance in main's OPD
                                        // equals
				    // main then we must add the new thing
                                        // here
				    // also
				    if ((main.getKey().getEntityInOpdId() == mainOpd
					    .getMainInstance().getKey()
					    .getEntityInOpdId())) {
					Instance[] insArray = new Instance[1];
					insArray[0] = main;
					ConsistencyAction action = new ConsistencyAction(
						ConsistencyAction.THING_INZOMMED_INSIDE_ADDITION,
						main.getOpd(), addedIns,
						insArray);
					this.getResults().setAction(action);
				    }
				}
			    }
			}
		    }
		}
	    }

	}
    }

    public void deploy(ConsistencyResult checkResult) {
	OpdProject project = this.getMyOptions().getProject();
	Hashtable OPDS = new Hashtable();

	// run here the deletion deploy GUI and actions
	ConsistencyDeployDialog deploy = new ConsistencyDeployDialog("Adding inside the main entity Rule");
	deploy.setAlwaysOnTop(true);
	deploy.setModal(true);
	deploy.setLocationRelativeTo(project.getMainFrame());
	deploy.setText("Add in this OPD ?");
	deploy.setTextIcon(StandardImages.NEW);

	// now loop on all the actions in the results
	Vector results = checkResult.getActions();
	for (int i = 0; i < results.size(); i++) {
	    ConsistencyAction action = (ConsistencyAction) results.get(i);
	    if (action.getMyType() == ConsistencyAction.THING_INZOMMED_INSIDE_ADDITION) {
		if (!deploy.isCancelPressed() && !deploy.isWindowClosing()) {
		    Opd opd = action.getMyOpd();
		    if (OPDS.get(new Long(opd.getOpdId())) == null) {
			OPDS.put(new Long(opd.getOpdId()), opd);
			project.showOPD(opd.getOpdId());
			opd.removeSelection();
			action.getMySourceInstance().setSelected(true);
			Vector v = new Vector();
			v.add(action.getMySourceInstance());
			if (!deploy.isAutoPressed()) {
			    deploy.setVisible(true);
			}
			if (deploy.isOkPressed() || deploy.isAutoPressed()) {
			    // project.delete(v.elements(),
                                // OpdProject.DELETE);
			    OpdKey key = new OpdKey(opd.getOpdId(), action
				    .getMySourceInstance().getEntry().getId());
			    if (!this.getResults().isDeployed(key)) {
				Instance newIns = null;
				Instance ins = action.getMySourceInstance();
				ins.getOpd().getSelection().removeSelection();
				ins.getOpd().getSelection().addSelection(ins,
					true);
				project._copy(ins.getOpd(), opd, 100, 100, opd
					.getDrawingArea(), false);
				Iterator iter = Collections.list(
					ins.getEntry().getInstances())
					.iterator();
				while (iter.hasNext()) {
				    newIns = (Instance) iter.next();
				}
				this.getResults().setDeployed(newIns);
			    }
			}
			opd.removeSelection();
		    }
		    deploy.setOkPressed(false);
		    deploy.setNextPresed(false);
		} else {
		    break;
		}
	    }
	}
	deploy.setAutoPressed(false);
	deploy.setCancelPressed(false);
	deploy.setWindowClosing(false);

    }

    public boolean isDeploy() {
	return this.deploy;
    }

}
