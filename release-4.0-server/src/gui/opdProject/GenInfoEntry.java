package gui.opdProject;

/**
 * Represent a General Information Entry. The class is used in the <code>ProjectPropertiesDialog<code>
 * class. Each entry contains 3 fields:<p>
 * <ui>
 * <li>key - the key that identifies the entry.
 * <li>value - the value of the entry.
 * <li>title - the title of the field.
 * </ui>
 * @author Eran Toch
 * @see    ProjectPropertiesDialog
 * @version 1.0
 */

public class GenInfoEntry {

	private String key = null;
	private String title = null;
	private String value = null;

	/**
	 * Constructor. Creates the entry according to the given fields.
	 */
    public GenInfoEntry(String key, String title, String value) {
		this.key = key;
		this.title = title;
		this.value = value;
    }

	/**
	 * Empty constructor.
	 */
	public GenInfoEntry()
	{
		return;
	}

	/**
	 * Returns the title.
	 */
	public String toString()
	{
		return this.title;
	}

	/**
	 * Returns the key.
	 */
	public String getKey()
	{
		return this.key;
	}

	/**
	 * Returns the value of the entry.
	 */
	public String getValue()
	{
		return this.value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
}