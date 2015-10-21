package extensionTools.Testing;

import java.util.Timer;

/**
 * <p>Title: Extension Tools</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author OPCAT team
 * @version 1.0
 */

class TestingTimer extends Timer {

  private java.lang.Boolean pauseInd = new Boolean(false);
  TestingSystem aSys = null;

  public TestingTimer() {
  }

  public TestingTimer(TestingSystem aSys) {
    this.aSys = aSys;
    // schedule next step
    this.schedule(new TestingTask(aSys), 0, TestingSettings.getInstance().getSTEP_DURATION());
  }

  public void testingPause() {

    synchronized (this.pauseInd) {
      this.aSys.guiPanel.showTestingStatus(2,this.aSys.currentStep);
      this.pauseInd = new Boolean(true);
    }
  }

  public void testingContinue()  {
    synchronized (this.pauseInd) {
      if (this.isPause())  {
        this.aSys.guiPanel.showTestingStatus(1,this.aSys.currentStep);
        this.pauseInd = new Boolean(false);
      }
    }
  }

  public boolean isPause() {
    return this.pauseInd.booleanValue();
  }

}