package gui.dataProject;

import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.projectStructure.Entry;
import gui.util.OpcatLogger;

import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

public class OpdLoader implements MetaLoader {

	OpdProject project;

	long opdID;

	Opd opd;

	private Vector rows = new Vector();

	private Vector headers = new Vector();

	private int nameIndex = 2;

	private int idIndex = 0;

	private MetaStatus status = new MetaStatus();


	OpdLoader(OpdProject project, long opdID) {
		this.project = project;
		this.opd = project.getSystemStructure().getOpd(opdID);
		this.opdID = opdID;
	}

	public void load() {
		try {

			headers.add("ID");
			headers.add("Type");
			headers.add("Name");
			headers.add("Description");

			Iterator iter = Collections.list(
					project.getSystemStructure()
							.getEntriesInOpd(opd.getOpdId())).iterator();
			while (iter.hasNext()) {
				Object obj = iter.next();
				// if (obj instanceof ThingEntry) {
				Entry entry = (Entry) obj;
				Vector row = new Vector();
				row = entry.getLogicalEntity().getAllData();
				rows.add(row);
				// }
			}
			status.setLoadFail(false);
		} catch (Exception ex) {
			status.setLoadFail(true);
			status.setFailReason(ex.getMessage());
			OpcatLogger.logError(ex);
		}

	}

	public int getColoringData(Vector row) {
		return ((Long) row.get(getColoringIndex())).intValue();
	}

	public int getColoringIndex() {
		return 0;
	}

	public String getExtID(Vector row) {
		return ((Long) row.elementAt(idIndex)).toString();
	}

	public String getID() {
		return opd.getName() + "@" + project.getPath();
	}

	public Vector getHeaders() {
		return headers;
	}

	public String getName() {
		return opd.getName();
	}

	public String getName(Vector row) {
		return (String) row.elementAt(nameIndex);
	}

	public Vector getRowAt(int i) {
		return (Vector) rows.elementAt(i);
	}

	public Iterator getRowsIterator() {
		return rows.iterator();
	}

	public int getSize() {
		return rows.size();
	}

	public boolean hasColoringData() {
		return true;
	}

	public boolean hasIDData() {
		return true;
	}

	public boolean hasNameData() {
		return true;
	}

	public MetaStatus getStatus() {
		return status;
	}

	public String getPath() {
		return opd.getName() + "@" + project.getPath();
	}

	public boolean isShowColorValueAsPrograssBar() {
		return false;
	}

}
