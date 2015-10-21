package gui.repository;

import gui.images.standard.StandardImages;

public class ViewsView extends BaseView {

	public ViewsView(String invisibleRoot) {
		super(invisibleRoot);
		this.tip = new String("Manage views");
		this.viewName = "Views";
		this.icon = StandardImages.System_Icon;
		this.setType(Repository.ViewsView);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
