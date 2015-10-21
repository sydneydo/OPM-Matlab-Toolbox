package gui.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

public class OpcatFileNameFilter implements FilenameFilter {

	private boolean allowDirectories = true;
	private boolean allowNonOpcatFiles = false;

	private List<String> opcat = Arrays.asList("opx", "opz");

	private List<String> nonOpcat = Arrays.asList("doc", "txt", "pdf", "sql",
			"rpg", "java", "log", "");

	private List<String> alwaysReject = Arrays.asList(".svn");

	public OpcatFileNameFilter(boolean allowDirectories,
			boolean allowNonOpcatFiles) {
		this.allowDirectories = allowDirectories;
		this.allowNonOpcatFiles = allowNonOpcatFiles;
	}

	@Override
	public boolean accept(File dir, String name) {

		boolean ret = false;

		for (String str : alwaysReject) {
			if (name.toLowerCase().endsWith(str)) {
				return false;
			}
		}

		for (String str : opcat) {
			if (name.toLowerCase().endsWith(str)) {
				return true;
			}
		}

		if (!ret && allowNonOpcatFiles) {
			for (String str : nonOpcat) {
				if (name.toLowerCase().endsWith(str)) {
					return true;
				}
			}
		}

		if (!ret && allowDirectories) {
			File file = new File(dir + File.separator + name);
			if ((file.isDirectory())) {
				return true;
			}
		}
		if (allowDirectories && name != null) {
			if (name.indexOf(".") < 0) {
				return true;
			}
		}

		return ret;
	}
}
