package extensionTools.validator.algorithm;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.ISystem;
import exportedAPI.opcatAPI.ISystemStructure;
import exportedAPI.opcatAPI.IConnectionEdgeEntry;
import extensionTools.validator.ValidationException;
import extensionTools.validator.finder.Finder;

/**
 * A super class for all reflections - a mapping between things in a certain
 * model and the sets of constraints each thing has.
 * 
 * @author Eran Toch Created: 06/05/2004
 */
public abstract class Reflection {

	/**
	 * Maps a VRole in the model to the {@link ConstraintSet} object, containing
	 * constraints on the role.
	 */
	public HashMap constraintMap;

	/**
	 * A reference to the compiled model.
	 */
	protected final ISystem model;

	/**
	 * A constructor method that compiles a given model into a set of
	 * constraints.
	 * 
	 * @param sys
	 *            A System model to be compiled.
	 */
	public Reflection(ISystem sys) {
		this.constraintMap = new HashMap();
		this.model = sys;
	}

	/**
	 * Compiles the model into a <code>constraintMap</code> set of rules.
	 * 
	 * @param sys
	 */
	public void compile() throws ValidationException {
		ISystemStructure structure = this.model.getISystemStructure();
		Enumeration locenum = structure.getElements();
		IEntry entry = null;
		IConnectionEdgeEntry thing;
		while (locenum.hasMoreElements()) {
			try {
				entry = (IEntry) locenum.nextElement();
				// OpmEntity entity = entry.getLogicalEntity();
				if (entry instanceof IConnectionEdgeEntry) {
					thing = (IConnectionEdgeEntry) entry;
					// Each extension implmenets it differently
					this.addPair(thing);
				}
			} catch (ValidationException ve) {
				String message = "Error while compiling on entry "
						+ entry.getName() + " \n" + ve.getMessage();
				ValidationException nve = new ValidationException(message);
				throw nve;
			}
		}
	}

	/**
	 * Builds a constraint object - implemented differently by the estensions.
	 * 
	 * @param rel
	 * @param direction
	 * @param applicant
	 * @return
	 */
	protected abstract Constraint createConstraintObject(int connectionType,
			int direction, MultipleApplicant applicant, IEntry connectionObject)
			throws ValidationException;

	/**
	 * Adds a rule to the <code>constraitMap</code>.
	 * 
	 * @param thing
	 *            The thing to be added.
	 */
	protected abstract void addPair(IConnectionEdgeEntry thing)
			throws ValidationException;

	/**
	 * Creates an applicant - implemented differently by each type of
	 * reflection.
	 * 
	 * @param thing
	 * @return
	 */
	protected abstract MultipleApplicant createApplicant(
			IConnectionEdgeEntry thing) throws ValidationException;

	/**
	 * Retrieves all the statements according to a given role.
	 * 
	 * @param role
	 * @return
	 */
	public ConstraintSet getStatements(VRole role) {
		Iterator eit = this.constraintMap.keySet().iterator();
		while (eit.hasNext()) {
			VRole v = (VRole) eit.next();
			if (v.equals(role)) {
				return (ConstraintSet) this.constraintMap.get(v);
			}
		}
		return null;
	}

	protected ConstraintSet findConstraints(Finder vThing,
			IConnectionEdgeEntry thingEntry) throws ValidationException {
		Vector neighbors = vThing.findNeighbors(thingEntry, this.model
				.getISystemStructure());
		ConstraintSet set = new ConstraintSet();
		for (int i = 0; i < neighbors.size(); i++) {
			Neighbor neighbor = (Neighbor) neighbors.get(i);
			try {
				MultipleApplicant applicant = this.createApplicant(neighbor
						.getConnectedThing());
				Constraint constraint = this.createConstraintObject(neighbor
						.getConnectionType(), neighbor.getDirection(),
						applicant, neighbor.getConnectionObject());
				set.add(constraint);
			} catch (ValidationException ve) {
				throw ve;
			}
		}
		return set;
	}

}
