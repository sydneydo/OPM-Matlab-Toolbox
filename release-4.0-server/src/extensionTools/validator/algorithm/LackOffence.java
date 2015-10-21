package extensionTools.validator.algorithm;

import exportedAPI.opcatAPI.ISystemStructure;
import gui.opmEntities.Constants;

/**
 * An offnce that represent a missing connection (relation, link or other connections)
 * in the model.
 * @author Eran Toch
 */
public class LackOffence extends Offence {
	public LackOffence(ModelVRole _role, Law _law)	{
		super(_role, _law);
	}
	
	/**
	 * Renders the conenction lacking text.
	 */
	public String renderOffenceText(ISystemStructure structure)	{
		String otherSideName = structure.getIEntry(this.getLaw().getApplicant().getRole().getThingID()).getName();
		String text = "Missing "
			+ Constants.type2String(this.getLaw().getEdgeType())
		    + " to "+ otherSideName;
		   return text;
	}
}
