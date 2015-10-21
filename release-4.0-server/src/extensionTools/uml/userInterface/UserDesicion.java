package extensionTools.uml.userInterface;

import java.util.Vector;

import extensionTools.uml.umlDiagrams.DiagramsCreator;

/**
 * The class is used as a communication message between {@link UMLChooser} and
 * {@link DiagramsCreator}.
 * @author Eran Toch
 *
 */
public class UserDesicion {
	/**
	 * If class diagram will be generated.
	 */
	public boolean UClassYes = false;
	
	/**
	 * If usecase diagram (root level) will be generated
	 */
	public boolean UURoot = false;
	
	/**
	 * If usecase diagram (all levels) will be generated
	 */
	public boolean UUAll = false;
	
	/**
	 * If no usecase diagram will be generated
	 */
	public boolean UUNone = true;
	
	/**
	 * if usecase diagram (up to a certain level) will be generated 
	 */
	public boolean UUUntil = false;
	public boolean UStateYes = false;
	public boolean USeqYes = false;
	public boolean UMLActTop = false;
	public boolean UMLActElse = false;
	public boolean UMLActListAll = false;
	public boolean UDepYes = false;
	
	public String UMLActTopText = "";
	public String UMLActListNum = "";
	public String UUUntilText = "";
	
	public int USeqList[] = null;
	public int UStateList[] = null;
	public int UMLActList[] = null;
	
	public Vector v_proc = null; 
	public Vector v_obj = null; 
	public Vector v_act = null; 
}
