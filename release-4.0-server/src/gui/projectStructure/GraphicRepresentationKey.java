package gui.projectStructure;
import exportedAPI.OpdKey;


public class GraphicRepresentationKey
{
  private OpdKey myOpdKey;
  private int relationType;



	public GraphicRepresentationKey(OpdKey key, int relType)
	{
		this.myOpdKey = key;
		this.relationType = relType;
	}

/**
* Returns a hash code value for the OpdKey.
* This method is supported for the benefit of hashtables such as
* those provided by java.util.Hashtable.
*/

	public int hashCode()
	{
		return this.myOpdKey.hashCode();
	}

/**
* Indicates whether some other object is "equal to" this one.
*/
	public boolean equals(Object obj)
	{

		GraphicRepresentationKey temp;
		if (!(obj instanceof GraphicRepresentationKey))
		{
			return false;
		}

		temp = (GraphicRepresentationKey)obj;
		if ((temp.getRelationType() == this.relationType) && temp.getOpdKey().equals(this.myOpdKey))
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	public OpdKey getOpdKey()
	{
		return this.myOpdKey;
	}

	public int getRelationType()
	{
		return this.relationType;
	}

/**
* Returns a string representation of this GraphicRepresentationKey.
*
* @return a string representation of this GraphicRepresentationKey
**/

  	public String toString()
	{
		return this.myOpdKey+" "+this.relationType;
	}

}
