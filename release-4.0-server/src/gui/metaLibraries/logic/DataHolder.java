package gui.metaLibraries.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import exportedAPI.opcatAPI.ISystem;
import exportedAPI.opcatAPI.ISystemStructure;
import gui.dataProject.DataProject;
import gui.opdProject.OpdProject;
import gui.projectStructure.Entry;
import gui.projectStructure.MainStructure;

public class DataHolder {

	private int type;

	private Object holder;

	public DataHolder(Object data, int dataType) {
		this.holder = data;
		this.type = dataType;
	}

	public Object getData() {
		if (this.type == MetaLibrary.TYPE_POLICY) {
			return this.holder;
		}
		if (this.type == MetaLibrary.TYPE_CLASSIFICATION) {
			return this.holder;
		}
		return null;
	}

	public Collection getDataComponents() {

		if (this.holder == null) {
			return new ArrayList();
		}

		if (this.type == MetaLibrary.TYPE_POLICY) {
			ArrayList ret = new ArrayList();
			MainStructure oStructure = ((OpdProject) this.holder)
					.getComponentsStructure();
			Iterator iter = Collections.list(oStructure.getElements())
					.iterator();
			while (iter.hasNext()) {
				Entry ent = (Entry) iter.next();
				ret.add(ent.getLogicalEntity());
			}
			return ret;
		}

		if (this.type == MetaLibrary.TYPE_CLASSIFICATION) {
			DataProject reqs = (DataProject) this.holder;
			return reqs.values();
		}

		return null;
	}

	public String getGlobalID() {
		if (this.type == MetaLibrary.TYPE_POLICY) {
			return ((OpdProject) this.holder).getGlobalID();
		}
		if (this.type == MetaLibrary.TYPE_CLASSIFICATION) {
			return ((DataProject) this.holder).getID();
		}
		return null;
	}

	public String getName() {
		if (this.type == MetaLibrary.TYPE_POLICY) {
			return ((OpdProject) this.holder).getName();
		}
		if (this.type == MetaLibrary.TYPE_CLASSIFICATION) {
			return ((DataProject) this.holder).getType();
		}
		return null;
	}

	/**
	 * TODO replace this later with more globel one.
	 * 
	 * @return
	 */
	public ISystemStructure getISystemStructure() {
		if (this.type == MetaLibrary.TYPE_POLICY) {
			return ((OpdProject) this.holder).getISystemStructure();
		}
		return null;
	}

	public ISystem getISystem() {
		if (this.type == MetaLibrary.TYPE_POLICY) {
			return (ISystem) this.holder;
		}
		return null;
	}
}
