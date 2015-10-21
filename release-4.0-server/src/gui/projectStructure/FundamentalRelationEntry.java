package gui.projectStructure;

import gui.opdProject.OpdProject;
import gui.opmEntities.OpmFundamentalRelation;

import java.util.Enumeration;

/**
 * <p>
 * This class represents OPM fundamental structural relations.
 * 
 * @version 2.0
 * @author Stanislav Obydionnov
 * @author Yevgeny Yaroker
 * 
 */

public class FundamentalRelationEntry extends RelationEntry {

	/**
	 * Creates FundamentalRelationEntry that holds all information about
	 * specified pRelation.
	 * 
	 * @param pRelation
	 *            object of OpmFundamentalRelation class.
	 */
	public FundamentalRelationEntry(OpmFundamentalRelation pRelation,
			OpdProject project) {
		super(pRelation, project);
	}

	public void updateInstances() {
		for (Enumeration e = this.getInstances(); e.hasMoreElements();) {
			FundamentalRelationInstance tempInstance = (FundamentalRelationInstance) (e
					.nextElement());
			tempInstance.update();
		}

	}

	public String getTypeString() {
	    return "Fundamental Relation";
	}
}
