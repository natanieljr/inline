package org.droidmate.uia_manual_test_cases;

import com.android.uiautomator.core.UiObjectNotFoundException;

public class DeBarcooAndroidTestCases extends TestCases
{

  private final String appPackageName    = "de.barcoo.android";
  private final String appLaunchIconText = "barcoo";

  /**
   * Prerequisites:
   * 1. The app package has to be cleared (reset) before running.
   * 2. Wi-Fi has to be working.
   *
   * @throws UiObjectNotFoundException
   */
  public void test_useCase_searchForProduct() throws UiObjectNotFoundException
  {
    executeTestCase(appPackageName, appLaunchIconText, "searchForProduct", 5, false);
  }

}
