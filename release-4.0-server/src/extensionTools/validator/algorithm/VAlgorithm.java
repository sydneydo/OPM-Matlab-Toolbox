package extensionTools.validator.algorithm;

import java.util.ConcurrentModificationException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import exportedAPI.opcatAPI.ISystem;
import exportedAPI.opcatAPI.ISystemStructure;
import extensionTools.validator.ValidationException;
import gui.images.standard.StandardImages;
import gui.metaLibraries.logic.MetaLibrary;

/**
 * An implementation of a simple validation algorithm that validates a given
 * model against its imported meta-libraries. The algorithm recieves as input an
 * OPM system model and a set of meta-libraries (represented as OPM models), and
 * outputs a list of vaidation warnings.
 * 
 * @author Eran Toch Created: 06/05/2004 TODO: zoom-in TODO: check that objects
 *         have the correct states.
 */
public class VAlgorithm {
	private HashMap metaLibraries = null;

	private HashMap metas = null;

	public Vector validate(ISystem sys) throws ValidationException {
		ModelReflection systemReality = new ModelReflection(sys);
		try {
			systemReality.compile();
		} catch (ValidationException ve) {
			throw new ValidationException("Model analysis had failed");
		}
		this.metas = new HashMap();
		this.metaLibraries = new HashMap();

		Enumeration tempMetaLibs = sys
				.getMetaLibraries(MetaLibrary.TYPE_POLICY);
		// TODO: this should not be here it's a patch
		Vector<MetaLibrary> metaVector = new Vector<MetaLibrary>();
		while (tempMetaLibs.hasMoreElements()) {
			MetaLibrary lib = (MetaLibrary) tempMetaLibs.nextElement();
			if (lib.isTemplate()) {
				metaVector.add(lib);
			}
		}
		Enumeration<MetaLibrary> metaLibs = metaVector.elements();
		// 

		HashSet flagedMetas = new HashSet();
		while (metaLibs.hasMoreElements()) {
			MetaLibrary lib = (MetaLibrary) metaLibs.nextElement();
			if (lib.getState() == MetaLibrary.STATE_LOADED) {
				if (!lib.isPolicy())
					continue;
				try {
					flagedMetas.add(new Long(lib.getID()));
					this.metaLibraries.put(new Long(lib.getID()), lib);
					MetaReflection laws = new MetaReflection(lib.getISystem(),
							lib.getID());
					laws.compile();
					this.metas.put(new Long(lib.getID()), laws);
				} catch (ValidationException ve) {
					throw new ValidationException(
							"Meta-Library analysis had failed for library "
									+ lib.getName() + "(" + lib.getPath() + ")");
				}
			}
		}

		// Cleaning the metas list from staled library reflections
		// cleanOldLawSets(flagedMetas);

		// Finding the offences
		Vector warnings = null;
		try {
			Vector offences = systemReality.justice(this.metas);

			// Creating the warnings
			warnings = this.createWarnings(offences, sys);
		} catch (ValidationException vex) {
			throw vex;
		}
		return warnings;
	}

	/**
	 * Cleans the <code>metas</code> list of "staled" meta-reflections -
	 * meta-libraries which are not currently in the libraries list.
	 * 
	 * @param currentList
	 */
	protected void cleanOldLawSets(HashSet currentList) {
		Set keys = this.metas.keySet();
		Iterator lawIt = keys.iterator();
		try {
			while (lawIt.hasNext()) {
				Long metaID = (Long) lawIt.next();
				if (!currentList.contains(metaID)) {
					this.metas.remove(metaID);
					this.metaLibraries.remove(metaID);
				}
			}

		} catch (ConcurrentModificationException ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * Creates the warnings data set from a given <code>Offence</code> objects
	 * <code>Vector</code>.
	 * 
	 * @param offences
	 * @param sys
	 * @return
	 */
	protected Vector createWarnings(Vector offences, ISystem sys) {
		Vector allWarnings = new Vector();
		for (int j = 0; j < offences.size(); j++) {
			Offence offence = (Offence) offences.get(j);
			Vector warning = new Vector();
			warning.add(StandardImages.VALIDATION);
			warning.add(offence);
			VRole lawRole = offence.getLaw().getApplicant().getRole();
			if (lawRole == null) {
				System.out.println("Error on offence" + offence
						+ " - no role was found");
				break;
			}
			long libID = offence.getLaw().getApplicant().getRole()
					.getMetaLibID();
			MetaLibrary metaLib = (MetaLibrary) this.metaLibraries
					.get(new Long(libID));
			if (metaLib == null) {
				System.out.println("MetaLib " + libID + " is missing");
				break;
			}
			ISystemStructure structure = metaLib.getStructure();
			String text = offence.renderOffenceText(structure);
			warning.add(text);
			allWarnings.add(warning);
		}
		return allWarnings;
	}
}
