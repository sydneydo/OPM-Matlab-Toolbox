package extensionTools.opcatLayoutManager;

import java.awt.geom.Line2D;
import java.text.MessageFormat;

/**
 * Adds some minor functionality to the {@link java.lang.Object.Line2D.Double Line2D.Double} class.
 */
public class Line extends Line2D.Double
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Initializes an instance of the class.
     * By convention we always construct lines so that the first endpoint
     * is with smaller x coordinate.
     *
     * @param x1 Specifies the x ccordinate of the first point.
     * @param y1 Specifies the y ccordinate of the first point.
     * @param x2 Specifies the x ccordinate of the second point.
     * @param y2 Specifies the y ccordinate of the second point.
     */
    public Line(double x1, double y1, double x2, double y2)
    {
        if (x1 <= x2)
        {
            this.setLine(x1, y1, x2, y2);
        }
        else
        {
            this.setLine(x2, y2, x1, y1);
        }
    }

    /**
     * Returns a string representation of the object.
     * @see java.lang.Object#toString()
     *
     * @return a string representation of the object
     */
    public String toString()
    {
        return MessageFormat.format( "[({0},{1})-({2},{3})]",
                                     new Object[] { new java.lang.Double(this.getX1()),
                                                    new java.lang.Double(this.getY1()),
                                                    new java.lang.Double(this.getX2()),
                                                    new java.lang.Double(this.getY2())});
    }
}
