package gui.opx.ver3;

import gui.Opcat2;
import gui.controls.FileControl;
import gui.dataProject.DataCreatorType;
import gui.dataProject.DataProject;
import gui.metaLibraries.logic.MetaException;
import gui.metaLibraries.logic.MetaLibrary;
import gui.metaLibraries.logic.MetaManager;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.opmEntities.Constants;
import gui.opmEntities.OpmConnectionEdge;
import gui.opmEntities.OpmFundamentalRelation;
import gui.opmEntities.OpmGeneralRelation;
import gui.opmEntities.OpmObject;
import gui.opmEntities.OpmProceduralLink;
import gui.opmEntities.OpmProcess;
import gui.opmEntities.OpmState;
import gui.opmEntities.OpmStructuralRelation;
import gui.opx.LoadException;
import gui.opx.ver2.LoaderVer2;
import gui.projectStructure.Entry;
import gui.projectStructure.FundamentalRelationEntry;
import gui.projectStructure.GeneralRelationEntry;
import gui.projectStructure.LinkEntry;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ProcessEntry;
import gui.projectStructure.RelationEntry;
import gui.projectStructure.RoleEntry;
import gui.projectStructure.StateEntry;
import gui.scenarios.Scenario;
import gui.scenarios.Scenarios;
import gui.util.OpcatLogger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JDesktopPane;
import javax.swing.JProgressBar;

import modelControl.OpcatMCManager;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * @author Eran Toch The 3rd version of the OPX loader. Adds the following
 *         features to the 2nd OPX loader version:
 *         <p>
 *         1. Meta-Libraries loading. <br>
 *         2. Roles loading. <br>
 */
public class LoaderVer3 extends LoaderVer2 {
	public String CURRENT_VERSION;

	public LoaderVer3(String path) {
		super(path);
		this.CURRENT_VERSION = "3";
	}

	public String getVersion() {
		return this.CURRENT_VERSION;
	}

	/**
	 * Overloads the load method of version 2. Currently, does not provide any
	 * functionality.
	 * 
	 */
	public OpdProject load(JDesktopPane parentDesktop, Document document,
			JProgressBar progressBar) throws LoadException {
		try {
			// URI docURI = new URI(document.getBaseURI());
			// path = docURI.getPath();

			OpdProject prj = super.load(parentDesktop, document, progressBar);
			return prj;
		} catch (LoadException lex) {
			throw lex;
		} // catch (URISyntaxException syn) {
		// throw new LoadException(syn.getMessage());
		// }
	}

	/**
	 * Overloads the system attributes loading, setting the version number of
	 * the OPX to <code>myProject</code>.
	 * 
	 * @see OpdProject#setVersionString
	 */
	protected void _loadSystemAttributes(Element system) throws LoadException {
		this.myProject.setVersionString(this.getVersion());

		try {
			super._loadSystemAttributes(system);
			if (system.getAttribute("global-id") != null) {
				this.myProject.setGlobalID(system
						.getAttributeValue("global-id"));
			}
			Element scenSection = system.getChild("Scenarios");
			if (scenSection != null) {
				List scens = scenSection.getChildren("Scenario");
				if (scens != null) {
					this.myProject.setScen(this._loadScenarios(scens));
				}
			}

			// Element opdMeteSection = system.getChild("OPDMeteSection");
			// if (opdMeteSection != null) {
			// Element metaSection =
			// opdMeteSection.getChild("MetaLibraries");
			// if (metaSection != null) {
			// List metaLibs = metaSection.getChildren("LibraryReference");
			// if (metaLibs != null) {
			// this._loadMetaLibraries(metaLibs, this.myProject
			// .getOpdMetaDataManager());
			// }
			// }
			// }
		} catch (LoadException lex) {
			throw lex;
		}
	}

	/**
	 * Extends the <code>SystemProperties</code> loading section, augmenting it
	 * with <b>MetaLibraries </b> support.
	 */
	protected void _loadSystemProperties(Element props) throws JDOMException {
		try {
			super._loadSystemProperties(props);
			Element metaSection = props.getChild("MetaLibraries");
			if (metaSection != null) {
				List metaLibs = metaSection.getChildren("LibraryReference");
				if (metaLibs != null) {
					this._loadMetaLibraries(metaLibs, this.myProject
							.getMetaManager());
				}
			}

		} catch (JDOMException ex) {
			throw ex;
		}
	}

