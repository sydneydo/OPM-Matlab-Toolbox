package extensionTools.validator.algorithm;


/**
 * A constraint reflected for a thing in a <b>meta-library</b>. Holds a min and max 
 * bounds that limit the number of {@link Case} objects accepted by the Law. 
 * @author Eran Toch
 * Created: 06/05/2004
 */
public class Law extends Constraint {
	
	private int min_bound;
	private int max_bound;
	
	public static int MAX_COUNTS = 10000;
	
	/**
	 * A constructor for a meta-library based Law.
	 * @param _app	A single applicant, because each connected thing does not hold 
	 * multiple roles.
	 * @param _edgeType
	 * @param _direction
	 */
	public Law(MultipleApplicant _app, int _edgeType, int _direction, int _min, int _max) {
		super(_app, _edgeType, _direction);
		this.max_bound = _max;
		this.min_bound = _min;
	}
	
	
	/**
	 * @return
	 */
	public int getMaxBound() {
		return this.max_bound;
	}

	/**
	 * @return
	 */
	public int getMinBound() {
		return this.min_bound;
	}

	/**
	 * @param i
	 */
	public void setMaxBound(int i) {
		this.max_bound = i;
	}

	/**
	 * @param i
	 */
	public void setMinBound(int i) {
		this.min_bound = i;
	}
	
//	private String genereteHashKey(VRole vrole, Applicant applicant)	{
//		ModelVRole mvrole = (ModelVRole)vrole;
//		String key = mvrole.getOriginalThing().getId() +":"+ vrole.getThingID() +":"+ applicant.getRole().getThingID();
//		return key;		
//	}
	
	public String toString()	{
		String output = super.toString();
		output += "min="+ this.min_bound +" max="+ this.max_bound;
		return output;
	}
	
}
