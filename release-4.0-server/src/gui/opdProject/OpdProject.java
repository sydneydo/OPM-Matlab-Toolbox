package gui.opdProject;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.print.Book;
import java.awt.print.PrinterJob;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;

import modelControl.OpcatMCManager;

import org.tmatesoft.svn.core.SVNURL;

import util.Configuration;

import exportedAPI.OpcatConstants;
import exportedAPI.OpdKey;
import exportedAPI.RelativeConnectionPoint;
import exportedAPI.opcatAPI.IConnectionEdgeInstance;
import exportedAPI.opcatAPI.ILinkInstance;
import exportedAPI.opcatAPI.IOpd;
import exportedAPI.opcatAPI.ISystem;
import exportedAPI.opcatAPI.ISystemStructure;
import exportedAPI.opcatAPIx.IXCheckResult;
import exportedAPI.opcatAPIx.IXConnectionEdgeInstance;
import exportedAPI.opcatAPIx.IXInstance;
import exportedAPI.opcatAPIx.IXLinkInstance;
import exportedAPI.opcatAPIx.IXObjectInstance;
import exportedAPI.opcatAPIx.IXOpd;
import exportedAPI.opcatAPIx.IXProcessInstance;
import exportedAPI.opcatAPIx.IXRelationInstance;
import exportedAPI.opcatAPIx.IXStateEntry;
import exportedAPI.opcatAPIx.IXSystem;
import exportedAPI.opcatAPIx.IXSystemStructure;
import exportedAPI.opcatAPIx.IXThingInstance;
import expose.OpcatExposeChangeResult;
import expose.OpcatExposeManager;
import expose.OpcatExposeConstants.OPCAT_EXPOSE_CHANGE_TYPE;
import expose.OpcatExposeConstants.OPCAT_EXPOSE_OP;
import extensionTools.opl2.OPLBuilder;
import gui.Opcat2;
import gui.checkModule.CheckModule;
import gui.checkModule.CheckResult;
import gui.checkModule.CommandWrapper;
import gui.controls.EditControl;
import gui.controls.FileControl;
import gui.controls.GuiControl;
import gui.metaLibraries.logic.MetaLibrary;
import gui.metaLibraries.logic.MetaManager;
import gui.metaLibraries.logic.Role;
import gui.opdGraphics.GraphicsUtils;
import gui.opdGraphics.dialogs.UnfoldingDialog;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;
import gui.opdGraphics.opdBaseComponents.OpdBaseComponent;
import gui.opdGraphics.opdBaseComponents.OpdConnectionEdge;
import gui.opdGraphics.opdBaseComponents.OpdObject;
import gui.opdGraphics.opdBaseComponents.OpdProcess;
import gui.opdGraphics.opdBaseComponents.OpdState;
import gui.opdGraphics.opdBaseComponents.OpdThing;
import gui.opdProject.consistency.ConsistencyAction;
import gui.opdProject.consistency.ConsistencyAbstractChecker;
import gui.opdProject.consistency.ConsistencyFactory;
import gui.opdProject.consistency.ConsistencyResult;
import gui.opmEntities.Constants;
import gui.opmEntities.OpmAgent;
import gui.opmEntities.OpmAggregation;
import gui.opmEntities.OpmBiDirectionalRelation;
import gui.opmEntities.OpmConditionLink;
import gui.opmEntities.OpmConnectionEdge;
import gui.opmEntities.OpmConsumptionEventLink;
import gui.opmEntities.OpmConsumptionLink;
import gui.opmEntities.OpmEffectLink;
import gui.opmEntities.OpmExceptionLink;
import gui.opmEntities.OpmExhibition;
import gui.opmEntities.OpmFundamentalRelation;
import gui.opmEntities.OpmGeneralRelation;
import gui.opmEntities.OpmInstantination;
import gui.opmEntities.OpmInstrument;
import gui.opmEntities.OpmInstrumentEventLink;
import gui.opmEntities.OpmInvocationLink;
import gui.opmEntities.OpmObject;
import gui.opmEntities.OpmProceduralLink;
import gui.opmEntities.OpmProcess;
import gui.opmEntities.OpmResultLink;
import gui.opmEntities.OpmSpecialization;
import gui.opmEntities.OpmState;
import gui.opmEntities.OpmStructuralRelation;
import gui.opmEntities.OpmThing;
import gui.opmEntities.OpmUniDirectionalRelation;
import gui.opx.Saver;
import gui.projectStructure.ConnectionEdgeEntry;
import gui.projectStructure.ConnectionEdgeInstance;
import gui.projectStructure.Entry;
import gui.projectStructure.FundamentalRelationEntry;
import gui.projectStructure.FundamentalRelationInstance;
import gui.projectStructure.GeneralRelationEntry;
import gui.projectStructure.GeneralRelationInstance;
import gui.projectStructure.GraphicRepresentationKey;
import gui.projectStructure.GraphicalRelationRepresentation;
import gui.projectStructure.Instance;
import gui.projectStructure.LinkEntry;
import gui.projectStructure.LinkInstance;
import gui.projectStructure.MainStructure;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ObjectInstance;
import gui.projectStructure.ProcessEntry;
import gui.projectStructure.ProcessInstance;
import gui.projectStructure.RelationEntry;
import gui.projectStructure.StateEntry;
import gui.projectStructure.StateInstance;
import gui.projectStructure.ThingEntry;
import gui.projectStructure.ThingInstance;
import gui.scenarios.Scenarios;
import gui.undo.CompoundUndoAction;
import gui.undo.UndoableAddFundamentalRelation;
import gui.undo.UndoableAddGeneralRelation;
import gui.undo.UndoableAddLink;
import gui.undo.UndoableAddObject;
import gui.undo.UndoableAddProcess;
import gui.undo.UndoableDeleteFundamentalRelation;
import gui.undo.UndoableDeleteGeneralRelation;
import gui.undo.UndoableDeleteLink;
import gui.undo.UndoableDeleteObject;
import gui.undo.UndoableDeleteProcess;
import gui.undo.UndoableDeleteState;
import gui.util.OpcatLogger;
import gui.util.opcatGrid.GridPanel;

/**
 * This class represents user's project.
 * <p>
 * This class manages all needed for the project: graphics, internal data
 * Unfolding and Zooming in, repository manager ,printing.
 * </p>
 */

public class OpdProject implements ISystem, IXSystem {

	private OpcatExposeManager myExposeManager;

	private SVNURL mcURL;

	public void setMcURL(SVNURL mcURL) {
		this.mcURL = mcURL;
	}

	public SVNURL getMcURL() {
		return mcURL;
	}

	public OpcatExposeManager getExposeManager() {
		return myExposeManager;
	}

	// scenarios attached to this model
	private Scenarios scenarios = new Scenarios();

	// MetaManager for managing converting OPD's into meta-libreries.
	private MetaManager opdMetaDataManager = new MetaManager();

	// project name
	private String projectName;

	// name of the project creator
	private String projectCreator;

	// Opcat model types
	private HashMap<String, String> modelTypes = new HashMap<String, String>();

	private Date createDate;

	// local projectID it is unique in this OPCAT session
	private long projectId;

	private MainStructure mainComponentsStructure;

	private OpcatLayoutManager layoutManager;

	private long entityMaxEntry;

	private long opdMaxEntry;

	private Opd currentOpd;

	private JDesktopPane parentDesktop;

	private long tempNum;

	private GenericTable config;

	private GenericTable generalInfo;

	private PrinterJob printJob;

	private PageSetupData pageSetupData;

	private OplColorScheme colorScheme;

	private String fileName = null;

	public static int CLIPBOARD_ID = 666;

	private boolean isPresented = true;

	private Opd clipBoard = null;

	private boolean canClose = true;

	private String path;

	public final static int DELETE = 100;

	public final static int COPY = 200;

	private final static int CUT = 300;

	public final static int PASTE = 400;

	/**
	 * A list of meta-libraries that are imported, or need to be imported, into
	 * the project.
	 * 
	 * @author Eran Toch
	 */
	private MetaManager metaManager = null;

	/**
	 * Holds the OPX version number, represented by a String. The value is
	 * updated by the loader.
	 */
	private String versionString = "";

	/**
	 * Holds a unique identifier for each of the opcat projects.
	 */
	private String opcatGlobalID = "";

	private boolean duringLoad = false;

	private Instance copyFormatThing;

	/**
	 * Creates OpdProject with specified parameters
	 * 
	 * @param parent
	 *            JDesktopPane which contains all OPD belonging to this project
	 * @param opcatTree
	 *            ReporistoryManager of the user's project
	 * @param pId
	 *            id of the user's project. This id have to be unique in
	 *            database.
	 */
	public OpdProject(JDesktopPane parent, long pId) {

		this.entityMaxEntry = 0;
		this.opdMaxEntry = 0;
		this.projectName = new String("NoName");
		this.projectCreator = new String("NoCreator");
		this.createDate = (new GregorianCalendar()).getTime();

		this.mainComponentsStructure = new MainStructure();
		this.config = new GenericTable("CONFIGURATION");
		this.initConfig();

		this.pageSetupData = new PageSetupData();
		this.colorScheme = new OplColorScheme();
		this.projectId = pId;

		this.parentDesktop = parent;
		if (parent != null)
			this.parentDesktop.setDesktopManager(new OpcatDesktopManager(this));

		this.clipBoard = new Opd(this, "ClipBoard", CLIPBOARD_ID, null, false);
		this.generalInfo = new GenericTable("CONFIGURATION");
		this.layoutManager = new OpcatLayoutManager(this);

		// Creating a MetaManger
		// By Eran Toch
		this.metaManager = new MetaManager();

		// Setting global unique ID
		this.opcatGlobalID = this.generateGlobalID();

		// Setting the version number
		this.setVersionString(Saver.currentVersion);

		this.myExposeManager = new OpcatExposeManager(this);

		loadSystemTypes();

	}

	/**
	 * Load the system types from a File, If the File is not present systems.ops
	 * in the OPCAT_HOME directory then load some generic types.
	 */
	private void loadSystemTypes() {
		FileControl file = FileControl.getInstance();
		File systemTypes = new File(file.getOPCATDirectory()
				+ FileControl.fileSeparator + "systems.ops");
		this.modelTypes.put("0", "System");

		if (systemTypes.exists()) {
			String line = " ";
			BufferedReader bufRdr = null;
			int i = 1;
			try {
				bufRdr = new BufferedReader(new FileReader(systemTypes));
				line = bufRdr.readLine();
			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			}
			try {
				while ((line != null) && (!line.trim().equalsIgnoreCase(""))) {
					this.modelTypes.put(String.valueOf(i), line);
					i++;
					line = " ";
					try {
						line = bufRdr.readLine();
					} catch (Exception ex) {
						OpcatLogger.logError(ex);
					}
				}
			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			}
		} else {
			this.modelTypes.put("1", "Architecture");
			this.modelTypes.put("2", "Testing");
			this.modelTypes.put("3", "Design Templates");
			this.modelTypes.put("4", "Inventory");
			this.modelTypes.put("5", "Generic Code");
			this.modelTypes.put("6", "Team Project");
		}

		this.modelTypes.put("current", "System");

	}

	public Object[] getModelTypesArray() {
		ArrayList ret = new ArrayList();
		Iterator iter = modelTypes.keySet().iterator();

		while (iter.hasNext()) {
			String key = (String) iter.next();
			if (!key.equalsIgnoreCase("current"))
				ret.add(modelTypes.get(key));
		}
		return ret.toArray();
	}

	public ArrayList getModelTypesArrayList() {
		ArrayList ret = new ArrayList();
		Iterator iter = modelTypes.keySet().iterator();

		while (iter.hasNext()) {
			String key = (String) iter.next();
			if (!key.equalsIgnoreCase("current"))
				ret.add(modelTypes.get(key));
		}
		return ret;
	}

	public void setModelType(String modelType) {
		modelTypes.remove("current");
		modelTypes.put("current", modelType);
	}

	public String getCurrentModelType() {
		String cur;
		if (this.modelTypes.get("current") != null) {
			cur = (String) this.modelTypes.get("current");
		} else {
			cur = "System";
		}
		return cur;
	}

	private void initConfig() {
		this.config.setProperty("ThingWidth", new Integer(120));
		this.config.setProperty("ThingHeight", new Integer(70));
		this.config.setProperty("MinimalThingWidth", new Integer(60));
		this.config.setProperty("MinimalThingHeight", new Integer(35));

		this.config.setProperty("FundamentalRelationWidth", new Integer(36));
		this.config.setProperty("FundamentalRelationHeight", new Integer(31));

		this.config.setProperty("StateWidth", new Integer(60));
		this.config.setProperty("StateHeight", new Integer(35));

		this.config.setProperty("OPDWidth", new Integer(1300));
		this.config.setProperty("OPDHeight", new Integer(860));
		this.config.setProperty("DraggerSize", new Integer(36));

		this.config.setProperty("ThingFont",
				new Font("OurFont", Font.PLAIN, 16));
		this.config.setProperty("URLFont", new Font("OurFont", Font.BOLD
				+ Font.ITALIC, 16));
		this.config.setProperty("StateFont",
				new Font("OurFont", Font.PLAIN, 12));
		this.config
				.setProperty("LineFont", new Font("OurFont", Font.PLAIN, 11));
		this.config.setProperty("LabelFont",
				new Font("OurFont", Font.PLAIN, 11));
		this.config
				.setProperty("LinkFont", new Font("OurFont", Font.PLAIN, 12));

		this.config.setProperty("SmallFont",
				new Font("OurFont", Font.PLAIN, 11));

		this.config.setProperty("BackgroundColor", new Color(230, 230, 230));
		// kind of gray
		this.config.setProperty("TextColor", Color.black);
		this.config.setProperty("UrlColor", Color.RED);
		this.config.setProperty("ObjectColor", new Color(0, 110, 0));
		// kind of green
		this.config.setProperty("ProcessColor", new Color(0, 0, 170));
		// kind of blue

		this.config.setProperty("StateColor", new Color(91, 91, 0));
		// kind of brown

		this.config.setProperty("NormalSize", new Integer(5));
		this.config.setProperty("CurrentSize", new Integer(4));

	}

	/**
	 * Creates a global ID, which is generated using a random number generator.
	 * The method takes the time in millisecods, use it as a random seed, and
	 * creates a string which is a concatination of the first number and the
	 * second, seperated by a dot (".").
	 * 
	 * @return
	 */
	public String generateGlobalID() {
		Calendar rightNow = Calendar.getInstance();
		long nowMillis = rightNow.getTimeInMillis();
		Random generator = new Random(nowMillis);
		long randomLong = generator.nextLong();
		if (randomLong < 0) {
			randomLong *= -1;
		}
		String globalID = String.valueOf(randomLong) + "."
				+ String.valueOf(nowMillis);
		return globalID;
	}

	/**
	 * Returns the main OPCAT frame.
	 * 
	 * @return JFrame main OPCAT frame.
	 */
	public JFrame getMainFrame() {
		return Opcat2.getFrame();
	}

	public Dimension getUserAreaSize() {
		// GenericTable config = myProject.getConfiguration();
		double normalSize = ((Integer) this.config.getProperty("NormalSize"))
				.doubleValue();
		double currentSize = ((Integer) this.config.getProperty("CurrentSize"))
				.doubleValue();
		double factor = currentSize / normalSize;
		double width = ((Integer) this.config.getProperty("OPDWidth"))
				.intValue()
				* factor;
		double height = ((Integer) this.config.getProperty("OPDHeight"))
				.intValue()
				* factor;
		return new Dimension((int) width, (int) height);
	}

	public void showRootOpd() {
		// tempNum = _getFreeOpdEntry();
		this.currentOpd = this.mainComponentsStructure.getOpd(1);

		if (this.currentOpd == null) {
			return;
		}

		this.currentOpd.show();
		OpdMap.UpdateOpdMap(this.currentOpd);
	}

	public Opd createRootOPD() {
		this.tempNum = this._getFreeOpdEntry();
		Opd opd = new Opd(this, "SD", this.tempNum, null);
		this.mainComponentsStructure.put(this.tempNum, opd);
		return opd;
	}

	public void showOPD(long opdNum) {
		showOPD(opdNum, true);
	}

	public void showOPD(long opdNum, boolean fullScreen, boolean updateMap) {
		Opd tempOPD = this.getOpdByID(opdNum);

		if (tempOPD == null) {
			return;
		}

		tempOPD.setVisible(true);

		try {
			tempOPD.getOpdContainer().setSelected(true);
			tempOPD.getOpdContainer().setMaximum(fullScreen);
		} catch (java.beans.PropertyVetoException pve) {
			OpcatLogger.logError(pve);
		}
		if (updateMap)
			OpdMap.UpdateOpdMap(this.getCurrentOpd());

		tempOPD.refit(tempOPD.getDrawingArea());
	}

	public void showOPD(long opdNum, boolean updateMap) {
		showOPD(opdNum, true, updateMap);
	}

	private long _getFreeEntityEntry() {
		this.entityMaxEntry++;
		return this.entityMaxEntry;
	}

	/**
	 * Returns an OPD according to its ID, or <code>null</code> if no OPD by
	 * this id was found.
	 */
	public Opd getOpdByID(long opdId) {
		return this.mainComponentsStructure.getOpd(opdId);
	}

