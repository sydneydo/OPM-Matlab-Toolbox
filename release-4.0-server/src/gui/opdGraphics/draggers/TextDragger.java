package gui.opdGraphics.draggers;
import exportedAPI.RelativeConnectionPoint;
import gui.opdGraphics.Connectable;
import gui.opdGraphics.OpdCardinalityLabel;
import gui.opdProject.OpdProject;

public class TextDragger extends TransparentAroundDragger
{
	//private String text;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	 
	protected OpdCardinalityLabel label;
	private int dX;
	private int dY;


	public TextDragger(Connectable pEdge, RelativeConnectionPoint pPoint, String text, OpdProject pProject)
	{
		super(pEdge, pPoint, pProject);
		this.label = new OpdCardinalityLabel(text, pProject);
		this.dX = 20;
		this.dY = 0;
		this.label.addMouseListener(this.label);
		this.label.addMouseMotionListener(this.label);
		this.label.setLocation(this.getX()+this.dX, this.getY() + this.dY);
		super.updateDragger();
	}

		public OpdCardinalityLabel getOpdCardinalityLabel()
		{
		  return this.label;
		}

		public void setText(String text)
		{
		  this.label.setText(text);
		}

		public String getText()
		{
		  return this.label.getText();
		}

		public void updateDragger()
		{
		  this.dX = this.label.getX() - this.getX();
		  this.dY = this.label.getY() - this.getY();
		  super.updateDragger();
		  this.label.setLocation(this.getX() + this.dX, this.getY() + this.dY);
		}


}
