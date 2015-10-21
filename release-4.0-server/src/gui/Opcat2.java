package gui;

import exportedAPI.OpcatConstants;
import exportedAPI.OpcatExtensionTool;
import exportedAPI.OpcatExtensionToolX;
import exportedAPI.opcatAPI.ISystem;
import exportedAPI.opcatAPIx.IXSystem;
import extensionTools.Documents.Doc.Document;
import extensionTools.Documents.UserInterface.DocSel;
import extensionTools.Documents.UserInterface.HandleTemp;
import extensionTools.Testing.TestingSettings;
import extensionTools.Testing.TestingSettingsWindow;
import extensionTools.Testing.TestingStatusBar;
import extensionTools.Testing.TestingSystem; //import extensionTools.hio.DrawAppNew;
import extensionTools.opltoopd.Opl2Opd;
import extensionTools.search.SearchExtensionTool;
import extensionTools.uml.userInterface.UMLChooser;
import extensionTools.validator.Validator;
import gui.actions.edit.AddElementAction;
import gui.actions.edit.BrowseAction;
import gui.actions.edit.CopyAction;
import gui.actions.edit.CloneAction;
import gui.actions.edit.CopyFormatAction;
import gui.actions.edit.CutAction;
import gui.actions.edit.DataFormAction;
import gui.actions.edit.DeSecretAction;
import gui.actions.edit.DeleteAction;
import gui.actions.edit.MetaColoringAction;
import gui.actions.edit.MetaDataAction;
import gui.actions.edit.MetaHideAction;
import gui.actions.edit.PasteAction;
import gui.actions.messages.OpcatMessagesShowConsoleAction;
import gui.actions.reports.ProjectSummaryAction;
import gui.actions.svn.SvnShowAdminConsoleAction;
import gui.actions.svn.SvnShowFileConsoleAction;
import gui.actions.edit.RedoAction;
import gui.actions.edit.RefrashOPDsNumbersAction;
import gui.actions.edit.SelectAllAction;
import gui.actions.edit.UndoAction;
import gui.actions.expose.OpcatExposeShowExposedList;
import gui.actions.expose.OpcatExposeShowEntity;
import gui.actions.file.CloseAction;
import gui.actions.file.ExitAction;
import gui.actions.file.GenerateSQL;
import gui.actions.file.LoadExampleAction;
import gui.actions.file.LoadOpxAction;
import gui.actions.file.LoadPrevious;
import gui.actions.file.NewProjectAction;
import gui.actions.file.ReuseImportAction;
import gui.actions.file.SaveAsImageAction;
import gui.actions.file.SaveAsOpxAction;
import gui.actions.file.SaveOpxAction;
import gui.actions.help.HelpAboutAction;
import gui.actions.help.HelpContentsAction;
import gui.actions.help.OpenUrlAction; //import gui.actions.plugins.VisWebAction;
import gui.controls.EditControl;
import gui.controls.FileControl;
import gui.controls.GuiControl;
import gui.dataProject.DataProject;
import gui.dataProject.MetaLoadColors;
import gui.images.expose.ExposeImages;
import gui.images.misc.MiscImages;
import gui.images.opm.OPMImages;
import gui.images.standard.StandardImages;
import gui.images.svn.SvnImages;
import gui.images.testing.TestingImages;
import gui.license.Features;
import gui.license.ILicense;
import gui.license.LicenseFactory;
import gui.metaLibraries.dialogs.LibrariesLoadingWindow;
import gui.metaLibraries.logic.MetaLibrary;
import gui.opdGraphics.SplashLabel;
import gui.opdProject.GenericTable;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.opdProject.OplColorsDialog;
import gui.opdProject.ProjectPropertiesDialog;
import gui.opdProject.StateMachine;
import gui.projectStructure.Entry;
import gui.repository.Repository;
import gui.undo.OpcatUndoManager;
import gui.util.CustomFileFilter;
import gui.util.Debug;
import gui.util.ExtensionToolInfo;
import gui.util.FileCopy;
import gui.util.JToolBarButton;
import gui.util.JToolBarToggleButton;
import gui.util.LastFileEntry;
import gui.util.LoadThingsCSV;
import gui.util.OpcatException;
import gui.util.OpcatLogger;
import gui.util.opcatGrid.GridPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.font.FontRenderContext;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Properties;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.undo.UndoManager;

import util.Configuration;

import modelControl.OpcatMCManager;
import net.java.swingfx.waitwithstyle.PerformanceInfiniteProgressPanel;

import com.pagosoft.plaf.PlafOptions;
import com.pagosoft.plaf.ThemeFactory;

/**
 * The main class for handling the OPCAT GUI.
 * 
 * @author raanan
 * 
 */
public class Opcat2 {

	private static String loadedfilename;

	private static final String EMPTY_HTML = "<html><head></head><body></body></html>";

	private Hashtable extensionsTable;

	private static JFrame myFrame;

	private static OpdProject currentProject;

	// /** *******************HIOTeam*************** */
	// public static DrawAppNew drawing = null;

	private JMenuBar mainMenuBar;

	private JToolBar componentsToolBar;

	private JToolBar standardToolBar;

	private JToolBar testingToolBar;

	private JPanel diagramPanel;

	private TestingSystem anSystem;

	/**
	 * A Validator object - enables validaton of the model.
	 * 
	 * @author Eran Toch
	 */
	private Validator validator = null;

	/**
	 * Holds the license object for the Opcat instance that determines the
	 * license policy.
	 */
	private static ILicense license = null;

	private boolean projectWasOpened;

	private JDesktopPane desktop;

	private JTabbedPane extensionToolsPane;

	private static JTextPane oplPane;

	protected static OpcatUndoManager myUndoManager = new OpcatUndoManager();

	private final static String fileSeparator = System
			.getProperty("file.separator");

	private final static String opmTypes = Configuration.getInstance()
			.getProperty("opcat_home")
			+ fileSeparator
			+ "codeGenerator"
			+ fileSeparator
			+ "opl2Java"
			+ fileSeparator + "opmTypes";

	public final static String OPCAT_SITE_URL = "http://www.opcat.com";

	private static Repository repository;

	private JToolBarButton newButton;

	private JToolBarButton openButton;

	private JToolBarButton closeButton;

	private JToolBarButton saveButton;

	private JToolBarButton printButton;

	private JToolBarButton searchButton;

	private JToolBarButton cutButton;

	private JToolBarButton copyButton;

	private JToolBarButton upButton;

	// private JToolBarButton downButton;
	private JToolBarButton backButton;

	private JToolBarButton forwardButton;

	private JToolBarButton projectSummary;

	private JToolBarButton cloneButton;

	private JToolBarButton copyFormatButton;

	private JToolBarButton pasteButton;

	private JToolBarButton anZoomInButton;

	private JToolBarButton anZoomOutButton;

	private JToolBarButton zoomInButton;

	private JToolBarButton zoomOutButton;

	private JToolBarButton importButton;

	private JToolBarButton metaLibsButton;

	private JToolBarButton metaLibsClasifficationButton;

	private JToolBarButton reqButton;

	private JToolBarButton dataFormButton;

	// private JToolBarButton testingButton;

	private JToolBarToggleButton anPauseButton;

	private JLabel anStatusLabel;

	private JTextField anForwardField;

	private JTextField anBackwardField;

	private static JToolBarToggleButton showHideExtensionTools;

	private static JToolBarButton undoButton;

	private static JToolBarButton redoButton;

	private JToolBarButton deleteButton;

	private static JToolBarToggleButton moveOnDragButton, selectOnDragButton;

	private ButtonGroup moveSelectGroup = new ButtonGroup();

	/** *****HIOTeam**** */
	// private static JToolBarToggleButton drawOnDragButton;
	private JMenuItem newItem;

	private JMenuItem openItem;

	private JMenuItem closeItem;

	private JMenuItem saveItem;

	private JMenuItem saveAsItem;

	private JMenuItem printItem;

	private JMenuItem printPreview;

	private JMenuItem cutItem;

	private JMenuItem copyItem;

	private JMenuItem cloneItem;

	private JMenuItem pasteItem;

	private JMenuItem refrashOPDNumbers;
	private JCheckBoxMenuItem showAdminConsole;
	private JCheckBoxMenuItem showMessagesConsole;
	private JCheckBoxMenuItem showFileConsole;

	private static JMenuItem undoItem;

	private static JMenuItem redoItem;

	private JMenuItem grZoomInItem;

	private JMenuItem grZoomOutItem;

	private JMenuItem deleteItem;

	private JMenuItem refreshItem;

	private JMenuItem selectAllItem;

	private JMenuItem searchItem;

	private JWindow splashScreen = null;

	private JWindow waitScreen = null;

	// By Eran Toch
	private JWindow metaWaitScreen = null;

	private JLabel splashLabel = null;

	private JProgressBar jpb;

	private static boolean isWholeProjectCompiled;

	// private static JCheckBoxMenuItem isOPLShown;
	private JSplitPane desktopTools;

	private JDialog aboutDlg;

	/**
	 * If set to <code>true</code>, then OPCAT2 would present the process name
	 * message (the one with the "ing").
	 * 
	 * @author Eran Toch
	 */
	private static boolean showProcessNameMessage = true;

	public static final int GRAPHICAL_CHANGE = 1;

	public static final int LOGICAL_CHANGE = 2; // without updating the OPD's

	// Tree

	public static final int OPD_CHANGE = 3;

	public static final int LOGICAL_CHANGE_TREE = 4;

	private int progressCounter = 0;

	private JToolBar[] toolbars;

	private JFileChooser myFileChooser = new JFileChooser();

	private static String drawingAreaOnMouseDragAction = "select";

	// can be 'select' or 'move'

	// ReuseComment
	private JMenuItem reuseImportItem;

	// EndReuseComment

	/** ******************** File Actions ************************** */
	private LoadOpxAction loadOpxActionA = null;

	private SaveOpxAction saveOpxActionA = null;

	private SaveAsOpxAction saveAsOpxActionA = null;

	private CloseAction closeActionA = null;

	private ExitAction exitActionA = null;

	private NewProjectAction newProjectActionA = null;

	private LoadExampleAction loadExampleActionA = null;

	private SaveAsImageAction saveAsImageActionA = null;

	private ReuseImportAction reuseImportActionA = null;

	/** ***************** Edit Actions *************************** */

	private CopyAction copyActionA = null;

	private CloneAction cloneActionA = null;

	private BrowseAction upActionA = null;

	// private BrowseAction downActionA = null;
	private BrowseAction backActionA = null;

	private BrowseAction forwardActionA = null;

	private ProjectSummaryAction projectSummaryA = null;

	private CopyFormatAction copyFormatActionA = null;

	private CutAction cutActionA = null;

	private DeleteAction deleteActionA = null;

	private PasteAction pasteActionA = null;

	private RedoAction redoActionA = null;

	private UndoAction undoActionA = null;

	private SelectAllAction selectAllActionA = null;

	/** ***************** Add Element Actions *************************** */
	private AddElementAction objectAction = null;

	private AddElementAction apAction = null;

	private AddElementAction fcAction = null;

	private AddElementAction gsAction = null;

	private AddElementAction ciAction = null;

	private AddElementAction unidirAction = null;

	private AddElementAction bidirAction = null;

	private AddElementAction stateAction = null;

	private AddElementAction processAction = null;

	private AddElementAction inAction = null;

	private AddElementAction agAction = null;

	private AddElementAction reAction = null;

	private AddElementAction effectAction = null;

	private AddElementAction exceptionAction = null;

	private AddElementAction invocationAction = null;

	private AddElementAction consumptionEventLinkAction = null;

	private AddElementAction instrumentEventLinkAction = null;

	private AddElementAction conditionAction = null;

	/** ***************** Plugins Actions *************************** */
	// private VisWebAction visWebActionA = null;
	private MetaDataAction reqActionA = null;

	private DataFormAction dataFormAction = null;

	// private ShowResultSetAction testingAction = null;

	/** ******************* Help Actions ****************************** */
	private HelpContentsAction helpContentsActionA = null;

	private HelpAboutAction helpAboutActionA = null;

	private OpenUrlAction checkForUpdatesActionA = null;

	/** ***************** Singletons Initating *************************** */
	private Configuration configuration = null;

	private FileControl fileControl = null;

	private GuiControl guiControl = null;

	private EditControl editControl = null;

	private static PerformanceInfiniteProgressPanel glassPane = null;

	private void checkInstalation() throws OpcatException {
		try {
			// Load configuration from the properties file
			this.configuration = Configuration.getInstance();
			configuration.saveProperties();
		} catch (Exception ex) {
			throw new OpcatException("OPCAT Setup problem");
		}
	}

