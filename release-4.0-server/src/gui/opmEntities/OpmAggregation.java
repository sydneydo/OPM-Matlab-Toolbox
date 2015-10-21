package gui.opmEntities;

/**
* This class represents Aggregation-Participation relation.
* For better understanding of this class you should be familiar with OPM.
*
* @version	2.0
* @author		Stanislav Obydionnov
* @author		Yevgeny   Yaroker
*/

public class OpmAggregation extends OpmFundamentalRelation
{
/**
* Creates an OpmAggregation with specified id and name, that relates sourceThing with destinationThing. Id of created OpmAggregation
* must be unique in OPCAT system.
*
* @param id OpmAggregation id
* @param name OpmAggregation name
* @param sourceThing source thing for this relation
* @param destinationThing destination thing for this relation
*/
  public 	OpmAggregation(long id,String name, OpmConnectionEdge sourceThing, OpmConnectionEdge destinationThing)
	{
		super(id,name, sourceThing, destinationThing);
	}
}
