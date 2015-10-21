package gui.opdProject.consistency.deleting;

import java.util.Vector;

import gui.images.standard.StandardImages;
import gui.opdProject.consistency.ConsistencyAction;
import gui.opdProject.consistency.ConsistencyAbstractChecker;
import gui.opdProject.consistency.ConsistencyCheckerInterface;
import gui.opdProject.consistency.ConsistencyDeployDialog;
import gui.opdProject.consistency.ConsistencyOptions;
import gui.opdProject.consistency.ConsistencyResult;
import gui.opdProject.consistency.deleting.rules.InzoomedDeletion;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;

/**
 * This class is used to check the consistency of deleting a Instance of for now
 * the only rule implemented is this -
 * 
 * rule 1) when deleting, see if the deleted object is outside an zoomed-in opd
 * if yes then return all the OPD's in which the pair (deleted, in-zoomed) are.
 * and state a deletion action for the deleted instance in each of this opd's
 * 
 * 
 * @author raanan
 * 
 */

public class DeletionChecker extends ConsistencyAbstractChecker implements
	ConsistencyCheckerInterface {
    private boolean isCancelPressed = false;

    public boolean isCancelPressed() {
	return isCancelPressed;
    }

    boolean cancled = false;

    private Vector checkers = new Vector();

    public DeletionChecker(ConsistencyOptions options, ConsistencyResult results) {
	super(options, results);
    }

    public boolean isStoping() {
	return false;
    }

    public void check() {

	/**
         * TODO: get all classes in Rules dir ? use the extension tools
         * installer class to see how to do it which start with the word Rule
         */

	// end load rules
	ConsistencyAbstractChecker checker = new InzoomedDeletion(this
		.getMyOptions(), this.getResults());
	checker.check();
	this.checkers.add(checker);
	// checkResults will collect all the actions and every deploy will use
        // it's own ?

    }

    public void deploy(ConsistencyResult checkResult) {

	for (int i = 0; i < this.checkers.size(); i++) {
	    ConsistencyAbstractChecker checker = (ConsistencyAbstractChecker) this.checkers
		    .get(i);
	    if (checker.isDeploy()) {
		checker.deploy(checkResult);
		return;
	    }
	}

	if (checkResult == null) {
	    return;
	}

	OpdProject project = this.getMyOptions().getProject();

	// run here the deletion deploy GUI and actions
	ConsistencyDeployDialog deploy = new ConsistencyDeployDialog("Delete");
	deploy.setAlwaysOnTop(true);
	deploy.setModal(true);
	deploy.setLocationRelativeTo(project.getMainFrame());
	deploy.setText("Delete ?");
	deploy.setTextIcon(StandardImages.DELETE);

	// now loop on all the actions in the results
	Vector results = checkResult.getActions();
	for (int i = 0; i < results.size(); i++) {
	    if (!deploy.isCancelPressed() && !deploy.isWindowClosing()) {
		ConsistencyAction action = (ConsistencyAction) results.get(i);
		Opd opd = action.getMyOpd();
		project.showOPD(opd.getOpdId());
		opd.removeSelection();
		action.getMySourceInstance().setSelected(true);
		Vector v = new Vector();
		v.add(action.getMySourceInstance());
		if (!deploy.isAutoPressed()) {
		    deploy.setVisible(true);
		}
		if (deploy.isOkPressed() || deploy.isAutoPressed()) {
		    project.delete(v.elements(), OpdProject.DELETE);
		}
		opd.removeSelection();
		deploy.setOkPressed(false);
		deploy.setNextPresed(false);
	    } else {
		break;
	    }
	}
	deploy.setAutoPressed(false);
	deploy.setCancelPressed(false);
	deploy.setWindowClosing(false);

    }

    public boolean isDeploy() {
	return true;
    }

}
