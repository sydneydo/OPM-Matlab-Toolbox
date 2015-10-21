package exportedAPI.opcatAPI;
import java.util.Enumeration;
/**
 * ISystemStructure is an interface to internal Opcat2 data structure, that
 * holds all System information. See extenal documentation on system structure.
 */

public interface ISystemStructure
{

	/**
	 * Returns IOpd (an interface to OPD) according to given opdID.
	 * @param pKey - OPD ID.
	 * @return IOpd according to given opdID.
	 */
	public IOpd getIOpd(long pKey);


	/**
	* Returns the IEntry to which the specified key is mapped in this ISystemStructure.
	* @param pKey a key in this IXSystemStructure
	* @return the IEntry to which the key is mapped in this ISystemStructure;
	* null if the key is not mapped to any Entry in this ISystemStructure.
	*/
	public IEntry getIEntry(long pKey);

	/**
	* Returns an enumeration of all the IEntries in user's system in this ISystemStructure.
	* Use the Enumeration methods on the returned object to fetch the IEntries sequentially
	*
	* @return an enumeration of the IEntries in this ISystemStructure
	*/
	public Enumeration getElements();

	/**
	* Returns an enumeration of the IOpds in this ISystemStructure.
	* Use the Enumeration methods on the returned object to fetch the IOpd sequentially
	*
	* @return an enumeration of the IOpd in this ISystemStructure
	*/
	public Enumeration getOpds();


	/**
	* This function returns Enumaration of all IInstances in the OPD specified by opdId.
	* @param opdId - ID of OPD to extract IInstances from.
	* @return Enumaration of all IInstances in the OPD.
	*/
	public Enumeration getInstancesInOpd(long opdId);

	/**
	* This function returns Enumeration of all ILinkInstances in the OPD specified by opdId.
	* @param opdId - ID of OPD to extract ILinkInstances from.
	* @return Enumaration of all ILinkInstances in the OPD.
	*/
	public Enumeration getLinksInOpd(long opdId);

	/**
	* This function returns Enumeration of IRelationInstances that are General Relations
	* in the OPD specified by opdId.
	* @param opdId - ID of OPD to extract IRelationInstances from.
	* @return Enumaration of IRelationInstances that are General Relations in the OPD.
	*/
	public Enumeration getGeneralRelationsInOpd(long opdId);


	/**
	* This function returns Enumeration of IRelationInstances that are Fundamental Relations
	* in the OPD specified by opdId.
	* @param opdId - ID of OPD to extract IRelationInstances from.
	* @return Enumaration of IRelationInstances that are Fundamental Relations in the OPD.
	*/
	public Enumeration getFundamentalRelationsInOpd(long opdId);

	/**
	* This function returns an Enumeration of all IThingInstances in the OPD specified by opdId
	* @param opdId - ID of OPD to extract IThingInstances from.
	* @return Enumeration of all IThingInstances in the OPD.
	*/
	public Enumeration getThingsInOpd(long opdId);
	
}
