package extensionTools.Documents.Doc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import exportedAPI.OpcatConstants;
import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.IInstance;
import exportedAPI.opcatAPI.ILinkEntry;
import exportedAPI.opcatAPI.IObjectEntry;
import exportedAPI.opcatAPI.IOpd;
import exportedAPI.opcatAPI.IProcessEntry;
import exportedAPI.opcatAPI.IRelationEntry;
import exportedAPI.opcatAPI.IStateEntry;
import exportedAPI.opcatAPI.ISystem;
import exportedAPI.opcatAPI.ISystemStructure;
import gui.Opcat2;
import gui.util.OpcatLogger;

/**
 * <P>
 * Document is a class that contains all the information relevant to the
 * document, and creates an .htm file according to the information in the
 * project database.
 * 
 * @author Olga Tavrovsky
 * @author Anna Levit
 */

public class Document {
	private String picDirName = "";

	public Document() {
	}

	public Document(ISystem sys) {
		this.mySys = sys;
	}

	/**
	 * Contains all the choices relevant to this document.
	 */
	public Info DocInfo = new Info();

	ISystem mySys;

	private final static String fileSeparator = System
			.getProperty("file.separator");

	/**
	 * Prints the relevant html tags and info for a relation.
	 * 
	 * @throws IOException
	 * @param rel
	 *            of type IRelationEntry to be printed
	 * @param MyStructure
	 *            of type ISystemStructure
	 * @param file
	 *            of type FileOutputStream
	 */
	private void PrintRel(IRelationEntry rel, ISystemStructure MyStructure,
			FileWriter file) throws IOException {
		try {
			String temp = "";
			IStateEntry temp_obj;
			IEntry abstractEntry;
			// get the source of the relation
			abstractEntry = (IEntry) MyStructure.getIEntry(rel.getSourceId());
			// if the source is a state of an object
			if (abstractEntry instanceof IStateEntry) {
				temp_obj = (IStateEntry) abstractEntry;
				temp = "<P><DT>Source Name: "
						+ temp_obj.getParentIObjectEntry().getName() + " "
						+ temp_obj.getName();
				file.write(temp);
			}
			// if the source is not a state
			else {
				temp = "<DT>Source Name: " + abstractEntry.getName();
				file.write(temp);
			}
			// cardinality to the source
			temp = "<DD>Cardinality: " + rel.getSourceCardinality();
			file.write(temp);
			// get the destination of the relation
			abstractEntry = (IEntry) MyStructure.getIEntry(rel
					.getDestinationId());
			// if the destination is state of an object
			if (abstractEntry instanceof IStateEntry) {
				temp_obj = (IStateEntry) abstractEntry;
				temp = "<DT>Destination Name: "
						+ temp_obj.getParentIObjectEntry().getName() + " "
						+ temp_obj.getName();
				file.write(temp);
			}
			// if the destination is not a state
			else {
				temp = "<DT>Destination Name: " + abstractEntry.getName();
				file.write(temp);
			}
			// cardinality to the destination
			temp = "<DD>Cardinality: " + rel.getDestinationCardinality();
			file.write(temp);
		} catch (IOException e) {
			System.out.println("Problem printing a relation");
			return;
		}
	}// end of PrintRel

	/**
	 * Prints the relevant html tags and info for a link
	 * 
	 * @throws IOException
	 * @param link
	 *            of type ILinkEntry to be printed
	 * @param MyStructure
	 *            of type ISystemStructure
	 * @param file
	 *            of type FileOutputStream
	 */
	private void PrintLink(ILinkEntry link, ISystemStructure MyStructure,
			FileWriter file) throws IOException {
		try {

			String temp = "";
			IEntry abstractEntry;
			IStateEntry temp_obj;
			// get the source of the link
			abstractEntry = (IEntry) MyStructure.getIEntry(link.getSourceId());
			// if the source is a state
			if (abstractEntry instanceof IStateEntry) {
				temp_obj = (IStateEntry) abstractEntry;
				temp = "<P><DT>Source Name: "
						+ temp_obj.getParentIObjectEntry().getName() + " "
						+ temp_obj.getName();
				file.write(temp);
			}
			// if the source not a state
			else {
				temp = "<P><DT>Source Name: " + abstractEntry.getName();
				file.write(temp);
			}
			// get the destination of the Link
			abstractEntry = (IEntry) MyStructure.getIEntry(link
					.getDestinationId());
			// if the destination is a state
			if (abstractEntry instanceof IStateEntry) {
				temp_obj = (IStateEntry) abstractEntry;
				temp = "<DT>Destination Name: "
						+ temp_obj.getParentIObjectEntry().getName() + " "
						+ temp_obj.getName();
				file.write(temp);
			}
			// if the destination is not a state
			else {
				temp = "<DT>Destination Name: " + abstractEntry.getName();
				file.write(temp);
			}
			// if needed print the link condition
			if ((this.DocInfo.Data.Link.getCond())
					&& (link.getCondition().compareTo("") != 0)) {
				temp = "<DD>Condition: " + link.getCondition();
				file.write(temp);
			}
			// if needed print the link path
			if ((this.DocInfo.Data.Link.getPath())
					&& (link.getPath().compareTo("") != 0)) {
				temp = "<DD>Path: " + link.getPath();
				file.write(temp);
			}
			// if needed print link reaction time
			if (this.DocInfo.Data.Link.getReactTime()) {
				if (link.getMinReactionTime().compareTo("0;0;0;0;0;0;0") != 0) {
					temp = "<DD>Min Reaction Time: ";
					file.write(temp);
					this.printActTime(link.getMinReactionTime(), file);
				}
				if (link.getMaxReactionTime().compareTo("infinity") != 0) {
					temp = "<DD>Max Reaction Time: ";
					file.write(temp);
					this.printActTime(link.getMaxReactionTime(), file);
				}
			}
		} catch (IOException e) {
			System.out.println("Problem printing a link");
			return;
		}
	}// end of PrintLink

	/**
	 * Prints the the activation time in html format.
	 * 
	 * @throws IOException
	 * @param t
	 *            of type String to be printed
	 * @param file
	 *            of type FileOutputStream
	 */

	void printActTime(String t, FileWriter file) throws IOException {
		String temp;
		try {
			// cut the string by ';'
			StringTokenizer st = new StringTokenizer(t, ";", false);
			temp = st.nextToken();
			// if msec != 0 print
			if (temp.compareTo("0") != 0) {
				temp += " msec ";
				file.write(temp);
			}
			temp = st.nextToken();
			// if sec != 0 print
			if (temp.compareTo("0") != 0) {
				temp += " sec ";
				file.write(temp);
			}
			// if min != 0 print
			temp = st.nextToken();
			if (temp.compareTo("0") != 0) {
				temp += " min ";
				file.write(temp);
			}
			temp = st.nextToken();
			// if hours != 0 print
			if (temp.compareTo("0") != 0) {
				temp += " hours ";
				file.write(temp);
			}
			temp = st.nextToken();
			// if days != 0 print
			if (temp.compareTo("0") != 0) {
				temp += " days ";
				file.write(temp);
			}
			temp = st.nextToken();
			// if months != 0 print
			if (temp.compareTo("0") != 0) {
				temp += " months ";
				file.write(temp);
			}
			temp = st.nextToken();
			// if years != 0 print
			if (temp.compareTo("0") != 0) {
				temp += " years ";
				file.write(temp);
			}

		} catch (IOException e) {
			System.out.println("Problem printing time");
			return;
		}
	}

	/**
	 * Prints an OPD diagram into the html file.
	 * 
	 * @throws IOException
	 * @param opd
	 *            of type IOpd to be printed
	 * @param f
	 *            of type FileOutputStream
	 * @param filename
	 *            of type String
	 */

	public void PrintOpd(IOpd opd, FileWriter f, String filename)
			throws IOException {
		try {
			long temp_long = opd.getOpdId();
			String opdfile;
			String oplfile;
			BufferedImage bi;
			String t = new String();
			// print the name of the diagram
			t = "<P><H3><B>" + opd.getName();
			if (f != null) {
				f.write(t);
			}
			// calls the picture file by the system number of the diagram
			opdfile = "" + opd.getName() + "_" + "" + "" + temp_long + "_OPDfile.jpeg";
			oplfile = "" + opd.getName() + "_" + "" + "" + temp_long
					+ "_OPlFileName.html";

			StringTokenizer st = new StringTokenizer(filename, ".", false);

			File pic_dir = new File(st.nextToken());
			if (!pic_dir.exists()) {
				// If directory does not exist
				pic_dir.mkdir(); // ...create it
			}
			this.picDirName = pic_dir.getName();
			File aFile = new File(pic_dir, opdfile);
			File oFile = new File(pic_dir, oplfile);

			aFile.createNewFile(); // Now create a new file if necessary
			oFile.createNewFile();

			FileOutputStream fos = new FileOutputStream(aFile);
			FileWriter opl_os = new FileWriter(oFile);

			String opl = this.mySys.getOPL(true, opd.getOpdId());

			opl_os.write(opl);
			opl_os.flush();
			opl_os.close();

			bi = opd.getImageRepresentation();
			JPEGImageEncoder jpeg = JPEGCodec.createJPEGEncoder(fos);
			JPEGEncodeParam param = jpeg.getDefaultJPEGEncodeParam(bi);
			param.setQuality(1.0f, false);
			jpeg.setJPEGEncodeParam(param);
			jpeg.encode(bi);
			fos.close();
			t = "<P><IMG SRC=\"" + pic_dir.getName() + fileSeparator + opdfile
					+ "\">";
			if (f != null) {
				f.write(t);
			}

		} catch (IOException e) {
			OpcatLogger.logError(e);
			return;
		}
	}

	/**
	 * Calculates the level of the diagram in the tree.
	 * 
	 * @param name
	 *            of type String. The name of the diagram.
	 * @return level integer. The level of the diagram in the tree.
	 */

	public int level(String name) {
		String temp;
		int level = 1;
		StringTokenizer st = new StringTokenizer(name, " ", false);
		temp = st.nextToken();
		// if root
		if (temp.compareTo("SD") == 0) {
			return 1;
		} else {
			StringTokenizer s = new StringTokenizer(temp, ".", false);
			while (s.hasMoreTokens()) {
				temp = s.nextToken();
				level++;
			}
			return level;
		}
	}

	/**
	 * Prints the document into html file.
	 * 
	 * @throws IOException
	 * @param sys
	 *            of type ISystem
	 * @param filename
	 *            of type String
	 */

