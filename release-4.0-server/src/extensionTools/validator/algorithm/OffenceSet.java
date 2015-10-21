package extensionTools.validator.algorithm;

import java.util.Vector;

/**
 * Holds a set of offences.
 * @author Eran Toch
 * Created: 31/05/2004
 */
public class OffenceSet extends Vector {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	public boolean add(Object obj)	{
		if (obj instanceof Offence)	{
			return super.add(obj);
		}
		return false;
	}
}
