package extensionTools.uml.umlDiagrams;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.ILinkEntry;
import exportedAPI.opcatAPI.IObjectEntry;
import exportedAPI.opcatAPI.IObjectInstance;
import exportedAPI.opcatAPI.IProcessEntry;
import exportedAPI.opcatAPI.IRelationEntry;
import exportedAPI.opcatAPI.ISystem;
import extensionTools.uml.userInterface.UserDesicion;
import gui.util.OpcatException;
import gui.util.OpcatLogger;

/**
 * Creates all UML diagrams choosed by the user. Differences between StarUML and Poseidon: 1. <UML:Diagram.element> is required by StarUML. Rejected
 * by Poseidon. 2. An upper <UML:Model> tag is required by StarUML. Rejected by Poseidon.
 */
public class DiagramsCreator {

	public static final String UML_NAMESPACE = "http://org.omg/UML/1.3";

	public static final String XMI_VERSION = "1.2";

	public static final String MAIN_MODEL_NAME = "UML_Model_1";

	Enumeration baseEnum = null;

	XmiWriter writer = null;

	public DiagramsCreator() {
	}

	ISystem mySys;

	private int optionClass = -1;

	private int optionUseCase = -1;

	private int levelUseCase = -1;

	private int optionSequence = -1;

	private Vector vecSq = new Vector(4, 2);

	private int optionStatechart = -1;

	private Vector vecSt = new Vector(4, 2);

	private int optionActivity = -1;

	private int levA = -1;

	private Vector vecA = new Vector(4, 2);

	private int optionDeployment = -1;

	Vector usecaseVec = new Vector(15, 2);

	Vector usecaseIncludeVec = new Vector(10, 2);

	Vector usecaseInzoomIncludeVec = new Vector(10, 2);

	Vector usecaseSterIncludeVec = new Vector(10, 2);

	Vector usecaseExtendVec = new Vector(10, 2);

	Vector<String> actorTagsVec = new Vector<String>(15, 2);

	Vector usecaseassVec = new Vector(15, 2);

	Vector usecaseassEndVec = new Vector(15, 2);

	Vector usecasespecVec = new Vector(10, 2);

	Vector<String> tagsVec = new Vector<String>(15, 2);

	Vector<String> classDiagramElements = new Vector<String>(20, 2);

	Vector assVec = new Vector(10, 2);

	Vector assEndVec = new Vector(10, 2);

	Vector specVec = new Vector(10, 2);

	// --------------------------for deployment
	Vector deplPrVec = new Vector(4, 2);

	Vector deplRelVec = new Vector(4, 2);

	// ----------------------------------------------------------
	// for activity
	Vector actDiagramsVec = new Vector(2, 2);

	Vector ActivityPrint = new Vector(2, 2);

	Vector TransitionPrint = new Vector(2, 2);

	Vector BranchPrint = new Vector(2, 2);

	Vector ForkPrint = new Vector(2, 2);

	Vector JoinPrint = new Vector(2, 2);

	Vector FinalPrint = new Vector(2, 2);

	Vector InitialPrint = new Vector(2, 2);

	// -----------------------------------------------------------
	// for statechart
	Vector StateSimplePrint = new Vector(1, 1);

	Vector TransitionStatePrint = new Vector(1, 1);

	Vector stateDiagramsOut = new Vector(1, 1);

	Vector finalStatePrint = new Vector(1, 1);

	Vector initialStatePrint = new Vector(1, 1);

	// -----------------------------------------------------------
	// for sequence
	Vector ClassifierRolePrint = new Vector(4, 2);

	Vector AssociationPrint = new Vector(4, 2);

	Vector seqEntryVec = new Vector(4, 2);

	private HashMap<Long,IObjectInstance> classInstances = null;
	
	int dest1;

	int dest2;

	int dest3;

	int dest4;

	int destSeq;
	
	private ClassDiagram classDiagram = new ClassDiagram(baseEnum);
	private UseCaseDiagram usecaseDiagram = new UseCaseDiagram();
	private Statechart_d statechart = new Statechart_d();
	private sequence_d sequence = new sequence_d();
	private Deployment_d deployment = new Deployment_d();
	private Activity_d activity = new Activity_d();

	/**
	 * Increces the 4 geometric parameters, used in external printing.
	 */
	private void dest() {
		this.dest1 = this.dest1 + 200;
		this.dest2 = this.dest2 + 200;
		this.dest3 = this.dest3 + 200;
		this.dest4 = this.dest4 + 200;
		this.destSeq = this.destSeq + 350;
		if ((this.dest1 > 8000) || (this.dest2 > 8000) || (this.dest3 > 8000) || (this.dest4 > 8000) || (this.destSeq > 8000)) {
			this.initialize_dest();
		}
		return;
	}

	/**
	 * Initializes the 4 geometric parameters, used in external printing.
	 */
	private void initialize_dest() {
		this.dest1 = 48;
		this.dest2 = 34;
		this.dest3 = 24;
		this.dest4 = 18;
		this.destSeq = 20;
	}