	protected Scenarios _loadScenarios(List scenarios) {
		Scenarios prjScenarios = new Scenarios();
		for (Iterator i = scenarios.iterator(); i.hasNext();) {
			Element scen = (Element) i.next();
			String scenName = scen.getAttributeValue("ScenName");
			Scenario scenrio = new Scenario(scenName);
			List types = scen.getChildren("types");
			for (Iterator j = types.iterator(); j.hasNext();) {
				Element typeElem = (Element) j.next();
				String type = typeElem.getAttributeValue("type");
				HashMap<Long, Long> typeHash = new HashMap<Long, Long>();
				List pairs = typeElem.getChildren("pairs");
				for (Iterator k = pairs.iterator(); k.hasNext();) {
					Element pairElement = (Element) k.next();
					Long ObjID = new Long(pairElement
							.getAttributeValue("ObjectID"));
					Long StateID = new Long(pairElement
							.getAttributeValue("StateID"));
					typeHash.put(ObjID, StateID);
				}
				if (type.equalsIgnoreCase("initial")) {
					scenrio.setInitialObjectsHash(typeHash);
				} else {
					scenrio.setFinalObjectsHash(typeHash);
				}
			}

			Element metaSection = scen.getChild("MetaLibraries");
			if (metaSection != null) {
				List metaLibs = metaSection.getChildren("LibraryReference");
				if (metaLibs != null) {
					this._loadMetaLibraries(metaLibs, scenrio.getSettings());
				}
			}

			prjScenarios.add(scenrio);
		}
		return prjScenarios;
	}

	private String fixPrivatePath(String path, String loadingPath) {
		// loading Private
		return fixRelativePath(path, loadingPath);
	}

	private String fixTemplatesPath(String path, String templateOriginalPath) {

		// loading templates so we have to make sure to load from models
		// dir. this should realy be implemented as DataCreatorType

		String originalPath = templateOriginalPath;

		String workingCopyPath = OpcatMCManager.WORKING_COPY;

		int temp = workingCopyPath.lastIndexOf(FileControl.fileSeparator);

		String modelsDir = workingCopyPath.substring(temp + 1);

		String localPathToModelsDir = workingCopyPath.substring(0, temp);

		temp = originalPath.lastIndexOf(modelsDir);

		if (temp > 0) {
			originalPath = localPathToModelsDir
					+ originalPath.substring(temp - 1);
		}

		return fixRelativePath(path, originalPath);
	}

	/**
	 * referencePath is assumed to be a path to a file i.e. it ends with a file
	 * name. this file path is the reference for the path element
	 * 
	 * @param relativePath
	 * @param referencePath
	 * @return
	 */
	private String fixRelativePath(String relativePath, String referencePath) {

		String path = referencePath;

		path = path.replace("\\", FileControl.fileSeparator);

		referencePath = referencePath.replace("\\", FileControl.fileSeparator);

		path = path.replace("/", FileControl.fileSeparator);

		referencePath = referencePath.replace("/", FileControl.fileSeparator);

		if (path.startsWith(".")) {
			String filename = referencePath;
			int last = filename.lastIndexOf(FileControl.fileSeparator);
			path = filename.substring(0, last) + path.substring(1);
		}

		String myFileSep = "@";
		path = path.replace(FileControl.fileSeparator, myFileSep);
		if (path.contains("..")) {
			StringTokenizer tok = new StringTokenizer(path, myFileSep);
			String prev = "";
			while (tok.hasMoreElements()) {
				String current = tok.nextToken();
				if (current.equalsIgnoreCase("..")) {
					path = path.replaceFirst(myFileSep + prev + myFileSep
							+ "..", "");
				}
				prev = current;
			}

		}
		path = path.replace(myFileSep, FileControl.fileSeparator);
		return path;
	}

