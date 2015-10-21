package opmTypes;

public class opmState {

  String stateName;
  boolean isInitial;
  boolean isFinal;
  boolean isCurrent;

  public opmState(String newStateName, boolean newIsInitial, boolean newIsFinal, boolean newIsDefault) {
    this.stateName = newStateName;
    this.isInitial = newIsInitial;
    this.isFinal = newIsFinal;
    this.isCurrent = newIsDefault;
  }

  public String getStateName() {
    return this.stateName;
  }

  public boolean getIsInitial() {
    return this.isInitial;
  }

  public boolean getIsFinal() {
    return this.isFinal;
  }

  public boolean getIsCurrent() {
    return this.isCurrent;
  }

  public void whenEnters() {
    this.isCurrent = true;
  }

  public void whenExists() {
    this.isCurrent = false;
  }

}