	/**
	 * Creates the XMI file, and generates the general tags.
	 * 
	 * @param ISystem
	 *            sys
	 * @param String
	 *            filename
	 */
	public void generateXmiFile(ISystem sys, String filename, UserDesicion desicion) throws OpcatException {
		try {
			this.mySys = sys;
			writer = new XmiWriter(filename);
			String temp = "";

			temp = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?> \n";
			temp += "<XMI xmlns:UML=\"" + UML_NAMESPACE + "\" xmi.version=\"" + XMI_VERSION + "\" timestamp=\"Sun Jun 02 15:46:18 2002\"> \n";
			temp += "\t<XMI.header>\n";
			temp += "\t\t<XMI.documentation>\n";
			temp += "\t\t\t<XMI.exporter>Opcat</XMI.exporter>\n";
			temp += "\t\t</XMI.documentation>\n";
			temp += "\t\t<XMI.metamodel xmi.name=\"UML\" xmi.version=\"1.3\" />\n";
			temp += "\t</XMI.header>\n";
			temp += "\t<XMI.content>\n";
			writer.write(temp.getBytes());

			// -------class-------------
			if (desicion.UClassYes) {
				optionClass = 1;
			}
			// -------use case-------------
			if (desicion.UURoot) {
				optionUseCase = 1;
				levelUseCase = 1;
			}
			if (desicion.UUAll) {
				optionUseCase = 2;
				levelUseCase = 0;
			}
			if (desicion.UUNone) {
				optionUseCase = -1;
				levelUseCase = 0;
			}
			if (desicion.UUUntil) {
				optionUseCase = 1;
				levelUseCase = Integer.parseInt(desicion.UUUntilText);
			}
			// -------statechart-------------
			if (desicion.UStateYes) {
				optionStatechart = 1;
				int selected[] = desicion.UStateList;
				int i = 0;
				while (i < selected.length) {
					vecSt.addElement(desicion.v_obj.get(selected[i]));
					i++;
				}
			}
			// -------sequence-------------
			if (desicion.USeqYes) {
				optionSequence = 1;
				int selected[] = desicion.USeqList;
				int i = 0;
				while (i < selected.length) {
					vecSq.addElement(desicion.v_proc.get(selected[i]));
					i++;
				}
			}
			// -------activity-------------
			if (desicion.UMLActTop) {
				optionActivity = 1;
				levA = Integer.parseInt(desicion.UMLActTopText);
			}
			if (desicion.UMLActElse) {
				optionActivity = 2;// by processes
				int selected[] = desicion.UMLActList;
				int i = 0;
				while (i < selected.length) {
					vecA.addElement(desicion.v_act.get(selected[i]));
					i++;
				}
				if (desicion.UMLActListAll) {
					levA = 1000;
				} else {
					levA = Integer.parseInt(desicion.UMLActListNum);
				}
			}
			// -------deployment-------------
			if (desicion.UDepYes) {
				optionDeployment = 1;
			}

			generateAllDiagrams();
			generateGraphicalData();

			temp = "\t</XMI.content>\n";
			temp += "</XMI>\n";
			writer.write(temp.getBytes());
			writer.close();
		}// end of try
		catch (OpcatException ex) {
			throw ex;
		}// end of catch

	}// end of func

	/**
	 * Creates the xml code of the xml file according the users choice.
	 */
	private void generateAllDiagrams() throws OpcatException {
		try {
			String temp = "", tempId = "";

			Enumeration diagramElementsEnum;
			// ----------------------------------------------------
						String output = "<!--  ==================  [General Model Segment] ====================  --> \n";
			output += "<UML:Model xmi.id=\"" + MAIN_MODEL_NAME + "\" name=\"" + mySys.getName() + "\">\n";
			writer.write(output.getBytes());

			// ********CALLING OF ALL DIAGRAMS*************************
			if (optionClass != -1) {
				classInstances = new HashMap<Long,IObjectInstance>(); 
				classDiagram.createClassDiagram(mySys, writer, tagsVec, classDiagramElements, specVec, assEndVec, assVec, classInstances);
			}

			if (optionUseCase != -1) {
				usecaseDiagram
						.createUseCaseDiagram(mySys, writer, usecaseVec, usecaseIncludeVec, usecaseInzoomIncludeVec, usecasespecVec, usecaseassVec, usecaseassEndVec, usecaseSterIncludeVec, actorTagsVec, usecaseExtendVec, optionUseCase, levelUseCase);
			}
			if ((optionStatechart != -1) && (!vecSt.isEmpty())) {
				statechart
						.StateChartDiagramCreate(mySys, writer, vecSt, stateDiagramsOut, StateSimplePrint, TransitionStatePrint, finalStatePrint, initialStatePrint);
			}

			if ((optionSequence != -1) && (!vecSq.isEmpty())) {
				sequence.SequenceDiagramCreate(mySys, writer, vecSq, seqEntryVec/* ,restObjVec */, ClassifierRolePrint, AssociationPrint);
			}

			if (optionDeployment == 1) {
				deployment.DeploymentDiagramCreate(mySys, writer, deplPrVec, deplRelVec);
			}

			if ((optionActivity == 1) || ((optionActivity == 2) && (!vecA.isEmpty()))) {
				activity
						.ActivityDiagramCreate(mySys, writer, optionActivity, vecA, levA, actDiagramsVec, ActivityPrint, ForkPrint, JoinPrint, InitialPrint, FinalPrint, BranchPrint, TransitionPrint);
			}

			output = "</UML:Model>\n";
			writer.write(output.getBytes());

			/** ************* Tagged values for classes ********************** */
			if (optionClass != -1) {
				diagramElementsEnum = tagsVec.elements();
				temp = "<!--  ==================  [Tagged values for classes] ====================  --> \n";
				while (diagramElementsEnum.hasMoreElements()) {
					tempId = (String) diagramElementsEnum.nextElement();
					temp += "<UML:TaggedValue xmi.id=\"XX." + tempId + "." + tempId + "\" modelElement=\"S." + tempId + "\" /> \n";

				}
				writer.write(temp.getBytes());
			}

			/** *** tagged values for actors actors ****** */
			if (optionUseCase != -1) {

				temp = "<!--  ==================  [Tagged values for actors] ====================  --> \n";
				diagramElementsEnum = actorTagsVec.elements();
				while (diagramElementsEnum.hasMoreElements()) {
					tempId = (String) diagramElementsEnum.nextElement();
					temp += "<UML:TaggedValue xmi.id=\"XX." + tempId + "." + tempId + "\" modelElement=\"S." + tempId + "\" /> \n";

				}
				writer.write(temp.getBytes());
			}

			/** *********** Graphical information ******************** */
		} catch (Exception ex) {
			OpcatLogger.logError(ex) ; 
			throw new OpcatException(ex.getMessage());
		}// end of catch
	}