	/**
	 * Gets the meta-libraries from the XML file and adds them to the
	 * <code>MetaManager</code>.
	 * 
	 * @param metaLibs
	 * @param man
	 */
	protected void _loadMetaLibraries(List<Element> metaLibs, MetaManager man) {
		for (Iterator<Element> i = metaLibs.iterator(); i.hasNext();) {

			Element prop = i.next();

			int dataReferenceType = (prop
					.getAttributeValue("creatorReferenceType") != null ? Integer
					.parseInt(prop.getAttributeValue("creatorReferenceType"))
					: DataCreatorType.REFERENCE_TYPE_TEMPLATE_FILE);

			int libraryType = (prop.getAttributeValue("libDataType") != null ? Integer
					.parseInt(prop.getAttributeValue("libDataType"))
					: MetaLibrary.TYPE_POLICY);

			boolean hidden = (prop.getAttributeValue("hidden") == null ? false
					: Boolean.parseBoolean(prop.getAttributeValue("hidden")));

			int dataType = (prop.getAttributeValue("creatorDataType") != null ? Integer
					.parseInt(prop.getAttributeValue("creatorDataType"))
					: DataCreatorType.DATA_TYPE_OPCAT_LIBRARAY);

			String libDataID = (prop.getAttributeValue("libDataID") != null ? prop
					.getAttributeValue("libDataID")
					: "-1");

			String path = prop.getChildText("LibPath");

			DataProject data;

			if (dataReferenceType == DataCreatorType.REFERENCE_TYPE_TEMPLATE_FILE) {

				String originalPath = (prop.getChildText("OriginalPath") != null ? prop
						.getChildText("OriginalPath")
						: loadingPath);

				path = fixTemplatesPath(path, originalPath);

			} else if (dataReferenceType == DataCreatorType.REFERENCE_TYPE_PRIVATE_FILE) {
				path = fixPrivatePath(path, loadingPath);

			}

			MetaLibrary lib = null;
			Object param;
			if (dataReferenceType == DataCreatorType.REFERENCE_TYPE_PRIVATE_FILE) {
				param = new Object[] { path, myProject };
				// data = new DataProject(param, new DataCreatorType(dataType,
				// dataReferenceType));
			} else {
				param = path;
				// data = new DataProject(path, new DataCreatorType(dataType,
				// dataReferenceType));
			}

			lib = new MetaLibrary(libraryType, param, dataType,
					dataReferenceType);

			lib.setHidden(hidden);

			try {
				man.addMetaLibrary(lib);
			} catch (MetaException ex) {
				OpcatLogger.logError("Error loading libraries: \n"
						+ ex.getMessage());
				OpcatLogger.logError(ex);
			}
		}
	}

	/**
	 * Loads the <code>Entry</code> attributes, using the previous OPX version,
	 * plus <code>Role</code> and URL loading.
	 */
	protected void _loadEntityAttr(Entry entity, Element xmlEntity)
			throws JDOMException {
		// super._loadThingAttr(thing, xmlThing);
		super._loadEntityAttr(entity, xmlEntity);
		Element rolesSection = xmlEntity.getChild("Roles");
		if (rolesSection != null) {
			List roles = rolesSection.getChildren("OPMRole");
			if (roles != null) {
				for (Iterator i = roles.iterator(); i.hasNext();) {
					Element xmlRole = (Element) i.next();
					String roleID = xmlRole.getAttributeValue("roleID");
					String libID = xmlRole.getAttributeValue("libID");
					long roleIDNum = Long.parseLong(roleID);
					long libIDNum = Long.parseLong(libID);
					RoleEntry role = new RoleEntry(roleIDNum, libIDNum);
					entity.addRole(role);
				}
			}
		}

		try {
			if (xmlEntity.getChildText("url").equalsIgnoreCase("")) {

				entity.setUrl("none");
			} else {
				String str = xmlEntity.getChildText("url");
				if (str.substring(0, 8).equalsIgnoreCase("file:///")) {
					str = str.substring(8);

					if (str.startsWith(".")) {
						String filename = Opcat2.getLoadedfilename();
						int last = filename
								.lastIndexOf(FileControl.fileSeparator);
						str = filename.substring(0, last) + str.substring(1);
						str = "file:///" + str;
					}
				}
				entity.setUrl(str);
			}
		} catch (Exception e) {

		}
	}

