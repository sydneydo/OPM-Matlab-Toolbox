package gui.opx.ver2;

import expose.OpcatExposeKey;
import gui.actions.expose.OpcatExposeChange;
import gui.opdGraphics.lines.StyledLine;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;
import gui.opdGraphics.opdBaseComponents.OpdFundamentalRelation;
import gui.opdGraphics.opdBaseComponents.OpdThing;
import gui.opdProject.GenericTable;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.opx.SaveException;
import gui.opx.SaverI;
import gui.projectStructure.ConnectionEdgeInstance;
import gui.projectStructure.Entry;
import gui.projectStructure.FundamentalRelationInstance;
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

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JProgressBar;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format.TextMode;

/**
 * 
 * @author lera t To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class SaverVer2 implements SaverI {
	public String CURRENT_VERSION;

	protected final static int ROOT_OPD = 1;

	protected final static int OR_TYPE = 0;

	protected final static int XOR_TYPE = 1;

	protected XMLOutputter outp = new XMLOutputter();

	protected Document doc;

	protected OpdProject myProject;

	public SaverVer2() {
		this.CURRENT_VERSION = "2";
	}

	public String getVersion() {
		return this.CURRENT_VERSION;
	}

	public void save(OpdProject project, OutputStream os,
			JProgressBar progressBar) throws SaveException {
		try {
			this.myProject = project;
			this.doc = new Document();
			Element mainTag = new Element("OPX");
			this.doc.setRootElement(mainTag);
			mainTag.setAttribute("version", this.getVersion());
			Element system = new Element("OPMSystem");
			mainTag.addContent(system);
			// creationDate="2003-06-24T16:44:29.000+02:00"
			// lastUpdate="2003-07-18T13:17:05.218+02:00"
			system
					.setAttribute("creationDate",
							"2003-06-24T16:44:29.000+02:00");
			system.setAttribute("lastUpdate", "2003-06-24T16:44:29.000+02:00");
			this._saveSystemAttributes(system);
			system.addContent(this._saveSystemProperties());
			system.addContent(this._saveLogicalStructure());
			system.addContent(this._saveVisualPart());
			Format format = this.outp.getFormat();
			format.setIndent("      ");
			format.setTextMode(TextMode.TRIM);
			format.setLineSeparator(System.getProperty("line.separator"));
			// outp.setTextTrim(true);
			// outp.setNewlines(true);
			this.outp.output(this.doc, os);
		} catch (JDOMException dome) {
			SaveException sex = new SaveException(dome.getMessage());
			OpcatLogger.logError(dome);
			throw sex;
		} catch (IOException ioe) {
			SaveException sex = new SaveException(ioe.getMessage());
			OpcatLogger.logError(ioe);
			throw sex;
		} catch (Exception ex) {
			SaveException sex = new SaveException(ex.getMessage());
			OpcatLogger.logError(ex);
			throw sex;
		}

	}

	// Gets OPMSystem
	protected void _saveSystemAttributes(Element system) {
		system.setAttribute("author", this.myProject.getCreator());
		system.setAttribute("modeltype", myProject.getCurrentModelType());
		system.setAttribute("name", this.myProject.getName());

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		system.setAttribute("creationDate", df.format(this.myProject
				.getCreationDate()));
		GregorianCalendar cal = new GregorianCalendar();
		system.setAttribute("lastUpdate", df.format(cal.getTime()));
		Attribute attr = new Attribute("maxEntityEntry", Long
				.toString(this.myProject.getEntityMaxEntry()));
		system.setAttribute(attr);
		attr = new Attribute("maxOpdEntry", Long.toString(this.myProject
				.getOpdMaxEntry()));
		system.setAttribute(attr);
	}

	// SystemProperties
	protected Element _saveSystemProperties() throws JDOMException {
		Element props = new Element("SystemProperties");
		Element cnfg = new Element("SystemConfiguration");
		this._saveGenericTable(cnfg, this.myProject.getConfiguration());
		props.addContent(cnfg);
		Element genInfo = new Element("GeneralInfo");
		this._saveGenericTable(genInfo, this.myProject.getGeneralInfo());
		props.addContent(genInfo);

		return props;
	}

	// Gets cnfg - SystemConfiguration and adds to it list of "Property"
	// elements
	protected void _saveGenericTable(Element cnfg, GenericTable table)
			throws JDOMException {
		List propsList = new LinkedList();
		for (Enumeration e = table.propertyNames(); e.hasMoreElements();) {
			String propName = (String) e.nextElement();
			String propValue = table.getDBString(propName);
			Element prop = new Element("Property");
			prop.setAttribute("key", propName);
			Element val = new Element("value");
			val.setText(propValue);
			prop.addContent(val);
			propsList.add(prop);
		}
		cnfg.addContent(propsList);
	}

	// LogicalStructure
	protected Element _saveLogicalStructure() throws JDOMException {
		Element logicalStruct = new Element("LogicalStructure");
		logicalStruct.addContent(this._saveObjectSection());
		logicalStruct.addContent(this._saveProcessSection());
		logicalStruct.addContent(this._saveRelationSection());
		logicalStruct.addContent(this._saveLinkSection());
		return logicalStruct;
	}

	protected Element _saveObjectSection() throws JDOMException {
		Element objectSect = new Element("ObjectSection");
		for (Enumeration e = this.myProject.getComponentsStructure()
				.getElements(); e.hasMoreElements();) {
			Entry currentEntry = (Entry) (e.nextElement());
			if (currentEntry instanceof ObjectEntry) {
				this._saveLogicalObject((ObjectEntry) currentEntry, objectSect);
			}
		}
		return objectSect;
	}

	protected void _saveLogicalObject(ObjectEntry objEntry, Element obj)
			throws JDOMException {
		Element xmlObject = new Element("LogicalObject");
		xmlObject.addContent(this._saveEntityAttr(objEntry));
		xmlObject.addContent(this._saveThingAttr(objEntry));

		xmlObject.setAttribute("indexName", objEntry.getIndexName());
		xmlObject.setAttribute("indexOrder", Integer.toString(objEntry
				.getIndexOrder()));
		xmlObject.setAttribute("initialValue", objEntry.getInitialValue());
		xmlObject.setAttribute("key", Boolean.toString(objEntry.isKey()));
		xmlObject.setAttribute("persistent", Boolean.toString(objEntry
				.isPersistent()));
		xmlObject.setAttribute("type", objEntry.getType());
		xmlObject.setAttribute("typeOriginId", Long.toString(objEntry
				.getTypeOriginId()));

		this._saveLogicalStates(xmlObject, objEntry);
		if (obj.getChildren("LogicalObject") != null) {
			obj.getChildren("LogicalObject").add(xmlObject);
		} else {
			obj.addContent(xmlObject);
		}
	}

	protected void _saveLogicalStates(Element obj, ObjectEntry parentEntry)
			throws JDOMException {
		List statesList = new LinkedList();
		for (Enumeration e = parentEntry.getStates(); e.hasMoreElements();) {
			StateEntry state = (StateEntry) e.nextElement();

			Element xmlState = new Element("LogicalState");
			xmlState.addContent(this._saveEntityAttr(state));
			xmlState.setAttribute("default", Boolean
					.toString(state.isDefault()));
			xmlState.setAttribute("final", Boolean.toString(state.isFinal()));
			// xmlState.setFinal(state.isFinal());
			xmlState.setAttribute("initial", Boolean
					.toString(state.isInitial()));
			// xmlState.setInitial(state.isInitial());
			xmlState.setAttribute("maxTime", state.getMaxTime());
			xmlState.setAttribute("minTime", state.getMinTime());
			statesList.add(xmlState);
		}
		obj.addContent(statesList);
	}

	protected Element _saveEntityAttr(Entry entity) throws JDOMException {
		Element xmlEntity = new Element("EntityAttr");// factory.createEntityAttrType();
		xmlEntity.setAttribute("id", Long.toString(entity.getId()));
		Element name = new Element("name");
		name.addContent(entity.getName());
		xmlEntity.addContent(name);
		name = new Element("description");
		name.addContent(entity.getDescription());
		xmlEntity.addContent(name);
		xmlEntity.setAttribute("environmental", Boolean.toString(entity
				.isEnvironmental()));

		xmlEntity.setAttribute("exposed", Boolean.toString(entity
				.getLogicalEntity().isPublicExposed()));

		xmlEntity.setAttribute("privateexposed", Boolean.toString(entity
				.getLogicalEntity().isPrivateExposed()));

		// if (entity.getLogicalEntity().isExposed()) {

		// }

		try {
			xmlEntity.addContent(_saveExposePrivateChange(myProject
					.getExposeManager().getExposeLastChangeOp(
							entity.getLogicalEntity().getId(), true)));

			xmlEntity.addContent(_saveExposePublicChange(myProject
					.getExposeManager().getExposeLastChangeOp(
							entity.getLogicalEntity().getId(), false)));

		} catch (Exception ex) {
			OpcatLogger.logError(new Exception("Error saving expose history",
					ex));
		}

		name = new Element("iconName");
		name.addContent(entity.getIconPath());
		xmlEntity.addContent(name);

		return xmlEntity;
	}

	protected Element _saveThingAttr(ThingEntry thing) throws JDOMException {
		Element xmlThing = new Element("ThingAttr");// factory.createThingAttrType();
		xmlThing.setAttribute("numberOfInstances", Integer.toString(thing
				.getNumberOfMASInstances()));
		xmlThing.setAttribute("physical", Boolean.toString(thing.isPhysical()));
		xmlThing.setAttribute("role", thing.getFreeTextRole());
		xmlThing.setAttribute("scope", thing.getScope());

		return xmlThing;
	}

	// ProcessSection
	protected Element _saveProcessSection() throws JDOMException {
		Element processSect = new Element("ProcessSection");

		for (Enumeration e = this.myProject.getComponentsStructure()
				.getElements(); e.hasMoreElements();) {
			Entry currentEntry = (Entry) (e.nextElement());
			if (currentEntry instanceof ProcessEntry) {

				this._saveLogicalProcess((ProcessEntry) currentEntry,
						processSect);
			}

		}

		return processSect;
	}

	protected void _saveLogicalProcess(ProcessEntry procEntry,
			Element processSect) throws JDOMException {
		Element xmlProcess = new Element("LogicalProcess");
		xmlProcess.addContent(this._saveEntityAttr(procEntry));
		xmlProcess.addContent(this._saveThingAttr(procEntry));
		xmlProcess.setAttribute("maxTimeActivation", procEntry
				.getMaxTimeActivation());
		xmlProcess.setAttribute("minTimeActivation", procEntry
				.getMinTimeActivation());
		Element procBody = new Element("processBody");
		procBody.addContent(procEntry.getProcessBody());
		xmlProcess.addContent(procBody);
		if (processSect.getChildren("LogicalProcess") != null) {
			processSect.getChildren("LogicalProcess").add(xmlProcess);
		} else {
			processSect.addContent(xmlProcess);
		}
	}

	// RelationSection
	protected Element _saveRelationSection() throws JDOMException {
		Element relationSect = new Element("RelationSection");

		for (Enumeration e = this.myProject.getComponentsStructure()
				.getElements(); e.hasMoreElements();) {
			Entry currentEntry = (Entry) (e.nextElement());
			if (currentEntry instanceof RelationEntry) {
				this._saveLogicalRelation((RelationEntry) currentEntry,
						relationSect);
			}

		}

		return relationSect;
	}

	protected void _saveLogicalRelation(RelationEntry relEntry,
			Element relationSect) throws JDOMException {
		Element xmlRelation = new Element("LogicalRelation");
		xmlRelation.addContent(this._saveEntityAttr(relEntry));
		xmlRelation.setAttribute("relationType", Integer.toString(relEntry
				.getRelationType()));
		xmlRelation.setAttribute("sourceId", Long.toString(relEntry
				.getSourceId()));
		xmlRelation.setAttribute("destinationId", Long.toString(relEntry
				.getDestinationId()));
		xmlRelation.setAttribute("sourceCardinality", relEntry
				.getSourceCardinality());
		xmlRelation.setAttribute("destinationCardinality", relEntry
				.getDestinationCardinality());
		xmlRelation.setAttribute("forwardRelationMeaning", relEntry
				.getForwardRelationMeaning());
		xmlRelation.setAttribute("backwardRelationMeaning", relEntry
				.getBackwardRelationMeaning());
		if (relationSect.getChildren("LogicalRelation") != null) {
			relationSect.getChildren("LogicalRelation").add(xmlRelation);
		} else {
			relationSect.addContent(xmlRelation);
		}
	}

	// LinkSection
	protected Element _saveLinkSection() throws JDOMException {
		Element linkSect = new Element("LinkSection");

		for (Enumeration e = this.myProject.getComponentsStructure()
				.getElements(); e.hasMoreElements();) {
			Entry currentEntry = (Entry) (e.nextElement());
			if (currentEntry instanceof LinkEntry) {
				this._saveLogicalLink((LinkEntry) currentEntry, linkSect);
			}

		}

		return linkSect;
	}

	protected void _saveLogicalLink(LinkEntry lEntry, Element link)
			throws JDOMException {
		Element xmlLink = new Element("LogicalLink");

		xmlLink.addContent(this._saveEntityAttr(lEntry));
		xmlLink
				.setAttribute("linkType", Integer
						.toString(lEntry.getLinkType()));
		xmlLink.setAttribute("sourceId", Long.toString(lEntry.getSourceId()));
		xmlLink.setAttribute("destinationId", Long.toString(lEntry
				.getDestinationId()));
		xmlLink.setAttribute("condition", lEntry.getCondition());
		xmlLink.setAttribute("path", lEntry.getPath());
		xmlLink.setAttribute("maxReactionTime", lEntry.getMaxReactionTime());
		xmlLink.setAttribute("minReactionTime", lEntry.getMinReactionTime());
		if (link.getChildren("LogicalLink") != null) {
			link.getChildren("LogicalLink").add(xmlLink);
		} else {
			link.addContent(xmlLink);
		}
	}

	// VisualPart
	protected Element _saveVisualPart() throws JDOMException {
		Element vpt = new Element("VisualPart");
		Hashtable opdDependencies = this._prepareOpdsDependencies();
		vpt.addContent(this._saveOpd(ROOT_OPD, opdDependencies));
		return vpt;
	}

	/**
	 * Creates an OPX representation of an OPD. Uses
	 * <code>_appendOpdPropetries</code> in order to render the properties of
	 * the actual OPD, and <code>_appendOpdPropetries</code> in order to render
	 * the subelements of the OPD.
	 * 
	 * @param opdNum
	 *            The number of OPD
	 */
	protected Element _saveOpd(long opdNum, Hashtable opdDependencies)
			throws JDOMException {
		Element opd = new Element("OPD");
		Opd myOpd = this.myProject.getComponentsStructure().getOpd(opdNum);
		myOpd.setViewZoomIn(false);
		opd = this._appendOpdPropetries(opd, myOpd);
		opd = this._appendOpdChildren(opdNum, opdDependencies, opd, myOpd);
		return opd;
	}

	/**
	 * Saves the properties of the OPD element.
	 * 
	 * @param opd
	 *            The XML Element rendering of the OPD
	 * @param myOpd
	 *            The original OPD object
	 * @return A modified opd XML element
	 * @throws JDOMException
	 */
	protected Element _appendOpdPropetries(Element opd, Opd myOpd)
			throws JDOMException {
		opd.setAttribute("id", Long.toString(myOpd.getOpdId()));
		opd.setAttribute("name", myOpd.getName());
		opd.setAttribute("maxEntityEntry", Long.toString(myOpd
				.getEntityInOpdMaxEntry()));
		return opd;
	}

	/**
	 * Renders the OPD subelements, such as tjomgs. fundumental relations etc.
	 * 
	 * @throws JDOMException
	 */
	protected Element _appendOpdChildren(long opdNum,
			Hashtable opdDependencies, Element opd, Opd myOpd)
			throws JDOMException {
		Hashtable parentChildren = this
				._prepareParentChildrenDependencies(opdNum);

		if (opdNum != ROOT_OPD) {
			opd.addContent(this._saveMainEntity(myOpd.getMainInstance(),
					opdNum, parentChildren));
		}

		opd.addContent(this._saveThingSection(opdNum, parentChildren));
		opd.addContent(this._saveFundamentalRelationSection(opdNum));
		opd.addContent(this._saveGeneralRelationSection(opdNum));
		opd.addContent(this._saveVisualLinkSection(opdNum));
		opd.addContent(this._saveUnfolded(opdNum, opdDependencies));
		opd.addContent(this._saveView(opdNum, opdDependencies));
		opd.addContent(this._saveInZoomed(opdNum, opdDependencies));

		return opd;
	}

	// MainEntity
	protected Element _saveMainEntity(ThingInstance mainInstance, long opdNum,
			Hashtable parentChildren) throws JDOMException {
		Element mainEntity = new Element("MainEntity");
		LinkedList rootList = (LinkedList) parentChildren.get(new Long(0));
		rootList.remove(mainInstance);
		mainEntity.addContent(this._saveVisualThing(mainInstance,
				parentChildren));

		return mainEntity;
	}

	// ThingSection
	protected Element _saveThingSection(long opdNum, Hashtable parentChildren)
			throws JDOMException {
		Element thingSect = new Element("ThingSection");
		List thingList = new LinkedList();// thingSect.getVisualThing();

		LinkedList rootList = (LinkedList) parentChildren.get(new Long(0));

		for (Iterator it = rootList.iterator(); it.hasNext();) {
			ThingInstance instance = (ThingInstance) it.next();
			thingList.add(this._saveVisualThing(instance, parentChildren));
		}
		thingSect.addContent(thingList);
		return thingSect;
	}

	protected Hashtable _prepareParentChildrenDependencies(long opdNum) {
		Hashtable parentChildren = new Hashtable();
		LinkedList rootList = new LinkedList();
		parentChildren.put(new Long(0), rootList);

		for (Enumeration e = this.myProject.getComponentsStructure()
				.getThingsInOpd(opdNum); e.hasMoreElements();) {
			ThingInstance currIns = (ThingInstance) e.nextElement();
			java.awt.Container parent = currIns.getConnectionEdge().getParent();

			if (!(parent instanceof OpdThing)) {
				rootList.add(currIns);
			} else {
				OpdThing parentThing = (OpdThing) parent;
				LinkedList tempList = (LinkedList) parentChildren.get(new Long(
						parentThing.getEntityInOpdId()));
				if (tempList == null) {
					tempList = new LinkedList();
					parentChildren.put(
							new Long(parentThing.getEntityInOpdId()), tempList);
				}
				tempList.add(currIns);
			}
		}

		return parentChildren;
	}

	protected Element _saveExportKey(OpcatExposeKey key) {
		Element tData = new Element("ExportKey");
		if (key != null) {
			tData.setAttribute("modeluri", key.getModelEncodedURI());
			tData.setAttribute("opmentityid", Long.toString(key
					.getOpmEntityID()));
			tData.setAttribute("exposeid", Long.toString(key.getId()));
			tData.setAttribute("private", Boolean.toString(key.isPrivate()));
		}

		return tData;
	}

	protected Element _saveExposePublicChange(OpcatExposeChange change) {
		return _saveExposeChange(change, "ExposePublicChange");
	}

	protected Element _saveExposePrivateChange(OpcatExposeChange change) {
		return _saveExposeChange(change, "ExposePrivateChange");
	}

	private Element _saveExposeChange(OpcatExposeChange change, String name) {
		Element tData = new Element(name);

		if (change != null) {
			tData.setAttribute("private", Boolean.toString(change
					.isPrivateAction()));
			tData.setAttribute("op", change.getOp().toString());

			tData.setAttribute("changed", Boolean.toString(true));
		} else {
			tData.setAttribute("changed", Boolean.toString(false));

		}

		return tData;
	}

	// VisualThing
	protected Element _saveVisualThing(ThingInstance tInstance,
			Hashtable parentChildren) throws JDOMException {
		Element vThing = new Element("VisualThing");
		Element tData = new Element("ThingData");

		if (tInstance instanceof ProcessInstance) {
			tData.addContent(this
					._saveVisualProcess((ProcessInstance) tInstance));
		} else {
			tData
					.addContent(this
							._saveVisualObject((ObjectInstance) tInstance));
		}

		vThing.addContent(tData);
		Element childrenCont = new Element("ChildrenContainer");
		vThing.addContent(childrenCont);

		LinkedList childrenList = (LinkedList) parentChildren.get(new Long(
				tInstance.getKey().getEntityInOpdId()));

		if (childrenList != null) {
			List xmlChildrenList = new LinkedList();// childrenCont.getVisualThing();

			for (Iterator it = childrenList.iterator(); it.hasNext();) {
				xmlChildrenList.add(this._saveVisualThing((ThingInstance) it
						.next(), parentChildren));
			}
			childrenCont.addContent(xmlChildrenList);
		}

		return vThing;
	}

	// VisualProcess
	protected Element _saveVisualProcess(ProcessInstance proc)
			throws JDOMException {
		Element xmlProcess = new Element("VisualProcess");

		xmlProcess.addContent(this._saveInstanceAttr(proc));
		xmlProcess.addContent(this._saveConnectionEdgeAttr(proc));
		xmlProcess.addContent(this._saveThingInstanceAttr(proc));
		xmlProcess.addContent(_saveExportKey(proc.getPointer()));
		xmlProcess.setAttribute("istemplate", Boolean.toString(proc
				.isTemplateInstance()));
		xmlProcess.setAttribute("originalexpose", Boolean.toString(proc
				.isExposeOriginal()));
		xmlProcess.setAttribute("advisorinstance", Boolean.toString(proc
				.isAdvisor()));

		return xmlProcess;

	}

	// VisualObject
	protected Element _saveVisualObject(ObjectInstance obj)
			throws JDOMException {
		Element xmlObject = new Element("VisualObject");

		xmlObject.addContent(this._saveInstanceAttr(obj));
		xmlObject.addContent(this._saveConnectionEdgeAttr(obj));
		xmlObject.addContent(this._saveThingInstanceAttr(obj));
		xmlObject.setAttribute("statesAutoArranged", Boolean.toString(obj
				.isStatesAutoArranged()));
		xmlObject.addContent(_saveExportKey(obj.getPointer()));
		xmlObject.setAttribute("originalexpose", Boolean.toString(obj
				.isExposeOriginal()));
		xmlObject.setAttribute("istemplate", Boolean.toString(obj
				.isTemplateInstance()));

		List statesList = new LinkedList();// xmlObject.getVisualState();
		for (Enumeration e = obj.getStateInstances(); e.hasMoreElements();) {
			statesList.add(this._saveVisualState((StateInstance) e
					.nextElement()));
		}
		xmlObject.addContent(statesList);
		return xmlObject;
	}

	// VisualState
	protected Element _saveVisualState(StateInstance state)
			throws JDOMException {
		Element xmlState = new Element("VisualState");

		xmlState.addContent(this._saveInstanceAttr(state));
		xmlState.addContent(this._saveConnectionEdgeAttr(state));
		xmlState.setAttribute("visible", Boolean.toString(state.isVisible()));
		xmlState.addContent(_saveExportKey(state.getPointer()));
		xmlState.setAttribute("istemplate", Boolean.toString(state
				.isTemplateInstance()));

		return xmlState;
	}

	// InstanceAttr
	protected Element _saveInstanceAttr(Instance ins) throws JDOMException {
		Element xmlInstance = new Element("InstanceAttr");
		xmlInstance.setAttribute("entityId", Long.toString(ins.getLogicalId()));
		xmlInstance.setAttribute("entityInOpdId", Long.toString(ins.getKey()
				.getEntityInOpdId()));
		xmlInstance.setAttribute("backgroundColor", Integer.toString(ins
				.getBackgroundColor().getRGB()));
		xmlInstance.setAttribute("borderColor", Integer.toString(ins
				.getBorderColor().getRGB()));
		xmlInstance.setAttribute("textColor", Integer.toString(ins
				.getTextColor().getRGB()));
		xmlInstance
				.setAttribute("isVisible", Boolean.toString(ins.isVisible()));
		return xmlInstance;
	}

	// ConnectionEdgeAttr
	protected Element _saveConnectionEdgeAttr(ConnectionEdgeInstance cEdge)
			throws JDOMException {
		Element xmlConEdge = new Element("ConnectionEdgeAttr");
		xmlConEdge.setAttribute("x", Integer.toString(cEdge.getX()));
		xmlConEdge.setAttribute("y", Integer.toString(cEdge.getY()));
		xmlConEdge.setAttribute("width", Integer.toString(cEdge.getWidth()));
		xmlConEdge.setAttribute("height", Integer.toString(cEdge.getHeight()));
		xmlConEdge.setAttribute("sentToBack", Boolean.toString(cEdge
				.getGraphicalRepresentation().isSentToBAck()));
		return xmlConEdge;
	}

	// ThingInstanceAttr
	protected Element _saveThingInstanceAttr(ThingInstance tInstance)
			throws JDOMException {
		Element xmlThingIns = new Element("ThingInstanceAttr");

		xmlThingIns.setAttribute("layer", Integer
				.toString(tInstance.getLayer()));
		xmlThingIns.setAttribute("textPosition", tInstance.getTextPosition());

		OpdThing opdthing = (OpdThing) tInstance.getGraphicalRepresentation();
		xmlThingIns.setAttribute("showIcon", Boolean.toString(opdthing
				.isIconVisible()));

		return xmlThingIns;
	}

	// FundamentalRelationSection
	protected Element _saveFundamentalRelationSection(long opdNum)
			throws JDOMException {
		Element fundamentalSect = new Element("FundamentalRelationSection");

		List relList = new LinkedList();

		for (Enumeration e = this.myProject.getComponentsStructure()
				.getGraphicalRepresentationsInOpd(opdNum); e.hasMoreElements();) {
			relList.add(this
					._saveCommonPart((GraphicalRelationRepresentation) e
							.nextElement()));
		}
		fundamentalSect.addContent(relList);
		return fundamentalSect;
	}

	protected Element _saveCommonPart(GraphicalRelationRepresentation rel)
			throws JDOMException {
		Element xmlRelation = new Element("CommonPart");
		OpdFundamentalRelation commonPart = rel.getRelation();

		xmlRelation.setAttribute("sourceId", Long.toString(rel.getSource()
				.getEntity().getId()));
		xmlRelation.setAttribute("sourceInOpdId", Long.toString(rel.getSource()
				.getEntityInOpdId()));
		xmlRelation.setAttribute("sourceConnectionSide", Integer.toString(rel
				.getSourceDragger().getSide()));
		xmlRelation.setAttribute("sourceConnectionParameter", Float
				.toString((float) rel.getSourceDragger().getParam()));
		xmlRelation.setAttribute("x", Integer.toString(commonPart.getX()));
		xmlRelation.setAttribute("y", Integer.toString(commonPart.getY()));
		xmlRelation.setAttribute("width", Integer.toString(commonPart
				.getWidth()));
		xmlRelation.setAttribute("height", Integer.toString(commonPart
				.getHeight()));

		xmlRelation.setAttribute("backgroundColor", Integer.toString(commonPart
				.getBackgroundColor().getRGB()));
		xmlRelation.setAttribute("borderColor", Integer.toString(commonPart
				.getBorderColor().getRGB()));

		List insList = new LinkedList();

		for (Enumeration e = rel.getInstances(); e.hasMoreElements();) {
			insList
					.add(this
							._saveVisualFundamentalRelation((FundamentalRelationInstance) e
									.nextElement()));
		}
		xmlRelation.addContent(insList);
		return xmlRelation;
	}

	// VisualFundamentalRelation
	protected Element _saveVisualFundamentalRelation(
			FundamentalRelationInstance rel) throws JDOMException {
		Element xmlRelation = new Element("VisualFundamentalRelation");

		xmlRelation.addContent(this._saveInstanceAttr(rel));

		xmlRelation.setAttribute("destinationId", Long.toString(rel
				.getDestination().getEntity().getId()));
		xmlRelation.setAttribute("destinationInOpdId", Long.toString(rel
				.getDestination().getEntityInOpdId()));
		xmlRelation.setAttribute("destinationSide", Integer.toString(rel
				.getDestinationDragger().getSide()));
		xmlRelation.setAttribute("destinationParameter", Float
				.toString((float) rel.getDestinationDragger().getParam()));
		xmlRelation.setAttribute("labelX", Integer.toString(rel
				.getDestinationDragger().getOpdCardinalityLabel().getX()));
		xmlRelation.setAttribute("labelY", Integer.toString(rel
				.getDestinationDragger().getOpdCardinalityLabel().getY()));
		xmlRelation
				.setAttribute("isVisible", Boolean.toString(rel.isVisible()));

		return xmlRelation;
	}

	// GeneralRelationSection
	protected Element _saveGeneralRelationSection(long opdNum)
			throws JDOMException {
		Element genRelSect = new Element("GeneralRelationSection");
		List relList = new LinkedList();

		for (Enumeration e = this.myProject.getComponentsStructure()
				.getGeneralRelationsInOpd(opdNum); e.hasMoreElements();) {
			relList.add(this
					._saveVisualGeneralRelation((GeneralRelationInstance) e
							.nextElement()));
		}
		genRelSect.addContent(relList);
		return genRelSect;
	}

	// VisualGeneralRelation
	protected Element _saveVisualGeneralRelation(GeneralRelationInstance rel)
			throws JDOMException {
		Element xmlRelation = new Element("VisualGeneralRelation");
		xmlRelation.addContent(this._saveInstanceAttr(rel));
		xmlRelation.addContent(this._saveLineAttr(rel.getLine()));
		xmlRelation.setAttribute("sourceLabelX", Integer.toString(rel
				.getSourceDragger().getOpdCardinalityLabel().getX()));
		xmlRelation.setAttribute("sourceLabelY", Integer.toString(rel
				.getSourceDragger().getOpdCardinalityLabel().getY()));
		xmlRelation.setAttribute("destinationLabelX", Integer.toString(rel
				.getDestinationDragger().getOpdCardinalityLabel().getX()));
		xmlRelation.setAttribute("destinationLabelY", Integer.toString(rel
				.getDestinationDragger().getOpdCardinalityLabel().getY()));

		if (!rel.getLine().isStraight()) {
			xmlRelation.addContent(this._saveBreakPoints(rel.getLine()));
		}
		return xmlRelation;
	}

	protected Element _saveLineAttr(StyledLine line) throws JDOMException {
		Element xmlLine = new Element("LineAttr");

		xmlLine.setAttribute("autoArranged", Boolean.toString(line
				.isAutoArranged()));
		xmlLine.setAttribute("sourceId", Long.toString(line.getSource()
				.getEntity().getId()));
		xmlLine.setAttribute("sourceInOpdId", Long.toString(line.getSource()
				.getEntityInOpdId()));
		xmlLine.setAttribute("sourceConnectionSide", Integer.toString(line
				.getSourceDragger().getSide()));
		xmlLine.setAttribute("sourceConnectionParameter", Float
				.toString((float) line.getSourceDragger().getParam()));
		xmlLine.setAttribute("destinationId", Long.toString(line
				.getDestination().getEntity().getId()));
		xmlLine.setAttribute("destinationInOpdId", Long.toString(line
				.getDestination().getEntityInOpdId()));
		xmlLine.setAttribute("destinationConnectionSide", Integer.toString(line
				.getDestinationDragger().getSide()));
		xmlLine.setAttribute("destinationConnectionParameter", Float
				.toString((float) line.getDestinationDragger().getParam()));
		return xmlLine;
	}

	// BreakPoints
	protected Element _saveBreakPoints(StyledLine line) throws JDOMException {
		Element points = new Element("BreakPoints");

		List pointsList = new LinkedList();

		BaseGraphicComponent pointsArray[] = line.getPointsArray();

		for (int i = 0; i < pointsArray.length; i++) {
			Element xmlPoint = new Element("Point");
			xmlPoint.setAttribute("x", Integer.toString(pointsArray[i].getX()));
			xmlPoint.setAttribute("y", Integer.toString(pointsArray[i].getY()));
			pointsList.add(xmlPoint);
		}
		points.addContent(pointsList);
		return points;
	}

	// VisualLinkSection
	protected Element _saveVisualLinkSection(long opdNum) throws JDOMException {
		Element linkSect = new Element("VisualLinkSection");

		List linkList = new LinkedList();// linkSect.getVisualLink();

		for (Enumeration e = this.myProject.getComponentsStructure()
				.getLinksInOpd(opdNum); e.hasMoreElements();) {
			linkList.add(this._saveVisualLink((LinkInstance) e.nextElement()));
		}
		linkSect.addContent(linkList);
		this._saveOrXor(linkSect, opdNum);

		return linkSect;
	}

	// Gets VisualLinkSection
	protected void _saveOrXor(Element linkSect, long opdNum)
			throws JDOMException {
		Hashtable sourceGroups = new Hashtable();
		Hashtable destinationGroups = new Hashtable();

		for (Enumeration e = this.myProject.getComponentsStructure()
				.getLinksInOpd(opdNum); e.hasMoreElements();) {
			LinkInstance lInstance = (LinkInstance) e.nextElement();
			if (lInstance.getSourceOr() != null) {
				sourceGroups.put(lInstance.getKey(), lInstance);
			}

			if (lInstance.getDestOr() != null) {
				destinationGroups.put(lInstance.getKey(), lInstance);
			}
		}

		List orList = new LinkedList();// linkSect.getOrXorGroup();

		while (!sourceGroups.isEmpty()) {
			Enumeration e = sourceGroups.elements();
			LinkInstance lInstance = (LinkInstance) e.nextElement();
			OrInstance orIns = lInstance.getSourceOr();

			Element xmlOr = new Element("OrXorGroup");
			xmlOr.setAttribute("isSourceGroup", "true");
			List memberList = new LinkedList();// xmlOr.getMember();

			if (orIns.isOr()) {
				xmlOr.setAttribute("type", Integer.toString(OR_TYPE));
			} else {
				xmlOr.setAttribute("type", Integer.toString(XOR_TYPE));
			}

			for (Enumeration e2 = orIns.getInstances(); e2.hasMoreElements();) {
				LinkInstance cLink = (LinkInstance) e2.nextElement();
				Element xmlMember = new Element("Member");
				xmlMember.setAttribute("memberId", Long.toString(cLink
						.getEntry().getId()));
				xmlMember.setAttribute("memberInOpdId", Long.toString(cLink
						.getKey().getEntityInOpdId()));
				memberList.add(xmlMember);
				sourceGroups.remove(cLink.getKey());
			}
			xmlOr.addContent(memberList);
			orList.add(xmlOr);
		}

		while (!destinationGroups.isEmpty()) {
			Enumeration e = destinationGroups.elements();
			LinkInstance lInstance = (LinkInstance) e.nextElement();
			OrInstance orIns = lInstance.getDestOr();

			Element xmlOr = new Element("OrXorGroup");
			xmlOr.setAttribute("isSourceGroup", "false");
			List memberList = new LinkedList();// xmlOr.getMember();

			if (orIns.isOr()) {
				xmlOr.setAttribute("type", Integer.toString(OR_TYPE));
			} else {
				xmlOr.setAttribute("type", Integer.toString(XOR_TYPE));
			}

			for (Enumeration e2 = orIns.getInstances(); e2.hasMoreElements();) {
				LinkInstance cLink = (LinkInstance) e2.nextElement();
				Element xmlMember = new Element("Member");
				xmlMember.setAttribute("memberId", Long.toString(cLink
						.getEntry().getId()));
				xmlMember.setAttribute("memberInOpdId", Long.toString(cLink
						.getKey().getEntityInOpdId()));
				memberList.add(xmlMember);
				destinationGroups.remove(cLink.getKey());
			}
			xmlOr.addContent(memberList);
			orList.add(xmlOr);
		}
		linkSect.addContent(orList);

	}

	// VisualLink
	protected Element _saveVisualLink(LinkInstance link) throws JDOMException {
		Element xmlLink = new Element("VisualLink");

		xmlLink.addContent(this._saveInstanceAttr(link));
		xmlLink.addContent(this._saveLineAttr(link.getLine()));

		if (!link.getLine().isStraight()) {
			xmlLink.addContent(this._saveBreakPoints(link.getLine()));
		}
		return xmlLink;
	}

	// Unfolded
	protected Element _saveUnfolded(long opdNum, Hashtable opdDependencies)
			throws JDOMException {
		Element unfolded = new Element("Unfolded");

		List xmlChildrenList = new LinkedList();// unfolded.getUnfoldingProperties();

		LinkedList children = (LinkedList) opdDependencies
				.get(new Long(opdNum));

		if (children != null) {
			for (Iterator it = children.iterator(); it.hasNext();) {
				Opd currOpd = (Opd) it.next();
				if (currOpd.getParentOpd().getOpdId() == opdNum) {
					ThingInstance parentInstance = this
							._getParentInstance(currOpd);
					if (parentInstance != null) {
						Element xmlOpd = new Element("UnfoldingProperties");
						xmlOpd.setAttribute("entityId", Long
								.toString(parentInstance.getLogicalId()));
						xmlOpd.setAttribute("entityInOpdId", Long
								.toString(parentInstance.getKey()
										.getEntityInOpdId()));
						xmlOpd.addContent(this._saveOpd(currOpd.getOpdId(),
								opdDependencies));
						xmlChildrenList.add(xmlOpd);
					}

				}
			}
		}

		unfolded.addContent(xmlChildrenList);
		return unfolded;
	}

	protected Element _saveView(long opdNum, Hashtable opdDependencies)
			throws JDOMException {
		Element unfolded = new Element("View");

		List xmlChildrenList = new LinkedList();// unfolded.getUnfoldingProperties();

		LinkedList children = (LinkedList) opdDependencies
				.get(new Long(opdNum));

		if (children != null) {
			for (Iterator it = children.iterator(); it.hasNext();) {
				Opd currOpd = (Opd) it.next();
				if (currOpd.getParentOpd().getOpdId() == opdNum) {
					ThingInstance parentInstance = _getParentInstance(currOpd);
					if (parentInstance != null) {
						// is unfolding opd
						continue;
					}
					if (currOpd.getMainEntry().getZoomedInOpd() == currOpd) {
						// is in-zoom opd
						continue;
					}
					parentInstance = currOpd.getMainInstance();

					if (parentInstance != null) {
						Element xmlOpd = new Element("ViewProperties");
						xmlOpd.setAttribute("entityId", Long
								.toString(parentInstance.getLogicalId()));
						xmlOpd.setAttribute("entityInOpdId", Long
								.toString(parentInstance.getKey()
										.getEntityInOpdId()));
						xmlOpd.addContent(this._saveOpd(currOpd.getOpdId(),
								opdDependencies));
						xmlChildrenList.add(xmlOpd);
					}

				}
			}
		}

		unfolded.addContent(xmlChildrenList);
		return unfolded;
	}

	protected ThingInstance _getParentInstance(Opd child) {
		ThingEntry mainEntry = child.getMainEntry();
		for (Enumeration e = mainEntry.getInstances(); e.hasMoreElements();) {

			ThingInstance parentInstance = (ThingInstance) e.nextElement();
			Opd unfoldingOpd = parentInstance.getUnfoldingOpd();
			if ((unfoldingOpd != null)
					&& (unfoldingOpd.getOpdId() == child.getOpdId())) {
				return parentInstance;
			}
		}
		return null;
	}

	// InZoomed
	protected Element _saveInZoomed(long opdNum, Hashtable opdDependencies)
			throws JDOMException {
		Element inZoomed = new Element("InZoomed");
		List xmlChildrenList = new LinkedList();// inZoomed.getOPD();

		LinkedList children = (LinkedList) opdDependencies
				.get(new Long(opdNum));

		if (children != null) {
			for (Iterator it = children.iterator(); it.hasNext();) {
				Opd currOpd = (Opd) it.next();
				if (currOpd.getMainEntry().getZoomedInOpd() == currOpd) {
					xmlChildrenList.add(this._saveOpd(currOpd.getOpdId(),
							opdDependencies));
				}
			}
		}
		inZoomed.addContent(xmlChildrenList);
		return inZoomed;
	}

	protected Hashtable _prepareOpdsDependencies() {
		Hashtable OpdsDep = new Hashtable();

		for (Enumeration e = this.myProject.getComponentsStructure().getOpds(); e
				.hasMoreElements();) {
			Opd currOpd = (Opd) e.nextElement();
			Opd parentOpd = currOpd.getParentOpd();
			long parentNum;

			if (parentOpd == null) {
				parentNum = 0;
			} else {
				parentNum = parentOpd.getOpdId();
			}

			LinkedList tempList = (LinkedList) OpdsDep.get(new Long(parentNum));
			if (tempList == null) {
				tempList = new LinkedList();
				OpdsDep.put(new Long(parentNum), tempList);
			}
			tempList.add(currOpd);

		}

		return OpdsDep;
	}

}