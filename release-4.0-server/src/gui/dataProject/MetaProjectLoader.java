package gui.dataProject;

import gui.opdProject.OpdProject;
import gui.util.OpcatLogger;

import java.util.Iterator;
import java.util.Vector;

public class MetaProjectLoader implements MetaLoader {

	protected MetaLoader loader = null;

	private MetaStatus status = new MetaStatus();

	public MetaProjectLoader(DataCreatorType type, Object source) {

		if (type.getType() == DataCreatorType.DATA_TYPE_TEXT_FILE_CSV) {
			loadCSV((String) source, type.getReferenceType());
		}

		if (type.getType() == DataCreatorType.DATA_TYPE_OPCAT_PROJECT) {
			loadProject((OpdProject) source);
		}

		if (type.getType() == DataCreatorType.DATA_TYPE_OPCAT_OPD) {
			loadOpd((OpdProject) ((Object[]) source)[0],
					((Long) ((Object[]) source)[1]).longValue());
		}

		/**
		 * used for classification library only
		 */
		if (type.getType() == DataCreatorType.DATA_TYPE_OPCAT_FILE_OPZ) {
			loadOPZ((String) source, type.getReferenceType());
		}

		if (type.getType() == DataCreatorType.DATA_TYPE_OPCAT_LIBRARAY) {
			if (type.getReferenceType() == DataCreatorType.REFERENCE_TYPE_PRIVATE_FILE) {
				loadPrivateLibrary(((String) ((Object[]) source)[0]),
						((OpdProject) ((Object[]) source)[1]), type
								.getReferenceType());

			} else {
				loadLibrary((String) source, type.getReferenceType());
			}
		}
	}

	private void loadPrivateLibrary(String path, OpdProject project,
			int referenceType) {
		try {
			if (referenceType == DataCreatorType.REFERENCE_TYPE_PRIVATE_FILE) {
				loader = new LibraryFileLoader(path, referenceType, project);
			}
		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
	}

	private void loadLibrary(String path, int referenceType) {
		try {
			loader = new LibraryFileLoader(path, referenceType);
		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
	}

	private void loadProject(OpdProject project) {
		loader = new OpdProjectLoader(project);
	}

	private void loadOpd(OpdProject project, long opdID) {
		loader = new OpdLoader(project, opdID);
	}

	private void loadOPZ(String _path, int referenceTyp) {
		loader = new OpdProjectFileLoader(_path);
	}

	private void loadCSV(String file, int referenceTyp) {
		loader = new CSVFileLoader(file);
	}

	public MetaLoader getLoader() {
		return loader;
	}

	public String getID() {
		return loader.getID();
	}

	public Vector getHeaders() {
		return loader.getHeaders();
	}

	public Vector getRowAt(int i) {
		return loader.getRowAt(i);
	}

	public Iterator getRowsIterator() {
		return loader.getRowsIterator();
	}

	public int getSize() {
		return loader.getSize();
	}

	public String getName(Vector row) {
		return loader.getName(row);
	}

	public int getColoringData(Vector row) {
		return loader.getColoringData(row);
	}

	public String getExtID(Vector row) {
		return loader.getExtID(row);
	}

	public int getColoringIndex() {
		return loader.getColoringIndex();
	}

	public String getName() {
		return loader.getName();
	}

	public boolean hasColoringData() {
		return loader.hasColoringData();
	}

	public boolean hasIDData() {
		return loader.hasIDData();
	}

	public boolean hasNameData() {
		return loader.hasNameData();
	}

	public MetaStatus getStatus() {
		return loader.getStatus();
	}

	public String getPath() {
		return loader.getPath();
	}

	public boolean isShowColorValueAsPrograssBar() {
		return loader.isShowColorValueAsPrograssBar();
	}

	public void load() {
		loader.load();
	}

}