	/**
	 * Marks whether the opd is locked, and sets the path to a referenced system
	 * if the opd is "open reused"
	 */
	protected Opd _loadOpd(Element xmlOpd, Opd parentOpd,
			int relationsWithParent) throws JDOMException {
		Opd myOpd = super._loadOpd(xmlOpd, parentOpd, relationsWithParent);
		return myOpd;
	}

	// LogicalObject
	protected ObjectEntry _loadLogicalObject(Element xmlObject)
			throws JDOMException {
		long objectId = xmlObject.getChild("EntityAttr").getAttribute("id")
				.getLongValue();
		ObjectEntry myObject = new ObjectEntry(new OpmObject(objectId, ""),
				this.myProject);
		this._loadEntityAttr(myObject, xmlObject.getChild("EntityAttr"));
		this._loadThingAttr(myObject, xmlObject.getChild("ThingAttr"));
		myObject.setIndexName(xmlObject.getAttributeValue("indexName"));
		myObject.setIndexOrder(xmlObject.getAttribute("indexOrder")
				.getIntValue());
		myObject.setInitialValue(xmlObject.getAttributeValue("initialValue"));
		myObject.setKey(xmlObject.getAttribute("key").getBooleanValue());
		myObject.setPersistent(xmlObject.getAttribute("persistent")
				.getBooleanValue());
		myObject.setType(xmlObject.getAttributeValue("type"));

		// xmlObject.setAttribute("measurmentUnit", opm.getMesurementUnit());
		// xmlObject.setAttribute("measurmentUnitValue",
		// Double.toString(opm.getMesurementUnitValue()));

		OpmObject opm = (OpmObject) myObject.getLogicalEntity();
		if (xmlObject.getAttribute("measurmentUnit") != null) {
			opm.setMesurementUnit(xmlObject.getAttribute("measurmentUnit")
					.getValue());
		}

		if (xmlObject.getAttribute("measurmentUnitInitialValue") != null) {
			opm.setMesurementUnitInitialValue(xmlObject.getAttribute(
					"measurmentUnitInitialValue").getDoubleValue());
		}

		if (xmlObject.getAttribute("measurmentUnitMinValue") != null) {
			opm.setMesurementUnitMinValue(xmlObject.getAttribute(
					"measurmentUnitMinValue").getDoubleValue());
		}

		if (xmlObject.getAttribute("measurmentUnitMaxValue") != null) {
			opm.setMesurementUnitMaxValue(xmlObject.getAttribute(
					"measurmentUnitMaxValue").getDoubleValue());
		}

		if (xmlObject.getAttribute("typeOriginId") != null) {
			myObject.setTypeOriginId(xmlObject.getAttribute("typeOriginId")
					.getLongValue());
		}

		this.myProject.getComponentsStructure().put(objectId, myObject);
		return myObject;
	}

	// LogicalProcess
	protected void _loadLogicalProcess(Element xmlProcess) throws JDOMException {

		long processId = xmlProcess.getChild("EntityAttr").getAttribute("id")
				.getLongValue();

		ProcessEntry myProcess = new ProcessEntry(
				new OpmProcess(processId, ""), this.myProject);

		this._loadEntityAttr(myProcess, xmlProcess.getChild("EntityAttr"));
		this._loadThingAttr(myProcess, xmlProcess.getChild("ThingAttr"));

		myProcess.setMaxTimeActivation(xmlProcess
				.getAttributeValue("maxTimeActivation"));
		myProcess.setMinTimeActivation(xmlProcess
				.getAttributeValue("minTimeActivation"));
		myProcess.setProcessBody(xmlProcess.getChildText("processBody"));
		this.myProject.getComponentsStructure().put(processId, myProcess);

	}

	// LogicalRelation
	protected void _loadLogicalRelation(Element xmlRelation)

