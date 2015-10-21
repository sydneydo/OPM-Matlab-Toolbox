package extensionTools.validator.algorithm;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.ISystem;
import exportedAPI.opcatAPI.IConnectionEdgeEntry;
import extensionTools.validator.ValidationException;
import extensionTools.validator.finder.FindSpecificNeighbors;
import extensionTools.validator.finder.Finder;
import extensionTools.validator.finder.HandleBiDirectionalRelations;
import extensionTools.validator.finder.NeighborFinder;
import gui.metaLibraries.logic.MetaLibrary;
import gui.metaLibraries.logic.Role;
import gui.opdProject.Opd;

/**
 * A reflection of the actual model - where the mapping are between the roles
 * TODO: remember that a role might be present in a several things.
 * 
 * @author Eran Toch Created: 06/05/2004
 */
public class ModelReflection extends Reflection {

	public ModelReflection(ISystem sys) {
		super(sys);
	}

	/**
	 * Adds a <code>VRole</code>/<code>ConstraintSet</code> pair to the
	 * <code>constraintMap</code>. For model-based reflection, the attached role
	 * is role of the inspected thing. The method sets a set of Finder
	 * algorithms, each of them implements the <code>NeighborFinder</code>
	 * interface.
	 * 
	 * @see NeighborFinder
	 */
	public void addPair(IConnectionEdgeEntry thing) throws ValidationException {
		Enumeration allRoles;
		allRoles = thing.getRoles(MetaLibrary.TYPE_POLICY);
		while (allRoles.hasMoreElements()) {
			try {
				Role role = (Role) allRoles.nextElement();
				ModelVRole vrole = new ModelVRole(role.getLibraryId(), role
						.getThingId(), thing);

				// Adding a list of Finder algorithms
				Finder vThing = new Finder();
				vThing.addFinder(new FindSpecificNeighbors());
				vThing.addFinder(new HandleBiDirectionalRelations());
				// State finder is removed because a special case should be
				// implemented to support it
				// vThing.addFinder(new StateFinder());
				ConstraintSet set = this.findConstraints(vThing, thing, vrole);
				this.constraintMap.put(vrole, set);
			} catch (ValidationException ve) {
				throw ve;
			}
		}
	}

	/**
	 * Builds a {@link MultipleApplicant} and populates it with
	 * <code>VRole</code> objects. In a model, each thing can be mapped to
	 * multiple roles of things on a single relation/link because each mapped
	 * thing can have multiple roles.
	 */
	protected MultipleApplicant createApplicant(IConnectionEdgeEntry thing) {
		Enumeration allRoles = thing.getRoles();
		MultipleApplicant mp = new MultipleApplicant();
		while (allRoles.hasMoreElements()) {
			Role role = (Role) allRoles.nextElement();
			VRole vrole = new VRole(role.getLibraryId(), role.getThingId(),
					getType(role.getLibraryId(), role.getThingId()));
			mp.addApplicant(vrole);
		}
		return mp;
	}

	private String getType(long libraryID, long thingID) {
		Enumeration<MetaLibrary> libs = this.model
				.getMetaLibraries(MetaLibrary.TYPE_POLICY);

		while (libs.hasMoreElements()) {
			MetaLibrary i = libs.nextElement();
			if (i.getID() == libraryID) {
				IEntry entry = this.model.getISystemStructure().getIEntry(
						thingID);
				if (entry != null) {
					return Opd.getEntryType(entry);
				}
			}
		}
		return "";
	}

	/**
	 * Builds a {@link Case} and return it as a constraint.
	 * 
	 * @param relType
	 * @param dir
	 * @param applicant
	 * @return
	 */
	protected Constraint createConstraintObject(int connectionType, int dir,
			MultipleApplicant applicant, IEntry connectionObject)
			throws ValidationException {
		Case theCase = new Case(applicant, connectionType, dir);
		return theCase;
	}

