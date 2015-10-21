package gui.opdGraphics.popups;
import gui.actions.edit.CopyAction;
import gui.actions.edit.CutAction;
import gui.actions.edit.DeleteAction;
import gui.actions.edit.PasteAction;
import gui.images.standard.StandardImages;
import gui.opdProject.OpdProject;
import gui.projectStructure.Instance;

import javax.swing.JPopupMenu;


public class DefaultPopup extends JPopupMenu
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	 
	OpdProject myProject;
	Instance myInstance;
	protected boolean multiSelected = false;

	public DefaultPopup(OpdProject prj, Instance inst)
	{
		//super(((ThingInstance)(prj.getCurrentOpd().getSelectedItem())).getThing().getEntity().getEntityName());
//		setSelectionModel(new MySingleSelectionModel());
		this.myProject = prj;
		this.myInstance = inst;
		if((inst != null) && (this.myProject.getComponentsStructure().getOpd(inst.getKey().getOpdId()).getSelectedItemsHash().size() > 1))
		{
			this.multiSelected = true;
		}

	}

	CopyAction copyAction = new CopyAction("Copy", StandardImages.COPY) ;
	CopyAction copyToParentAction = new CopyAction("Copy to Upper SD", StandardImages.COPY) ; 
	PasteAction pasteAction = new PasteAction("Paste", StandardImages.PASTE);
	CutAction cutAction = new CutAction("Cut", StandardImages.CUT ) ;   
	DeleteAction deleteAction = new DeleteAction("Delete", StandardImages.DELETE);

}