package extensionTools.validator.algorithm;

import extensionTools.validator.finder.Finder;
import gui.opmEntities.Constants;



/**
 * Represent a constraint - a relation/link pattern for a certain <code>IConnectionEdgeEntry</code>.
 * @author Eran Toch
 * Created: 06/05/2004
 */
public abstract class Constraint {

	protected MultipleApplicant applicant;
	private int edgeType;
	private int direction;

	public Constraint(MultipleApplicant _app, int _edgeType, int _direction)	{
		this.applicant = _app;
		this.edgeType = _edgeType;
		this.direction = _direction;
	}
	
	public MultipleApplicant getApplicant()	{
		return this.applicant;
	}
	
	/**
	 * @return
	 */
	public int getDirection() {
		return this.direction;
	}

	/**
	 * @return
	 */
	public int getEdgeType() {
		return this.edgeType;
	}

	/**
	 * @param applicant
	 */
	public void setApplicant(MultipleApplicant applicant) {
		this.applicant = applicant;
	}

	/**
	 * @param i
	 */
	public void setDirection(int i) {
		this.direction = i;
	}

	/**
	 * @param i
	 */
	public void setEdgeType(int i) {
		this.edgeType = i;
	}
	
	public String toString()	{
		String output = this.applicant.toString() +" ("+ Constants.type2String(this.getEdgeType()) +")";
		if (this.direction == Finder.DESTINATION_DIRECTION)	{
			output += " destination";
		}
		else if (this.direction == Finder.SOURCE_DIRECTION)	{
			output += " source";
		}
		return output;
	}

}
