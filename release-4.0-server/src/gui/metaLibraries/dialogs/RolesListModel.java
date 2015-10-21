package gui.metaLibraries.dialogs;

import gui.metaLibraries.logic.Role;

import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.AbstractListModel;

/**
 * The class represent a ListModel for OPM Roles, used in JList objects.
 */
public class RolesListModel extends AbstractListModel {
	 

	/**
	 * 
	 */
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Data structure for the roles.
	 */
	private Vector<Role> roles;

	/**
	 * Creating the roles structure.
	 */
	public RolesListModel() {
		this.roles = new Vector<Role>();
	}

	/**
	 * Building a RolesListModel using an existing collection.
	 */
	@SuppressWarnings("unchecked")
	public RolesListModel(Vector<Role> roles) {
		if (roles == null) {
			roles = new Vector<Role>();
		}
		else {
			this.roles = roles;
			Collections.sort(this.roles) ; 
			this.fireContentsChanged(this, 0, roles.size());
		}
	}

	/**
	 * Returns a Role element according to an index.
	 */
	public Object getElementAt(int index){
		Iterator it = this.roles.iterator();
		int i = 0;
		while(it.hasNext()){
			if(i == index){
				return it.next();
			}
			it.next();
			i++;
		}
		return null;
	}

	public int getSize(){
		if (this.roles == null) {
			return 0;
		}
		return this.roles.size();
	}

	/**
	 * Return the roles as an ArrayList model.
	 */
	public Vector<Role> getRoles(){
		return this.roles;
	}


	/**
	 * Sets the roles.
	 */
	public void setRoles(Vector<Role> roles){
		this.roles = roles;
		this.fireContentsChanged(this, 0, roles.size());
	}

	public boolean addRole(Role role){

		if (!this.isRoleInList(role)){
			this.roles.add(role);
			this.fireContentsChanged(this, 0, this.roles.size());
			return true;
		}
		return false;
	}

	public boolean removeRole(Role role){
		Iterator it = this.roles.iterator();
		Role temp;
		while(it.hasNext()){
			temp = (Role)(it.next());
			if(temp.equals(role)){
				this.roles.remove(temp);
				this.fireContentsChanged(this,0,this.roles.size());
				return true;
			}
		}
		return false;
	}

	private boolean isRoleInList(Role role){
		if (this.roles == null) {
			return false;
		}
		Iterator it = this.roles.iterator();
		while(it.hasNext()){
			if(((Role)(it.next())).equals(role)){
				return true;
			}
		}
		return false;
	}
}