	public void PrintDocument(ISystem sys, String filename) throws IOException {

		try {
			this.mySys = sys;
			//FileOutputStream file;
			//file = new FileOutputStream(filename);
			FileWriter file = new FileWriter(filename) ;
			IEntry abstractEntry;
			IObjectEntry obj;
			IStateEntry state;
			IProcessEntry proc;
			IRelationEntry rel;
			ILinkEntry link;
			IOpd opd;
			ISystemStructure MyStructure;
			Enumeration state_e;
			String temp = "1";
			Enumeration total_e;
			Vector obj_v = new Vector(0, 1);
			Vector proc_v = new Vector(0, 1);
			Vector link_v = new Vector(0, 1);
			Vector agent_l_v = new Vector(0, 1);
			Vector cond_l_v = new Vector(0, 1);
			Vector cons_ev_l_v = new Vector(0, 1);
			Vector cons_l_v = new Vector(0, 1);
			Vector effect_l_v = new Vector(0, 1);
			Vector exception_l_v = new Vector(0, 1);
			Vector instr_ev_l_v = new Vector(0, 1);
			Vector instr_l_v = new Vector(0, 1);
			Vector invoc_l_v = new Vector(0, 1);
			Vector res_l_v = new Vector(0, 1);

			Vector rel_v = new Vector(0, 1);
			Vector aggr_r_v = new Vector(0, 1);
			Vector feat_r_v = new Vector(0, 1);
			Vector gen_r_v = new Vector(0, 1);
			Vector class_r_v = new Vector(0, 1);
			Vector uni_r_v = new Vector(0, 1);
			Vector bi_r_v = new Vector(0, 1);

			String opl = "";

			MyStructure = this.mySys.getISystemStructure();
			total_e = MyStructure.getElements();
			// ------------------------------------------------elements
			// division-----------------------------------------------------------------------------------------
			// checking the types of each element in the system and putting it
			// in the vector of that type
			while (total_e.hasMoreElements()) {
				abstractEntry = (IEntry) total_e.nextElement();
				// if object
				if (abstractEntry instanceof IObjectEntry) {
					obj_v.addElement((IObjectEntry) abstractEntry);
				}
				// if process
				if (abstractEntry instanceof IProcessEntry) {
					proc_v.addElement((IProcessEntry) abstractEntry);
				}
				// if link
				if (abstractEntry instanceof ILinkEntry) {
					link = (ILinkEntry) abstractEntry;
					link_v.addElement(link);
					if (link.getLinkType() == OpcatConstants.AGENT_LINK) {
						agent_l_v.addElement(link);
					}
					if (link.getLinkType() == OpcatConstants.INSTRUMENT_LINK) {
						instr_l_v.addElement(link);
					}
					if (link.getLinkType() == OpcatConstants.RESULT_LINK) {
						res_l_v.addElement(link);
					}
					if (link.getLinkType() == OpcatConstants.CONSUMPTION_LINK) {
						cons_l_v.addElement(link);
					}
					if (link.getLinkType() == OpcatConstants.EFFECT_LINK) {
						effect_l_v.addElement(link);
					}
					if (link.getLinkType() == OpcatConstants.CONSUMPTION_EVENT_LINK) {
						cons_ev_l_v.addElement(link);
					}
					if (link.getLinkType() == OpcatConstants.INSTRUMENT_EVENT_LINK) {
						instr_ev_l_v.addElement(link);
					}
					if (link.getLinkType() == OpcatConstants.CONDITION_LINK) {
						cond_l_v.addElement(link);
					}
					if (link.getLinkType() == OpcatConstants.EXCEPTION_LINK) {
						exception_l_v.addElement(link);
					}
					if (link.getLinkType() == OpcatConstants.INVOCATION_LINK) {
						invoc_l_v.addElement(link);
					}
				} // of link if
				// if relation
				if (abstractEntry instanceof IRelationEntry) {
					rel = (IRelationEntry) abstractEntry;
					rel_v.addElement(rel);
					if (rel.getRelationType() == OpcatConstants.AGGREGATION_RELATION) {
						aggr_r_v.addElement(rel);
					}
					if (rel.getRelationType() == OpcatConstants.EXHIBITION_RELATION) {
						feat_r_v.addElement(rel);
					}
					if (rel.getRelationType() == OpcatConstants.SPECIALIZATION_RELATION) {
						gen_r_v.addElement(rel);
					}
					if (rel.getRelationType() == OpcatConstants.INSTANTINATION_RELATION) {
						class_r_v.addElement(rel);
					}
					if (rel.getRelationType() == OpcatConstants.UNI_DIRECTIONAL_RELATION) {
						uni_r_v.addElement(rel);
					}
					if (rel.getRelationType() == OpcatConstants.BI_DIRECTIONAL_RELATION) {
						bi_r_v.addElement(rel);
					}
				}// of rel if
			} // of while of division
			// ------------------------------------------------opening-------------------------------------------------------------------------------------------
			temp = "<HTML><HEAD><TITLE>";
			file.write(temp);
			temp = ""
					+ this.mySys.getName()
					+ "</TITLE></HEAD><BODY><BASEFONT FACE=\"Georgia\", SIZE=2>";
			file.write(temp);
			temp = "<p ALIGN=\"right\">Creation Date: "
					+ this.mySys.getCreationDate() + " </P>";
			file.write(temp);
			temp = "<H1 align=\"center\"><FONT COLOR = #800080>"
					+ this.mySys.getName() + "</FONT></p></H1>";
			file.write(temp);
			temp = "Prepared by: " + this.mySys.getCreator() + " </P><HR>";
			file.write(temp);
			// ------------------------------------------------table of
			// contents-------------------------------------------------------------------------------------------

			temp = "<H2 ALIGN=\"center\"><FONT COLOR = NAVY>Table of contents</FONT></H2><DL>";
			file.write(temp);
			// for each field of general info check if it is chosen and if there
			// is data
			// in the field: if yes -> print, else don't print
			if (this.DocInfo.GI.isSelected()) {
				temp = "<P><DT><A HREF=\"#GENERAL INFORMATION\">GENERAL INFORMATION</A>";
				file.write(temp);
				if ((this.DocInfo.GI.getClient())
						&& (this.mySys.getInfoValue("Client").compareTo("") != 0)) {
					temp = "<DD><A HREF=\"#Client\">Client</A>";
					file.write(temp);
				}
				if ((this.DocInfo.GI.getOverview())
						&& (this.mySys.getInfoValue("System_Overview").compareTo("") != 0)) {
					temp = "<DD><A HREF=\"#System Overview\">System Overview</A>";
					file.write(temp);
				}
				if ((this.DocInfo.GI.getGoals())
						&& (this.mySys.getInfoValue(
								"Goals_and_Objectives_of_the_Project")
								.compareTo("") != 0)) {
					temp = "<DD><A HREF=\"#Goals and Objectives of the Project\">Goals and Objectives of the Project</A>";
					file.write(temp);
				}
				if ((this.DocInfo.GI.getUsers())
						&& (this.mySys.getInfoValue("Possible_Users_for_the_System")
								.compareTo("") != 0)) {
					temp = "<DD><A HREF=\"#Possible Users for the System\">Possible Users for the System</A>";
					file.write(temp);
				}
				if ((this.DocInfo.GI.getHard())
						&& (this.mySys.getInfoValue(
								"Hardware_and_Software_Requirements")
								.compareTo("") != 0)) {
					temp = "<DD><A HREF=\"#Hardware and Software Requirements\">Hardware and Software Requirements</A>";
					file.write(temp);
				}
				if ((this.DocInfo.GI.getInputs())
						&& (this.mySys.getInfoValue(
								"Inputs_Processing_functionality_and_Outputs")
								.compareTo("") != 0)) {
					temp = "<DD><A HREF=\"#Inputs, Processing functionality and Outputs\">Inputs, Processing functionality and Outputs</A>";
					file.write(temp);
				}
				if ((this.DocInfo.GI.getFuture())
						&& (this.mySys.getInfoValue("Future_Goals").compareTo("") != 0)) {
					temp = "<DD><A HREF=\"#Future Goals\">Future Goals</A>";
					file.write(temp);
				}
				if ((this.DocInfo.GI.getOper())
						&& (this.mySys.getInfoValue("Operation_and_Maintenance")
								.compareTo("") != 0)) {
					temp = "<DD><A HREF=\"#Operation and Maintenance\">Operation and Maintenance</A>";
					file.write(temp);
				}
				if ((this.DocInfo.GI.getGoals())
						&& (this.mySys.getInfoValue(
								"Hardware_and_Software_Requirements")
								.compareTo("") != 0)) {
					temp = "<DD><A HREF=\"#Hardware and Software Requirements\">Hardware and Software Requirements</A>";
					file.write(temp);
				}
				if ((this.DocInfo.GI.getProblems())
						&& (this.mySys.getInfoValue("Problems").compareTo("") != 0)) {
					temp = "<DD><A HREF=\"#Problems\">Problems</A>";
					file.write(temp);
				}
				if ((this.DocInfo.GI.getCurrent())
						&& (this.mySys.getInfoValue("The_Current_State").compareTo(
								"") != 0)) {
					temp = "<DD><A HREF=\"#The Current State\">The Current State</A>";
					file.write(temp);
				}
				if ((this.DocInfo.GI.getIssues())
						&& (this.mySys.getInfoValue("Open_Issues").compareTo("") != 0)) {
					temp = "<DD><A HREF=\"#Open Issues\">Open Issues</A>";
					file.write(temp);
				}
			}
			// if OPD is selected and OPL is selected print
			if ((this.DocInfo.opdopl.OPDisSelected())
					|| (this.DocInfo.opdopl.OPLisSelected())) {
				temp = "<P><DT><A HREF=\"#DIAGRAMS & OPL\">DIAGRAMS & OPL</A>";
				file.write(temp);
				if ((this.DocInfo.opdopl.OPDisSelected())
						&& (this.DocInfo.opdopl.getOPLAccording())) {
					temp = "<DD><A HREF=\"#OPD & OPL\">OPD & OPL</A>";
					file.write(temp);
				}
				// if only OPD is selected
				else {
					if (this.DocInfo.opdopl.OPDisSelected()) {
						temp = "<DD><A HREF=\"#OPD\">OPD</A>";
						file.write(temp);
					}
					// if only OPL is selected
					if (this.DocInfo.opdopl.OPLisSelected()) {
						temp = "<DD><A HREF=\"#OPL\">OPL</A>";
						file.write(temp);
					}
				}
			}

			// if at least one part of the elements dictionary is selected print
			if ((this.DocInfo.Data.isSelected())
					&& ((!obj_v.isEmpty()) || (!proc_v.isEmpty())
							|| (!link_v.isEmpty()) || (!rel_v.isEmpty()))) {
				temp = "<P><DT><A HREF=\"#ELEMENTS DICTIONARY\">ELEMENTS DICTIONARY</A>";
				file.write(temp);
				// if at least on of the "things" is selected (objects or
				// processes)
				if (((this.DocInfo.Data.Obj.isSelected()) || (this.DocInfo.Data.Proc
						.isSelected()))
						&& ((!obj_v.isEmpty()) || (!proc_v.isEmpty()))) {
					temp = "<DD><DL><DT><A HREF=\"#Things\">Things</A>";
					file.write(temp);
					// if objects are selected and there is at least one object
					// in the project
					if (this.DocInfo.Data.Obj.isSelected() && (!obj_v.isEmpty())) {
						temp = "<DD><A HREF=\"#Objects\">Objects</A>";
						file.write(temp);
					}// obj if end
					// if processes are selected and there is at least one
					// process in the project
					if (this.DocInfo.Data.Proc.isSelected() && (!proc_v.isEmpty())) {
						temp = "<DD><A HREF=\"#Processes\">Processes</A>";
						file.write(temp);
					}// proc if end
					temp = "</DL>";
					file.write(temp);
				}// things if end
				// if relations are selected and there is at least one relation
				// in the project
				if (this.DocInfo.Data.Rel.isSelected() && (!rel_v.isEmpty())) {
					temp = "<DD><A HREF=\"#Relations\">Relations</A>";
					file.write(temp);
				}// rel if end
				// if links are selected and there is at least one link in the
				// project
				if (this.DocInfo.Data.Link.isSelected() && (!link_v.isEmpty())) {
					temp = "<DD><A HREF=\"#Links\">Links</A>";
					file.write(temp);
				}// link if end
			}// elem if end
			temp = "</DL><HR>";
			file.write(temp);
			// ------------------------------------------------end of table of
			// contents--------------------------------------------------------------------------------
			// -------------------------------------------------General
			// Info-------------------------------------------------------------------------------
			// for each field of general info check if it is chosen and if there
			// is data
			// in the field: if yes -> print, else don't print
			if (this.DocInfo.GI.isSelected()) {
				temp = "<H2 ALIGN=\"center\"><FONT COLOR = NAVY><U><A NAME=\"GENERAL INFORMATION\">GENERAL INFORMATION</A></FONT></U></H2><DL>";
				file.write(temp);
				if ((this.DocInfo.GI.getClient())
						&& (this.mySys.getInfoValue("Client").compareTo("") != 0)) {
					temp = "<DT><A NAME=\"Client\">Client:";
					file.write(temp);
					temp = "<DD>" + this.mySys.getInfoValue("Client") + "";
					file.write(temp);
				}
				if ((this.DocInfo.GI.getOverview())
						&& (this.mySys.getInfoValue("System_Overview").compareTo("") != 0)) {
					temp = "<DT><A NAME=\"System Overview\">System Overview:";
					file.write(temp);
					temp = "<DD>" + this.mySys.getInfoValue("System_Overview") + "";
					file.write(temp);
				}
				if ((this.DocInfo.GI.getGoals())
						&& (this.mySys.getInfoValue(
								"Goals_and_Objectives_of_the_Project")
								.compareTo("") != 0)) {
					temp = "<DT><A NAME=\"Goals and Objectives of the Project\">Goals and Objectives of the Project:";
					file.write(temp);
					temp = "<DD>"
							+ this.mySys
									.getInfoValue("Goals_and_Objectives_of_the_Project")
							+ "";
					file.write(temp);
				}
				if ((this.DocInfo.GI.getUsers())
						&& (this.mySys.getInfoValue("Possible_Users_for_the_System")
								.compareTo("") != 0)) {
					temp = "<DT><A NAME=\"Possible Users for the System\">Possible Users for the System:";
					file.write(temp);
					temp = "<DD>"
							+ this.mySys
									.getInfoValue("Possible_Users_for_the_System")
							+ "";
					file.write(temp);
				}
				if ((this.DocInfo.GI.getHard())
						&& (this.mySys.getInfoValue(
								"Hardware_and_Software_Requirements")
								.compareTo("") != 0)) {
					temp = "<DT><A NAME=\"Hardware and Software Requirements\">Hardware and Software Requirements:";
					file.write(temp);
					temp = "<DD>"
							+ this.mySys
									.getInfoValue("Hardware_and_Software_Requirements")
							+ "";
					file.write(temp);
				}
				if ((this.DocInfo.GI.getInputs())
						&& (this.mySys.getInfoValue(
								"Inputs_Processing_functionality_and_Outputs")
								.compareTo("") != 0)) {
					temp = "<DT><A NAME=\"Inputs, Processing functionality and Outputs\">Inputs, Processing functionality and Outputs:";
					file.write(temp);
					temp = "<DD>"
							+ this.mySys
									.getInfoValue("Inputs_Processing_functionality_and_Outputs")
							+ "";
					file.write(temp);
				}
				if ((this.DocInfo.GI.getFuture())
						&& (this.mySys.getInfoValue("Future_Goals").compareTo("") != 0)) {
					temp = "<DT><A NAME=\"Future Goals\">Future Goals:";
					file.write(temp);
					temp = "<DD>" + this.mySys.getInfoValue("Future_Goals") + "";
					file.write(temp);
				}
				if ((this.DocInfo.GI.getOper())
						&& (this.mySys.getInfoValue("Operation_and_Maintenance")
								.compareTo("") != 0)) {
					temp = "<DT><A NAME=\"Operation and Maintenance\">Operation and Maintenance:";
					file.write(temp);
					temp = "<DD>"
							+ this.mySys.getInfoValue("Operation_and_Maintenance")
							+ "";
					file.write(temp);
				}
				if ((this.DocInfo.GI.getGoals())
						&& (this.mySys.getInfoValue(
								"Hardware_and_Software_Requirements")
								.compareTo("") != 0)) {
					temp = "<DT><A NAME=\"Hardware and Software Requirements\">Hardware and Software Requirements:";
					file.write(temp);
					temp = "<DD>"
							+ this.mySys
									.getInfoValue("Hardware_and_Software_Requirements")
							+ "";
					file.write(temp);
				}
				if ((this.DocInfo.GI.getProblems())
						&& (this.mySys.getInfoValue("Problems").compareTo("") != 0)) {
					temp = "<DT><A NAME=\"Problems\">Problems:";
					file.write(temp);
					temp = "<DD>" + this.mySys.getInfoValue("Problems") + "";
					file.write(temp);
				}
				if ((this.DocInfo.GI.getCurrent())
						&& (this.mySys.getInfoValue("The_Current_State").compareTo(
								"") != 0)) {
					temp = "<DT><A NAME=\"The Current State\">The Current State:";
					file.write(temp);
					temp = "<DD>" + this.mySys.getInfoValue("The_Current_State")
							+ "";
					file.write(temp);
				}
				if ((this.DocInfo.GI.getIssues())
						&& (this.mySys.getInfoValue("Open_Issues").compareTo("") != 0)) {
					temp = "<DT><A NAME=\"Open Issues\">Open Issues:";
					file.write(temp);
					temp = "<DD>" + this.mySys.getInfoValue("Open_Issues") + "";
					file.write(temp);
				}
				temp = "</DL>";
				file.write(temp);
			}

			// ------------------------------------------------OPDs---------------------------------------------------------------------------------------------------------------
			// if at least one (OPD or OPL) is selected
			if ((this.DocInfo.opdopl.OPDisSelected())
					|| (this.DocInfo.opdopl.OPLisSelected())) {
				temp = "<H2 ALIGN=\"center\"><FONT COLOR = NAVY><U><A NAME=\"DIAGRAMS & OPL\">DIAGRAMS & OPL</A></FONT></U></H2>";
				file.write(temp);
				Enumeration temp_opds = MyStructure.getOpds();
				Vector opds_v = new Vector(0, 1);

				// making a sorted array of opds
				while (temp_opds.hasMoreElements()) {
					opds_v.addElement(temp_opds.nextElement());
				}
				IOpd arr[] = new IOpd[opds_v.size()];
				opds_v.copyInto(arr);
				compOpd cmp = new compOpd(this.mySys);
				Arrays.sort(arr, cmp);

				Object[] opds_a;
				long temp_long;
				int size;

				// if opd selected and opl according
				if ((this.DocInfo.opdopl.getOPLAccording())
						&& (this.DocInfo.opdopl.OPDisSelected())) {
					temp = "<H2><A NAME=\"OPD & OPL\">OPD & OPL</A></U></H2>";
					file.write(temp);

					// opd all, opl according
					if (this.DocInfo.opdopl.getOPDAll()) {
						size = 0;
						while (size < arr.length) {
							opd = (IOpd) arr[size];
							temp_long = opd.getOpdId();
							opl = this.mySys.getOPL(true, temp_long);
							this.PrintOpd(opd, file, filename);
							temp = "<P>";
							file.write(temp);
							file.write(opl);
							size++;
						}
					}
					// opd by name, opl according
					if (this.DocInfo.opdopl.getOPDByName()) {
						temp_opds = MyStructure.getOpds();
						opds_a = this.DocInfo.opdopl.getOPDList1();
						Arrays.sort(opds_a);
						size = 0;
						while (size < opds_a.length) {
							while (temp_opds.hasMoreElements()) {
								opd = (IOpd) temp_opds.nextElement();
								if (opd.getName().compareTo(
										opds_a[size].toString()) == 0) {
									temp_long = opd.getOpdId();
									opl = this.mySys.getOPL(true, temp_long);
									this.PrintOpd(opd, file, filename);
									temp = "<P>";
									file.write(temp);
									file.write(opl);
									break;
								}// of if
							}// of while
							size++;
							temp_opds = MyStructure.getOpds();
						}// of while
					}// of if

					// opd untill, opl according
					if (this.DocInfo.opdopl.getOPDUntil().compareTo("-1") != 0) {
						int lev = Integer
								.parseInt(this.DocInfo.opdopl.getOPDUntil());
						int i = 0;
						while (i < arr.length) {
							opd = (IOpd) arr[i];
							if (this.level(opd.getName()) <= lev) {
								temp_long = opd.getOpdId();
								opl = this.mySys.getOPL(true, temp_long);
								this.PrintOpd(opd, file, filename);
								temp = "<P>";
								file.write(temp);
								file.write(opl);
							}// of if
							i++;
						}// of while
					}// of if
				}// according

				// if opl is not attached to opd
				else {
					int i;
					// opd all, opl all
					if ((this.DocInfo.opdopl.getOPDAll())
							&& (this.DocInfo.opdopl.getOPLAll())) {
						temp = "<H2><A NAME=\"OPD\">OPD</A></U></H2>";
						file.write(temp);
						i = 0;
						while (i < arr.length) {
							opd = (IOpd) arr[i];
							this.PrintOpd(opd, file, filename);
							i++;
						}// of while
						temp = "<H2><A NAME=\"OPL\">OPL</A></U></H2>";
						file.write(temp);
						temp = "<P>";
						file.write(temp);
						opl = this.mySys.getOPL(true);
						file.write(opl);
					}// of if

					// opd all, opl none
					if ((this.DocInfo.opdopl.getOPDAll())
							&& (this.DocInfo.opdopl.getOPLNone())) {
						temp = "<H2><A NAME=\"OPD\">OPD</A></U></H2>";
						file.write(temp);
						i = 0;
						while (i < arr.length) {
							opd = (IOpd) arr[i];
							this.PrintOpd(opd, file, filename);
							i++;
						}// of while
					}// of if

					// opd none, opl all
					if ((this.DocInfo.opdopl.getOPDNone())
							&& (this.DocInfo.opdopl.getOPLAll())) {
						temp = "<H2><A NAME=\"OPL\">OPL</A></U></H2>";
						file.write(temp);
						temp = "<P>";
						file.write(temp);
						opl = this.mySys.getOPL(true);
						file.write(opl);
					}// of if

					// opd all, opl by name
					if ((this.DocInfo.opdopl.getOPDAll())
							&& (this.DocInfo.opdopl.getOPLByName())) {
						temp = "<H2><A NAME=\"OPD\">OPD</A></U></H2>";
						file.write(temp);
						i = 0;
						while (i < arr.length) {
							opd = (IOpd) arr[i];
							this.PrintOpd(opd, file, filename);
							i++;
						}// of while
						temp = "<H2><A NAME=\"OPL\">OPL</A></U></H2>";
						file.write(temp);

						temp_opds = MyStructure.getOpds();
						opds_a = this.DocInfo.opdopl.getOPLList1();
						Arrays.sort(opds_a);
						i = 0;
						while (i < opds_a.length) {
							while (temp_opds.hasMoreElements()) {
								opd = (IOpd) temp_opds.nextElement();
								if (opd.getName().compareTo(
										opds_a[i].toString()) == 0) {
									temp_long = opd.getOpdId();
									opl = this.mySys.getOPL(true, temp_long);
									temp = "<P>";
									file.write(temp);
									file.write(opl);
									break;
								}// of if
							}// of while
							i++;
							temp_opds = MyStructure.getOpds();
						}// while
					}// of if

					// opd by name, opl all
					if ((this.DocInfo.opdopl.getOPDByName())
							&& (this.DocInfo.opdopl.getOPLAll())) {
						temp = "<H2><A NAME=\"OPD\">OPD</A></U></H2>";
						file.write(temp);
						temp_opds = MyStructure.getOpds();
						opds_a = this.DocInfo.opdopl.getOPDList1();
						Arrays.sort(opds_a);
						i = 0;
						while (i < opds_a.length) {
							while (temp_opds.hasMoreElements()) {
								opd = (IOpd) temp_opds.nextElement();
								if (opd.getName().compareTo(
										opds_a[i].toString()) == 0) {
									temp_long = opd.getOpdId();
									this.PrintOpd(opd, file, filename);
									temp = "<P>";
									file.write(temp);
									break;
								}// of if
							}// of while
							i++;
							temp_opds = MyStructure.getOpds();
						}// of while
						temp = "<H2><A NAME=\"OPL\">OPL</A></U></H2>";
						file.write(temp);
						opl = this.mySys.getOPL(true);
						file.write(opl);
					}// of if

					// opd by name, opl by name
					if ((this.DocInfo.opdopl.getOPDByName())
							&& (this.DocInfo.opdopl.getOPLByName())) {
						temp = "<H2><A NAME=\"OPD\">OPD</A></U></H2>";
						file.write(temp);
						temp_opds = MyStructure.getOpds();
						opds_a = this.DocInfo.opdopl.getOPDList1();
						i = 0;
						Arrays.sort(opds_a);
						while (i < opds_a.length) {
							while (temp_opds.hasMoreElements()) {
								opd = (IOpd) temp_opds.nextElement();
								if (opd.getName().compareTo(
										opds_a[i].toString()) == 0) {
									temp_long = opd.getOpdId();
									this.PrintOpd(opd, file, filename);
									temp = "<P>";
									file.write(temp);
									break;
								}// of if
							}// of while
							i++;
							temp_opds = MyStructure.getOpds();
						}// of while
						temp = "<H2><A NAME=\"OPL\">OPL</A></U></H2>";
						file.write(temp);

						temp_opds = MyStructure.getOpds();
						opds_a = this.DocInfo.opdopl.getOPLList1();
						i = 0;
						Arrays.sort(opds_a);
						while (i < opds_a.length) {
							while (temp_opds.hasMoreElements()) {
								opd = (IOpd) temp_opds.nextElement();
								if (opd.getName().compareTo(
										opds_a[i].toString()) == 0) {
									temp_long = opd.getOpdId();
									opl = this.mySys.getOPL(true, temp_long);
									temp = "<P>";
									file.write(temp);
									file.write(opl);
									break;
								}// of if
							}// of while
							i++;
							temp_opds = MyStructure.getOpds();
						}// while
					}// of if

					// opd by name, opl none
					if ((this.DocInfo.opdopl.getOPDByName())
							&& (this.DocInfo.opdopl.getOPLNone())) {
						temp = "<H2><A NAME=\"OPD\">OPD</A></U></H2>";
						file.write(temp);
						temp_opds = MyStructure.getOpds();
						opds_a = this.DocInfo.opdopl.getOPDList1();
						Arrays.sort(opds_a);
						size = 0;
						while (size < opds_a.length) {
							while (temp_opds.hasMoreElements()) {
								opd = (IOpd) temp_opds.nextElement();
								if (opd.getName().compareTo(
										opds_a[size].toString()) == 0) {
									temp_long = opd.getOpdId();
									this.PrintOpd(opd, file, filename);
									temp = "<P>";
									file.write(temp);
									break;
								}// of if
							}// of while
							size++;
							temp_opds = MyStructure.getOpds();
						}// of while
					}// of if

					// opd none, opl by name
					if ((this.DocInfo.opdopl.getOPDNone())
							&& (this.DocInfo.opdopl.getOPLByName())) {
						temp = "<H2><A NAME=\"OPL\">OPL</A></U></H2>";
						file.write(temp);
						temp_opds = MyStructure.getOpds();
						opds_a = this.DocInfo.opdopl.getOPLList1();
						Arrays.sort(opds_a);
						i = 0;
						while (i < opds_a.length) {
							while (temp_opds.hasMoreElements()) {
								opd = (IOpd) temp_opds.nextElement();
								if (opd.getName().compareTo(
										opds_a[i].toString()) == 0) {
									temp_long = opd.getOpdId();
									opl = this.mySys.getOPL(true, temp_long);
									temp = "<P>";
									file.write(temp);
									file.write(opl);
									break;
								}// of if
							}// of while
							i++;
							temp_opds = MyStructure.getOpds();
						}// while
					}// of if

					// opd until, opl none
					if (((this.DocInfo.opdopl.getOPDUntil()).compareTo("-1") != 0)
							&& (this.DocInfo.opdopl.getOPLNone())) {
						temp = "<H2><A NAME=\"OPD\">OPD</A></U></H2>";
						file.write(temp);
						int lev = Integer
								.parseInt(this.DocInfo.opdopl.getOPDUntil());
						i = 0;
						while (i < arr.length) {
							opd = (IOpd) arr[i];
							if (this.level(opd.getName()) <= lev) {
								this.PrintOpd(opd, file, filename);
							}
							i++;
						}// of while
					}// of if

					// opd none, opl untill
					if ((this.DocInfo.opdopl.getOPDNone())
							&& (this.DocInfo.opdopl.getOPLUntil().compareTo("-1") != 0)) {
						temp = "<H2><A NAME=\"OPL\">OPL</A></U></H2>";
						file.write(temp);
						int lev = Integer
								.parseInt(this.DocInfo.opdopl.getOPLUntil());
						i = 0;
						while (i < arr.length) {
							opd = (IOpd) arr[i];
							if (this.level(opd.getName()) <= lev) {
								temp_long = opd.getOpdId();
								opl = this.mySys.getOPL(true, temp_long);
								temp = "<P>";
								file.write(temp);
								file.write(opl);
							}// of if
							i++;
						}// of while
					}// of if

					// opd all, opl untill
					if ((this.DocInfo.opdopl.getOPDAll())
							&& (this.DocInfo.opdopl.getOPLUntil().compareTo("-1") != 0)) {
						temp = "<H2><A NAME=\"OPD\">OPD</A></U></H2>";
						file.write(temp);
						i = 0;
						while (i < arr.length) {
							opd = (IOpd) arr[i];
							this.PrintOpd(opd, file, filename);
							i++;
						}// of while
						temp = "<H2><A NAME=\"OPL\">OPL</A></U></H2>";
						file.write(temp);
						i = 0;
						int lev = Integer
								.parseInt(this.DocInfo.opdopl.getOPLUntil());
						while (i < arr.length) {
							opd = (IOpd) arr[i];
							if (this.level(opd.getName()) <= lev) {
								temp_long = opd.getOpdId();
								opl = this.mySys.getOPL(true, temp_long);
								temp = "<P>";
								file.write(temp);
								file.write(opl);
							}// of if
							i++;
						}// of while
					}// of if

					// opd untill, opl all
					if ((this.DocInfo.opdopl.getOPDUntil().compareTo("-1") != 0)
							&& (this.DocInfo.opdopl.getOPLAll())) {
						temp = "<H2><A NAME=\"OPD\">OPD</A></U></H2>";
						file.write(temp);
						int lev = Integer
								.parseInt(this.DocInfo.opdopl.getOPDUntil());
						i = 0;
						while (i < arr.length) {
							opd = (IOpd) arr[i];
							if (this.level(opd.getName()) <= lev) {
								this.PrintOpd(opd, file, filename);
							}
							i++;
						}// of while
						temp = "<H2><A NAME=\"OPL\">OPL</A></U></H2>";
						file.write(temp);
						temp = "<P>";
						file.write(temp);
						opl = this.mySys.getOPL(true);
						file.write(opl);
					}// of if

					// opd untill, opl untill
					if ((this.DocInfo.opdopl.getOPDUntil().compareTo("-1") != 0)
							&& (this.DocInfo.opdopl.getOPLUntil().compareTo("-1") != 0)) {
						temp = "<H2><A NAME=\"OPD\">OPD</A></U></H2>";
						file.write(temp);
						i = 0;
						int lev = Integer
								.parseInt(this.DocInfo.opdopl.getOPDUntil());
						while (i < arr.length) {
							opd = (IOpd) arr[i];
							if (this.level(opd.getName()) <= lev) {
								this.PrintOpd(opd, file, filename);
							}
							i++;
						}// of while
						temp = "<H2><A NAME=\"OPL\">OPL</A></U></H2>";
						file.write(temp);
						i = 0;
						lev = Integer.parseInt(this.DocInfo.opdopl.getOPLUntil());
						while (i < arr.length) {
							opd = (IOpd) arr[i];
							if (this.level(opd.getName()) <= lev) {
								temp_long = opd.getOpdId();
								opl = this.mySys.getOPL(true, temp_long);
								temp = "<P>";
								file.write(temp);
								file.write(opl);
							}// of if
							i++;
						}// of while
					}// of if

					// opd by name, opl untill
					if ((this.DocInfo.opdopl.getOPDByName())
							&& (this.DocInfo.opdopl.getOPLUntil().compareTo("-1") != 0)) {
						temp = "<H2><A NAME=\"OPD\">OPD</A></U></H2>";
						file.write(temp);
						temp_opds = MyStructure.getOpds();
						i = 0;
						opds_a = this.DocInfo.opdopl.getOPDList1();
						Arrays.sort(opds_a);
						while (i < opds_a.length) {
							while (temp_opds.hasMoreElements()) {
								opd = (IOpd) temp_opds.nextElement();
								if (opd.getName().compareTo(
										opds_a[i].toString()) == 0) {
									temp_long = opd.getOpdId();
									this.PrintOpd(opd, file, filename);
									temp = "<P>";
									file.write(temp);
									break;
								}// of if
							}// of while
							i++;
							temp_opds = MyStructure.getOpds();
						}// of while
						temp = "<H2><A NAME=\"OPL\">OPL</A></U></H2>";
						file.write(temp);
						int lev = Integer
								.parseInt(this.DocInfo.opdopl.getOPLUntil());
						i = 0;
						while (i < arr.length) {
							opd = (IOpd) arr[i];
							if (this.level(opd.getName()) <= lev) {
								temp_long = opd.getOpdId();
								opl = this.mySys.getOPL(true, temp_long);
								temp = "<P>";
								file.write(temp);
								file.write(opl);
							}// of if
							i++;
						}// of while
					}// of if

					// opd untill, opl by name
					if ((this.DocInfo.opdopl.getOPDUntil().compareTo("-1") != 0)
							&& (this.DocInfo.opdopl.getOPLByName())) {
						temp = "<H2><A NAME=\"OPD\">OPD</A></U></H2>";
						file.write(temp);
						int lev = Integer
								.parseInt(this.DocInfo.opdopl.getOPDUntil());
						i = 0;
						while (i < arr.length) {
							opd = (IOpd) arr[i];
							if (this.level(opd.getName()) <= lev) {
								this.PrintOpd(opd, file, filename);
							}
							i++;
						}// of while
						temp = "<H2><A NAME=\"OPL\">OPL</A></U></H2>";
						file.write(temp);
						temp_opds = MyStructure.getOpds();
						opds_a = this.DocInfo.opdopl.getOPLList1();
						i = 0;
						Arrays.sort(opds_a);
						while (i < opds_a.length) {
							while (temp_opds.hasMoreElements()) {
								opd = (IOpd) temp_opds.nextElement();
								if (opd.getName().compareTo(
										opds_a[i].toString()) == 0) {
									temp_long = opd.getOpdId();
									opl = this.mySys.getOPL(true, temp_long);
									temp = "<P>";
									file.write(temp);
									file.write(opl);
									break;
								}// of if
							}// of while
							i++;
							temp_opds = MyStructure.getOpds();
						}// while
					}// of if
				}// of else

				temp = "<HR>";
				file.write(temp);
			}
			// ------------------------------------------------End of
			// OPDs-----------------------------------------------------------------------------------------------------
			// ------------------------------------------------Elements
			// Dictionary--------------------------------------------------------------------------------------
			// after the opl paragraph, return the font
			temp =  "<BASEFONT FACE=\"Georgia\", SIZE=2>";
			file.write(temp);
			// if elements dictionary should be printed
			if ((this.DocInfo.Data.isSelected())
					&& ((!obj_v.isEmpty()) || (!proc_v.isEmpty())
							|| (!link_v.isEmpty()) || (!rel_v.isEmpty()))) {
				temp = "<H2 ALIGN=\"center\"><FONT COLOR = NAVY><U><A NAME=\"ELEMENTS DICTIONARY\">ELEMENTS DICTIONARY</A></FONT></U></H2>";
				file.write(temp);
				// ------------------------------------------------Things-------------------------------------------------------------------------------------------------
				if (((this.DocInfo.Data.Obj.isSelected()) || (this.DocInfo.Data.Proc
						.isSelected()))
						&& ((!obj_v.isEmpty()) || (!proc_v.isEmpty()))) {
					temp = "<H3><FONT COLOR = TEAL><A NAME=\"Things\">Things</A></FONT></H3>";
					file.write(temp);
					// ------------------------------------------------Objects--------------------------------------------------------------------------------------
					if (this.DocInfo.Data.Obj.isSelected() && (!obj_v.isEmpty())) {
						temp = "<FONT size=2><U><A NAME=\"Objects\">Objects</A></U><P><DL>";
						file.write(temp);
						IObjectEntry arr[] = new IObjectEntry[obj_v.size()];
						obj_v.copyInto(arr);
						compar cmp = new compar();
						Arrays.sort(arr, cmp);// make a sorted array of
						// objects
						int i = 0;
						// for each object
						while (i < arr.length) {
							obj = (IObjectEntry) arr[i];
							i++;
							temp = "<P><DT>Object Name: " + obj.getName();
							file.write(temp);
							Enumeration objEnum = obj.getInstances();
							while (objEnum.hasMoreElements()) {
								IInstance ins = (IInstance) objEnum
										.nextElement();
								if (this.picDirName.equalsIgnoreCase("")) {
									temp = "<DD>Object Opd: "
											+ sys
													.getISystemStructure()
													.getIOpd(
															ins.getKey()
																	.getOpdId())
													.getName();
								} else {
									temp = "<DD>Object Opd: <a href=\""
											+ this.picDirName
											+ fileSeparator
											+ ""
											+ sys
													.getISystemStructure()
													.getIOpd(
															ins.getKey()
																	.getOpdId())
													.getName()
											+ "_"
											+ ""
											+ ins.getKey().getOpdId()
											+ "_OPDfile.jpeg\">"
											+ sys
													.getISystemStructure()
													.getIOpd(
															ins.getKey()
																	.getOpdId())
													.getName() + "</a>";
								}
								file.write(temp);
							}

							// if the type should be printed
							if ((this.DocInfo.Data.Obj.getType())
									&& (obj.getType().compareTo("") != 0)) {
								temp = "<DD>Object Type: " + obj.getType();
								file.write(temp);
							}
							// if the description should be printed
							if ((this.DocInfo.Data.Obj.getDesc())
									&& (obj.getDescription().compareTo("none") != 0)) {
								temp = "<DD>Description: "
										+ obj.getDescription();
								file.write(temp);
							}
							// /if initial value should be printed
							if ((this.DocInfo.Data.Obj.getInValue())
									&& (obj.getInitialValue().compareTo("") != 0)) {
								temp = "<DD>Initial Value: "
										+ obj.getInitialValue();
								file.write(temp);
							}
							// if essence should be printed
							if (this.DocInfo.Data.Obj.getEssence()) {
								if (obj.isPhysical()) {
									temp = "<DD>Essence: Phisical";
								} else {
									temp = "<DD>Essence: Informatical";
								}
								file.write(temp);
							}
							// if origin should be printed
							if (this.DocInfo.Data.Obj.getOrigin()) {
								if (obj.isEnvironmental()) {
									temp = "<DD>Origin: Environmental";
								} else {
									temp = "<DD>Origin: Systemic";
								}
								file.write(temp);
							}
							// if scope should be printed
							if (this.DocInfo.Data.Obj.getScope()) {
								if (obj.getScope().compareTo("0") == 0) {
									temp = "<DD>Scope: Public";
								}
								if (obj.getScope().compareTo("1") == 0) {
									temp = "<DD>Scope: Protected";
								}
								if (obj.getScope().compareTo("2") == 0) {
									temp = "<DD>Scope: Private";
								}
								file.write(temp);
							}
							// if index should be printed
							if ((this.DocInfo.Data.Obj.getIndex())
									&& (obj.getIndexName().compareTo("") != 0)) {
								temp = "<DD>Index: Field "
										+ obj.getIndexOrder() + " in Index "
										+ obj.getIndexName();
								file.write(temp);
							}
							// for states of the object
							state_e = obj.getStates();
							// if states should be printed
							if ((this.DocInfo.Data.Obj.getStates())
									&& (state_e.hasMoreElements())) {
								Vector state_v = new Vector(1, 1);
								while (state_e.hasMoreElements()) {
									state_v.addElement(state_e.nextElement());
								}
								IStateEntry arr_s[] = new IStateEntry[state_v
										.size()];
								state_v.copyInto(arr_s);
								compar c = new compar();
								Arrays.sort(arr_s, c);// sort the states in
								// ABC order
								temp = "<DD><DL><DT>State: ";
								file.write(temp);
								int j = 0;
								// /for each state
								while (j < arr_s.length) {
									state = (IStateEntry) arr_s[j];
									j++;
									// print state name
									temp = "<DD><DL><DT>State Name: "
											+ state.getName();
									file.write(temp);
									// if description should be printed
									if ((this.DocInfo.Data.Obj.getStateDesc())
											&& (state.getDescription()
													.compareTo("None") != 0)) {
										temp = "<DD>Description: "
												+ state.getDescription();
										file.write(temp);
									}
									// if state times should be printed
									if (this.DocInfo.Data.Obj.getStateTime()) {
										// if mintime!=0
										if (state.getMinTime().compareTo(
												"0;0;0;0;0;0;0") != 0) {
											temp = "<DD>Min Activation Time: ";
											file.write(temp);
											this.printActTime(state.getMinTime(),
													file);
										}
										// if maxtime != infinty
										if (state.getMaxTime().compareTo(
												"infinity") != 0) {
											temp = "<DD>Max Activation Time: ";
											file.write(temp);
											this.printActTime(state.getMaxTime(),
													file);
										}
									}
									// if initial/final should be printed
									if (this.DocInfo.Data.Obj.getStateInitial()) {
										if (state.isInitial()) {
											temp = "<DD>Initial";
											file.write(temp);
										}
										if (state.isFinal()) {
											temp = "<DD>Final";
											file.write(temp);
										}
									}
									temp = "</DL>";
									file.write(temp);
								}// state while end
								temp = "</DL>";
								file.write(temp);
							}// state if end
						}// obj while end
						temp = "</DL><P>";
						file.write(temp);
					}// obj if end
					// ------------------------------------------------end of
					// Objects--------------------------------------------------------------------------------------
					// ------------------------------------------------Processes--------------------------------------------------------------------------------------

					// if processes should be printed
					if ((this.DocInfo.Data.Proc.isSelected()) && (!proc_v.isEmpty())) {
						temp = "<U><A NAME=\"Processes\">Processes</A></U><P><DL>";
						file.write(temp);
						IProcessEntry arr_p[] = new IProcessEntry[proc_v.size()];
						proc_v.copyInto(arr_p);
						compar cmp = new compar();
						Arrays.sort(arr_p, cmp);// sort processes in ABC order
						int i = 0;
						// for each process
						while (i < arr_p.length) {
							proc = (IProcessEntry) arr_p[i];
							i++;
							// process name
							temp = "<DT>Process Name: " + proc.getName();
							file.write(temp);

							Enumeration procEnum = proc.getInstances();
							while (procEnum.hasMoreElements()) {
								IInstance ins = (IInstance) procEnum
										.nextElement();
								if (this.picDirName.equalsIgnoreCase("")) {
									temp = "<DD>Process Opd: "
											+ sys
													.getISystemStructure()
													.getIOpd(
															ins.getKey()
																	.getOpdId())
													.getName();
								} else {
									temp = "<DD>Process Opd: <a href=\""
											+ this.picDirName
											+ fileSeparator
											+ ""
											+ sys
													.getISystemStructure()
													.getIOpd(
															ins.getKey()
																	.getOpdId())
													.getName()
											+ "_"
											+ ""
											+ ins.getKey().getOpdId()
											+ "_OPDfile.jpeg\">"
											+ sys
													.getISystemStructure()
													.getIOpd(
															ins.getKey()
																	.getOpdId())
													.getName() + "</a>";
								}
								file.write(temp);
							}
							// if description should be printed
							if ((this.DocInfo.Data.Proc.getDesc())
									&& (proc.getDescription().compareTo("none") != 0)) {
								temp = "<DD>Description: "
										+ proc.getDescription();
								file.write(temp);
							}
							// if essence should be printed
							if (this.DocInfo.Data.Proc.getEssence()) {
								if (proc.isPhysical()) {
									temp = "<DD>Essence: Phisical";
								} else {
									temp = "<DD>Essence: Informatical";
								}
								file.write(temp);
							}
							// if origin should be printed
							if (this.DocInfo.Data.Proc.getOrigin()) {
								if (proc.isEnvironmental()) {
									temp = "<DD>Origin: Environmental";
								} else {
									temp = "<DD>Origin: Systemic";
								}
								file.write(temp);
							}
							// if scope should be printed
							if (this.DocInfo.Data.Proc.getScope()) {
								if (proc.getScope().compareTo("0") == 0) {
									temp = "<DD>Scope: Public";
								}
								if (proc.getScope().compareTo("1") == 0) {
									temp = "<DD>Scope: Protected";
								}
								if (proc.getScope().compareTo("2") == 0) {
									temp = "<DD>Scope: Private";
								}
								file.write(temp);
							}
							// if activation time should be printed
							if (this.DocInfo.Data.Proc.getActTime()) {
								// if mintime!=0
								if (proc.getMinTimeActivation().compareTo(
										"0;0;0;0;0;0;0") != 0) {
									temp = "<DD>Min activation time: ";
									file.write(temp);
									this.printActTime(proc.getMinTimeActivation(),
											file);
								}
								// if maxtime!=infinity
								if (proc.getMaxTimeActivation().compareTo(
										"infinity") != 0) {
									temp = "<DD>Max activation time: ";
									file.write(temp);
									this.printActTime(proc.getMaxTimeActivation(),
											file);
								}
							}
							// if process body should be printed
							StringTokenizer st = new StringTokenizer(proc
									.getProcessBody(), "\n", false);
							if ((this.DocInfo.Data.Proc.getBody())
									&& (proc.getProcessBody().compareTo("none") != 0)) {
								temp = "<DD>Body: ";
								file.write(temp);
								while (st.hasMoreTokens()) {
									temp = st.nextToken();
									file.write(temp);
								}

							}
							temp = "</P>";
							file.write(temp);
						}// proc while end
						temp = "</DL>";
						file.write(temp);
					}// proc if end
					// ------------------------------------------------end of
					// Processes--------------------------------------------------------------------------------------
					temp = "<HR>";
					file.write(temp);
				}// things if end
				// ------------------------------------------------end of
				// Things--------------------------------------------------------------------------------------------

				// ------------------------------------------------Relations--------------------------------------------------------------------------------------------
				// if relations should be printed
				if ((this.DocInfo.Data.Rel.isSelected()) && (!rel_v.isEmpty())) {
					temp = "<H3><FONT COLOR = TEAL><A NAME=\"Relations\">Relations</A></FONT></H3><FONT size=2>";
					file.write(temp);
					// if aggregaton rel should be printed
					if ((this.DocInfo.Data.Rel.getAgPar()) && (!aggr_r_v.isEmpty())) {
						IRelationEntry arr_aggr_r[] = new IRelationEntry[aggr_r_v
								.size()];
						aggr_r_v.copyInto(arr_aggr_r);
						compRel cmp = new compRel(this.mySys);
						Arrays.sort(arr_aggr_r, cmp);// sort by ABC of source
						// and destination
						int i = 0;
						temp = "<U>Aggregation-Participation Relations:</U><DL>";
						file.write(temp);
						while (i < arr_aggr_r.length) {
							temp = "<P>";
							file.write(temp);
							rel = (IRelationEntry) arr_aggr_r[i];
							this.PrintRel(rel, MyStructure, file);
							i++;
						}
						temp = "</DL>";
						file.write(temp);
					}
					// if feature characterization rel should be printed
					if ((this.DocInfo.Data.Rel.getFeChar()) && (!feat_r_v.isEmpty())) {
						IRelationEntry arr_feat_r[] = new IRelationEntry[feat_r_v
								.size()];
						feat_r_v.copyInto(arr_feat_r);
						compRel cmp = new compRel(this.mySys);
						Arrays.sort(arr_feat_r, cmp);// sort by ABC of source
						// and destination
						int i = 0;
						temp = "<U>Featuring-Characterization Relations: </U><DL>";
						file.write(temp);
						while (i < arr_feat_r.length) {
							temp = "<P>";
							file.write(temp);
							rel = (IRelationEntry) arr_feat_r[i];
							this.PrintRel(rel, MyStructure, file);
							i++;
						}
						temp = "</DL>";
						file.write(temp);
					}
					// if Generalization rel should be printed
					if ((this.DocInfo.Data.Rel.getGenSpec()) && (!gen_r_v.isEmpty())) {
						IRelationEntry arr_gen_r[] = new IRelationEntry[gen_r_v
								.size()];
						gen_r_v.copyInto(arr_gen_r);
						compRel cmp = new compRel(this.mySys);
						Arrays.sort(arr_gen_r, cmp);// sort by ABC of source and
						// destination
						int i = 0;
						temp = "<U>Generalization-Specification Relations: </U><DL>";
						file.write(temp);
						while (i < arr_gen_r.length) {
							temp = "<P>";
							file.write(temp);
							rel = (IRelationEntry) arr_gen_r[i];
							this.PrintRel(rel, MyStructure, file);
							i++;
						}
						temp = "</DL>";
						file.write(temp);
					}
					// if Classification rel should be printed
					if ((this.DocInfo.Data.Rel.getClassInst())
							&& (!class_r_v.isEmpty())) {
						IRelationEntry arr_class_r[] = new IRelationEntry[class_r_v
								.size()];
						class_r_v.copyInto(arr_class_r);
						compRel cmp = new compRel(this.mySys);
						Arrays.sort(arr_class_r, cmp);// sort by ABC of source
						// and destination
						int i = 0;
						temp = "<U>Classification-Instantiation Relations: </U><DL>";
						file.write(temp);
						while (i < arr_class_r.length) {
							temp = "<P>";
							file.write(temp);
							rel = (IRelationEntry) arr_class_r[i];
							this.PrintRel(rel, MyStructure, file);
							i++;
						}
						temp = "</DL>";
						file.write(temp);
					}
					// if Uni-directional rel should be printed
					if ((this.DocInfo.Data.Rel.getUniDir()) && (!uni_r_v.isEmpty())) {
						IRelationEntry arr_uni_r[] = new IRelationEntry[uni_r_v
								.size()];
						uni_r_v.copyInto(arr_uni_r);
						compRel cmp = new compRel(this.mySys);
						Arrays.sort(arr_uni_r, cmp);// sort by ABC of source and
						// destination
						int i = 0;
						temp = "<U>Uni-directional General Relations: </U><DL>";
						file.write(temp);
						while (i < arr_uni_r.length) {
							temp = "<P>";
							file.write(temp);
							rel = (IRelationEntry) arr_uni_r[i];
							i++;
							// if forward meaning should be printed
							if (rel.getForwardRelationMeaning().compareTo("") != 0) {
								temp = "<DT>Meaning: "
										+ rel.getForwardRelationMeaning();
								file.write(temp);
							}
							this.PrintRel(rel, MyStructure, file);
						}
						temp = "</DL>";
						file.write(temp);
					}

					// if Bi-directional rel should be printed
					if ((this.DocInfo.Data.Rel.getBiDir()) && (!bi_r_v.isEmpty())) {
						IRelationEntry arr_bi_r[] = new IRelationEntry[bi_r_v
								.size()];
						bi_r_v.copyInto(arr_bi_r);
						compRel cmp = new compRel(this.mySys);
						Arrays.sort(arr_bi_r, cmp);// sort by ABC of source and
						// destination
						int i = 0;
						temp = "<U>Bi-directional General Relations: </U><DL>";
						file.write(temp);
						while (i < arr_bi_r.length) {
							temp = "<P>";
							file.write(temp);
							rel = (IRelationEntry) arr_bi_r[i];
							i++;
							// if forward meaning should be printed
							if (rel.getForwardRelationMeaning().compareTo("") != 0) {
								temp = "<DT>Meaning: "
										+ rel.getForwardRelationMeaning();
								file.write(temp);
							}
							// if Backward meaning should be printed
							if (rel.getBackwardRelationMeaning().compareTo("") != 0) {
								temp = "<DT>Meaning: "
										+ rel.getBackwardRelationMeaning();
								file.write(temp);
							}
							this.PrintRel(rel, MyStructure, file);
						}
						temp = "</DL>";
						file.write(temp);
					}
					temp = "<HR>";
					file.write(temp);
				}
				// ------------------------------------------------end of
				// Relations--------------------------------------------------------------------------------------------
				// ------------------------------------------------Links--------------------------------------------------------------------------------------------------------------
				if ((this.DocInfo.Data.Link.isSelected()) && (!link_v.isEmpty())) {
					temp = "<H3><FONT COLOR = TEAL><A NAME=\"Links\">Links</A></FONT></H3><FONT size=2>";
					file.write(temp);
					// Agent Links
					if ((this.DocInfo.Data.Link.getAgent())
							&& (!agent_l_v.isEmpty())) {
						ILinkEntry arr_agent_l[] = new ILinkEntry[agent_l_v
								.size()];
						agent_l_v.copyInto(arr_agent_l);
						compLink cmp = new compLink(this.mySys);
						Arrays.sort(arr_agent_l, cmp);
						int i = 0;
						temp = "<U>Agent Links:</U><DL>";
						file.write(temp);
						while (i < arr_agent_l.length) {
							link = (ILinkEntry) arr_agent_l[i];
							this.PrintLink(link, MyStructure, file);
							i++;
						}
						temp = "</DL>";
						file.write(temp);
					}
					// Instrument Links
					if ((this.DocInfo.Data.Link.getInstrument())
							&& (!instr_l_v.isEmpty())) {
						ILinkEntry arr_instr_l[] = new ILinkEntry[instr_l_v
								.size()];
						instr_l_v.copyInto(arr_instr_l);
						compLink cmp = new compLink(this.mySys);
						Arrays.sort(arr_instr_l, cmp);
						int i = 0;
						temp = "<U>Instrument Links:</U><DL>";
						file.write(temp);
						while (i < arr_instr_l.length) {
							link = (ILinkEntry) arr_instr_l[i];
							this.PrintLink(link, MyStructure, file);
							i++;
						}
						temp = "</DL>";
						file.write(temp);
					}
					// Result Links
					if ((this.DocInfo.Data.Link.getResCons())
							&& (!res_l_v.isEmpty())) {
						ILinkEntry arr_res_l[] = new ILinkEntry[res_l_v.size()];
						res_l_v.copyInto(arr_res_l);
						compLink cmp = new compLink(this.mySys);
						Arrays.sort(arr_res_l, cmp);
						int i = 0;
						temp = "<U>Result Links:</U><DL>";
						file.write(temp);
						while (i < arr_res_l.length) {
							link = (ILinkEntry) arr_res_l[i];
							this.PrintLink(link, MyStructure, file);
							i++;
						}
						temp = "</DL>";
						file.write(temp);
					}
					// Consumption Links
					if ((this.DocInfo.Data.Link.getResCons())
							&& (!cons_l_v.isEmpty())) {
						ILinkEntry arr_cons_l[] = new ILinkEntry[cons_l_v
								.size()];
						cons_l_v.copyInto(arr_cons_l);
						compLink cmp = new compLink(this.mySys);
						Arrays.sort(arr_cons_l, cmp);
						int i = 0;
						temp = "<U>Consumption Links:</U><DL>";
						file.write(temp);
						while (i < arr_cons_l.length) {
							link = (ILinkEntry) arr_cons_l[i];
							this.PrintLink(link, MyStructure, file);
							i++;
						}
						temp = "</DL>";
						file.write(temp);
					}
					// Effect Links
					if ((this.DocInfo.Data.Link.getEffect())
							&& (!effect_l_v.isEmpty())) {
						ILinkEntry arr_effect_l[] = new ILinkEntry[effect_l_v
								.size()];
						effect_l_v.copyInto(arr_effect_l);
						compLink cmp = new compLink(this.mySys);
						Arrays.sort(arr_effect_l, cmp);
						int i = 0;
						temp = "<U>Effect Links:</U><DL>";
						file.write(temp);
						while (i < arr_effect_l.length) {
							link = (ILinkEntry) arr_effect_l[i];
							this.PrintLink(link, MyStructure, file);
							i++;
						}
						temp = "</DL>";
						file.write(temp);
					}
					// Event Links
					if ((this.DocInfo.Data.Link.getEvent())
							&& (!cons_ev_l_v.isEmpty())) {
						ILinkEntry arr_cons_ev_l[] = new ILinkEntry[cons_ev_l_v
								.size()];
						cons_ev_l_v.copyInto(arr_cons_ev_l);
						compLink cmp = new compLink(this.mySys);
						Arrays.sort(arr_cons_ev_l, cmp);
						int i = 0;
						temp = "<U>Event Links:</U><DL>";
						file.write(temp);
						while (i < arr_cons_ev_l.length) {
							link = (ILinkEntry) arr_cons_ev_l[i];
							this.PrintLink(link, MyStructure, file);
							i++;
						}
						temp = "</DL>";
						file.write(temp);
					}
					// Instrument Event Links
					if ((this.DocInfo.Data.Link.getInstEvent())
							&& (!instr_ev_l_v.isEmpty())) {
						ILinkEntry arr_instr_ev_l[] = new ILinkEntry[instr_ev_l_v
								.size()];
						instr_ev_l_v.copyInto(arr_instr_ev_l);
						compLink cmp = new compLink(this.mySys);
						Arrays.sort(arr_instr_ev_l, cmp);
						int i = 0;
						temp = "<U>Instrument Event Links:</U><DL>";
						file.write(temp);
						while (i < arr_instr_ev_l.length) {
							link = (ILinkEntry) arr_instr_ev_l[i];
							this.PrintLink(link, MyStructure, file);
							i++;
						}
						temp = "</DL>";
						file.write(temp);
					}
					// Condition Links
					if ((this.DocInfo.Data.Link.getCondition())
							&& (!cond_l_v.isEmpty())) {
						ILinkEntry arr_cond_l[] = new ILinkEntry[cond_l_v
								.size()];
						cond_l_v.copyInto(arr_cond_l);
						compLink cmp = new compLink(this.mySys);
						Arrays.sort(arr_cond_l, cmp);
						int i = 0;
						temp = "<U>Condition Links:</U><DL>";
						file.write(temp);
						while (i < arr_cond_l.length) {
							link = (ILinkEntry) arr_cond_l[i];
							this.PrintLink(link, MyStructure, file);
							i++;
						}
						temp = "</DL>";
						file.write(temp);
					}
					// Exception Links
					if ((this.DocInfo.Data.Link.getException())
							&& (!exception_l_v.isEmpty())) {
						ILinkEntry arr_exception_l[] = new ILinkEntry[exception_l_v
								.size()];
						exception_l_v.copyInto(arr_exception_l);
						compLink cmp = new compLink(this.mySys);
						Arrays.sort(arr_exception_l, cmp);
						int i = 0;
						temp = "<U>Exception Links:</U><DL>";
						file.write(temp);
						while (i < arr_exception_l.length) {
							link = (ILinkEntry) arr_exception_l[i];
							this.PrintLink(link, MyStructure, file);
							i++;
						}
						temp = "</DL>";
						file.write(temp);
					}
					// Invocation Links
					if ((this.DocInfo.Data.Link.getInvocation())
							&& (!invoc_l_v.isEmpty())) {
						ILinkEntry arr_invoc_l[] = new ILinkEntry[invoc_l_v
								.size()];
						invoc_l_v.copyInto(arr_invoc_l);
						compLink cmp = new compLink(this.mySys);
						Arrays.sort(arr_invoc_l, cmp);
						int i = 0;
						temp = "<U>Invocation Links:</U><DL>";
						file.write(temp);
						while (i < arr_invoc_l.length) {
							link = (ILinkEntry) arr_invoc_l[i];
							this.PrintLink(link, MyStructure, file);
							i++;
						}
						temp = "</DL>";
						file.write(temp);
					}
				}
				// ------------------------------------------------end of
				// Links-------------------------------------------------------------------------------------------------------
			}
			// ------------------------------------------------end of Elements
			// Dictionary-------------------------------------------------------------------------------
			temp = "</BODY></HTML>";
			file.write(temp);
			file.close();
		} catch (IOException e) {
			System.out.println("Error printing a document");
			return;
		}
	}// end of PrintDocument

