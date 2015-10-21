package gui.opdGraphics.dialogs;

import gui.opdGraphics.opdBaseComponents.OpdState;
import gui.opmEntities.OpmState;

class StateListCell{
	private OpmState lState;
	private OpdState gState;
	private boolean selected;

	public StateListCell(OpdState opdS, boolean yn)
	{
		this.lState = (OpmState)opdS.getEntity();
		this.gState = opdS;
		this.selected = yn;
	}

	public boolean isSelected()
	{
		return this.selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	public OpmState getOpmState(){
		return this.lState;
	}

	public OpdState getOpdState(){
		return this.gState;
	}

	public void invertSelected()
	{
		this.selected = !this.selected;
	}

	public String toString()
	{
		return this.lState.toString().replace('\n',' ');
	}
}
