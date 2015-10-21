package gui.opmEntities;

/**
* This Class represents condition link.
* For better understanding of this class you should be familiar with OPM.
*
* @version	2.0
* @author		Stanislav Obydionnov
* @author		Yevgeny   Yaroker
*/


public class OpmConditionLink extends OpmTransformationLink
{
/**
* Creates an OpmConditionLink with specified id and name. Id of created OpmConditionLink
* must be unique in OPCAT system
*
* @param id OpmConditionLink id
* @param name OpmConditionLink name
*/
	public OpmConditionLink(long linkId,String linkName,long sourceId, long destinationId)
	{
		super(linkId,linkName, sourceId, destinationId);
	}

}