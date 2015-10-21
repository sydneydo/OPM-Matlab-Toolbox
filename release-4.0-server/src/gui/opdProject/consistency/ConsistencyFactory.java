package gui.opdProject.consistency;

import java.util.HashMap;
import gui.opdProject.OpdProject;
import gui.opdProject.consistency.adding.AdditionChecker;
import gui.opdProject.consistency.deleting.DeletionChecker;
import gui.opdProject.consistency.globel.GlobalChecker;
import gui.projectStructure.Instance;

public class ConsistencyFactory {

	private OpdProject myProject = null;

	private int myOP;

	private HashMap instances;

	private ConsistencyResult results;

	public ConsistencyFactory(Instance instance, OpdProject project, int OP,
			ConsistencyResult results) {
		this.myProject = project;
		this.myOP = OP;
		this.results = results;
		instances = new HashMap();
		instances.put(instance, instance);
	}
	
	public ConsistencyFactory(HashMap instances, OpdProject project, int OP,
			ConsistencyResult results) {
		this.myProject = project;
		this.myOP = OP;
		this.results = results;
		this.instances = instances;
	}

	public Object create() {

		ConsistencyOptions options = new ConsistencyOptions(this.myProject,
				instances, this.myOP);

		if (this.myOP == ConsistencyAction.DELETION) {
			return new DeletionChecker(options, this.results);
		}

		if (this.myOP == ConsistencyAction.ADDITION) {
			return new AdditionChecker(options, this.results);
		}
		
		if (this.myOP == ConsistencyAction.GLOBAL) {
			return new GlobalChecker(options, this.results);
		}

		return null;
	}

}
