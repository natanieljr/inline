package org.droidmate.uia_manual_test_cases;

import com.android.uiautomator.core.UiObjectNotFoundException;

public class ComIndeedAndroidJobsearchTestCases extends TestCases
{

  private final String appPackageName    = "com.indeed.android.jobsearch";
  private final String appLaunchIconText = "Job Search";

  /**
   * Prerequisites:
   * 1. The app package has to be cleared (reset) before running.
   * 2. Wi-Fi has to be working.
   *
   * @throws UiObjectNotFoundException
   */
  public void test_useCase_searchForJob() throws UiObjectNotFoundException
  {
    executeTestCase(appPackageName, appLaunchIconText, "searchForJob", 9, false);
  }

}
