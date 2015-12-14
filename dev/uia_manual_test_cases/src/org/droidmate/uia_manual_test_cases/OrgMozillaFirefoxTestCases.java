package org.droidmate.uia_manual_test_cases;

import com.android.uiautomator.core.UiObjectNotFoundException;

public class OrgMozillaFirefoxTestCases extends TestCases
{

  private final String appPackageName    = "org.mozilla.firefox";
  private final String appLaunchIconText = "Firefox";

  /**
   * Prerequisites:
   * 1. The app package has to be cleared (reset) before running.
   * 2. Wi-Fi has to be working.
   *
   * @throws UiObjectNotFoundException
   */
  public void test_useCase_openUrl() throws UiObjectNotFoundException
  {
    executeTestCase(appPackageName, appLaunchIconText, "openUrl", 3, false);
  }

}
