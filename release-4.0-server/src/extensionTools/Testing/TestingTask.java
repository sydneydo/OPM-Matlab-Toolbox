package extensionTools.Testing;

import java.util.TimerTask;

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
 * @author OPCAT team
 * @version 1.0
 */

class TestingTask extends TimerTask {
    TestingSystem aSys;

    public TestingTask() {
    }

    public TestingTask(TestingSystem aSys) {
	this.aSys = aSys;
    }

    public void run() {
	if (this.aSys.currentStep == 0) {

	    this.aSys.firstStep();
	    // set pause to enter into step by step mode
	    if (TestingSettings.getInstance().isSTEP_BY_STEP_MODE()) {
		this.aSys.testingPause();
		// schedule pause after 1 step
		/*
                 * aSys.testingTimer.schedule(new TimerTask() {public void run() {
                 * aSys.testingPause();}}, TestingSettings.STEP_DURATION);
                 */
	    }
	} else {
	    this.aSys.nextStep();
	}

    }
}