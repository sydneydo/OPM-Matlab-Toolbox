package gui.opx;

import gui.controls.FileControl;
import gui.opdProject.OpdProject;
import gui.opx.ver2.LoaderVer2;
import gui.opx.ver3.LoaderVer3;
import gui.util.OpcatLogger;

import java.io.InputStream;

import javax.swing.JDesktopPane;
import javax.swing.JProgressBar;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class Loader {

	String path;

	public Loader(String path) {
		this.path = path;
	}

	public OpdProject load(JDesktopPane parentDesktop, InputStream is,
			JProgressBar progressBar) throws LoadException {

		FileControl.getInstance().setDuringFileAction(true);

		try {
			SAXBuilder builder = new SAXBuilder(
					"org.apache.xerces.parsers.SAXParser");
			Document testDoc = null;
			try {
				testDoc = builder.build(is);
			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			}
			Element rootTag = null;
			try {
				rootTag = testDoc.getRootElement();
			} catch (NullPointerException nex) {
				FileControl.getInstance().setDuringFileAction(false);
				throw new LoadException(
						"Error loading file - OPX file is empty");
			} catch (Exception ex) {
				FileControl.getInstance().setDuringFileAction(false);
				throw new LoadException("Error loading file");
			}
			String version = rootTag.getAttributeValue("version");
			LoaderI myLoader = this.chooseLoader(version);
			if (myLoader == null) {
				FileControl.getInstance().setDuringFileAction(false);
				throw new LoadException("The version: \"" + version
						+ "\" is not supported");
			}

			myLoader.setPath(path);
			OpdProject project = myLoader.load(parentDesktop, testDoc,
					progressBar);
			project.setPath(path);
			FileControl.getInstance().setDuringFileAction(false);
			return project;

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		} finally {
			FileControl.getInstance().setDuringFileAction(false);
		}
		FileControl.getInstance().setDuringFileAction(false);
		return null;
	}

	private LoaderI chooseLoader(String version) {
		if (version.equals("") || version.equals("1") || version.equals("2")) {
			return new LoaderVer2(path);
		}
		if (version.equals("3")) {
			return new LoaderVer3(path);
		}
		return null;
	}
}