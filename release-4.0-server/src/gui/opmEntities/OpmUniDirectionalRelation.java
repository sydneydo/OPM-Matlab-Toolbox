package gui.opmEntities;

/**
* This Class represents uni directional general relation.
* For better understanding of this class you should be familiar with OPM.
*
* @version	2.0
* @author		Stanislav Obydionnov
* @author		Yevgeny   Yaroker
*/

public class OpmUniDirectionalRelation extends OpmGeneralRelation
{

/**
* Creates an OpmUniDirectionalRelation with specified id and name, that relates sourceThing with destinationThing.
* Id of created OpmUniDirectionalRelation must be unique in OPCAT system.
*
* @param id OpmUniDirectionalRelation id
* @param name OpmUniDirectionalRelation name
* @param sourceThing source thing for this relation
* @param destinationThing destination thing for this relation
*/

  public 	OpmUniDirectionalRelation(long id,String name, OpmConnectionEdge sourceThing, OpmConnectionEdge destinationThing)
	{
		super(id,name, sourceThing, destinationThing);
	}

}