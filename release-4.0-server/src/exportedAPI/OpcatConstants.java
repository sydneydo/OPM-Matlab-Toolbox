package exportedAPI;

/**
 * OpcatConstants contains different constants used in Opcat2 system
 */

public class OpcatConstants {

  /**
   * Represents public scope
   */
  public final static String PUBLIC = "0";

  /**
   * Represents protected scope
   */
  public final static String PROTECTED = "1";

  /**
   * Represents private scope
   */
  public final static String PRIVATE = "2";

  /**
   * OPM object type
   */
  public static final int OBJECT = 100;

  /**
   * OPM process type
   */
  public static final int PROCESS = 101;

  /**
   * OPM state type
   */
  public static final int STATE = 102;

  /**
   * OPM note type
   */
  public static final int NOTE = 103;

  /**
   * OPM Specialization fundamental relation type
   */
  public static final int SPECIALIZATION_RELATION = 201;

  /**
   * OPM Exfibition fundamental relation type
   */
  public static final int EXHIBITION_RELATION = 202;

  /**
   * OPM Instantiation fundamental relation type
   */
  public static final int INSTANTINATION_RELATION = 203;

  /**
   * OPM Aggregation fundamental relation type
   */
  public static final int AGGREGATION_RELATION = 204;

  /**
   * OPM Uni Direction general relation type
   */
  public static final int UNI_DIRECTIONAL_RELATION = 205;

  /**
   * OPM Bi Direction general relation type
   */
  public static final int BI_DIRECTIONAL_RELATION = 206;

  /**
   * OPM Agent link type
   */
  public final static int AGENT_LINK = 305;

  /**
   * OPM Instrument link type
   */
  public final static int INSTRUMENT_LINK = 303;

  /**
   * OPM Result link type
   */
  public final static int RESULT_LINK = 306;

  /**
   * OPM Consumption link type
   */
  public final static int CONSUMPTION_LINK = 301;

  /**
   * OPM Effect link type
   */
  public final static int EFFECT_LINK = 302;

  /**
   * OPM Condition link type
   */
  public final static int CONDITION_LINK = 304;

  /**
   * OPM Invocation link type
   */
  public final static int INVOCATION_LINK = 307;

  /**
   * OPM Consumption Event link type
   */
  public final static int CONSUMPTION_EVENT_LINK = 310;

  /**
   * OPM Instrument Event link type
   */
  public final static int INSTRUMENT_EVENT_LINK = 308;

  /**
   * OPM Exception link type
   */
  public final static int EXCEPTION_LINK = 309;

  /**
   * Checks if given type is a structural relation
   * @param type one of the types above
   * @return true if given type is structural relation, false otherwise
   */
  public static boolean isRelation(int type) {
    return
        ((SPECIALIZATION_RELATION == type) ||
         (EXHIBITION_RELATION == type) ||
         (INSTANTINATION_RELATION == type) ||
         (AGGREGATION_RELATION == type) ||
         (UNI_DIRECTIONAL_RELATION == type) ||
         (BI_DIRECTIONAL_RELATION == type));
  }

  /**
   * Checks if given type is a procedural event link
   * @param type one of the types above
   * @return true if given type is procedural link, false otherwise
   */
  public static boolean isEventLink(int type) {
    return
        ((INVOCATION_LINK == type) ||
         (INSTRUMENT_EVENT_LINK == type) ||
         (EXCEPTION_LINK == type) ||
         (CONSUMPTION_EVENT_LINK == type));
  }

  /**
   * Checks if given type is a procedural link
   * @param type one of the types above
   * @return true if given type is procedural link, false otherwise
   */
  public static boolean isLink(int type) {
    return
        ((CONSUMPTION_LINK == type) ||
         (EFFECT_LINK == type) ||
         (INSTRUMENT_LINK == type) ||
         (CONDITION_LINK == type) ||
         (AGENT_LINK == type) ||
         (RESULT_LINK == type) ||
         (INVOCATION_LINK == type) ||
         (INSTRUMENT_EVENT_LINK == type) ||
         (EXCEPTION_LINK == type) ||
         (CONSUMPTION_EVENT_LINK == type));
  }

  /**
   *  North component border
   */
  public static final int N_BORDER = 1;

  /**
   *  North-east component corner
   */
  public static final int NE_BORDER = 2;

  /**
   *  North-west component corner
   */
  public static final int NW_BORDER = 3;

  /**
   *  South component border
   */
  public static final int S_BORDER = 4;

  /**
   *  South-east component corner
   */
  public static final int SE_BORDER = 5;

  /**
   *  South-west component corner
   */
  public static final int SW_BORDER = 6;

  /**
   *  West component border
   */
  public static final int W_BORDER = 7;

  /**
   *  East component border
   */
  public static final int E_BORDER = 8;

  /**
   *  Some point that is not in the border
   */
  public static final int NOT_IN_BORDER = 0;

}