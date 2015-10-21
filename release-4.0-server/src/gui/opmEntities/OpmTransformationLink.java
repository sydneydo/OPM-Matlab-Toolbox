package gui.opmEntities;

/**
* The Base Class for all tranformation links existing in OPM.
* For better understanding of this class you should be familiar with OPM.
*
* @version	2.0
* @author		Stanislav Obydionnov
* @author		Yevgeny   Yaroker
*/

public abstract class OpmTransformationLink extends OpmProceduralLink
{

/**
* Creates an OpmTransformationLink with specified id and name. Id of created OpmTransformationLink
* must be unique in OPCAT system
*
* @param id OpmTransformationLink id
* @param name OpmTransformationLink name
*/
	public OpmTransformationLink(long linkId,String linkName,long sourceId, long destinationId)
	{
		super(linkId,linkName, sourceId, destinationId);
	}

}
