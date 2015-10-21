package exportedAPI.opcatAPIx;

import java.util.Enumeration;

/**
 * IXObjectEntry is an interface to logical representation of Object in OPM
 * meaning
 */

public interface IXObjectEntry extends IXThingEntry {

    /**
         * Checks if this object is persistent.
         * 
         * @return true if object is persistent.
         */
    public boolean isPersistent();

    /**
         * Sets the persistent property of this OpmObject.
         * 
         */
    public void setPersistent(boolean persistent);

    /**
         * Returns String representing type property of this OPM object.
         * 
         * @return String represening type of the OPM object
         */
    public String getType();

    /**
         * Sets type property of this OPM Object. Do not set this property
         * directly without valididy check
         * 
         * @param new
         *                type for OPM object
         */
    public void setType(String type);

    /**
         * Checks if OPM object is a key.
         * 
         * @return true if the OPM object is key, false otherwise
         */
    public boolean isKey();

    /**
         * Sets key property of this OPM Object.
         * 
         * @param true
         *                if OPM object is key
         */
    public void setKey(boolean key);

    /**
         * Returns index name of this OPM Object.
         * 
         * @return index name of this OPM Object.
         */
    public String getIndexName();

    /**
         * Sets index name of this OPM Object.
         * 
         * @param new
         *                index name for this OPM Object.
         */
    public void setIndexName(String indexName);

    /**
         * Returns index order of this OPM Object.
         * 
         * @return index order of this OPM Object.
         */
    public int getIndexOrder();

    /**
         * Sets index order of this OPM Object.
         * 
         * @param new
         *                index order of this OPM Object.
         */
    public void setIndexOrder(int indexOrder);

    /**
         * Returns initial value of this OPM Object.
         * 
         * @return initial value of this OPM Object.
         */
    public String getInitialValue();

    /**
         * Sets initial value of this OPM Object.
         * 
         * @param new
         *                initial value of this OPM Object.
         */
    public void setInitialValue(String initialValue);

    /**
         * Sets the IXObjectEntry's type origin.<br>
         * If IXObjectEntry is an instance of some non-basic type this method
         * sets the ID of IXObjectEntry that represents type definition.
         * 
         * @param typeOriginId -
         *                the ID of IXObjectEntry that represents type
         *                definition
         * @see IXObjectEntry#getTypeOriginId()
         */
    public void setTypeOriginId(long typeOriginId);

    /**
         * Returns the IXObjectEntry's type origin.<br>
         * If IXObjectEntry is an instance of some non-basic type this method
         * returns the ID of IXObjectEntry that represents type definition.
         * 
         * @return the ID of IXObjectEntry that represents type definition.
         * @see IXObjectEntry#setTypeOriginId(long typeOriginId)
         */
    public long getTypeOriginId();

    /**
         * Returns Enumeration of IXStateEntry which contains all states
         * belonging to this object. Use the Enumeration methods on the returned
         * object to fetch the IXStateEntry sequentially
         */

    public Enumeration getStates();

}