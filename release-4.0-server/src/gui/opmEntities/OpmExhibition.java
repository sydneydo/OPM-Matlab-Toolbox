package gui.opmEntities;

/**
* This class represents featuring-characterization relation.
* For better understanding of this class you should be familiar with OPM.
*
* @version	2.0
* @author		Stanislav Obydionnov
* @author		Yevgeny   Yaroker
*/

public class OpmExhibition extends OpmFundamentalRelation
{
/**
* Creates an OpmFeaturing with specified id and name, that relates sourceThing with destinationThing. Id of created OpmFeaturing
* must be unique in OPCAT system.
*
* @param id OpmFeaturing id
* @param name OpmFeaturing name
* @param sourceThing source thing for this relation
* @param destinationThing destination thing for this relation
*/
  	public 	OpmExhibition(long id,String name, OpmConnectionEdge sourceThing, OpmConnectionEdge destinationThing)
	{
		super(id,name, sourceThing, destinationThing);
	}
}