package gui.opmEntities;

/**
 * This class represents State in OPM. For better understanding of this class
 * you should be familiar with OPM.
 * 
 * @version 2.0
 * @author Stanislav Obydionnov
 * @author Yevgeny Yaroker
 */

public class OpmState extends OpmConnectionEdge {
    // ---------------------------------------------------------------------------
    // The private attributes/members are located here
    private String minTime, maxTime;

    boolean initialState, finalState, defaultState;

    public final static String EXISTENT_STATE_NAME = "Exsistent";

    public final static String NONEXISTENT_STATE_NAME = "Non-Exsistent";

    /**
         * Creates an OpmState with specified id and name. Id of created
         * OpmState must be unique in OPCAT system. Other data members of
         * OpmState get default values.
         * 
         * @param id
         *                OpmState id
         * @param name
         *                OpmState name
         */

    public OpmState(long stateId, String stateName) {
	super(stateId, stateName);
	this.minTime = new String("0;0;0;0;0;0;0");
	this.maxTime = "infinity";
	this.setDescription("None");
	this.initialState = false;
	this.finalState = false;
	this.defaultState = false;
    }

    public void copyPropsFrom(OpmState origin) {
	super.copyPropsFrom(origin);
	this.minTime = origin.getMinTime();
	this.maxTime = origin.getMaxTime();
	this.initialState = origin.isInitial();
	this.finalState = origin.isFinal();
	this.defaultState = origin.isDefault();
    }

    // ---------------------------------------------------------------------------
    // The public methods are located here

    /**
         * Returns String representing minimum activation time of this OpmState.
         * This String contains non-negative integer X 7 (msec, sec, min, hours,
         * days, months, years) with semi-colons separation.
         * 
         */
    public String getMinTime() {
	return this.minTime;
    }

    /**
         * Sets minimum activation time of this OpmState. This field contains
         * non-negative integer X 7 (msec, sec, min, hours, days, months, years)
         * with semi-colons separation.
         * 
         */
    public void setMinTime(String minTime) {
	this.minTime = minTime;
    }

    /**
         * Returns String representing maximum activation time of this OpmState.
         * This String contains non-negative integer X 7 (msec, sec, min, hours,
         * days, months, years) with semi-colons separation.
         * 
         */
    public String getMaxTime() {
	return this.maxTime;
    }

    /**
         * Sets maximum activation time of this OpmState. This field contains
         * non-negative integer X 7 (msec, sec, min, hours, days, months, years)
         * with semi-colons separation.
         * 
         */
    public void setMaxTime(String maxTime) {
	this.maxTime = maxTime;
    }

    /**
         * Sets the initialState property of OpmState.
         * 
         */
    public void setInitial(boolean initialState) {
	this.initialState = initialState;
    }

    /**
         * Returns true if this State is initial.
         * 
         */
    public boolean isInitial() {
	return this.initialState;
    }

    /**
         * Sets the finalState property of OpmState.
         * 
         */
    public void setFinal(boolean finalState) {
	this.finalState = finalState;
    }

    /**
         * Returns true if this State is final.
         * 
         */
    public boolean isFinal() {
	return this.finalState;
    }

    public boolean isDefault() {
	return this.defaultState;
    }

    public void setDefault(boolean defaultState) {
	this.defaultState = defaultState;
    }

}