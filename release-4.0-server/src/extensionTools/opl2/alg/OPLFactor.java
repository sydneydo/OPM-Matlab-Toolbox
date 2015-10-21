package extensionTools.opl2.alg;

import java.util.LinkedList;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBException;

import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.ILinkEntry;
import exportedAPI.opcatAPI.ILinkInstance;
import exportedAPI.opcatAPI.IObjectEntry;
import exportedAPI.opcatAPI.IProcessEntry;
import exportedAPI.opcatAPI.IRelationEntry;
import exportedAPI.opcatAPI.IRole;
import exportedAPI.opcatAPI.IStateEntry;
import exportedAPI.opcatAPI.IThingEntry;
import extensionTools.opl2.generated.ExhibitedObject;
import extensionTools.opl2.generated.MaxReactionTime;
import extensionTools.opl2.generated.MaxTimeValue;
import extensionTools.opl2.generated.MaxTimeoutValue;
import extensionTools.opl2.generated.MinReactionTime;
import extensionTools.opl2.generated.MinTimeValue;
import extensionTools.opl2.generated.OPLscript;
import extensionTools.opl2.generated.OPLscriptType;
import extensionTools.opl2.generated.ObjectFactory;
import extensionTools.opl2.generated.ObjectIndex;
import extensionTools.opl2.generated.Operation;
import extensionTools.opl2.generated.Role;
import extensionTools.opl2.generated.ThingSentenceSet;
import extensionTools.opl2.generated.TimeValue;
import gui.metaLibraries.logic.MetaLibrary;

/**
 * <p>
 * Title: Extension Tools
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author OPCAT team
 * @version 1.0
 */

public class OPLFactor {

