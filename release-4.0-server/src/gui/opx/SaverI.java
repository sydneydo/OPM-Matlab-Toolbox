package gui.opx;

import gui.opdProject.OpdProject;

import java.io.OutputStream;

import javax.swing.JProgressBar;

public interface SaverI {
	public void save(OpdProject project, OutputStream os,
			JProgressBar progressBar) throws SaveException;

	public String getVersion();
}