package extensionTools.validator.algorithm;

import exportedAPI.opcatAPI.ISystemStructure;
import gui.opmEntities.Constants;
/**
 * An offence which is a violation of the cardinality of a certain connection (link
 * or relation).
 * @author Eran Toch
 */
public class ExcessOffence extends Offence {
	public ExcessOffence(ModelVRole _role, Law _law)	{
		super(_role, _law);
	}
	
	/**
	 * Renders the conenction lacking text.
	 */
	public String renderOffenceText(ISystemStructure structure)	{
		String otherSideName = structure.getIEntry(this.getLaw().getApplicant().getRole().getThingID()).getName();
		String text = ""
			+ Constants.type2String(this.getLaw().getEdgeType())
			+ " to "+ otherSideName
			+ " exceeds maximum cardinality ("+ this.getLaw().getMaxBound() +")";
		   return text;
	}
}
