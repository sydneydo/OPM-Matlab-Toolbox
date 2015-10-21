package extensionTools.opltoopd;


/**
 * Represents a State class
 */
public class State {
  private String StateName;
  private String MaxTime;
  private String MinTime;
  private boolean IsInitial;
  private boolean IsFinal;
  private boolean From = false;

  /**
  * Creates a new State
  */
  public State(){}

  /**
  * Creates a new State
  * @param name State name
  * @param max State max time activation
  * @param min State min time activation
  * @param isinitial is true if the State is initial
  * @param isfinal is true if the State is final
 */
  public State(String name,String max, String min,
               boolean isinitial, boolean isfinal) {
    this.StateName = name;
    this.MaxTime = max;
    this.MinTime = min;
    this.IsInitial = isinitial;
    this.IsFinal = isfinal;
  }

  /**
   * Returns the State name
   * @return State name
   */
  public String getStateName(){
    return this.StateName;
  }

  /**
   * Returns the max time activation of the State
   * @return MaxTime or null
   */
  public String getMaxTime(){
    return this.MaxTime;
  }

  /**
   * Returns true if the State is a source.
   * @return true if the State is source
   */
  public boolean getFrom(){
    return this.From;
  }

  /**
   * Returns the min time activation of the State
   * @return MinTime or null
   */
  public String getMinTime(){
    return this.MinTime;
  }

  /**
   * Returns true if the State is Initial
   * @return is Initial
   */
  public boolean getIsInitial(){
    return this.IsInitial;
  }

  /**
   * Returns true if the State is Final
   * @return is Final
   */
  public boolean getIsFinal(){
   return this.IsFinal;
  }

  /**
   * Sets the name of the State
   * @param name the name of the State
   */
  public void setStateName(String name){
    this.StateName = name;
  }

  /**
   * Sets the from parameter of the State
   * @param from true if the State is sourse
   */
  public void setFrom(boolean from){
    this.From = from;
  }

  /**
   * Sets the max time activation of the State
   * @param max the max time activation of the State
   *                     or null if there is no max time activation
   */
  public void setMaxTime(String max){
    this.MaxTime = max;
  }

  /**
   * Sets the min time activation of the State
   * @param min the min time activation of the State
   *                     or null if there is no min time activation
   */
  public void setMinTime(String min){
    this.MinTime = min;
  }

  /**
   * Sets the initial parameter of the State
   * @param isinitial true if State is initial
 */
  public void setIsInitial(boolean isinitial){
    this.IsInitial = isinitial;
  }

  /**
   * Sets the final parameter of the State
   * @param isfinal true if State is final
 */
  public void setIsFinal(boolean isfinal){
    this.IsFinal = isfinal;
  }

  /**
  * Overrides method in Object class
  *
  */
  public boolean equals (State newState){
    if (!(this.StateName.equals(newState.getStateName()))) {
		return false;
	}

    if ((this.MaxTime!=null)&&(newState.getMaxTime()!=null)) {
		if (!(this.MaxTime.equals(newState.getMaxTime()))) {
			return false;
		} else
		  if(((this.MaxTime!=null)&&(newState.getMaxTime()==null))|| ((this.MaxTime==null)&&(newState.getMaxTime()!=null))) {
			return false;
		}
	}

    if ((this.MinTime!=null)&&(newState.getMinTime()!=null)) {
		if (!(this.MinTime.equals(newState.getMinTime()))) {
			return false;
		} else
		  if(((this.MinTime!=null)&&(newState.getMinTime()==null))|| ((this.MinTime==null)&&(newState.getMinTime()!=null))) {
			return false;
		}
	}

    if (this.IsInitial!=newState.IsInitial) {
		return false;
	}
    if (this.IsFinal!=newState.IsFinal) {
		return false;
	}
    return true;

  }

}