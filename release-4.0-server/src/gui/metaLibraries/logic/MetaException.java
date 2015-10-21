/*
 * Created on 01/05/2004
 */
package gui.metaLibraries.logic;

/**
 * Represent a super class for all exceptions related to Meta-Modeling support in 
 * Opcat.
 * 
 * @author Eran Toch
 * Created: 01/05/2004
 */
public class MetaException extends Exception {
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	 
	String path = "";
	
	/**
	 * A constructor that takes the ontology path as well as the regular exception text. 
	 * @param eText	The text of the exception
	 * @param _path	The path of the MetaLibrary that was onvolved in the exception.
	 */
	public MetaException(String eText, String _path)	{
		super(eText);
		this.path = _path;
	}
	
	/**
	 * Returns the message of the exception, with the path of the ontology contactnated 
	 * at the end of the message.
	 */
	public String getMessage()	{
		String message = super.getMessage();
		message += " \nAt Library "+ this.path;
		return message;		
	}
}