	public Opcat2() {
		/**
		 * first thing is to check if the OPCAT installation is correct.
		 * 
		 */
		try {
			checkInstalation();
		} catch (OpcatException ex) {
			JOptionPane.showMessageDialog(getFrame(), ex.getMessage(),
					"OPCAT II", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		// JOptionPane.showConfirmDialog(getFrame(),
		// "A process which is defined by an extrnal library was changed",
		// "OPCAT II", JOptionPane.OK_OPTION, JOptionPane.CANCEL_OPTION);

		// debug in dev versions
		// do not touch this line as iuseBoldMenuFontst is managed by the ant
		// build
		Debug.setDebugLevelToCore();
		//

		// Viewer or not ? defined at compile time
		boolean viewer = false; // do not change this line auto frm compiler
		if (viewer)
			StateMachine.setAnimated(viewer);

		// set locale to en_US
		Locale.setDefault(Locale.ENGLISH);
		// Locale.setDefault(new Locale("is"));

		// for(int i = 0 ; i < Locale.getAvailableLocales().length; i++)
		// System.out.print(Locale.getAvailableLocales()[i] + "\n") ;

		// System.out.print(Locale.getDefault()) ;

		// load the colors file
		MetaLoadColors.getInstance().load();

		myFrame = new JFrame("Opcat II");

		// javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(new
		// OpcatTheme());
		// SwingUtilities.updateComponentTreeUI(myFrame);

		myFrame.setIconImage(MiscImages.LOGO_MID_ICON.getImage());
		Container cp = myFrame.getContentPane();
		cp.setLayout(new BorderLayout());

		glassPane = new PerformanceInfiniteProgressPanel(false);
		myFrame.setGlassPane(glassPane);

		// Load License
		license = this.loadLicense();

		/** ************************* Initiating Controls **************** */
		this.fileControl = FileControl.getInstance();
		this.fileControl.setOpcat(this);
		this.guiControl = GuiControl.getInstance();
		this.guiControl.setOpcat(this);
		this.editControl = EditControl.getInstance();
		this.editControl.setOpcat(this);

		/** ************************* Initiating File Actions **************** */
		this.loadOpxActionA = new LoadOpxAction("Open", StandardImages.OPEN);
		this.saveOpxActionA = new SaveOpxAction("Save", StandardImages.SAVE);
		this.saveAsOpxActionA = new SaveAsOpxAction("Save As",
				StandardImages.SAVE_AS);
		this.closeActionA = new CloseAction("Close", StandardImages.CLOSE);
		this.exitActionA = new ExitAction("Exit", StandardImages.EXIT);
		this.newProjectActionA = new NewProjectAction("New...",
				StandardImages.NEW);
		this.loadExampleActionA = new LoadExampleAction("",
				StandardImages.System_Icon);
		this.saveAsImageActionA = new SaveAsImageAction("Save As Image",
				StandardImages.EMPTY);
		this.reuseImportActionA = new ReuseImportAction("Import Model",
				StandardImages.IMPORT);

		/** ************************* Initiating Edit Actions **************** */
		this.copyActionA = new CopyAction("Copy", StandardImages.COPY);
		this.cloneActionA = new CloneAction("Clone", StandardImages.CLONE);

		this.upActionA = new BrowseAction("Up", StandardImages.UP);
		// this.downActionA = new BrowseAction("Down", StandardImages.DOWN);
		this.backActionA = new BrowseAction("Back", StandardImages.BACK);
		this.forwardActionA = new BrowseAction("Forward",
				StandardImages.FORWARD);

		this.projectSummaryA = new ProjectSummaryAction();
		this.copyFormatActionA = new CopyFormatAction("Copy Format",
				StandardImages.COPYFORMAT);
		this.cutActionA = new CutAction("Cut", StandardImages.CUT);
		this.deleteActionA = new DeleteAction("Delete", StandardImages.DELETE);
		this.pasteActionA = new PasteAction("Paste", StandardImages.PASTE);
		this.redoActionA = new RedoAction("Redo", StandardImages.REDO);
		this.undoActionA = new UndoAction("Undo", StandardImages.UNDO);
		this.selectAllActionA = new SelectAllAction("Select All",
				StandardImages.EMPTY);

		// mergeDraggingActionA = new MergeDraggingAction("Merge Entities",
		// StandardImages.EMPTY);

		/***********************************************************************
		 * ************************* Initiating Add Element Actions
		 **********************************************************************/
		this.objectAction = new AddElementAction("Object", OPMImages.OBJECT,
				StateMachine.ADD_OBJECT, OpcatConstants.OBJECT);
		this.apAction = new AddElementAction("Agregation-Particulation",
				OPMImages.AGGREGATION, StateMachine.ADD_RELATION,
				OpcatConstants.AGGREGATION_RELATION);
		this.fcAction = new AddElementAction("Exhibition-Characterization",
				OPMImages.EXHIBITION, StateMachine.ADD_RELATION,
				OpcatConstants.EXHIBITION_RELATION);
		this.gsAction = new AddElementAction("Generalization-Specialization",
				OPMImages.GENERALIZATION, StateMachine.ADD_RELATION,
				OpcatConstants.SPECIALIZATION_RELATION);
		this.ciAction = new AddElementAction("Classification-Instantiation",
				OPMImages.INSTANTIATION, StateMachine.ADD_RELATION,
				OpcatConstants.INSTANTINATION_RELATION);
		this.unidirAction = new AddElementAction("Unidirectional Relation",
				OPMImages.UNI_DIRECTIONAL, StateMachine.ADD_GENERAL_RELATION,
				OpcatConstants.UNI_DIRECTIONAL_RELATION);
		this.bidirAction = new AddElementAction("Bidirectional Relation",
				OPMImages.BI_DIRECTIONAL, StateMachine.ADD_GENERAL_RELATION,
				OpcatConstants.BI_DIRECTIONAL_RELATION);
		this.stateAction = new AddElementAction("State", OPMImages.STATE,
				StateMachine.ADD_STATE, OpcatConstants.STATE);
		this.processAction = new AddElementAction("Process", OPMImages.PROCESS,
				StateMachine.ADD_PROCESS, OpcatConstants.PROCESS);
		this.inAction = new AddElementAction("Instrument Link",
				OPMImages.INSTRUMENT_LINK, StateMachine.ADD_LINK,
				OpcatConstants.INSTRUMENT_LINK);
		this.agAction = new AddElementAction("Agent Link",
				OPMImages.AGENT_LINK, StateMachine.ADD_LINK,
				OpcatConstants.AGENT_LINK);
		this.reAction = new AddElementAction("Result Link",
				OPMImages.RESULT_LINK, StateMachine.ADD_LINK,
				OpcatConstants.CONSUMPTION_LINK);
		this.effectAction = new AddElementAction("Effect Link",
				OPMImages.EFFECT_LINK, StateMachine.ADD_LINK,
				OpcatConstants.EFFECT_LINK);
		this.exceptionAction = new AddElementAction("Exception Link",
				OPMImages.EXCEPTION_LINK, StateMachine.ADD_LINK,
				OpcatConstants.EXCEPTION_LINK);
		this.invocationAction = new AddElementAction("Invocation Link",
				OPMImages.INVOCATION_LINK, StateMachine.ADD_LINK,
				OpcatConstants.INVOCATION_LINK);
		this.consumptionEventLinkAction = new AddElementAction(
				"Consumption Event Link", OPMImages.CONSUMPTION_EVENT_LINK,
				StateMachine.ADD_LINK, OpcatConstants.CONSUMPTION_EVENT_LINK);
		this.instrumentEventLinkAction = new AddElementAction(
				"Instrument Event Link", OPMImages.INSTRUMENT_EVENT_LINK,
				StateMachine.ADD_LINK, OpcatConstants.INSTRUMENT_EVENT_LINK);
		this.conditionAction = new AddElementAction("Condition Link",
				OPMImages.CONDITION_LINK, StateMachine.ADD_LINK,
				OpcatConstants.CONDITION_LINK);

		/** ************************* Initiating Plugin Actions **************** */
		// this.visWebActionA = new VisWebAction("OWL Generation");
		this.reqActionA = new MetaDataAction(
				"Show Classification Libraries in Grid", StandardImages.REQ);

		this.dataFormAction = new DataFormAction("Create Data Form",
				StandardImages.DATAFORM);

		// this.testingAction = new ShowResultSetAction("In-Memory DB Test",
		// StandardImages.DATAFORM);

		/** ************************* Initiating Help Actions **************** */
		this.helpContentsActionA = new HelpContentsAction("Contents",
				StandardImages.HELPBOOK);
		this.helpAboutActionA = new HelpAboutAction("About",
				StandardImages.EMPTY);
		this.checkForUpdatesActionA = new OpenUrlAction("Check for Updates",
				StandardImages.EMPTY, OPCAT_SITE_URL);

		// Create and throw the splash screen up. Since this will
		// physically throw bits on the screen, we need to do this
		// on the GUI thread using invokeLater.
		this._createSplashScreen();

		// do the following on the gui thread
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Opcat2.this.splashScreen.setVisible(true);
			}
		});

		myFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		myFrame.setSize((int) (screenSize.getWidth() * 7 / 8),
				(int) (screenSize.getHeight() * 7 / 8));
		myFrame.setExtendedState(Frame.MAXIMIZED_BOTH);

		this.desktop = new JDesktopPane();
		// desktop.setPreferredSize(new Dimension(1200, 800));
		this.desktop.setBackground(Color.lightGray);

		oplPane = new JTextPane();
		oplPane.setContentType("text/html");
		oplPane.setEditable(false);
		oplPane.setPreferredSize(new Dimension(300, 450));

		oplPane.setBackground(new Color(230, 230, 230));

		this.jpb.setValue(++this.progressCounter);

		repository = new Repository(currentProject, this);
		repository.setMinimumSize(repository.getPreferredSize());

		this.jpb.setValue(++this.progressCounter);

		cp.add(this._constructStandardToolBar(), BorderLayout.NORTH);

		this.jpb.setValue(++this.progressCounter);

		this.diagramPanel = new JPanel();
		this.diagramPanel.setLayout(new BorderLayout());
		this.diagramPanel.add(this._constructComponentToolBar(),
				BorderLayout.SOUTH);

		this._constructTestingToolBar();

		this.jpb.setValue(++this.progressCounter);
		this.extensionToolsPane = new JTabbedPane(SwingConstants.BOTTOM);
		// add OPL Pane
		this.extensionToolsPane.add("OPL Generator", new JScrollPane(oplPane));

		int high = Integer
				.valueOf(
						Configuration.getInstance().getProperty(
								"lower_panel_min_high")).intValue();
		extensionToolsPane.setMinimumSize(new Dimension(extensionToolsPane
				.getWidth(), high));

		// ----------------------------------------------------------------
		// init all Extension Tools here and add them to extensionToolsPane
		// ----------------------------------------------------------------

		this.desktopTools = this._constructdesktopToolsPane(this.desktop,
				this.extensionToolsPane);
		this.diagramPanel.add(this.desktopTools, BorderLayout.CENTER);

		cp.add(this._constructSplitPane(/* opcatTree */
		repository, this.diagramPanel), BorderLayout.CENTER);
		// cp.add(ConstructSplitPane(opcatTree, desktop), BorderLayout.CENTER);
		this.projectWasOpened = false;

		this.toolbars = new JToolBar[2];
		this.toolbars[0] = this.standardToolBar;
		this.toolbars[1] = this.componentsToolBar;
		// setProjectActionEnable(false);

		myFrame.setJMenuBar(this._constructMenuBar());

		this.jpb.setValue(++this.progressCounter);

