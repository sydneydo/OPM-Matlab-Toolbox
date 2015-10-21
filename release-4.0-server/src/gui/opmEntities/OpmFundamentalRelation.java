package gui.opmEntities;

/**
* The Base Class for all fundamental relations existing in OPM.
* For better understanding of this class you should be familiar with OPM.
*
* @version	2.0
* @author		Stanislav Obydionnov
* @author		Yevgeny   Yaroker
*/


public abstract class OpmFundamentalRelation extends OpmStructuralRelation
{

/**
* Creates an OpmFundamentalRelation with specified id and name, that relates sourceThing with destinationThing.
* Id of created OpmFundamentalRelation must be unique in OPCAT system.
*
* @param id OpmFundamentalRelation id
* @param name OpmFundamentalRelation name
* @param sourceThing source thing for this relation
* @param destinationThing destination thing for this relation
*/
  public 	OpmFundamentalRelation(long id,String name, OpmConnectionEdge sourceThing, OpmConnectionEdge destinationThing)
	{
		super(id,name,sourceThing, destinationThing);
	}
}

