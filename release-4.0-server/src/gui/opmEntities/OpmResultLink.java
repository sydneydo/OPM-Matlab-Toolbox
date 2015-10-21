package gui.opmEntities;

/**
* This Class represents result link.
* For better understanding of this class you should be familiar with OPM.
*
* @version	2.0
* @author		Stanislav Obydionnov
* @author		Yevgeny   Yaroker
*/


public class OpmResultLink extends OpmTransformationLink
{
/**
* Creates an OpmResultLink with specified id and name. Id of created OpmResultLink
* must be unique in OPCAT system
*
* @param id OpmResultLink id
* @param name OpmResultLink name
*/
	public OpmResultLink(long linkId,String linkName,long sourceId, long destinationId)
	{
		super(linkId,linkName, sourceId, destinationId);
	}

}