package gui.dataProject;

import java.util.Enumeration;

import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.projectStructure.Instance;
import gui.projectStructure.ProcessInstance;

public class DataCreatorType {

	/**
	 * A constant indicating a reference to a local file.
	 */
	public static final int REFERENCE_TYPE_PRIVATE_FILE = 0;

	/**
	 * A constant indicating a reference to a URL.
	 */
	public static final int REFERENCE_TYPE_URL = 1;

	/**
	 * A constant indicating a reference to a URL.
	 */
	public static final int REFERENCE_TYPE_REPOSITORY_FILE = 2;

	public static final int REFERENCE_TYPE_TEMPLATE_FILE = 3;

	/**
	 * load meta data from opz file
	 */
	public static int DATA_TYPE_OPCAT_FILE_OPZ = 1;

	/**
	 * load from csv files
	 */
	public static int DATA_TYPE_TEXT_FILE_CSV = 2;

	/**
	 * load from a Project data structure
	 */
	public static int DATA_TYPE_OPCAT_PROJECT = 5;

	public static int DATA_TYPE_UNDEFINED = 6;

	public static int DATA_TYPE_OPCAT_OPD = 7;

	public static int DATA_TYPE_OPCAT_LIBRARAY = 8;

	int type = -9999;

	int referenceType;

	public int getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(int referenceType) {
		this.referenceType = referenceType;
	}

	public DataCreatorType(int dataType, int referenceType) {
		this.type = dataType;
		this.referenceType = referenceType;
	}

	public int getType() {
		return type;
	}

}
