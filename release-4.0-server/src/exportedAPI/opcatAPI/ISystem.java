package exportedAPI.opcatAPI;
import java.util.Date;
import java.util.Enumeration;

import javax.swing.JFrame;

/**
 * ISystem is an interface to Opcat2 system. This is the 'enterance' to internal structure.
 * Through this class one can get access to ISystemStructure and all other info. See example how to get
 * into ISystem.
 */

public interface ISystem
{
    /**
    * Returns the main OPCAT frame.
    * @return JFrame main OPCAT frame.
    */
    public JFrame getMainFrame();

	/**
	* Returns an interface to current OPD (user is currently working with this OPD)
	* @return current IOpd
	*/
	public IOpd getCurrentIOpd();


	/**
	* Returns an interface to ISystemStructure - data structure which
    * holds all inforation about this project
	* @return ISystemStructure Opcat's ISystemStructure
	* @see ISystemStructure
	*/
	public ISystemStructure getISystemStructure();


	/**
	* Returns project's name
	* @return String project name
	*/
	 public String getName();

	/**
	* Returns name of project's creator
	* @return author string
	*/
   public String getCreator();

	/**
	* Returns project's creaion date
	* @return project creaion date
	*/

   public String getInfoValue(String key);
   public Date getCreationDate();

	/**
	 * Returns String that holds OPL for OPD given by opdNum
	 * @param isHTML - if true String returned is HTML formated string, if false plain text OPL
	 * @param opdNum - the ID of OPD to generate OPL for.
	 * @return String that holds OPL for OPD given by opdNum
	 */
   public String getOPL(boolean isHTML, long opdNum);

	/**
	 * Returns String that holds OPL for System.
	 * @param isHTML - if true String returned is HTML formated string, if false plain text OPL
	 * @return String that holds OPL for System
	 */
   public String getOPL(boolean isHTML);

   /**
    * Returns ILinkInstance between given source and detination.
    * Returns null if no link connects given source and destination.
    * @param source - source for possible relation.
    * @param destination - destination for possible relation.
    * @return ILinkInstance or null if no link connects given source and destination.
    */

   public ILinkInstance getILinkBetweenInstances(IConnectionEdgeInstance source, IConnectionEdgeInstance destination);

   /**
    * Returns Enumeration of IRelationInstance between given source and
    * detination. Returns empty enumeration if no relation connects
    * given source and destination.
    * @param source - source for possible relation.
    * @param destination - destination for possible relation.
    * @return Enumeration of IRelationInstance.
    */
   public Enumeration getIRelationsBetweenInstances(IConnectionEdgeInstance source, IConnectionEdgeInstance destination);

   /**
	* Returns an Enumeration of all the <code>MetaLibrary</code> models imported
	* by this system.
	* @return	Enumeration containing <code>MetaLibrary</code> objects.
	*/
   public Enumeration getMetaLibraries();
   
   public Enumeration getMetaLibraries(int type);
}