	private void generateGraphicalData() throws OpcatException {
		String temp = "";
		String tempId = "";
		Enumeration diagramElementsEnum = null;
		try {
			
//			 ---------------------------------------Printout of Class Digram----------------------------------------
			if (optionClass != -1) {

				temp = "<UML:Diagram xmi.id=\"S.0.0.0\" name=\"Main\" diagramType=\"ClassDiagram\" style=\"\" owner=\""+ classDiagram.getDiagramID() +"\">\n";
				temp += "<UML:Diagram.element>\n";
				writer.write(temp.getBytes());

				this.initialize_dest();
				diagramElementsEnum = classDiagramElements.elements();
				while (diagramElementsEnum.hasMoreElements()) {
					this.dest();
					tempId = (String) diagramElementsEnum.nextElement();
					IObjectInstance instance = classInstances.get(new Long(tempId));
					int x = instance.getX();
					System.out.println(tempId +" "+ x);
					temp = "<UML:DiagramElement xmi.id=\"XX." + tempId + "\" geometry=\"" + this.dest1 + ", " + this.dest2 + ", " + this.dest3 + ", " + this.dest4 + ",\" style=\"Font.Blue= 0,Font.Green= 0,Font.Red= 0,Font.FaceName=Arial,Font.Size= 10,Font.Bold=0,Font.Italic=0,Font.Strikethrough=0,Font.Underline=0,LineColor.Blue= 51,LineColor.Green= 0,LineColor.Red= 153,FillColor.Blue= 204,FillColor.Green= 255,FillColor.Red= 255,FillColor.Transparent=1,AutomaticResize=1,ShowAllAttributes=1,ShowAllOperations=1,ShowOperationSignature=1,SuppressAttributes=0,SuppressOperations=0,\" subject=\"S." + tempId + "\" /> \n";
					writer.write(temp.getBytes());
				}
				diagramElementsEnum = assVec.elements();
				while (diagramElementsEnum.hasMoreElements()) {
					this.dest();
					tempId = (String) diagramElementsEnum.nextElement();
					temp = "<UML:DiagramElement xmi.id =\"XX." + tempId + "\" geometry =\"" + this.dest1 + ", " + this.dest2 + ", " + this.dest3 + ", " + this.dest4 + ",\" style = \"Association:Font.Blue= 0,Font.Green= 0,Font.Red= 0,Font.FaceName=Arial,Font.Size= 10,Font.Bold=0,Font.Italic=0,Font.Strikethrough=0,Font.Underline=0,LineColor.Blue= 51,LineColor.Green= 0,LineColor.Red= 153, \" subject = \"G." + tempId + "\" /> \n";
					writer.write(temp.getBytes());
				}
				diagramElementsEnum = assEndVec.elements();
				while (diagramElementsEnum.hasMoreElements()) {
					this.dest();
					tempId = (String) diagramElementsEnum.nextElement();
					temp = "<UML:DiagramElement xmi.id = \"XX." + tempId + "\" geometry =\"" + this.dest1 + ", " + this.dest2 + ", " + this.dest3 + ", " + this.dest4 + ",\" style = \"Role\" subject = \"G." + tempId + "\" />\n";
					writer.write(temp.getBytes());
				}
				diagramElementsEnum = specVec.elements();
				while (diagramElementsEnum.hasMoreElements()) {
					this.dest();
					tempId = (String) diagramElementsEnum.nextElement();
					temp = "<UML:DiagramElement xmi.id = \"XX." + tempId + "\" geometry = \"" + this.dest1 + ", " + this.dest2 + ", " + this.dest3 + ", " + this.dest4 + ",\" style = \"Inheritance\" subject = \"G." + tempId + "\" />\n";
					writer.write(temp.getBytes());
				}

				temp = " </UML:Diagram.element>\n";
				writer.write(temp.getBytes());
				temp = "</UML:Diagram>\n";
				writer.write(temp.getBytes());
			}

			// ---------------------------------------Printout of UseCase Digram----------------------------------------
			if (optionUseCase != -1) {

				temp = "<UML:Diagram xmi.id=\"S.0.0.0.1\" name=\"Main\" diagramType=\"UseCaseDiagram\" style=\"\" owner=\"G.0000.1\">\n";
				writer.write(temp.getBytes());
				temp = "<UML:Diagram.element>\n";
				writer.write(temp.getBytes());

				this.initialize_dest();
				diagramElementsEnum = usecaseVec.elements();
				while (diagramElementsEnum.hasMoreElements()) {
					this.dest();
					tempId = (String) diagramElementsEnum.nextElement();
					temp = "<UML:DiagramElement geometry=\"" + this.dest1 + ", " + this.dest2 + ", " + this.dest3 + ", " + this.dest4 + ",\" style=\"FillColor.Blue= 204,FillColor.Green= 255,FillColor.Red= 255,FillColor.Transparent=1,Font.Blue= 0,Font.Green= 0,Font.Red= 0,Font.FaceName=Arial,Font.Size= 10,Font.Bold=0,Font.Italic=0,Font.Strikethrough=0,Font.Underline=0,LineColor.Blue= 51,LineColor.Green= 0,LineColor.Red= 153,\" subject=\"S." + tempId + "\" />\n";
					writer.write(temp.getBytes());
				}

				diagramElementsEnum = actorTagsVec.elements();
				while (diagramElementsEnum.hasMoreElements()) {
					this.dest();
					tempId = (String) diagramElementsEnum.nextElement();
					temp = "<UML:DiagramElement xmi.id=\"XX." + tempId + "\" geometry=\"" + this.dest1 + ", " + this.dest2 + ", " + this.dest3 + ", " + this.dest4 + ",\" style=\"Font.Blue= 0,Font.Green= 0,Font.Red= 0,Font.FaceName=Arial,Font.Size= 10,Font.Bold=0,Font.Italic=0,Font.Strikethrough=0,Font.Underline=0,LineColor.Blue= 51,LineColor.Green= 0,LineColor.Red= 153,FillColor.Blue= 255,FillColor.Green= 255,FillColor.Red= 255,FillColor.Transparent=0,AutomaticResize=1,ShowAllAttributes=1,ShowAllOperations=1,ShowOperationSignature=0,SuppressAttributes=0,SuppressOperations=0,\" subject=\"S." + tempId + "\" />\n";
					writer.write(temp.getBytes());
				}

				diagramElementsEnum = usecaseassVec.elements();
				while (diagramElementsEnum.hasMoreElements()) {
					this.dest();
					tempId = (String) diagramElementsEnum.nextElement();
					temp = "<UML:DiagramElement xmi.id=\"XX." + tempId + "\" geometry=\"" + this.dest1 + ", " + this.dest2 + ", " + this.dest3 + ", " + this.dest4 + ",\" style=\"Association:Font.Blue= 0,Font.Green= 0,Font.Red= 0,Font.FaceName=Arial,Font.Size= 10,Font.Bold=0,Font.Italic=0,Font.Strikethrough=0,Font.Underline=0,LineColor.Blue= 51,LineColor.Green= 0,LineColor.Red= 153,\" subject=\"G." + tempId + "\" /> \n";
					writer.write(temp.getBytes());
				}

				diagramElementsEnum = usecaseassEndVec.elements();
				while (diagramElementsEnum.hasMoreElements()) {
					this.dest();
					tempId = (String) diagramElementsEnum.nextElement();
					temp = "<UML:DiagramElement xmi.id=\"XX." + tempId + "\" geometry=\"" + this.dest1 + ", " + this.dest2 + ", " + this.dest3 + ", " + this.dest4 + ",\" style=\"Role\" subject=\"G." + tempId + "\" />  \n";
					writer.write(temp.getBytes());
				}

				diagramElementsEnum = usecasespecVec.elements();
				while (diagramElementsEnum.hasMoreElements()) {
					this.dest();
					tempId = (String) diagramElementsEnum.nextElement();
					temp = "<UML:DiagramElement xmi.id=\"XX." + tempId + "\" geometry=\"" + this.dest1 + ", " + this.dest2 + ", " + this.dest3 + ", " + this.dest4 + ",\" style=\"Inheritance\" subject=\"G." + tempId + "\" /> \n";
					writer.write(temp.getBytes());
				}

				diagramElementsEnum = usecaseSterIncludeVec.elements();
				while (diagramElementsEnum.hasMoreElements()) {
					this.dest();
					tempId = (String) diagramElementsEnum.nextElement();
					temp = "<UML:DiagramElement xmi.id=\"XX." + tempId + "\" geometry=\"" + this.dest1 + ", " + this.dest2 + ", " + this.dest3 + ", " + this.dest4 + ",\" style=\"Association:Font.Blue= 0,Font.Green= 0,Font.Red= 0,Font.FaceName=Arial,Font.Size= 10,Font.Bold=0,Font.Italic=0,Font.Strikethrough=0,Font.Underline=0,LineColor.Blue= 51,LineColor.Green= 0,LineColor.Red= 153,\" subject=\"G." + tempId + "\" /> \n";
					writer.write(temp.getBytes());
					this.dest();
					temp = "<UML:DiagramElement xmi.id=\"XX." + tempId + ".1\" geometry=\"" + this.dest1 + ", " + this.dest2 + ", " + this.dest3 + ", " + this.dest4 + ",\" style=\"Role\" subject=\"G." + tempId + ".1\" />\n";
					writer.write(temp.getBytes());
					this.dest();
					temp = "<UML:DiagramElement xmi.id=\"XX." + tempId + ".2\" geometry=\"" + this.dest1 + ", " + this.dest2 + ", " + this.dest3 + ", " + this.dest4 + ",\" style=\"Role\" subject=\"G." + tempId + ".2\" />\n";
					writer.write(temp.getBytes());
				}

				diagramElementsEnum = usecaseExtendVec.elements();
				while (diagramElementsEnum.hasMoreElements()) {
					this.dest();
					tempId = "" + ((ILinkEntry) diagramElementsEnum.nextElement()).getId() + "";
					temp = "<UML:DiagramElement xmi.id=\"XX." + tempId + "\" geometry=\"" + this.dest1 + ", " + this.dest2 + ", " + this.dest3 + ", " + this.dest4 + ",\" style=\"Association:Font.Blue= 0,Font.Green= 0,Font.Red= 0,Font.FaceName=Arial,Font.Size= 10,Font.Bold=0,Font.Italic=0,Font.Strikethrough=0,Font.Underline=0,LineColor.Blue= 51,LineColor.Green= 0,LineColor.Red= 153,\" subject=\"G." + tempId + "\" /> \n";
					writer.write(temp.getBytes());
					this.dest();
					temp = "<UML:DiagramElement xmi.id=\"XX." + tempId + ".1\" geometry=\"" + this.dest1 + ", " + this.dest2 + ", " + this.dest3 + ", " + this.dest4 + ",\" style=\"Role\" subject=\"G." + tempId + ".1\" />\n";
					writer.write(temp.getBytes());
					this.dest();
					temp = "<UML:DiagramElement xmi.id=\"XX." + tempId + ".2\" geometry=\"" + this.dest1 + ", " + this.dest2 + ", " + this.dest3 + ", " + this.dest4 + ",\" style=\"Role\" subject=\"G." + tempId + ".2\" />\n";
					writer.write(temp.getBytes());

				}

				temp = " </UML:Diagram.element>\n";
				writer.write(temp.getBytes());
				temp = "</UML:Diagram>\n";
				writer.write(temp.getBytes());
			}

			// ---------------------------------PRINTOUTS OF DEPLOYMENT DIAGRAM----------------------------------
			if (optionDeployment == 1) {
				long id;
				temp = "<UML:Diagram xmi.id=\"G.0000.6\" name=\"Deployment View\" diagramType=\"DeploymentDiagram\" style=\"\" owner=\"G.0000\">";
				writer.write(temp.getBytes());
				temp = "<UML:Diagram.element>";
				writer.write(temp.getBytes());

				this.initialize_dest();
				diagramElementsEnum = deplPrVec.elements();
				while (diagramElementsEnum.hasMoreElements()) {
					id = ((IObjectEntry) diagramElementsEnum.nextElement()).getId();
					this.dest();
					temp = "<UML:DiagramElement xmi.id=\"XX." + id + "\" geometry=\"" + this.dest1 + ", " + this.dest2 + ", " + this.dest3 + ", " + this.dest4 + ",\" style=\"FillColor.Blue= 204,FillColor.Green= 255,FillColor.Red= 255,FillColor.Transparent=1,Font.Blue= 0,Font.Green= 0,Font.Red= 0,Font.FaceName=Arial,Font.Size= 10,Font.Bold=0,Font.Italic=0,Font.Strikethrough=0,Font.Underline=0,LineColor.Blue= 51,LineColor.Green= 0,LineColor.Red= 153,\" subject=\"G." + id + "\" /> ";
					writer.write(temp.getBytes());
				}
				diagramElementsEnum = deplRelVec.elements();
				while (diagramElementsEnum.hasMoreElements()) {
					id = ((IRelationEntry) diagramElementsEnum.nextElement()).getId();
					temp = "<UML:DiagramElement xmi.id=\"XX." + id + "\" geometry=\"472, 575, 102, 417,\" style=\"FillColor.Blue= 204,FillColor.Green= 255,FillColor.Red= 255,FillColor.Transparent=0,Font.Blue= 0,Font.Green= 0,Font.Red= 0,Font.FaceName=Arial,Font.Size= 10,Font.Bold=0,Font.Italic=0,Font.Strikethrough=0,Font.Underline=0,LineColor.Blue= 51,LineColor.Green= 0,LineColor.Red= 153,\" /> ";
					writer.write(temp.getBytes());
				}

				temp = "</UML:Diagram.element>";
				writer.write(temp.getBytes());
				temp = "</UML:Diagram>";
				writer.write(temp.getBytes());
			}

			// ---------------------------------PRINTOUTS OF ACTIVITY DIAGRAM
			if (optionActivity == 1) {
				Enumeration actTransitions, actJoins, actForks, actBranchs, actFinals, actInitials, actActivities;
				Enumeration actDiagrams = actDiagramsVec.elements();// all activity
				// diagrams that
				// should be
				// created
				String diagramId = "", diagramName, ownerId;
				Activity_d.PrintStructure tempPrintStructure;

				while (actDiagrams.hasMoreElements()) {
					this.initialize_dest();
					diagramId = (String) actDiagrams.nextElement();
					diagramName = (String) actDiagrams.nextElement();
					ownerId = this.isActSubDiagram(diagramId, vecA);
					temp = "<UML:Diagram xmi.id=\"S.458" + diagramId + "\" name=\"" + diagramName + "\" diagramType=\"ActivityDiagram\" style=\"\" owner=\"";
					writer.write(temp.getBytes());
					if (ownerId.compareTo("G") == 0) {
						temp = "G." + diagramId + ".5";
						writer.write(temp.getBytes());
					} else {
						temp = "" + ownerId + "";
						writer.write(temp.getBytes());
					}
					temp = "\">";
					writer.write(temp.getBytes());
					temp = "<UML:Diagram.element>";
					writer.write(temp.getBytes());

					// initials
					actInitials = InitialPrint.elements();
					while (actInitials.hasMoreElements()) {
						tempPrintStructure = (Activity_d.PrintStructure) actInitials.nextElement();
						if (tempPrintStructure.getDiagramId().compareTo(diagramId) == 0) {
							temp = "<UML:DiagramElement xmi.id=\"XX.a" + tempPrintStructure.getElementId() + "\" geometry=\"816," + this.dest2 + ", 60, 60,\" style=\"FillColor.Blue= 255,FillColor.Green= 255,FillColor.Red= 255,FillColor.Transparent=0,Font.Blue= 0,Font.Green= 0,Font.Red= 0,Font.FaceName=Arial,Font.Size= 10,Font.Bold=0,Font.Italic=0,Font.Strikethrough=0,Font.Underline=0,LineColor.Blue= 51,LineColor.Green= 0,LineColor.Red= 153,\" subject=\"G." + tempPrintStructure
									.getElementId() + "\" />";
							writer.write(temp.getBytes()); // 188
						}
					}

					// forks
					actForks = ForkPrint.elements();
					while (actForks.hasMoreElements()) {
						tempPrintStructure = (Activity_d.PrintStructure) actForks.nextElement();
						if (tempPrintStructure.getDiagramId().compareTo(diagramId) == 0) {
							this.dest();
							temp = "<UML:DiagramElement xmi.id=\"XX.a" + tempPrintStructure.getElementId() + "\" geometry=\" 864," + this.dest2 + ", 992, 18,\" style=\"SyncItem, Horizontal=1,\" subject=\"G." + tempPrintStructure
									.getElementId() + "\" />";
							writer.write(temp.getBytes()); // 864, 956 "+dest1+"
						}
					}
					// branches
					actBranchs = BranchPrint.elements();
					while (actBranchs.hasMoreElements()) {
						tempPrintStructure = (Activity_d.PrintStructure) actBranchs.nextElement();
						if (tempPrintStructure.getDiagramId().compareTo(diagramId) == 0) {
							this.dest();
							temp = "<UML:DiagramElement xmi.id=\"XX.a" + tempPrintStructure.getElementId() + "\" geometry=\"800," + this.dest2 + ", 150, 74,\" style=\"FillColor.Blue= 204,FillColor.Green= 255,FillColor.Red= 255,FillColor.Transparent=1,Font.Blue= 0,Font.Green= 0,Font.Red= 0,Font.FaceName=Arial,Font.Size= 10,Font.Bold=0,Font.Italic=0,Font.Strikethrough=0,Font.Underline=0,LineColor.Blue= 51,LineColor.Green= 0,LineColor.Red= 153,\" subject=\"G." + tempPrintStructure
									.getElementId() + "\" />";
							writer.write(temp.getBytes()); // 1420
						}
					}

					// activities
					actActivities = ActivityPrint.elements();
					while (actActivities.hasMoreElements()) {
						tempPrintStructure = (Activity_d.PrintStructure) actActivities.nextElement();
						if (tempPrintStructure.getDiagramId().compareTo(diagramId) == 0) {
							this.dest();
							temp = "<UML:DiagramElement xmi.id=\"XX.a" + tempPrintStructure.getElementId() + "\" geometry=\" 736," + this.dest2 + ", 411, 124,\" style=\"FillColor.Blue= 204,FillColor.Green= 255,FillColor.Red= 255,FillColor.Transparent=1,Font.Blue= 0,Font.Green= 0,Font.Red= 0,Font.FaceName=Arial,Font.Size= 10,Font.Bold=0,Font.Italic=0,Font.Strikethrough=0,Font.Underline=0,LineColor.Blue= 51,LineColor.Green= 0,LineColor.Red= 153,\" subject=\"G." + tempPrintStructure
									.getElementId() + ".5\" />";
							writer.write(temp.getBytes()); // 736, 476 "+dest2+"
						}
					}
					// transitons
					actTransitions = TransitionPrint.elements();
					while (actTransitions.hasMoreElements()) {
						tempPrintStructure = (Activity_d.PrintStructure) actTransitions.nextElement();
						if (tempPrintStructure.getDiagramId().compareTo(diagramId) == 0) {
							temp = "<UML:DiagramElement xmi.id=\"XX.a" + tempPrintStructure.getElementId() + "\" geometry=\"736, 339, 162, 150,\" style=\"Transition\" subject=\"G." + tempPrintStructure
									.getElementId() + "\" />";
							writer.write(temp.getBytes());
						}
					}
					// joins
					actJoins = JoinPrint.elements();
					while (actJoins.hasMoreElements()) {
						tempPrintStructure = (Activity_d.PrintStructure) actJoins.nextElement();
						if (tempPrintStructure.getDiagramId().compareTo(diagramId) == 0) {
							this.dest();
							temp = "<UML:DiagramElement xmi.id=\"XX.a" + tempPrintStructure.getElementId() + "\" geometry=\" 864," + this.dest2 + ", 992, 18,\" style=\"SyncItem, Horizontal=1,\" subject=\"G." + tempPrintStructure
									.getElementId() + "\" />";
							writer.write(temp.getBytes()); // 832, 1564 "+dest1+"
						}
					}
					// finals
					actFinals = FinalPrint.elements();
					while (actFinals.hasMoreElements()) {
						tempPrintStructure = (Activity_d.PrintStructure) actFinals.nextElement();
						if (tempPrintStructure.getDiagramId().compareTo(diagramId) == 0) {
							this.dest();
							temp = "<UML:DiagramElement xmi.id=\"XX.a" + tempPrintStructure.getElementId() + "\" geometry=\"800," + this.dest2 + ", 84, 84,\" style=\"FillColor.Blue= 255,FillColor.Green= 255,FillColor.Red= 255,FillColor.Transparent=0,Font.Blue= 0,Font.Green= 0,Font.Red= 0,Font.FaceName=Arial,Font.Size= 10,Font.Bold=0,Font.Italic=0,Font.Strikethrough=0,Font.Underline=0,LineColor.Blue= 51,LineColor.Green= 0,LineColor.Red= 153,\" subject=\"G." + tempPrintStructure
									.getElementId() + "\" />";
							writer.write(temp.getBytes()); // 2396
						}
					}

					temp = "</UML:Diagram.element>";
					writer.write(temp.getBytes());
					temp = "</UML:Diagram>";
					writer.write(temp.getBytes());
				}
			}

			// ---------------------------------PRINTOUTS OF STATECHART DIAGRAM
			if (optionStatechart == 1) {
				int flag = 0;
				Enumeration e, e2, e3;

				IEntry ent, father_ent;
				e = stateDiagramsOut.elements();
				Enumeration e1 = StateSimplePrint.elements();
				e3 = TransitionStatePrint.elements();
				e2 = finalStatePrint.elements();
				Enumeration e4 = initialStatePrint.elements();

				flag = 0;
				while (e.hasMoreElements()) {
					this.initialize_dest();
					ent = (IEntry) e.nextElement();
					father_ent = (IEntry) e.nextElement();
					temp = "<UML:Diagram xmi.id=\"S." + ent.getId() + ".4\" name=\"" + ent.getName() + "\" diagramType=\"StateDiagram\" style=\"\" owner=\"S." + father_ent
							.getId() + ".44\">";
					writer.write(temp.getBytes());
					temp = "<UML:Diagram.element>";
					writer.write(temp.getBytes());
					flag = 0;
					while ((e1.hasMoreElements()) && (flag == 0)) {
						tempId = (String) e1.nextElement();
						if (tempId != "-1") {
							this.dest();
							temp = "<UML:DiagramElement xmi.id=\"XX." + tempId + "\" geometry=\"" + this.dest1 + ", " + this.dest2 + ", 300, 134,\" style=\"FillColor.Blue= 204,FillColor.Green= 255,FillColor.Red= 255,FillColor.Transparent=1,Font.Blue= 0,Font.Green= 0,Font.Red= 0,Font.FaceName=Arial,Font.Size= 10,Font.Bold=0,Font.Italic=0,Font.Strikethrough=0,Font.Underline=0,LineColor.Blue= 51,LineColor.Green= 0,LineColor.Red= 153,\" subject=\"G." + tempId + "\" /> ";
							writer.write(temp.getBytes());
						} else {
							flag = 1;
						}
					}
					flag = 0;
					while ((e3.hasMoreElements()) && (flag == 0)) {
						tempId = (String) e3.nextElement();
						if (tempId != "-1") {
							temp = "<UML:DiagramElement xmi.id=\"XX." + tempId + "\" geometry=\"528, 194, 162, 150,\" style=\"Transition\" subject=\"G." + tempId + "\" /> ";
							writer.write(temp.getBytes());
						} else {
							flag = 1;
						}
					}
					// finalStatePrint
					flag = 0;
					while ((e2.hasMoreElements()) && (flag == 0)) {
						tempId = (String) e2.nextElement();
						if (tempId != "-1") {
							temp = "<UML:DiagramElement xmi.id=\"XX." + tempId + "\" geometry=\"1024, 848, 84, 84,\" style=\"FillColor.Blue= 204,FillColor.Green= 255,FillColor.Red= 255,FillColor.Transparent=1,Font.Blue= 0,Font.Green= 0,Font.Red= 0,Font.FaceName=Arial,Font.Size= 10,Font.Bold=0,Font.Italic=0,Font.Strikethrough=0,Font.Underline=0,LineColor.Blue= 51,LineColor.Green= 0,LineColor.Red= 153,\" subject=\"G." + tempId + "\" /> ";
							writer.write(temp.getBytes());
						} else {
							flag = 1;
						}
					}
					// initialStatePrint
					flag = 0;
					while ((e4.hasMoreElements()) && (flag == 0)) {
						tempId = (String) e4.nextElement();
						if (tempId != "-1") {
							temp = "<UML:DiagramElement xmi.id=\"XX." + tempId + "\" geometry=\"1536, 432, 60, 60,\" style=\"FillColor.Blue= 255,FillColor.Green= 255,FillColor.Red= 255,FillColor.Transparent=0,Font.Blue= 0,Font.Green= 0,Font.Red= 0,Font.FaceName=Arial,Font.Size= 10,Font.Bold=0,Font.Italic=0,Font.Strikethrough=0,Font.Underline=0,LineColor.Blue= 51,LineColor.Green= 0,LineColor.Red= 153,\" subject=\"G." + tempId + "\" /> ";
							writer.write(temp.getBytes());
						} else {
							flag = 1;
						}
					}

					temp = "</UML:Diagram.element>";
					writer.write(temp.getBytes());
					temp = "</UML:Diagram>";
					writer.write(temp.getBytes());
				}
			}

			// ---------------------------------PRINTOUTS OF Sequence DIAGRAM
			Enumeration clsRoleEnum;
			Enumeration assEnum;
			String diagIdC = "", diagIdA = "", nameC = "";
			int sqn;
			boolean M1, M2;

			clsRoleEnum = ClassifierRolePrint.elements();
			while (clsRoleEnum.hasMoreElements()) {
				if (clsRoleEnum.hasMoreElements()) {
					diagIdC = (String) clsRoleEnum.nextElement();
				}
				if (clsRoleEnum.hasMoreElements()) {
					nameC = (String) clsRoleEnum.nextElement();
				}

				temp = "<UML:Diagram xmi.id=\"S." + diagIdC + "\" name=\"" + nameC + "\" diagramType=\"SequenceDiagram\" style=\"\" owner=\"G." + diagIdC + "\">";
				writer.write(temp.getBytes());
				temp = "<UML:Diagram.element>";
				writer.write(temp.getBytes());

				this.initialize_dest();

				M1 = false;
				while ((!M1) && (clsRoleEnum.hasMoreElements())) {
					tempId = (String) clsRoleEnum.nextElement();
					if (tempId == "-1") {
						M1 = true;
					} else {
						this.dest();
						temp = "<UML:DiagramElement xmi.id=\"XX." + tempId + "\" geometry=\"" + this.destSeq + ", 224, 300, 118,\" style=\"FillColor.Blue= 204,FillColor.Green= 255,FillColor.Red= 255,FillColor.Transparent=1,Font.Blue= 0,Font.Green= 0,Font.Red= 0,Font.FaceName=Arial,Font.Size= 10,Font.Bold=0,Font.Italic=0,Font.Strikethrough=0,Font.Underline=1,LineColor.Blue= 51,LineColor.Green= 0,LineColor.Red= 153,\" subject=\"G." + tempId + "\" /> ";
						writer.write(temp.getBytes());
					}
				}

				M1 = false;
				M2 = false;
				sqn = 0;
				assEnum = AssociationPrint.elements();
				while ((assEnum.hasMoreElements()) && (!M1)) {
					M2 = false;
					diagIdA = (String) assEnum.nextElement();// diagramID
					if (diagIdA.compareTo(diagIdC) == 0) {
						while (!M1) {
							tempId = (String) assEnum.nextElement();
							if (tempId == "-1") {
								M1 = true;
							} else {
								sqn++;
								this.dest();
								temp = "<UML:DiagramElement xmi.id=\"XX." + tempId + "\" geometry=\"" + this.destSeq + ", 324, 12, 52,\" style=\"Message, SQN= " + sqn + ",\" subject=\"G." + tempId + "\" /> ";
								writer.write(temp.getBytes());
							}// end else
						}
					} else {// not relavent diagram-->out until -1 (M1)
						while (!M2) {
							if (assEnum.hasMoreElements()) {
								tempId = (String) assEnum.nextElement();
							}
							if (tempId == "-1") {
								M2 = true;
							}

						}
					}
				}

				temp = "</UML:Diagram.element>";
				writer.write(temp.getBytes());
				temp = "</UML:Diagram>";
				writer.write(temp.getBytes());
			}

		} catch (Exception ex) {
			throw new OpcatException(ex.getMessage());
		}
	}

	/**
	 * Calculates if the given diagram id is an id of subdiagram in activity diagram or it is a main diagram
	 * 
	 * @param String
	 *            diagramId
	 * @param Vector
	 *            vecA - contains process that need to be diagrams - processes that the chooser selected.
	 * @retuns "S" if main diagram
	 * @retuns "G" if subDiagram diagram
	 */
	private String isActSubDiagram(String diagramId, Vector vecA) {
		Enumeration locenum = vecA.elements();
		IProcessEntry proc;
		String temp = "";

		if (diagramId.compareTo("00.555") == 0) {
			return "S.0000.5";
		}
		while (locenum.hasMoreElements()) {
			proc = (IProcessEntry) locenum.nextElement();
			temp = "" + proc.getId() + ".5";
			if (diagramId.compareTo(temp) == 0) {
				return "S.0000.5";
			}
		}
		return "G";
	}

	public Enumeration getBaseEnum() {
		return baseEnum;
	}

	public void setBaseEnum(Enumeration baseEnum) {
		this.baseEnum = baseEnum;
		classDiagram = new ClassDiagram(baseEnum);
	}

}