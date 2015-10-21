/*
 * Created on 14/05/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui.projectStructure;

import exportedAPI.opcatAPI.IRole;
import exportedAPI.opcatAPIx.IXRole;
import gui.metaLibraries.logic.MetaLibrary;
import gui.metaLibraries.logic.Role;

/**
 * A Role of a thing, according to an imported {@link MetaLibrary}.
 * @author Eran Toch
 */
public class RoleEntry implements IXRole, IRole {
	private Role theRole;
	
	public RoleEntry(long _thingID, long _libID)	{
		this.define(_thingID, _libID);
	}
	
	public RoleEntry(Role _role)	{
		this.theRole = _role;
	}
	
	/**
	 * Defines a role using a given thing ID and meta-library ID.
	 */
	public void define(long _thingID, long _libID)	{
		Role role = new Role(_thingID, _libID);
		this.theRole = role;
	}
	
	public Role getLogicalRole()	{
		return this.theRole;
	}
		
	public String getThingName()	{
		return this.theRole.getThingName();
	}
	
	public String getLibraryName()	{
		return this.theRole.getLibraryName();
	}
	
	public long getThingId()	{
		return this.theRole.getThingId();
	}
	
	public long getLibraryId()	{
		return this.theRole.getLibraryId();
	}
}
