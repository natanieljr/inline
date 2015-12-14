package org.droidmate.uia_manual_test_cases;

import com.android.uiautomator.core.UiObjectNotFoundException;

public class ComEbayMobileTestCases extends TestCases
{

  private final String appPackageName    = "com.ebay.mobile";
  private final String appLaunchIconText = "eBay";

  /**
   * Prerequisites:
   * 1. The app package has to be cleared (reset) before running.
   * 2. An eBay account has to be setup, with following credentials:
   * Email: debugg7@gmail.com
   * Pass:  qwerfdsa1
   * 3. Wi-Fi has to be working.
   *
   * @throws UiObjectNotFoundException
   */
  public void test_useCase_findBySearch() throws UiObjectNotFoundException
  {
    executeTestCase(appPackageName, appLaunchIconText, "findBySearch", 8, false);
  }

}
