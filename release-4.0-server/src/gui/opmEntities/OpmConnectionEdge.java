package gui.opmEntities;

//-----------------------------------------------------------------
/**
* The Super Class for OpmThing and OpmState, both of them
* have common property - can be connected by links or relations, therefore
* they should have a common superclass.
* For better understanding of this class you should be familiar with OPM.
*
* @version	2.0
* @author		Stanislav Obydionnov
* @author		Yevgeny   Yaroker
*/

public abstract class OpmConnectionEdge extends OpmEntity
{

/**
* Creates an OpmConnectionEdge with specified id and name. Id of created OpmConnectionEdge
* must be unique in OPCAT system
*
* @param id OpmConnectionEdge id
* @param name OpmConnectionEdge name
*/

	public OpmConnectionEdge(long id,String name)
	{
		super(id, name);
	}
	

}