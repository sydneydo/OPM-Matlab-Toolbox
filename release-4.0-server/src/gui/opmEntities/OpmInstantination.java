package gui.opmEntities;

/**
* This class represents classification-instantination relation.
* For better understanding of this class you should be familiar with OPM.
*
* @version	2.0
* @author		Stanislav Obydionnov
* @author		Yevgeny   Yaroker
*/

public class OpmInstantination extends OpmFundamentalRelation
{
/**
* Creates an OpmInstantination with specified id and name, that relates sourceThing with destinationThing.
* Id of created OpmInstantination must be unique in OPCAT system.
*
* @param id OpmInstantination id
* @param name OpmInstantination name
* @param sourceThing source thing for this relation
* @param destinationThing destination thing for this relation
*/
  	public 	OpmInstantination(long id,String name, OpmConnectionEdge sourceThing, OpmConnectionEdge destinationThing)
	{
		super(id,name, sourceThing, destinationThing);
	}
}