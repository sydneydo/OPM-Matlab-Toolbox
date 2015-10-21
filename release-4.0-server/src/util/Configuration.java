/*
 * Created on 10/05/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package util;

import gui.controls.FileControl;
import gui.license.Features;
import gui.util.LastFileEntry;
import gui.util.OpcatException;
import gui.util.OpcatLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * @author eran
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Configuration {
	private static Configuration instance = null;

	private Properties properties;

	public final static String CONF_FILE_URL = FileControl.getInstance()
			.getOPCATDirectory()
			+ FileControl.fileSeparator + "conf.txt";

	public static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
		}
		return instance;
	}

	protected Configuration() {
		this.properties = new Properties();

		try {
			FileControl file = FileControl.getInstance();

			File conf = new File(CONF_FILE_URL);

			if (!conf.exists()) {
				System.out.println("No Configuration File found in path - "
						+ CONF_FILE_URL);
				System.exit(1);
			}
			FileInputStream in = new FileInputStream(CONF_FILE_URL);

			this.properties.setProperty("opcat_home", file.getOPCATDirectory());

			this.properties.setProperty("error_log", file.getOPCATDirectory()
					+ FileControl.fileSeparator + "errors.txt");

			this.properties.setProperty("backup_directory", file
					.getOPCATDirectory()
					+ FileControl.fileSeparator + "backup");

			this.properties.setProperty("icon_directory", file
					.getOPCATDirectory()
					+ FileControl.fileSeparator + "icons");

			this.properties.setProperty("system_types", file
					.getOPCATDirectory()
					+ FileControl.fileSeparator + "systems.ops");

			this.properties.setProperty("colors", file.getOPCATDirectory()
					+ FileControl.fileSeparator + "colors.ops");

			this.properties.setProperty("meta_path", Features.getReqFilePath());

			this.properties.setProperty("has_meta", new Boolean(Features
					.hasReq()).toString());

			this.properties.setProperty("java_version", System
					.getProperty("java.vm.version"));

			this.properties.setProperty("show_roles_on_process", "no");

			this.properties.setProperty("show_roles_on_objects", "no");

			this.properties.setProperty("show_state_not_result_rule", "no");

			this.properties.setProperty("show_link_addition_rule", "yes");

			this.properties.setProperty("search.width", "1");

			this.properties.setProperty("search.depth", "1");

			this.properties.setProperty("lower_panel_min_high", "100");

			this.properties.setProperty("req_tool_tip", "true");

			this.properties.setProperty("simulation_level_range", "3");

			this.properties.setProperty("show_icons", "true");

			this.properties.setProperty("show_opl", "true");

			this.properties.setProperty("thing_icon_AlphaComposite", "0.75");

			this.properties.setProperty("simulation_STOP_AT_AGENT", "false");
			this.properties.setProperty("simulation_SHOW_GRAPHICS", "true");
			this.properties.setProperty(
					"simulation_MIN_TIME__PROCESS_DURATION", "true");
			this.properties.setProperty("simulation_RANDOM_PROCESS_DURATION",
					"false");
			this.properties.setProperty("simulation_SHOW_RESOURCE", "false");
			this.properties.setProperty("simulation_SHOW_LIFE_SPAN", "false");
			this.properties.setProperty("simulation_RANDOM_STATE_SELECTION",
					"true");
			this.properties.setProperty("simulation_STEP_BY_STEP_MODE", "true");
			this.properties.setProperty("simulation_MOVE_BETWEEN_OPD", "true");
			this.properties.setProperty("simulation_AUTOMATIC_INITIATION",
					"true");
			this.properties.setProperty("simulation_ONE_OBJECT_INSTANCE",
					"true");
			this.properties.setProperty("simulation_REACTION_TIME_RANGE_END",
					"5");
			this.properties.setProperty("simulation_REACTION_TIME_RANGE_START",
					"0");
			this.properties.setProperty("simulation_REACTION_TIME", "0");
			this.properties.setProperty("simulation_FIXED_REACTION_TIME",
					"true");
			this.properties.setProperty(
					"simulation_PROCESS_DURATION_RANGE_END", "15");
			this.properties.setProperty(
					"simulation_PROCESS_DURATION_RANGE_START", "5");
			this.properties.setProperty("simulation_PROCESS_DURATION", "1");
			this.properties.setProperty("simulation_FIXED_PROCESS_DURATION",
					"false");
			this.properties.setProperty("simulation_STEP_DURATION", "1000");

			// new settings for opcat enterprise
			this.properties.setProperty("MCaccesstype", "svn");
			this.properties.setProperty("MCserver", "127.0.0.1");
			this.properties.setProperty("MCrepository", "Systems");
			this.properties.setProperty("MCTemplates", "Templates");

			this.properties.setProperty("DBtype", "mysql");
			this.properties.setProperty("DBserver", "127.0.0.1");
			this.properties.setProperty("DBdatabasename", "opcat");

			this.properties.setProperty("models_directory", file
					.getOPCATDirectory()
					+ FileControl.fileSeparator + "Working Copy");

			this.properties.setProperty("jarname", "Opcat2.jar");

			this.properties.setProperty("show_reguler_links_messages", "false");

			this.properties.setProperty(
					"expose_treat_relation_delete_as_change", "true");

			this.properties.setProperty("expose_treat_links_delete_as_change",
					"true");

			this.properties.setProperty(
					"expose_treat_object_type_change_as_change", "true");

			this.properties.load(in);
		} catch (FileNotFoundException e) {
			saveProperties();
			OpcatLogger.logError(e);
		} catch (IOException e) {
			OpcatLogger.logError(e);
		}
	}

	public String getProperty(String name) {
		return (String) this.properties.get(name);
	}

	public ListIterator<String> getUserDirectories() throws OpcatException {

		ArrayList<String> list = new ArrayList<String>();
		list.add(Configuration.getInstance().getProperty("models_directory"));

		if ((this.getProperty("user_directories") == null)
				|| (this.getProperty("user_directories")).trim().equals("")) {
			return list.listIterator();
		}

		StringTokenizer st = new StringTokenizer(this
				.getProperty("user_directories"), ",");
		if (st == null) {
			throw new OpcatException("No used files were found");
		}
		while (st.hasMoreElements()) {
			String entry = st.nextToken();
			list.add(entry);
		}
		return list.listIterator();
	}

	public void removeDirectoryToUserDirectories(String path) {
		StringTokenizer st = null;
		if (this.getProperty("user_directories") != null) {
			st = new StringTokenizer(this.getProperty("user_directories"), ",");
			if (st == null) {
				OpcatLogger
						.logError("[Configuration] No user directories were found");
			}
		}

		int i = 0;
		String newFileList = "";
		if (this.getProperty("user_directories") != null) {
			while (st.hasMoreElements()) {
				String entry = st.nextToken();
				if (!entry.equalsIgnoreCase(path)) {
					newFileList += "," + entry;
					i++;
					OpcatLogger.logMessage(" Removing From list - " + path);
				}
			}
		}
		if (newFileList.length() <= 1) {
			this.properties.setProperty("user_directories", "");
		} else {
			this.properties.setProperty("user_directories", newFileList
					.substring(1, newFileList.length()));
		}

		saveProperties();

	}

	public void addDirectoryToUserDirectories(String path) {
		StringTokenizer st = null;
		if (this.getProperty("user_directories") != null) {
			st = new StringTokenizer(this.getProperty("user_directories"), ",");
			if (st == null) {
				OpcatLogger
						.logError("[Configuration] No used files were found");
			}
		}

		int i = 0;
		String newFileList = path;
		if (this.getProperty("user_directories") != null) {
			while (st.hasMoreElements()) {
				String entry = st.nextToken();
				if (!entry.equalsIgnoreCase(path)) {
					newFileList += "," + entry;
					i++;
				}
			}
		}
		this.properties.setProperty("user_directories", newFileList);

		saveProperties();

	}

	public ListIterator<LastFileEntry> getLastUsedFiles() throws OpcatException {
		ArrayList<LastFileEntry> list = new ArrayList<LastFileEntry>();
		if (this.getProperty("last_used_files") == null) {
			return list.listIterator();
		}
		StringTokenizer st = new StringTokenizer(this
				.getProperty("last_used_files"), ",");
		if (st == null) {
			throw new OpcatException("No used files were found");
		}
		while (st.hasMoreElements()) {
			String entry = st.nextToken();
			StringTokenizer et = new StringTokenizer(entry, "@");
			String pName = et.nextToken();
			if (!et.hasMoreTokens()) {
				throw new OpcatException("Bad entry");
			}
			String url = et.nextToken();
			LastFileEntry last = new LastFileEntry(pName, url);
			list.add(last);
		}
		return list.listIterator();
	}

	public void addFileToLastUsed(LastFileEntry last) {
		StringTokenizer st = null;
		if (this.getProperty("last_used_files") != null) {
			st = new StringTokenizer(this.getProperty("last_used_files"), ",");
			if (st == null) {
				OpcatLogger
						.logError("[Configuration] No used files were found");
			}
		}
		String newFile = last.getProjectName() + "@" + last.getFileUrl();
		String newFileList = newFile;
		int i = 0;
		if (this.getProperty("last_used_files") != null) {
			while (st.hasMoreElements() && (i < 4)) {
				String entry = st.nextToken();
				if (entry.compareTo(newFile) != 0) {
					newFileList += "," + entry;
					i++;
				}
			}
		}
		this.properties.setProperty("last_used_files", newFileList);

		saveProperties();

	}

	public void saveProperties() {
		try {
			FileOutputStream out = new FileOutputStream(CONF_FILE_URL);
			this.properties.store(out, "updated");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getSearchWidth() {

		int ret = Integer.parseInt(properties.getProperty("search.width"));
		if (ret <= 0)
			ret = 10;
		return ret;

	}

	public int getSearchDepth() {
		int ret = Integer.parseInt(properties.getProperty("search.depth"));
		if (ret <= 0)
			ret = 10;
		return ret;
	}

	// public Dimension getPanelDimensions() {
	// return panelDimensions;
	// }
	//
	// public void setPanelDimensions(Dimension d) {
	// panelDimensions = d;
	// }

}