package exportedAPI.opcatAPI;

/**
 * IStateEntry - interface to logical representation of OPM state
 */

public interface IStateEntry
    extends IConnectionEdgeEntry {

  /**
   * Returns String representing minimum activation time
   * of this IStateEntry. This String contains non-negative integer X 7
   * (msec, sec, min, hours, days, months, years) with semi-colons
   * separation.
   * @return minimum link activation type as string
   */
  public String getMinTime();

  /**
   * Returns String representing maximum activation time
   * of this IStateEntry. This String contains non-negative integer X 7
   * (msec, sec, min, hours, days, months, years) with semi-colons
   * separation.
   * @return maximum link activation type as string
   */
  public String getMaxTime();

  /**
   * Checks if IStateEntry is initial.
   * @return true if state is initial.
   */
  public boolean isInitial();

  /**
   * Checks if IStateEntry is final.
   * @return true if state is final.
   */
  public boolean isFinal();

  /**
   * Checks if IStateEntry is default.
   * @return true if state is default.
   */
  public boolean isDefault();

  /**
   * Returns parent IObjectEntry of this state.
   */

  public IObjectEntry getParentIObjectEntry();

  /**
   * Checks if IStateEntry has graphical representation which is visible
   * (not hidden by user) in Opd specified by <code>opdNumber</code> parameter
   * @param opdNumber specifies an OPD to check graphical representation in
   * @return true if IStateEntry has an graphical representation which is visible
   * (not hidden by user)in specified OPD, otherwise false
   */
  public boolean hasVisibleInstancesInOpd(long opdNumber);

}