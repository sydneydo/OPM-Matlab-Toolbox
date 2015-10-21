package extensionTools.Testing;


/**
 * <p>Title: Extension Tools</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public interface Testing {
  public void testingStart();
  public void testingStop();
  public void testingActivate();
  public void testingDeactivate();
  public void testingForward(long numberOfSteps);
  public void testingBackward(long numberOfSteps);
  public void testingPause();
  public void testingContinue();
  public boolean isTestingPaused();
  public boolean canFarward();
  public boolean canBackward();

}