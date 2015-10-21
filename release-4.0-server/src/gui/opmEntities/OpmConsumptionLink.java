package gui.opmEntities;

/**
* This Class represents consumption link.
* For better understanding of this class you should be familiar with OPM.
*
* @version	2.0
* @author		Stanislav Obydionnov
* @author		Yevgeny   Yaroker
*/

public class OpmConsumptionLink extends OpmTransformationLink
{

/**
* Creates an OpmConsumptionLink with specified id and name. Id of created OpmConsumptionLink
* must be unique in OPCAT system
*
* @param id OpmConsumptionLink id
* @param name OpmConsumptionLink name
*/

	public OpmConsumptionLink(long linkId,String linkName,long sourceId, long destinationId)
	{
		super(linkId,linkName, sourceId, destinationId);
	}

}