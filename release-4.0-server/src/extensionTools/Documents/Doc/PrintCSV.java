package extensionTools.Documents.Doc;

import gui.Opcat2;
import gui.controls.FileControl;
import gui.controls.GuiControl;
import gui.license.Features;
import gui.opdGraphics.opdBaseComponents.OpdThing;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.projectStructure.Entry;
import gui.projectStructure.Instance;
import gui.projectStructure.ProcessEntry;
import gui.projectStructure.ProcessInstance;
import gui.util.OpcatLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JOptionPane;

class PrintCSV extends Thread {
    private final static String fileSeparator = System
	    .getProperty("file.separator");

    File csv_file;

    public PrintCSV(File file) {
	this.csv_file = file;
    }

    public void run() {
	GuiControl gui = GuiControl.getInstance();
	gui.startWaitingMode("Saving CSV File...", true);
	try {
	    this.doCSV(this.csv_file);
	} catch (Exception e) {
	    gui.stopWaitingMode();
	    OpcatLogger.logError(e);
	    FileControl file = FileControl.getInstance();
	    JOptionPane.showMessageDialog(file.getCurrentProject()
		    .getMainFrame(),
		    "Save had failed becasue of the following error:\n"
			    + e.getMessage(), "Message",
		    JOptionPane.ERROR_MESSAGE);
	} finally {
	    gui.stopWaitingMode();
	}
    }

    private void doCSV(File file) {
	// Class
	// Name
	// Folder
	// description
	// decomposes (SystemProcess)
	// tested by (TestElement)
	// verifies (PerformanceIndex)
	// opmExternalFilePath
	// decoposed by (SystemProcess)
	// externalFilePath
	// oplExternalFilePath

	OpdProject pr = FileControl.getInstance().getCurrentProject();
	try {
	    FileWriter fw = new FileWriter(file);
	    
	    /**
	     * load col names from docsv file
	     */
	    File csvDef = Features.getCSVExportDefFilePath();
	    if (csvDef != null) {
		BufferedReader csvDefReader = new BufferedReader(new FileReader(csvDef));
		String line = csvDefReader.readLine() ;
		if((line != null) && (!line.equalsIgnoreCase(""))) {
		    fw.write(line + "\n") ; 
		}
	    }
	    /**
	     *	we might think on changing this to include the real definitions of the out CSV
	     *	so it will be ok not obly for Elta, but as there is no need for this yet I leae it
	     *	as is.  
	     */
	    
	    Document doc = new Document(pr);
	    Iterator entries = Collections.list(
		    pr.getSystemStructure().getElements()).iterator();
	    StringTokenizer st = new StringTokenizer(file.getPath(), ".", false);
	    String dir_name = st.nextToken();
	    while (entries.hasNext()) {
		Entry entry = (Entry) entries.next();
		if (entry instanceof ProcessEntry) {
		    ProcessEntry processEntry = (ProcessEntry) entry;
		    String sons = "null";
		    if (processEntry.getZoomedInOpd() != null) {
			Opd opd = processEntry.getZoomedInOpd();
			Vector included = processEntry
				.GetInZoomedIncludedInstances(opd);
			for (int i = 0; i < included.size(); i++) {
			    Instance incInstance = (Instance) included.get(i);
			    if (incInstance instanceof ProcessInstance) {
				if (sons.equalsIgnoreCase("null")) {
				    sons = new String();
				}
				sons = sons
					+ incInstance.getEntry().getName()
						.replace("\n", " ");
				if (i != included.size() - 1) {
				    sons = sons + ";";
				}

			    }
			}
		    }
		    Iterator instances = Collections.list(entry.getInstances())
			    .iterator();
		    while (instances.hasNext()) {
			Instance ins = (Instance) instances.next();
			OpdThing parant = ins.getParent();
			String parantName;
			if (parant == null) {
			    parantName = "null";
			} else {
			    parantName = parant.getName();
			}
			String oplName = dir_name + fileSeparator;
			fw.write("SystemProcess"
				+ ","
				+ ins.getEntry().getName().replace("\n", " ")
				+ ","
				+ "SystemProcess"
				+ ","
				// +
				// parantName.replace("\n",
				// " ") + ","
				+ ins.getEntry().getDescription().replace("\n",
					" ").replaceAll("none", "null") + ","
				+ ins.getOpd().getName().replace("\n", " ")
				+ "," + oplName
				+ ins.getOpd().getName().replace("\n", " ")
				+ "_" + "" + "" + ins.getOpd().getOpdId()
				+ "_OPlFileName.html" + "," + oplName
				+ ins.getOpd().getName().replace("\n", " ")
				+ "_" + "" + "" + ins.getOpd().getOpdId()
				+ "_OPDfile.jpeg" + "," + sons + ","
				+ parantName.replace("\n", " ")
				// + "tested by"
				// + ","
				// + "verifies
				// (Constraint)"
				// + ","
				// + "verifies
				// (PerformanceIndex)"
				+ "\n");
		    }
		}
	    }
	    fw.flush();
	    fw.close();

	    // print the OPD's
	    Iterator opds = Collections.list(pr.getSystemStructure().getOpds())
		    .iterator();
	    while (opds.hasNext()) {
		Opd opd = (Opd) opds.next();
		doc.PrintOpd(opd, null, file.getPath());
	    }
	} catch (IOException e) {
	    OpcatLogger.logError(e);
	    JOptionPane.showMessageDialog(Opcat2.getFrame(),
		    "Error Saving CSV file", "OPCAT II", JOptionPane.OK_OPTION);
	    return;
	}

    }
}