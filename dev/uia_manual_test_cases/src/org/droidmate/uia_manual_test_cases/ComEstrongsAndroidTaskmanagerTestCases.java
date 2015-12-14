package org.droidmate.uia_manual_test_cases;

import com.android.uiautomator.core.UiObjectNotFoundException;

public class ComEstrongsAndroidTaskmanagerTestCases extends TestCases
{

  private final String appPackageName    = "com.estrongs.android.taskmanager";
  private final String appLaunchIconText = "ES Task Manager";

  /**
   * Prerequisites:
   * 1. The app package has to be cleared (reset) before running.
   *
   * @throws UiObjectNotFoundException
   */
  public void test_useCase_killTask() throws UiObjectNotFoundException
  {
    executeTestCase(appPackageName, appLaunchIconText, "killTask", 3, false);
  }

}
