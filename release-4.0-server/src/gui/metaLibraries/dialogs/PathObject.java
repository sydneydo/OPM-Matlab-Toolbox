/*
 * Created on 02/06/2004
 */
package gui.metaLibraries.dialogs;

import gui.metaLibraries.logic.MetaLibrary;

/**
 * A representation of the path string - in a short form for the ontologies
 * editing window and in the full form for all other purposes.
 * 
 * @author Eran Toch Created: 02/06/2004
 */
public class PathObject {
	private String fullPath = "";
	private String shortPath = "";
	private int threshold = 35;
	private MetaLibrary myMeta;

	public MetaLibrary getMeta() {
		return myMeta;
	}

	public PathObject(MetaLibrary myMeta) {
		this.myMeta = myMeta;
		this.fullPath = myMeta.getPath();
		if (this.fullPath.length() > this.threshold) {
			int firstSlash = Math.max(this.fullPath.indexOf("/"), this.fullPath
					.indexOf("\\"));
			int lastSlash = Math.max(this.fullPath.lastIndexOf("/"),
					this.fullPath.lastIndexOf("\\"));
			if ((lastSlash - firstSlash) < 10) {
				this.shortPath = this.fullPath;
				return;
			}
			String finalPath = this.fullPath.substring(0, firstSlash + 1);
			finalPath += " ... ";
			finalPath += this.fullPath.substring(lastSlash);
			this.shortPath = finalPath;
		} else {
			this.shortPath = this.fullPath;
		}
	}

	public String getFullPath() {
		return this.fullPath;
	}

	public String getShortPath() {
		return this.shortPath;
	}

	public String toString() {
		return this.getShortPath();
	}
}
