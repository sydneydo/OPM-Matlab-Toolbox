package gui.opdProject.consistency.globel;

import java.util.Vector;

import gui.opdProject.Opd;
import gui.opdProject.consistency.ConsistencyAbstractChecker;
import gui.opdProject.consistency.ConsistencyCheckerInterface;
import gui.opdProject.consistency.ConsistencyOptions;
import gui.opdProject.consistency.ConsistencyResult;
import gui.opdProject.consistency.globel.rules.ThingInZoomCheckerReport;

public class GlobalChecker extends ConsistencyAbstractChecker implements
		ConsistencyCheckerInterface {

	private Vector checkers = new Vector();

	private boolean active = true;

	boolean cancled = false;

	public GlobalChecker(ConsistencyOptions options, ConsistencyResult results) {
		super(options, results);
	}

	public boolean isStoping() {
		return false;
	}

	public void check() {

		/**
		 * put the stoping checkers first.
		 */

		/**
		 * end of stoping checkers
		 */
		ConsistencyCheckerInterface checker = new ThingInZoomCheckerReport(this
				.getMyOptions(), this.getResults());
		checker.check();
		this.checkers.add(checker);

	}

	public void deploy(ConsistencyResult checkResult) {
		Opd opd = this.getMyOptions().getProject().getCurrentOpd();
		for (int i = 0; i < this.checkers.size(); i++) {
			ConsistencyCheckerInterface checker = (ConsistencyCheckerInterface) this.checkers
					.get(i);
			if (checker.isDeploy()) {
				checker.deploy(checkResult);
			}
		}
		this.getMyOptions().getProject().showOPD(opd.getOpdId());

	}

	public boolean isActive() {
		return this.active;
	}

	public boolean isDeploy() {
		return true;
	}

	public void setActive(boolean active) {
		this.active = active;

	}

	public void setDeploy(boolean deploy) {

	}

}
