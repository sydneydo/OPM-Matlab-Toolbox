package gui.views;

import java.util.HashMap;

public class OpcatView extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name = "";

	protected OpcatView(String name) {
		this.name = name;
	}

	public void addComponent(String id, Object obj)
			throws ViewComponentExistsException {
		if (isComponentExists(id)) {
			throw new ViewComponentExistsException("Component Exists");
		}

		put(id, obj);
	}

	public Object getComponent(String id)
			throws ViewComponentExistsException {

		if (!isComponentExists(id)) {
			throw new ViewComponentExistsException("Component does not Exists");
		}

		return get(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isComponentExists(String id) {
		return (get(id) != null);
	}

}
