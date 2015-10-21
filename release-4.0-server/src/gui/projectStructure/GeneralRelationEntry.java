package gui.projectStructure;

import gui.opdProject.OpdProject;
import gui.opmEntities.OpmGeneralRelation;

import java.util.Enumeration;

/**
 * <p>
 * This class represents OPM general structural relations.
 * 
 * @version 2.0
 * @author Stanislav Obydionnov
 * @author Yevgeny Yaroker
 * 
 */

public class GeneralRelationEntry extends RelationEntry {

	/**
	 * Creates GeneralRelationEntry that holds all information about specified
	 * pRelation.
	 * 
	 * @param pRelation
	 *            object of OpmGeneralRelation class.
	 */

	public GeneralRelationEntry(OpmGeneralRelation pRelation, OpdProject project) {
		super(pRelation, project);
	}

	public void updateInstances() {

		for (Enumeration e = this.getInstances(); e.hasMoreElements();) {
			GeneralRelationInstance currentInstance = ((GeneralRelationInstance) (e
					.nextElement()));
			currentInstance.update();
		}

	}

	public String getTypeString() {
	    return "General Relation";
	}

}
