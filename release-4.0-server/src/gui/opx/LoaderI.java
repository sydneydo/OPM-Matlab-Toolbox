package gui.opx;

import gui.opdProject.OpdProject;

import javax.swing.JDesktopPane;
import javax.swing.JProgressBar;

import org.jdom.Document;

public interface LoaderI {
	public OpdProject load(JDesktopPane parentDesktop, Document document,
			JProgressBar progressBar) throws LoadException;

	public String getVersion();

	public void setPath(String path);
}
