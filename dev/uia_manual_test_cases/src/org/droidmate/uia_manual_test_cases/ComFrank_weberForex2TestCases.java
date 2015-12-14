package org.droidmate.uia_manual_test_cases;

import com.android.uiautomator.core.UiObjectNotFoundException;

public class ComFrank_weberForex2TestCases extends TestCases
{

  private final String appPackageName    = "com.frank_weber.forex2";
  private final String appLaunchIconText = "currency converter";

  /**
   * Prerequisites:
   * 1. The app package has to be cleared (reset) before running.
   *
   * @throws UiObjectNotFoundException
   */
  public void test_useCase_convertCurrency() throws UiObjectNotFoundException
  {
    executeTestCase(appPackageName, appLaunchIconText, "convertCurrency", 5, false);
  }

}
