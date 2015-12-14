package org.droidmate.uia_manual_test_cases;

import com.android.uiautomator.core.UiObjectNotFoundException;

public class ComPicsartStudioTestCases extends TestCases
{

  private final String appPackageName    = "com.picsart.studio";
  private final String appLaunchIconText = "PicsArt";

  /**
   * Prerequisites:
   * 1. The app package has to be cleared (reset) before running.
   * 2. A picture has to be available in gallery.
   *
   * @throws UiObjectNotFoundException
   */
  public void test_useCase_addEffect() throws UiObjectNotFoundException
  {
    executeTestCase(appPackageName, appLaunchIconText, "addEffect", 9, false);
  }

}
