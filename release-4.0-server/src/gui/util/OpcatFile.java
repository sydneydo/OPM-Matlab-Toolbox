package gui.util;

import gui.controls.FileControl;

import java.io.File;

public class OpcatFile extends File {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean workinCopyFile = false;

	public boolean isWorkinCopyFile() {
		return workinCopyFile;
	}

	public void setWorkinCopyFile(boolean workinCopyFile) {
		this.workinCopyFile = workinCopyFile;
	}

	public OpcatFile(String path, boolean isWorkinCopyFile) {
		super(path);
		workinCopyFile = isWorkinCopyFile;
	}

	public String toString() {
		return getName();
	}

	/**
	 * This method return realPath as a path relative to the ProjPath. any \.\
	 * in the metaPath should be replaced by ProjPath when loading this path
	 * 
	 * @param projectPath
	 * @param metaPath
	 * @return
	 */
	public static String getRealativePath(String projectPath, String realPath) {
		// String sep = FileControl.fileSeparator;
		String[] meta = realPath.split("\\" + FileControl.fileSeparator);
		String[] proj = projectPath.split("\\" + FileControl.fileSeparator);
		int metaLength = meta.length;
		int projLength = proj.length;

		int minLength = 0;
		if (metaLength < projLength) {
			minLength = metaLength;
		} else {
			minLength = projLength;
		}

		/**
		 * find the max index of which the path parts of the current project and
		 * the metalib are equal
		 */
		int equalIndex = 0;
		for (equalIndex = 0; equalIndex < minLength; equalIndex++) {
			if (!meta[equalIndex].equalsIgnoreCase(proj[equalIndex])) {
				break;
			}
		}
		if (equalIndex == 0) {
			return realPath;
		}

		String tmpStr = "";
		for (int j = 0; j < projLength - equalIndex - 1; j++) {
			tmpStr = tmpStr + FileControl.fileSeparator + "..";
		}

		String path = "." + tmpStr;

		for (int j = equalIndex; j < metaLength; j++) {
			path = path + FileControl.fileSeparator + meta[j];
		}

		if (path.equalsIgnoreCase("."))
			path = "." + FileControl.fileSeparator + meta[metaLength - 1];

		return path;
		// }
		// return null ;
	}

}
