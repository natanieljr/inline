package org.droidmate.uia_manual_test_cases;

import com.android.uiautomator.core.UiObjectNotFoundException;

public class ComAdobeReaderTestCases extends TestCases
{

  private final String appPackageName    = "com.adobe.reader";
  private final String appLaunchIconText = "Adobe Reader";


  /**
   * Prerequisite: there is a .pdf document located in some subdir of /storage/emulated/
   * @throws UiObjectNotFoundException
   */
  public void test_useCase_viewDocument() throws UiObjectNotFoundException
  {
    executeTestCase(appPackageName, appLaunchIconText, "viewDocument", 4, true);
  }

}
