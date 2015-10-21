package extensionTools.validator.algorithm;


/**
 * Represent a role which is the target part of a constraint.
 * @author Eran Toch
 * Created: 06/05/2004
 */
public class Applicant {
	private VRole theRole;
	
	public Applicant(VRole role)	{
		this.theRole = role;
	}
	
	public Applicant()	{}
	
	public VRole getRole()	{
		return this.theRole;
	}
	
	public boolean contains(VRole role)	{
		if (this.theRole.equals(role))	{
			return true;
		}
		return false;
	}
	
	public String toString()	{
		return this.theRole.toString();
	}
}
