package gui.opmEntities;

/**
* This Class represents event link.
* For better understanding of this class you should be familiar with OPM.
*
* @version	2.0
* @author		Stanislav Obydionnov
* @author		Yevgeny   Yaroker
*/


public class OpmInstrumentEventLink extends OpmTransformationLink
{
/**
* Creates an OpmEventLink with specified id and name. Id of created OpmEventLink
* must be unique in OPCAT system
*
* @param id OpmEventLink id
* @param name OpmEventLink name
*/
	public OpmInstrumentEventLink(long linkId,String linkName,long sourceId, long destinationId)
	{
		super(linkId,linkName, sourceId, destinationId);
	}

}