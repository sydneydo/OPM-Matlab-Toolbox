
package gui.opmEntities;

/**
* This Class represents agent link.
* For better understanding of this class you should be familiar with OPM.
*
* @version	2.0
* @author		Stanislav Obydionnov
* @author		Yevgeny   Yaroker
*/

public class OpmAgent extends OpmEnabler
{
/**
* Creates an OpmAgent with specified id and name. Id of created OpmAgent
* must be unique in OPCAT system
*
* @param id OpmAgent id
* @param name OpmAgent name
*/
	public OpmAgent(long linkId,String linkName,long sourceId, long destinationId)
	{
		super(linkId,linkName, sourceId, destinationId);
	}

}