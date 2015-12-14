package org.droidmate.myapplication2.app;

import android.support.test.uiautomator.*;
import android.test.InstrumentationTestCase;
import android.util.Log;
import org.junit.Test;

public class UiautomatorTest extends InstrumentationTestCase
{

  private UiDevice mDevice;

  public void testMy() {
    // Initialize UiDevice instance
    mDevice = UiDevice.getInstance(getInstrumentation());

    Log.w("XXX", "setting up UIA!");
      // Start from the home screen
      launchApp("barcoo", "de.barcoo.android", false);

  }


  //region Launching app
  protected void launchApp(String appLaunchIconText, String appPackageName, boolean longLaunch)
  {
    UiObject app = navigateToAppLaunchIcon(appLaunchIconText);
    logAndClick(app, "", "Press the " + appLaunchIconText + " app icon to launch it.");
    assertTrue(getPackageName().equals(appPackageName));
  }

  private UiObject navigateToAppLaunchIcon(String appLaunchIconName)
  {
    try
    {
      // Simulate a short press on the HOME button.
      mDevice.pressHome();

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
    String packageName = mDevice.getCurrentPackageName();
    int getPackageNameAttemptsLeft = 10;
    while (packageName == null && getPackageNameAttemptsLeft > 0)
    {
      try
      {
        Thread.sleep(1000);
      } catch (InterruptedException e)
      {
        e.printStackTrace();
      }
      packageName = mDevice.getCurrentPackageName();
      getPackageNameAttemptsLeft--;
    }
    if (packageName == null)
      throw new RuntimeException("Exhausted all attempts to obtain non-null package name");
    return packageName;
  }

  protected void logAndClick(UiObject uiObject, String resId, String comment)
  {
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
    try
    {
      Thread.sleep(1000);
    } catch (InterruptedException e)
    {
      e.printStackTrace();
    }
    mDevice.waitForIdle();
    try
    {
      Thread.sleep(1000);
    } catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }
}