	/**
	 * Finds the constraints for a model constraint set - adding the role which
	 * owns the constraints.
	 * 
	 * @param vThing
	 * @param thingEntry
	 * @param role
	 * @return
	 */
	protected ConstraintSet findConstraints(Finder vThing,
			IConnectionEdgeEntry thingEntry, VRole role)
			throws ValidationException {
		ConstraintSet set = null;
		try {
			set = super.findConstraints(vThing, thingEntry);
			set.setRole(role);
		} catch (ValidationException ve) {
			throw ve;
		}
		return set;
	}

	/**
	 * Checks the current model reflection agains a set of meta-reflections.
	 * 
	 * @param lawsSet
	 * @return A <code>Vector</code> containing <code>Offence</code> objects,
	 *         each of them signifies an offence. Note that the offences have
	 *         polymorphic behaviour, according to the offence type.
	 */
	public Vector justice(HashMap lawsSet) throws ValidationException {
		Set keys = lawsSet.keySet();
		Iterator lawIt = keys.iterator();
		Vector offences = new Vector();
		try {
			while (lawIt.hasNext()) {
				Long metaID = (Long) lawIt.next();
				MetaReflection meta = (MetaReflection) lawsSet.get(metaID);
				offences.addAll(this.justiceMeta(meta));
			}
		} catch (ValidationException vex) {
			throw vex;
		} catch (Exception ex) {
			throw new ValidationException(
					"A validation exception had occured. " + ex.getMessage());
		}
		return offences;
	}

	/**
	 * Checks the current model reflection agains a given meta-reflection.
	 * 
	 * @param laws
	 *            The reflection of the meta-library.
	 * @return A <code>Vector</code> containing <code>Offence</code> objects,
	 *         each of them signifies an offence.
	 */
	public Vector justiceMeta(MetaReflection laws) throws ValidationException {
		Set rolesOfThings = this.constraintMap.keySet();
		Iterator it = rolesOfThings.iterator();
		Vector offences = new Vector();
		try {
			while (it.hasNext()) {
				ModelVRole role = (ModelVRole) it.next();
				OffenceSet set = this.checkValidity(role, laws);
				if (set != null) {
					offences.addAll(set);
				}
			}
		} catch (ValidationException vex) {
			throw vex;
		}
		return offences;
	}

	/**
	 * Checks the validity of a given role according to a given meta-library.
	 * The method runs on all the laws of the meta-library which are relevant to
	 * the role in the model, comparing them to every constraint in the model
	 * (case).
	 * 
	 * @param role
	 *            A given role of a specific thing in the model.
	 * @param laws
	 *            The set of laws that reflect a specific meta-library.
	 * @return A set of offences that represent the violation of
	 *         <code>role</code> to the given <code>laws</code>.
	 * @throws ValidationException
	 *             TODO: add hashing values to the applicants to speed up search
	 */
	private OffenceSet checkValidity(ModelVRole role, MetaReflection laws)
			throws ValidationException {
		if (laws == null) {
			throw new ValidationException("Laws object is null");
		}
		ConstraintSet modelConstraints = this.getStatements(role);
		ConstraintSet roleLaws = laws.getStatements(role);
		// The role might not corresponding laws
		if (roleLaws != null) {
			Iterator lawsIT = roleLaws.iterator();
			OffenceSet set = new OffenceSet();

			// Running on the laws list
			while (lawsIT.hasNext()) {
				Law law = (Law) lawsIT.next();
				int numberOfSuccesses = 0;
				Iterator it = modelConstraints.iterator();

				// Running on all the cases of the model concerning the role
				while (it.hasNext()) {
					Case cas = (Case) it.next();
					if (cas.obeys(role, law)) {
						numberOfSuccesses++;
					}
				}
				if (numberOfSuccesses < law.getMinBound()) {
					Offence off = new LackOffence(role, law);

					set.add(off);
				} else if (numberOfSuccesses > law.getMaxBound()) {
					Offence off = new ExcessOffence(role, law);
					set.add(off);
				}
			}
			if (set.size() > 0) {
				return set;
			}
		}
		return null;
	}
}
