package gui.repository;

import gui.metaLibraries.logic.MetaLibrary;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;

class HashKey
{
	String key;
	HashKey(Object o)
	{
		//this.key = new String(Long.toString(key));
		if(o instanceof OpdProject)
		{
			this.key = "Project" +((OpdProject)o).getProjectId();
			return;
		}
		if(o instanceof Opd)
		{
			this.key = "Opd" +((Opd)o).getOpdId();
			return;
		}
		if(o instanceof String)
		{
			this.key = new String(o.toString());
			return;
		}
		
		if(o instanceof MetaLibrary)
		{
			this.key = "Meta" + ((MetaLibrary)o).getPath() ;
			return;
		}		
	}

	public int hashCode()
	{
		return this.key.hashCode();
	}

	public boolean equals(Object o)
	{
		if(!(o instanceof HashKey))
		{
			return false;
		}

		return this.key.equals(((HashKey)o).toString());
	}

	public String toString()
	{
		return this.key;
	}
}