package extensionTools.Testing;

import util.Configuration;

/**
 * <p>
 * Title: Extension Tools
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class TestingSettings extends Object {

	private long STEP_DURATION = Integer.parseInt(Configuration.getInstance()
			.getProperty("simulation_STEP_DURATION"));

	private boolean FIXED_PROCESS_DURATION = Boolean.parseBoolean(Configuration
			.getInstance().getProperty("simulation_FIXED_PROCESS_DURATION"));

	private boolean MIN_TIME__PROCESS_DURATION = Boolean
			.parseBoolean(Configuration.getInstance().getProperty(
					"simulation_MIN_TIME__PROCESS_DURATION"));

	private int PROCESS_DURATION = Integer.parseInt(Configuration.getInstance()
			.getProperty("simulation_PROCESS_DURATION"));

	private int PROCESS_DURATION_RANGE_START = Integer.parseInt(Configuration
			.getInstance().getProperty(
					"simulation_PROCESS_DURATION_RANGE_START"));

	private int PROCESS_DURATION_RANGE_END = Integer
			.parseInt(Configuration.getInstance().getProperty(
					"simulation_PROCESS_DURATION_RANGE_END"));

	private boolean FIXED_REACTION_TIME = Boolean.parseBoolean(Configuration
			.getInstance().getProperty("simulation_FIXED_REACTION_TIME"));

	private int REACTION_TIME = Integer.parseInt(Configuration.getInstance()
			.getProperty("simulation_REACTION_TIME"));

	private int REACTION_TIME_RANGE_START = Integer.parseInt(Configuration
			.getInstance().getProperty("simulation_REACTION_TIME_RANGE_START"));

	private int REACTION_TIME_RANGE_END = Integer.parseInt(Configuration
			.getInstance().getProperty("simulation_REACTION_TIME_RANGE_END"));

	private boolean ONE_OBJECT_INSTANCE = Boolean.parseBoolean(Configuration
			.getInstance().getProperty("simulation_ONE_OBJECT_INSTANCE"));

	private int MAX_OBJECT_INSTANCES = 9999;
	private boolean AUTOMATIC_INITIATION = Boolean.parseBoolean(Configuration
			.getInstance().getProperty("simulation_AUTOMATIC_INITIATION"));

	private boolean MOVE_BETWEEN_OPD = Boolean.parseBoolean(Configuration
			.getInstance().getProperty("simulation_MOVE_BETWEEN_OPD"));

	private boolean STEP_BY_STEP_MODE = Boolean.parseBoolean(Configuration
			.getInstance().getProperty("simulation_STEP_BY_STEP_MODE"));

	private boolean RANDOM_STATE_SELECTION = Boolean.parseBoolean(Configuration
			.getInstance().getProperty("simulation_RANDOM_STATE_SELECTION"));

	private boolean SHOW_LIFE_SPAN = Boolean.parseBoolean(Configuration
			.getInstance().getProperty("simulation_SHOW_LIFE_SPAN"));

	private boolean SHOW_RESOURCE = Boolean.parseBoolean(Configuration
			.getInstance().getProperty("simulation_SHOW_RESOURCE"));

	private boolean SHOW_GRAPHICS = Boolean.parseBoolean(Configuration
			.getInstance().getProperty("simulation_SHOW_GRAPHICS"));

	private boolean STOP_AT_AGENT = Boolean.parseBoolean(Configuration
			.getInstance().getProperty("simulation_STOP_AT_AGENT"));

	private boolean RANDOM_PROCESS_DURATION = Boolean
			.parseBoolean(Configuration.getInstance().getProperty(
					"simulation_RANDOM_PROCESS_DURATION"));

	private final int ACTIVATION_EVENT = 1;
	private final int TERMINATION_EVENT = 0;
	private long LAST_EXECUTION_ID = 0;

	private static TestingSettings myInstance = null;

	private TestingSettings() {
	}

	public static TestingSettings getInstance() {
		if (myInstance == null) {
			myInstance = new TestingSettings();
		}

		return myInstance;
	}

	public void loadDefaultSettings() {
		myInstance = null ; 
		myInstance = new TestingSettings();
	}

	public void saveSettings() {
	}

	public long getSTEP_DURATION() {
		return STEP_DURATION;
	}

	public boolean isFIXED_PROCESS_DURATION() {
		return FIXED_PROCESS_DURATION;
	}

	public boolean isMIN_TIME__PROCESS_DURATION() {
		return MIN_TIME__PROCESS_DURATION;
	}

	public int getPROCESS_DURATION() {
		return PROCESS_DURATION;
	}

	public int getPROCESS_DURATION_RANGE_START() {
		return PROCESS_DURATION_RANGE_START;
	}

	public int getPROCESS_DURATION_RANGE_END() {
		return PROCESS_DURATION_RANGE_END;
	}

	public boolean isFIXED_REACTION_TIME() {
		return FIXED_REACTION_TIME;
	}

	public int getREACTION_TIME() {
		return REACTION_TIME;
	}

	public int getREACTION_TIME_RANGE_START() {
		return REACTION_TIME_RANGE_START;
	}

	public int getREACTION_TIME_RANGE_END() {
		return REACTION_TIME_RANGE_END;
	}

	public boolean isONE_OBJECT_INSTANCE() {
		return ONE_OBJECT_INSTANCE;
	}

	public int getMAX_OBJECT_INSTANCES() {
		return MAX_OBJECT_INSTANCES;
	}

	public boolean isAUTOMATIC_INITIATION() {
		return AUTOMATIC_INITIATION;
	}

	public boolean isMOVE_BETWEEN_OPD() {
		return MOVE_BETWEEN_OPD;
	}

	public boolean isSTEP_BY_STEP_MODE() {
		return STEP_BY_STEP_MODE;
	}

	public boolean isRANDOM_STATE_SELECTION() {
		return RANDOM_STATE_SELECTION;
	}

	public boolean isSHOW_LIFE_SPAN() {
		return SHOW_LIFE_SPAN;
	}

	public boolean isSHOW_RESOURCE() {
		return SHOW_RESOURCE;
	}

	public boolean isSHOW_GRAPHICS() {
		return SHOW_GRAPHICS;
	}

	public boolean isSTOP_AT_AGENT() {
		return STOP_AT_AGENT;
	}

	public int getACTIVATION_EVENT() {
		return ACTIVATION_EVENT;
	}

	public int getTERMINATION_EVENT() {
		return TERMINATION_EVENT;
	}

	public long getLAST_EXECUTION_ID() {
		return LAST_EXECUTION_ID;
	}

	public long incLAST_EXECUTION_ID() {
		return LAST_EXECUTION_ID++;
	}

	public boolean isRANDOM_PROCESS_DURATION() {
		return RANDOM_PROCESS_DURATION;
	}

	public void setSTEP_DURATION(long step_duration) {
		STEP_DURATION = step_duration;
	}

	public void setFIXED_PROCESS_DURATION(boolean fixed_process_duration) {
		FIXED_PROCESS_DURATION = fixed_process_duration;
	}

	public void setMIN_TIME__PROCESS_DURATION(boolean min_time__process_duration) {
		MIN_TIME__PROCESS_DURATION = min_time__process_duration;
	}

	public void setPROCESS_DURATION(int process_duration) {
		PROCESS_DURATION = process_duration;
	}

	public void setPROCESS_DURATION_RANGE_START(int process_duration_range_start) {
		PROCESS_DURATION_RANGE_START = process_duration_range_start;
	}

	public void setPROCESS_DURATION_RANGE_END(int process_duration_range_end) {
		PROCESS_DURATION_RANGE_END = process_duration_range_end;
	}

	public void setFIXED_REACTION_TIME(boolean fixed_reaction_time) {
		FIXED_REACTION_TIME = fixed_reaction_time;
	}

	public void setREACTION_TIME(int reaction_time) {
		REACTION_TIME = reaction_time;
	}

	public void setREACTION_TIME_RANGE_START(int reaction_time_range_start) {
		REACTION_TIME_RANGE_START = reaction_time_range_start;
	}

	public void setREACTION_TIME_RANGE_END(int reaction_time_range_end) {
		REACTION_TIME_RANGE_END = reaction_time_range_end;
	}

	public void setONE_OBJECT_INSTANCE(boolean one_object_instance) {
		ONE_OBJECT_INSTANCE = one_object_instance;
	}

	public void setMAX_OBJECT_INSTANCES(int max_object_instances) {
		MAX_OBJECT_INSTANCES = max_object_instances;
	}

	public void setAUTOMATIC_INITIATION(boolean automatic_initiation) {
		AUTOMATIC_INITIATION = automatic_initiation;
	}

	public void setMOVE_BETWEEN_OPD(boolean move_between_opd) {
		MOVE_BETWEEN_OPD = move_between_opd;
	}

	public void setSTEP_BY_STEP_MODE(boolean step_by_step_mode) {
		STEP_BY_STEP_MODE = step_by_step_mode;
	}

	public void setRANDOM_STATE_SELECTION(boolean random_state_selection) {
		RANDOM_STATE_SELECTION = random_state_selection;
	}

	public void setSHOW_LIFE_SPAN(boolean show_life_span) {
		SHOW_LIFE_SPAN = show_life_span;
	}

	public void setSHOW_RESOURCE(boolean show_resource) {
		SHOW_RESOURCE = show_resource;
	}

	public void setSHOW_GRAPHICS(boolean show_graphics) {
		SHOW_GRAPHICS = show_graphics;
	}

	public void setSTOP_AT_AGENT(boolean stop_at_agent) {
		STOP_AT_AGENT = stop_at_agent;
	}

	public void setLAST_EXECUTION_ID(long last_execution_id) {
		LAST_EXECUTION_ID = last_execution_id;
	}

	public void setRANDOM_PROCESS_DURATION(boolean random_process_duration) {
		RANDOM_PROCESS_DURATION = random_process_duration;
	}
}