		// Escape listener
		KeyListener escapeListener = new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if ((e.getKeyCode() == KeyEvent.VK_ESCAPE)
						&& (currentProject != null)
						&& (currentProject.getCurrentOpd() != null)) {
					if (!StateMachine.isWaiting()) {
						StateMachine.reset();
						currentProject
								.getCurrentOpd()
								.getOpdContainer()
								.getDrawingArea()
								.setCursor(
										Cursor
												.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						currentProject.removeSelection();
					}
				}
			}
		};

		myFrame.addKeyListener(escapeListener);

		// Window closer
		WindowListener l = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Opcat2.this.fileControl._exit();
			}
		};
		myFrame.addWindowListener(l);
		// end Window closer

		this.jpb.setValue(++this.progressCounter);

		try {

			PlafOptions.useBoldMenuFonts(true);
			PlafOptions.setCurrentTheme(ThemeFactory.GRAY);
			PlafOptions.setOfficeScrollBarEnabled(true);
			PlafOptions.setVistaStyle(true);
			PlafOptions.useShadowBorder(true);
			PlafOptions.useToolBarIcon(true);

			PlafOptions.setAsLookAndFeel();

			PlafOptions.updateAllUIs();

			// UIManager.setLookAndFeel(new NimbusLookAndFeel());

		} catch (Exception ex) {

		}

		// _loadClasses();
		// Show the main screen and take down the splash screen. Note that
		// we again must do this on the GUI thread using invokeLater.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				myFrame.setVisible(true);
				Opcat2.this.splashScreen.setVisible(false);
				Opcat2.this.splashScreen = null;
				Opcat2.this.splashLabel = null;
			}
		});

	}

	// ==========================================================================================
	// create menu bar with all its handling
	private JMenuBar _constructMenuBar() {

		/**
		 * 
		 */
		// OpcatSkinManager skinma = OpcatSkinManager.instance();
		// String[] themes = skinma.getThemes();
		// skinma.setMainComponent(getFrame());
		// do not change this, used during build to set def skin
		// String compileSkinName = "replaced by auto build skin xxxxxx";
		// skinma.setTheme(compileSkinName);
		/**
		 * 
		 */

		repository.getOutlookBar().setBold();

		this.mainMenuBar = new JMenuBar();

		this.openItem = new JMenuItem(this.loadOpxActionA);
		this.openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.CTRL_MASK));
		this.closeItem = new JMenuItem(this.closeActionA);
		this.saveItem = new JMenuItem(this.saveOpxActionA);
		this.saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));
		this.saveAsItem = new JMenuItem(this.saveAsOpxActionA);
		this.printItem = new JMenuItem(this.printAction);
		this.printItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				ActionEvent.CTRL_MASK));
		this.printPreview = new JMenuItem(this.printPreviewAction);
		this.cutItem = new JMenuItem(this.cutActionA);
		this.cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				ActionEvent.CTRL_MASK));
		this.copyItem = new JMenuItem(this.copyActionA);
		this.copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				ActionEvent.CTRL_MASK));

		this.cloneItem = new JMenuItem(this.cloneActionA);
		this.cloneItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				ActionEvent.CTRL_MASK));

		this.pasteItem = new JMenuItem(this.pasteActionA);
		this.pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
				ActionEvent.CTRL_MASK));
		undoItem = new JMenuItem(this.undoActionA);
		undoItem.setEnabled(false);
		undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				ActionEvent.CTRL_MASK));

		redoItem = new JMenuItem(this.redoActionA);
		redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
		redoItem.setEnabled(false);

		this.deleteItem = new JMenuItem(this.deleteActionA);
		this.deleteItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_DELETE, 0));

		this.selectAllItem = new JMenuItem(this.selectAllActionA);
		this.selectAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.CTRL_MASK));

		this.searchItem = new JMenuItem(this.SearchActionA);
		this.searchItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				ActionEvent.CTRL_MASK));

		this.reuseImportItem = new JMenuItem(this.reuseImportActionA);

		// File menu
		JMenu fileMenu;
		fileMenu = new JMenu("System");
		this.newItem = new JMenuItem(this.newProjectActionA);
		this.newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.CTRL_MASK));
		fileMenu.add(this.newItem);

		fileMenu.add(this.openItem);

		fileMenu.add(this.closeItem);
		fileMenu.add(this.saveItem);
		fileMenu.add(this.saveAsItem);

		File[] examples = (new File(FileControl.getInstance()
				.getOPCATDirectory()
				+ FileControl.fileSeparator + "Examples" + fileSeparator))
				.listFiles(new CustomFileFilter("opz"));

		if (examples == null) {
			examples = (new File("Examples" + fileSeparator))
					.listFiles(new CustomFileFilter("opz"));
		}

		JMenu examplesMenu = new JMenu("Examples");
		examplesMenu.setIcon(StandardImages.System_Icon);
		if (examples != null) {
			for (int i = 0; i < examples.length; i++) {
				JMenuItem tempItem = new JMenuItem(this.loadExampleActionA);
				tempItem.setText(examples[i].getName().replaceAll(".opz", ""));
				examplesMenu.add(tempItem);
			}
		}
		fileMenu.add(examplesMenu);

		fileMenu.add(new JSeparator());

		fileMenu.add(this.projectPropertiesAction);
		fileMenu.add(this.projectMetaLibrariesAction);
		fileMenu.add(this.projectMetaLibrariesClasifficationAction);
		fileMenu.add(this.reuseImportItem);

		fileMenu.add(new JSeparator());
		fileMenu.add(this.saveAsImageActionA);
		fileMenu.add(new JSeparator());

		fileMenu.add(new JMenuItem(this.pageSetupAction));
		fileMenu.add(this.printItem);
		fileMenu.add(this.printPreview);
		fileMenu.add(new JSeparator());

		fileMenu.add(new JSeparator());

		// Adding previous files used
		if (this.configuration != null) {
			try {
				ListIterator<LastFileEntry> lastFiles = this.configuration
						.getLastUsedFiles();
				LastFileEntry last = null;
				int i = 0;
				while (lastFiles.hasNext() && (i < 4)) {
					last = (LastFileEntry) lastFiles.next();
					if (last != null) {
						i++;
						LoadPrevious loadPrevious = new LoadPrevious(i + "  "
								+ last.getFileNameWithoutPath(),
								StandardImages.System_Icon, last.getFileUrl());
						JMenuItem loadItem = new JMenuItem(loadPrevious);
						fileMenu.add(loadItem);
					}
				}
			} catch (OpcatException e) {
			}
		}
		fileMenu.add(new JSeparator());
		fileMenu.add(this.exitActionA);

		// edit menu
		JMenu editMenu;
		editMenu = new JMenu("Edit");

		editMenu.add(undoItem);
		editMenu.add(redoItem);

		editMenu.add(this.cutItem);
		editMenu.add(this.copyItem);
		// editMenu.add(cloneItem);
		editMenu.add(this.pasteItem);
		editMenu.add(this.deleteItem);
		editMenu.add(this.selectAllItem);
		editMenu.add(new JSeparator());
		editMenu.add(this.searchItem);

		// Merge is Remarked by Eran Toch
		// editMenu.add(new JSeparator());
		// draggingButton = new JMenuItem(mergeDraggingActionA);
		// editMenu.add(draggingButton);

		// view menu
		JMenu viewMenu;
		JMenuItem tmpItem;
		viewMenu = new JMenu("View");

		this.grZoomInItem = new JMenuItem(this.viewZoomInAction);
		this.grZoomOutItem = new JMenuItem(this.viewZoomOutAction);

		viewMenu.add(this.grZoomInItem);
		viewMenu.add(this.grZoomOutItem);

		JMenu toolbarsMenu = new JMenu("Toolbars");
		toolbarsMenu.setIcon(StandardImages.EMPTY);

		for (int i = 0; i < this.toolbars.length; i++) {
			tmpItem = new JCheckBoxMenuItem(this.toolbars[i].getName(), true);
			tmpItem.addActionListener(this.tBarslst);
			toolbarsMenu.add(tmpItem);
		}

		viewMenu.add(toolbarsMenu);

		// JMenu mLF = new JMenu("Look&Feel");
		//
		// ButtonGroup uiButtonGroup = new ButtonGroup();
		// for (int k = 0; k < themes.length; k++) { // k < lfs.length; k++)
		// // {
		// String name = themes[k]; // lfs[k].getName();
		// JMenuItem lf = new JRadioButtonMenuItem(name);
		//
		// //
		//
		// // System.out.println(name) ;
		// if (name == skinma.getCurrentTheme()) {
		// lf.setSelected(true);
		// }
		// // m_lfs.put(name, lfs[k].getClassName());
		// lf.addActionListener(this.lst);
		// uiButtonGroup.add(lf);
		// mLF.add(lf);
		// }
		//
		// mLF.setIcon(StandardImages.EMPTY);
		// viewMenu.add(mLF);

		this.refreshItem = new JMenuItem(this.refreshAction);
		this.refreshItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5,
				0));
		viewMenu.add(this.refreshItem);
		viewMenu.add(new JSeparator());
		refrashOPDNumbers = new JMenuItem(new RefrashOPDsNumbersAction(
				"Renumber OPDs", StandardImages.RENUMBER_OPDS));
		showFileConsole = new JCheckBoxMenuItem("File Console", false);
		showFileConsole.addActionListener(new SvnShowFileConsoleAction(
				"File Console", SvnImages.ACTION_FILE_CONSOLE_VIEW));
		showFileConsole.setIcon(SvnImages.ACTION_FILE_CONSOLE_VIEW);

		showAdminConsole = new JCheckBoxMenuItem("Admin Console", false);
		showAdminConsole.setIcon(SvnImages.ACTION_ADMIN_CONSOLE_VIEW);
		showAdminConsole.addActionListener(new SvnShowAdminConsoleAction(
				"Admin Console", SvnImages.ACTION_ADMIN_CONSOLE_VIEW));

		showMessagesConsole = new JCheckBoxMenuItem("Messages Console", false);
		showMessagesConsole.setIcon(SvnImages.ACTION_MESSAGES_CONSOLE_VIEW);
		showMessagesConsole
				.addActionListener(new OpcatMessagesShowConsoleAction(
						"Messages Console",
						SvnImages.ACTION_MESSAGES_CONSOLE_VIEW));
		// refrashOPDNumbers.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
		// 0)) ;

		viewMenu.add(this.refrashOPDNumbers);
		viewMenu.add(new JSeparator());
		viewMenu.add(showFileConsole);
		viewMenu.add(showAdminConsole);
		viewMenu.add(showMessagesConsole);

		// OPM Notation
		JMenu opmMenu;
		opmMenu = new JMenu("Notation");

		JMenu thingMenu = new JMenu("Thing");
		thingMenu.add(this.objectAction);
		thingMenu.add(this.stateAction);
		thingMenu.add(this.processAction);

		opmMenu.add(thingMenu);

		JMenu linkMenu = new JMenu("Link");
		linkMenu.add(this.inAction);
		linkMenu.add(this.agAction);
		linkMenu.add(this.reAction);
		linkMenu.add(this.effectAction);
		linkMenu.add(this.instrumentEventLinkAction);
		linkMenu.add(this.consumptionEventLinkAction);
		linkMenu.add(this.conditionAction);
		linkMenu.add(this.exceptionAction);
		linkMenu.add(this.invocationAction);

		opmMenu.add(linkMenu);

		JMenu relationMenu = new JMenu("Relation ");
		relationMenu.add(this.apAction);
		relationMenu.add(this.fcAction);
		relationMenu.add(this.gsAction);
		relationMenu.add(this.ciAction);
		relationMenu.add(this.unidirAction);
		relationMenu.add(this.bidirAction);
		opmMenu.add(relationMenu);

		// opmMenu.add(new JSeparator());

		// JMenu graphicObject = new JMenu("Graphic Object");
		//
		// tmpItem = new JMenuItem("Rectangle");
		// tmpItem.setEnabled(false);
		// graphicObject.add(tmpItem);
		//
		// tmpItem = new JMenuItem("Oval");
		// tmpItem.setEnabled(false);
		// graphicObject.add(tmpItem);
		//
		// tmpItem = new JMenuItem("Bubble");
		// tmpItem.setEnabled(false);
		// graphicObject.add(tmpItem);
		// opmMenu.add(graphicObject);

		// scaling menu
		JMenu operationsMenu = new JMenu("Operation");

		operationsMenu.add(this.unfoldingAction);
		operationsMenu.add(this.zoomInAction);
		operationsMenu.add(this.viewAction);
		operationsMenu.add(new JSeparator());
		operationsMenu.add(this.go2Testing);
		// operationsMenu.add(this.go2TestingTEstMoreThenOneAnimationScreen);

		// Ontology validation menu item
		operationsMenu.add(new JSeparator());
		operationsMenu.add(new JMenuItem(this.validationAction));

		// generation menu
		JMenu generationMenu;
		generationMenu = new JMenu("Generation");

		JMenu oplMenu = new JMenu("OPL");

		oplMenu.add(this.saveOPLAction);
		oplMenu.add(this.oplColorsAction);
		oplMenu.add(new JSeparator());
		ButtonGroup oplGroup = new ButtonGroup();
		isWholeProjectCompiled = false;
		JMenuItem oplTemp = new JRadioButtonMenuItem("OPL for current OPD",
				true);
		oplTemp.addActionListener(this.OPLCompileAction);
		oplGroup.add(oplTemp);
		oplMenu.add(oplTemp);
		oplTemp = new JRadioButtonMenuItem("OPL for whole System", false);
		oplTemp.addActionListener(this.OPLCompileAction);
		oplGroup.add(oplTemp);
		oplMenu.add(oplTemp);

		generationMenu.add(oplMenu);
		generationMenu.add(new JMenuItem(this.opl2opd));
		if (Features.hasCSVThingsLoading()) {
			generationMenu.add(new JMenuItem(this.csvLoadingAction));
		}

		if (Features.hasDeSecretOption()) {
			generationMenu.add(new JMenuItem(new DeSecretAction(
					"Desecret Project", null)));
		}
		generationMenu.add(new JSeparator());

		JMenu docMenu = new JMenu("Documents");
		docMenu.add(new JMenuItem(this.docAction));
		docMenu.add(new JMenuItem(this.templateAction));
		if (Features.hasCSV()) {
			docMenu.add(new JMenuItem(this.csvAction));
		}

		generationMenu.add(docMenu);

		generationMenu.add(new JMenuItem(this.umlAction));

		generationMenu.add(new JSeparator());
		generationMenu.add(new JMenuItem(this.generateOplXml));
		generationMenu.add(new JMenuItem(new GenerateSQL("Genrate SQL",
				StandardImages.EMPTY)));
		generationMenu.add(new JMenuItem(this.generateCode));

		// vis web
		// generationMenu.add(new JMenuItem(this.visWebActionA));
		// ************************** OWL-S Start
		// **********************************
		/*
		 * generationMenu.add(new JSeparator()); JMenu semWebMenu = new
		 * JMenu("SemanticWeb"); semWebMenu.add(new JMenuItem(semweb2opm));
		 * semWebMenu.add(new JMenuItem(opm2semweb));
		 * generationMenu.add(semWebMenu);
		 */
		// ************************** OWL-S End
		// **********************************
		/** **************************help menu**************************** */
		JMenu helpMenu;
		helpMenu = new JMenu("Help");
		helpMenu.add(this.helpContentsActionA);
		helpMenu.add(this.checkForUpdatesActionA);

		helpMenu.add(new JSeparator());

		// helpMenu.add(_constructExtensionToolsAboutMenu());

		helpMenu.add(this.helpAboutActionA);

		// add menus to menubar
		this.mainMenuBar.add(fileMenu);
		this.mainMenuBar.add(editMenu);
		this.mainMenuBar.add(viewMenu);
		this.mainMenuBar.add(opmMenu);
		this.mainMenuBar.add(operationsMenu);
		this.mainMenuBar.add(generationMenu);
		// LERA EXTENSION TOOL
		// debug = Debug.getInstance() ;
		// if (debug.IsDebug()) {
		// _loadExtensionTools();
		// mainMenuBar.add(_constractExtensionToolsMenu());
		// }

		this.mainMenuBar.add(helpMenu);

		/**
		 * init OpcatSvn
		 */
		OpcatMCManager.getInstance(true);

		return this.mainMenuBar;

	}

	public boolean getShowAdminConsoleState() {

		return (showAdminConsole != null) && showAdminConsole.getState();
	}

	public boolean getShowFileConsoleState() {
		return (showFileConsole != null) && showFileConsole.getState();
	}

	public boolean getShowMessagesConsoleState() {
		return (showMessagesConsole != null) && showMessagesConsole.getState();
	}

	// ==========================================================================================
	// create general toolbar
	private JToolBar _constructStandardToolBar() {
		this.standardToolBar = new JToolBar();
		this.standardToolBar.setName("Standard");
		this.standardToolBar.setFloatable(false);

		this.newButton = new JToolBarButton(this.newProjectActionA,
				"Create New Model");
		this.openButton = new JToolBarButton(this.loadOpxActionA, "Open Model");
		this.closeButton = new JToolBarButton(this.closeActionA, "Close Model");
		this.saveButton = new JToolBarButton(this.saveOpxActionA, "Save Model");
		this.printButton = new JToolBarButton(this.printQuickAction,
				"Print current OPD");
		this.searchButton = new JToolBarButton(this.SearchActionA, "Search");
		this.cutButton = new JToolBarButton(this.cutActionA,
				"Cut Object to Clipboard");
		this.copyButton = new JToolBarButton(this.copyActionA,
				"Copy Object to Clipboard");
		this.cloneButton = new JToolBarButton(this.cloneActionA, "Clone");

		JToolBarButton showExposed = new JToolBarButton(
				new OpcatExposeShowExposedList("Exposed Things List",
						ExposeImages.expose_list), "Exposed Things List");

		this.upButton = new JToolBarButton(this.upActionA, "Up");
		this.upButton.setToolTipText("Up");
		// this.downButton = new JToolBarButton(this.downActionA, "Down");
		this.backButton = new JToolBarButton(this.backActionA, "Back");
		this.backButton.setToolTipText("Back");
		this.forwardButton = new JToolBarButton(this.forwardActionA, "Forward");
		this.forwardButton.setToolTipText("Forward");

		this.projectSummary = new JToolBarButton(this.projectSummaryA,
				"Project Summary");
		this.projectSummary.setToolTipText("Show project Summary Report");

		this.copyFormatButton = new JToolBarButton(this.copyFormatActionA,
				"Copy Object Format");
		this.pasteButton = new JToolBarButton(this.pasteActionA,
				"Paste Object from Clipboard");
		undoButton = new JToolBarButton(this.undoActionA, "Undo Last Action");
		this.zoomInButton = new JToolBarButton(this.viewZoomInAction, "Zoom In");
		this.zoomOutButton = new JToolBarButton(this.viewZoomOutAction,
				"Zoom Out");

		this.importButton = new JToolBarButton(this.reuseImportActionA,
				"Import Model");
		this.metaLibsButton = new JToolBarButton(
				this.projectMetaLibrariesAction, "Handle Design Policies");

		this.metaLibsClasifficationButton = new JToolBarButton(
				this.projectMetaLibrariesClasifficationAction,
				"Classification Libraries");

		// validateButton = new JToolBarButton(validationAction, "Validate
		// Model");

		this.reqButton = new JToolBarButton(this.reqActionA,
				"Show Classification Libraries in Grid");

		this.dataFormButton = new JToolBarButton(this.dataFormAction,
				"Create Data Form");

		// this.testingButton = new JToolBarButton(this.testingAction,
		// "for testing");

		undoButton.setEnabled(false);
		redoButton = new JToolBarButton(this.redoActionA, "Redo Last Action");
		redoButton.setEnabled(false);
		this.deleteButton = new JToolBarButton(this.deleteActionA, "Delete");
		showHideExtensionTools = new JToolBarToggleButton(
				this.showExtensionToolsAction, "Hide Extension Tools",
				"Show Extension Tools");
		showHideExtensionTools.setSelected(true);

		moveOnDragButton = new JToolBarToggleButton(
				this.drawingAreaMouseDragActionMoves,
				"Move OPD Contents by Mouse Dragging",
				"Drag Drawing Area by Mouse dragging");
		selectOnDragButton = new JToolBarToggleButton(
				this.drawingAreaMouseDragActionSelects,
				"Select by Mouse Dragging", "Select by Mouse Dragging");
		selectOnDragButton.setSelected(true);

		// /** ***********HIOTeam************* */
		// drawOnDragButton = new JToolBarToggleButton(
		// this.drawingAreaMouseDragActionDraw, "Draw by Mouse Dragging",
		// "Draw by Mouse Dragging");
		//
		// this.moveSelectGroup.add(moveOnDragButton);
		// this.moveSelectGroup.add(selectOnDragButton);
		// this.moveSelectGroup.add(drawOnDragButton);
		// /** *********HIOTeam***************************** */

		this.standardToolBar.add(this.newButton);
		this.standardToolBar.add(this.openButton);
		this.standardToolBar.add(this.closeButton);
		this.standardToolBar.add(this.saveButton);

		this.standardToolBar.add(new JToolBar.Separator());
		this.standardToolBar.add(this.searchButton);

		this.standardToolBar.add(new JToolBar.Separator());
		this.standardToolBar.add(this.printButton);

		this.standardToolBar.add(new JToolBar.Separator());
		this.standardToolBar.add(this.importButton);
		this.standardToolBar.add(this.metaLibsButton);
		this.standardToolBar.add(this.metaLibsClasifficationButton);

		if (Features.hasReq()) {
			this.standardToolBar.add(this.reqButton);
		}

		this.standardToolBar.add(new JToolBar.Separator());
		this.standardToolBar.add(this.cutButton);
		this.standardToolBar.add(this.copyButton);
		// standardToolBar.add(cloneButton);
		this.standardToolBar.add(this.pasteButton);
		this.standardToolBar.add(undoButton);
		this.standardToolBar.add(redoButton);
		this.standardToolBar.add(this.deleteButton);
		this.standardToolBar.add(this.copyFormatButton);

		this.standardToolBar.add(new JToolBar.Separator());
		this.standardToolBar.add(showHideExtensionTools);
		this.standardToolBar.add(this.zoomInButton);
		this.standardToolBar.add(this.zoomOutButton);
		this.standardToolBar.add(new JToolBar.Separator());

		this.standardToolBar.add(showExposed);
		this.standardToolBar.add(new JToolBar.Separator());

		this.standardToolBar.add(selectOnDragButton);
		this.standardToolBar.add(moveOnDragButton);
		// this.standardToolBar.add(drawOnDragButton);
		/** ***************HIOTeam************************** */

		this.standardToolBar.add(new JToolBar.Separator());
		this.standardToolBar.add(new JToolBarButton(this.go2Testing,
				"Test System"));
		// this.standardToolBar.add(new JToolBarButton(
		// this.go2TestingTEstMoreThenOneAnimationScreen,
		// "Test the Test System"));

		this.standardToolBar.add(new JToolBar.Separator());
		this.standardToolBar.add(backButton);
		this.standardToolBar.add(upButton);
		// this.standardToolBar.add(downButton);
		this.standardToolBar.add(forwardButton);

		this.standardToolBar.add(new JToolBar.Separator());
		this.standardToolBar.add(projectSummary);

		this.standardToolBar.add(new JToolBar.Separator());
		this.standardToolBar.add(dataFormButton);

		this.standardToolBar.add(new JToolBar.Separator());
		this.standardToolBar.add(new JToolBar.Separator());
		// this.standardToolBar.add(testingButton);

		// standardToolBar.add(new SkinPanel()) ;

		this._setBorders4ToolBar(this.standardToolBar);

		return this.standardToolBar;

	}

	// create structual relation toolbar
	private JToolBar _constructComponentToolBar() {
		this.componentsToolBar = new JToolBar();

		this.componentsToolBar.setName("Components");
		this.componentsToolBar.setFloatable(false);

		this.componentsToolBar.add(new JToolBarButton(this.objectAction,
				"Object"));
		this.componentsToolBar
				.add(new JToolBarButton(this.stateAction, "State"));
		this.componentsToolBar.add(new JToolBarButton(this.processAction,
				"Process"));
		this.componentsToolBar.add(new JToolBar.Separator());
		this.componentsToolBar.add(new JToolBar.Separator());
		this.componentsToolBar.add(new JToolBarButton(this.apAction,
				"Aggregation-Participation"));
		this.componentsToolBar.add(new JToolBarButton(this.fcAction,
				"Exhibition-Characterization"));
		this.componentsToolBar.add(new JToolBarButton(this.gsAction,
				"Generalization-Specialization"));
		this.componentsToolBar.add(new JToolBarButton(this.ciAction,
				"Classification-Instantiation"));
		this.componentsToolBar.add(new JToolBarButton(this.unidirAction,
				"Unidirectional Relation"));
		this.componentsToolBar.add(new JToolBarButton(this.bidirAction,
				"Bidirectional Relation"));
		this.componentsToolBar.add(new JToolBar.Separator());
		this.componentsToolBar.add(new JToolBar.Separator());
		this.componentsToolBar.add(new JToolBarButton(this.agAction,
				"Agent Link"));
		this.componentsToolBar.add(new JToolBarButton(this.inAction,
				"Instrument Link"));
		this.componentsToolBar.add(new JToolBarButton(this.reAction,
				"Result/Consumption Link"));
		this.componentsToolBar.add(new JToolBarButton(this.effectAction,
				"Effect Link"));
		this.componentsToolBar.add(new JToolBarButton(
				this.instrumentEventLinkAction, "Instrument Event Link"));
		this.componentsToolBar.add(new JToolBarButton(
				this.consumptionEventLinkAction, "Consumption Event Link"));
		this.componentsToolBar.add(new JToolBarButton(this.conditionAction,
				"Condition Link"));
		this.componentsToolBar.add(new JToolBarButton(this.exceptionAction,
				"Exception Link"));
		this.componentsToolBar.add(new JToolBarButton(this.invocationAction,
				"Invocation Link"));
		this._setBorders4ToolBar(this.componentsToolBar);

		return this.componentsToolBar;
	}

	private void _setBorders4ToolBar(JToolBar bar) {
		for (int i = 0; i < bar.getComponentCount(); i++) {
			if (bar.getComponentAtIndex(i) instanceof JToolBarButton) {
				((JToolBarButton) bar.getComponentAtIndex(i))
						.setInitialBorder();
			} else if (bar.getComponentAtIndex(i) instanceof JToolBarToggleButton) {
				JToolBarToggleButton tButton = (JToolBarToggleButton) bar
						.getComponentAtIndex(i);
				tButton.setSelected(tButton.isSelected());
			}

		}

	}

	private JToolBar _constructTestingToolBar() {
		this.testingToolBar = new JToolBar();
		this.anPauseButton = new JToolBarToggleButton(this.anPause, "Continue",
				"Pause");

		JLabel l = new JLabel("Backward");
		Font f = l.getFont();
		int maxPanelsSize = 14 + (int) Math.max(f.getStringBounds("Forward",
				new FontRenderContext(null, true, false)).getWidth(), f
				.getStringBounds("Backward",
						new FontRenderContext(null, true, false)).getWidth());

		int maxPanelsHeigt = TestingImages.FORWARD.getIconHeight() + 5;

		JPanel anForwardPanel = new JPanel(new GridLayout(2, 1));
		anForwardPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEtchedBorder(), BorderFactory
						.createEmptyBorder(2, 5, 2, 5)));
		this.anForwardField = new JTextField("1", 3);
		this.anForwardField.setMaximumSize(this.anForwardField
				.getPreferredSize());
		this.anForwardField.setBorder(null);
		anForwardPanel.add(new JLabel("Forward"));
		anForwardPanel.add(this.anForwardField);
		anForwardPanel.setMaximumSize(new Dimension(maxPanelsSize,
				maxPanelsHeigt));

		JPanel anBackwardPanel = new JPanel(new GridLayout(2, 1));
		anBackwardPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEtchedBorder(), BorderFactory
						.createEmptyBorder(2, 5, 2, 5)));
		this.anBackwardField = new JTextField("1", 3);
		this.anBackwardField.setMaximumSize(this.anBackwardField
				.getPreferredSize());
		this.anBackwardField.setBorder(null);
		anBackwardPanel.add(new JLabel("Backward"));
		anBackwardPanel.add(this.anBackwardField);
		anBackwardPanel.setMaximumSize(new Dimension(maxPanelsSize,
				maxPanelsHeigt));

		this.testingToolBar
				.add(new JToolBarButton(this.anPlay, "Play/Continue"));
		this.testingToolBar
				.add(new JToolBarButton(this.anBackward, "Backward"));
		this.testingToolBar.add(anBackwardPanel);
		this.testingToolBar.add(new JToolBarButton(this.anForward, "Forward"));
		this.testingToolBar.add(anForwardPanel);
		this.testingToolBar.add(this.anPauseButton);
		this.testingToolBar.add(new JToolBarButton(this.anStop, "Stop"));
		this.testingToolBar.add(new JToolBar.Separator());
		this.testingToolBar
				.add(new JToolBarButton(this.anActivate, "Activate"));
		this.testingToolBar.add(new JToolBarButton(this.anDeactivate,
				"Deactivate"));
		this.testingToolBar.add(new JToolBarButton(this.anSettings,
				"Testing Settings"));
		this.testingToolBar.add(new JToolBar.Separator());

		this.anZoomInButton = new JToolBarButton(this.viewZoomInAction,
				"Zoom In");
		this.anZoomInButton.setIcon(TestingImages.ZOOM_IN);
		this.anZoomOutButton = new JToolBarButton(this.viewZoomOutAction,
				"Zoom Out");
		this.anZoomOutButton.setIcon(TestingImages.ZOOM_OUT);
		this.testingToolBar.add(this.anZoomInButton);
		this.testingToolBar.add(this.anZoomOutButton);

		this.testingToolBar.add(new JToolBar.Separator());
		this.testingToolBar.add(new JToolBarButton(this.closeTesting,
				"Exit Testing Mode"));

		this._setBorders4ToolBar(this.testingToolBar);
		return this.testingToolBar;
	}

	// ==========================================================================================
	// create Split pane
	private JSplitPane _constructSplitPane(Component left, Component right) {
		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);

		// sp.setDividerLocation((int)(myFrame.getHeight()*0.29));
		sp.setResizeWeight(0.0);

		sp.setOneTouchExpandable(true);
		return sp;
	}

	private JSplitPane _constructdesktopToolsPane(Component top,
			Component bottom) {
		JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, bottom);

		sp.setDividerLocation((int) (myFrame.getHeight() * 0.65));
		sp.setResizeWeight(1.0);
		sp.setOneTouchExpandable(true);
		return sp;
	}

	// ==========================================================================================
	// unirversal actions for all controls
	// ==========================================================================================

	/**
	 * Validates the current model, according to the attached ontology
	 * libraries.
	 * 
	 * @author Eran Toch
	 */
	Action validationAction = new AbstractAction("Templates Validation",
			StandardImages.VALIDATION_Menu) {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7567240516888546575L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Opcat2.this.validate();
		}
	};

	Action generateOplXml = new AbstractAction("OPL-XML script") {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5664248010827314245L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Opcat2.this.myFileChooser.setSelectedFile(new File(""));
			Opcat2.this.myFileChooser.resetChoosableFileFilters();
			String[] exts = { "xml" };
			CustomFileFilter myFilter = new CustomFileFilter(exts, "XML Files");
			Opcat2.this.myFileChooser.addChoosableFileFilter(myFilter);

			int retVal = Opcat2.this.myFileChooser.showDialog(
					Opcat2.getFrame(), "Extract OPL-XML");

			if (retVal != JFileChooser.APPROVE_OPTION) {
				return;
			}

			Thread runner = new Thread() {
				public void run() {
					try {

						JProgressBar pBar = new JProgressBar();
						pBar.setIndeterminate(true);
						Opcat2.this.showWaitMessage(
								"Extracting OPL-XML script...", pBar);
						StateMachine.setWaiting(true);
						Opcat2.this.setCursor4All(Cursor.WAIT_CURSOR);

						String fileName = Opcat2.this.myFileChooser
								.getSelectedFile().getPath();
						if (!fileName.endsWith(".xml")) {
							fileName = fileName + ".xml";
						}

						File f = new File(fileName);
						FileOutputStream os = new FileOutputStream(f);
						os.write(currentProject.getOplXmlScript().toString()
								.getBytes());
						os.close();

					} catch (IOException ioe) {
					} finally {
						Opcat2.this.hideWaitMessage();
						StateMachine.reset();
						StateMachine.setWaiting(false);
						Opcat2.this.setCursor4All(Cursor.DEFAULT_CURSOR);

					}
				}
			};

			runner.start();

		}

	};

	Action printQuickAction = new AbstractAction("Print", StandardImages.PRINT) {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7597874673647616437L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Thread runner = new Thread() {
				public void run() {
					currentProject.print(false, false);
				}
			};
			runner.start();
		}
	};

	Action printAction = new AbstractAction("Print", StandardImages.PRINT) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2549877156835261513L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Thread runner = new Thread() {
				public void run() {
					currentProject.print(true, false);
				}
			};
			runner.start();
		}
	};

	Action pageSetupAction = new AbstractAction("Page Setup",
			StandardImages.PAGE_SETUP) {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5401329124923150955L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Thread runner = new Thread() {
				public void run() {
					currentProject.pageSetup();
				}
			};
			runner.start();
		}
	};

	Action printPreviewAction = new AbstractAction("Print Preview",
			StandardImages.PRINT_PREVIEW) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6750379746054726413L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Thread runner = new Thread() {
				public void run() {
					currentProject.print(true, true);
					Opcat2.myFrame.repaint();
				}
			};
			runner.start();
		}
	};

	Action projectPropertiesAction = new AbstractAction("Properties",
			StandardImages.PROPERTIES) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7036536439929889185L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}

			ProjectPropertiesDialog ppd = new ProjectPropertiesDialog(
					currentProject, myFrame, "System Properties");
			boolean okPressed = ppd.showDialog();

			// Handling Meta-librares (ontologies) import
			// by Eran Toch
			if (okPressed) {
				Opcat2.this.refreshMetaLibraries(ppd.getMetaLibraries());

				GuiControl gui = GuiControl.getInstance();
				gui.getRepository().rebuildTrees(true);
			}
		}
	};

	Action projectMetaLibrariesClasifficationAction = new AbstractAction(
			"Classification Meta Data", StandardImages.Classification) {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1478908579731553136L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				LibrariesLoadingWindow importedLibraries = new LibrariesLoadingWindow(
						Opcat2.getCurrentProject().getMetaManager(), Opcat2
								.getCurrentProject(), false, false, Opcat2
								.getFrame());
				boolean ret = importedLibraries.showDialog();
				if (ret) {
					refreshMetaLibraries(importedLibraries.getUpdatedLibs());
				}
			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			}

		}
	};

	Action projectMetaLibrariesAction = new AbstractAction("Templates",
			StandardImages.Policies) {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1478908579731553136L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				LibrariesLoadingWindow importedLibraries = new LibrariesLoadingWindow(
						Opcat2.getCurrentProject().getMetaManager(), Opcat2
								.getCurrentProject(), false, true, Opcat2
								.getFrame());
				boolean ret = importedLibraries.showDialog();
				if (ret) {
					Vector<MetaLibrary> updated = importedLibraries
							.getUpdatedLibs();
					refreshMetaLibraries(updated);
				}
			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			}

		}
	};

	Action viewZoomInAction = new AbstractAction("Zoom in",
			StandardImages.ZOOM_IN) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 724046729267803394L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Opcat2.getCurrentProject().getCurrentOpd().setViewZoomIn(false);

			GenericTable config = currentProject.getConfiguration();
			int previousSize = ((Integer) config.getProperty("CurrentSize"))
					.intValue();
			int normalSize = ((Integer) config.getProperty("NormalSize"))
					.intValue();

			if (previousSize < normalSize + 15) {
				int currentSize = previousSize + 1;
				double factor = (double) currentSize / (double) previousSize;
				config.setProperty("CurrentSize", new Integer(currentSize));
				currentProject.viewZoomIn(factor);
				Opcat2.this._enableZoomOut(true);

				if (currentSize == normalSize + 15) {
					Opcat2.this._enableZoomIn(false);
				}
			}
		}
	};

	Action viewZoomOutAction = new AbstractAction("Zoom out",
			StandardImages.ZOOM_OUT) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2590784430441524993L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Opcat2.getCurrentProject().getCurrentOpd().setViewZoomIn(false);

			GenericTable config = currentProject.getConfiguration();
			int previousSize = ((Integer) config.getProperty("CurrentSize"))
					.intValue();
			int normalSize = ((Integer) config.getProperty("NormalSize"))
					.intValue();

			if (previousSize > normalSize - 4) {
				int currentSize = previousSize - 1;
				double factor = (double) currentSize / (double) previousSize;
				config.setProperty("CurrentSize", new Integer(currentSize));
				currentProject.viewZoomIn(factor);

				Opcat2.this._enableZoomIn(true);
				if (currentSize == normalSize - 4) {
					Opcat2.this._enableZoomOut(false);
				}

			}

		}
	};

	Action unfoldingAction = new AbstractAction("Unfolding",
			StandardImages.EMPTY) {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7976670690259606898L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				getGlassPane().setVisible(true);
				getGlassPane().start();

				currentProject.unfolding();

				getGlassPane().stop();
			} catch (Exception e1) {
				System.out.println(e1);
			}
		}
	};

	Action viewAction = new AbstractAction("Create View", StandardImages.EMPTY) {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				getGlassPane().setVisible(true);
				getGlassPane().start();

				currentProject.createView();

				getGlassPane().stop();
			} catch (Exception e1) {
				System.out.println(e1);
			}
		}
	};

	Action zoomInAction = new AbstractAction("In-Zooming", StandardImages.EMPTY) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6470859190024978803L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			getGlassPane().setVisible(true);
			getGlassPane().start();

			try {

				currentProject.zoomIn();
			} catch (Exception e1) {
				OpcatLogger.logError(e1);
			}

			getGlassPane().stop();
		}
	};

	// /** *start*****HIOTeam*** */
	// Action drawAction = new AbstractAction("drawing", StandardImages.PEN) {
	//
	// /**
	// *
	// */
	// private static final long serialVersionUID = 5587451960866063043L;
	//
	// public void actionPerformed(ActionEvent e) {
	// if (!Opcat2.this.projectWasOpened || (currentProject == null)) {
	//
	// moveOnDragButton.setSelected(false);
	// drawOnDragButton.setSelected(false);
	// selectOnDragButton.setSelected(true);
	//
	// JOptionPane.showMessageDialog(myFrame, "No system is opened",
	// "Message", JOptionPane.ERROR_MESSAGE);
	//
	// return;
	// }
	// drawingAreaOnMouseDragAction = "draw";
	// moveOnDragButton.setSelected(false);
	// selectOnDragButton.setSelected(false);
	// drawOnDragButton.setSelected(true);
	// StateMachine.reset();
	// if (currentProject.getCurrentOpd() == null) {
	// return;
	// }
	// Graphics g = currentProject.getCurrentOpd().getDrawingArea()
	// .getGraphics();
	// Graphics2D g2 = (Graphics2D) g;
	// if (drawing == null) {
	// try {
	// drawing = new DrawAppNew(g2, currentProject);
	// } catch (Exception e1) {
	// // System.out.println("can't create drawing");
	// }
	// }
	//
	// }
	// };
	//
	// /** **start******HIOTeam******** */
	// Action drawingAreaMouseDragActionDraw = new AbstractAction("draw",
	// StandardImages.PEN) {
	//
	// /**
	// *
	// */
	// private static final long serialVersionUID = 3802858545940378057L;
	//
	// public void actionPerformed(ActionEvent e) {
	//
	// if (!Opcat2.this.projectWasOpened || (currentProject == null)) {
	// JOptionPane.showMessageDialog(myFrame, "No system is opened",
	// "Message", JOptionPane.ERROR_MESSAGE);
	// moveOnDragButton.setSelected(false);
	// selectOnDragButton.setSelected(true);
	// drawOnDragButton.setSelected(false);
	//
	// return;
	// }
	// drawingAreaOnMouseDragAction = "draw";
	// moveOnDragButton.setSelected(false);
	// selectOnDragButton.setSelected(false);
	// drawOnDragButton.setSelected(true);
	// StateMachine.reset();
	// if (currentProject.getCurrentOpd() == null) {
	// return;
	// }
	// Graphics g = currentProject.getCurrentOpd().getDrawingArea()
	// .getGraphics();
	// Graphics2D g2 = (Graphics2D) g;
	// if (drawing == null) {
	// try {
	// drawing = new DrawAppNew(g2, currentProject);
	// } catch (Exception e1) {
	// JOptionPane.showMessageDialog(myFrame,
	// "The Drawing Application could not be initialized",
	// "Message", JOptionPane.ERROR_MESSAGE);
	// }
	// }
	//
	// return;
	//
	// }
	// };

	/** **end*****HIOTeam******** */
	Action SearchActionA = new AbstractAction("Search", StandardImages.SEARCH) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3925967089695033621L;

		public void actionPerformed(ActionEvent e) {

			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			SearchExtensionTool searchTool = new SearchExtensionTool();
			searchTool.execute((IXSystem) currentProject);
		}
	};

	Action showExtensionToolsAction = new AbstractAction("Show OPL",
			StandardImages.HIDE_TOOLS) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6032099129237893599L;

		public void actionPerformed(ActionEvent e) {
			if (!showHideExtensionTools.isSelected()) {
				Opcat2.this.desktopTools.setBottomComponent(null);
				Opcat2.this.desktopTools.setDividerSize(0);
			} else {
				Opcat2.this.desktopTools
						.setBottomComponent(Opcat2.this.extensionToolsPane);
				Opcat2.this.desktopTools.setDividerSize(10);
				Opcat2.this.desktopTools.setDividerLocation((int) (myFrame
						.getHeight() * 0.63));
				Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
			}

		}
	};

	Action go2Testing = new AbstractAction("Test System",
			StandardImages.ANIMATE) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 457605027497827709L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}

			GridPanel.updateColor(null);
			MetaColoringAction colorLevel = new MetaColoringAction("dummy",
					null, StandardImages.META_COLOR_DEF);
			colorLevel.actionPerformed(null);

			MetaHideAction hide = new MetaHideAction("dummy", null, false,
					StandardImages.System_Icon);
			hide.actionPerformed(null);

			StateMachine.setAnimated(true);
			if (showHideExtensionTools.isSelected()) {
				// desktopTools.setBottomComponent(null);
				// desktopTools.setDividerSize(0);
			}

			for (int i = 0; i < Opcat2.this.mainMenuBar.getMenuCount(); i++) {
				Opcat2.this.mainMenuBar.getMenu(i).setVisible(false);
			}
			// viewZoomInAction.set
			Opcat2.this.anPauseButton.setSelected(false);
			Opcat2.this.mainMenuBar.setEnabled(false);
			myFrame.getContentPane().remove(Opcat2.this.standardToolBar);
			myFrame.getContentPane().add(Opcat2.this.testingToolBar,
					BorderLayout.NORTH);
			Opcat2.this.anBackwardField.setText("1");
			Opcat2.this.anForwardField.setText("1");
			Opcat2.this.anStatusLabel = new TestingStatusBar();
			Opcat2.this.anStatusLabel.setBorder(BorderFactory
					.createLoweredBevelBorder());
			Opcat2.this.anStatusLabel.setFont(new Font("mine", Font.PLAIN, 16));
			Opcat2.this.diagramPanel.remove(Opcat2.this.componentsToolBar);
			Opcat2.this.diagramPanel.add(Opcat2.this.anStatusLabel,
					BorderLayout.SOUTH);
			Opcat2.this.anSystem = new TestingSystem(currentProject,
					(TestingStatusBar) Opcat2.this.anStatusLabel);
			myFrame.validate();
			myFrame.repaint();
		}
	};

	Action go2TestingTEstMoreThenOneAnimationScreen = new AbstractAction(
			"Test System", StandardImages.ANIMATE) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 457605027497827709L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}

			GridPanel.updateColor(null);
			MetaColoringAction colorLevel = new MetaColoringAction("dummy",
					null, StandardImages.META_COLOR_DEF);
			colorLevel.actionPerformed(null);

			MetaHideAction hide = new MetaHideAction("dummy", null, false,
					StandardImages.System_Icon);
			hide.actionPerformed(null);

			StateMachine.setAnimated(true);
			if (showHideExtensionTools.isSelected()) {
				// desktopTools.setBottomComponent(null);
				// desktopTools.setDividerSize(0);
			}

			for (int i = 0; i < Opcat2.this.mainMenuBar.getMenuCount(); i++) {
				Opcat2.this.mainMenuBar.getMenu(i).setVisible(false);
			}
			// viewZoomInAction.set
			Opcat2.this.anPauseButton.setSelected(false);
			Opcat2.this.mainMenuBar.setEnabled(false);
			myFrame.getContentPane().remove(Opcat2.this.standardToolBar);
			myFrame.getContentPane().add(Opcat2.this.testingToolBar,
					BorderLayout.NORTH);
			Opcat2.this.anBackwardField.setText("1");
			Opcat2.this.anForwardField.setText("1");
			Opcat2.this.anStatusLabel = new TestingStatusBar();
			Opcat2.this.anStatusLabel.setBorder(BorderFactory
					.createLoweredBevelBorder());
			Opcat2.this.anStatusLabel.setFont(new Font("mine", Font.PLAIN, 16));
			Opcat2.this.diagramPanel.remove(Opcat2.this.componentsToolBar);
			Opcat2.this.diagramPanel.add(Opcat2.this.anStatusLabel,
					BorderLayout.SOUTH);
			MetaLibrary meta = (MetaLibrary) currentProject.getMetaManager()
					.getMetaLibraries().nextElement();
			OpdProject project = (OpdProject) meta.getProjectHolder();
			project.showOPD(((Opd) project.getSystemStructure().getOpds()
					.nextElement()).getOpdId());
			Opcat2.this.anSystem = new TestingSystem(project,
					(TestingStatusBar) Opcat2.this.anStatusLabel);
			myFrame.validate();
			myFrame.repaint();
		}
	};

	public Action closeTesting = new AbstractAction("Close",
			TestingImages.CLOSE) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3388581642408821883L;

		public void actionPerformed(ActionEvent e) {
			GridPanel.updateColor(null);
			MetaColoringAction colorLevel = new MetaColoringAction("dummy",
					null, StandardImages.META_COLOR_DEF);
			colorLevel.actionPerformed(null);

			MetaHideAction hide = new MetaHideAction("dummy", null, false,
					StandardImages.System_Icon);
			hide.actionPerformed(null);

			boolean tempLifeSpan = TestingSettings.getInstance()
					.isSHOW_LIFE_SPAN();
			boolean tempREsource = TestingSettings.getInstance()
					.isSHOW_RESOURCE();
			TestingSettings.getInstance().setSHOW_RESOURCE(false);
			TestingSettings.getInstance().setSHOW_LIFE_SPAN(false);
			Opcat2.this.anSystem.testingStop();
			TestingSettings.getInstance().setSHOW_LIFE_SPAN(tempLifeSpan);
			TestingSettings.getInstance().setSHOW_RESOURCE(tempREsource);

			// Remove the gridpanel

			Opcat2.this.anSystem.getTestingPanel()
					.RemoveFromExtensionToolsPanel();

			// anSystem.getLifeSpanGrid().RemoveFromExtensionToolsPanel() ;
			// anSystem.getObjectsScenrios().RemoveFromExtensionToolsPanel()
			// ;
			// anSystem.getBreakPointsGrid().RemoveFromExtensionToolsPanel();
			// anSystem.getTestingLogPanel().RemoveFromExtensionToolsPanel();

			anSystem = null;

			StateMachine.setAnimated(false);
			// componentsToolBar.setVisible(true);

			for (int i = 0; i < Opcat2.this.mainMenuBar.getMenuCount(); i++) {
				Opcat2.this.mainMenuBar.getMenu(i).setVisible(true);
			}

			Opcat2.this.mainMenuBar.setEnabled(true);

			myFrame.getContentPane().remove(Opcat2.this.testingToolBar);
			myFrame.getContentPane().add(Opcat2.this.standardToolBar,
					BorderLayout.NORTH);

			Opcat2.this.diagramPanel.remove(Opcat2.this.anStatusLabel);
			Opcat2.this.diagramPanel.add(Opcat2.this.componentsToolBar,
					BorderLayout.SOUTH);

			if (showHideExtensionTools.isSelected()) {
				Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
			}
			myFrame.validate();
			myFrame.repaint();

			// Viewer or not ? defined at compile time
			boolean viewer = false; // do not change this line auto frm
			// compiler
			if (viewer)
				StateMachine.setAnimated(viewer);
		}
	};

	Action anPlay = new AbstractAction("Play", TestingImages.PLAY) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 85473341585933863L;

		public void actionPerformed(ActionEvent e) {
			GridPanel.updateColor(null);
			MetaColoringAction colorLevel = new MetaColoringAction("dummy",
					null, StandardImages.META_COLOR_DEF);
			colorLevel.actionPerformed(null);

			MetaHideAction hide = new MetaHideAction("dummy", null, false,
					StandardImages.System_Icon);
			hide.actionPerformed(null);

			// //!anSystem.isTestingON() ||
			if (!Opcat2.this.anSystem.isTestingPaused()) {
				Opcat2.this.anSystem.testingStart();
			} else {
				Opcat2.this.anSystem.testingContinue();
				Opcat2.this.anPauseButton.setSelected(false);
			}

		}
	};

	Action anStop = new AbstractAction("Stop testing", TestingImages.STOP) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8763689823234065805L;

		public void actionPerformed(ActionEvent e) {
			GridPanel.updateColor(null);
			MetaColoringAction colorLevel = new MetaColoringAction("dummy",
					null, StandardImages.META_COLOR_DEF);
			colorLevel.actionPerformed(null);

			MetaHideAction hide = new MetaHideAction("dummy", null, false,
					StandardImages.System_Icon);
			hide.actionPerformed(null);

			Opcat2.this.anSystem.testingStop();

			Opcat2.this.anPauseButton.setSelected(false);
		}
	};

	Action anForward = new AbstractAction("Forward", TestingImages.FORWARD) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6734309390725539002L;

		public void actionPerformed(ActionEvent e) {
			GridPanel.updateColor(null);
			MetaColoringAction colorLevel = new MetaColoringAction("dummy",
					null, StandardImages.META_COLOR_DEF);
			colorLevel.actionPerformed(null);

			MetaHideAction hide = new MetaHideAction("dummy", null, false,
					StandardImages.System_Icon);
			hide.actionPerformed(null);
			try {
				Opcat2.this.anSystem.testingForward(Integer.decode(
						Opcat2.this.anForwardField.getText()).intValue());
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(myFrame,
						"Number of forward steps must be integer", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	};

	Action anBackward = new AbstractAction("Backward", TestingImages.BACKWARD) {

		/**
		 * 
		 */
		private static final long serialVersionUID = -717364374449343049L;

		public void actionPerformed(ActionEvent e) {
			GridPanel.updateColor(null);
			MetaColoringAction colorLevel = new MetaColoringAction("dummy",
					null, StandardImages.META_COLOR_DEF);
			colorLevel.actionPerformed(null);

			MetaHideAction hide = new MetaHideAction("dummy", null, false,
					StandardImages.System_Icon);
			hide.actionPerformed(null);
			try {
				Opcat2.this.anSystem.testingBackward(Integer.decode(
						Opcat2.this.anBackwardField.getText()).intValue());
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(myFrame,
						"Number of backward steps must be integer", "Error",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	};

	Action anPause = new AbstractAction("Pause", TestingImages.PAUSE) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6849776112109576431L;

		public void actionPerformed(ActionEvent e) {
			GridPanel.updateColor(null);
			MetaColoringAction colorLevel = new MetaColoringAction("dummy",
					null, StandardImages.META_COLOR_DEF);
			colorLevel.actionPerformed(null);

			MetaHideAction hide = new MetaHideAction("dummy", null, false,
					StandardImages.System_Icon);
			hide.actionPerformed(null);
			if (!Opcat2.this.anSystem.isTestingPaused()) {
				Opcat2.this.anSystem.testingPause();
			} else {
				Opcat2.this.anSystem.testingContinue();
			}

		}
	};

	Action anActivate = new AbstractAction("Activate", TestingImages.ACTIVATE) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3484091198340502325L;

		public void actionPerformed(ActionEvent e) {
			GridPanel.updateColor(null);
			MetaColoringAction colorLevel = new MetaColoringAction("dummy",
					null, StandardImages.META_COLOR_DEF);
			colorLevel.actionPerformed(null);

			MetaHideAction hide = new MetaHideAction("dummy", null, false,
					StandardImages.System_Icon);
			hide.actionPerformed(null);
			Opcat2.this.anSystem.testingActivate();
		}
	};

	Action anDeactivate = new AbstractAction("Deactivate",
			TestingImages.DEACTIVATE) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7511099493151747477L;

		public void actionPerformed(ActionEvent e) {
			GridPanel.updateColor(null);
			MetaColoringAction colorLevel = new MetaColoringAction("dummy",
					null, StandardImages.META_COLOR_DEF);
			colorLevel.actionPerformed(null);

			MetaHideAction hide = new MetaHideAction("dummy", null, false,
					StandardImages.System_Icon);
			hide.actionPerformed(null);
			Opcat2.this.anSystem.testingDeactivate();
		}
	};

	Action anSettings = new AbstractAction("Testing Settings",
			TestingImages.SETTINGS) {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4461026515245661348L;

		public void actionPerformed(ActionEvent e) {
			(new TestingSettingsWindow()).setVisible(true);
		}
	};

	Action oplColorsAction = new AbstractAction("OPL Style") {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3365203554223586058L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					OplColorsDialog ocd = new OplColorsDialog(Opcat2.myFrame,
							"OPL Style", currentProject.getOplColorScheme());
					ocd.setVisible(true);
				}
			});

		}
	};

	Action opl2opd = new AbstractAction("OPD") {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7083119903529671146L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Opl2Opd tDialog = new Opl2Opd(currentProject);
			tDialog.getDialog().setVisible(true);

			if (tDialog.isChanged()) {
				Opcat2.getUndoManager().discardAllEdits();
				Opcat2.setRedoEnabled(false);
				Opcat2.setUndoEnabled(false);
			}

		}
	};

	Action umlAction = new AbstractAction("UML") {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8201791432963325353L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// UMLmain tmp = new UMLmain(currentProject);
			// tmp.getDialog().show();
			(new UMLChooser(currentProject)).setVisible(true);
		}
	};

	Action docAction = new AbstractAction("Handle Document") {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1771997016114402494L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// DocumentMain tmp = new DocumentMain(currentProject);
			// tmp.getDocumentDialog().show();
			(new DocSel(currentProject)).setVisible(true);

		}
	};

	Action templateAction = new AbstractAction("Handle Template") {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6516797803382364991L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// DocumentMain tmp = new DocumentMain(currentProject);
			// tmp.getTemplateDialog().show();
			(new HandleTemp(currentProject)).setVisible(true);
		}
	};

	Action csvAction = new AbstractAction("Export to CSV") {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5662264367978336974L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// DocumentMain tmp = new DocumentMain(currentProject);
			// tmp.getTemplateDialog().show();
			Document.PrintCSVFile();
		}
	};

	Action csvLoadingAction = new AbstractAction("Import Things CSV") {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5662264367978336974L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}

			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(Opcat2.getFrame());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File csv_file = fc.getSelectedFile();
				if (csv_file != null) {
					LoadThingsCSV loader = new LoadThingsCSV(csv_file);
					loader.load();
				}
			}

		}
	};

	Action drawingAreaMouseDragActionSelects = new AbstractAction(
			"Select by Dragging", StandardImages.POINTER) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5637367531831345993L;

		public void actionPerformed(ActionEvent e) {
			drawingAreaOnMouseDragAction = "select";
			moveOnDragButton.setSelected(false);
			// drawOnDragButton.setSelected(false);
			selectOnDragButton.setSelected(true);
			StateMachine.reset();
		}
	};

	Action drawingAreaMouseDragActionMoves = new AbstractAction(
			"Move by Dragging", StandardImages.HAND) {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1552745727576522111L;

		public void actionPerformed(ActionEvent e) {
			drawingAreaOnMouseDragAction = "move";
			selectOnDragButton.setSelected(false);
			// drawOnDragButton.setSelected(false);
			moveOnDragButton.setSelected(true);
			StateMachine.reset();
			// moveSelectGroup.setSelected(moveOnDragButton.getModel(),
			// true);
		}
	};

	Action saveOPLAction = new AbstractAction("Save OPL As...") {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6019793467442024249L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Opcat2.this.myFileChooser.setSelectedFile(new File(""));
			Opcat2.this.myFileChooser.resetChoosableFileFilters();
			Opcat2.this.myFileChooser.setFileFilter(new CustomFileFilter(
					"html", "HTML Files"));
			int returnVal = Opcat2.this.myFileChooser.showSaveDialog(myFrame);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {

					String s;

					if (isWholeProjectCompiled) {
						s = currentProject.getOPL(true);
					} else {
						Opd currentOpd = currentProject.getCurrentOpd();
						if (currentOpd == null) {
							s = EMPTY_HTML;
						} else {
							s = currentProject.getOPL(true, currentOpd
									.getOpdId());
						}
					}

					FileOutputStream file = new FileOutputStream(
							Opcat2.this.myFileChooser.getSelectedFile()
									.getAbsolutePath());
					// file.write(oplPane.getText().getBytes());
					file.write(s.getBytes());
					file.flush();
					file.close();
				} catch (java.io.IOException IOE) {
					System.out.println("IOException " + IOE);
				}
			}
		}
	};

	ActionListener OPLCompileAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {

			String command = e.getActionCommand();

			if (command.equals("OPL for current OPD")) {
				isWholeProjectCompiled = false;
			} else {
				isWholeProjectCompiled = true;
			}
			Opcat2.oplPane.setCaretPosition(0);

			Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);

		}
	};

	Action refreshAction = new AbstractAction("Refresh", StandardImages.REFRESH) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7163536110917081981L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}

			currentProject.refresh();
			Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
			Opcat2.myFrame.repaint();
		}
	};

	Action generateCode = new AbstractAction("Java Code") {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5332772446749038992L;

		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Opcat2.this.myFileChooser.setSelectedFile(new File(""));
			Opcat2.this.myFileChooser.resetChoosableFileFilters();
			Opcat2.this.myFileChooser
					.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int retVal = Opcat2.this.myFileChooser.showDialog(
					Opcat2.getFrame(), "Generate Java Code");
			Opcat2.this.myFileChooser
					.setFileSelectionMode(JFileChooser.FILES_ONLY);

			if (retVal != JFileChooser.APPROVE_OPTION) {
				return;
			}
			Thread runner = new Thread() {
				public void run() {
					StateMachine.setWaiting(true);
					Opcat2.this.setCursor4All(Cursor.WAIT_CURSOR);

					JProgressBar pBar = new JProgressBar();
					pBar.setIndeterminate(true);
					Opcat2.this.showWaitMessage("Generating Code...", pBar);

					File selectedDirectory = Opcat2.this.myFileChooser
							.getSelectedFile();
					/* START Coping opmTypes directory */
					/* LERA */
					try {
						File opmDir = new File(selectedDirectory, "opmTypes");
						if (!opmDir.exists()) {
							opmDir.mkdir();
							File opmType_ = new File(opmTypes);
							File[] files = opmType_.listFiles();
							for (int i = 0; i < files.length; i++) {
								if (!files[i].getAbsolutePath()
										.endsWith(".svn")) {
									FileCopy.copy(files[i].getAbsolutePath(),
											opmDir.getAbsolutePath());
								}
							}
						} else {
							JOptionPane
									.showMessageDialog(
											myFrame,
											"Code Generator Message: Using existing opmTypes Directory and Files",
											"Info",
											JOptionPane.INFORMATION_MESSAGE);
						}
					} catch (IOException e) {
						JOptionPane.showMessageDialog(myFrame,
								"Code Generator Error: I/O error in types copying\\"
										+ e.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (Exception ex) {
						JOptionPane
								.showMessageDialog(
										myFrame,
										"Code Generator Error: opm types copying failed",
										"Error", JOptionPane.ERROR_MESSAGE);
					}

					translator.ITranslator engine = new translator.Engine(
							new String[] {});

					try {
						engine.loadLanguage(new FileInputStream(Configuration
								.getInstance().getProperty("opcat_home")
								+ fileSeparator
								+ "codeGenerator"
								+ fileSeparator + "opl2Java.xml"));
						engine
								.setOutputPREProcessor(new FileInputStream(
										Configuration.getInstance()
												.getProperty("opcat_home")
												+ fileSeparator
												+ "codeGenerator"
												+ fileSeparator
												+ "transform.java.xsl"));
						engine.translateOPDS(new ByteArrayInputStream(
								currentProject.getOplXmlScript().toString()
										.getBytes()), selectedDirectory, true,
								System.out);

					} catch (translator.TranslationException te) {
						Opcat2.this.hideWaitMessage();
						StateMachine.setWaiting(false);
						Opcat2.this.setCursor4All(Cursor.DEFAULT_CURSOR);
						JOptionPane.showMessageDialog(myFrame, te.getMessage(),
								"Error", JOptionPane.ERROR_MESSAGE);

					} catch (FileNotFoundException fnfe) {
						Opcat2.this.hideWaitMessage();
						StateMachine.setWaiting(false);
						Opcat2.this.setCursor4All(Cursor.DEFAULT_CURSOR);
						JOptionPane.showMessageDialog(myFrame, fnfe
								.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} finally {
						Opcat2.this.hideWaitMessage();
						StateMachine.setWaiting(false);
						Opcat2.this.setCursor4All(Cursor.DEFAULT_CURSOR);
					}

				}
			};

			runner.start();

		}
	};

	ActionListener extensionToolsListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (!Opcat2.this.projectWasOpened) {
				JOptionPane.showMessageDialog(myFrame, "No system is opened",
						"Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			JPanel panel = null;
			String str = e.getActionCommand();

			ExtensionToolInfo eti = (ExtensionToolInfo) Opcat2.this.extensionsTable
					.get(str);

			if (eti != null) {
				for (int i = 1; i < Opcat2.this.extensionToolsPane
						.getTabCount(); i++) {

					if (Opcat2.this.extensionToolsPane.getTitleAt(i).equals(
							eti.getName())) {
						Opcat2.this.extensionToolsPane.setSelectedIndex(i);
						return;
					}
				}

				try {
					if (eti.getExtension() instanceof OpcatExtensionToolX) {
						panel = ((OpcatExtensionToolX) eti.getExtension())
								.execute((IXSystem) currentProject);
					} else {
						panel = ((OpcatExtensionTool) eti.getExtension())
								.execute((ISystem) currentProject);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				if (panel != null) {
					Opcat2.this.extensionToolsPane.add(eti.getName(), panel);
					Opcat2.this.extensionToolsPane.setSelectedComponent(panel);
				}
			} else {
				JOptionPane.showMessageDialog(myFrame, "No '" + str
						+ "' Extension Tool could be found.", "Opcat2 - Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	};

	ActionListener extensionToolsListenerAbout = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String str = e.getActionCommand();
			ExtensionToolInfo eti = (ExtensionToolInfo) Opcat2.this.extensionsTable
					.get(str);
			JPanel aboutPanel = null;
			if (eti != null) {
				aboutPanel = eti.getExtension().getAboutBox();

				if (aboutPanel != null) {
					Opcat2.this.aboutDlg = new JDialog(Opcat2.getFrame(), true);
					Opcat2.this.aboutDlg.setTitle("About " + eti.getName());
					Opcat2.this.aboutDlg.getContentPane().setLayout(
							new BorderLayout());
					Opcat2.this.aboutDlg.getContentPane().add(aboutPanel);
					JButton tButton = new JButton("Close");
					JPanel tp = new JPanel(new GridLayout(1, 5));
					tp
							.setBorder(BorderFactory.createEmptyBorder(0, 10,
									10, 10));
					tp.add(Box.createHorizontalGlue());
					tp.add(Box.createHorizontalGlue());
					tp.add(tButton);
					tp.add(Box.createHorizontalGlue());
					tp.add(Box.createHorizontalGlue());
					tButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							Opcat2.this.aboutDlg.dispose();
						}
					});

					Opcat2.this.aboutDlg.getContentPane().add(tp,
							BorderLayout.SOUTH);
					Opcat2.this.aboutDlg
							.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

					Opcat2.this.aboutDlg.pack();

					int fX = Opcat2.getFrame().getX();
					int fY = Opcat2.getFrame().getY();
					int pWidth = Opcat2.getFrame().getWidth();
					int pHeight = Opcat2.getFrame().getHeight();
					Opcat2.this.aboutDlg.setLocation(fX
							+ Math.abs(pWidth / 2
									- Opcat2.this.aboutDlg.getWidth() / 2), fY
							+ Math.abs(pHeight / 2
									- Opcat2.this.aboutDlg.getHeight() / 2));

					Opcat2.this.aboutDlg.setVisible(true);
				}
			}
		}
	};

	ActionListener tBarslst = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JToolBar tmpBar = Opcat2.this.standardToolBar;

			for (int i = 0; i < Opcat2.this.toolbars.length; i++) {
				if (Opcat2.this.toolbars[i].getName().equals(
						e.getActionCommand())) {
					tmpBar = Opcat2.this.toolbars[i];
				}
			}

			tmpBar.setVisible(!tmpBar.isVisible());
		}
	};

	//
	// ActionListener lst = new ActionListener() {
	// public void actionPerformed(ActionEvent e) {
	// String str = e.getActionCommand();
	// // if (obj != null)
	// if (str != null) {
	// try {
	// // String className = (String) obj;
	// // Class lnfClass = Class.forName(className);
	//
	// // UIManager.setLookAndFeel((LookAndFeel) (lnfClass
	// // .newInstance()));
	//
	// OpcatSkinManager skinma = OpcatSkinManager.instance();
	// skinma.setTheme(str);
	//
	// SwingUtilities.updateComponentTreeUI(myFrame);
	// Opcat2.this._setBorders4ToolBar(Opcat2.this.testingToolBar);
	// Opcat2.this
	// ._setBorders4ToolBar(Opcat2.this.standardToolBar);
	// Opcat2.this
	// ._setBorders4ToolBar(Opcat2.this.componentsToolBar);
	//
	// repository.getOutlookBar().setBold();
	//
	// } catch (Exception ex) {
	// OpcatLogger.logError(ex);
	// }
	// }
	// }
	// };

	// ************************** OWL-S Start
	// **********************************
	/*
	 * Action semweb2opm = new AbstractAction("SemanticWeb to OPM") { public
	 * void actionPerformed(ActionEvent e) { if (!projectWasOpened) {
	 * JOptionPane.showMessageDialog(myFrame, "No system is opened", "Message",
	 * JOptionPane.ERROR_MESSAGE); return; }
	 * 
	 * (new SemWebToOpm(currentProject, currentProject)).show(); } };
	 * 
	 * Action opm2semweb = new AbstractAction("OPM to OWL-S") { public void
	 * actionPerformed(ActionEvent e) { if (!projectWasOpened) {
	 * JOptionPane.showMessageDialog(myFrame, "No system is opened", "Message",
	 * JOptionPane.ERROR_MESSAGE); return; }
	 * 
	 * (new OpmToSemWeb(currentProject, currentProject)).show(); } };
	 */
	// ************************** OWL-S End
	// **********************************
	// ==========================================================================================
	// end unirversal actions for all controls
	// ==========================================================================================
	private void _createSplashScreen() {
		Calendar rightNow = new GregorianCalendar();
		int year = rightNow.get(Calendar.YEAR);
		if (year < 2004) {
			year = 2004;
		}
		String copyrightString = " OPCAT Systems, All rights reserved (c) 2005-2008 \n";
		this.splashLabel = new SplashLabel(copyrightString, MiscImages.SPLASH
				.getImage());
		this.splashLabel.setPreferredSize(new Dimension(370, 253));
		this.splashLabel.setForeground(new Color(44, 71, 148));
		this.splashLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.splashLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		this.splashScreen = new JWindow();
		this.splashScreen.getContentPane().setLayout(new BorderLayout());
		JPanel bottomPanel = new JPanel(new BorderLayout());
		this.splashScreen.getContentPane().add(this.splashLabel,
				BorderLayout.CENTER);
		this.jpb = new JProgressBar();
		this.jpb.setForeground(new Color(37, 119, 197));
		this.jpb.setString("Loading... Please wait");
		this.jpb.setStringPainted(true);
		this.jpb.setMinimum(0);
		this.jpb.setMaximum(18);
		this.jpb.setValue(0);
		bottomPanel.add(this.jpb, BorderLayout.CENTER);
		JLabel licenseLabel = new JLabel(Opcat2.getLicense()
				.getLicenseCaption());
		licenseLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bottomPanel.add(licenseLabel, BorderLayout.SOUTH);
		this.splashScreen.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		this.splashScreen.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.splashScreen.setLocation(screenSize.width / 2
				- this.splashScreen.getSize().width / 2, screenSize.height / 2
				- this.splashScreen.getSize().height / 2);
	}

	// private void _loadClasses() {
	// OpdProject tProject = new OpdProject(desktop, 1);
	// jpb.setValue(++progressCounter);
	// Opd tOpd = new Opd(tProject, "tempName", 1, null);
	// tProject.getComponentsStructure().put(1, tOpd);
	// tProject.setCurrentOpd(tOpd);
	//
	// jpb.setValue(++progressCounter);
	//
	// jpb.setValue(++progressCounter);
	// jpb.setValue(++progressCounter);
	// jpb.setValue(++progressCounter);
	// jpb.setValue(++progressCounter);
	// jpb.setValue(++progressCounter);
	// jpb.setValue(++progressCounter);
	// jpb.setValue(++progressCounter);
	//
	// jpb.setValue(++progressCounter);
	// jpb.setValue(++progressCounter);
	//
	// }

	public void setProjectActionEnable(boolean isEnable) {
		this.newProjectActionA.setEnabled(!isEnable);

		this.closeActionA.setEnabled(isEnable);
		this.saveOpxActionA.setEnabled(isEnable);

		this.projectPropertiesAction.setEnabled(isEnable);
		this.printAction.setEnabled(isEnable);
		this.cutActionA.setEnabled(isEnable);
		this.copyActionA.setEnabled(isEnable);
		this.cloneActionA.setEnabled(isEnable);

		this.upActionA.setEnabled(isEnable);
		// this.downActionA.setEnabled(isEnable);
		this.backActionA.setEnabled(isEnable);
		this.forwardActionA.setEnabled(isEnable);

		this.copyFormatActionA.setEnabled(isEnable);
		this.pasteActionA.setEnabled(isEnable);
		this.undoActionA.setEnabled(myUndoManager.canUndo());
		this.redoActionA.setEnabled(myUndoManager.canRedo());
		this.deleteActionA.setEnabled(isEnable);
		this.viewZoomInAction.setEnabled(isEnable);
		this.viewZoomOutAction.setEnabled(isEnable);
		this.objectAction.setEnabled(isEnable);

		this.processAction.setEnabled(isEnable);
		this.inAction.setEnabled(isEnable);
		this.agAction.setEnabled(isEnable);
		this.reAction.setEnabled(isEnable);
		this.effectAction.setEnabled(isEnable);
		this.instrumentEventLinkAction.setEnabled(isEnable);
		this.conditionAction.setEnabled(isEnable);
		this.exceptionAction.setEnabled(isEnable);
		this.invocationAction.setEnabled(isEnable);
		this.apAction.setEnabled(isEnable);
		this.fcAction.setEnabled(isEnable);
		this.gsAction.setEnabled(isEnable);
		this.ciAction.setEnabled(isEnable);
		this.unidirAction.setEnabled(isEnable);
		this.bidirAction.setEnabled(isEnable);
		this.unfoldingAction.setEnabled(isEnable);
		this.viewAction.setEnabled(true);
		this.zoomInAction.setEnabled(isEnable);
		this.viewZoomOutAction.setEnabled(isEnable);

		this.saveOPLAction.setEnabled(isEnable);
		this.newButton.setEnabled(!isEnable);
		this.openButton.setEnabled(!isEnable);
		this.closeButton.setEnabled(isEnable);
		this.saveButton.setEnabled(isEnable);
		this.printButton.setEnabled(isEnable);
		this.searchButton.setEnabled(isEnable);
		this.cutButton.setEnabled(isEnable);
		this.copyButton.setEnabled(isEnable);
		this.cloneButton.setEnabled(isEnable);

		this.upButton.setEnabled(isEnable);
		// this.downButton.setEnabled(isEnable);
		this.backButton.setEnabled(isEnable);
		this.forwardButton.setEnabled(isEnable);
		this.projectSummary.setEnabled(true);

		this.copyFormatButton.setEnabled(isEnable);
		this.pasteButton.setEnabled(isEnable);
		undoButton.setEnabled(myUndoManager.canUndo());
		redoButton.setEnabled(myUndoManager.canRedo());
		this.deleteButton.setEnabled(isEnable);
	}

	public static JFrame getFrame() {
		return myFrame;
	}

	public static UndoManager getUndoManager() {
		return myUndoManager;
	}

	public static void setUndoEnabled(boolean flag) {
		undoButton.setEnabled(flag);
		undoItem.setEnabled(flag);
	}

	public static void setRedoEnabled(boolean flag) {
		redoButton.setEnabled(flag);
		redoItem.setEnabled(flag);
	}

	public static boolean isUndoEnabled() {
		return undoButton.isEnabled();
	}

	public static boolean isRedoEnabled() {
		return undoButton.isEnabled();
	}

	/**
	 * function to updateStructurechange with an option not to update the OPL.
	 * this is usful when the object is not presened on the screen but generated
	 * automaticly.
	 * 
	 * @param kindOfChange
	 * @param withOPL
	 *            - is OPL to be generated ?
	 */
	public static void updateStructureChange(int kindOfChange) {

		if ((currentProject == null)
				|| (kindOfChange == Opcat2.GRAPHICAL_CHANGE)
				|| StateMachine.isAnimated()) {
			return;
		}

		if (currentProject.isPresented()) {

			if (kindOfChange == Opcat2.OPD_CHANGE) {
				if (showHideExtensionTools.isSelected()
						&& !isWholeProjectCompiled) {
					Opd currentOpd = currentProject.getCurrentOpd();
					try {
						if (currentOpd == null) {
							oplPane.setText(EMPTY_HTML);
						} else {
							oplPane.setText(currentProject.getOPL(true,
									currentOpd.getOpdId()));
						}
					} catch (Exception ex) {
						OpcatLogger.logError(ex);
					}
					oplPane.setCaretPosition(0);
				}

				return;
			}

			// Logical change
			if (kindOfChange == Opcat2.LOGICAL_CHANGE_TREE)
				repository.rebuildTrees(true);
			if (!showHideExtensionTools.isSelected()) {
				return;
			}

			int currentPosition = oplPane.getCaretPosition();
			try {
				if (isWholeProjectCompiled) {
					oplPane.setText(currentProject.getOPL(true));
				} else {
					Opd currentOpd = currentProject.getCurrentOpd();
					if ((currentOpd == null)
							|| (currentOpd.getOpdId() == OpdProject.CLIPBOARD_ID)) {
						oplPane.setText(EMPTY_HTML);
					} else {
						oplPane.setText(currentProject.getOPL(true, currentOpd
								.getOpdId()));
					}
				}

				oplPane.setCaretPosition(Math.min(currentPosition, oplPane
						.getText().length() - 1));
			} catch (java.lang.IllegalArgumentException e) {
				OpcatLogger.logError(e);
			}
		}
	}

	public void _enableZoomOut(boolean yn) {
		this.zoomOutButton.setEnabled(yn);
		this.anZoomOutButton.setEnabled(yn);
		this.viewZoomOutAction.setEnabled(yn);
	}

	public void _enableZoomIn(boolean yn) {
		this.zoomInButton.setEnabled(yn);
		this.anZoomInButton.setEnabled(yn);
		this.viewZoomInAction.setEnabled(yn);
	}

	// private JMenu _constractExtensionToolsMenu() {
	// JMenu extensionsMenu = new JMenu("Extension Tools");
	// JMenuItem menuItem;
	// Enumeration locenum = extensionsTable.elements();
	// ExtensionToolInfo eti = null;
	// extensionsMenu.setEnabled(true);
	//
	// for (; locenum.hasMoreElements();) {
	// extensionsMenu.setEnabled(true);
	// eti = (ExtensionToolInfo) locenum.nextElement();
	// menuItem = new JMenuItem(eti.getName());
	// menuItem.addActionListener(extensionToolsListener);
	// extensionsMenu.add(menuItem);
	// }
	// return extensionsMenu;
	// }

	// private JMenu _constructExtensionToolsAboutMenu() {
	// JMenu extensionsMenu = new JMenu("About Extension Tools");
	// extensionsMenu.setIcon(StandardImages.EMPTY);
	// JMenuItem menuItem;
	// Enumeration locenum = extensionsTable.elements();
	// ExtensionToolInfo eti = null;
	// extensionsMenu.setEnabled(true);
	//
	// for (; locenum.hasMoreElements();) {
	// eti = (ExtensionToolInfo) locenum.nextElement();
	// if (eti.getExtension().getAboutBox() != null) {
	// extensionsMenu.setEnabled(true);
	// menuItem = new JMenuItem(eti.getName());
	// menuItem.addActionListener(extensionToolsListenerAbout);
	// extensionsMenu.add(menuItem);
	// }
	// }
	// return extensionsMenu;
	// }

	// private void _loadExtensionTools() {
	// Enumeration locEnum = ExtensionToolsInstaller.getExtensionTools();
	// ExtensionToolInfo eti = null;
	// extensionsTable = new Hashtable();
	//
	// for (; locEnum.hasMoreElements();) {
	// eti = (ExtensionToolInfo) locEnum.nextElement();
	// extensionsTable.put(eti.getName(), eti);
	// }
	// }

	public static String getDrawingAreaOnMouseDragAction() {
		return drawingAreaOnMouseDragAction;
	}

	private void setCursor4All(final int cursorType) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				myFrame.setCursor(Cursor.getPredefinedCursor(cursorType));
				oplPane.setCursor(Cursor.getPredefinedCursor(cursorType));

				if (currentProject != null) {
					for (Enumeration e = currentProject
							.getComponentsStructure().getOpds(); e
							.hasMoreElements();) {
						Opd tempOpd = (Opd) e.nextElement();
						tempOpd.getDrawingArea().setCursor(
								Cursor.getPredefinedCursor(cursorType));
					}
				}
			}
		});

	}

	public void showWaitMessage(String message, JProgressBar pBar) {
		this.waitScreen = new JWindow(myFrame);
		this.handleWaitScreen(this.waitScreen, message, pBar);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Opcat2.this.waitScreen.setVisible(true);
			}
		});

	}

	private void hideWaitMessage() {
		if (this.waitScreen == null) {
			return;
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Opcat2.this.waitScreen.dispose();
			}
		});

	}

	public void handleWaitScreen(JWindow theScreen, String message,
			JProgressBar pBar) {
		JLabel msgLabel = new JLabel(message);
		msgLabel.setBorder(new EmptyBorder(25, 30, 25, 30));
		msgLabel.setForeground(new Color(0, 51, 102));
		msgLabel.setFont(new Font("Arial", Font.BOLD, 16));

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(msgLabel, BorderLayout.CENTER);
		panel.add(pBar, BorderLayout.SOUTH);

		panel.setBorder(new LineBorder(new Color(0, 51, 102), 1, true));
		panel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		theScreen.getContentPane().setLayout(new BorderLayout());
		theScreen.getContentPane().add(panel, BorderLayout.CENTER);
		pBar.setForeground(new Color(7, 65, 122));
		pBar.setBorder(new LineBorder(new Color(0, 51, 102)));
		pBar.setFont(new Font("Arial", Font.BOLD, 14));

		theScreen.pack();
		theScreen.setLocation(myFrame.getWidth() / 2
				- theScreen.getSize().width / 2, myFrame.getHeight() / 2
				- theScreen.getSize().height / 2);
	}

	private void showMetaWaitMessage(String message, JProgressBar pBar) {
		if (this.metaWaitScreen == null) {
			this.metaWaitScreen = new JWindow(myFrame);
		}

		this.handleWaitScreen(this.metaWaitScreen, message, pBar);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Opcat2.this.metaWaitScreen.setVisible(true);
			}
		});
	}

	private void hideMetaWaitMessage() {
		if (this.metaWaitScreen == null) {
			return;
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Opcat2.this.metaWaitScreen.dispose();
			}
		});

	}

	/**
	 * Handles situations when the system was already opened, and a new system
	 * should be opened. The method closes the old system, if a system is
	 * opened.
	 * 
	 * @return <code>false</code> if the user cancelled the operation.
	 *         <code>true</code> otherwise.
	 */

	public static void main(String[] args) {
		Opcat2 op = null;

		try {
			op = new Opcat2();
		} catch (Exception exc) {
			OpcatLogger.logError(exc);
		}
		// If Opcat recieves a file name as input, it tries to load it
		if ((op != null) && (args.length > 0)) {
			loadedfilename = args[0].toString();
			op.getFileControl().loadOpxFile(args[0].toString(), true);

			if (args.length == 2) {
				try {
					String idString = args[1];
					long id = Long.parseLong(idString);
					Entry entry = op.getFileControl().getCurrentProject()
							.getSystemStructure().getEntry(id);
					if (entry != null) {
						OpcatExposeShowEntity show = new OpcatExposeShowEntity(
								"none", null, entry.getInstances()
										.nextElement());
						show.actionPerformed(null);
					}

				} catch (Exception ex) {
					OpcatLogger.logError(ex);
				}
			}

		}
	}

	/**
	 * Checks wheather there is a need to save the system before closing it. The
	 * method cheks if the undo manager has any changes or the
	 * <code>systemChangeProbability</code> variable is set to <code>true</code>
	 * .
	 * 
	 * @return <code>true</code> if it's possible to exit without changes.
	 *         <code>false</code> otherwise.
	 * @see UndoManager
	 * @see #getSystemChangeProbability
	 */
	public boolean canExitWithoutSave() {
		return myUndoManager.canCloseProject();
	}

	/**
	 * Returns the <code>showProcessNameMessage</code> flag. <code>true</code>
	 * means that the user is interested in displaying annoying messages
	 * regarding the "ing" ending in the process name.
	 */
	public static boolean getShowProcessNameMessage() {
		return showProcessNameMessage;
	}

	public Repository getRepository() {
		return repository;
	}

	/* added by YG */
	public JDesktopPane getDesktop() {
		return this.desktop;
	}

	/**
	 * returns the current {@link OpdProject} asociated with this {@link Opcat2}
	 * GUI. there could be only one current project in this GUI
	 * 
	 * @return {@link OpdProject}
	 */
	public static OpdProject getCurrentProject() {
		return currentProject;
	}

	/**
	 * sets the current {@link OpdProject} of this GUI.
	 * 
	 * @param {@link IXSystem newCurrentProject}
	 * 
	 */
	public void setCurrentProject(IXSystem newCurrentProject) {
		currentProject = (OpdProject) newCurrentProject;
	}

	/* added by YG */

	public void setProjectOpened(boolean rc) {
		this.projectWasOpened = rc;
	}

	public boolean isProjectOpened() {
		return this.projectWasOpened;
	}

	/**
	 * Handles the meta-libraries assigned to this project. The method laods the
	 * libraries and handles the changes done to the list in the
	 * <code>ProjectPropertiesDialog</code>.
	 * 
	 * @param metaLibraries
	 *            A Vector containing <code>MetaLibrary</code> objects. If null,
	 *            the method will return without carrying out any operation.
	 */
	public void refreshMetaLibraries(Vector metaLibraries) {
		if (metaLibraries == null) {
			return;
		}
		currentProject.getMetaManager().setLibrariesVector(metaLibraries);
		JProgressBar pBar = new JProgressBar();
		this.showMetaWaitMessage("Loading meta-data...", pBar);
		try {
			currentProject.getMetaManager().refresh(currentProject, pBar);
		} finally {
			this.hideMetaWaitMessage();
		}
		if (currentProject.getMetaManager().containFailed()) {
			LibrariesLoadingWindow libsLoad = new LibrariesLoadingWindow(
					currentProject.getMetaManager(), currentProject, myFrame);
			if (libsLoad.showDialog()) {
				this.refreshMetaLibraries(libsLoad.getUpdatedLibs());
			}
			/**
			 * JOptionPane.showMessageDialog( Opcat2.getFrame(),
			 * currentProject.getMetaManager().getFailedMessage(), "Opcat2 -
			 * Error", JOptionPane.INFORMATION_MESSAGE);
			 */
		}
	}

	/**
	 * Removes the validation window.
	 * 
	 * @author Eran Toch BUG: the index might not always be 1
	 */
	public void clearValidationWindow() {
		int indexOfValidation = this.extensionToolsPane
				.indexOfTab(Validator.VALIDATION_TAG_NAME);
		if (indexOfValidation > 0) {
			try {
				this.extensionToolsPane.remove(indexOfValidation);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Validates the model against its meta-libraries.
	 * 
	 * @author Eran Toch
	 */
	public void validate() {
		if (this.validator == null) {
			this.validator = new Validator();
		}
		JPanel validPanel = this.validator.execute(currentProject, Opcat2.this,
				myFrame);
		int indexOfValidation = this.extensionToolsPane
				.indexOfTab(Validator.VALIDATION_TAG_NAME);
		if (indexOfValidation > 0) {
			try {
				this.extensionToolsPane.remove(indexOfValidation);
			} catch (Exception ex) {
			}
		}
		this.extensionToolsPane.add(Validator.VALIDATION_TAG_NAME, validPanel);
		this.extensionToolsPane.setIconAt(this.extensionToolsPane
				.indexOfTab(Validator.VALIDATION_TAG_NAME),
				StandardImages.VALIDATION);
		this.extensionToolsPane.setSelectedComponent(validPanel);
	}

	/**
	 * Sets the CHECKED image on the <code>setEnableDraggingButton</code> menu
	 * item.
	 * 
	 * @param doEnable
	 *            If <code>true</code>, the option would be displayed as
	 *            <code>CHECKED</code>. Otherwise, it would be displayed as
	 *            <code>EMPTY</code>.
	 */
	// public void enableDragging(boolean doEnable) {
	// if (doEnable) {
	// draggingButton.setIcon(StandardImages.CHECKED);
	// } else {
	// draggingButton.setIcon(StandardImages.EMPTY);
	// }
	// }
	// endReuseComment
	/**
	 * Performs clean-up operations when a project is closed down.
	 */
	public void closeCleanUp() {

		if (currentProject != null)
			currentProject.close();

		// Merge remarked
		// enableDragging(false); //reuse
		Opcat2.getFrame().setTitle("Opcat II");
		for (int i = 0; i < getDesktop().getAllFrames().length; i++) {
			getDesktop().getAllFrames()[i].setVisible(false);
			getDesktop().getAllFrames()[i] = null;
		}
		repository.clearHistory();
		repository.setProject(null);
		currentProject = null;
		oplPane.setText(EMPTY_HTML);
		GridPanel.RemoveALLPanels();
		System.gc();
		this.projectWasOpened = false;

		//getRepository().getModelsView().setRefrashState(View_RefreshState.ALL);
		//getRepository().rebuildTrees(true);

	}

	/**
	 * Displays a message to the user.
	 * 
	 * @param title
	 *            The title of the windows ("Error")
	 * @param text
	 *            The text of the message ("Code generator cannot work, becuase
	 *            there is not enough memory")
	 * @param type
	 *            The type of the message, according to JOptionPane vocabulary
	 *            (JOptionPane.ERROR_MESSAGE)
	 */
	public static void showMessage(String title, String text, int type) {
		JOptionPane.showMessageDialog(myFrame, text, title, type);
	}

	/**
	 * Returns the license class.
	 */
	public static ILicense getLicense() {
		return license;
	}

	/**
	 * Loads the license code from the license file.
	 * 
	 * @return
	 */
	public ILicense loadLicense() {
		ILicense lic = null;
		Properties props = new Properties();
		boolean isError = false;
		try {
			FileInputStream fis = new FileInputStream(FileControl.getInstance()
					.getOPCATDirectory()
					+ FileControl.fileSeparator + "license.lic");
			props.load(fis);
			String code = props.getProperty("code");
			lic = LicenseFactory.createLicense(code);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			isError = true;
		} catch (IOException e1) {
			e1.printStackTrace();
			isError = true;
		}
		if (isError) {
			lic = LicenseFactory.createDefaultLicense();
		}
		return lic;
	}

	/**
	 * @return the opl pane.
	 */
	public static JTextPane getOplPane() {
		return oplPane;
	}

	/**
	 * @return Returns the waitScreen.
	 */
	public JWindow getWaitScreen() {
		return this.waitScreen;
	}

	/**
	 * @param waitScreen
	 *            The waitScreen to set.
	 */
	public void setWaitScreen(JWindow waitScreen) {
		this.waitScreen = waitScreen;
	}

	/**
	 * @return Returns the projectWasOpened.
	 */
	public boolean isProjectWasOpened() {
		return this.projectWasOpened;
	}

	/**
	 * @param projectWasOpened
	 *            The projectWasOpened to set.
	 */
	public void setProjectWasOpened(boolean projectWasOpened) {
		this.projectWasOpened = projectWasOpened;
	}

	/**
	 * @return Returns the metaWaitScreen.
	 */
	public JWindow getMetaWaitScreen() {
		return this.metaWaitScreen;
	}

	/**
	 * @param metaWaitScreen
	 *            The metaWaitScreen to set.
	 */
	public void setMetaWaitScreen(JWindow metaWaitScreen) {
		this.metaWaitScreen = metaWaitScreen;
	}

	/**
	 * Returns the Opcat undo manager.
	 */
	public OpcatUndoManager getOpcatUndoManager() {
		return myUndoManager;
	}

	/**
	 * @param myFileChooser
	 *            The myFileChooser to set.
	 */
	public void setMyFileChooser(JFileChooser myFileChooser) {
		this.myFileChooser = myFileChooser;
	}

	/**
	 * @return Returns the extensionToolsPane.
	 */
	public JTabbedPane getExtensionToolsPane() {
		return this.extensionToolsPane;
	}

	public void setCurrentProject(OpdProject newCurrentProject) {
		currentProject = newCurrentProject;
	}

	public FileControl getFileControl() {
		return this.fileControl;
	}

	/**
	 * @return Returns the isWholeProjectCompiled.
	 */
	public boolean isWholeProjectCompiled() {
		return isWholeProjectCompiled;
	}

	/**
	 * @param isWholeProjectCompiled
	 *            The isWholeProjectCompiled to set.
	 */
	public void setWholeProjectCompiled(boolean isWholeProjectCompiled) {
		Opcat2.isWholeProjectCompiled = isWholeProjectCompiled;
	}

	public void refreshMenuBar() {
		// if (myFrame != null) {
		myFrame.setJMenuBar(this._constructMenuBar());
		try {
			myFrame.validate();
		} catch (Exception ex) {
			//
		}
		myFrame.repaint();
		// }
	}

	/**
	 * Handles the redo/undo buttons appearence, after they were change by the
	 * user.
	 * 
	 * @param isUndo
	 *            If the parameter is <code>true</code>, then the operation will
	 *            set the buttons to their state after an undo. Otherwise, it
	 *            would be set to the state after redo.
	 */
	public void toggleUndoButtons(boolean isUndo) {
		if (isUndo) {
			undoButton.setEnabled(myUndoManager.canUndo());
			undoButton.setToolTipText(myUndoManager.getUndoPresentationName());
			redoButton.setEnabled(myUndoManager.canRedo());
			redoButton.setToolTipText(myUndoManager.getRedoPresentationName());
			undoItem.setEnabled(myUndoManager.canUndo());
			redoItem.setEnabled(myUndoManager.canRedo());
		} else {
			undoButton.setEnabled(myUndoManager.canUndo());
			redoButton.setEnabled(myUndoManager.canRedo());
			undoItem.setEnabled(myUndoManager.canUndo());
			redoItem.setEnabled(myUndoManager.canRedo());
		}
	}

	public static String getLoadedfilename() {
		re                                                                                                                                                                                                                                                                                                                                                          