	throws JDOMException {

		long relId = xmlRelation.getChild("EntityAttr").getAttribute("id")
				.getLongValue();
		OpmConnectionEdge source = (OpmConnectionEdge) this.myProject
				.getComponentsStructure().getEntry(
						xmlRelation.getAttribute("sourceId").getLongValue())
				.getLogicalEntity();
		OpmConnectionEdge destination = (OpmConnectionEdge) this.myProject
				.getComponentsStructure().getEntry(
						xmlRelation.getAttribute("destinationId")
								.getLongValue()).getLogicalEntity();

		OpmStructuralRelation opmRel = Constants.createRelation(xmlRelation
				.getAttribute("relationType").getIntValue(), relId, source,
				destination);
		RelationEntry myRelation;

		if (opmRel instanceof OpmFundamentalRelation) {
			myRelation = new FundamentalRelationEntry(
					(OpmFundamentalRelation) opmRel, this.myProject);
		} else {
			myRelation = new GeneralRelationEntry((OpmGeneralRelation) opmRel,
					this.myProject);
		}

		this._loadEntityAttr(myRelation, xmlRelation.getChild("EntityAttr"));

		myRelation.setSourceCardinality(xmlRelation
				.getAttributeValue("sourceCardinality"));
		myRelation.setDestinationCardinality(xmlRelation
				.getAttributeValue("destinationCardinality"));
		myRelation.setForwardRelationMeaning(xmlRelation
				.getAttributeValue("forwardRelationMeaning"));
		myRelation.setBackwardRelationMeaning(xmlRelation
				.getAttributeValue("backwardRelationMeaning"));

		this.myProject.getComponentsStructure().put(relId, myRelation);
	}

	// LogicalState
	protected void _loadLogicalStates(Element xmlState, ObjectEntry parentEntry)
			throws JDOMException {
		long stateId = xmlState.getChild("EntityAttr").getAttribute("id")
				.getLongValue();
		StateEntry myState = new StateEntry(new OpmState(stateId, ""),
				(OpmObject) parentEntry.getLogicalEntity(), this.myProject);

		this._loadEntityAttr(myState, xmlState.getChild("EntityAttr"));

		myState.setDefault(xmlState.getAttribute("default").getBooleanValue());
		myState.setFinal(xmlState.getAttribute("final").getBooleanValue());
		myState.setInitial(xmlState.getAttribute("initial").getBooleanValue());
		myState.setMaxTime(xmlState.getAttributeValue("maxTime"));
		myState.setMinTime(xmlState.getAttributeValue("minTime"));

		this.myProject.getComponentsStructure().put(stateId, myState);
		parentEntry.addState((OpmState) myState.getLogicalEntity());
	}

	protected void _loadLogicalLink(Element xmlLink) throws JDOMException {
		long linkId = xmlLink.getChild("EntityAttr").getAttribute("id")
				.getLongValue();

		OpmProceduralLink opmLink = Constants.createLink(xmlLink.getAttribute(
				"linkType").getIntValue(), linkId, xmlLink.getAttribute(
				"sourceId").getLongValue(), xmlLink.getAttribute(
				"destinationId").getLongValue());

		LinkEntry myLink = new LinkEntry(opmLink, this.myProject);

		this._loadEntityAttr(myLink, xmlLink.getChild("EntityAttr"));

		myLink.setCondition(xmlLink.getAttributeValue("condition"));
		myLink.setPath(xmlLink.getAttributeValue("path"));
		myLink.setMaxReactionTime(xmlLink.getAttributeValue("maxReactionTime"));
		myLink.setMinReactionTime(xmlLink.getAttributeValue("minReactionTime"));
		if (xmlLink.getAttributeValue("resourceConsumption") != null) {
			myLink.setResourceConsumption(Double.valueOf(xmlLink
					.getAttributeValue("resourceConsumption")));
		}
		if (xmlLink.getAttributeValue("resourceConsumptionAccumolated") != null) {
			myLink.setResourceConsumptionAccumolated(Boolean.valueOf(xmlLink
					.getAttributeValue("resourceConsumptionAccumolated")));
		}

		this.myProject.getComponentsStructure().put(linkId, myLink);
	}

}
