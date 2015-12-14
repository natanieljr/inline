package org.droidmate.uia_manual_test_cases;

import com.android.uiautomator.core.UiObjectNotFoundException;

public class ComCleanmasterSecurityTestCases extends TestCases
{

  private final String appPackageName    = "com.cleanmaster.security";
  private final String appLaunchIconText = "CM Security";

  /**
   * Prerequisite 1: The app package has to be cleared (reset) before running.
   * Note that the app might crash on first launch, but should work on second one. This means you have to:
   *
   * 1. reset the app:
   *   reset.sh com.cleanmaster.security
   *
   * 2. try to launch it, see it crash.
   *
   * 3. run the test case without resetting, but with force-stopping:
   *   build_push_fstop_run.sh ComCleanmasterSecurity scanReport com.cleanmaster.security
   *
   * Prerequisite 2: A "com.antivirus" app has to be installed, possibly it has to be inlined, not sure. The app is needed because
   * the use case assumes that as a result of the scan, this app will be considered suspicious and will be reported.
   * @throws UiObjectNotFoundException
   */
  public void test_useCase_scanReport() throws UiObjectNotFoundException
  {
    executeTestCase(appPackageName, appLaunchIconText, "scanReport", 5, true);
  }

}
