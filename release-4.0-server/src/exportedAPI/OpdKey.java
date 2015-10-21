package exportedAPI;

/**
* This class defines a key for graphical instances of OPM entities.
* Actually this key is compound from two elements - OPD id to which this entity
* belongs and entity in OPD id(id of this instance in specified OPD).
*/

public class OpdKey
{
  private long opdId;
  private long entityInOpdId;

  
	/**
	* Creates OpdKey with specified pOpdId and pEntityInOpdId.
	*
	* @param pOpdId OPD id to which this entity belongs.
	* @pEntityInOpdId entity in OPD id(id of this instance in specified OPD).
	*/
	public OpdKey(long pOpdId, long pEntityInOpdId)
	{
		this.opdId=pOpdId;
		this.entityInOpdId=pEntityInOpdId;
	}

	/**
	* Returns a hash code value for the OpdKey.
	* This method is supported for the benefit of hashtables such as
	* those provided by java.util.Hashtable.
	*/
	public int hashCode()
	{
		return (int)this.opdId;
	}

	/**
	* Indicates whether some other object is "equal to" this one.
	*/
	public boolean equals(Object obj)
	{

		OpdKey tempKey;
		if (!(obj instanceof OpdKey))
		{
			return false;
		}

		tempKey = (OpdKey)obj;
		if ((tempKey.getOpdId() == this.opdId) && (tempKey.getEntityInOpdId() == this.entityInOpdId))
		{
			return true;
		}
		return false;
	}


	/**
	* Returns an OPD id of this OpdKey(OPD id to which this entity
	* belongs).
	*
	**/
	public long getOpdId()
	{
		return this.opdId;
	}

	/**
	* Returns entity in OPD id (id of this instance in OPD to which it belongs) of this OpdKey.
	*
	**/
	public long getEntityInOpdId()
	{
		return this.entityInOpdId;
	}

	/**
	* Returns a string representation of this OpdKey.
	*
	* @return a string representation of this OpdKey
	**/
	public String toString()
	{
		return this.opdId+" "+this.entityInOpdId;
	}

}
