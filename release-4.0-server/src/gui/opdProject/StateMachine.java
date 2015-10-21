package gui.opdProject;

import gui.Opcat2;

/**
 *  <p>This class is State Machine or Automata that tracks all GUI activity.
 *  We have to have such mechanizm because some user action supposed to be followed by
 *  other action (for example: clicking on "Object" button on toolbar supposed to be followed by clicking
 *  on <a href = "../opdGraphics/DrawingArea.html"><code>DrawingArea</code></a> ).
 *  If any action followed by illegal operation <a href = "IllegalPassException.html"><code>IllegalPassException</code></a> is thrown.</p><br>
 *  Default state for machine is <code>USUAL</code>, in this state machine waits for user action.
 *  There are main work modes for <code>OpdStateMachine</code>
 *  <ol>
 *		<li>Adding Object or Process.<br>
 *			By clicking "Add Object" or "Add Process" buttons or choosing the same actions from "OPM notation" menu,
 *			user passes the machine to <code>ADD_OBJECT</code> or <code>ADD_PROCESS</code> state relatively
 *			After this by clicking an empty area on <code>DrawingArea</code> user adds chossed thing to diagram and passes the machine to <code>USUAL</code> state.</il>
 *		<li>Adding link.<br>
 *			By ckicking "Add *** Link" button, where *** is a link name, or choosing the same actions from "OPM notation" menu,
 *			user passes the machine to <code>ADD_***_LINK</code>. Then by clicking any thing on diagram, user defines first edge for the link and
 *			passes the machine to <code>ADD_***_LINK_FIRST</code> (first thing is clicked).
 *			After this, by clicking another thing, user creates link, adds it to diagram and passes the machine to <code>USUAL</code> state.
 *			<strong>note:</strong> link is created and added only if it is legal in OPM.</li>
 *		<li>Adding link.<br>
 *			By ckicking "Add *** Relation" button, where *** is a relation name, or choosing the same actions from "OPM notation" menu,
 *			user passes the machine to <code>ADD_***_RELATION</code>. Then by clicking any thing on diagram, user defines source for the relation and
 *			passes the machine to <code>ADD_***_RELATION_FIRST</code> (first thing is clicked). For the Bi Directional Relation source and destination has no meaning.
 *			After this, by clicking another thing, user creates relation, adds it to diagram and passes the machine to <code>USUAL</code> state.
 *			<strong>note:</strong> realation is created and added only if it is legal in OPM.</li>
 *	</ol>
 *	<strong>Note:</strong> In other cases the machine will throw <a href = "IllegalPassException.html"><code>IllegalPassException</code></a>.
 *	<strong>Note:</strong> User always can reset the machine to <code>USUAL</code> state by pressing "Escape" key, or clicking an empty area in <code>DrawingArea</code> (the second option will not work with addind Object or Processes
 */
public class StateMachine {
  /**
   *
   */
//	public final static int USUAL = 1000;
  public final static int USUAL_SELECT = 1000;
  public final static int USUAL_MOVE = 1001;
  public final static int IN_SELECTION = 1002;
  public final static int USUAL_DRAW = 1003;/**************HIOTeam*******************/
  
  /*
   * States for forcing the user to finish a CUT operation
   * 
   */
  public final static int EDIT_CUT = 1004;


  /**
   *
   */
  public final static int ADD_PROCESS = 2000;

  /**
   *
   */
  public final static int ADD_OBJECT = 3000;
  public final static int ADD_STATE = 3500;
  public final static int ADD_LINK = 4000;
  public final static int ADD_RELATION = 5000;
  public final static int ADD_GENERAL_RELATION = 6000;

  public final static int ADD_NOTE = 7000;

  private static int currentState = USUAL_SELECT;
  private static int desiredComponent;
  private static boolean waiting = false;
  private static boolean animated = false;

  /**
       *  Constructs state machine, that is in it's default state <code>USUAL</code>.
   */
  public StateMachine() {
    currentState = USUAL_SELECT;
    waiting = false;
  }

  /**
   *  @retun The current state of the machine
   */

  public static boolean isWaiting() {
    return waiting;
  }

  public static void setWaiting(boolean wait) {
    waiting = wait;
  }

  public static boolean isAnimated() {
    return animated;
  }

  public static void setAnimated(boolean isAnimated) {
    animated = isAnimated;
  }

  public static int getCurrentState() {
    return currentState;
  }

  public static int getDesiredComponent() {
    return desiredComponent;
  }

  public static void setDesiredComponent(int desComponent) {
    desiredComponent = desComponent;
  }
  
