package org.droidmate.uia_manual_test_cases;

import com.android.uiautomator.core.UiObjectNotFoundException;

public class ComAntivirusTestCases extends TestCases
{

  private final String appPackageName    = "com.antivirus";
  private final String appLaunchIconText = "AntiVirus";

  public void test_useCase_scan() throws UiObjectNotFoundException
  {
    executeTestCase(appPackageName, appLaunchIconText, "scan", 3, true);
  }

}
