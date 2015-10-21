package gui.opx.ver2;

import exportedAPI.OpdKey;
import exportedAPI.RelativeConnectionPoint;
import expose.OpcatExposeKey;
import expose.OpcatExposeConstants.OPCAT_EXPOSE_OP;
import gui.Opcat2;
import gui.actions.expose.OpcatExposeChange;
import gui.controls.FileControl;
import gui.opdGraphics.draggers.OpdRelationDragger;
import gui.opdGraphics.lines.StyledLine;
import gui.opdGraphics.opdBaseComponents.OpdConnectionEdge;
import gui.opdGraphics.opdBaseComponents.OpdFundamentalRelation;
import gui.opdGraphics.opdBaseComponents.OpdObject;
import gui.opdGraphics.opdBaseComponents.OpdThing;
import gui.opdProject.GenericTable;
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
import gui.opx.LoaderI;
import gui.projectStructure.ConnectionEdgeEntry;
import gui.projectStructure.ConnectionEdgeInstance;
import gui.projectStructure.Entry;
import gui.projectStructure.FundamentalRelationEntry;
import gui.projectStructure.FundamentalRelationInstance;
import gui.projectStructure.GeneralRelationEntry;
import gui.projectStructure.GeneralRelationInstance;
import gui.projectStructure.GraphicalRelationRepresentation;
import gui.projectStructure.Instance;
import gui.projectStructure.LinkEntry;
import gui.projectStructure.LinkInstance;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ObjectInstance;
import gui.projectStructure.OrInstance;
import gui.projectStructure.ProcessEntry;
import gui.projectStructure.ProcessInstance;
import gui.projectStructure.RelationEntry;
import gui.projectStructure.StateEntry;
import gui.projectStructure.StateInstance;
import gui.projectStructure.ThingEntry;
import gui.projectStructure.ThingInstance;
import gui.util.OpcatLogger;
import java.awt.Color;
import java.awt.Cursor;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

//import javax.xdml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Unmarshaller;

public class LoaderVer2 implements LoaderI {
	public String CURRENT_VERSION;

	protected final static int ROOT_OPD = 1;

	protected final static int OR_TYPE = 0;

	protected final static int XOR_TYPE = 1;

	protected final static int NO_PARENT = 0;

	protected final static int UNFOLDED = 1;

	protected final static int INZOOMED = 2;

	protected int numOfOpds = 0;

	protected OpdProject myProject;

	protected JProgressBar pBar;

	protected String loadingPath = null;

	private String path;

	public LoaderVer2(String path) {
		this.CURRENT_VERSION = "2";
		this.loadingPath = path;
	}

	public String getVersion() {
		return this.CURRENT_VERSION;
	}