	public static void PrintCSVFile() {
		JFileChooser fc = new JFileChooser();

		int returnVal = fc.showOpenDialog(Opcat2.getFrame());

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File csv_file = fc.getSelectedFile();
			if (csv_file.exists()) {
				int ret = JOptionPane.showConfirmDialog(Opcat2.getFrame(),
						"File Exists, are you sure ?", "OPCAT II",
						JOptionPane.OK_CANCEL_OPTION);
				if (ret != JOptionPane.OK_OPTION) {
					return;
				}
			}
			StringTokenizer st = new StringTokenizer(csv_file.getPath(), ".",
					false);
			File file = new File(st.nextToken() + ".csv");
			Thread runner = new PrintCSV(file);
			runner.start();
		}

	}

	/**
	 * Creates a template file, that contains info regarding the selected fields
	 * in the document.
	 * 
	 * @throws IOException
	 * @param sys
	 *            of type ISystem
	 * @param filename
	 *            of type String
	 */

	public void CreateTemplate(ISystem sys, String filename) throws IOException {
		try {
			this.mySys = sys;
			FileOutputStream file;

			String dirName = "Templates"; // Directory name
			File dir = new File(dirName); // File object for directory
			if (!dir.exists()) {
				dir.mkdir(); // ...create it
			}

			File aFile = new File(dir, filename);
			aFile.createNewFile(); // Now create a new file if necessary

			file = new FileOutputStream(aFile);

			// for each field of genInfo put 1 if selected, else 0
			if (this.DocInfo.GI.getClient()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.GI.getOverview()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.GI.getCurrent()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.GI.getGoals()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.GI.getBusiness()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.GI.getFuture()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.GI.getHard()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.GI.getInputs()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.GI.getIssues()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.GI.getProblems()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.GI.getUsers()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.GI.getOper()) {
				file.write('1');
			} else {
				file.write('0');
			}

			// for each field of object put 1 if selected, else 0
			if (this.DocInfo.Data.Obj.getType()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Obj.getDesc()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Obj.getInValue()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Obj.getEssence()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Obj.getIndex()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Obj.getScope()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Obj.getOrigin()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Obj.getStates()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Obj.getStateDesc()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Obj.getStateInitial()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Obj.getStateTime()) {
				file.write('1');
			} else {
				file.write('0');
			}

			// for each field of process put 1 if selected, else 0
			if (this.DocInfo.Data.Proc.getDesc()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Proc.getEssence()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Proc.getOrigin()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Proc.getScope()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Proc.getBody()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Proc.getActTime()) {
				file.write('1');
			} else {
				file.write('0');
			}

			// for each field of relation put 1 if selected, else 0
			if (this.DocInfo.Data.Rel.getAgPar()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Rel.getClassInst()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Rel.getFeChar()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Rel.getGenSpec()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Rel.getBiDir()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Rel.getUniDir()) {
				file.write('1');
			} else {
				file.write('0');
			}

			// for each field of link put 1 if selected, else 0
			if (this.DocInfo.Data.Link.getAgent()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Link.getCondition()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Link.getEffect()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Link.getEvent()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Link.getException()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Link.getInstEvent()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Link.getInvocation()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Link.getResCons()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Link.getInstrument()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Link.getCond()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Link.getPath()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.Data.Link.getReactTime()) {
				file.write('1');
			} else {
				file.write('0');
			}

			// for each field of OPD put 1 if selected, else 0
			if (this.DocInfo.opdopl.getOPDAll()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.opdopl.getOPDByName()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.opdopl.getOPDNone()) {
				file.write('1');
			} else {
				file.write('0');
			}

			// for each field of OPL put 1 if selected, else 0
			if (this.DocInfo.opdopl.getOPLAll()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.opdopl.getOPLByName()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.opdopl.getOPLNone()) {
				file.write('1');
			} else {
				file.write('0');
			}

			if (this.DocInfo.opdopl.getOPLAccording()) {
				file.write('1');
			} else {
				file.write('0');
			}

			// for opd Until level put the value in the file
			file.write(this.DocInfo.opdopl.getOPDUntil().getBytes());
			file.write(';');
			// for opl Until level put the value in the file
			file.write(this.DocInfo.opdopl.getOPLUntil().getBytes());
			file.write(';');

		} catch (IOException e) {
			System.out.println("Error creating a template");
			return;
		}
	}// end of CreateTemplate

	/**
	 * Takes the information from the template file and puts it in the document.
	 * 
	 * @param inFile
	 *            the file containing the template
	 * @throws IOException
	 */
	public void FromTemp(FileInputStream inFile) throws IOException {
		try {
			FileInputStream file = inFile;

			if (file.read() == '1') {
				this.DocInfo.GI.setClient(true);
			} else {
				this.DocInfo.GI.setClient(false);
			}

			if (file.read() == '1') {
				this.DocInfo.GI.setOverview(true);
			} else {
				this.DocInfo.GI.setOverview(false);
			}

			if (file.read() == '1') {
				this.DocInfo.GI.setCurrent(true);
			} else {
				this.DocInfo.GI.setCurrent(false);
			}

			if (file.read() == '1') {
				this.DocInfo.GI.setGoals(true);
			} else {
				this.DocInfo.GI.setGoals(false);
			}

			if (file.read() == '1') {
				this.DocInfo.GI.setBusiness(true);
			} else {
				this.DocInfo.GI.setBusiness(false);
			}

			if (file.read() == '1') {
				this.DocInfo.GI.setFuture(true);
			} else {
				this.DocInfo.GI.setFuture(false);
			}

			if (file.read() == '1') {
				this.DocInfo.GI.setHard(true);
			} else {
				this.DocInfo.GI.setHard(false);
			}

			if (file.read() == '1') {
				this.DocInfo.GI.setInputs(true);
			} else {
				this.DocInfo.GI.setInputs(false);
			}

			if (file.read() == '1') {
				this.DocInfo.GI.setIssues(true);
			} else {
				this.DocInfo.GI.setIssues(false);
			}

			if (file.read() == '1') {
				this.DocInfo.GI.setProblems(true);
			} else {
				this.DocInfo.GI.setProblems(false);
			}

			if (file.read() == '1') {
				this.DocInfo.GI.setUsers(true);
			} else {
				this.DocInfo.GI.setUsers(false);
			}

			if (file.read() == '1') {
				this.DocInfo.GI.setOper(true);
			} else {
				this.DocInfo.GI.setOper(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Obj.setType(true);
			} else {
				this.DocInfo.Data.Obj.setType(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Obj.setDesc(true);
			} else {
				this.DocInfo.Data.Obj.setDesc(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Obj.setInValue(true);
			} else {
				this.DocInfo.Data.Obj.setInValue(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Obj.setEssence(true);
			} else {
				this.DocInfo.Data.Obj.setEssence(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Obj.setIndex(true);
			} else {
				this.DocInfo.Data.Obj.setIndex(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Obj.setScope(true);
			} else {
				this.DocInfo.Data.Obj.setScope(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Obj.setOrigin(true);
			} else {
				this.DocInfo.Data.Obj.setOrigin(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Obj.setStates(true);
			} else {
				this.DocInfo.Data.Obj.setStates(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Obj.setStateDesc(true);
			} else {
				this.DocInfo.Data.Obj.setStateDesc(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Obj.setStateInitial(true);
			} else {
				this.DocInfo.Data.Obj.setStateInitial(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Obj.setStateTime(true);
			} else {
				this.DocInfo.Data.Obj.setStateTime(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Proc.setDesc(true);
			} else {
				this.DocInfo.Data.Proc.setDesc(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Proc.setEssence(true);
			} else {
				this.DocInfo.Data.Proc.setEssence(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Proc.setOrigin(true);
			} else {
				this.DocInfo.Data.Proc.setOrigin(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Proc.setScope(true);
			} else {
				this.DocInfo.Data.Proc.setScope(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Proc.setBody(true);
			} else {
				this.DocInfo.Data.Proc.setBody(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Proc.setActTime(true);
			} else {
				this.DocInfo.Data.Proc.setActTime(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Rel.setAgPar(true);
			} else {
				this.DocInfo.Data.Rel.setAgPar(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Rel.setClassInst(true);
			} else {
				this.DocInfo.Data.Rel.setClassInst(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Rel.setFeChar(true);
			} else {
				this.DocInfo.Data.Rel.setFeChar(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Rel.setGenSpec(true);
			} else {
				this.DocInfo.Data.Rel.setGenSpec(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Rel.setBiDir(true);
			} else {
				this.DocInfo.Data.Rel.setBiDir(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Rel.setUniDir(true);
			} else {
				this.DocInfo.Data.Rel.setUniDir(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Link.setAgent(true);
			} else {
				this.DocInfo.Data.Link.setAgent(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Link.setCondition(true);
			} else {
				this.DocInfo.Data.Link.setCondition(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Link.setEffect(true);
			} else {
				this.DocInfo.Data.Link.setEffect(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Link.setEvent(true);
			} else {
				this.DocInfo.Data.Link.setEvent(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Link.setException(true);
			} else {
				this.DocInfo.Data.Link.setException(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Link.setInstEvent(true);
			} else {
				this.DocInfo.Data.Link.setInstEvent(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Link.setInvocation(true);
			} else {
				this.DocInfo.Data.Link.setInvocation(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Link.setResCons(true);
			} else {
				this.DocInfo.Data.Link.setResCons(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Link.setInstrument(true);
			} else {
				this.DocInfo.Data.Link.setInstrument(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Link.setCond(true);
			} else {
				this.DocInfo.Data.Link.setCond(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Link.setPath(true);
			} else {
				this.DocInfo.Data.Link.setPath(false);
			}

			if (file.read() == '1') {
				this.DocInfo.Data.Link.setReactTime(true);
			} else {
				this.DocInfo.Data.Link.setReactTime(false);
			}

			if (file.read() == '1') {
				this.DocInfo.opdopl.setOPDAll(true);
			} else {
				this.DocInfo.opdopl.setOPDAll(false);
			}

			if (file.read() == '1') {
				this.DocInfo.opdopl.setOPDByName(true);
			} else {
				this.DocInfo.opdopl.setOPDByName(false);
			}

			if (file.read() == '1') {
				this.DocInfo.opdopl.setOPDNone(true);
			} else {
				this.DocInfo.opdopl.setOPDNone(false);
			}

			if (file.read() == '1') {
				this.DocInfo.opdopl.setOPLAll(true);
			} else {
				this.DocInfo.opdopl.setOPLAll(false);
			}

			if (file.read() == '1') {
				this.DocInfo.opdopl.setOPLByName(true);
			} else {
				this.DocInfo.opdopl.setOPLByName(false);
			}

			if (file.read() == '1') {
				this.DocInfo.opdopl.setOPLNone(true);
			} else {
				this.DocInfo.opdopl.setOPLNone(false);
			}

			if (file.read() == '1') {
				this.DocInfo.opdopl.setOPLAccording(true);
			} else {
				this.DocInfo.opdopl.setOPLAccording(false);
			}

			String until = "";
			char c = (char) file.read();
			while (c != ';') {
				until += c;
				c = (char) file.read();
			}
			this.DocInfo.opdopl.setOPDUntil(until);

			until = "";
			c = (char) file.read();
			while (c != ';') {
				until += c;
				c = (char) file.read();
			}
			this.DocInfo.opdopl.setOPLUntil(until);
		} catch (IOException e) {
			System.out.println("error");
			return;
		}

	}// end of FromTemp

}// end of class Document
