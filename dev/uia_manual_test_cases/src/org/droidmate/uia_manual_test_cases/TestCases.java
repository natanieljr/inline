package org.droidmate.uia_manual_test_cases;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TestCases extends UiAutomatorTestCase
{
  // WISH !!! DUPLICATION WARNING !!!
  // With org.droidmate.common_android.Constants#uiaTestCaseTag
  protected static final String tag                   = "UiaTestCase";
  // With org.droidmate.utils.UiaTestCaseLogsProcessor#consumeExplorationAction
  protected static final String testCaseStartedPrefix = "Test case started. Name: ";
  protected static final String actionType_click      = "click";
  protected static final String actionType_enterText  = "enterText";
  // With <dm repo>\dev\droidmate\scripts\save_dm_uia_tc_logs.sh
  private static final   String testCaseFinished      = "Test case finished.";
  // end of duplication warning

  protected static final String diagTag = "UiaTestCaseDiag";

  private static List<TestCaseStep> readTestCaseStepsFromWindowDumps(String appPackageName, String testCaseName)
  {
    final List<TestCaseStep> testCaseSteps = new ArrayList<TestCaseStep>();
    final WindowDumpXmlParser parser = new WindowDumpXmlParser();
    for (File file : Environment.getDataDirectory().listFiles())
    {
      if (file.getName().startsWith(appPackageName) && file.getName().endsWith(".xml"))
      {
        for (TestCaseStep testCaseStep : parser.parse(file))
          if (testCaseStep.testCaseName.equals(testCaseName))
            testCaseSteps.add(testCaseStep);
      }
    }

    sortSteps(testCaseSteps);

    return testCaseSteps;
  }

  private static void sortSteps(List<TestCaseStep> testCaseSteps)
  {
    Collections.sort(testCaseSteps, new Comparator<TestCaseStep>()
    {
      @Override
      public int compare(TestCaseStep lhs, TestCaseStep rhs)
      {
        return Integer.valueOf(lhs.stepIndex).compareTo(rhs.stepIndex);
      }
    });
  }

  //region Launching app
  protected void launchApp(String appLaunchIconText, String appPackageName, boolean longLaunch)
  {
    UiObject app = navigateToAppLaunchIcon(appLaunchIconText);
    logAndClick(app, "", "Press the " + appLaunchIconText + " app icon to launch it.");
    if (longLaunch)
      this.sleep(10000);
    assertTrue(getPackageName().equals(appPackageName));
  }

  private UiObject navigateToAppLaunchIcon(String appLaunchIconName)
  {
    try
    {
      // Simulate a short press on the HOME button.
      this.getUiDevice().pressHome();

      // We're now in the home screen. Next, we want to simulate
      // a user bringing up the All Apps screen.
      // If you use the uiautomatorviewer tool to capture a snapshot
      // of the Home screen, notice that the All Apps button's
      // content-description property has the value "Apps".  We can
      // use this property to create a UiSelector to find the button.
      UiObject allAppsButton = new UiObject(new UiSelector().description("Apps"));

      // Simulate a click to bring up the All Apps screen.
      allAppsButton.clickAndWaitForNewWindow();

      // In the All Apps screen, the app launch icon is located in
      // the Apps tab. To simulate the user bringing up the Apps tab,
      // we create a UiSelector to find a tab with the text
      // label "Apps"
      UiObject appsTab = new UiObject(new UiSelector().text("Apps"));

      // Simulate a click to enter the Apps tab.
      appsTab.click();

      // Next, in the apps tabs, we can simulate a user swiping until
      // they come to the app launch icon. Since the container view
      // is scrollable, we can use a UiScrollable object.
      UiScrollable appViews = new UiScrollable(new UiSelector().scrollable(true));

      // Set the swiping mode to horizontal (the default is vertical)
      appViews.setAsHorizontalList();

      // Create a UiSelector to find the app launch icon and simulate
      // a user click to launch the app.
      return appViews.getChildByText(
        new UiSelector().className(android.widget.TextView.class.getName()),
        appLaunchIconName);
    } catch (UiObjectNotFoundException e)
    {
      throw new RuntimeException(e);
    }
  }

  private String getPackageName()
  {
    String packageName = this.getUiDevice().getCurrentPackageName();
    int getPackageNameAttemptsLeft = 10;
    while (packageName == null && getPackageNameAttemptsLeft > 0)
    {
      sleep(1000);
      packageName = this.getUiDevice().getCurrentPackageName();
      getPackageNameAttemptsLeft--;
    }
    if (packageName == null)
      throw new RuntimeException("Exhausted all attempts to obtain non-null package name");
    return packageName;
  }

  protected void logAndClick(UiObject uiObject, String resId, String comment)
  {
    logActionInfo(uiObject, actionType_click, resId, comment);

    try
    {
      uiObject.clickAndWaitForNewWindow();
    } catch (UiObjectNotFoundException e)
    {
      throw new RuntimeException(e);
    }

    sleepWait();
  }

  protected void sleepWait()
  {
    this.sleep(1000);
    this.getUiDevice().waitForIdle();
    this.sleep(1000);
  }

  protected void finishTestCase()
  {
    /*
     * The sleep is added here to ensure all the monitored API calls logs are output to logcat before the "testCaseFinished"
     * message is logged. It is necessary because logcat outputs messages at varying time intervals from the moment of
     * logging, so the messages might be output out of order with regards to the logging order. In particular, some API call log(s)
     * might be output after the final log "testCaseFinished" done in this method.
     */
    sleep(10 * 1000);
    Log.i(tag, testCaseFinished);
  }

  protected void executeTestCase(String appPackageName, String appLaunchIconText, String testCaseName, int testCaseStepsCount, boolean longLaunch)
  {
    Log.i(tag, testCaseStartedPrefix + "use-case:" + testCaseName);

    final List<TestCaseStep> testCaseSteps = readTestCaseStepsFromWindowDumps(appPackageName, testCaseName);

    System.out.println("Number of parsed test case steps: " + testCaseSteps.size());
    assertTrue(testCaseSteps.size() == testCaseStepsCount);

    logSpecsToStdout(testCaseSteps);

    launchApp(appLaunchIconText, appPackageName, longLaunch);
    conductTestCaseSteps(testCaseName, testCaseSteps);

    sleepWait();
    finishTestCase();
  }

  private void logSpecsToStdout(List<TestCaseStep> testCaseSteps)
  {
    for (TestCaseStep testCaseStep : testCaseSteps)
      System.out.println("Parsed spec: " + testCaseStep.sourceSpec);
  }

  private void conductTestCaseSteps(String testCaseName, List<TestCaseStep> testCaseSteps)
  {
    int stepsLeft = testCaseSteps.size();
    int currentStep = 1;

    while (stepsLeft > 0)
    {
      TestCaseStep step = findTestCaseStep(testCaseSteps, testCaseName, currentStep);

      logStepInfo(step);
      sleepWait();
      checkTestCaseStepUiObject(step);
      executeStep(step);

      currentStep++;
      stepsLeft--;
    }
  }

  private TestCaseStep findTestCaseStep(List<TestCaseStep> testCaseSteps, String testCaseName, int stepIndex)
  {
    for (TestCaseStep step : testCaseSteps)
      if (step.testCaseName.equals(testCaseName) && step.stepIndex == stepIndex)
        return step;

    throw new RuntimeException("Failed to find step with test case name of " + testCaseName + " and step index of " + stepIndex);
  }

  @TargetApi(Build.VERSION_CODES.CUPCAKE)
  private void checkTestCaseStepUiObject(TestCaseStep step)
  {
    final UiObject uiObject = getUiObject(step);

    if (step.wait)
      waitForUiObject(uiObject, step.toString());

    if (!uiObject.exists())
    {
      if (step.actionType.startsWith(TestCases.actionType_enterText))
        fail("The actual UI doesn't contain the object expected by a test case step. " +
          "The test case will now abort, as for the given test case step a text is to be entered, " +
          "thus the UI object has to be found. The test case step in question: " + step);
      else
        System.out.println("WARNING: The actual UI doesn't contain the object expected by a test case step. The step: " + step);
    }

  }

  private void waitForUiObject(UiObject uiObject, String testCaseStepString)
  {
    int waitInterval = 5000; // 5s
    int checksLeft = 60; // 60*5s = 5 min
    while (!uiObject.exists() && checksLeft > 0)
    {
      System.out.println("Waiting for UI object to click to appear. Wait time left in seconds: " + waitInterval / 1000 * checksLeft);
      sleep(waitInterval);
      checksLeft--;
    }

    if (!uiObject.exists())
    {
      fail("The actual UI doesn't contain the object expected by a test case step that was waiting for the object to appear. " +
        "The test case will now abort." + testCaseStepString);
    }
  }

  private UiObject getUiObject(TestCaseStep step)
  {
    if (step.view.resourceId.length() > 0)
      return new UiObject(
        new UiSelector()
          .resourceId(step.view.resourceId)
          .index(Integer.valueOf(step.view.index))
          .className(step.view.className)
          .clickable(Boolean.parseBoolean(step.view.clickable))
      );
    else
      return new UiObject(
        new UiSelector()
          .index(Integer.valueOf(step.view.index))
          .className(step.view.className)
          .clickable(Boolean.parseBoolean(step.view.clickable))
      );
  }

  // WISH !!! DUPLICATION WARNING !!!
  // With org.droidmate.common.logcat.UiaTestActionLogcatMessage#actionTypeCommentAndWidgetFrom
  @TargetApi(Build.VERSION_CODES.CUPCAKE)
  protected void logActionInfo(UiObject uiObject, String actionType, String resId, String comment)
  {
    try
    {
      Log.i(tag,
        "actionType: " + actionType +
          " comment: " + comment +
          " index: " + "-1" + // uiObject cannot be queried for index :(
          " text: " + uiObject.getText() +
          " resourceId: " + resId + // Provided as method param, as the uiObject cannot be queried for it :(
          " className: " + uiObject.getClassName() +
          " packageName: " + uiObject.getPackageName() +
          " contentDesc: " + uiObject.getContentDescription() +
          " checkable: " + uiObject.isCheckable() +
          " checked: " + uiObject.isChecked() +
          " clickable: " + uiObject.isClickable() +
          " enabled: " + uiObject.isEnabled() +
          " focusable: " + uiObject.isFocusable() +
          " focused: " + uiObject.isFocused() +
          " scrollable: " + uiObject.isScrollable() +
          " longClickable: " + uiObject.isLongClickable() +
          " password: " + "false" + // uiObject cannot be queried for password :(
          " selected: " + uiObject.isSelected() +
          " bounds: " + uiObject.getVisibleBounds().toShortString());
    } catch (UiObjectNotFoundException e)
    {
      throw new RuntimeException(e);
    }
  }

  // WISH !!! DUPLICATION WARNING !!!
  // With org.droidmate.common.logcat.UiaTestActionLogcatMessage#actionTypeCommentAndWidgetFrom
  @TargetApi(Build.VERSION_CODES.CUPCAKE)
  protected void logStepInfo(TestCaseStep step)
  {
    System.out.println("Executing test case step. Test case: " + step.testCaseName + " Step index: " + step.stepIndex + " Comment: " + step.comment);
    Log.i(tag,
      "actionType: " + step.actionType +
        " comment: " + step.comment +
        " index: " + step.view.index +
        " text: " + step.view.text +
        " resourceId: " + step.view.resourceId +
        " className: " + step.view.className +
        " packageName: " + step.view.packageName +
        " contentDesc: " + step.view.contentDesc +
        " checkable: " + step.view.checkable +
        " checked: " + step.view.checked +
        " clickable: " + step.view.clickable +
        " enabled: " + step.view.enabled +
        " focusable: " + step.view.focusable +
        " focused: " + step.view.focused +
        " scrollable: " + step.view.scrollable +
        " longClickable: " + step.view.longClickable +
        " password: " + step.view.password +
        " selected: " + step.view.selected +
        " bounds: " + step.view.boundsRect.toShortString());
  }

  private void executeStep(TestCaseStep step)
  {
    if (step.actionType.equals(TestCases.actionType_click))
    {
      click(step);

    } else if (step.actionType.startsWith(TestCases.actionType_enterText))
    {
      try
      {
        new UiObject(new UiSelector()
          .resourceId(step.view.resourceId)
          .className(step.view.className))
          .setText(step.actionPayload_textToEnter);
      } catch (UiObjectNotFoundException e)
      {
        throw new RuntimeException(e);
      }

    } else throw new AssertionError("Unknown action type of '" + step.actionType + "' in test case step of: " + step.toString());
  }

  private void click(TestCaseStep step)
  {
    boolean clickResult = this.getUiDevice().click(step.x, step.y);
    if (!clickResult)
      System.out.println("Button click failed! The comment of the relevant test case step: " + step.comment);
  }

  /**
   * This method is unused, but it is left in the code as a reference on how to complete the task described by it.
   */
  public void pressDoneOnNativeKeyboardNexus7_2014_vertical()
  {
    int x = 745;
    int y = 950;
    getUiDevice().click(x,y);

    // Does not work.
    // getUiDevice().pressKeyCode(KeyEvent.KEYCODE_ENTER);
    // Does not work.
    // getUiDevice().pressEnter();
    // See also: https://discuss.appium.io/t/how-to-handle-native-android-keyboard-using-appium/1764
  }
}