	public class EmptySentenceException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		 
		 
	}

	public static OPLscript buildOPLscript(String systemName) throws Exception {
		OPLscript opl_;
		try {
			opl_ = objFactory.createOPLscript();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		((OPLscriptType) opl_).setSystemName(systemName);
		((OPLscriptType) opl_).setSystemDescription(" ");
		return opl_;
	}

	protected void throwSpecialException(IEntry abstractEntry)
			throws EmptySentenceException {
		this.builder.VisitedThings.remove(abstractEntry);
		throw new EmptySentenceException();
	}

	protected ThingSentenceSet buildThingSentence(IEntry abstractEntry)
			throws Exception, EmptySentenceException {
		ThingSentenceSet thing;
		if (this.builder.VisitedThings.contains(abstractEntry)) {
			throw new Exception();
		}
		// Has non visited zooming father
		if ( /* builder.hasNonVisitedInheritenceFather(abstractEntry)|| */
		this.builder.hasNonVisitedZoomingFather(abstractEntry)) {
			throw new EmptySentenceException();
		}
		this.builder.VisitedThings.add(abstractEntry);
		try {
			thing = objFactory.createThingSentenceSet();
			thing.setSystemName(this.systemName);

			thing.setSubjectThingName(abstractEntry.getName());
			this.builder.addFatherName(abstractEntry, thing);
			if (abstractEntry instanceof IObjectEntry) {
				IObjectEntry obj = (IObjectEntry) abstractEntry;
				if (!this.builder.fillThingSentence(obj, thing)) {
					// HERE WE CAN MAKE EMPTY SENTENCE
					throw new EmptySentenceException();
				}
				if (!obj.getIndexName().equals("")) {
					ObjectIndex key = objFactory.createObjectIndex();
					key.setIndexName(obj.getIndexName());
					key.setIndexLocation(obj.getIndexOrder());
					thing.getObjectIndex().add(key);
				}
				// thing.setSubjectRole(obj.getRole());
			} else {
				IProcessEntry proc = (IProcessEntry) abstractEntry;
				if (!this.builder.fillThingSentence(proc, thing)) {
					// HERE WE CAN MAKE EMPTY SENTENCE
					throw new EmptySentenceException();
				}
				thing.setMinTimeValue(this.buildMinTimeValue(proc
						.getMinTimeActivation()));
				thing.setMaxTimeValue(this.buildMaxTimeValue(proc
						.getMaxTimeActivation()));
				// thing.setSubjectRole(proc.getRole());
			}
		} catch (Exception e) {
			throw e;
		}
		return thing;
	}

	public OPLFactor(OPLTreeBuilder builder_, String systemName_,
			ObjectFactory ob) {
		this.builder = builder_;
		// try{
		// jc = JAXBContext.newInstance( "extensionTools.opl2.generated" );
		// }catch(JAXBException t){t.printStackTrace();}
		// objFactory = new ObjectFactory();
		objFactory = ob;
		this.systemName = systemName_;
	}

	java.util.List buildRoles(IThingEntry abstractEntry) throws JAXBException {
		java.util.List lst = new LinkedList();
		java.util.Enumeration en = abstractEntry.getRoles(MetaLibrary.TYPE_POLICY);
		while (en.hasMoreElements()) {
			IRole aRole = (IRole) en.nextElement();
			String roleThingName = aRole.getThingName();// getProperty(OPLGeneral.THING_NAME);
			String roleLibraryName = aRole.getLibraryName();// getProperty(OPLGeneral.LIBRARY_NAME);
			Role rl = objFactory.createRole();
			rl.setRoleName(roleThingName);
			if ((roleLibraryName != null) && (!roleLibraryName.equals(""))) {
				rl.setLibrary(roleLibraryName);
			}
			lst.add(rl);
		}
		if (lst.isEmpty() && !abstractEntry.getFreeTextRole().equals("")) {
			Role rl = objFactory.createRole();
			rl.setRoleName(abstractEntry.getFreeTextRole());
			lst.add(rl);
		}
		return lst;
	}

	public ExhibitedObject buildExhibitedObject(IEntry destination,
			IRelationEntry rel) throws JAXBException {
		ExhibitedObject obj;
		try {
			obj = objFactory.createExhibitedObject();
		} catch (JAXBException e) {
			e.printStackTrace();
			throw e;
		}
		obj.setAttributeName(destination.getName());
		int[] cardinality = OPLGeneral.getCardinality(rel
				.getDestinationCardinality());
		int max = 1, min = 0;
		obj.setMaximalCardinality(cardinality[max]);
		obj.setMinimalCardinality(cardinality[min]);
		return obj;
	}

	protected Operation buildOperation(String operationName)
			throws JAXBException {
		Operation op = objFactory.createOperation();
		op.setOperationName(operationName);
		return op;
	}

	protected MaxReactionTime buildMaxReactionTime(ILinkEntry link)
			throws Exception {
		MaxReactionTime max = objFactory.createMaxReactionTime();
		max.setTimeValue(this.buildTimeValue(link.getMaxReactionTime()));
		return max;
	}

	protected MinReactionTime buildMinReactionTime(ILinkEntry link)
			throws Exception {
		MinReactionTime max = objFactory.createMinReactionTime();
		max.setTimeValue(this.buildTimeValue(link.getMinReactionTime()));
		return max;
	}

	protected MaxTimeoutValue buildMaxTimeoutValue(IStateEntry state)
			throws Exception {
		MaxTimeoutValue max = objFactory.createMaxTimeoutValue();
		max.setTimeValue(this.buildTimeValue(state.getMaxTime()));
		return max;
	}

	protected MaxTimeoutValue buildMaxTimeoutValue(IProcessEntry state)
			throws Exception {
		MaxTimeoutValue max = objFactory.createMaxTimeoutValue();
		max.setTimeValue(this.buildTimeValue(state.getMaxTimeActivation()));
		return max;
	}

	protected MaxTimeValue buildMaxTimeValue(String sourceTime_)
			throws Exception {
		MaxTimeValue max = objFactory.createMaxTimeValue();
		max.setTimeValue(this.buildTimeValue(sourceTime_));
		return max;
	}

	protected MinTimeValue buildMinTimeValue(String sourceTime_)
			throws Exception {
		MinTimeValue max = objFactory.createMinTimeValue();
		max.setTimeValue(this.buildTimeValue(sourceTime_));
		return max;
	}

	protected TimeValue buildTimeValue(String sourceTime_) throws Exception {
		TimeValue time;
		int[] sourceTime = OPLGeneral.getTime(sourceTime_);
		try {
			time = objFactory.createTimeValue();
			time.setDays(sourceTime[OPLGeneral.DAY]);
			time.setHours(sourceTime[OPLGeneral.HOUR]);
			time.setMinutes(sourceTime[OPLGeneral.MINUTE]);
			time.setMonths(sourceTime[OPLGeneral.MONTH]);
			time.setMilliSeconds(sourceTime[OPLGeneral.MSEC]);
			time.setSeconds(sourceTime[OPLGeneral.SECOND]);
			time.setYears(sourceTime[OPLGeneral.YEAR]);
		} catch (Exception e) {
			throw e;
		}
		return time;
	}

	public IEntry getEntry(IRelationEntry rel) {
		return this.builder.myStructure.getIEntry(rel.getDestinationId());
	}

	protected void fillPath(java.util.List labelPath, String path) {
		if (!path.equals("")) {
			StringTokenizer st = new StringTokenizer(path, ",");
			while (st.hasMoreTokens()) {
				labelPath.add(st.nextToken());
			}
		}
	}

	protected void fillPath(java.util.List labelPath, java.util.List path) {
		if (!path.isEmpty()) {
			// StringTokenizer st = new StringTokenizer(path, ",");
			for (int i = 0; i < path.size(); i++) {
				// while (st.hasMoreTokens()) {
				labelPath.add(path.get(i));
			}
		}
	}

	protected String setLogicBySource(ILinkEntry link) {
		boolean or = false;
		boolean xor = false;
		if (this.builder.getIInstanceByOPD(link) != null) {
			if (((ILinkInstance) this.builder.getIInstanceByOPD(link))
					.getOrXorSourceNeighbours(true) != null) {
				or = ((ILinkInstance) this.builder.getIInstanceByOPD(link))
						.getOrXorSourceNeighbours(true).hasMoreElements();
			}
		}
		if (this.builder.getIInstanceByOPD(link) != null) {
			if (((ILinkInstance) this.builder.getIInstanceByOPD(link))
					.getOrXorSourceNeighbours(false) != null) {
				xor = ((ILinkInstance) this.builder.getIInstanceByOPD(link))
						.getOrXorSourceNeighbours(false).hasMoreElements();
			}
		}
		if (or) {
			return "or";
		} else if (xor) {
			return "xor";
		}
		return "and";
	}

	protected String setLogicByDestination(ILinkEntry link) {
		boolean or = false;
		boolean xor = false;
		if (this.builder.getIInstanceByOPD(link) != null) {
			if (((ILinkInstance) this.builder.getIInstanceByOPD(link))
					.getOrXorDestinationNeighbours(true) != null) {
				or = ((ILinkInstance) this.builder.getIInstanceByOPD(link))
						.getOrXorDestinationNeighbours(true).hasMoreElements();
			}
		}
		if (this.builder.getIInstanceByOPD(link) != null) {
			if (((ILinkInstance) this.builder.getIInstanceByOPD(link))
					.getOrXorDestinationNeighbours(false) != null) {
				xor = ((ILinkInstance) this.builder.getIInstanceByOPD(link))
						.getOrXorDestinationNeighbours(false).hasMoreElements();
			}
		}
		if (or) {
			return "or";
		} else if (xor) {
			return "xor";
		}
		return "and";
	}

	// protected static JAXBContext jc;
	protected String systemName;

	protected OPLTreeBuilder builder;

	protected static ObjectFactory objFactory;

	protected final int MIN = 0;

	protected final int MAX = 1;

}