	/**
	 * Returns current OPD (user is currently working with this OPD)
	 */
	public Opd getCurrentOpd() {
		if (this.currentOpd == null) {

			if (this.getOpdByID(1) == null) {
				createRootOPD();
			}

			this.currentOpd = getOpdByID(1);
		}
		return this.currentOpd;
	}

	public void deselectAll() {
		Iterator opdIter = Collections.list(
				this.getSystemStructure().getAllOpds()).iterator();
		while (opdIter.hasNext()) {
			Opd opd = (Opd) opdIter.next();
			opd.getSelection().removeSelection();
			opd.getSelection().resetSelection();
		}
	}

	public HashMap getSelection() {
		HashMap map = new HashMap();
		Iterator opdIter = Collections
				.list(this.getSystemStructure().getOpds()).iterator();
		while (opdIter.hasNext()) {
			Opd opd = (Opd) opdIter.next();
			Iterator iter = Collections.list(
					opd.getSelection().getSelectedItems()).iterator();
			while (iter.hasNext()) {
				Instance ins = (Instance) iter.next();
				map.put(ins.getKey(), ins);
			}
		}
		return map;
	}

	public IOpd getCurrentIOpd() {
		return getCurrentOpd();
	}

	public IXOpd getCurrentIXOpd() {
		return getCurrentOpd();
	}

	public void updateChanges() {
		Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
	}

	/**
	 * Sets current OPD
	 */
	public void setCurrentOpd(Opd pOpd) {
		this.currentOpd = pOpd;
	}

	/**
	 * Returns MainStructure - data structure which holds all inforation about
	 * this project
	 */
	public MainStructure getComponentsStructure() {
		return this.mainComponentsStructure;
	}

	public ISystemStructure getISystemStructure() {
		return this.mainComponentsStructure;
	}

	public IXSystemStructure getIXSystemStructure() {
		return this.mainComponentsStructure;
	}

	private long _getFreeOpdEntry() {
		this.opdMaxEntry++;
		return this.opdMaxEntry;
	}

	/**
	 * Returns project's name
	 */
	public String getName() {
		return this.projectName;
	}

	/**
	 * Returns String representing this project
	 */
	public String toString() {
		return this.projectName;
	}

	/**
	 * Returns name of project's creator
	 */

	public String getCreator() {
		return this.projectCreator;
	}

	/**
	 * Returns project's creaion date
	 */

	public Date getCreationDate() {
		return this.createDate;
	}

	public String getInfoValue(String key) {
		return (String) this.generalInfo.getProperty(key);
	}

	public void setInfoValue(String key, String value) {
		this.generalInfo.setProperty(key, value);
	}

	/**
	 * Returns unique project's id (for save purposes)
	 */
	public long getProjectId() {
		return this.projectId;
	}

	public void setProjectId(long prId) {
		this.projectId = prId;
	}

	/**
	 * Sets project name
	 */
	public void setName(String name) {
		this.projectName = name;
	}

	public OplColorScheme getOplColorScheme() {
		return this.colorScheme;
	}

	public GenericTable getConfiguration() {
		return this.config;
	}

	public GenericTable getGeneralInfo() {
		return this.generalInfo;
	}

	/**
	 * Sets project creator name
	 */
	public void setCreator(String creator) {
		this.projectCreator = creator;
	}

	/**
	 * Sets project creation date
	 */
	public void setCreationDate(Date cDate) {
		this.createDate = cDate;
	}

	public LinkInstance getLinkBetweenInstances(ConnectionEdgeInstance source,
			ConnectionEdgeInstance destination) {
		for (Enumeration e = source.getRelatedInstances(); e.hasMoreElements();) {
			Object o = e.nextElement();
			if (o instanceof LinkInstance) {
				LinkInstance tmp = (LinkInstance) o;
				if (tmp.getSource().getOpdKey().equals(
						source.getConnectionEdge().getOpdKey())
						&& tmp.getDestination().getOpdKey().equals(
								destination.getConnectionEdge().getOpdKey())) {
					return tmp;
				}
			}
		}

		return null;
	}

	public IXLinkInstance getIXLinkBetweenInstances(
			IXConnectionEdgeInstance source,
			IXConnectionEdgeInstance destination) {
		return this.getLinkBetweenInstances((ConnectionEdgeInstance) source,
				(ConnectionEdgeInstance) destination);
	}

	public ILinkInstance getILinkBetweenInstances(
			IConnectionEdgeInstance source, IConnectionEdgeInstance destination) {
		return this.getLinkBetweenInstances((ConnectionEdgeInstance) source,
				(ConnectionEdgeInstance) destination);
	}

	public Enumeration getRelationBetweenInstances(
			ConnectionEdgeInstance source, ConnectionEdgeInstance destination) {
		Vector tempVector = new Vector();

		for (Enumeration e = source.getRelatedInstances(); e.hasMoreElements();) {
			Object o = e.nextElement();
			if (o instanceof GeneralRelationInstance) {
				GeneralRelationInstance tmp = (GeneralRelationInstance) o;
				if (tmp.getSource().getOpdKey().equals(
						source.getConnectionEdge().getOpdKey())
						&& tmp.getDestination().getOpdKey().equals(
								destination.getConnectionEdge().getOpdKey())) {
					tempVector.add(tmp);
				}
			}
		}

		return tempVector.elements();
	}

	public Enumeration getIXRelationsBetweenInstances(
			IXConnectionEdgeInstance source,
			IXConnectionEdgeInstance destination) {
		return this.getRelationBetweenInstances(
				(ConnectionEdgeInstance) source,
				(ConnectionEdgeInstance) destination);
	}

	public Enumeration getIRelationsBetweenInstances(
			IConnectionEdgeInstance source, IConnectionEdgeInstance destination) {
		return this.getRelationBetweenInstances(
				(ConnectionEdgeInstance) source,
				(ConnectionEdgeInstance) destination);
	}

	// public String getOPL(boolean isHTML, long opdNum) {
	// return generateOPL(isHTML, opdNum);
	// }

	public String getOPL(boolean isHTML, long opdNum) {

		if (Configuration.getInstance().getProperty("show_opl")
				.equalsIgnoreCase("false")) {
			return " ";
		}

		OPLBuilder myOplBuilder = new OPLBuilder(this, this.colorScheme);

		try {
			if (isHTML) {
				return myOplBuilder.getOplHTML(opdNum).toString();
			}
			return myOplBuilder.getOplText(opdNum).toString();
		} catch (Exception ex) {
			OpcatLogger.logError(ex);
			return "";
		}
	}

	public String getOPL(boolean isHTML) {

		if (Configuration.getInstance().getProperty("show_opl")
				.equalsIgnoreCase("false")) {
			return " ";
		}

		OPLBuilder myOplBuilder = new OPLBuilder(this, this.colorScheme);

		if (isHTML) {
			String opl = myOplBuilder.getOplHTML().toString();
			return opl;
		}
		return myOplBuilder.getOplText().toString();
	}

	public StringBuffer getOplXmlScript() {
		OPLBuilder myOplBuilder = new OPLBuilder(this, this.colorScheme);
		return myOplBuilder.getOplXmlScript();
	}

	public void applyNewBackground(Color newBackgroundColor) {
		Color oldColor = (Color) this.config.getProperty("BackgroundColor");
		this.config.setProperty("BackgroundColor", newBackgroundColor);
		for (Enumeration<Entry> e1 = this.mainComponentsStructure.getElements(); e1
				.hasMoreElements();) {
			Entry tempEntry = (Entry) e1.nextElement();
			for (Enumeration<Instance> e2 = tempEntry.getInstances(); e2
					.hasMoreElements();) {
				Instance tempInstance = (Instance) e2.nextElement();
				if (tempInstance.getBackgroundColor().equals(oldColor)) {
					tempInstance.setBackgroundColor(newBackgroundColor);
				}
			}
		}

		for (Enumeration<GraphicalRelationRepresentation> e1 = this.mainComponentsStructure
				.getGraphicalRepresentations(); e1.hasMoreElements();) {
			GraphicalRelationRepresentation tempRepr = (GraphicalRelationRepresentation) e1
					.nextElement();
			if (tempRepr.getBackgroundColor().equals(oldColor)) {
				tempRepr.setBackgroundColor(newBackgroundColor);
			}
		}

	}

	public void print(boolean showDialog, boolean preview) {

		Book nBook = new Book();

		TreeMap sortedOpds = new TreeMap();
		LinkedList tempOpds = new LinkedList();

		boolean change = true;
		long pageNum = 1;

		sortedOpds.put(new Long(pageNum), this.mainComponentsStructure
				.getOpd(1));

		while (change) {
			tempOpds.clear();
			change = false;

			for (Enumeration e = this.mainComponentsStructure.getOpds(); e
					.hasMoreElements();) {
				Opd tOpd = (Opd) e.nextElement();
				if (sortedOpds.containsValue(tOpd.getParentOpd())
						&& !sortedOpds.containsValue(tOpd)) {
					tempOpds.add(tOpd);
					change = true;
				}
			}

			for (Iterator i = tempOpds.listIterator(); i.hasNext();) {
				pageNum++;
				sortedOpds.put(new Long(pageNum), i.next());
			}
		}

		for (Iterator i = sortedOpds.values().iterator(); i.hasNext();) {
			nBook.append((Opd) i.next(), this.pageSetupData);
		}

		// PrinterJob printJob = PrinterJob.getPrinterJob();
		this.getPrinterJob().setPageable(nBook);

		if (preview) {
			new PrintPreview(this.printJob, nBook, this.pageSetupData);
			return;
		}

		if (showDialog) {
			// new PrintDialog(Opcat2.getFrame(), "Printing Properties",
			// getPrinterJob(), pf, pp);
			if (!this.getPrinterJob().printDialog()) {
				// applyNewBackground(oldColor);
				return;
			}
		}

		// else direct print

		try {
			this.getPrinterJob().print();
		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
	}

	/** *********************** OWL-S Start *********************** */
	public void zoomOut() {
		this.currentOpd = this.getCurrentOpd().getParentOpd();
		// this.currentOpd.setVisible(true);
		this.showOPD(this.currentOpd.getOpdId());
		try {
			this.currentOpd.getOpdContainer().setSelected(true);
		} catch (PropertyVetoException e) {
			OpcatLogger.logError(e);
		}
	}

	/** *********************** OWL-S End *********************** */

	public Opd zoomIn(ThingInstance parentInstance) {

		ThingEntry myEntry;

		ThingInstance newInstance;
		Opd oldCurrentOpd = this.currentOpd;
		Opd parent = parentInstance.getOpd();

		myEntry = (ThingEntry) parentInstance.getEntry();

		this.currentOpd = myEntry.getZoomedInOpd();

		if (this.currentOpd == null) {

			Opcat2.getUndoManager().discardAllEdits();
			Opcat2.setRedoEnabled(false);
			Opcat2.setUndoEnabled(false);

			this.tempNum = this._getFreeOpdEntry();
			this.currentOpd = new Opd(this, myEntry.getName()
					+ " - In-Zooming OPD", this.tempNum, parent);
			// currentOpd.show();
			this.mainComponentsStructure.put(this.tempNum, this.currentOpd);
			myEntry.setZoomedInOpd(this.currentOpd);

			if (myEntry instanceof ProcessEntry) {
				newInstance = this.addProcess(0, 0, this.currentOpd
						.getOpdContainer().getDrawingArea(), myEntry.getId(),
						-1);
			} else {
				newInstance = this.addObject(0, 0, this.currentOpd
						.getOpdContainer().getDrawingArea(), myEntry.getId(),
						-1, true);
			}

			newInstance.setZoomedIn(true);
			((OpdThing) (newInstance.getConnectionEdge())).setTextPosition("N");

			Integer nSize = (Integer) this.config.getProperty("NormalSize");
			Integer cSize = (Integer) this.config.getProperty("CurrentSize");

			double factor = cSize.doubleValue() / nSize.doubleValue();

			newInstance.setLocation((int) (225.0 * factor),
					(int) (170.0 * factor));
			newInstance.setSize((int) (570.0 * factor), (int) (475.0 * factor));
			if (newInstance instanceof ObjectInstance) {
				((OpdObject) newInstance.getThing()).drawStates();
			}
			this.drawLinkedThings(newInstance, parentInstance);
			myEntry.updateInstances();

			this.currentOpd.setMainEntry(myEntry);
			this.currentOpd.setMainInstance(newInstance);
			this.setNames4OPDs();
			Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE_TREE);
			setCanClose(false);
		}

		Opd retOpd = this.currentOpd;
		this.currentOpd = oldCurrentOpd;
		return retOpd;
	}

	public void createView() {
		ThingInstance myInstance;

		Instance tempComp = this.currentOpd.getSelectedItem();
		if ((tempComp == null) || !(tempComp instanceof ThingInstance)) {
			return;
		}

		myInstance = (ThingInstance) tempComp;

		ThingEntry myEntry = (ThingEntry) myInstance.getEntry();
		if (myEntry.isExternalFunction()) {

			int ret = JOptionPane.showOptionDialog(Opcat2.getFrame(),
					"Thing is defined by external library\nOpen library ?",
					"OPCAT II", JOptionPane.YES_NO_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, null, null);
			if (ret != JOptionPane.OK_OPTION) {
				return;
			}
			Iterator iter = Collections.list(
					((OpmThing) myEntry.getLogicalEntity()).getRolesManager()
							.getLoadedRoles()).iterator();

			while (iter.hasNext()) {
				Role role = (Role) iter.next();
				try {
					FileControl.getInstance().runNewOPCAT(
							new File(role.getOntology().getPath()),
							myInstance.getEntry().getId());
				} catch (Exception ex) {
					OpcatLogger.logError(ex);
				}
			}
			return;
		}

		this.currentOpd.removeSelection();
		// /
		OpdThing unfoldedThing = myInstance.getThing();
		Opd parent = getUnfoldingRoot();
		ThingInstance newInstance;
		long thingId = unfoldedThing.getEntity().getId();
		Integer nSize = (Integer) this.config.getProperty("NormalSize");
		Integer cSize = (Integer) this.config.getProperty("CurrentSize");

		double factor = cSize.doubleValue() / nSize.doubleValue();

		Opcat2.getUndoManager().discardAllEdits();
		Opcat2.setRedoEnabled(false);
		Opcat2.setUndoEnabled(false);

		this.tempNum = this._getFreeOpdEntry();
		this.currentOpd = new Opd(this, unfoldedThing + "  -  View OPD",
				this.tempNum, parent);
		this.currentOpd.setView(true);
		// currentOpd.show();
		this.mainComponentsStructure.put(this.tempNum, this.currentOpd);

		if (myEntry instanceof ProcessEntry) {
			newInstance = this.addProcess(0, 0, this.currentOpd
					.getOpdContainer().getDrawingArea(), thingId, -1);
		} else {
			newInstance = this.addObject(0, 0, this.currentOpd
					.getOpdContainer().getDrawingArea(), thingId, -1, true);
		}

		newInstance.setLocation((int) (350 * factor), (int) (20 * factor));
		newInstance.getConnectionEdge().fitToContent();

		this.currentOpd.setMainEntry(myEntry);
		this.currentOpd.setMainInstance(newInstance);

		this.setNames4OPDs();
		Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE_TREE);
		myEntry.updateInstances();

