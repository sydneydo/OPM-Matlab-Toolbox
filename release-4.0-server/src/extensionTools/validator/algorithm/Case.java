package extensionTools.validator.algorithm;

/**
 * A constraint reflected by a thing of the <b>model</b>. Can check if a given
 * law applies to it.
 * 
 * @author Eran Toch Created: 06/05/2004
 */
public class Case extends Constraint {

	/**
	 * A constructor for a case (on the model side).
	 * 
	 * @param _app
	 *            <code>MultipleApplicant</code> object that can hold all the
	 *            roles connected to the given thing.
	 * @param _edgeType
	 * @param _direction
	 */
	public Case(MultipleApplicant _app, int _edgeType, int _direction) {
		super(_app, _edgeType, _direction);
	}

	/**
	 * Checks to see if this case (a specific connection in the model) does not
	 * violate a given law. The returned <code>Offence</code> object might be
	 * polymorphic in order to support several offence types. For instance,
	 * offences that indicate that a constraint is missing or offences that
	 * indicate that a constraint exeeded the maximum cardinality.
	 * 
	 * @param theLaw
	 *            The law (meta-model connection: relation, link etc).
	 * @return The <code>Offence</code> object which reflects the offence, or
	 *         <code>null</code> if the case did not violate the law.
	 */
	public boolean obeys(ModelVRole role, Law theLaw) {
		if (theLaw.getEdgeType() != this.getEdgeType()) {
			return false;
		}
		if (theLaw.getDirection() != this.getDirection()) {
			return false;
		}
		// Checking that the one of the current applicants contains the given
		// applicant.
		// Each law has a single applicant, while each Case holds
		// MultipleApplicants
		if (!this.applicant.contains(theLaw.getApplicant())) {
			return false;
		}
		return true;
	}
}
