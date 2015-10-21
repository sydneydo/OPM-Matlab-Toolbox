package exportedAPI.opcatAPIx;

/**
 * IXStateEntry - interface to logical representation of OPM state
 */

public interface IXStateEntry
    extends IXConnectionEdgeEntry {

  /**
   * Returns String representing minimum activation time
   * of this IXStateEntry. This String contains non-negative integer X 7
   * (msec, sec, min, hours, days, months, years) with semi-colons
   * separation.
   * @return minimum link activation type as string
   */
  public String getMinTime();

  /**
   * Sets minimum activation time
   * of this IXStateEntry. This String contains non-negative integer X 7
   * (msec, sec, min, hours, days, months, years) with semi-colons
   * separation.
   * <strong>Importent</strong>: Legality of given string is not checked when using this method.
   * @param minimum link activation type as string
   */
  public void setMinTime(String minTime);

  /**
   * Returns String representing maximum activation time
   * of this IXStateEntry. This String contains non-negative integer X 7
   * (msec, sec, min, hours, days, months, years) with semi-colons
   * separation.
   * @return maximum link activation type as string
   */
  public String getMaxTime();

  /**
   * Sets maximum activation time
   * of this IXStateEntry. This String contains non-negative integer X 7
   * (msec, sec, min, hours, days, months, years) with semi-colons
   * separation.
   * <strong>Importent</strong>: Legality of given string is not checked when using this method.
   * @param maximum link activation type as string
   */
  public void setMaxTime(String maxTime);

  /**
   * Sets the IXStateEntry to be initial or not accoring to given parameter.
   * @param initialState - true if state going to be initial.
   */
  public void setInitial(boolean initialState);

  /**
   * Checks if IXStateEntry is initial.
   * @return true if state is initial.
   */
  public boolean isInitial();

  /**
   * Sets the IXStateEntry to be final or not accoring to given parameter.
   * @param finalState - true if state going to be final.
   */
  public void setFinal(boolean finalState);

  /**
   * Checks if IXStateEntry is final.
   * @return true if state is final.
   */
  public boolean isFinal();

  /**
   * Checks if IXStateEntry is default.
   * @return true if state is default.
   */
  public boolean isDefault();

  /**
   * Sets the IXStateEntry to be default or not accoring to given parameter.
   * @param defaultState - true if state going to be default.
   */
  public void setDefault(boolean defaultState);

  /**
   * Returns parent IXObjectEntry of this state.
   */
  public IXObjectEntry getParentIXObjectEntry();

  /**
   * Checks if IXStateEntry has graphical representation which is visible
   * (not hidden by user) in Opd specified by <code>opdNumber</code> parameter
   * @param opdNumber specifies an OPD to check graphical representation in
   * @return true if IXStateEntry has an graphical representation which is visible
   * (not hidden by user)in specified OPD, otherwise false
   */
  public boolean hasVisibleInstancesInOpd(long opdNumber);


}