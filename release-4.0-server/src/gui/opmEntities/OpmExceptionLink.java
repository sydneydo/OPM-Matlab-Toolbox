package gui.opmEntities;

/**
* This Class represents exception link.
* For better understanding of this class you should be familiar with OPM.
*
* @version	2.0
* @author		Stanislav Obydionnov
* @author		Yevgeny   Yaroker
*/

public class OpmExceptionLink extends OpmTransformationLink
{

/**
* Creates an OpmExceptionLink with specified id and name. Id of created OpmExceptionLink
* must be unique in OPCAT system
*
* @param id OpmExceptionLink id
* @param name OpmExceptionLink name
*/

	public OpmExceptionLink(long linkId,String linkName,long sourceId, long destinationId)
	{
		super(linkId,linkName, sourceId, destinationId);
	}

}