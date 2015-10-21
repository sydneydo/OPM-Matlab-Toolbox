package gui.checkModule;

import exportedAPI.OpcatConstants;
import exportedAPI.OpdKey;
import gui.opdProject.OpdProject;
import gui.projectStructure.ConnectionEdgeEntry;
import gui.projectStructure.ConnectionEdgeInstance;
import gui.projectStructure.MainStructure;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ProcessEntry;
import gui.projectStructure.StateEntry;

public class CommandWrapper
{
	private ConnectionEdgeEntry sEntry, dEntry;
	private ConnectionEdgeInstance sInstance, dInstance;
	private OpdProject prj;
	private int type, sType, dType;
	private boolean checkGraphic;


	public CommandWrapper(long sourceEntityId,
						  OpdKey sourceInstanceId,
						  long destinationEntityId,
						  OpdKey destinationInstanceId,
						  int linkOrRelationType,
						  OpdProject prj)
	{
		this.prj = prj;
		this.type = linkOrRelationType;
		MainStructure ms = prj.getComponentsStructure();

		this.sEntry = (ConnectionEdgeEntry)ms.getEntry(sourceEntityId);
		this.sInstance = (ConnectionEdgeInstance)this.sEntry.getInstance(sourceInstanceId);
		this.sType = this._entry2Type(this.sEntry);

		this.dEntry = (ConnectionEdgeEntry)ms.getEntry(destinationEntityId);
		this.dInstance = (ConnectionEdgeInstance)this.dEntry.getInstance(destinationInstanceId);
		this.dType = this._entry2Type(this.dEntry);
		this.checkGraphic = true;
	}


	public ConnectionEdgeEntry getSourceEntry()
	{
		return this.sEntry;
	}

	public ConnectionEdgeEntry getDestinationEntry()
	{
		return this.dEntry;
	}

	public ConnectionEdgeInstance getSourceInstance()
	{
		return this.sInstance;
	}

	public ConnectionEdgeInstance getDestinationInstance()
	{
		return this.dInstance;
	}

	public int getType()
	{
		return this.type;
	}

	public int getSourceType()
	{
		return this.sType;
	}

	private int _getConstantsType(int type)
	{
		switch(type)
		{
			case 0: return OpcatConstants.OBJECT;
			case 1: return OpcatConstants.PROCESS;
			case 2: return OpcatConstants.STATE;
			default: return 0;
		}
	}

	public int getConstantsSourceType()
	{
		return this._getConstantsType(this.sType);
	}

	public int getConstantsDestinationType()
	{
		return this._getConstantsType(this.dType);
	}



	public int getDestinationType()
	{
		return this.dType;
	}


	private int _entry2Type(ConnectionEdgeEntry e)
	{
		if(e instanceof ObjectEntry) {
			return 0;
		}
		if(e instanceof ProcessEntry) {
			return 1;
		}
		if(e instanceof StateEntry) {
			return 2;
		}
		return -1;
	}

	public static String type2String(int tp)
	{
		switch (tp)
		{
			case 0: return "an object";
			case 1: return "a process";
			case 2: return "a state";
			default: return "";
		}
	}

	public OpdProject getProject()
	{
		return this.prj;
	}

	public boolean isCheckGraphic()
	{
		return this.checkGraphic;
	}

}