package gui.opmEntities;

/**
* This Class represents effect link.
* For better understanding of this class you should be familiar with OPM.
*
* @version	2.0
* @author		Stanislav Obydionnov
* @author		Yevgeny   Yaroker
*/

public class OpmEffectLink extends OpmTransformationLink
{

/**
* Creates an OpmEffectLink with specified id and name. Id of created OpmEffectLink
* must be unique in OPCAT system
*
* @param id OpmEffectLink id
* @param name OpmEffectLink name
*/
	public OpmEffectLink(long linkId,String linkName,long sourceId, long destinationId)
	{
		super(linkId,linkName, sourceId, destinationId);
	}

}