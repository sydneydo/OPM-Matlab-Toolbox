package gui.dataProject;

import expose.OpcatExposeManager;
import gui.controls.FileControl;
import gui.opdProject.OpdProject;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import javax.swing.JDesktopPane;

import org.tmatesoft.svn.core.SVNURL;

import modelControl.OpcatMCManager;

public class LibraryFileLoader implements MetaLoader {

	OpdProject project;

	private String id;

	public OpdProject getProject() {
		return project;
	}

	private Vector<Object> rows = new Vector<Object>();

	private Vector<String> headers = new Vector<String>();

	private int nameIndex = 2;

	private int idIndex = 0;

	private MetaStatus status = new MetaStatus();

	int referenceType;

	String path;

	LibraryFileLoader(String _path, int refType) throws Exception {
		if (refType == DataCreatorType.REFERENCE_TYPE_PRIVATE_FILE) {
			throw (new Exception("Self reference type error"));
		}
		this.path = _path;
		this.referenceType = refType;

	}

	LibraryFileLoader(String _path, int refType, OpdProject project)
			throws Exception {
		if (refType != DataCreatorType.REFERENCE_TYPE_PRIVATE_FILE) {
			throw (new Exception("Not a self reference type"));
		}
		this.path = _path;
		this.referenceType = refType;
		this.id = project.getGlobalID();
		this.project = project;

	}

	public void load() {
		// if (metaLibraries == null) {
		// return;
		// }
		// MetaLibrary metaLib;

		InputStream is = null;
		String fileName = "";
		// Handling meta libraries which are on the local file systems

		if (referenceType == DataCreatorType.REFERENCE_TYPE_PRIVATE_FILE) {
			// project = FileControl.getInstance().getCurrentProject();
			if (project != null)
				return;
		}
		if ((referenceType == DataCreatorType.REFERENCE_TYPE_PRIVATE_FILE)
				|| (referenceType == DataCreatorType.REFERENCE_TYPE_TEMPLATE_FILE)) {
			fileName = getPath();
			File f = new File(fileName);
			if (!f.canRead()) {
				status.setLoadFail(true);
				status.setFailReason("can not read file");
				return;
			} else {
				try {
					if (fileName.endsWith(".opz")) {
						is = new GZIPInputStream(new FileInputStream(f), 4096);
					} else {
						is = new BufferedInputStream(new FileInputStream(f),
								4096);
					}
				} catch (Exception E) {
					status.setLoadFail(true);
					status
							.setFailReason("can not create input stream from file "
									+ E.getMessage());
					return;
				}
			}
		} else if ((referenceType == DataCreatorType.REFERENCE_TYPE_REPOSITORY_FILE)) {
			try {
				if (OpcatMCManager.isConnectedToerver()) {
					File file = OpcatExposeManager.getModelIntoOpcatTemp(this
							.getPath());

					fileName = file.getPath();
					if (!file.canRead()) {
						status.setLoadFail(true);
						status.setFailReason("can not read file");
						return;
					} else {

						if (fileName.endsWith(".opz")) {
							is = new GZIPInputStream(new FileInputStream(file),
									4096);
						} else {
							is = new BufferedInputStream(new FileInputStream(
									file), 4096);
						}
					}
				} else {
					status.setLoadFail(true);
					status.setFailReason("not connected to MC server");
					return;
				}
			} catch (Exception e) {
				status.setLoadFail(true);
				return;
			}

		} else if (referenceType == DataCreatorType.REFERENCE_TYPE_URL) {
			try {
				// TODO: support OPZ files from URL's
				fileName = getPath();
				URL url = new URL(this.getPath());
				is = url.openStream();
				if (url.getPath().endsWith(".opz")) {
					is = new GZIPInputStream(is);
				}
			} catch (Exception E) {
				status.setLoadFail(true);
				return;
			}
		}

		// Handling ontology loading
		try {
			if (is != null) {
				gui.opx.Loader ld = new gui.opx.Loader(fileName);
				project = this._loadProject(ld, is);
				if (project != null) {
					project.setPath(fileName);
					is.close();
				} else {
					status.setFailReason("loaded an empty project");
					status.setLoadFail(true);
					return;
				}
			}
		} catch (Exception E) {
			status.setFailReason("while loading the project " + E.getMessage());
			status.setLoadFail(true);
			return;
		} finally {
		}

		status.setLoadFail(false);
		status.setFailReason("");

	}

	private OpdProject _loadProject(gui.opx.Loader ld, InputStream is)
			throws gui.opx.LoadException {
		OpdProject tProject = ld.load(new JDesktopPane(), is, null);
		// OpdProject tProject =
		// ld.load(FileControl.getInstance().getOPCATDesktop(), is, null);
		return tProject;
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
		if (referenceType == DataCreatorType.REFERENCE_TYPE_TEMPLATE_FILE) {
			SVNURL url = OpcatMCManager.getInstance(true).getFileURL(
					new File(getPath()));
			return url.getURIEncodedPath();
		} else if (referenceType == DataCreatorType.REFERENCE_TYPE_PRIVATE_FILE) {
			return id;

		}
		return getPath();
	}

	public Vector getHeaders() {
		return headers;
	}

	public String getName() {
		if (project != null) {
			return project.getName();
		} else {
			return "null-ontology";
		}
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
