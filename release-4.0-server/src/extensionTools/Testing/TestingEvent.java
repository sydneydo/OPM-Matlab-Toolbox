package extensionTools.Testing;

/*
  TestingEvent class is a storage of various properties relevant
  for a specific activation or termination.
 */

class TestingEvent {

  private long step = 0;  // the step when the event is scheduled
  private String path;    // path of the link that activated the element
  private long duration;  // duration of GUI effect
  private int numberOfInstances = 1; // number of instances to be activated/deactivated
  private int eventType = -1;  // 1 - activation; 0 - termination; -1 not defined
  private long schedulingStep = 0; // step when the event was issued
  private TestingEntry parentProcess = null;
  private long executionId = 0;
  private boolean waitForEvent = false;
  private int numberOfSpecializations = 0; // number of specializations
  private TestingEntry aEventLink = null;  // event link that activated the process
  private long cancelStep = 0;  // indicates whether the event was cancelled and when

 // if this indicator is true the system will try to activate the event until it will succeed
  private boolean recurringInd = false;

  public TestingEvent() {
    this.path = new String();
  }

  public TestingEvent(long step, String path, long duration, int numberOfInstances, long schedulingStep)  {
    this.step = step;
    this.path = path;
    this.duration = duration;
    this.numberOfInstances = numberOfInstances;
    this.schedulingStep = schedulingStep;
  }

  public TestingEvent(TestingEvent aEvent)  {
    this.step = aEvent.step;
    this.path = aEvent.path;
    this.duration = aEvent.duration;
    this.numberOfInstances = aEvent.numberOfInstances;
    this.schedulingStep = aEvent.schedulingStep;
    this.eventType = aEvent.eventType;
    this.recurringInd = aEvent.recurringInd;
    this.parentProcess = aEvent.parentProcess;
    this.executionId = aEvent.executionId;
    this.waitForEvent = aEvent.waitForEvent;
    this.numberOfSpecializations = aEvent.numberOfSpecializations;
    this.aEventLink = aEvent.aEventLink;
    this.cancelStep = aEvent.cancelStep;
  }

  public void setStep(long step) {
    this.step = step;
  }

  public long getStep() {
    return this.step;
  }

  public void setNumberOfInstances(int numberOfInstances)  {
    this.numberOfInstances = numberOfInstances;
  }

  public int getNumberOfInstances()  {
    return this.numberOfInstances;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }

  public long getDuration() {
    return this.duration;
  }

  public void setSchedulingStep(long step) {
    this.schedulingStep = step;
  }

  public long getSchedulingStep() {
    return this.schedulingStep;
  }

  public void setParentProcess(TestingEntry parentProcess) {
    this.parentProcess = parentProcess;
  }

  public TestingEntry getParentProcess() {
    return this.parentProcess;
  }

  public void setEventType(int type) {
    if (type == TestingSettings.getInstance().getACTIVATION_EVENT()) {
      this.eventType = TestingSettings.getInstance().getACTIVATION_EVENT();
    }
    else  {
      this.eventType = TestingSettings.getInstance().getTERMINATION_EVENT();
    }
  }

  public int getEventType() {
    return this.eventType;
  }

  public void setRecurringInd(boolean recurringInd) {
    this.recurringInd = recurringInd;
  }

  public boolean getRecurringInd() {
    return this.recurringInd;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getPath() {
    return this.path;
  }

  public void setExecutionId() {
    this.executionId = TestingSettings.getInstance().incLAST_EXECUTION_ID();
  }

  public void setExecutionId(long executionId) {
    this.executionId = executionId;
  }

  public long getExecutionId() {
    return this.executionId;
  }

  public void setWaitForEvent(boolean waitForEvent) {
    this.waitForEvent = waitForEvent;
  }

  public boolean getWaitForEvent() {
    return this.waitForEvent;
  }

  public void setNumberOfSpecializations(int n) {
    this.numberOfSpecializations = n;
  }

  public int getNumberOfSpecializations() {
    return this.numberOfSpecializations;
  }

  public void setEventLink(TestingEntry aEventLink) {
    this.aEventLink = aEventLink;
  }

  public TestingEntry getEventLink() {
    return this.aEventLink;
  }

  public void setCancelStep(long step) {
    this.cancelStep = step;
  }

  public long getCancelStep() {
    return this.cancelStep;
  }

}