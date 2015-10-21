package exportedAPI.opcatAPI;
import java.awt.image.BufferedImage;
import java.awt.print.Printable;
import java.util.Enumeration;

/**
 * IOpd - is an interface for OPD.
 */


public interface IOpd extends Printable
{

	/**
	 * @return The unique ID of the Opd.
	 */
	public long getOpdId();

	/**
	 * @return The name of the Opd
	 */
	public String getName();

	/**
	 * If OPD is not ROOT OPD it always has parent OPD, from which it was unfolded or inzoomed.
	 * This method returns IOpd interface to parent OPD.
	 * @return IOpd interface to parent OPD.
	 */
	public IOpd getParentIOpd();

	/**
	 * Return an Enumeration of all items selected in this OPD.
	 * @return an Enumeration of selected items
	 */
	public Enumeration getSelectedItems();

	/**
	 * If the OPD is inzoomed or unfolded returns an IThingEntry of OPM thing
	 * that is inzoomed or unfolded by this OPD
	 * @return IThingEntry of OPM thing that is inzoomed or unfolded
	 */
	public IThingEntry getMainIEntry();

	/**
	 * If the OPD is inzoomed or unfolded returns an IThingInstance (interface to graphical
	 * representation) of inzoomed or unfolded OPM thing in this OPD
	 * @return IThingInstance of inzoomed or unfolded OPM thing in this OPD
	 */

	public IThingInstance getMainIInstance();
	/**
	 * Return an BufferedImage representation of this OPD.
	 */

    public BufferedImage getImageRepresentation();
}