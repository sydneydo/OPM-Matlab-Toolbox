package gui.projectStructure;

import exportedAPI.opcatAPI.IRelationEntry;
import exportedAPI.opcatAPIx.IXRelationEntry;
import gui.opdProject.OpdProject;
import gui.opmEntities.Constants;
import gui.opmEntities.OpmStructuralRelation;

/**
 * <p>
 * The base class for OPM structural relations.
 * 
 * @version 2.0
 * @author Stanislav Obydionnov
 * @author Yevgeny Yaroker
 * 
 */

public abstract class RelationEntry extends Entry implements IRelationEntry,
		IXRelationEntry {

	/**
	 * Creates RelationEntry that holds all information about specified pEntity.
	 * 
	 * @param pEntity
	 *            object of OpmEntity class.
	 */
	public RelationEntry(OpmStructuralRelation pEntity, OpdProject project) {
		super(pEntity, project);
	}

	public int getRelationType() {
		return Constants
				.getType4Relation((OpmStructuralRelation) this.logicalEntity);
	}

	/**
	 * Returns the source id of this OpmStructuralRelation.
	 * 
	 */
	public long getSourceId() {
		return ((OpmStructuralRelation) this.logicalEntity).getSourceId();
	}

	/**
	 * Returns the destination id of this OpmStructuralRelation.
	 * 
	 */

	public long getDestinationId() {
		return ((OpmStructuralRelation) this.logicalEntity).getDestinationId();
	}

	/**
	 * Returns String representing cardinality of the source.
	 * 
	 */
	public String getSourceCardinality() {
		return ((OpmStructuralRelation) this.logicalEntity)
				.getSourceCardinality();
	}

	/**
	 * Sets cardinality of the source.
	 * 
	 */
	public void setSourceCardinality(String cardinality) {
		((OpmStructuralRelation) this.logicalEntity)
				.setSourceCardinality(cardinality);
	}

	/**
	 * Returns String representing cardinality of the destination.
	 * 
	 */
	public String getDestinationCardinality() {
		return ((OpmStructuralRelation) this.logicalEntity)
				.getDestinationCardinality();
	}

	/**
	 * Sets cardinality of the source.
	 * 
	 */
	public void setDestinationCardinality(String cardinality) {
		((OpmStructuralRelation) this.logicalEntity)
				.setDestinationCardinality(cardinality);
	}

	/**
	 * Returns forward relation meaning of this OpmStructuralRelation.
	 * 
	 */
	public String getForwardRelationMeaning() {
		return ((OpmStructuralRelation) this.logicalEntity)
				.getForwardRelationMeaning();
	}

	/**
	 * Sets forward relation meaning of this OpmStructuralRelation.
	 * 
	 */
	public void setForwardRelationMeaning(String meaning) {
		((OpmStructuralRelation) this.logicalEntity)
				.setForwardRelationMeaning(meaning);
	}

	/**
	 * Returns backward relation meaning of this OpmStructuralRelation.
	 * 
	 */
	public String getBackwardRelationMeaning() {
		return ((OpmStructuralRelation) this.logicalEntity)
				.getBackwardRelationMeaning();
	}

	/**
	 * Sets backward relation meaning of this OpmStructuralRelation.
	 * 
	 */
	public void setBackwardRelationMeaning(String meaning) {
		((OpmStructuralRelation) this.logicalEntity)
				.setBackwardRelationMeaning(meaning);
	}

	public String getTypeString() {
		return "Relation";
	}
}
