package org.droidmate.uia_manual_test_cases;

import com.android.uiautomator.core.UiObjectNotFoundException;

public class ComRhmsoftFmTestCases extends TestCases
{

  private final String appPackageName    = "com.rhmsoft.fm";
  private final String appLaunchIconText = "File Manager";

  /**
   * Prerequisites:
   * 1. The app package has to be cleared (reset) before running.
   * 2. The "temp_utc" dir has to not exist inside the first dir displayed when the app launches. This is because this use case
   * will open up the first displayed dir and then attempt to create a dir in it named "temp_utc" and if it already exists,
   * it will fail, as the "OK" button confirming the dir creation will be disabled to prevent dir name collision.
   *
   * @throws UiObjectNotFoundException
   */
  public void test_useCase_viewAndCreateDir() throws UiObjectNotFoundException
  {
    executeTestCase(appPackageName, appLaunchIconText, "viewAndCreateDir", 5, false);
  }

}
