package gui.opmEntities;

/**
* This class represents generalization-specialization relation.
* For better understanding of this class you should be familiar with OPM.
*
* @version	2.0
* @author		Stanislav Obydionnov
* @author		Yevgeny   Yaroker
*/

public class OpmSpecialization extends OpmFundamentalRelation
{
/**
* Creates an OpmGeneralization with specified id and name, that relates sourceThing with destinationThing.
* Id of created OpmGeneralization must be unique in OPCAT system.
*
* @param id OpmGeneralization id
* @param name OpmGeneralization name
* @param sourceThing source thing for this relation
* @param destinationThing destination thing for this relation
*/
  public 	OpmSpecialization(long id,String name, OpmConnectionEdge sourceThing, OpmConnectionEdge destinationThing)
	{
		super(id,name, sourceThing, destinationThing);
	}
}