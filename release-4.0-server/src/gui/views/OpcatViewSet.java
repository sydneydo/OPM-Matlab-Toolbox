package gui.views;

import java.util.HashMap;

public class OpcatViewSet extends HashMap<String, OpcatView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static OpcatViewSet viewset = null ; 

	private OpcatViewSet() {
		super();
	}
	
	public static OpcatViewSet getInstance() {
		if (viewset== null) {
			viewset = new OpcatViewSet() ; 
		}
		return viewset ; 
	}

	public OpcatView addView(String name) throws ViewExistsExecption {
		if (isViewExists(name)) {
			throw new ViewExistsExecption("View exists");
		}
		OpcatView view = new OpcatView(name);
		put(name, view);
		return view;
	}

	public OpcatView getView(String name) throws ViewExistsExecption {
		if (!isViewExists(name)) {
			throw new ViewExistsExecption("View does not exist");
		}

		return get(name);
	}

	public void removeView(String name) throws ViewExistsExecption {
		remove(name);
	}

	public boolean isViewExists(String name) {
		return (get(name) != null);
	}

	public void SaveViewAs(String filename) throws ViewExistsExecption {

		if (!isViewExists(filename)) {
			throw new ViewExistsExecption("View does not exist");
		}

		/**
		 * save as a viewer file
		 * 
		 * i.e. save the all file but mark this view as active view. this will
		 * hide all outlook bars besides this view in the views bar.
		 * 
		 * 
		 */

		/**
		 * set the active view property of this project
		 */

		/**
		 * save the project
		 */

		/**
		 * end save
		 */
	}

}