  private static int _reset() {
	    int tmpState = currentState;
	    
	    if (Opcat2.getDrawingAreaOnMouseDragAction().equals("select")) {
	      currentState = USUAL_SELECT;
	    }
	    else if(Opcat2.getDrawingAreaOnMouseDragAction().equals("draw"))/***HIOTeam****/
	    {
	          currentState = USUAL_DRAW;
	    }
	    else { 
	      currentState = USUAL_MOVE;
	    }
	    return tmpState;
	  }


  /**
   *  This method resets the machine to the <code>USUAL</code> state. the user can reset th emachine only if it was not in a EDIT OP
   *  at the moment it's only EDIT_CUT. 
   *  @return The state in which the machine was, when was reseted.
   */
  public static int reset() {
    
    if (currentState == StateMachine.EDIT_CUT ) { 
    	/**
    	 * do nothing if we are in a CUT 
    	 */
    	return currentState ; 
    } 
	/**
	 * reset if it's not in CUT
	 */
    return _reset() ;
  }

  /**
   * This method will reset the machine to the <code>USUAL</code> state even if it was in a CUT state
   * @param CutReset Is the reset done to realese from a CUT state ? 
   * @return the state in which th emachine was before th ereset
   */
  public static int reset(boolean CutReset) {
	   
	  if (CutReset ) {
		  return _reset(); 
	  }   
	  return reset(); 
  }
  
  /**
   *  The passes function for the machine.
   *  @param nextState the state machine should go to.
   *  @retun the current machine state.
   */
  public static int go(int nextState, int desComponent) throws
      IllegalPassException {
	  
	
    desiredComponent = desComponent;
    switch (currentState) {
      case USUAL_SELECT:
    	  switch (nextState) {
    	  case EDIT_CUT: 
    		  currentState = nextState ; 
    		  break ; 
    	  }  
    	  if (currentState == EDIT_CUT) {
			break ;
		} 
      case USUAL_MOVE:
      case USUAL_DRAW:/************************HIOTeam*******************************/

//				if (nextState != FIRST_THING_CLICKED && nextState != SECOND_THING_CLICKED)
        switch (nextState) {
          case ADD_OBJECT:
          case ADD_PROCESS:
          case ADD_STATE:
          case ADD_RELATION:
          case ADD_GENERAL_RELATION:
          case ADD_LINK:
          case IN_SELECTION:
            currentState = nextState;
            break;
          default:
            throw new IllegalPassException("You cannot pass from " +
                                           getStringEntepretation(currentState) +
                                           " to" +
                                           getStringEntepretation(currentState));
        }
        break;

      case ADD_PROCESS:
      case ADD_OBJECT:
      case ADD_STATE:
      case ADD_RELATION:
      case ADD_GENERAL_RELATION:
      case ADD_NOTE:
      case ADD_LINK:

        switch (nextState) {
          case ADD_PROCESS:
          case ADD_OBJECT:
          case ADD_STATE:
          case ADD_RELATION:
          case ADD_GENERAL_RELATION:
          case ADD_NOTE:
          case ADD_LINK:
          case USUAL_SELECT:
          case USUAL_MOVE:
          case USUAL_DRAW:/********************HIOTeam****************************/
            currentState = nextState;
            break;
        	  
          default:
            throw new IllegalPassException("You cannot pass from " +
                                           getStringEntepretation(currentState) +
                                           " to" +
                                           getStringEntepretation(currentState));
        }
      case EDIT_CUT: 
      case IN_SELECTION:
        currentState = nextState;     
    }
    return currentState;
  }

  /**
   *  @param state any legal state for the machine.
   *  @return string representation for the given state e.g for state <code>USUAL</code> it will return the string "USUAL".
   */
  public static String getStringEntepretation(int state) {
    switch (state) {
      case USUAL_SELECT:
        return new String("USUAL_SELECT");
      case USUAL_MOVE:
        return new String("USUAL_MOVE");
      case USUAL_DRAW: /*************HIOTeam******************/
        return new String("USUAL_DRAW");/*************HIOTeam******************/
      case IN_SELECTION:
        return new String("IN_SELECTION");
      case ADD_PROCESS:
        return new String("ADD_PROCESS");
      case ADD_OBJECT:
        return new String("ADD_OBJECT");
      case ADD_RELATION:
        return new String("ADD_RELATION");
      case ADD_LINK:
        return new String("ADD_LINK");
      case ADD_NOTE:
        return new String("ADD_NOTE");
      case EDIT_CUT:
          return new String("EDIT_CUT");        
    }
    return new String("NO_SUCH_STATE");
  }
}