		// this.currentOpd.setVisible(true);
		this.showOPD(this.currentOpd.getOpdId());
		if (parentDesktop != null) {
			this.parentDesktop.getDesktopManager().deiconifyFrame(
					this.currentOpd.getOpdContainer());
		}
		try {
			this.currentOpd.getOpdContainer().setSelected(true);
		} catch (PropertyVetoException e) {
			OpcatLogger.logError(e);
		}
		this.currentOpd.refit();
		setCanClose(false);
	}

	public Opd unfolding() {

		ThingInstance myInstance;

		Instance tempComp = this.currentOpd.getSelectedItem();
		if ((tempComp == null) || !(tempComp instanceof ThingInstance)) {
			return null;
		}

		myInstance = (ThingInstance) tempComp;

		ThingEntry myEntry = (ThingEntry) myInstance.getEntry();
		// if (myEntry.isExternalFunction()) {
		//
		// int ret = JOptionPane.showOptionDialog(Opcat2.getFrame(),
		// "Thing is defined by external library\nOpen library ?",
		// "OPCAT II", JOptionPane.YES_NO_OPTION,
		// JOptionPane.INFORMATION_MESSAGE, null, null, null);
		// if (ret != JOptionPane.OK_OPTION) {
		// return null;
		// }
		// Iterator iter = Collections.list(
		// ((OpmThing) myEntry.getLogicalEntity()).getRolesManager()
		// .getLoadedRoles()).iterator();
		//
		// while (iter.hasNext()) {
		// Role role = (Role) iter.next();
		// try {
		// FileControl.getInstance().runNewOPCAT(
		// new File(role.getOntology().getPath()),
		// myInstance.getEntry().getId());
		// // URL url = new URL("file", "",
		// // role.getOntology().getPath());
		// // BrowserLauncher2.openURL(url.toExternalForm());
		// } catch (Exception ex) {
		// OpcatLogger.logError(ex);
		// }
		// }
		// return null;
		// }

		if (myInstance.getUnfoldingOpd() != null) {
			return null;
		}

		UnfoldingDialog diag = new UnfoldingDialog(Opcat2.getFrame());
		diag.setLocation(GraphicsUtils.getLastMouseEvent().getX(), // .getXOnScreen(),
				GraphicsUtils.getLastMouseEvent().getY()); // .getYOnScreen());
		if (myInstance.getUnfoldingOpd() == null) {
			diag.setVisible(true);
			if (diag.isCancled()) {
				return null;
			}
		}

		this.currentOpd.removeSelection();
		this.currentOpd = this
				.unfolding(myInstance, diag.isBringObjects(), diag
						.isBringProcesses(), diag.isAllLevels(), diag
						.isBringRole());

		currentOpd.setView(true);
		// this.currentOpd.setVisible(true);
		this.showOPD(this.currentOpd.getOpdId());
		if (parentDesktop != null) {
			this.parentDesktop.getDesktopManager().deiconifyFrame(
					this.currentOpd.getOpdContainer());
		}

		try {
			this.currentOpd.getOpdContainer().setSelected(true);
		} catch (PropertyVetoException e) {
			OpcatLogger.logError(e);
		}
		this.currentOpd.refit();

		return currentOpd;
	}

	private Opd getUnfoldingRoot() {
		Opd opd = getSystemStructure().getOpd(1);
		if (opd == null) {
			return this.currentOpd;
		} else {
			return opd;
		}
	}

	public Opd unfolding(ThingInstance parentInstance, boolean bringObjects,
			boolean bringProcesses, boolean allLevels,
			boolean bringRoleRelatedThings) {
		ThingEntry myEntry;
		ThingInstance newInstance;
		long thingId;
		Opd parent = getUnfoldingRoot();
		Hashtable addedInstances = new Hashtable();

		Integer nSize = (Integer) this.config.getProperty("NormalSize");
		Integer cSize = (Integer) this.config.getProperty("CurrentSize");

		double factor = cSize.doubleValue() / nSize.doubleValue();

		OpdThing unfoldedThing = parentInstance.getThing();

		thingId = unfoldedThing.getEntity().getId();
		myEntry = (ThingEntry) parentInstance.getEntry();

		Opd oldCurrentOpd = this.currentOpd;
		this.currentOpd = parentInstance.getUnfoldingOpd();

		if (this.currentOpd == null) {
			Opcat2.getUndoManager().discardAllEdits();
			Opcat2.setRedoEnabled(false);
			Opcat2.setUndoEnabled(false);

			this.tempNum = this._getFreeOpdEntry();
			this.currentOpd = new Opd(this, unfoldedThing
					+ "  -  Unfolding OPD", this.tempNum, parent);
			// currentOpd.show();
			this.mainComponentsStructure.put(this.tempNum, this.currentOpd);
			parentInstance.setUnfoldingOpd(this.currentOpd);

			if (myEntry instanceof ProcessEntry) {
				newInstance = this.addProcess(0, 0, this.currentOpd
						.getOpdContainer().getDrawingArea(), thingId, -1);
			} else {
				newInstance = this.addObject(0, 0, this.currentOpd
						.getOpdContainer().getDrawingArea(), thingId, -1, true);
			}

			newInstance.setLocation((int) (350 * factor), (int) (20 * factor));
			newInstance.getConnectionEdge().fitToContent();

			this.currentOpd.setMainEntry(myEntry);
			this.currentOpd.setMainInstance(newInstance);

			if (bringObjects || bringProcesses || bringRoleRelatedThings) {
				this.drawRelatedThings(newInstance, addedInstances,
						bringObjects, bringProcesses, bringRoleRelatedThings,
						allLevels, 1, new Hashtable());
			}

			this.setNames4OPDs();
			Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE_TREE);
			myEntry.updateInstances();
		}

		Opd retOpd = this.currentOpd;
		this.currentOpd = oldCurrentOpd;
		setCanClose(false);
		return retOpd;
	}

	/**
	 * Closes this project. This operation frees all resources used by this
	 * project.
	 */
	public void close() {
		// JOptionPane.showConfirmDialog(Opcat2.getFrame(), "OPDProject : start
		// of close() " ) ;

		try {
			// ArrayList<Opd> opds =
			// Collections.list(this.mainComponentsStructure.getOpds()) ;
			// JOptionPane.showConfirmDialog(Opcat2.getFrame(), "OPDProject :
			// start disposing of : " + opds.size() + " OPD's " ) ;
			// int i = 0 ;
			for (Enumeration<Opd> e = this.mainComponentsStructure.getOpds(); e
					.hasMoreElements();) {
				Opd opd = e.nextElement();
				opd.dispose();
				// i++ ;
				// JOptionPane.showConfirmDialog(Opcat2.getFrame(), "OPDProject
				// : disposed of " + i + " OPD's " ) ;
			}

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}

		Opcat2.getUndoManager().discardAllEdits();
		Opcat2.setRedoEnabled(false);
		Opcat2.setUndoEnabled(false);
		// JOptionPane.showConfirmDialog(Opcat2.getFrame(), "OPDProject : end of
		// close() " ) ;

	}

	private void drawRelatedThings(ThingInstance root,
			Hashtable addedInstances, boolean bringObjects,
			boolean bringProcesses, boolean bringRoleRelatedThings,
			boolean allLevels, int level, Hashtable layouts) {
		// OpmThing lRoot = (OpmThing)rootThing.getEntity();
		OpdThing rootThing = (OpdThing) root.getConnectionEdge();
		OpdThing gRelatedThing;

		OpmStructuralRelation lRelation;
		ThingEntry rootEntry;
		ThingEntry newEntry;
		ThingInstance newInstance = null;

		rootEntry = (ThingEntry) root.getEntry();
		double x, y;

		Integer nSize = (Integer) this.config.getProperty("NormalSize");
		Integer cSize = (Integer) this.config.getProperty("CurrentSize");

		double factor = cSize.doubleValue() / nSize.doubleValue();

		y = level * 250 * factor;

		Integer tempX = (Integer) layouts.get(new Integer(level));
		if (tempX == null) {
			x = 100 * factor;
		} else {
			x = tempX.intValue();
		}

		// make a vector of all relations including the inherited ones.
		Vector relationsVec = new Vector();
		if (bringObjects || bringProcesses) {
			HashMap relationMap = rootEntry.getAllInharatedProperties();
			Iterator relIter = relationMap.values().iterator();
			while (relIter.hasNext()) {
				OpmStructuralRelation opmrel = (OpmStructuralRelation) ((FundamentalRelationEntry) relIter
						.next()).getLogicalEntity();
				relationsVec.add(opmrel);
			}
			for (Enumeration e = rootEntry.getSourceRelations(); e
					.hasMoreElements();) {
				OpmStructuralRelation opmrel = (OpmStructuralRelation) (e
						.nextElement());
				relationsVec.add(opmrel);
			}
		}

		HashMap rolesMap = new HashMap();
		if (bringRoleRelatedThings) {
			Iterator rolesIter = rootEntry.getLogicalEntity().getRolesManager()
					.getLoadedRolesVector(MetaLibrary.TYPE_POLICY).iterator();
			while (rolesIter.hasNext()) {
				Role role = (Role) rolesIter.next();
				OpdProject roleProject = (OpdProject) role.getOntology()
						.getProjectHolder();
				ThingEntry roleEntry = (ThingEntry) roleProject
						.getSystemStructure().getEntry(
								((OpmThing) role.getThing()).getId());
				if (roleEntry != null) {

					HashMap relationMap = roleEntry
							.getAllFundemantulRelationSons();
					Iterator relIter = relationMap.keySet().iterator();
					while (relIter.hasNext()) {
						ThingEntry locEntry = (ThingEntry) relIter.next();
						FundamentalRelationEntry opmrel = (FundamentalRelationEntry) relationMap
								.get(locEntry);
						Role locRole = new Role(locEntry.getLogicalEntity(),
								role.getOntology());
						rolesMap.put(locRole, opmrel);
					}

					relationMap = roleEntry.getAllInharatedProperties();
					relIter = relationMap.keySet().iterator();
					while (relIter.hasNext()) {
						ThingEntry locEntry = (ThingEntry) relIter.next();
						FundamentalRelationEntry opmrel = (FundamentalRelationEntry) relationMap
								.get(locEntry);
						Role locRole = new Role(locEntry.getLogicalEntity(),
								role.getOntology());
						rolesMap.put(locRole, opmrel);
					}

				}
			}
		}
		for (Enumeration e = relationsVec.elements(); e.hasMoreElements();) {
			RelativeConnectionPoint sourcePoint = new RelativeConnectionPoint(
					OpcatConstants.S_BORDER, 0.5);
			RelativeConnectionPoint targetPoint = new RelativeConnectionPoint(
					OpcatConstants.N_BORDER, 0.5);

			lRelation = (OpmStructuralRelation) (e.nextElement());

			Entry relEntry = (RelationEntry) (this.mainComponentsStructure
					.getEntry(lRelation.getId()));

			// now for all the instances of this relation bring the
			// destinations.
			Enumeration relInstancesEnum = relEntry.getInstances();
			while (relInstancesEnum.hasMoreElements()) {
				Instance relInstance = (Instance) relInstancesEnum
						.nextElement();

				// newEntry = (ThingEntry) (this.mainComponentsStructure
				// .getEntry(lRelation.getDestinationId()));

				newEntry = (ThingEntry) (this.mainComponentsStructure
						.getEntry(((FundamentalRelationInstance) relInstance)
								.getDestinationInstance().getEntry().getId()));

				long thingId = newEntry.getId();

				newInstance = (ThingInstance) addedInstances.get(new Long(
						thingId));

				if (newInstance == null) {
					if (bringProcesses && (newEntry instanceof ProcessEntry)) {
						newInstance = this.addProcess(0, 0, this.currentOpd
								.getOpdContainer().getDrawingArea(), thingId,
								-1);
					}
					// NOTE
					else if (bringObjects && (newEntry instanceof ObjectEntry)) {
						newInstance = this.addObject(0, 0, this.currentOpd
								.getOpdContainer().getDrawingArea(), thingId,
								-1, true);
					}
					if (newInstance != null) {
						addedInstances.put(new Long(thingId), newInstance);
						newInstance.setLocation((int) x, (int) y);
						newInstance.getConnectionEdge().fitToContent();
						x += 160 * factor;
						if (allLevels)
							this.drawRelatedThings(newInstance, addedInstances,
									bringObjects, bringProcesses,
									bringRoleRelatedThings, allLevels,
									level + 1, layouts);
					}
				}
			}

			if (newInstance != null) {
				gRelatedThing = newInstance.getThing();
				this.addRelation(rootThing, sourcePoint, gRelatedThing,
						targetPoint, Constants.getType4Relation(lRelation),
						rootThing.getParent(), -1, -1);
			}

		}

		if (bringRoleRelatedThings) {
			Iterator iter = rolesMap.keySet().iterator();
			while (iter.hasNext()) {
				Role myRole = (Role) iter.next();
				OpmThing roleOpmEntry = (OpmThing) myRole.getThing();
				ThingEntry roleEntry = (ThingEntry) ((OpdProject) myRole
						.getOntology().getProjectHolder()).getSystemStructure()
						.getEntry(roleOpmEntry.getId());
				ThingInstance roleInstance = (ThingInstance) roleEntry
						.getInstances().nextElement();

				FundamentalRelationEntry roleRelation = (FundamentalRelationEntry) rolesMap
						.get(myRole);

				// add an entry of
				newInstance = null;
				if (bringProcesses && (roleEntry instanceof ProcessEntry)) {
					newInstance = addProcess(0, 0, this.currentOpd
							.getOpdContainer().getDrawingArea(), -1, -1);
				} else if (bringObjects && (roleEntry instanceof ObjectEntry)) {
					newInstance = addObject(0, 0, this.currentOpd
							.getOpdContainer().getDrawingArea(), -1, -1, true);

				}

				if (newInstance != null) {
					addedInstances.put(
							new Long(newInstance.getEntry().getId()),
							newInstance);

					newInstance.getEntry().setName(roleOpmEntry.getName());
					if (newInstance instanceof ObjectInstance) {
						((OpmObject) newInstance.getEntry().getLogicalEntity())
								.copyPropsFrom((OpmObject) roleOpmEntry);
					} else {
						((OpmProcess) newInstance.getEntry().getLogicalEntity())
								.copyPropsFrom((OpmProcess) roleOpmEntry);
					}
					newInstance.setLocation((int) x, (int) y);
					newInstance.setBackgroundColor(roleInstance
							.getBackgroundColor());
					newInstance.setBorderColor(roleInstance.getBorderColor());
					newInstance.setTextColor(roleInstance.getTextColor());
					newInstance.setTextPosition(roleInstance.getTextPosition());

					Role role = new Role(roleOpmEntry, myRole.getOntology());
					newInstance.getEntry().addRole(role);

					newInstance.getOpd().getDrawingArea().repaint();

					newInstance.getConnectionEdge().fitToContent();

					// now add the relation
					RelativeConnectionPoint sourcePoint = new RelativeConnectionPoint(
							OpcatConstants.S_BORDER, 0.5);
					RelativeConnectionPoint targetPoint = new RelativeConnectionPoint(
							OpcatConstants.N_BORDER, 0.5);
					addRelation(root.getThing(), sourcePoint, newInstance
							.getThing(), targetPoint, roleRelation
							.getRelationType(), root.getOpd().getDrawingArea(),
							-1, -1);

					x += 160 * factor;
					if (allLevels)
						this.drawRelatedThings(newInstance, addedInstances,
								bringObjects, bringProcesses,
								bringRoleRelatedThings, allLevels, level + 1,
								layouts);
				}
			}
		}

		newInstance = null;
		if (bringObjects || bringProcesses) {
			Iterator i = rootEntry.getSourceGeneralRelations();
			for (; i.hasNext();) {

				Object obj = i.next();

				lRelation = null;
				if (obj instanceof OpmGeneralRelation) {
					lRelation = (OpmGeneralRelation) obj;
				} else {
					OpcatLogger.logError(obj.getClass().getCanonicalName());
					continue;
				}

				Entry relEntry = (GeneralRelationEntry) (this.mainComponentsStructure
						.getEntry(lRelation.getId()));

				// now for all the instances of this relation bring the
				// destinations.
				Enumeration relInstancesEnum = relEntry.getInstances();
				while (relInstancesEnum.hasMoreElements()) {
					Instance relInstance = (Instance) relInstancesEnum
							.nextElement();

					// newEntry = (ThingEntry) (this.mainComponentsStructure
					// .getEntry(lRelation.getDestinationId()));

					newEntry = (ThingEntry) (this.mainComponentsStructure
							.getEntry(((GeneralRelationInstance) relInstance)
									.getDestinationInstance().getEntry()
									.getId()));

					long thingId = newEntry.getId();

					newInstance = (ThingInstance) addedInstances.get(new Long(
							thingId));

					if (newInstance == null) {

						if (bringProcesses
								&& (newEntry instanceof ProcessEntry)) {
							newInstance = this.addProcess(0, 0, this.currentOpd
									.getOpdContainer().getDrawingArea(),
									thingId, -1);
						} else if (bringObjects
								&& (newEntry instanceof ObjectEntry)) {
							newInstance = this.addObject(0, 0, this.currentOpd
									.getOpdContainer().getDrawingArea(),
									thingId, -1, true);
						}
						if (newInstance != null) {
							addedInstances.put(new Long(thingId), newInstance);
							newInstance.setLocation((int) x, (int) y);
							newInstance.getConnectionEdge().fitToContent();
							x += 160 * factor;
						}
					}
					if (newInstance != null) {

						gRelatedThing = newInstance.getThing();

						RelativeConnectionPoint sourcePoint = GraphicsUtils
								.calculateConnectionPoint(rootThing,
										gRelatedThing);
						RelativeConnectionPoint targetPoint = GraphicsUtils
								.calculateConnectionPoint(gRelatedThing,
										rootThing);

						GeneralRelationInstance gi = this.addGeneralRelation(
								rootThing, sourcePoint, gRelatedThing,
								targetPoint, rootThing.getParent(), Constants
										.getType4Relation(lRelation), -1, -1);

						gi.update();
					}
				}
			}
		}

		layouts.put(new Integer(level), new Integer((int) x));
		return;

	}

	private boolean _isSource4link(ThingInstance ti, LinkInstance li) {
		return (ti.getEntry().getId() == li.getSource().getEntry().getId());
	}

	private void drawLinkedThings(ThingInstance rootInstance,
			ThingInstance originInstance) {
		Vector sourceLinks = new Vector();
		Vector destinationLinks = new Vector();
		Hashtable addedInstances = new Hashtable();

		for (Enumeration e = originInstance.getRelatedInstances(); e
				.hasMoreElements();) {
			Object currInstance = e.nextElement();

			if (currInstance instanceof LinkInstance) {
				if (this._isSource4link(originInstance,
						(LinkInstance) currInstance)) {
					sourceLinks.add(currInstance);
				} else {
					destinationLinks.add(currInstance);
				}
			}
		}

		ThingInstance originParent = (ThingInstance) originInstance
				.getParentIXThingInstance();

		if (originParent != null) {
			for (Enumeration e2 = originParent.getRelatedInstances(); e2
					.hasMoreElements();) {

				Instance currInstance = (Instance) e2.nextElement();
				if (currInstance instanceof LinkInstance) {
					OpmProceduralLink currLink = (OpmProceduralLink) currInstance
							.getEntry().getLogicalEntity();

					CommandWrapper cw;
					if (this._isSource4link(originParent,
							(LinkInstance) currInstance)) {
						cw = new CommandWrapper(
								rootInstance.getEntry().getId(), rootInstance
										.getKey(),
								((LinkInstance) currInstance)
										.getDestinationInstance().getEntry()
										.getId(), ((LinkInstance) currInstance)
										.getDestinationInstance().getKey(),
								Constants.getType4Link(currLink), this);
					} else {
						cw = new CommandWrapper(((LinkInstance) currInstance)
								.getSourceInstance().getEntry().getId(),
								((LinkInstance) currInstance)
										.getSourceInstance().getKey(),
								rootInstance.getEntry().getId(), rootInstance
										.getKey(), Constants
										.getType4Link(currLink), this);
					}

					CheckResult cr = CheckModule.checkCommand(cw);
					if (cr.getResult() == IXCheckResult.RIGHT) {
						if (this._isSource4link(originParent,
								(LinkInstance) currInstance)) {
							// check if the entry is alrady in
							long insID = currInstance.getEntry().getId();
							int i = 0;
							for (i = 0; i < sourceLinks.size(); i++) {
								if (((Instance) sourceLinks.get(i)).getEntry()
										.getId() == insID) {
									break;
								}
							}
							if (i == sourceLinks.size())
								sourceLinks.add(currInstance);
						} else {

							long insID = currInstance.getEntry().getId();
							int i = 0;
							for (i = 0; i < destinationLinks.size(); i++) {
								if (((Instance) destinationLinks.get(i))
										.getEntry().getId() == insID) {
									break;
								}
							}
							if (i == destinationLinks.size())
								destinationLinks.add(currInstance);
						}
					}

				}
			}
		}

		for (int i = 0; i < sourceLinks.size(); i++) {
			this._drawElement(rootInstance, (LinkInstance) sourceLinks.get(i),
					true, i, sourceLinks.size() + destinationLinks.size(),
					addedInstances);
		}

		try {
			for (int i = 0; i < destinationLinks.size(); i++) {
				this._drawElement(rootInstance, (LinkInstance) destinationLinks
						.get(i), false, i + sourceLinks.size(), sourceLinks
						.size()
						+ destinationLinks.size(), addedInstances);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// _drawElements(sourceLinks, addedInstances, rootThing, 0,
		// sourceLinks.size()+destinationLinks.size(), true);
		// _drawElements(destinationLinks, addedInstances, rootThing,
		// sourceLinks.size(), sourceLinks.size()+destinationLinks.size(),
		// false);

	}

	private void _drawElement(ThingInstance rootInstance,
			LinkInstance addedLink, boolean isSource, int numInCircle,
			int numOfLinks, Hashtable addedInstances) {
		// OpmProceduralLink lLink;
		ThingInstance newInstance = null;
		OpdConnectionEdge gRelatedThing = null;
		ConnectionEdgeEntry newEntry = null;

		Integer nSize = (Integer) this.config.getProperty("NormalSize");
		Integer cSize = (Integer) this.config.getProperty("CurrentSize");
		double factor = cSize.doubleValue() / nSize.doubleValue();

		LinkEntry addedLinkEntry = (LinkEntry) addedLink.getEntry();
		int linkType = addedLinkEntry.getLinkType();

		if (isSource) {
			newEntry = (ConnectionEdgeEntry) this.mainComponentsStructure
					.getEntry(addedLink.getDestination().getEntry().getId());
		} else {
			newEntry = (ConnectionEdgeEntry) (this.mainComponentsStructure
					.getEntry(addedLink.getSource().getEntry().getId()));
		}

		long thingId = newEntry.getId();
		newInstance = (ThingInstance) addedInstances.get(new Long(thingId));

		if (newEntry instanceof ProcessEntry) {
			if (newInstance == null) {
				newInstance = this.addProcess(0, 0, this.currentOpd
						.getOpdContainer().getDrawingArea(), thingId, -1);
				addedInstances.put(new Long(thingId), newInstance);
			}
			gRelatedThing = newInstance.getThing();
		}

		if (newEntry instanceof ObjectEntry) {
			if (newInstance == null) {
				newInstance = this.addObject(0, 0, this.currentOpd
						.getOpdContainer().getDrawingArea(), thingId, -1, true);
				addedInstances.put(new Long(thingId), newInstance);
			}
			gRelatedThing = newInstance.getThing();
		}

		if (newEntry instanceof StateEntry) {
			StateEntry se = (StateEntry) newEntry;
			thingId = se.getParentObjectId();

			newInstance = (ThingInstance) addedInstances.get(new Long(thingId));
			if (newInstance == null) {
				newInstance = this.addObject(0, 0, this.currentOpd
						.getOpdContainer().getDrawingArea(), thingId, -1, true);
				addedInstances.put(new Long(thingId), newInstance);
			}

			for (Enumeration e = ((ObjectInstance) newInstance)
					.getStateInstances(); e.hasMoreElements();) {
				StateInstance currState = (StateInstance) e.nextElement();
				if (currState.getEntry().getId() == se.getId()) {
					gRelatedThing = currState.getConnectionEdge();
					break;
				}
			}

		}

		int size = (numOfLinks + 1) / 2;
		int currAngle = numInCircle / 2 + 1;
		double k = Math.tan(((Math.PI - 0.0001) * currAngle) / size + Math.PI
				/ 4);
		double a2 = rootInstance.getWidth() / 2 + 140 * factor;
		a2 = a2 * a2;
		double b2 = rootInstance.getHeight() / 2 + 140 * factor;
		b2 = b2 * b2;
		double x = (Math.sqrt((a2 * b2) / (b2 + a2 * k * k)));

		if (numInCircle % 2 == 0) {
			x = -1 * x;
		}

		double y = k * x;

		newInstance.getConnectionEdge().fitToContent();

		x = x + rootInstance.getX() + rootInstance.getWidth() / 2
				- newInstance.getWidth() / 2;
		y = y + rootInstance.getY() + rootInstance.getHeight() / 2
				- newInstance.getHeight() / 2;
		newInstance.setLocation((int) x, (int) y);

		// Extra check for link legality is for avoiding of links duplications
		if (isSource) {

			CommandWrapper cw = new CommandWrapper(rootInstance.getEntry()
					.getId(), rootInstance.getKey(), gRelatedThing.getEntity()
					.getId(), new OpdKey(gRelatedThing.getOpdId(),
					gRelatedThing.getEntityInOpdId()), linkType, this);

			CheckResult cr = CheckModule.checkCommand(cw);
			if (cr.getResult() == IXCheckResult.RIGHT) {
				RelativeConnectionPoint sourcePoint = GraphicsUtils
						.calculateConnectionPoint(rootInstance
								.getConnectionEdge(), gRelatedThing);
				RelativeConnectionPoint targetPoint = GraphicsUtils
						.calculateConnectionPoint(gRelatedThing, rootInstance
								.getConnectionEdge());

				this.addLink(rootInstance.getConnectionEdge(), sourcePoint,
						gRelatedThing, targetPoint, rootInstance
								.getConnectionEdge().getParent(), linkType, -1,
						-1);
			}
		} else {

			CommandWrapper cw = new CommandWrapper(gRelatedThing.getEntity()
					.getId(), new OpdKey(gRelatedThing.getOpdId(),
					gRelatedThing.getEntityInOpdId()), rootInstance.getEntry()
					.getId(), rootInstance.getKey(), linkType, this);

			CheckResult cr = CheckModule.checkCommand(cw);
			if (cr.getResult() == IXCheckResult.RIGHT) {
				RelativeConnectionPoint sourcePoint = GraphicsUtils
						.calculateConnectionPoint(gRelatedThing, rootInstance
								.getConnectionEdge());
				RelativeConnectionPoint targetPoint = GraphicsUtils
						.calculateConnectionPoint(rootInstance
								.getConnectionEdge(), gRelatedThing);

				this.addLink(gRelatedThing, sourcePoint, rootInstance
						.getConnectionEdge(), targetPoint, rootInstance
						.getConnectionEdge().getParent(), linkType, -1, -1);
			}
		}

	}

	/**
	 * Zooms in (graphically - not OPM scaling operation) current OPD
	 */
	public void viewZoomIn(double zoomFactor) {
		for (Enumeration e = this.mainComponentsStructure.getOpds(); e
				.hasMoreElements();) {
			((Opd) e.nextElement()).viewZoomIn(zoomFactor);
		}
	}

	public void viewZoomIn(double widthFactor, double highetFactor) {
		for (Enumeration e = this.mainComponentsStructure.getOpds(); e
				.hasMoreElements();) {
			((Opd) e.nextElement()).viewZoomIn(widthFactor, highetFactor);
		}
	}

	/**
	 * Zooms out (graphically - not OPM scaling operation) current OPD
	 */

	public void viewZoomOut() {
		if (this.currentOpd == null) {
			return;
		}
		this.currentOpd.viewZoomOut(1.2);
	}

	/**
	 * Adds selected item to the Hashtable of selected object. (for multiple
	 * selection)
	 */
	public void addSelection(Instance sItem, boolean removePrevious) {
		if (this.currentOpd == null) {
			return;
		}
		this.currentOpd.addSelection(sItem, removePrevious);
	}

	public void addSelection(BaseGraphicComponent sItem, boolean removePrevious) {
		if (this.currentOpd == null) {
			return;
		}
		this.currentOpd.addSelection(sItem, removePrevious);
	}

	/**
	 * Clears Hashtable of selected object
	 */

	public void removeSelection() {
		if (this.currentOpd == null) {
			return;
		}
		this.currentOpd.removeSelection();
	}

	public void removeSelection(Instance inst) {
		this.currentOpd.removeSelection(inst);
	}

	public IXObjectInstance addObject(String name, int x, int y, long opdId) {

		Opd tempOpd = this.mainComponentsStructure.getOpd(opdId);

		if (tempOpd == null) {
			return null;
		}
		if ((this.currentOpd == null) || (this.currentOpd.getOpdId() != opdId)) {
			this.showOPD(opdId);
		}

		IXObjectInstance newIns = this.addObject(x, y,
				tempOpd.getDrawingArea(), -1, -1, false);
		newIns.getIXEntry().setName(name);

		((ObjectInstance) newIns).getConnectionEdge().fitToContent();
		newIns.setLocation(x, y);
		newIns.getIXEntry().updateInstances();
		return newIns;

	}

	public IXObjectInstance addObject(String name, int x, int y,
			IXThingInstance parentInstance) {
		if (!parentInstance.isZoomedIn()) {
			return null;
		}

		Opd tempOpd = this.mainComponentsStructure.getOpd(parentInstance
				.getKey().getOpdId());

		if (tempOpd == null) {
			return null;
		}
		if ((this.currentOpd == null)
				|| (this.currentOpd.getOpdId() != tempOpd.getOpdId())) {
			this.showOPD(tempOpd.getOpdId());
		}

		Entry tempEntry = this.mainComponentsStructure.getEntry(parentInstance
				.getLogicalId());
		ThingInstance tempInstance = (ThingInstance) tempEntry
				.getInstance(parentInstance.getKey());
		IXObjectInstance newIns = this.addObject(x, y, tempInstance.getThing(),
				-1, -1, false);
		newIns.getIXEntry().setName(name);
		newIns.getIXEntry().updateInstances();
		return newIns;
	}

	public UndoableDeleteObject deleteObject(ObjectInstance delInstance) {

		UndoableDeleteObject ud = new UndoableDeleteObject(this, delInstance);
		ThingEntry selectedEntry = (ThingEntry) delInstance.getEntry();

		for (Enumeration e = delInstance.getStateInstances(); e
				.hasMoreElements();) {
			StateInstance si = (StateInstance) e.nextElement();
			StateEntry se = (StateEntry) si.getEntry();

			se.removeInstance(si.getKey());

			if (se.isEmpty()) {
				this.mainComponentsStructure.removeEntry(se.getId());
			}
		}

		selectedEntry.removeInstance(delInstance.getKey());

		if (selectedEntry.isEmpty()) {
			this.mainComponentsStructure.removeEntry(selectedEntry.getId());
		}
		GuiControl gui = GuiControl.getInstance();
		gui.getRepository().rebuildTrees(true);
		return ud;
	}

	public IXProcessInstance addProcess(String name, int x, int y, long opdId) {
		Opd tempOpd = this.mainComponentsStructure.getOpd(opdId);

		if (tempOpd == null) {
			return null;
		}

		if ((this.currentOpd == null) || (this.currentOpd.getOpdId() != opdId)) {
			this.showOPD(opdId);
		}

		IXProcessInstance newIns = this.addProcess(x, y, tempOpd
				.getDrawingArea(), -1, -1);
		newIns.getIXEntry().setName(name);
		((ProcessInstance) (newIns)).getConnectionEdge().fitToContent();
		newIns.setLocation(x, y);
		newIns.getIXEntry().updateInstances();
		return newIns;
	}

	public IXProcessInstance addProcess(Opd destOpd, String name, int x, int y,
			IXThingInstance parentInstance) {

		Opd current = null;
		if (destOpd != null) {
			current = getCurrentOpd();
			setCurrentOpd(destOpd);
		}

		IXProcessInstance myInstance = addProcess(name, x, y, parentInstance);
		if (current != null)
			setCurrentOpd(current);

		return myInstance;

	}

	public IXProcessInstance addProcess(String name, int x, int y,
			IXThingInstance parentInstance) {
		if (!parentInstance.isZoomedIn()) {
			return null;
		}

		Opd tempOpd = this.mainComponentsStructure.getOpd(parentInstance
				.getKey().getOpdId());

		if (tempOpd == null) {
			return null;
		}
		if ((this.currentOpd == null)
				|| (this.currentOpd.getOpdId() != tempOpd.getOpdId())) {
			this.showOPD(tempOpd.getOpdId());
		}

		Entry tempEntry = this.mainComponentsStructure.getEntry(parentInstance
				.getLogicalId());
		ThingInstance tempInstance = (ThingInstance) tempEntry
				.getInstance(parentInstance.getKey());
		IXProcessInstance newIns = this.addProcess(x, y, tempInstance
				.getThing(), -1, -1);
		newIns.getIXEntry().setName(name);
		newIns.getIXEntry().updateInstances();
		return newIns;
	}

	public UndoableDeleteProcess deleteProcess(ProcessInstance delInstance) {

		UndoableDeleteProcess ud = new UndoableDeleteProcess(this, delInstance);
		ThingEntry selectedEntry = (ThingEntry) delInstance.getEntry();

		selectedEntry.removeInstance(delInstance.getKey());

		if (selectedEntry.isEmpty()) {
			this.mainComponentsStructure.removeEntry(selectedEntry.getId());
		}

		GuiControl gui = GuiControl.getInstance();
		gui.getRepository().rebuildTrees(true);
		return ud;
	}

	private Point computeRelationPoint(OpdBaseComponent source,
			OpdBaseComponent destination) {

		int tempX, tempY;
		int relWidth = ((Integer) this.config
				.getProperty("FundamentalRelationWidth")).intValue();
		int relHeight = ((Integer) this.config
				.getProperty("FundamentalRelationHeight")).intValue();

		tempX = (source.getX() + source.getWidth() / 2 + destination.getX() + destination
				.getWidth() / 2)
				/ 2 - relWidth / 2;
		tempY = (source.getY() + source.getHeight() / 2 + destination.getY() + destination
				.getHeight() / 2)
				/ 2 - relHeight / 2;
		// if (source.getX()+source.getWidth() < destination.getX())
		// {
		// tempX = ((source.getX()+source.getWidth()+destination.getX())/2);
		// }
		// else
		// {
		// tempX =
		// ((source.getX()+destination.getWidth()+destination.getX())/2);
		// }
		//
		// if (source.getY()+source.getHeight() < destination.getY())
		// {
		// tempY = ((source.getY()+source.getHeight()+destination.getY())/2);
		// }
		// else
		// {
		// tempY =
		// ((source.getY()+destination.getHeight()+destination.getY())/2);
		// }

		return new Point(tempX, tempY);
	}

	private OpmFundamentalRelation getRelation4type(int type, long id,
			OpmConnectionEdge source, OpmConnectionEdge destination) {
		switch (type) {
		case OpcatConstants.AGGREGATION_RELATION:
			return new OpmAggregation(id, "Aggregation " + id, source,
					destination);
		case OpcatConstants.EXHIBITION_RELATION:
			return new OpmExhibition(id, "Featuring " + id, source, destination);
		case OpcatConstants.INSTANTINATION_RELATION:
			return new OpmInstantination(id, "Instantination " + id, source,
					destination);
		case OpcatConstants.SPECIALIZATION_RELATION:
			return new OpmSpecialization(id, "Generalization " + id, source,
					destination);

		default: {
			JOptionPane
					.showMessageDialog(
							Opcat2.getFrame(),
							" Serious internal bug occured in AddRelation function \n Please contact software developers.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
			return null;
		}
		}
	}

	public IXRelationInstance addRelation(IXConnectionEdgeInstance source,
			IXConnectionEdgeInstance destination, int relationType) {
		Opd tOpd = this.mainComponentsStructure.getOpd(source.getKey()
				.getOpdId());

		if (tOpd == null) {
			return null;
		}

		if (isConnectedWithRelation((ConnectionEdgeInstance) source,
				(ConnectionEdgeInstance) destination, relationType)) {
			return null;
		}

		if ((!OpcatConstants.isRelation(relationType))
				|| (tOpd.getOpdId() != destination.getKey().getOpdId())) {
			return null;
		}

		if ((this.currentOpd == null)
				|| (this.currentOpd.getOpdId() != tOpd.getOpdId())) {
			this.showOPD(tOpd.getOpdId());
		}

		Entry sourceEntry = this.mainComponentsStructure.getEntry(source
				.getLogicalId());
		ConnectionEdgeInstance sourceInstance = (ConnectionEdgeInstance) sourceEntry
				.getInstance(source.getKey());

		Entry destinationEntry = this.mainComponentsStructure
				.getEntry(destination.getLogicalId());
		ConnectionEdgeInstance destinationInstance = (ConnectionEdgeInstance) destinationEntry
				.getInstance(destination.getKey());

		RelativeConnectionPoint sourcePoint = GraphicsUtils
				.calculateConnectionPoint(sourceInstance.getConnectionEdge(),
						destinationInstance.getConnectionEdge());
		RelativeConnectionPoint destinationPoint = GraphicsUtils
				.calculateConnectionPoint(destinationInstance
						.getConnectionEdge(), sourceInstance
						.getConnectionEdge());

		JLayeredPane tempContainer = tOpd.getDrawingArea();

		if (sourceInstance.getConnectionEdge().getParent() == destinationInstance
				.getConnectionEdge().getParent()) {
			tempContainer = (JLayeredPane) sourceInstance.getConnectionEdge()
					.getParent();
		}

		if ((relationType == OpcatConstants.BI_DIRECTIONAL_RELATION)
				|| (relationType == OpcatConstants.UNI_DIRECTIONAL_RELATION)) {
			return this.addGeneralRelation(sourceInstance.getConnectionEdge(),
					sourcePoint, destinationInstance.getConnectionEdge(),
					destinationPoint, tempContainer, relationType, -1, -1);
		}
		return this.addRelation(sourceInstance.getConnectionEdge(),
				sourcePoint, destinationInstance.getConnectionEdge(),
				destinationPoint, relationType, tempContainer, -1, -1);

	}

	public boolean isConnectedWithRelation(ConnectionEdgeInstance source,
			ConnectionEdgeInstance destination, int relationType) {

		Enumeration<FundamentalRelationInstance> funInstances = source
				.getRelatedSourceFundamentalRelation();
		while (funInstances.hasMoreElements()) {
			FundamentalRelationInstance fun = ((FundamentalRelationInstance) funInstances
					.nextElement());
			if (((FundamentalRelationEntry) fun.getEntry()).getRelationType() == relationType
					&& fun.getDestinationInstance().getKey().equals(
							destination.getKey())) {
				return true;
			}
		}

		Iterator<GeneralRelationInstance> genInstances = source
				.getRelatedSourceGeneralRelations().iterator();

		while (genInstances.hasNext()) {
			GeneralRelationInstance gen = ((GeneralRelationInstance) genInstances
					.next());
			if (((GeneralRelationEntry) gen.getEntry()).getRelationType() == relationType
					&& gen.getDestinationInstance().getKey().equals(
							destination.getKey())) {
				return true;
			}
		}

		return false;
	}

	public boolean isConnectedWithLink(ConnectionEdgeInstance source,
			ConnectionEdgeInstance destination, int linkType) {

		Enumeration<LinkInstance> funInstances = source.getRelatedSourceLinks();
		while (funInstances.hasMoreElements()) {
			LinkInstance fun = ((LinkInstance) funInstances.nextElement());

			if (((LinkEntry) fun.getEntry()).getLinkType() == linkType
					&& fun.getDestinationInstance().getKey().equals(
							destination.getKey())) {
				return true;
			}
		}

		return false;
	}

	public IXLinkInstance addLink(IXConnectionEdgeInstance source,
			IXConnectionEdgeInstance destination, int linkType) {
		Opd tOpd = this.mainComponentsStructure.getOpd(source.getKey()
				.getOpdId());

		if (tOpd == null) {
			return null;
		}

		if (isConnectedWithLink((ConnectionEdgeInstance) source,
				(ConnectionEdgeInstance) destination, linkType)) {
			return null;
		}

		if ((!OpcatConstants.isLink(linkType))
				|| (tOpd.getOpdId() != destination.getKey().getOpdId())) {
			return null;
		}

		if ((this.currentOpd == null)
				|| (this.currentOpd.getOpdId() != tOpd.getOpdId())) {
			this.showOPD(tOpd.getOpdId());
		}

		Entry sourceEntry = this.mainComponentsStructure.getEntry(source
				.getLogicalId());
		ConnectionEdgeInstance sourceInstance = (ConnectionEdgeInstance) sourceEntry
				.getInstance(source.getKey());

		Entry destinationEntry = this.mainComponentsStructure
				.getEntry(destination.getLogicalId());
		ConnectionEdgeInstance destinationInstance = (ConnectionEdgeInstance) destinationEntry
				.getInstance(destination.getKey());

		RelativeConnectionPoint sourcePoint = GraphicsUtils
				.calculateConnectionPoint(sourceInstance.getConnectionEdge(),
						destinationInstance.getConnectionEdge());
		RelativeConnectionPoint destinationPoint = GraphicsUtils
				.calculateConnectionPoint(destinationInstance
						.getConnectionEdge(), sourceInstance
						.getConnectionEdge());

		JLayeredPane tempContainer = tOpd.getDrawingArea();

		if (sourceInstance.getConnectionEdge().getParent() == destinationInstance
				.getConnectionEdge().getParent()) {
			tempContainer = (JLayeredPane) sourceInstance.getConnectionEdge()
					.getParent();
		}

		return this.addLink(sourceInstance.getConnectionEdge(), sourcePoint,
				destinationInstance.getConnectionEdge(), destinationPoint,
				tempContainer, linkType, -1, -1);
	}

	/**
	 * Adds procedural link between source and destination to the specified
	 * container. linkType specifies which type of procedural link we add.
	 * 
	 * @param source
	 *            source thing for this procedural link.
	 * @param side1
	 *            - the side of source component. Possible values are defined as
	 *            constants in file - OpdBaseComponent.
	 * @param param1
	 *            Relative coordinate of the point to which we connect this link
	 *            on the source component. Float number between 0 and 1 that
	 *            means which point (relatively) is connection point. In order
	 *            to get absolute point of connection(in coordinates of
	 *            connected object) just multiply the length of connection side
	 *            by this number.
	 * @param destination
	 *            destination thing for this procedural link.
	 * @param side2
	 *            - the side of destination component. Possible values are
	 *            defined as constants in file - OpdBaseComponent.
	 * @param param2
	 *            Relative coordinate of the point to which we connect this link
	 *            on the destination component. Float number between 0 and 1
	 *            that means which point (relatively) is connection point. In
	 *            order to get absolute point of connection(in coordinates of
	 *            connected object) just multiply the length of connection side
	 *            by this number.
	 * @param linkType
	 *            one of constants defined in Interface OpmEntities.Constants
	 *            specifying which kind of procedural link will be added.
	 * @param container
	 *            Container to which we add this procedural link
	 */
	private LinkInstance _addLink(OpdConnectionEdge source,
			RelativeConnectionPoint pPoint1, OpdConnectionEdge destination,
			RelativeConnectionPoint pPoint2, Container container, int linkType,
			long entityId, long entityInOpdId) {

		if ((linkType == OpcatConstants.EFFECT_LINK)
				&& (source instanceof OpdProcess)) {
			return this._addLink(destination, pPoint2, source, pPoint1,
					container, linkType, entityId, entityInOpdId);
		}

		OpmProceduralLink lLink = null;
		LinkEntry myEntry;
		LinkInstance myInstance;
		ConnectionEdgeEntry sourceEntry, destinationEntry;

		long linkId;
		long linkInOpdId;
		OpdKey instanceId;

		long sourceId = source.getEntity().getId();
		long destinationId = destination.getEntity().getId();

		sourceEntry = (ConnectionEdgeEntry) (this.mainComponentsStructure
				.getEntry(sourceId));
		destinationEntry = (ConnectionEdgeEntry) (this.mainComponentsStructure
				.getEntry(destinationId));

		// if (entityId == -1) {
		/**
		 * check if there is alrady an entry which can be used for this link.
		 */
		// entityId = LinkEntry.getEntryID((ConnectionEdgeEntry) source
		// .getEntry(), (ConnectionEdgeEntry) destination.getEntry());
		/**
		 * this is kinda global. i hope it will be ok. we will see in the
		 * testing. this is used ? as i took care of the in-zoom and switch
		 * links else where.
		 */
		// }
		if (entityId != -1) {
			myEntry = (LinkEntry) this.mainComponentsStructure
					.getEntry(entityId);
			lLink = (OpmProceduralLink) myEntry.getLogicalEntity();
		} else {
			lLink = sourceEntry.getLinkByDestination(destinationId, linkType);

			if (lLink == null) {
				linkId = this._getFreeEntityEntry();
				lLink = this.createLink4Type(linkId, linkType, sourceId,
						destinationId);
				myEntry = new LinkEntry(lLink, this);
				this.mainComponentsStructure.put(lLink.getId(), myEntry);
			} else {
				myEntry = (LinkEntry) this.mainComponentsStructure
						.getEntry(lLink.getId());
				lLink = (OpmProceduralLink) myEntry.getLogicalEntity();
			}

		}

		sourceEntry.addLinkSource(lLink);
		destinationEntry.addLinkDestination(lLink);

		if (entityInOpdId == -1) {
			linkInOpdId = this.currentOpd._getFreeEntityInOpdEntry();
		} else {
			linkInOpdId = entityInOpdId;
		}

		instanceId = new OpdKey(this.currentOpd.getOpdId(), linkInOpdId);
		myInstance = new LinkInstance(source, pPoint1, destination, pPoint2,
				lLink, instanceId, this, container);

		// currentOpd.addLinkInstance(myInstance, container);

		myEntry.addInstance(instanceId, myInstance);
		return myInstance;
	}

	private OpmProceduralLink createLink4Type(long linkId, int linkType,
			long sourceId, long destinationId) {
		switch (linkType) {
		case OpcatConstants.AGENT_LINK:
			return new OpmAgent(linkId, "Agent " + linkId, sourceId,
					destinationId);
		case OpcatConstants.INSTRUMENT_LINK:
			return new OpmInstrument(linkId, "Instrument " + linkId, sourceId,
					destinationId);
		case OpcatConstants.CONDITION_LINK:
			return new OpmConditionLink(linkId, "Condition " + linkId,
					sourceId, destinationId);
		case OpcatConstants.INSTRUMENT_EVENT_LINK:
			return new OpmInstrumentEventLink(linkId, "Event " + linkId,
					sourceId, destinationId);
		case OpcatConstants.RESULT_LINK:
			return new OpmResultLink(linkId, "Result " + linkId, sourceId,
					destinationId);
		case OpcatConstants.CONSUMPTION_LINK:
			return new OpmConsumptionLink(linkId, "Consumption " + linkId,
					sourceId, destinationId);
		case OpcatConstants.EFFECT_LINK:
			return new OpmEffectLink(linkId, "Effect " + linkId, sourceId,
					destinationId);
		case OpcatConstants.EXCEPTION_LINK:
			return new OpmExceptionLink(linkId, "Exception " + linkId,
					sourceId, destinationId);
		case OpcatConstants.INVOCATION_LINK:
			return new OpmInvocationLink(linkId, "Invocation " + linkId,
					sourceId, destinationId);
		case OpcatConstants.CONSUMPTION_EVENT_LINK:
			return new OpmConsumptionEventLink(linkId, "Instrument Event "
					+ linkId, sourceId, destinationId);

		default: {
			JOptionPane
					.showMessageDialog(
							Opcat2.getFrame(),
							" Serious internal bug occured in AddLink function \n Please contact software developers.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
			return null;
		}

		}

	}

	public void deleteEmptyEntries() {

		Iterator iter = Collections.list(getSystemStructure().getElements())
				.iterator();
		while (iter.hasNext()) {
			Entry ent = (Entry) iter.next();
			if (ent.isEmpty()) {
				OpcatLogger.logError("Empty Entry - " + ent.getName());
				mainComponentsStructure.removeEntry(ent.getId());
			}
		}

	}

	public UndoableDeleteLink deleteLink(LinkInstance delInstance) {
		return this._deleteLink(delInstance);
	}

	// public UndoableDeleteLink deleteLinkConsistent(LinkInstance delInstance)
	// {
	// // handle the simplest case nothing Zoomed - In.
	// // This case is checked separated from others cos it's fast to check
	// OpdConnectionEdge source = delInstance.getSource();
	// OpdConnectionEdge destination = delInstance.getDestination();
	// if (!LinkPrecedence.isEdgesZoomedIn(source, destination, this)
	// && !LinkPrecedence.hasDifferentContainers(source, destination,
	// this)) {
	// UndoableDeleteLink li = this._deleteLink(delInstance);
	// Opcat2.getUndoManager().undoableEditHappened(
	// new UndoableEditEvent(this, li));
	// Opcat2.setUndoEnabled(Opcat2.getUndoManager().canUndo());
	// Opcat2.setRedoEnabled(Opcat2.getUndoManager().canRedo());
	// return li;
	// }
	// // src will always hold In-Zoomed thing or one that has In-Zooming OPD
	// // e. g. problematic thing
	// switch (LinkPrecedence.detectLinkActionCase(source, destination, this)) {
	// case 1:
	// return this._deleteLinkFromParentOpd(delInstance);
	//
	// case 2:
	// return this._deleteLinkFromParentOpd(delInstance);
	//
	// case 3:
	// return this._deleteLinkFromChildOpd(delInstance);
	//
	// case 4:
	// return this._deleteLinkFromChildOpd(delInstance);
	//
	// case 5:
	// return this._deleteLinkFromChildOpd(delInstance);
	//
	// case 6:
	// return this._deleteLinkFromChildOpd(delInstance);
	//
	// default:
	// JOptionPane.showMessageDialog(Opcat2.getFrame(),
	// "Failed to delete link.", "Opcat II - Error",
	// JOptionPane.ERROR_MESSAGE);
	// }
	//
	// return null;
	// }

	private UndoableDeleteLink _deleteLink(LinkInstance delInstance) {
		UndoableDeleteLink udl = new UndoableDeleteLink(this, delInstance);

		Entry selectedEntry = delInstance.getEntry();

		ConnectionEdgeEntry sourceEntry, destinationEntry;
		OpmProceduralLink lLink = (OpmProceduralLink) selectedEntry
				.getLogicalEntity();

		long sourceId = delInstance.getSource().getEntity().getId();
		long destinationId = delInstance.getDestination().getEntity().getId();

		sourceEntry = (ConnectionEdgeEntry) (this.mainComponentsStructure
				.getEntry(sourceId));
		destinationEntry = (ConnectionEdgeEntry) (this.mainComponentsStructure
				.getEntry(destinationId));

		selectedEntry.removeInstance(delInstance.getKey());

		if (selectedEntry.isEmpty()) {
			if (sourceEntry != null) {
				sourceEntry.removeLinkSource(lLink);
			}
			if (destinationEntry != null) {
				destinationEntry.removeLinkDestination(lLink);
			}
			boolean retval = this.mainComponentsStructure
					.removeEntry(selectedEntry.getId());
			if (retval == false) {
				OpcatLogger.logError("can not remove entry "
						+ selectedEntry.getName());
			}
		}

		if (!duringLoad) {
			getExposeManager().treatExpsoeChange(delInstance);
		}

		return udl;
	}

	public CheckResult switchLinkEdge(LinkInstance instance,
			OpdConnectionEdge oldEdge, OpdConnectionEdge newEdge,
			RelativeConnectionPoint newPoint) {
		OpdConnectionEdge source;
		OpdConnectionEdge destination;
		RelativeConnectionPoint sourcePoint;
		RelativeConnectionPoint destinationPoint;
		boolean switchSource = false;

		if (instance.getDestination() == oldEdge) {
			source = instance.getSource();
			sourcePoint = new RelativeConnectionPoint(instance
					.getSourceDragger().getSide(), instance.getSourceDragger()
					.getParam());
			destination = newEdge;
			destinationPoint = new RelativeConnectionPoint(newPoint.getSide(),
					newPoint.getParam());
		} else {
			source = newEdge;
			sourcePoint = new RelativeConnectionPoint(newPoint.getSide(),
					newPoint.getParam());
			destination = instance.getDestination();
			destinationPoint = new RelativeConnectionPoint(instance
					.getDestinationDragger().getSide(), instance
					.getDestinationDragger().getParam());
			switchSource = true;
		}

		OpdKey sourceKey = new OpdKey(source.getOpdId(), source
				.getEntityInOpdId());
		OpdKey targetKey = new OpdKey(destination.getOpdId(), destination
				.getEntityInOpdId());

		CommandWrapper cw = new CommandWrapper(source.getEntity().getId(),
				sourceKey, destination.getEntity().getId(), targetKey,
				Constants.getType4Link(instance.getLink()), this);

		CheckResult cr = CheckModule.checkCommand(cw);

		if (cr.getResult() == IXCheckResult.WRONG) {
			return cr;
		}

		if ((cr != null) && (cr.getResult() == IXCheckResult.WARNING)) {
			int retValue = JOptionPane.showConfirmDialog(Opcat2.getFrame(), cr
					.getMessage()
					+ "\n Do you want to continue?", "Opcat2 - Warning",
					JOptionPane.YES_NO_OPTION);

			if (retValue == JOptionPane.NO_OPTION) {
				return cr;

			}
		}

		CompoundUndoAction undoAction = new CompoundUndoAction();

		Container cn;
		Container destinationContainer = destination.getParent();
		Container sourceContainer = source.getParent();

		if (destinationContainer == sourceContainer) {
			cn = sourceContainer;
		} else if (switchSource && (source instanceof OpdState)) {
			cn = ((StateInstance) ((OpdState) source).getInstance())
					.getParent().getParent();
		} else if (!switchSource && (destination instanceof OpdState)) {
			cn = ((StateInstance) ((OpdState) destination).getInstance())
					.getParent().getParent();
		} else {
			cn = this.getCurrentOpd().getDrawingArea();
		}

		LinkInstance newInstance;

		newInstance = this.addLink(source, sourcePoint, destination,
				destinationPoint, cn, Constants
						.getType4Link(instance.getLink()), -1, -1);

		String myName = newInstance.getEntry().getName();
		newInstance.getLink().copyPropsFrom(instance.getLink());
		newInstance.copyPropsFrom(instance);
		newInstance.getEntry().setName(myName);
		// newInstance.getEntry().setName(name) ;
		newInstance.updateOrConnections();
		newInstance.update();

		undoAction.addAction(new UndoableAddLink(this, newInstance));
		undoAction.addAction(new UndoableDeleteLink(this, instance));

		Opcat2.getUndoManager().undoableEditHappened(
				new UndoableEditEvent(this, undoAction));
		Opcat2.setUndoEnabled(Opcat2.getUndoManager().canUndo());
		Opcat2.setRedoEnabled(Opcat2.getUndoManager().canRedo());

		this.deleteLink(instance);
		// // now decide if to switch the linkEntry destinatin and source.
		// if (this.getSystemStructure().getEntry(
		// ((OpmProceduralLink) instance.getEntry().getLogicalEntity())
		// .getSourceId()) == null) {
		// // chenge source to source in the same OPD
		// }
		//
		// if (this.getSystemStructure().getEntry(
		// ((OpmProceduralLink) instance.getEntry().getLogicalEntity())
		// .getDestinationId()) == null) {
		// // chenge destination to destination in the same OPD
		// }
		// //

		this.removeSelection();
		this.addSelection(newInstance, true);
		Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
		return cr;
	}

	public CheckResult switchGeneralRelationEdge(
			GeneralRelationInstance instance, OpdConnectionEdge oldEdge,
			OpdConnectionEdge newEdge, RelativeConnectionPoint newPoint) {
		OpdConnectionEdge source;
		OpdConnectionEdge destination;
		RelativeConnectionPoint sourcePoint;
		RelativeConnectionPoint destinationPoint;

		if (instance.getDestination() == oldEdge) {
			source = instance.getSource();
			sourcePoint = new RelativeConnectionPoint(instance
					.getSourceDragger().getSide(), instance.getSourceDragger()
					.getParam());
			destination = newEdge;
			destinationPoint = new RelativeConnectionPoint(newPoint.getSide(),
					newPoint.getParam());
		} else {
			source = newEdge;
			sourcePoint = new RelativeConnectionPoint(newPoint.getSide(),
					newPoint.getParam());
			destination = instance.getDestination();
			destinationPoint = new RelativeConnectionPoint(instance
					.getDestinationDragger().getSide(), instance
					.getDestinationDragger().getParam());
		}

		OpdKey sourceKey = new OpdKey(source.getOpdId(), source
				.getEntityInOpdId());
		OpdKey targetKey = new OpdKey(destination.getOpdId(), destination
				.getEntityInOpdId());

		CommandWrapper cw = new CommandWrapper(source.getEntity().getId(),
				sourceKey, destination.getEntity().getId(), targetKey,
				Constants.getType4Relation(instance.getGeneralRelation()), this);

		CheckResult cr = CheckModule.checkCommand(cw);

		if (cr.getResult() == IXCheckResult.WRONG) {
			return cr;
		}

		if ((cr != null) && (cr.getResult() == IXCheckResult.WARNING)) {
			int retValue = JOptionPane.showConfirmDialog(Opcat2.getFrame(), cr
					.getMessage()
					+ "\n Do you want to continue?", "Opcat2 - Warning",
					JOptionPane.YES_NO_OPTION);

			if (retValue == JOptionPane.NO_OPTION) {
				return cr;

			}
		}

		CompoundUndoAction undoAction = new CompoundUndoAction();

		Container cn;
		if (source.getParent() == destination.getParent()) {
			cn = source.getParent();
		} else {
			cn = this.getCurrentOpd().getDrawingArea();
		}

		GeneralRelationInstance newInstance = this.addGeneralRelation(source,
				sourcePoint, destination, destinationPoint, cn, Constants
						.getType4Relation(instance.getGeneralRelation()), -1,
				-1);

		// instance.getEntry().getId()

		newInstance.getGeneralRelation().copyPropsFrom(
				instance.getGeneralRelation());
		newInstance.copyPropsFrom(instance);
		newInstance.update();

		undoAction.addAction(new UndoableAddGeneralRelation(this, newInstance));
		undoAction.addAction(new UndoableDeleteGeneralRelation(this, instance));

		Opcat2.getUndoManager().undoableEditHappened(
				new UndoableEditEvent(this, undoAction));
		Opcat2.setUndoEnabled(Opcat2.getUndoManager().canUndo());
		Opcat2.setRedoEnabled(Opcat2.getUndoManager().canRedo());

		this.deleteGeneralRelation(instance);
		this.removeSelection();
		this.addSelection(newInstance, true);
		Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
		return cr;
	}

	public UndoableDeleteFundamentalRelation deleteFundamentalRelation(
			FundamentalRelationInstance delInstance) {
		UndoableDeleteFundamentalRelation udf = new UndoableDeleteFundamentalRelation(
				this, delInstance);

		GraphicalRelationRepresentation gRelR = delInstance
				.getGraphicalRelationRepresentation();

		Enumeration<FundamentalRelationInstance> instances = gRelR
				.getInstances();

		while (instances.hasMoreElements()) {
			FundamentalRelationInstance ins = instances.nextElement();
			if (ins.getSourceInstance().getKey().toString().equalsIgnoreCase(
					delInstance.getSourceInstance().getKey().toString())
					&& ins.getDestinationInstance().getKey().toString()
							.equalsIgnoreCase(
									delInstance.getDestinationInstance()
											.getKey().toString())) {

				ConnectionEdgeEntry sourceEntry, destinationEntry;

				Entry selectedEntry = ins.getEntry();

				OpmFundamentalRelation lRel = (OpmFundamentalRelation) selectedEntry
						.getLogicalEntity();

				long sourceId = ins.getSourceInstance().getEntry().getId(); // gRelR.getSource().getEntity().getId();
				long destinationId = ins.getDestination().getEntity().getId();

				sourceEntry = (ConnectionEdgeEntry) (this.mainComponentsStructure
						.getEntry(sourceId));
				destinationEntry = (ConnectionEdgeEntry) (this.mainComponentsStructure
						.getEntry(destinationId));

				selectedEntry.removeInstance(ins.getKey());

				if (selectedEntry.isEmpty()) {
					if (sourceEntry != null)
						sourceEntry.removeRelationSource(lRel);
					if (destinationEntry != null)
						destinationEntry.removeRelationDestination(lRel);
					this.mainComponentsStructure.removeEntry(selectedEntry
							.getId());
				}
				gRelR.removeInstance(ins.getKey());

				if (gRelR.isEmpty()) {
					OpdKey sourceKey = new OpdKey(gRelR.getSource().getOpdId(),
							gRelR.getSource().getEntityInOpdId());
					GraphicRepresentationKey key = new GraphicRepresentationKey(
							sourceKey, gRelR.getType());
					this.mainComponentsStructure
							.removeGraphicalRelationRepresentation(key);
				}
				// else {
				// gRelR.setVisible(false);
				// }

			}
		}
		/**
		 * check what links to hide
		 */

		ThingInstance mainInstance = delInstance.getOpd().getMainInstance();
		if (mainInstance != null) {
			HashMap<ThingInstance, FundamentalRelationInstance> aggs = mainInstance
					.getFundemantulRelationInstancesSons(OpcatConstants.AGGREGATION_RELATION);
			Iterator<ThingInstance> sons = aggs.keySet().iterator();
			while (sons.hasNext()) {
				ThingInstance ins = sons.next();
				FundamentalRelationInstance rel = aggs.get(ins);
				if (mainInstance.isIncluding(ins)) {
					rel.setVisible(false);
				}
			}
			aggs = mainInstance
					.getFundemantulRelationInstancesSons(OpcatConstants.EXHIBITION_RELATION);
			sons = aggs.keySet().iterator();
			while (sons.hasNext()) {
				ThingInstance ins = sons.next();
				FundamentalRelationInstance rel = aggs.get(ins);
				if (mainInstance.isIncluding(ins)) {
					rel.setVisible(false);
				}
			}
		}

		if (!duringLoad) {
			getExposeManager().treatExpsoeChange(delInstance);
		}
		return udf;
	}

	public UndoableDeleteGeneralRelation deleteGeneralRelation(
			GeneralRelationInstance delInstance) {
		UndoableDeleteGeneralRelation dgr = new UndoableDeleteGeneralRelation(
				this, delInstance);
		Entry selectedEntry = delInstance.getEntry();

		ConnectionEdgeEntry sourceEntry, destinationEntry;
		OpmGeneralRelation lRelation = (OpmGeneralRelation) selectedEntry
				.getLogicalEntity();

		long sourceId = delInstance.getSource().getEntity().getId();
		long destinationId = delInstance.getDestination().getEntity().getId();

		sourceEntry = (ConnectionEdgeEntry) (this.mainComponentsStructure
				.getEntry(sourceId));
		destinationEntry = (ConnectionEdgeEntry) (this.mainComponentsStructure
				.getEntry(destinationId));

		selectedEntry.removeInstance(delInstance.getKey());

		if (selectedEntry.isEmpty()) {
			sourceEntry.removeGeneralRelationSource(lRelation);
			destinationEntry.removeGeneralRelationDestination(lRelation);
			this.mainComponentsStructure.removeEntry(selectedEntry.getId());
		}
		return dgr;
	}

	public IXStateEntry addState(String name, IXObjectInstance parentObject) {
		long opdId = parentObject.getKey().getOpdId();

		if ((this.currentOpd == null) || (this.currentOpd.getOpdId() != opdId)) {
			this.showOPD(opdId);
		}

		ObjectEntry parentEntry = (ObjectEntry) this.mainComponentsStructure
				.getEntry(parentObject.getLogicalId());
		ObjectInstance parentInstance = (ObjectInstance) parentEntry
				.getInstance(parentObject.getKey());

		IXStateEntry newEntry = this.addState((OpdObject) parentInstance
				.getThing());
		newEntry.setName(name);
		newEntry.updateInstances();

		for (Enumeration e = parentObject.getIXEntry().getInstances(); e
				.hasMoreElements();) {
			ObjectInstance tempObject = (ObjectInstance) e.nextElement();
			if (tempObject.isStatesAutoArranged()) {
				tempObject.getConnectionEdge().fitToContent();
			}
		}

		return newEntry;
	}

	public UndoableDeleteState deleteState(StateInstance delInstance) {
		StateEntry myEntry = (StateEntry) delInstance.getEntry();

		if (myEntry == null) {
			return null;
		}

		UndoableDeleteState ud = new UndoableDeleteState(this, myEntry);

		ObjectEntry parentEntry = myEntry.getParentObject();

		for (Enumeration e = myEntry.getInstances(); e.hasMoreElements();) {
			StateInstance si = (StateInstance) e.nextElement();
			myEntry.removeInstance(si.getKey());
		}

		parentEntry.removeState(myEntry.getId());
		this.mainComponentsStructure.removeEntry(myEntry.getId());

		return ud;
	}

	/**
	 * Returns maximal Entity Entry (for save purposes)
	 */
	public long getEntityMaxEntry() {
		return this.entityMaxEntry;
	}

	/**
	 * Sets maximal Entity Entry (for load purposes)
	 */

	public void setEntityMaxEntry(long pEntityMaxEntry) {
		this.entityMaxEntry = pEntityMaxEntry;
	}

	/**
	 * Returns maximal OPD Entry (for save purposes)
	 */

	public long getOpdMaxEntry() {
		return this.opdMaxEntry;
	}

	/**
	 * Returns maximal OPD Entry (for save purposes)
	 */

	public void setOpdMaxEntry(long pOpdMaxEntry) {
		this.opdMaxEntry = pOpdMaxEntry;
	}

	/**
	 * Returns JDesktopPane which is parent (contains) for this project
	 */
	public JDesktopPane getParentDesktop() {
		return this.parentDesktop;
	}

	public void copy(Opd from) {
		this.clearClipBoard();
		String message = this._copy(from, this.clipBoard, 0, 0, this.clipBoard
				.getDrawingArea(), false);
		if (message != null) {
			JOptionPane.showMessageDialog(Opcat2.getFrame(), message,
					"Opcat2 - Message", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void cloneSelected() {
		int x = 0;
		int y = 0;

		// copy(true);
		this._copy(this.currentOpd, this.currentOpd, x, y, this.currentOpd
				.getDrawingArea(), false);

	}

	public void copy() {

		// clipBoard.selectAll();
		// _delete(clipBoard, DELETE);
		this.clearClipBoard();
		String message = this._copy(this.getCurrentOpd(), this.clipBoard, 0, 0,
				this.clipBoard.getDrawingArea(), false);
		if (message != null) {
			JOptionPane.showMessageDialog(Opcat2.getFrame(), message,
					"Opcat2 - Message", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void paste(int x, int y, Container parent) {

		Opd current = this.getCurrentOpd();

		// can't undo rerooting of an OPD
		Opcat2.getUndoManager().discardAllEdits();
		Opcat2.setRedoEnabled(false);
		Opcat2.setUndoEnabled(false);
		setCanClose(false);

		EditControl edit = EditControl.getInstance();

		this.clipBoard.selectAll();
		try {
			this._copy(this.clipBoard, current, x, y, parent, true);
		} catch (Exception e) {
			OpcatLogger.logError(e);
		}
		if (edit.IsCutPending()) {
			// reroot the zoomed in opds if it's a paste-cut
			this.clipBoard.selectAll();

			Iterator iter = Collections.list(clipBoard.getSelectedItems())
					.iterator();
			while (iter.hasNext()) {
				Instance ins = (Instance) iter.next();

				if (ins instanceof ObjectInstance) {
					ObjectEntry object = (ObjectEntry) ins.getEntry();
					if (object.getZoomedInIOpd() != null) {
						object.getZoomedInOpd().setParentOpd(current);
					}
					if (((ObjectInstance) ins).getUnfoldingOpd() != null) {
						((ObjectInstance) ins).getUnfoldingOpd().setParentOpd(
								current);
					}

				}
				if (ins instanceof ProcessInstance) {
					ProcessEntry object = (ProcessEntry) ins.getEntry();
					if (object.getZoomedInIOpd() != null) {
						object.getZoomedInOpd().setParentOpd(current);
					}
				}
			}

		}
		setNames4OPDsAfterCut();
		this.setCurrentOpd(current);
		// if(parent instanceof OpdThing) {

		// } else {
		this.getCurrentOpd().refit(parent);
		// }
	}

	public AbstractUndoableEdit delete(IXInstance delInstance) {
		if (this.checkDeletion(delInstance).getResult() == IXCheckResult.WRONG) {
			return null;
		}
		return this.deleteInstance((Instance) delInstance);
	}

	public IXCheckResult checkDeletion(IXInstance checkedInstance) {
		return CheckModule.checkDeletion((Instance) checkedInstance, this);
	}

	public IXCheckResult checkLinkAddition(IXConnectionEdgeInstance source,
			IXConnectionEdgeInstance destination, int linkType) {
		CommandWrapper cw = new CommandWrapper(source.getLogicalId(), source
				.getKey(), destination.getLogicalId(), destination.getKey(),
				linkType, this);

		return CheckModule.checkCommand(cw);

	}

	public IXCheckResult checkRelationAddition(IXConnectionEdgeInstance source,
			IXConnectionEdgeInstance destination, int relationType) {
		CommandWrapper cw = new CommandWrapper(source.getLogicalId(), source
				.getKey(), destination.getLogicalId(), destination.getKey(),
				relationType, this);

		return CheckModule.checkCommand(cw);

	}

	public void delete() {
		GuiControl gui = GuiControl.getInstance();
		String s = "";
		boolean ask = false;
		Vector<Instance> vec = new Vector<Instance>();

		/*
		 * first find if there are any more to delete.
		 */
		Enumeration deleteFromEnum = this.getCurrentOpd().getSelectedItems();
		if (!deleteFromEnum.hasMoreElements()) {
			return;
		}

		int selectedCount = 0;
		while (deleteFromEnum.hasMoreElements()) {
			Instance ins = (Instance) deleteFromEnum.nextElement();
			selectedCount++;
			Enumeration deleteFromInstancesEnum = ins.getEntry().getInstances();
			while (deleteFromInstancesEnum.hasMoreElements()) {
				Instance intIns = (Instance) deleteFromInstancesEnum
						.nextElement();
				vec.add(intIns);
			}
		}

		for (Instance delInstance : vec) {
			boolean skip;
			if (delInstance instanceof ThingInstance) {
				skip = (((ThingEntry) ((ThingInstance) delInstance).getEntry())
						.getZoomedInOpd() != null);
				skip = skip
						|| (((ThingEntry) ((ThingInstance) delInstance)
								.getEntry()).getUnfoldingOpd() != null);

				if (skip)
					continue;

				Component[] components = delInstance
						.getGraphicalRepresentation().getComponents();
				for (int i = 0; i < components.length; i++) {
					if ((delInstance instanceof ThingInstance)
							&& (components[i] instanceof OpdThing)
							&& !(((OpdThing) components[i]).getInstance()
									.getKey().toString()
									.equalsIgnoreCase(delInstance.getKey()
											.toString()))) {
						JOptionPane
								.showMessageDialog(
										Opcat2.getFrame(),
										"Instance ("
												+ delInstance.getEntry()
														.getName().replaceAll(
																"\n", " ")
												+ ") containes things.\nFirst delete the contained instances",
										"Opcat - Message",
										JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			}
		}
		ask = (vec.size() > selectedCount);
		if (ask) {
			Object[] possibilities = { "Selected", "All", "Step by Step",
					"Consistency Checker" };
			s = (String) JOptionPane.showInputDialog(gui.getFrame(),
					"Delete - ", "Opcat II", JOptionPane.QUESTION_MESSAGE,
					null, possibilities, "Selected");
		} else {
			// only one instance to delete ;
			s = "All";
		}

		// If a string was returned, say so.
		String message = null;

		if ((s != null) && (s.length() > 0)) {
			if (s.equalsIgnoreCase("Selected")) {
				message = this._delete(this.getCurrentOpd(), DELETE);
			}
			if (s.equalsIgnoreCase("All")) {
				message = this._deleteALL(this.getCurrentOpd(), DELETE, false);
			}
			if (s.equalsIgnoreCase("Step by Step")) {
				message = this._deleteALL(this.getCurrentOpd(), DELETE, true);
			}
			if (s.equalsIgnoreCase("Consistency Checker")) {
				ConsistencyResult checkResult = new ConsistencyResult();
				HashMap instances = new HashMap();

				for (Iterator iter = currentOpd.getSelection()
						.getSelectedItemsHash().values().iterator(); iter
						.hasNext();) {
					Instance ins = (Instance) iter.next();
					instances.put(ins, ins);
				}
				ConsistencyAbstractChecker checker = (ConsistencyAbstractChecker) (new ConsistencyFactory(
						instances, this, ConsistencyAction.DELETION,
						checkResult)).create();
				checker.check();
				if (checkResult.isConsistent()) {
					message = this._delete(this.getCurrentOpd(), DELETE);
				} else {
					checker.deploy(checkResult);
				}
			}

			if (message != null) {
				JOptionPane.showMessageDialog(Opcat2.getFrame(), message,
						"Opcat2 - Message", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void refresh() {
		if (this.currentOpd != null) {
			this.currentOpd.getDrawingArea().repaint();
		}
	}

	public PrinterJob getPrinterJob() {
		if (this.printJob == null) {
			this.printJob = PrinterJob.getPrinterJob();
		}
		return this.printJob;
	}

	public void pageSetup() {
		// JOptionPane.showMessageDialog(Opcat2.getFrame(), "This is page
		// Setup", "Message" ,JOptionPane.ERROR_MESSAGE);
		new PageSetupDialog(Opcat2.getFrame(), "Page Setup", this.pageSetupData);
	}

	public void setNames4OPDs() {
		setNames4OPDs(false);
	}

	public void setNames4OPDs(boolean regnerateOPDNumbers) {
		// String currentLevel = "";
		Hashtable<Long, Opd> currentLevel = new Hashtable<Long, Opd>();
		Hashtable<Long, Opd> previousLevel = new Hashtable<Long, Opd>();
		Hashtable<Long, Opd> notScanned = new Hashtable<Long, Opd>();
		Opd currOPD;

		for (Enumeration e = this.mainComponentsStructure.getOpds(); e
				.hasMoreElements();) {
			currOPD = (Opd) e.nextElement();
			notScanned.put(new Long(currOPD.getOpdId()), currOPD);
		}
		currOPD = mainComponentsStructure.getOpd(1);
		currOPD.setName("SD");
		notScanned.remove(new Long(currOPD.getOpdId()));

		this._setName4ChildOPD(currOPD, notScanned, regnerateOPDNumbers);

		previousLevel.put(new Long(currOPD.getOpdId()), currOPD);

		while (!notScanned.isEmpty()) {
			for (Enumeration e = notScanned.elements(); e.hasMoreElements();) {
				currOPD = (Opd) e.nextElement();
				if (previousLevel.containsKey(new Long(currOPD.getParentOpd()
						.getOpdId()))) {
					// System.err.println(currOPD.getOpdId());
					currentLevel.put(new Long(currOPD.getOpdId()), currOPD);
					notScanned.remove(new Long(currOPD.getOpdId()));
					this._setName4ChildOPD(currOPD, notScanned,
							regnerateOPDNumbers);
				}
			}

			previousLevel = new Hashtable<Long, Opd>(currentLevel);
			currentLevel.clear();
		}
	}

	public void setNames4OPDsAfterCut() {
		// String currentLevel = "";
		Hashtable<Long, Opd> currentLevel = new Hashtable<Long, Opd>();
		Hashtable<Long, Opd> previousLevel = new Hashtable<Long, Opd>();
		Hashtable<Long, Opd> notScanned = new Hashtable<Long, Opd>();

		Opd currOPD;

		for (Enumeration e = this.mainComponentsStructure.getAllOpds(); e
				.hasMoreElements();) {
			currOPD = (Opd) e.nextElement();
			notScanned.put(new Long(currOPD.getOpdId()), currOPD);
		}

		currOPD = this.mainComponentsStructure.getOpd(1);
		currOPD.setName("SD");
		notScanned.remove(new Long(currOPD.getOpdId()));

		this._setName4ChildOPD(currOPD, notScanned, false);

		previousLevel.put(new Long(currOPD.getOpdId()), currOPD);

		while (!notScanned.isEmpty()) {
			for (Enumeration e = notScanned.elements(); e.hasMoreElements();) {
				currOPD = (Opd) e.nextElement();
				if ((previousLevel.containsKey(new Long(currOPD.getParentOpd()
						.getOpdId())))) {
					// System.err.println(currOPD.getOpdId());
					currentLevel.put(new Long(currOPD.getOpdId()), currOPD);
					notScanned.remove(new Long(currOPD.getOpdId()));
					this._setName4ChildOPD(currOPD, notScanned, false);
				}
			}

			previousLevel = new Hashtable<Long, Opd>(currentLevel);
			currentLevel.clear();
		}
	}

	private void _setName4ChildOPD(Opd myOpd, Hashtable notScanned,
			boolean regnerateOPDNumbers) {

		TreeMap currentLevel = new TreeMap();
		String header;
		// unfold only appear in level 1
		String uheader = Opd.getViewInitials();
		boolean unfold = false;
		if (myOpd.getOpdId() == 1) {
			header = "SD";
		} else {
			StringTokenizer st = new StringTokenizer(myOpd.getName(), " ");
			header = st.nextToken() + ".";
		}

		for (Enumeration e = notScanned.elements(); e.hasMoreElements();) {
			Opd currOPD = (Opd) e.nextElement();

			if ((currOPD.getParentOpd() != null)
					&& (myOpd.getOpdId() == currOPD.getParentOpd().getOpdId())) {
				currentLevel.put(currOPD.getMainEntry().getLogicalEntity()
						.getName()
						+ " " + currOPD.getOpdId(), currOPD);
			}
		}

		int ucounter = 0;
		// count unfolding opd's
		for (Iterator i = currentLevel.values().iterator(); i.hasNext();) {
			Opd currOPD = (Opd) i.next();
			// if it's not an INZOOMED opd it's a UD or the root SD
			if (!((currOPD.getMainEntry().getZoomedInOpd() != null) && (currOPD
					.getMainEntry().getZoomedInOpd().getOpdId() == currOPD
					.getOpdId()))) {

				ucounter++;
			}

		}
		int counter = currentLevel.values().size() - ucounter;

		// int counter = 1;

		for (Iterator i = currentLevel.values().iterator(); i.hasNext();) {
			Opd currOPD = (Opd) i.next();
			String footer = currOPD.getMainEntry().getLogicalEntity().getName()
					.replace('\n', ' ');

			// if (currOPD.isView()) {
			// if (!name.equalsIgnoreCase("View OPD")) {
			// footer = name;
			// }
			// if ((currOPD.getMainInstance().getUnfoldingOpd() != null)) {
			//
			// }
			// unfold = true;
			// } else {
			// if ((currOPD.getMainEntry().getZoomedInOpd() != null)) {
			// footer += " in-zoomed";
			// }
			// unfold = false;
			// }

			if ((currOPD.getMainEntry().getZoomedInOpd() != null)
					&& (currOPD.getMainEntry().getZoomedInOpd().getOpdId() == currOPD
							.getOpdId())) {
				footer += " in-zoomed";
				unfold = false;
			} else if ((currOPD.getMainInstance().getUnfoldingOpd() != null)
					&& (currOPD.getMainInstance().getUnfoldingOpd().getOpdId() == currOPD
							.getOpdId())) {
				footer += " unfolded";
				unfold = true;
			} else {
				footer += " ";
				unfold = true;
			}
			if (regnerateOPDNumbers
					|| currOPD.getTreeLevel().equalsIgnoreCase("")) {
				if (!unfold) {
					currOPD.setName(header + counter + " - " + footer);
					currOPD.setTreeLevel(header + counter + " - " + footer);
					counter--;
				} else {
					String name = currOPD.getName().substring(
							currOPD.getName().indexOf("-") + 2);
					if (!name.equalsIgnoreCase(" View OPD")) {
						footer = name;
					}
					currOPD.setName(uheader + ucounter + " - " + footer);
					currOPD.setTreeLevel(uheader + ucounter + " - " + footer);
					ucounter--;
				}
			}

		}

	}

	public String cut(Opd cuttingFrom, int action) {

		String message = null;

		this.clearClipBoard();

		boolean isUndoable = false;
		if (action == OpdProject.PASTE) {
			isUndoable = true;
		}
		this._copy(this.getCurrentOpd(), this.clipBoard, 0, 0, this.clipBoard
				.getDrawingArea(), isUndoable);

		message = this._delete(cuttingFrom, action);
		return message;

	}

	public void selectAll() {
		this.getCurrentOpd().selectAll();
	}

	/**
	 * there are 2 methods generally: 1. _copy(Opd copyFrom, opd copyTo) that
	 * copies selection from one opd to another 2. _delete(Opd deleteFrom) that
	 * deletes selection from given Opd
	 * 
	 * all copy/cut/paste/delete are combinations of these 2 methods
	 */

	// /////////////////////////
	// IMPORTENT: handle any2state and state2any links and relations --
	// DONE!!
	// ///////////////////////////
	public String _copy(Opd copyFrom, Opd copyTo, int x, int y,
			Container parent, boolean isUndoable) {

		GridPanel.updateColor(null);

		try {

			Hashtable t = new Hashtable(); // things
			Hashtable l = new Hashtable(); // links
			Hashtable fr = new Hashtable(); // fundamental relations
			Hashtable gr = new Hashtable(); // general relations
			Hashtable selectedItems = copyFrom.getSelectedItemsHash();
			// all selected Instances

			boolean wasNewOpd = false;

			Instance inst = null;
			Enumeration e;
			ThingInstance ti = null;

			// undo only when action == PASTE
			CompoundUndoAction undoAction = new CompoundUndoAction();

			Opd oldOpd = this.currentOpd;

			this.currentOpd = copyFrom;

			// sort all selected Instances according to the type
			for (e = selectedItems.elements(); e.hasMoreElements();) {
				inst = (Instance) e.nextElement();
				if (inst instanceof ThingInstance) {
					t.put(new Long(inst.getKey().getEntityInOpdId()), inst);
				} else if (inst instanceof LinkInstance) {
					l.put(new Long(inst.getKey().getEntityInOpdId()), inst);
				} else if (inst instanceof FundamentalRelationInstance) {
					fr.put(new Long(inst.getKey().getEntityInOpdId()), inst);
				} else if (inst instanceof GeneralRelationInstance) {
					gr.put(new Long(inst.getKey().getEntityInOpdId()), inst);
				}
			}

			if (t.size() == 0) {
				// no things where selected so nothing to do
				return null;
			}

			// now calculate the most topleft point
			// we calculate it from _graphical_ selection so if
			// one of DraggedPoints or OpdFundamentalRelations outstends the
			// OpdThings
			// bounds it would taken in count
			Component[] bgcs = copyFrom.getSelection()
					.graphicalSelectionComponents();
			int topleftX = Integer.MAX_VALUE;
			int topleftY = Integer.MAX_VALUE;

			for (int i = 0; i < bgcs.length; i++) {
				if (bgcs[i].getX() < topleftX) {
					topleftX = bgcs[i].getX();
				}
				if (bgcs[i].getY() < topleftY) {
					topleftY = bgcs[i].getY();
				}
			}

			int theX, theY; // really x,y to paste
			this.currentOpd = copyTo;
			// ////////////////////////////////////////////////////////
			// this hash holds oldInstance/newInstance pairs
			// so we can add all links/relations properly
			Hashtable orig2new = new Hashtable();
			// ////////////////////////////////////////////////////////

			for (Enumeration e1 = t.elements(); e1.hasMoreElements();) {
				ti = (ThingInstance) e1.nextElement();

				// calc real location
				theX = x + ti.getX() - topleftX;
				theY = y + ti.getY() - topleftY;

				if (ti instanceof ProcessInstance) {
					ProcessInstance pi;
					pi = this.addProcess(theX, theY, parent, ti.getLogicalId(),
							-1);
					pi.setLocation(theX, theY);
					pi.getThing().fitToContent();
					pi.copyPropsFrom((ProcessInstance) ti);
					pi.update();

					if (isUndoable == true) {
						undoAction.addAction(new UndoableAddProcess(this, pi));
					}

					orig2new.put(ti, pi);
				}

				if (ti instanceof ObjectInstance) {
					ObjectInstance oi;
					oi = this.addObject(theX, theY, parent, ti.getLogicalId(),
							-1, true);
					oi.setLocation(theX, theY);
					oi.getThing().fitToContent();
					oi.copyPropsFrom((ObjectInstance) ti);
					// oi.setUnfoldingOpd(ti.getUnfoldingOpd());
					oi.update();

					if (isUndoable == true) {
						undoAction.addAction(new UndoableAddObject(this, oi));
					}
					orig2new.put(ti, oi);

					ObjectInstance oldOi = (ObjectInstance) ti;
					StateInstance newSi, oldSi;
					for (Enumeration locEnum = oldOi.getStateInstances(); locEnum
							.hasMoreElements();) {
						oldSi = (StateInstance) locEnum.nextElement();
						for (Enumeration enum1 = oi.getStateInstances(); enum1
								.hasMoreElements();) {
							newSi = (StateInstance) enum1.nextElement();
							if (oldSi.getLogicalId() == newSi.getLogicalId()) {
								orig2new.put(oldSi, newSi);
							}
						}
					}
				}
			}

			ConnectionEdgeInstance origSrc, origDst, newSrc, newDst;
			// it's time to paste links
			LinkInstance origLi, newLi;
			for (Enumeration e2 = l.elements(); e2.hasMoreElements();) {
				origLi = (LinkInstance) e2.nextElement();
				origSrc = origLi.getSourceInstance();
				origDst = origLi.getDestinationInstance();
				newSrc = (ConnectionEdgeInstance) orig2new.get(origSrc);
				newDst = (ConnectionEdgeInstance) orig2new.get(origDst);
				if ((newSrc != null) && (newDst != null)) {
					newLi = this.addLink(newSrc.getConnectionEdge(), origLi
							.getSourceDragger().getRelativeConnectionPoint(),
							newDst.getConnectionEdge(), origLi
									.getDestinationDragger()
									.getRelativeConnectionPoint(), newSrc
									.getConnectionEdge().getParent(), Constants
									.getType4Link((OpmProceduralLink) origLi
											.getEntry().getLogicalEntity()),
							origLi.getEntry().getId(), -1);
					newLi.copyPropsFrom(origLi);
					newLi.update();
					if (isUndoable == true) {
						undoAction.addAction(new UndoableAddLink(this, newLi));
					}
				}

			}

			// paste fundamental relations
			FundamentalRelationInstance origFri, newFri;
			for (Enumeration e2 = fr.elements(); e2.hasMoreElements();) {
				origFri = (FundamentalRelationInstance) e2.nextElement();
				origSrc = (ConnectionEdgeInstance) origFri
						.getGraphicalRelationRepresentation()
						.getSourceInstance();
				origDst = (ConnectionEdgeInstance) origFri
						.getDestinationInstance();
				newSrc = (ConnectionEdgeInstance) orig2new.get(origSrc);
				newDst = (ConnectionEdgeInstance) orig2new.get(origDst);
				if ((newSrc != null) && (newDst != null)) {
					newFri = this
							.addRelation(
									newSrc.getConnectionEdge(),
									origFri.getDestinationDragger()
											.getRelativeConnectionPoint(),
									newDst.getConnectionEdge(),
									origFri.getDestinationDragger()
											.getRelativeConnectionPoint(),
									Constants
											.getType4Relation((OpmStructuralRelation) origFri
													.getEntry()
													.getLogicalEntity()),
									newSrc.getConnectionEdge().getParent(),
									origFri.getEntry().getId(), -1);
					// newFri.copyPropsFrom(origFri);
					// newFri.update();
					if (isUndoable == true) {
						undoAction
								.addAction(new UndoableAddFundamentalRelation(
										this, newFri));
					}
				}

			}

			// paste general relations
			GeneralRelationInstance origGri, newGri;
			for (Enumeration e2 = gr.elements(); e2.hasMoreElements();) {
				origGri = (GeneralRelationInstance) e2.nextElement();
				origSrc = (ConnectionEdgeInstance) origGri.getSourceInstance();
				origDst = (ConnectionEdgeInstance) origGri
						.getDestinationInstance();
				newSrc = (ConnectionEdgeInstance) orig2new.get(origSrc);
				newDst = (ConnectionEdgeInstance) orig2new.get(origDst);
				if ((newSrc != null) && (newDst != null)) {
					newGri = this
							.addGeneralRelation(
									newSrc.getConnectionEdge(),
									origGri.getDestinationDragger()
											.getRelativeConnectionPoint(),
									newDst.getConnectionEdge(),
									origGri.getDestinationDragger()
											.getRelativeConnectionPoint(),
									newSrc.getConnectionEdge().getParent(),
									Constants
											.getType4Relation((OpmStructuralRelation) origGri
													.getEntry()
													.getLogicalEntity()),
									origGri.getEntry().getId(), -1);
					// newGri.copyPropsFrom(origFri);
					newGri.update();
					if (isUndoable == true) {
						undoAction.addAction(new UndoableAddGeneralRelation(
								this, newGri));
					}
				}
			}

			this.currentOpd = oldOpd;

			if (wasNewOpd) {
				Opcat2.getUndoManager().discardAllEdits();
				Opcat2.setRedoEnabled(false);
				Opcat2.setUndoEnabled(false);
			}
			if (isUndoable == true) {
				Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
				Opcat2.getUndoManager().undoableEditHappened(
						new UndoableEditEvent(this, undoAction));
				Opcat2.setUndoEnabled(Opcat2.getUndoManager().canUndo());
				Opcat2.setRedoEnabled(Opcat2.getUndoManager().canRedo());
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(Opcat2.getFrame(),
					"Could not Copy, Check Selection Validity", "Opcat2 Error",
					JOptionPane.ERROR_MESSAGE);
			deselectAll();
			StateMachine.reset();
			OpcatLogger.logError(ex);
		}
		return null;
	}

	/**
	 * Delete instances from the Project. if a thing can not be deleted a String
	 * is returned which holds the Error Message. If the deletion was succesfull
	 * then a null is returned.
	 * 
	 * @param thingsToDelete
	 *            an Enumeration of Instances to delete
	 * @param action
	 *            CUT or DELETE action
	 * @return String which is null if no Error was Encountered.
	 */
	private String _delete(Enumeration thingsToDelete, int action) {

		Hashtable selConnectionEdgesInst = new Hashtable();
		Hashtable selLinksRelationsInst = new Hashtable();
		Instance tmpInst = null;

		for (; thingsToDelete.hasMoreElements();) {
			tmpInst = (Instance) thingsToDelete.nextElement();
			if (tmpInst instanceof ConnectionEdgeInstance) {
				selConnectionEdgesInst.put(tmpInst.getKey(), tmpInst);
			} else {
				selLinksRelationsInst.put(tmpInst.getKey(), tmpInst);
			}
		}

		if ((selConnectionEdgesInst.size() == 0)
				&& (selLinksRelationsInst.size() == 0)) {
			return "Nothing was selected";
		}

		if (selLinksRelationsInst.size() != 0) {
			CheckResult cr = null;

			for (Enumeration e1 = selLinksRelationsInst.elements(); e1
					.hasMoreElements();) {
				if ((action == DELETE)) {
					cr = CheckModule.checkDeletion((Instance) e1.nextElement(),
							this);
				}
				if (action == CUT) {
					cr = CheckModule
							.checkCut((Instance) e1.nextElement(), this);
				}
				if ((cr != null) && (cr.getResult() == IXCheckResult.WRONG)) {
					return cr.getMessage();
				}

				// if ((cr != null) && (cr.getResult() ==
				// IXCheckResult.WARNING)) {
				if ((cr != null) && (cr.getResult() == IXCheckResult.WARNING)) {
					int retValue = JOptionPane.showConfirmDialog(Opcat2
							.getFrame(), cr.getMessage()
							+ "\n Do you want to continue?",
							"Opcat2 - Warning", JOptionPane.YES_NO_OPTION);

					if (retValue == JOptionPane.NO_OPTION) {
						return null;

					}
				}
				// }
			}

		}

		if (selConnectionEdgesInst.size() != 0) {
			CheckResult cr = null;

			// run over all ConnectionEdges and checkTheir possibility for
			// deletion
			// if one of them is illegal to delete, operation is canceled
			for (Enumeration e1 = selConnectionEdgesInst.elements(); e1
					.hasMoreElements();) {
				if ((action == DELETE)) {
					cr = CheckModule.checkDeletion((Instance) e1.nextElement(),
							this);
				}
				if (action == CUT) {
					cr = CheckModule
							.checkCut((Instance) e1.nextElement(), this);
				}
				if ((cr != null) && (cr.getResult() == IXCheckResult.WRONG)) {
					return cr.getMessage();
				}

				if ((cr != null) && (cr.getResult() == IXCheckResult.WARNING)) {
					int retValue = JOptionPane.showConfirmDialog(Opcat2
							.getFrame(), cr.getMessage()
							+ "\n Do you want to continue?",
							"Opcat2 - Warning", JOptionPane.YES_NO_OPTION);

					if (retValue == JOptionPane.NO_OPTION) {
						return null;

					}
				}

			}

			CompoundUndoAction undoAction = new CompoundUndoAction();

			for (Enumeration e2 = selConnectionEdgesInst.elements(); e2
					.hasMoreElements();) {
				tmpInst = (Instance) e2.nextElement();
				for (Enumeration e3 = ((ConnectionEdgeInstance) tmpInst)
						.getRelatedInstances(); e3.hasMoreElements();) {
					Instance currInstance = (Instance) e3.nextElement();
					undoAction.addAction(this.deleteInstance(currInstance));
				}
				undoAction.addAction(this.deleteInstance(tmpInst));
			}

			if ((action == CUT) || (action == DELETE)) {
				Opcat2.getUndoManager().undoableEditHappened(
						new UndoableEditEvent(this, undoAction));
				Opcat2.setUndoEnabled(Opcat2.getUndoManager().canUndo());
				Opcat2.setRedoEnabled(Opcat2.getUndoManager().canRedo());
			}

			Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
			return null;
		}
		// Deletion of links and relations
		CompoundUndoAction undoAction = new CompoundUndoAction();
		for (Enumeration e4 = selLinksRelationsInst.elements(); e4
				.hasMoreElements();) {
			undoAction.addAction(this.deleteInstance((Instance) e4
					.nextElement()));
		}
		if ((action == CUT) || (action == DELETE)) {
			Opcat2.getUndoManager().undoableEditHappened(
					new UndoableEditEvent(this, undoAction));
			Opcat2.setUndoEnabled(Opcat2.getUndoManager().canUndo());
			Opcat2.setRedoEnabled(Opcat2.getUndoManager().canRedo());
		}
		Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
		return null;
	}

	/**
	 * Delete from a specific OPD. Deleting is cancled upon error.
	 * 
	 * @param deletingFrom
	 *            The deleting from OPD. deleted things should be selected
	 * @param action
	 *            CUT or DELETE
	 * @return String null if no error.
	 */
	private String _delete(Opd deletingFrom, int action) {

		String retVal = null;

		if ((deletingFrom == null) || (deletingFrom.getOpdId() == -1)) {
			return null;
		}

		Enumeration e = deletingFrom.getSelectedItems();
		retVal = this._delete(e, action);
		deletingFrom.removeSelection();
		return retVal;

	}

	public String delete(Enumeration e, int action) {
		String retVal = null;
		retVal = this._delete(e, action);
		return retVal;
	}

	/**
	 * delete all instences of things which are in deletingFrom from the project
	 * 
	 * @param deletingFrom
	 *            Enumaration of instances which will be deleted
	 * @param action
	 * @return
	 */
	private String _deleteALL(Opd deletingFrom, int action, boolean stepBystep) {

		String retVal = null;
		Vector vec = new Vector();

		if ((deletingFrom == null) || (deletingFrom.getOpdId() == -1)) {
			return null;
		}

		Enumeration deleteFromEnum = deletingFrom.getSelectedItems();

		if (!deleteFromEnum.hasMoreElements()) {
			return null;
		}

		while (deleteFromEnum.hasMoreElements()) {
			Instance ins = (Instance) deleteFromEnum.nextElement();
			Enumeration deleteFromInstancesEnum = ins.getEntry().getInstances();
			while (deleteFromInstancesEnum.hasMoreElements()) {
				Instance intIns = (Instance) deleteFromInstancesEnum
						.nextElement();
				vec.add(intIns);
			}
		}

		if (!stepBystep) {
			retVal = this._delete(vec.elements(), action);
		} else {
			boolean cancel = false;
			Object[] options = { "Yes", "No", "Cancel" };
			GuiControl gui = GuiControl.getInstance();
			int n;

			Enumeration locEnum = vec.elements();
			boolean ask = true;
			while (locEnum.hasMoreElements() && !cancel) {
				Instance locIns = (Instance) locEnum.nextElement();
				this.showOPD(locIns.getKey().getOpdId());
				locIns.setSelected(true);
				if (ask) {
					n = JOptionPane.showOptionDialog(gui.getFrame(),
							"Delete This Instance ?", "OPCAT II - Message",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, // don't use a
							// custom Icon
							options, // the titles of buttons
							options[0]); // default button title
				} else {
					n = JOptionPane.OK_OPTION;
				}
				locIns.setSelected(false);
				this.removeSelection(locIns);
				switch (n) {
				case JOptionPane.OK_OPTION:
					Vector v = new Vector();
					v.add(locIns);
					retVal = this._delete(v.elements(), action);
					break;
				case JOptionPane.NO_OPTION:
					break;
				case JOptionPane.CANCEL_OPTION:
					cancel = true;
					break;
				}
				if (retVal != null) {
					cancel = true;
				}
			}
			// showOPD(opd.getOpdId());
		}
		deletingFrom.removeSelection();
		return retVal;

	}

	public void clearClipBoard() {
		this.clipBoard.getSelection().selectAll();
		this.clipBoard.selectAll();
		this._delete(this.clipBoard, DELETE);
	}

	public Opd getClipBoard() {
		return this.clipBoard;
	}

	/**
	 * Method checks legality of the link
	 * 
	 * @returns true if leagal, false otherwise
	 */
	private boolean _checkLinkLegality(OpdConnectionEdge source,
			OpdConnectionEdge destination, int linkType) {
		CommandWrapper cw = new CommandWrapper(source.getEntity().getId(),
				source.getOpdKey(), destination.getEntity().getId(),
				destination.getOpdKey(), linkType, this);

		CheckResult cr = CheckModule.checkCommand(cw);

		if (cr.getResult() == IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(Opcat2.getFrame(), cr.getMessage(),
					"Opcat2 - Error", JOptionPane.ERROR_MESSAGE);
			StateMachine.reset();
			return false;
		}

		if (cr.getResult() == IXCheckResult.WARNING) {
			int retValue = JOptionPane.showConfirmDialog(Opcat2.getFrame(), cr
					.getMessage()
					+ "\n Do you want to continue?", "Opcat2 - Warning",
					JOptionPane.YES_NO_OPTION);

			if (retValue == JOptionPane.NO_OPTION) {
				StateMachine.reset();
				return false;
			}
		}

		return true;
	}

	// delete all links
	private UndoableDeleteLink _deleteLinkFromParentOpd(LinkInstance delInstance) {
		CompoundUndoAction undoAction = new CompoundUndoAction();

		undoAction.addAction(this._deleteLink(delInstance));

		return null;
	}

	private UndoableDeleteLink _deleteLinkFromChildOpd(LinkInstance delInstance) {
		return null;
	}

	public String getFileName() {
		return getPath();
	}

	/**
	 * Sets the file path of this project file. <b>should be named
	 * setFilePath</b>
	 * 
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;

		mcURL = OpcatMCManager.getInstance(true).getFileURL(new File(fileName));
	}

	public OpcatLayoutManager getLayoutManager() {
		return this.layoutManager;
	}

	public MetaManager getMetaManager() {
		return this.metaManager;
	}

	/**
	 * Returns an Enumeration of all the <code>MetaLibrary</code> models
	 * imported by this system.
	 * 
	 * @return Enumeration containing <code>MetaLibrary</code> objects.
	 */
	public Enumeration getMetaLibraries() {
		return this.metaManager.getMetaLibraries();
	}

	public Enumeration getMetaLibraries(int type) {
		return this.metaManager.getMetaLibraries(type);
	}

	/**
	 * Gets the version string representation of the OPX file.
	 * 
	 * @return String representation of the version.
	 */
	public String getVersionString() {
		return this.versionString;
	}

	/**
	 * Sets the version string representation of the OPX file.
	 * 
	 * @param string
	 *            The version representation.
	 */
	public void setVersionString(String string) {
		this.versionString = string;
	}

	public void setDuringLoad(boolean dLoad) {
		this.duringLoad = dLoad;
	}

	public ThingInstance getThingInstanceByName(String name) {
		ThingInstance returnThing = null;
		Enumeration e = this.getSystemStructure().getInstancesInOpd(
				this.getCurrentOpd());
		while (e.hasMoreElements()) {
			Instance ins = (Instance) e.nextElement();
			if (ins instanceof ThingInstance) {
				returnThing = (ThingInstance) ins;
				if (returnThing.getEntry().getName().equals(name)) {
					return returnThing;
				}
				returnThing = null;
			}
		}
		return returnThing;
	}

	/**
	 * 
	 * @param source
	 * @param destination
	 * @param linkType
	 * @param target
	 * @return
	 */
	public LinkInstance addLink(Instance source, Instance destination,
			int linkType, Opd target) {

		Entry sourceEntry = this.mainComponentsStructure.getEntry(source
				.getLogicalId());
		ConnectionEdgeInstance sourceInstance = (ConnectionEdgeInstance) sourceEntry
				.getInstance(source.getKey());

		Entry destinationEntry = this.mainComponentsStructure
				.getEntry(destination.getLogicalId());
		ConnectionEdgeInstance destinationInstance = (ConnectionEdgeInstance) destinationEntry
				.getInstance(destination.getKey());

		RelativeConnectionPoint sourcePoint = GraphicsUtils
				.calculateConnectionPoint(sourceInstance.getConnectionEdge(),
						destinationInstance.getConnectionEdge());
		RelativeConnectionPoint destinationPoint = GraphicsUtils
				.calculateConnectionPoint(destinationInstance
						.getConnectionEdge(), sourceInstance
						.getConnectionEdge());

		JLayeredPane tempContainer = target.getDrawingArea();

		if (sourceInstance.getConnectionEdge().getParent() == destinationInstance
				.getConnectionEdge().getParent()) {
			tempContainer = (JLayeredPane) sourceInstance.getConnectionEdge()
					.getParent();
		}

		return this.addLink(sourceInstance.getConnectionEdge(), sourcePoint,
				destinationInstance.getConnectionEdge(), destinationPoint,
				tempContainer, linkType, -1, -1);
	}

	/**
	 * @name addObjectInstance
	 * @param name
	 * @param x
	 * @param y
	 * @param parentInstance
	 * @return ObjbjectInstnace this functions add a new Object to the current
	 *         Opd
	 * 
	 */
	public ObjectInstance addObjectInstance(String name, int x, int y,
			ThingEntry myEntry) {
		return this.addObjectInstanceToContainer(name, x, y, myEntry,
				this.currentOpd.getOpdContainer().getDrawingArea());
	}

	/**
	 * Adds a new object instance to a given container, which can be a thing or
	 * just an OPD.
	 * 
	 * @param name
	 *            The name of the instance
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @param myEntry
	 *            The logical entry of the instance
	 * @param container
	 *            The container that the thing is placed within. Can be an
	 *            inzoomed thing, or just the OPD. In the latter case, then
	 *            <code>currentOpd.getOpdContainer().getDrawingArea()</code>
	 *            should be passed to the method
	 * @return The instance that was created.
	 */
	public ObjectInstance addObjectInstanceToContainer(String name, int x,
			int y, ThingEntry myEntry, Container container) {
		IXObjectInstance newIns = this.addObject(x, y, container, myEntry
				.getId(), -1, true);
		newIns.getIXEntry().setName(name);
		newIns.getIXEntry().updateInstances();
		return (ObjectInstance) newIns;
	}

	/**
	 * @name createNewOpd
	 * 
	 * @param zoomed
	 *            - boolean value that indicates that this creation is a result
	 *            of zooming
	 * @param fInstanc                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             