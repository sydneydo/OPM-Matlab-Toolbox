package gui.opmEntities;

/**
* This Class represents bi directional general relation.
* For better understanding of this class you should be familiar with OPM.
*
* @version	2.0
* @author		Stanislav Obydionnov
* @author		Yevgeny   Yaroker
*/

public class OpmBiDirectionalRelation extends OpmGeneralRelation
{

/**
* Creates an OpmBiDirectionalRelation with specified id and name, that relates sourceThing with destinationThing.
* Id of created OpmBiDirectionalRelation must be unique in OPCAT system.
*
* @param id OpmBiDirectionalRelation id
* @param name OpmBiDirectionalRelation name
* @param sourceThing source thing for this relation
* @param destinationThing destination thing for this relation
*/
  public 	OpmBiDirectionalRelation(long id,String name, OpmConnectionEdge sourceThing, OpmConnectionEdge destinationThing)
	{
		super(id,name, sourceThing, destinationThing);
	}

}