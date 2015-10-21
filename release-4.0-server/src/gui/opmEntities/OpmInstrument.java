package gui.opmEntities;

/**
* This Class represents instrument link.
* For better understanding of this class you should be familiar with OPM.
*
* @version	2.0
* @author		Stanislav Obydionnov
* @author		Yevgeny   Yaroker
*/



public class OpmInstrument extends OpmEnabler
{
/**
* Creates an OpmInstrument with specified id and name. Id of created OpmInstrument
* must be unique in OPCAT system
*
* @param id OpmInstrument id
* @param name OpmInstrument name
*/
	public OpmInstrument(long linkId,String linkName,long sourceId, long destinationId)
	{
		super(linkId,linkName, sourceId, destinationId);
	}

}