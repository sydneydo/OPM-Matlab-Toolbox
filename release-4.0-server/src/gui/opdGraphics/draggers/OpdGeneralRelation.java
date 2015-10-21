package gui.opdGraphics.draggers;

import exportedAPI.RelativeConnectionPoint;
import gui.opdGraphics.Connectable;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmGeneralRelation;

public abstract class  OpdGeneralRelation extends OpdRelationDragger
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1912451108654157824L;

	public OpdGeneralRelation(Connectable pEdge, RelativeConnectionPoint pPoint, OpmGeneralRelation pRelation,
                                              long pOpdId, long pEntityInOpdId, OpdProject pProject)
    {
        super(pEdge,  pPoint,  pRelation, pOpdId, pEntityInOpdId, pProject);
    }
}