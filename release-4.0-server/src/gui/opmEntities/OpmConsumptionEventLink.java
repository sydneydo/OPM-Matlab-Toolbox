package gui.opmEntities;

/**
* This Class represents event link.
* For better understanding of this class you should be familiar with OPM.
*
* @version	2.0
* @author		Stanislav Obydionnov
* @author		Yevgeny   Yaroker
*/


public class OpmConsumptionEventLink extends OpmTransformationLink
{
/**
* Creates an OpmInstrumentEventLink with specified id and name. Id of created OpmEventLink
* must be unique in OPCAT system
*
* @param id OpmInstrumentEventLink id
* @param name OpmInstrumentEventLink name
*/
	public OpmConsumptionEventLink(long linkId,String linkName,long sourceId, long destinationId)
	{
		super(linkId,linkName, sourceId, destinationId);
	}

}