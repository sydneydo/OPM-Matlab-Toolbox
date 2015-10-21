package gui.opmEntities;

/**
* This Class represents invocation link.
* For better understanding of this class you should be familiar with OPM.
*
* @version	2.0
* @author		Stanislav Obydionnov
* @author		Yevgeny   Yaroker
*/

public class OpmInvocationLink extends OpmTransformationLink
{

/**
* Creates an OpmInvocationLink with specified id and name. Id of created OpmInvocationLink
* must be unique in OPCAT system
*
* @param id OpmInvocationLink id
* @param name OpmInvocationLink name
*/

	public OpmInvocationLink(long linkId,String linkName,long sourceId, long destinationId)
	{
		super(linkId,linkName, sourceId, destinationId);
	}

}