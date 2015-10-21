package gui.license;


import gui.opdProject.OpdProject;


/**
 * 
 * @author Eran Toch
 */
public class PolicyChecker {

	private ILicense license = null;
	
	public PolicyChecker(ILicense _license)	{
		this.license = _license;
	}
	
	/**
	 * checks wheter the Opcat license allows to add another thing (object or process)
	 * to the model.
	 * @return	<code>true</code> if it's legal to add another one, or <code>false</code>
	 * otherwise.
	 */
	public boolean canAddThing(OpdProject project)	{
		//System.out.println(project.getNumOfEntries());
		if (this.license == null)	{
			return true;
		}
		if (this.license.isLimitedLicense() == false)	{
			return true;
		}
		else	{
			if (project.getNumOfEntries() + 1 > this.license.getMaxNumberOfThings())	{
				return false;
			}
			else	{
				return true;
			}
		}
	}
}
