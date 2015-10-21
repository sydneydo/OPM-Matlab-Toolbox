package extensionTools.validator.algorithm;

import exportedAPI.opcatAPI.IConnectionEdgeEntry;
import gui.opdProject.Opd;

/**
 * Represent a role of a thing in the model.
 * 
 * @author Eran Toch Created: 27/05/2004
 */
public class ModelVRole extends VRole {

	protected IConnectionEdgeEntry originalEntry;

	/**
	 * The constructor allow setting the original
	 * <code>IConnectionEdgeEntry</code>, used for presentation issues.
	 * 
	 * @param _metaLibID
	 *            The ID of the meta-library
	 * @param _thingID
	 *            The ID of the thing (in the meta-library)
	 * @param _original
	 *            The original IConnectionEdgeEntry that the role belongs to.
	 */
	public ModelVRole(long _metaLibID, long _thingID,
			IConnectionEdgeEntry _original) {
		super(_metaLibID, _thingID, Opd.getEntryType(_original));
		this.originalEntry = _original;
	}

	/**
	 * Returns the name of the original thing that the role is set to.
	 */
	public String getThingName() {
		if (this.originalEntry != null) {
			return this.originalEntry.getName();
		}
		return "";
	}

	public IConnectionEdgeEntry getOriginalThing() {
		return this.originalEntry;
	}

}