	/*
	 * TODO: there is no need to pass the graphic objects like the desktop to
	 * the load function. the file funcctions shoud fucos on file ops only and
	 * not set any graphics objects. raanan
	 */
	public OpdProject load(JDesktopPane parentDesktop, Document document,
			JProgressBar progressBar) throws LoadException {
		try {
			this.pBar = progressBar;
			if (this.pBar != null) {
				this.pBar.setStringPainted(true);
			}

			Element rootTag = document.getRootElement();
			this._prepareProgressBar(rootTag);

			this.myProject = new OpdProject(parentDesktop, 1);
			myProject.setPath(path);
			Element system = rootTag.getChild("OPMSystem");
			this._loadSystemAttributes(system);
			Element systemProperties = system.getChild("SystemProperties");
			this._loadSystemProperties(systemProperties);

			// reuse comment
			this.myProject.setDuringLoad(true);
			// end reuse comment

			Element logicalStructure = system.getChild("LogicalStructure");
			if (this._loadLogicalStructure(logicalStructure)) {
				if (JOptionPane.showConfirmDialog(null,
						"Error in loading the file Continue loading ?",
						"Opcat2 Error", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
					throw new Exception("Error Loading LogicalStructure");
				}
			}
			Element visualPart = system.getChild("VisualPart");
			this._loadVisualPart(visualPart);

			// reuse comment
			this.myProject.setDuringLoad(false);
			// end reuse comment

			this.myProject.setCurrentOpd(this.myProject
					.getComponentsStructure().getOpd(ROOT_OPD));

			// Added by Eran Toch (2/12/04). The root OPD is shown only
			// after
			// all
			// the elements were displayed
			this.myProject.showRootOpd();
			sentToBack(this.myProject);
			return this.myProject;
		} catch (Exception e) {
			OpcatLogger.logError(e);
			throw new LoadException("This is not valid Opcat2 file");
		}
	}

	private void sentToBack(OpdProject project) {
		Enumeration elements = project.getSystemStructure().getElements();
		while (elements.hasMoreElements()) {
			Entry entry = (Entry) elements.nextElement();
			Enumeration insEnum = entry.getInstances();
			while (insEnum.hasMoreElements()) {
				Instance ins = (Instance) insEnum.nextElement();
				if (ins.getGraphicalRepresentation() != null) {
					if (ins.getGraphicalRepresentation().isSentToBAck()) {
						ins.getGraphicalRepresentation().sendToBack();
					}
				}
			}
		}
	}

	// OPMSystem
	protected void _loadSystemAttributes(Element system) throws LoadException {
		this.myProject.setCreator(system.getAttributeValue("author"));
		this.myProject.setModelType(system.getAttributeValue("modeltype"));
		this.myProject.setName(system.getAttributeValue("name"));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {

			Date d = df.parse(system.getAttributeValue("creationDate"));
			this.myProject.setCreationDate(d);
		} catch (Exception t) {
			t.printStackTrace();
		}

		try {
			this.myProject.setEntityMaxEntry(system.getAttribute(
					"maxEntityEntry").getLongValue());
			this.myProject.setOpdMaxEntry(system.getAttribute("maxOpdEntry")
					.getLongValue());
		} catch (DataConversionException e) {
			throw new LoadException("This is not valid Opcat2 file");
		}
	}

	// SystemProperties
	protected void _loadSystemProperties(Element props) throws JDOMException {
		List propList = props.getChild("SystemConfiguration").getChildren(
				"Property");
		this._loadGenericTable(propList, this.myProject.getConfiguration());
		List propGenList = props.getChild("GeneralInfo")
				.getChildren("Property");
		this._loadGenericTable(propGenList, this.myProject.getGeneralInfo());

	}

	// Property list
	protected void _loadGenericTable(List props, GenericTable table)
			throws JDOMException {
		for (Iterator i = props.iterator(); i.hasNext();) {
			Element prop = (Element) i.next();
			table.setDBString(prop.getAttributeValue("key"), prop
					.getChildText("value"));
		}
	}

	// LogicalStructure
	protected boolean _loadLogicalStructure(Element logicalStruct)
			throws JDOMException {
		boolean error = false;

		error = this
				._loadObjectSection(logicalStruct.getChild("ObjectSection"));
		this.incrementProgressBar();
		error = this._loadProcessSection(logicalStruct
				.getChild("ProcessSection"));
		this.incrementProgressBar();
		error = this._loadRelationSection(logicalStruct
				.getChild("RelationSection"));
		this.incrementProgressBar();
		error = this._loadLinkSection(logicalStruct.getChild("LinkSection"));
		this.incrementProgressBar();

		return error;
	}

	// ObjectSection
	protected boolean _loadObjectSection(Element objectSect)
			throws JDOMException {
		boolean error = false;
		List objectsList = objectSect.getChildren("LogicalObject");
		for (Iterator it = objectsList.iterator(); it.hasNext();) {
			// LogicalObject
			Element xmlObject = (Element) it.next();
			ObjectEntry oe = this._loadLogicalObject(xmlObject);

			List statesList = xmlObject.getChildren("LogicalState");
			for (Iterator stit = statesList.iterator(); stit.hasNext();) {
				try {
					this._loadLogicalStates((Element) stit.next(), oe);
				} catch (Exception e) {
					error = true;
				}
			}
		}
		return error;
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
		if (xmlObject.getAttribute("typeOriginId") != null) {
			myObject.setTypeOriginId(xmlObject.getAttribute("typeOriginId")
					.getLongValue());
		}
		this.myProject.getComponentsStructure().put(objectId, myObject);
		return myObject;
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

	private void _loadExposeChange(Entry entity, Element exposeOP) {

		try {
			if (exposeOP != null) {
				boolean isChanged = exposeOP.getAttribute("changed")
						.getBooleanValue();
				if (isChanged) {
					OPCAT_EXPOSE_OP op = Enum.valueOf(OPCAT_EXPOSE_OP.class,
							exposeOP.getAttribute("op").getValue());

					if (op != OPCAT_EXPOSE_OP.NONE) {
						boolean isPrivate = exposeOP.getAttribute("private")
								.getBooleanValue();
						if (!isPrivate) {
							myProject.getExposeManager().addPublicExposeChange(
									entity, op);
						} else {
							myProject.getExposeManager()
									.addPrivateExposeChange(entity, op);
						}
					}
				}
			}
		} catch (Exception ex) {
			OpcatLogger
					.logError(new Exception(
							"Error loading expose history, trying to load rest of model",
							ex));
			OpcatLogger.logError(ex);
		}

	}

	// EntityAttr
	protected void _loadEntityAttr(Entry entity, Element xmlEntity)
			throws JDOMException {
		entity.setName(xmlEntity.getChildText("name"));
		entity.setEnvironmental(xmlEntity.getAttribute("environmental")
				.getBooleanValue());
		entity.setDescription(xmlEntity.getChildText("description"));

		entity.getLogicalEntity().setPublicExposed(
				(xmlEntity.getAttribute("exposed") != null ? xmlEntity
						.getAttribute("exposed").getBooleanValue() : false));

		entity.getLogicalEntity().setPrivateExposed(
				(xmlEntity.getAttribute("privateexposed") != null ? xmlEntity
						.getAttribute("privateexposed").getBooleanValue()
						: false));

		_loadExposeChange(entity, xmlEntity.getChild("ExposePublicChange"));
		_loadExposeChange(entity, xmlEntity.getChild("ExposePrivateChange"));

		if (xmlEntity.getChildText("iconName") != null) {

			String path = xmlEntity.getChildText("iconName");
			if (path.startsWith(".")) {
				String filename = null;
				if (loadingPath != null) {
					filename = loadingPath;
				} else {
					filename = Opcat2.getLoadedfilename();
				}
				int last = filename.lastIndexOf(FileControl.fileSeparator);
				path = filename.substring(0, last) + path.substring(1);
			}

			entity.setIcon(path);
		}

	}

	// ThingAttr
	protected void _loadThingAttr(ThingEntry thing, Element xmlThing)
			throws JDOMException {
		thing.setNumberOfMASInstances(xmlThing
				.getAttribute("numberOfInstances").getIntValue());
		thing.setPhysical(xmlThing.getAttribute("physical").getBooleanValue());
		thing.setRole(xmlThing.getAttributeValue("role"));
		thing.setScope(xmlThing.getAttributeValue("scope"));

	}

	// ProcessSection
	protected boolean _loadProcessSection(Element processSect)
			throws JDOMException {
		boolean error = false;
		List processesList = processSect.getChildren("LogicalProcess");

		for (Iterator it = processesList.iterator(); it.hasNext();) {
			Element xmlProcess = (Element) it.next();
			try {
				this._loadLogicalProcess(xmlProcess);
			} catch (Exception e) {
				error = true;
			}
		}

		return error;
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
		// myProcess.se
		this.myProject.getComponentsStructure().put(processId, myProcess);
	}

	// RelationSection
	protected boolean _loadRelationSection(Element relationSect)
			throws JDOMException {
		List relationsList = relationSect.getChildren("LogicalRelation");
		boolean error = false;

		for (Iterator it = relationsList.iterator(); it.hasNext();) {
			Element xmlRelation = (Element) it.next();
			try {
				this._loadLogicalRelation(xmlRelation);
			} catch (Exception e) {
				error = true;
			}
		}

		return error;
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

	// LinkSection
	protected boolean _loadLinkSection(Element linkSect) throws JDOMException {
		List linksList = linkSect.getChildren("LogicalLink");
		boolean error = false;

		for (Iterator it = linksList.iterator(); it.hasNext();) {
			Element xmlLink = (Element) it.next();
			try {
				this._loadLogicalLink(xmlLink);
			} catch (Exception e) {
				error = true;
			}
		}
		return error;
	}

	// LogicalLink
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

		this.myProject.getComponentsStructure().put(linkId, myLink);
	}

	// VisualPart
	protected void _loadVisualPart(Element vpt) throws JDOMException {
		this._loadOpd(vpt.getChild("OPD"), null, LoaderVer2.NO_PARENT);
	}

	// OPD
	protected Opd _loadOpd(Element xmlOpd, Opd parentOpd,
			int relationsWithParent) throws JDOMException {

		boolean error = false;
		String errorMsg = "";

		long opdId = xmlOpd.getAttribute("id").getLongValue();

		// Loading also the OPD name attribute
		Opd myOpd = new Opd(this.myProject, xmlOpd.getAttributeValue("name"),
				opdId, parentOpd);

		if (myOpd.getName().indexOf("-") > -1) {
			String header = myOpd.getName().substring(0,
					myOpd.getName().indexOf("-"));
			if (header.contains(Opd.getViewInitials())) {
				myOpd.setView(true);
				// renameAction.setEnabled(false) ;
			}
		}

		this.myProject.getComponentsStructure().put(opdId, myOpd);
		myOpd.getDrawingArea().setCursor(
				Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		myOpd.setEntityInOpdMaxEntry(xmlOpd.getAttribute("maxEntityEntry")
				.getLongValue());
		this.myProject.setCurrentOpd(myOpd);

		if (opdId != ROOT_OPD) {
			error = this._loadMainEntity(xmlOpd.getChild("MainEntity"), myOpd,
					relationsWithParent);
			if (error)
				errorMsg = errorMsg + "\n" + "Loding MainEntity of "
						+ myOpd.getName();
		}

		error = this._loadThingSection(xmlOpd.getChild("ThingSection"), myOpd);
		if (error)
			errorMsg = errorMsg + "\n" + "Loading Things of " + myOpd.getName();
		error = this._loadFundamentalRelationSection(xmlOpd
				.getChild("FundamentalRelationSection"), myOpd);
		if (error)
			errorMsg = errorMsg + "\n" + "Loading Fundamental Relation of "
					+ myOpd.getName();
		error = this._loadGeneralRelationSection(xmlOpd
				.getChild("GeneralRelationSection"), myOpd);
		if (error)
			errorMsg = errorMsg + "\n" + "Loading General Relation of "
					+ myOpd.getName();
		error = this._loadVisualLinkSection(xmlOpd
				.getChild("VisualLinkSection"), myOpd);
		if (error)
			errorMsg = errorMsg + "\n" + "Loading Visual Links of "
					+ myOpd.getName();
		this.incrementProgressBar();

		error = this._loadUnfolded(xmlOpd.getChild("Unfolded"), myOpd);
		if (error)
			errorMsg = errorMsg + "\n" + "Loading Unfolded of "
					+ myOpd.getName();

		error = this._loadView(xmlOpd.getChild("View"), myOpd);
		if (error)
			errorMsg = errorMsg + "\n" + "Loading View " + myOpd.getName();

		error = this._loadInzoomed(xmlOpd.getChild("InZoomed"), myOpd);
		if (error)
			errorMsg = errorMsg + "\n" + "Loading In-zoomed of "
					+ myOpd.getName();
		if (!errorMsg.equalsIgnoreCase("")) {
			OpcatLogger.logError(errorMsg);
			int ans = JOptionPane.showConfirmDialog(LoaderVer2.this.pBar,
					"File Contains errors in OPD - " + "\n" + errorMsg + "\n"
							+ "continue loading this OPD ?", "OPCAT II",
					JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
			if (ans == JOptionPane.NO_OPTION) {
				throw (new JDOMException("Error in loading OPD - " + errorMsg));
			}
		}
		return myOpd;
	}

	// MainEntity
	protected boolean _loadMainEntity(Element mainEntity, Opd myOpd,
			int mainEntityOrigin) throws JDOMException {
		boolean ret = false;
		try {
			Element xmlThing = mainEntity.getChild("VisualThing");

			ThingInstance tInstance = this._loadVisualThing(xmlThing, myOpd,
					null, mainEntityOrigin);

			myOpd.setMainInstance(tInstance);
			myOpd.setMainEntry((ThingEntry) tInstance.getEntry());
		} catch (Exception e) {
			OpcatLogger.logError(e);
			ret = true;
		}

		return ret;
	}

	// ThingSection
	protected boolean _loadThingSection(Element thingSect, Opd currentOpd)
			throws JDOMException {
		boolean ret = false;
		List thingList = thingSect.getChildren("VisualThing");

		for (Iterator it = thingList.iterator(); it.hasNext();) {
			try {
				Element vThing = (Element) it.next();
				this._loadVisualThing(vThing, currentOpd, null,
						LoaderVer2.NO_PARENT);
			} catch (Exception e) {
				ret = true;
				OpcatLogger.logError(e);
			}
		}
		return ret;
	}

	// VisualThing
	protected ThingInstance _loadVisualThing(Element xmlThing, Opd currentOpd,
			ThingInstance parent, int relationWithParent) throws Exception {
		ThingInstance myThing;

		Element tData = xmlThing.getChild("ThingData");

		if (tData.getChild("VisualProcess") != null) {
			myThing = this._loadVisualProcess(tData.getChild("VisualProcess"),
					currentOpd, parent);
		} else {
			myThing = this._loadVisualObject(tData.getChild("VisualObject"),
					currentOpd, parent);
		}

		Element childrenContainer = xmlThing.getChild("ChildrenContainer");
		List childrenList = childrenContainer.getChildren("VisualThing");

		if (relationWithParent == LoaderVer2.INZOOMED) {
			myThing.setZoomedIn(true);
		} else {
			myThing.setZoomedIn(false);
		}

		// myThing.setZoomedIn(!childrenList.isEmpty());

		for (Iterator it = childrenList.iterator(); it.hasNext();) {
			Element vThing = (Element) it.next();
			this._loadVisualThing(vThing, currentOpd, myThing,
					LoaderVer2.NO_PARENT);
		}

		return myThing;
	}

	protected OpcatExposeKey _loadExposeKey(Element xmlKey, Instance parent)
			throws Exception {
		OpcatExposeKey key = null;

		if (xmlKey != null) {
			if (xmlKey.getAttributes().size() == 4) {
				String modelURI = xmlKey.getAttribute("modeluri").getValue();
				long opmentityid = xmlKey.getAttribute("opmentityid")
						.getLongValue();
				long exposeid = xmlKey.getAttribute("exposeid").getLongValue();

				boolean privateKey = xmlKey.getAttribute("private")
						.getBooleanValue();

				key = new OpcatExposeKey(exposeid, modelURI, opmentityid,
						privateKey);
			}
		}

		return key;
	}

	// VisualProcess
	protected ProcessInstance _loadVisualProcess(Element xmlProcess,
			Opd currentOpd, ThingInstance parent) throws JDOMException {
		Element insAttr = xmlProcess.getChild("InstanceAttr");
		ProcessInstance myProcess;
		if (parent == null) {
			myProcess = this.myProject.addProcess(0, 0, currentOpd
					.getDrawingArea(), insAttr.getAttribute("entityId")
					.getLongValue(), insAttr.getAttribute("entityInOpdId")
					.getLongValue());
		} else {
			myProcess = this.myProject.addProcess(0, 0, parent
					.getConnectionEdge(), insAttr.getAttribute("entityId")
					.getLongValue(), insAttr.getAttribute("entityInOpdId")
					.getLongValue());

		}
		this._loadInstanceAttr(myProcess, insAttr);
		this._loadConnectionEdgeAttr(myProcess, xmlProcess
				.getChild("ConnectionEdgeAttr"));
		this._loadThingInstanceAttr(myProcess, xmlProcess
				.getChild("ThingInstanceAttr"));

		/**
		 * pointer key
		 */

		try {
			myProcess.setPointer(_loadExposeKey(xmlProcess
					.getChild("ExportKey"), myProcess));
			myProcess
					.setTemplateInstance(xmlProcess.getAttribute("istemplate") != null ? xmlProcess
							.getAttribute("istemplate").getBooleanValue()
							: false);

			myProcess.setExposeOriginal((xmlProcess
					.getAttribute("originalexpose") != null ? xmlProcess
					.getAttribute("originalexpose").getBooleanValue() : false));

			myProcess
					.setAdvisor((xmlProcess.getAttribute("advisorinstance") != null ? xmlProcess
							.getAttribute("advisorinstance").getBooleanValue()
							: false));

		} catch (Exception ex) {
			OpcatLogger.logError(new Exception(
					"Error loading expose usage, tring to load rest of file:"
							+ myProcess.getEntry().getName()));
		}

		return myProcess;
	}

	// VisualObject
	protected ObjectInstance _loadVisualObject(Element xmlObject,
			Opd currentOpd, ThingInstance parent) throws JDOMException {
		Element insAttr = xmlObject.getChild("InstanceAttr");
		ObjectInstance myObject;

		if (parent == null) {
			myObject = this.myProject.addObject(0, 0, currentOpd
					.getDrawingArea(), insAttr.getAttribute("entityId")
					.getLongValue(), insAttr.getAttribute("entityInOpdId")
					.getLongValue(), false);
		} else {
			myObject = this.myProject.addObject(0, 0, parent
					.getConnectionEdge(), insAttr.getAttribute("entityId")
					.getLongValue(), insAttr.getAttribute("entityInOpdId")
					.getLongValue(), false);
		}

		this._loadInstanceAttr(myObject, insAttr);
		this._loadConnectionEdgeAttr(myObject, xmlObject
				.getChild("ConnectionEdgeAttr"));
		this._loadThingInstanceAttr(myObject, xmlObject
				.getChild("ThingInstanceAttr"));

		myObject.setStatesAutoArranged(xmlObject.getAttribute(
				"statesAutoArranged").getBooleanValue());

		/**
		 * pointer key
		 */

		try {
			myObject.setPointer(_loadExposeKey(xmlObject.getChild("ExportKey"),
					myObject));
			myObject
					.setTemplateInstance(xmlObject.getAttribute("istemplate") != null ? xmlObject
							.getAttribute("istemplate").getBooleanValue()
							: false);

			myObject.setExposeOriginal((xmlObject
					.getAttribute("originalexpose") != null ? xmlObject
					.getAttribute("originalexpose").getBooleanValue() : false));

		} catch (Exception ex) {
			OpcatLogger.logError(new Exception(
					"Error loading expose usage, tring to load rest of file:"
							+ myObject.getEntry().getName()));
		}

		for (Iterator i = xmlObject.getChildren("VisualState").iterator(); i
				.hasNext();) {
			this._loadVisualState((Element) i.next(), myObject, currentOpd);
		}

		return myObject;
	}

	// VisualState
	protected void _loadVisualState(Element xmlState,
			ObjectInstance parentInstance, Opd currentOpd) throws JDOMException {
		Element insAttr = xmlState.getChild("InstanceAttr");
		OpdKey myKey = new OpdKey(currentOpd.getOpdId(), insAttr.getAttribute(
				"entityInOpdId").getLongValue());

		StateEntry myEntry = (StateEntry) this.myProject
				.getComponentsStructure().getEntry(
						insAttr.getAttribute("entityId").getLongValue());
		StateInstance myInstance = new StateInstance(myEntry, myKey,
				(OpdObject) parentInstance.getConnectionEdge(), this.myProject);
		myEntry.addInstance(myKey, myInstance);

		this._loadInstanceAttr(myInstance, insAttr);
		this._loadConnectionEdgeAttr(myInstance, xmlState
				.getChild("ConnectionEdgeAttr"));
		myInstance.setVisible(xmlState.getAttribute("visible")
				.getBooleanValue());

		/**
		 * pointer key
		 */

		try {
			myInstance.setPointer(_loadExposeKey(
					xmlState.getChild("ExportKey"), myInstance));
			myInstance
					.setTemplateInstance(xmlState.getAttribute("istemplate") != null ? xmlState
							.getAttribute("istemplate").getBooleanValue()
							: false);
		} catch (Exception ex) {
			OpcatLogger.logError(new Exception(
					"Error loading expose usage, tring to load rest of file:"
							+ myInstance.getEntry().getName()));
		}

	}

	// InstanceAttr
	protected void _loadInstanceAttr(Instance ins, Element xmlInstance)
			throws JDOMException {
		ins.setBackgroundColor(new Color(xmlInstance.getAttribute(
				"backgroundColor").getIntValue()));
		ins.setBorderColor(new Color(xmlInstance.getAttribute("borderColor")
				.getIntValue()));
		ins.setTextColor(new Color(xmlInstance.getAttribute("textColor")
				.getIntValue()));
		xmlInstance
				.setAttribute("isVisible", Boolean.toString(ins.isVisible()));
		if (xmlInstance.getAttribute("isVisible") != null) {
			ins.setVisible(xmlInstance.getAttribute("isVisible")
					.getBooleanValue());
		}
	}

	// ConnectionEdgeAttr
	protected void _loadConnectionEdgeAttr(ConnectionEdgeInstance cEdge,
			Element xmlConEdge) throws JDOMException {
		cEdge.setLocation(xmlConEdge.getAttribute("x").getIntValue(),
				xmlConEdge.getAttribute("y").getIntValue());
		cEdge.setSize(xmlConEdge.getAttribute("width").getIntValue(),
				xmlConEdge.getAttribute("height").getIntValue());
		if (xmlConEdge.getAttribute("sentToBack") != null) {
			cEdge.getGraphicalRepresentation().setSentToBAck(
					xmlConEdge.getAttribute("sentToBack").getBooleanValue());
		}
	}

	// ThingInstanceAttr
	protected void _loadThingInstanceAttr(ThingInstance tInstance,
			Element xmlThingIns) throws JDOMException {
		tInstance.setLayer(xmlThingIns.getAttribute("layer").getIntValue());
		tInstance
				.setTextPosition(xmlThingIns.getAttributeValue("textPosition"));

		if (xmlThingIns.getAttributeValue("showIcon") != null) {
			OpdThing thing = (OpdThing) tInstance.getGraphicalRepresentation();
			thing.setIconVisible(Boolean.parseBoolean(xmlThingIns
					.getAttributeValue("showIcon")));
		}

	}

	// FundamentalRelationSection
	protected boolean _loadFundamentalRelationSection(Element relationSect,
			Opd currentOpd) throws JDOMException {
		boolean ret = false;
		List relationList = relationSect.getChildren("CommonPart");

		for (Iterator it = relationList.iterator(); it.hasNext();) {
			try {
				Element cPart = (Element) it.next();
				this._loadCommonPart(cPart, currentOpd);
			} catch (Exception e) {
				ret = true;
				OpcatLogger.logError(e);
			}
		}
		return ret;
	}

	// CommonPart
	protected void _loadCommonPart(Element cPart, Opd currentOpd)
			throws JDOMException {
		List relationList = cPart.getChildren("VisualFundamentalRelation");
		FundamentalRelationInstance fInstance = null;

		for (Iterator it = relationList.iterator(); it.hasNext();) {
			fInstance = this._loadVisualFundamentalRelation(cPart, (Element) it
					.next(), currentOpd);
		}

		if (fInstance == null) {
			return;
		}
		GraphicalRelationRepresentation repr = fInstance
				.getGraphicalRelationRepresentation();
		RelativeConnectionPoint sourcePoint = new RelativeConnectionPoint(cPart
				.getAttribute("sourceConnectionSide").getIntValue(),
				(double) cPart.getAttribute("sourceConnectionParameter")
						.getFloatValue());

		repr.getSourceDragger().setRelativeConnectionPoint(sourcePoint);
		repr.getSourceDragger().updateDragger();

		OpdFundamentalRelation triangle = repr.getRelation();
		triangle.setLocation(cPart.getAttribute("x").getIntValue(), cPart
				.getAttribute("y").getIntValue());
		triangle.setSize(cPart.getAttribute("width").getIntValue(), cPart
				.getAttribute("height").getIntValue());
		repr.setBackgroundColor(new Color(cPart.getAttribute("backgroundColor")
				.getIntValue()));
		repr.setBorderColor(new Color(cPart.getAttribute("borderColor")
				.getIntValue()));
		repr.arrangeLines();
	}

	// CommonPart VisualFundamentalRelation
	protected FundamentalRelationInstance _loadVisualFundamentalRelation(
			Element cPart, Element xmlRelation, Opd currentOpd)
			throws JDOMException {
		Element insAttr = xmlRelation.getChild("InstanceAttr");

		FundamentalRelationEntry myEntry = (FundamentalRelationEntry) this.myProject
				.getComponentsStructure().getEntry(
						insAttr.getAttribute("entityId").getLongValue());

		ConnectionEdgeEntry sourceEntry = (ConnectionEdgeEntry) this.myProject
				.getComponentsStructure().getEntry(
						cPart.getAttribute("sourceId").getLongValue());
		if (sourceEntry == null) {
			OpcatLogger.logError("Source Entry - NULL, ID In XMl file = "
					+ cPart.getAttribute("sourceId").getLongValue());
			OpcatLogger.logError("OPD - " + currentOpd.getName());
			OpcatLogger.logError("XML - " + xmlRelation.toString());
			return null;
		}
		ConnectionEdgeEntry destinationEntry = (ConnectionEdgeEntry) this.myProject
				.getComponentsStructure().getEntry(
						xmlRelation.getAttribute("destinationId")
								.getLongValue());
		if (destinationEntry == null) {
			OpcatLogger.logError("Source Entry - " + sourceEntry.getName());
			OpcatLogger.logError("Destination Entry - NULL, ID In XMl file = "
					+ xmlRelation.getAttribute("destinationId"));
			OpcatLogger.logError("OPD - " + currentOpd.getName());
			OpcatLogger.logError("XML - " + xmlRelation.toString());
			return null;
		}
		ConnectionEdgeInstance sourceInstance = (ConnectionEdgeInstance) (sourceEntry
				.getInstance(new OpdKey(currentOpd.getOpdId(), cPart
						.getAttribute("sourceInOpdId").getLongValue())));

		if (sourceInstance == null) {
			OpcatLogger.logError("Source Entry - " + sourceEntry.getName());
			OpcatLogger.logError("Destination Entry - "
					+ destinationEntry.getName());
			OpcatLogger.logError("Source Instance = NULL");
			OpcatLogger.logError("OPD - " + currentOpd.getName());
			OpcatLogger.logError("XML - " + xmlRelation.toString());
			return null;
		}
		OpdConnectionEdge source = sourceInstance.getConnectionEdge();
		ConnectionEdgeInstance destinationInstance = (ConnectionEdgeInstance) destinationEntry
				.getInstance(new OpdKey(currentOpd.getOpdId(), xmlRelation
						.getAttribute("destinationInOpdId").getLongValue()));
		OpdConnectionEdge destination = null;

		if (destinationInstance == null) {
			OpcatLogger.logError("Source Entry - " + sourceEntry.getName());
			OpcatLogger.logError("Destination Entry - "
					+ destinationEntry.getName());
			OpcatLogger.logError("Source Instance Key "
					+ sourceInstance.getKey());
			OpcatLogger.logError("OPD - " + currentOpd.getName());
			OpcatLogger.logError("XML - " + xmlRelation.toString());

			return null;
		}

		destination = destinationInstance.getConnectionEdge();

		JLayeredPane tempContainer;
		if (source.getParent() == destination.getParent()) {
			tempContainer = (JLayeredPane) source.getParent();
		} else {
			tempContainer = (JLayeredPane) currentOpd.getDrawingArea();
		}

		RelativeConnectionPoint sourcePoint = new RelativeConnectionPoint(cPart
				.getAttribute("sourceConnectionSide").getIntValue(),
				(double) cPart.getAttribute("sourceConnectionParameter")
						.getFloatValue());
		RelativeConnectionPoint destinationPoint = new RelativeConnectionPoint(
				xmlRelation.getAttribute("destinationSide").getIntValue(),
				(double) xmlRelation.getAttribute("destinationParameter")
						.getFloatValue());

		FundamentalRelationInstance myRelation = this.myProject.addRelation(
				source, sourcePoint, destination, destinationPoint, myEntry
						.getRelationType(), tempContainer, myEntry.getId(),
				insAttr.getAttribute("entityInOpdId").getLongValue());

		this._loadInstanceAttr(myRelation, insAttr);

		myRelation.update();
		myRelation.setAutoArranged(true);
		myRelation.arrangeLines();

		myRelation.getDestinationDragger().getOpdCardinalityLabel()
				.setLocation(xmlRelation.getAttribute("labelX").getIntValue(),
						xmlRelation.getAttribute("labelY").getIntValue());
		if (xmlRelation.getAttribute("isVisible") != null) {
			myRelation.setVisible(xmlRelation.getAttribute("isVisible")
					.getBooleanValue());
		}

		return myRelation;
	}

	// GeneralRelationSection
	protected boolean _loadGeneralRelationSection(Element relationSect,
			Opd currentOpd) throws JDOMException {

		boolean ret = false;
		List relationList = relationSect.getChildren("VisualGeneralRelation");

		for (Iterator it = relationList.iterator(); it.hasNext();) {
			try {
				Element vRelation = (Element) it.next();
				this._loadVisualGeneralRelation(vRelation, currentOpd);
			} catch (Exception e) {
				ret = true;
				OpcatLogger.logError(e);
			}
		}
		return ret;
	}

	// VisualGeneralRelation
	protected void _loadVisualGeneralRelation(Element xmlRelation,
			Opd currentOpd) throws JDOMException {
		Element insAttr = xmlRelation.getChild("InstanceAttr");
		Element lineAttr = xmlRelation.getChild("LineAttr");

		GeneralRelationEntry myEntry = (GeneralRelationEntry) this.myProject
				.getComponentsStructure().getEntry(
						insAttr.getAttribute("entityId").getLongValue());

		ConnectionEdgeEntry sourceEntry = (ConnectionEdgeEntry) this.myProject
				.getComponentsStructure().getEntry(
						lineAttr.getAttribute("sourceId").getLongValue());
		ConnectionEdgeEntry destinationEntry = (ConnectionEdgeEntry) this.myProject
				.getComponentsStructure().getEntry(
						lineAttr.getAttribute("destinationId").getLongValue());
		ConnectionEdgeInstance sourceInstance = (ConnectionEdgeInstance) (sourceEntry
				.getInstance(new OpdKey(currentOpd.getOpdId(), lineAttr
						.getAttribute("sourceInOpdId").getLongValue())));
		OpdConnectionEdge source = sourceInstance.getConnectionEdge();
		ConnectionEdgeInstance destinationInstance = (ConnectionEdgeInstance) destinationEntry
				.getInstance(new OpdKey(currentOpd.getOpdId(), lineAttr
						.getAttribute("destinationInOpdId").getLongValue()));
		OpdConnectionEdge destination = destinationInstance.getConnectionEdge();

		JLayeredPane tempContainer;
		if (source.getParent() == destination.getParent()) {
			tempContainer = (JLayeredPane) source.getParent();
		} else {
			tempContainer = (JLayeredPane) currentOpd.getDrawingArea();
		}

		RelativeConnectionPoint sourcePoint = new RelativeConnectionPoint(
				lineAttr.getAttribute("sourceConnectionSide").getIntValue(),
				(double) lineAttr.getAttribute("sourceConnectionParameter")
						.getFloatValue());
		RelativeConnectionPoint destinationPoint = new RelativeConnectionPoint(
				lineAttr.getAttribute("destinationConnectionSide")
						.getIntValue(), (double) lineAttr.getAttribute(
						"destinationConnectionParameter").getFloatValue());

		GeneralRelationInstance myRelation = this.myProject.addGeneralRelation(
				source, sourcePoint, destination, destinationPoint,
				tempContainer, myEntry.getRelationType(), myEntry.getId(),
				insAttr.getAttribute("entityInOpdId").getLongValue());

		this._loadInstanceAttr(myRelation, insAttr);

		Element points = xmlRelation.getChild("BreakPoints");
		if (points != null) {
			this._loadBreakPoints(points, myRelation.getLine());
		}

		if (lineAttr.getAttribute("autoArranged").getBooleanValue()) {
			myRelation.update();
			myRelation.setAutoArranged(true);
			myRelation.getLine().arrange();
		} else {
			myRelation.setAutoArranged(false);
			myRelation.getSourceDragger().setRelativeConnectionPoint(
					sourcePoint);
			myRelation.getDestinationDragger().setRelativeConnectionPoint(
					destinationPoint);
			myRelation.update();
		}

		((OpdRelationDragger) (myRelation.getSourceDragger()))
				.getOpdCardinalityLabel().setLocation(
						xmlRelation.getAttribute("sourceLabelX").getIntValue(),
						xmlRelation.getAttribute("sourceLabelY").getIntValue());
		((OpdRelationDragger) (myRelation.getDestinationDragger()))
				.getOpdCardinalityLabel().setLocation(
						xmlRelation.getAttribute("destinationLabelX")
								.getIntValue(),
						xmlRelation.getAttribute("destinationLabelY")
								.getIntValue());

	}

	// BreakPoints
	protected void _loadBreakPoints(Element points, StyledLine line)
			throws JDOMException {
		List pointsList = points.getChildren("Point");

		for (Iterator i = pointsList.iterator(); i.hasNext();) {
			Element xmlPoint = (Element) i.next();
			line.addPoint(new java.awt.Point(xmlPoint.getAttribute("x")
					.getIntValue(), xmlPoint.getAttribute("y").getIntValue()));
		}
	}

	// VisualLinkSection
	protected boolean _loadVisualLinkSection(Element linkSect, Opd currentOpd)
			throws JDOMException {
		boolean ret = false;
		List linkList = linkSect.getChildren("VisualLink");

		for (Iterator it = linkList.iterator(); it.hasNext();) {
			try {
				Element vLink = (Element) it.next();
				this._loadVisualLink(vLink, currentOpd);
			} catch (Exception e) {
				ret = true;
				OpcatLogger.logError(e);
			}
		}

		List orList = linkSect.getChildren("OrXorGroup");

		for (Iterator it = orList.iterator(); it.hasNext();) {
			try {
				Element xmlOr = (Element) it.next();
				this._loadOrXor(xmlOr, currentOpd);
			} catch (Exception e) {
				ret = true;
				OpcatLogger.logError(e);
			}
		}
		return ret;
	}

	// OrXorGroup
	protected void _loadOrXor(Element xmlOr, Opd currentOpd)
			throws JDOMException {
		List memberList = xmlOr.getChildren("Member");
		Element xmlMember = (Element) memberList.get(0);

		LinkEntry lEntry = (LinkEntry) this.myProject.getComponentsStructure()
				.getEntry(xmlMember.getAttribute("memberId").getLongValue());
		LinkInstance lInstance = (LinkInstance) lEntry.getInstance(new OpdKey(
				currentOpd.getOpdId(), xmlMember.getAttribute("memberInOpdId")
						.getLongValue()));

		if (lInstance == null) {
			System.err.println("Null Pointer in lInstance!"
					+ xmlMember.getAttributeValue("memberInOpdId"));
			return;
		}
		OrInstance myOr;

		if ((xmlOr.getAttribute("isSourceGroup") != null)
				&& xmlOr.getAttribute("isSourceGroup").getBooleanValue()) {
			myOr = lInstance.getSourceOr();
		} else {
			myOr = lInstance.getDestOr();
		}

		if (myOr == null) {
			return;
		}

		if ((xmlOr.getAttribute("type") != null)
				&& (xmlOr.getAttribute("type").getIntValue() == OR_TYPE)) {
			myOr.setOr(true);
		} else {
			myOr.setOr(false);
		}

	}

	// VisualLink
	protected void _loadVisualLink(Element xmlLink, Opd currentOpd)
			throws JDOMException {
		Element insAttr = xmlLink.getChild("InstanceAttr");
		Element lineAttr = xmlLink.getChild("LineAttr");

		LinkEntry myEntry = (LinkEntry) this.myProject.getComponentsStructure()
				.getEntry(insAttr.getAttribute("entityId").getLongValue());

		ConnectionEdgeEntry sourceEntry = (ConnectionEdgeEntry) this.myProject
				.getComponentsStructure().getEntry(
						lineAttr.getAttribute("sourceId").getLongValue());
		ConnectionEdgeEntry destinationEntry = (ConnectionEdgeEntry) this.myProject
				.getComponentsStructure().getEntry(
						lineAttr.getAttribute("destinationId").getLongValue());
		ConnectionEdgeInstance sourceInstance = (ConnectionEdgeInstance) (sourceEntry
				.getInstance(new OpdKey(currentOpd.getOpdId(), lineAttr
						.getAttribute("sourceInOpdId").getLongValue())));
		OpdConnectionEdge source = sourceInstance.getConnectionEdge();
		ConnectionEdgeInstance destinationInstance = (ConnectionEdgeInstance) destinationEntry
				.getInstance(new OpdKey(currentOpd.getOpdId(), lineAttr
						.getAttribute("destinationInOpdId").getLongValue()));
		if (destinationInstance == null) {
			OpcatLogger.logError("Source  " + sourceEntry.getName());
			OpcatLogger.logError("Dest " + destinationEntry.getName());
			OpcatLogger.logError("Dest in OPD ID "
					+ lineAttr.getAttribute("destinationInOpdId")
							.getLongValue());
		}
		OpdConnectionEdge destination = destinationInstance.getConnectionEdge();

		JLayeredPane tempContainer;
		if (source.getParent() == destination.getParent()) {
			tempContainer = (JLayeredPane) source.getParent();
		} else {
			tempContainer = (JLayeredPane) currentOpd.getDrawingArea();
		}
		RelativeConnectionPoint sourcePoint = new RelativeConnectionPoint(
				lineAttr.getAttribute("sourceConnectionSide").getIntValue(),
				(double) lineAttr.getAttribute("sourceConnectionParameter")
						.getFloatValue());
		RelativeConnectionPoint destinationPoint = new RelativeConnectionPoint(
				lineAttr.getAttribute("destinationConnectionSide")
						.getIntValue(), (double) lineAttr.getAttribute(
						"destinationConnectionParameter").getFloatValue());

		LinkInstance myLink = this.myProject.addLink(source, sourcePoint,
				destination, destinationPoint, tempContainer, myEntry
						.getLinkType(), myEntry.getId(), insAttr.getAttribute(
						"entityInOpdId").getLongValue());

		this._loadInstanceAttr(myLink, insAttr);

		Element points = xmlLink.getChild("BreakPoints");
		if (points != null) {
			this._loadBreakPoints(points, myLink.getLine());
		}

		if (lineAttr.getAttribute("autoArranged").getBooleanValue()) {
			myLink.update();
			myLink.setAutoArranged(true);
			myLink.getLine().arrange();
		} else {
			myLink.setAutoArranged(false);
			myLink.getSourceDragger().setRelativeConnectionPoint(sourcePoint);
			myLink.getDestinationDragger().setRelativeConnectionPoint(
					destinationPoint);
			myLink.update();
			myLink.updateOrConnections();
		}

	}

	// InZoomed
	protected boolean _loadInzoomed(Element inzoomed, Opd parentOpd)
			throws JDOMException {
		boolean ret = false;
		for (Iterator it = inzoomed.getChildren("OPD").iterator(); it.hasNext();) {
			try {
				Opd newOpd = this._loadOpd((Element) it.next(), parentOpd,
						LoaderVer2.INZOOMED);
				newOpd.getMainEntry().setZoomedInOpd(newOpd);
				newOpd.getMainEntry().updateInstances();
			} catch (Exception e) {
				OpcatLogger.logError(e);
				ret = true;
			}
		}
		return ret;
	}

	// Unfolded ,OPD
	protected boolean _loadUnfolded(Element unfolded, Opd parentOpd)
			throws JDOMException {

		boolean ret = false;
		for (Iterator it = unfolded.getChildren("UnfoldingProperties")
				.iterator(); it.hasNext();) {
			try {
				Element xmlOpd = (Element) it.next();
				// ThingEntry
				ThingEntry unfoldedEntry = (ThingEntry) this.myProject
						.getComponentsStructure().getEntry(
								xmlOpd.getAttribute("entityId").getLongValue());

				// ThingInstance unfoldedInstance = (ThingInstance)
				// unfoldedEntry
				// .getInstance(new OpdKey(parentOpd.getOpdId(), xmlOpd
				// .getAttribute("entityInOpdId").getLongValue()));
				Opd opd = this._loadOpd(xmlOpd.getChild("OPD"), parentOpd,
						LoaderVer2.UNFOLDED);
				Iterator insIter = Collections.list(
						unfoldedEntry.getInstances()).iterator();
				while (insIter.hasNext()) {
					ThingInstance unfoldedInstance = (ThingInstance) insIter
							.next();
					unfoldedInstance.setUnfoldingOpd(opd);
					unfoldedInstance.update();
				}
			} catch (Exception e) {
				OpcatLogger.logError(e);
				ret = true;
			}
		}

		return ret;
	}

	protected boolean _loadView(Element unfolded, Opd parentOpd)
			throws JDOMException {

		boolean ret = false;
		if ((unfolded != null)
				&& (unfolded.getChildren("ViewProperties") != null)) {

			for (Iterator it = unfolded.getChildren("ViewProperties")
					.iterator(); it.hasNext();) {
				try {
					Element xmlOpd = (Element) it.next();
					this._loadOpd(xmlOpd.getChild("OPD"), parentOpd,
							LoaderVer2.UNFOLDED);
				} catch (Exception e) {
					OpcatLogger.logError(e);
					ret = true;
				}
			}

		}

		return ret;
	}

	// Accepts OPX
	protected void _prepareProgressBar(Element rootTag) {
		Element rootOPD = rootTag.getChild("OPMSystem").getChild("VisualPart")
				.getChild("OPD");
		this.numOfOpds = 0;
		this._countSubTree(rootOPD);
		if (this.pBar != null) {
			this.pBar.setMinimum(0);
			this.pBar.setMaximum(this.numOfOpds + 4);
			this.pBar.setValue(0);
		}
	}

	// Element OPD
	protected void _countSubTree(Element opd) {
		this.numOfOpds++;
		List inZoomed = opd.getChild("InZoomed").getChildren("OPD");
		for (Iterator i1 = inZoomed.iterator(); i1.hasNext();) {
			// OPDType
			this._countSubTree((Element) i1.next());
		}
		List unfolded = opd.getChild("Unfolded").getChildren(
				"UnfoldingProperties");
		for (Iterator i2 = unfolded.iterator(); i2.hasNext();) {
			Element upt = (Element) i2.next();
			this._countSubTree(upt.getChild("OPD"));
		}

	}

	protected void incrementProgressBar() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (LoaderVer2.this.pBar != null) {
					synchronized (LoaderVer2.this.pBar) {
						LoaderVer2.this.pBar.setValue(LoaderVer2.this.pBar
								.getValue() + 1);
					}
				}
			}
		});

	}

	@Override
	public void setPath(String path) {
		this.path = path;
	}
}
