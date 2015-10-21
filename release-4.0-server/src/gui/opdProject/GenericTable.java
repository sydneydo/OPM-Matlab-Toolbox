package gui.opdProject;

import gui.Opcat2;

import java.awt.Color;
import java.awt.Font;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

public class GenericTable {

    private Hashtable config;

    private String tableName;

    /**
         * Initializes a GwnericTable object, and sets the DB table which it is
         * implemented on.
         */
    public GenericTable(String tableName) {
	this.tableName = tableName;
	this.config = new Hashtable();
    }

    /**
         * Returns the name of the DB table that the generic table is saved at.
         */
    public String getTableName() {
	return this.tableName;
    }

    /**
         * Returns a property saved in the generic table. The property might be
         * an object of many types: Font, Color, String etc.
         * 
         * @param property
         *                The name of the property (for example, "Client")
         */
    public Object getProperty(String property) {
	Object prop = this.config.get(property);
	if (prop == null) {
	    return new String("");
	}
	return prop;
    }

    /**
         * Sets a new property.
         * 
         * @param name
         *                The name of the property
         * @param value
         *                The object which will be saved as the property.
         */
    public void setProperty(String name, Object value) {
	this.config.put(name, value);
    }

    public String getDBString(String property) {
	Object value = this.config.get(property);
	if (value != null) {
	    if (value instanceof Integer) {
		return value.toString();
	    }

	    if (value instanceof Font) {
		Font tempFont = (Font) value;
		String retString = (new Integer(tempFont.getSize())).toString();
		retString = retString + " "
			+ (new Integer(tempFont.getStyle())).toString();
		return retString;
	    }

	    if (value instanceof Color) {
		Color tempColor = (Color) value;
		return (new Integer(tempColor.getRGB())).toString();
	    }

	    if (value instanceof String) {
		return (String) value;
	    }

	    JOptionPane
		    .showMessageDialog(
			    Opcat2.getFrame(),
			    " Serious internal bug occured in configuration module \n Please contact software developers.",
			    "Error", JOptionPane.ERROR_MESSAGE);
	    System.exit(1);

	}

	return null;
    }

    public void setDBString(String propertyName, String propertyValue) {
	Object value = this.config.get(propertyName);
	if (value != null) {
	    if (value instanceof Integer) {
		this.config.put(propertyName, new Integer(propertyValue));
		return;
	    }

	    if (value instanceof Font) {
		StringTokenizer st = new StringTokenizer(propertyValue);
		int size = 0;
		int style = 0;
		if (st.hasMoreTokens()) {
		    size = (new Integer(st.nextToken())).intValue();
		} else {
		    JOptionPane
			    .showMessageDialog(
				    Opcat2.getFrame(),
				    " Serious internal bug occured in configuration module (Bad Font Format) \n Please contact software developers.",
				    "Error", JOptionPane.ERROR_MESSAGE);
		    System.exit(1);
		}

		if (st.hasMoreTokens()) {
		    style = (new Integer(st.nextToken())).intValue();
		} else {
		    JOptionPane
			    .showMessageDialog(
				    Opcat2.getFrame(),
				    " Serious internal bug occured in configuration module (Bad Font Format) \n Please contact software developers.",
				    "Error", JOptionPane.ERROR_MESSAGE);
		    System.exit(1);
		}

		this.config
			.put(propertyName, new Font("Our Font", style, size));
		return;

	    }

	    if (value instanceof Color) {
		this.config.put(propertyName, new Color((new Integer(
			propertyValue)).intValue()));
		return;
	    }

	    if (value instanceof String) {
		this.config.put(propertyName, propertyValue);
		return;
	    }

	    JOptionPane
		    .showMessageDialog(
			    Opcat2.getFrame(),
			    " Serious internal bug occured in configuration module \n Please contact software developers.",
			    "Error", JOptionPane.ERROR_MESSAGE);
	    System.exit(1);

	} else {
	    this.config.put(propertyName, propertyValue);
	}

    }

    public Enumeration propertyNames() {
	return this.config.keys();
    }

    public int getSize() {
	return this.config.size();
    }

    /**
         * Sets a hashtable for the generic table data. The new hashtable would
         * replace the current one.
         * 
         * @param A
         *                new Hashtable that would be set.
         */
    public void setTable(Hashtable original) {
	this.config = original;
    }

    /**
         * Returns the current HAshtable that holds the generic table data.
         * 
         * @return A Hashtable that contains configuration infromation in a
         *         various objects as values, and String objects as keys.
         */
    public Hashtable getTable() {
	return this.config;
    }
}
