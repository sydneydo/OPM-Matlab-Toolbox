package gui.opmEntities;

/**
* The Base Class for all general relations existing in OPM.
* For better understanding of this class you should be familiar with OPM.
*
* @version	2.0
* @author		Stanislav Obydionnov
* @author		Yevgeny   Yaroker
*/


public abstract class OpmGeneralRelation extends OpmStructuralRelation
{

/**
* Creates an OpmGeneralRelation with specified id and name, that relates sourceThing with destinationThing.
* Id of created OpmGeneralRelation must be unique in OPCAT system.
*
* @param id OpmGeneralRelation id
* @param name OpmGeneralRelation name
* @param sourceThing source thing for this relation
* @param destinationThing destination thing for this relation
*/

  public  OpmGeneralRelation(long id,String name,OpmConnectionEdge sourceThing, OpmConnectionEdge destinationThing)
  {
	super(id,name, sourceThing, destinationThing);
  }

}