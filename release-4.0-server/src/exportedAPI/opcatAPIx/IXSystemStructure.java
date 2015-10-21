package exportedAPI.opcatAPIx;
import java.util.Enumeration;

/**
 * IXSystemStructure is an interface to internal Opcat2 data structure, that
 * holds all System information.  See extenal documentation on system structure.
 */

public interface IXSystemStructure
{
	/**
	 * Returns IXOpd (an interface to OPD) according to given opdID.
	 * @param pKey - OPD ID.
	 * @return IXOpd according to given opdID.
	 */
	public IXOpd getIXOpd(long pKey);


	/**
	* Returns the IXEntry to which the specified key is mapped in this IXSystemStructure.
	* @param pKey a key in this IXSystemStructure
	* @return the IXEntry to which the key is mapped in this IXSystemStructure;
	* null if the key is not mapped to any Entry in this IXSystemStructure.
	*/
	public IXEntry getIXEntry(long pKey);

	/**
	* Returns an enumeration of all the IXEntries in user's system in this IXSystemStructure.
	* Use the Enumeration methods on the returned object to fetch the IXEntries sequentially
	*
	* @return Enumeration of the IXEntries in this IXSystemStructure
	*/
	public Enumeration getElements();

	/**
	* Returns an enumeration of the IXOpds in this IXSystemStructure.
	* Use the Enumeration methods on the returned object to fetch the IXOpd sequentially
	*
	* @return Enumeration of the IXOpd in this IXSystemStructure
	*/
	public Enumeration getOpds();


	/**
	* This function returns Enumaration of all IXInstances in the OPD specified by opdId.
	* @param opdId - ID of OPD to extract IXInstances from.
	* @return Enumeration of all IXInstances in the OPD.
	*/
	public Enumeration getInstancesInOpd(long opdId);

	/**
	* This function returns Enumeration of all IXLinkInstances in the OPD specified by opdId.
	* @param opdId - ID of OPD to extract IXLinkInstances from.
	* @return Enumeration of all IXLinkInstances in the OPD.
	*/
	public Enumeration getLinksInOpd(long opdId);

	/**
	* This function returns Enumeration of IXRelationInstances that are General Relations
	* in the OPD specified by opdId.
	* @param opdId - ID of OPD to extract IXRelationInstances from.
	* @return Enumeration of IXRelationInstances that are General Relations in the OPD.
	*/
	public Enumeration getGeneralRelationsInOpd(long opdId);


	/**
	* This function returns Enumeration of IXRelationInstances that are Fundamental Relations
	* in the OPD specified by opdId.
	* @param opdId - ID of OPD to extract IXRelationInstances from.
	* @return Enumeration of IXRelationInstances that are Fundamental Relations in the OPD.
	*/
	public Enumeration getFundamentalRelationsInOpd(long opdId);

	/**
	* This function returns an Enumeration of all IXThingInstances in the OPD specified by opdId
	* @param opdId - ID of OPD to extract IXThingInstances from.
	* @return Enumeration of all IXThingInstances in the OPD.
	*/
	public Enumeration getThingsInOpd(long opdId);
}
