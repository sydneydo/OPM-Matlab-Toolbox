package gui.opmEntities;

/**
* The Base Class for all enabling links existing in OPM.
* For better understanding of this class you should be familiar with OPM.
*
* @version	2.0
* @author		Stanislav Obydionnov
* @author		Yevgeny   Yaroker
*/

public abstract class OpmEnabler extends OpmProceduralLink
{
/**
* Creates an OpmEnabler with specified id and name. Id of created OpmEnabler
* must be unique in OPCAT system
*
* @param id OpmEnabler id
* @param name OpmEnabler name
*/
	public OpmEnabler(long linkId,String linkName,long sourceId, long destinationId)
	{
		super(linkId,linkName, sourceId, destinationId);
	}

}