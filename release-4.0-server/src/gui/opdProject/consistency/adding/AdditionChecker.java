package gui.opdProject.consistency.adding;

import java.util.Vector;

import gui.opdProject.Opd;
import gui.opdProject.consistency.ConsistencyAbstractChecker;
import gui.opdProject.consistency.ConsistencyCheckerInterface;
import gui.opdProject.consistency.ConsistencyOptions;
import gui.opdProject.consistency.ConsistencyResult;
import gui.opdProject.consistency.adding.rules.InZommedInsideAddition;
import gui.opdProject.consistency.adding.rules.InZommedOutsideAddition;
import gui.opdProject.consistency.adding.rules.InstrumentLinkAdding;
import gui.opdProject.consistency.adding.rules.LinkAddition;
import gui.opdProject.consistency.adding.rules.SDAdding;

public class AdditionChecker extends ConsistencyAbstractChecker implements ConsistencyCheckerInterface{
	
	private Vector checkers = new Vector() ;  
	private boolean active = true ; 
	boolean cancled = false ; 

	public AdditionChecker (ConsistencyOptions options, ConsistencyResult results) {
		super(options, results) ; 
	}
	
	public boolean isStoping() {
		return false;
	}

	public void check() { 
		
		/**
		 * put the stoping checkers first. 
		 */
		ConsistencyCheckerInterface checker = new LinkAddition(this.getMyOptions(), this.getResults()) ; 
		checker.check();
		this.checkers.add(checker) ;		
		if(checker.isStoping()) return ; 
		
		
		/**
		 * end of stoping checkers
		 */
		
		checker = new InZommedOutsideAddition(this.getMyOptions(), this.getResults()) ; 
		checker.check() ;
		this.checkers.add(checker) ;

		checker = new SDAdding(this.getMyOptions(), this.getResults()) ; 
		checker.check();
		this.checkers.add(checker) ;
		
		checker = new InZommedInsideAddition(this.getMyOptions(), this.getResults()) ; 
		checker.check();
		this.checkers.add(checker) ;
		
		checker = new InstrumentLinkAdding(this.getMyOptions(), this.getResults()) ; 
		checker.check();
		this.checkers.add(checker) ;				
		
	}

	public void deploy(ConsistencyResult checkResult) {
		Opd opd = this.getMyOptions().getProject().getCurrentOpd() ; 
		for (int i = 0 ; i < this.checkers.size(); i++) {
			ConsistencyCheckerInterface checker = (ConsistencyCheckerInterface) this.checkers.get(i) ; 
			if(checker.isDeploy()) {
				checker.deploy(checkResult) ;
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
		this.active = active ; 
		
	}

	public void setDeploy(boolean deploy) {
		
	}

}
