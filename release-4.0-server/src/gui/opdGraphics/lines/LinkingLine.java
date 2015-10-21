package gui.opdGraphics.lines;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

public class LinkingLine extends JComponent
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	 
	private boolean leftDirection;

    public LinkingLine()
    {
      this.leftDirection = true;
    }

    public void setLeftDirection(boolean lDirection)
    {
      this.leftDirection = lDirection;
    }

    public void paintComponent(Graphics g)
    {
      Graphics2D g2 = (Graphics2D)g;
      Object AntiAlias = RenderingHints.VALUE_ANTIALIAS_ON;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, AntiAlias);


      g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[]{6}, 0.0f));

      if (this.leftDirection)
      {
        g2.drawLine(1, 1, this.getWidth()-1, this.getHeight()-1);
      }
      else
      {
        g2.drawLine(1, this.getHeight()-1, this.getWidth()-1, 1);
      }
    }
}
