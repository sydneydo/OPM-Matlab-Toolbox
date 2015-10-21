package gui.opmEntities;

/**
* The Base Class for all structural relations existing in OPM.
* For better understanding of this class you should be familiar with OPM.
*
* @version	2.0
* @author		Stanislav Obydionnov
* @author		Yevgeny   Yaroker
*/


public abstract class OpmStructuralRelation extends OpmEntity
{
//---------------------------------------------------------------------------
// The private attributes/members are located here
	//	private char relation_type;
	private long sourceId;
	private long destinationId;
	private String sourceCardinality;
	private String destinationCardinality;
	private String forwardRelationMeaning;
	private String backwardRelationMeaning;

/**
* Creates an OpmStructuralRelation with specified id and name, that relates sourceThing with destinationThing. Id of created OpmStructuralRelation
* must be unique in OPCAT system.
*
* @param id OpmStructuralRelation id
* @param name OpmStructuralRelation name
* @param sourceThing source thing for this relation
* @param destinationThing destination thing for this relation
*/
	public OpmStructuralRelation(long relationId,String relationName, OpmConnectionEdge sourceThing, OpmConnectionEdge destinationThing)
	{
		super(relationId,relationName);
		this.sourceCardinality = "1";
		this.destinationCardinality = "1";
		this.forwardRelationMeaning = "";
		this.backwardRelationMeaning = "";
		this.sourceId = sourceThing.getId();
		this.destinationId = destinationThing.getId();
	}

    public void copyPropsFrom(OpmStructuralRelation origin)
    {
        super.copyPropsFrom(origin);
		this.sourceCardinality = origin.getSourceCardinality();
		this.destinationCardinality = origin.getDestinationCardinality();
		this.forwardRelationMeaning = origin.getForwardRelationMeaning();
		this.backwardRelationMeaning = origin.getBackwardRelationMeaning();
    }


/**
* Returns the source id of this OpmStructuralRelation.
*
*/
	public long getSourceId()
	{
		return this.sourceId;
	}

/**
* Sets the source id of this OpmStructuralRelation.
*
*/
//	public void setSourceId(long id)
//	{
//		sourceId = id;
//	}

/**
* Returns the destination id of this OpmStructuralRelation.
*
*/

	public long getDestinationId()
	{
		return this.destinationId;
	}

/**
* Sets the destination id of this OpmStructuralRelation.
*
*/
//	public void setDestinationId(long id)
//	{
//		destinationId = id;
//	}

/**
* Returns String representing cardinality of the source.
*
*/
	public String getSourceCardinality()
	{
		return this.sourceCardinality;
	}

/**
* Sets cardinality of the source.
*
*/
	public void setSourceCardinality(String cardinality)
	{
		this.sourceCardinality = cardinality;
	}

/**
* Returns String representing cardinality of the destination.
*
*/
	public String getDestinationCardinality()
	{
		return this.destinationCardinality;
	}

/**
* Sets cardinality of the source.
*
*/
	public void setDestinationCardinality(String cardinality)
	{
		this.destinationCardinality = cardinality;
	}


/**
* Returns forward relation meaning of this OpmStructuralRelation.
*
*/
	public String getForwardRelationMeaning()
	{
		return this.forwardRelationMeaning;
	}

/**
* Sets forward relation meaning of this OpmStructuralRelation.
*
*/
	public void setForwardRelationMeaning(String meaning)
	{
		this.forwardRelationMeaning = meaning;
	}

/**
* Returns backward relation meaning of this OpmStructuralRelation.
*
*/
	public String getBackwardRelationMeaning()
	{
		return this.backwardRelationMeaning;
	}

/**
* Sets backward relation meaning of this OpmStructuralRelation.
*
*/
	public void setBackwardRelationMeaning(String meaning)
	{
		this.backwardRelationMeaning = meaning;
	}

}