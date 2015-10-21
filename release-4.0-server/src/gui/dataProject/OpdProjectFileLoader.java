package gui.dataProject;

import gui.opdProject.OpdProject;
import gui.projectStructure.Entry;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import javax.swing.JDesktopPane;

public class OpdProjectFileLoader implements MetaLoader {

	OpdProject project;

	private Vector<Object> rows = new Vector<Object>();

	private Vector<Entry> sourceElements = new Vector<Entry>();

	private Vector<String> headers = new Vector<String>();

	private int nameIndex = 2;

	private int idIndex = 0;

	private MetaStatus status = new MetaStatus();

	String path;

	OpdProjectFileLoader(String _path) {
		this.path = _path;
	}

	public void load() {
		try {
			gui.opx.Loader ld = new gui.opx.Loader(this.path);

			String fileName = path;
			InputStream is = null;
			File f = new File(fileName);
			if (fileName.endsWith(".opz")) {
				is = new GZIPInputStream(new FileInputStream(f), 4096);
			} else {
				is = new BufferedInputStream(new FileInputStream(f), 4096);
			}
			if (is != null) {
				project = ld.load(new JDesktopPane(), is, null);
				if (project != null) {
					project.setPath(fileName);
					project.close();
					is.close();
				} else {
					status.setLoadFail(true);
					return;
				}

			} else {
				status.setLoadFail(true);
				return;
			}

			headers.add("ID");
			headers.add("Type");
			headers.add("Name");
			headers.add("Description");

			Iterator iter = Collections.list(
					project.getSystemStructure().getAllElements()).iterator();
			while (iter.hasNext()) {
				Object obj = iter.next();
				Entry entry = (Entry) obj;
				Vector row = new Vector();
				row = entry.getLogicalEntity().getAllData();
				rows.add(row);
				sourceElements.add(entry);
			}

			status.setLoadFail(false);
		} catch (Exception ex) {
			status.setLoadFail(true);
			status.setFailReason(ex.getMessage());
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
		return getPath();
	}

	public Vector getHeaders() {
		return headers;
	}

	public String getName() {
		return project.getName();
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
		return false;
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
		return path;
	}

	public boolean isShowColorValueAsPrograssBar() {
		return false;
	}

}
