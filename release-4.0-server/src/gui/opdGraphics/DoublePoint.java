package gui.opdGraphics;

/**
 * <p>
 * This class implements abstract class <code>java.awt.geom.Point2D</code> and
 * extends its functionality.
 */

public class DoublePoint extends java.awt.Point// java.awt.geom.Point2D
{
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	 
	double x, y;

	/**
	 * Constructor takes double values as arguments
	 * 
	 * @param <code>xval</code> -- x coordinate of the point
	 * @param <code>yval</code> -- y coordinate of the point
	 */
	public DoublePoint(double xval, double yval) {
		this.x = xval;
		this.y = yval;
	}

	/**
	 * Constructor takes int values as arguments
	 * 
	 * @param <code>xval</code> -- x coordinate of the point
	 * @param <code>yval</code> -- y coordinate of the point
	 */
	public DoublePoint(int xval, int yval) {
		this.x = xval;
		this.y = yval;
	}

	public DoublePoint(java.awt.Point p) {
		this.x = p.getX();
		this.y = p.getY();
	}

	/**
	 * @returns x point coordinate
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * @returns y point coordinate
	 */
	public double getY() {
		return this.y;
	}

	/**
	 * @returns rounded x point coordinate, integer
	 */
	public int getRoundX() {
		return (int) (Math.round(this.x));
	}

	/**
	 * @returns rounded y point coordinate, integer
	 */
	public int getRoundY() {
		return (int) (Math.round(this.y));
	}

	/**
	 * Sets x point coordinate
	 * 
	 * @param <code>x</code> -- x point coordinate, double
	 */
	public void setX(double newX) {
		this.x = newX;
	}

	/**
	 * Sets y point coordinate
	 * 
	 * @param <code>y</code> -- y point coordinate, double
	 */
	public void setY(double newY) {
		this.y = newY;
	}

	/**
	 * Sets x point coordinate
	 * 
	 * @param <code>x</code> -- x point coordinate, int
	 */
	public void setX(int newX) {
		this.x = newX;
	}

	/**
	 * Sets y point coordinate
	 * 
	 * @param <code>y</code> -- y point coordinate, int
	 */
	public void setY(int newY) {
		this.y = newY;
	}

	/**
	 * Sets point location
	 * 
	 * @param <code>x</code> -- x point coordinate, double
	 * @param <code>y</code> -- y point coordinate, double
	 */
	public void setLocation(double x, double y) {
	};

	/**
	 * Rotates point round (0, 0) by angle <code>theta</code>
	 * 
	 * @param <code>theta</code> -- angle to rotate by in radians
	 */
	public void rotate(double theta) {
		double a = Math.cos(theta);
		double b = Math.sin(theta);
		double c = -b;
		double d = a;
		double tmpX = this.x;
		double tmpY = this.y;

		this.x = a * tmpX + c * tmpY;
		this.y = b * tmpX + d * tmpY;
	}

	public void rotate(double theta, java.awt.geom.Point2D cCenter) {
		double a = Math.cos(theta);
		double b = Math.sin(theta);
		double c = -b;
		double d = a;
		double tmpX = this.x - cCenter.getX();
		double tmpY = this.y - cCenter.getY();

		this.x = a * tmpX + c * tmpY + cCenter.getX();
		this.y = b * tmpX + d * tmpY + cCenter.getY();
	}

	public java.lang.String toString() {
		return "DoublePoint: " + this.x + " " + this.y + ".";
	}

}