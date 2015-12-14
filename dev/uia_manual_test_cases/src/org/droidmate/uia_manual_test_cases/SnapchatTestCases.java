package org.droidmate.uia_manual_test_cases;

import android.util.Log;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;

/*
 Official Snapchat use cases:
 https://support.snapchat.com/ca/howto

 Snaps: create, view, etc.
 https://support.snapchat.com/ca/snaps

 Stories: post, view, delete and save, etc.
 https://support.snapchat.com/ca/stories

 -----

 Some unofficial Snapchat use cases:
 http://mashable.com/2014/08/04/snapchat-for-beginners/
 http://www.wikihow.com/Use-Snapchat
*/
public class SnapchatTestCases extends TestCases
{
  private static final String actionType_longClick  = "longClick";
  private static final String actionType_pressBack  = "press back";
  // end of duplication warning

  private final String appPackageName    = "com.snapchat.android";
  private final String appLaunchIconText = "Snapchat";
  private final String userName          = "debugg7";
  private final String password          = "qwer////";

  // Based on: https://support.snapchat.com/a/create
  public void test_useCase_takeSnap() throws UiObjectNotFoundException
  {
    Log.i(tag, testCaseStartedPrefix + "use-case:take_snap");

    launchApp(appLaunchIconText, appPackageName, false);
    logIn(userName, password);

    clickButtonWithResId("com.snapchat.android:id/camera_take_snap_button", "Click the 'take snap' button in the lower middle to make a picture with a camera.");

    clickButtonWithResId("com.snapchat.android:id/home_layout_container", "Click the screen (displaying the taken snap) to make the caption edit field appear.");
    setText(" What a beautiful office ceiling", "com.snapchat.android:id/picture_caption", "Add a caption to the taken snap.");
    logAndPressBack();

    clickButtonWithResId("com.snapchat.android:id/display_time", "Click the stopwatch in the lower left corner to set time.");
    // We have to offset click by going down by 100 pixels to select different time that displays in the center.
    clickButtonWithResId("com.snapchat.android:id/time_picker_container", "Set snap retention time of 4 seconds (instead of default 3) by clicking in lower part of the time spinner menu, being displayed at the bottom.", 0, 100);
    logAndPressBack();

    openScreenSendTo();
    checkMyselfAsFriend();
    sendSnap();

    longClickButtonWithText("Press and hold to view", "com.snapchat.android:id/status", "View the snap that just arrived (because I sent it to myself) by clicking on its status in its entry in the snap feed list.");

    finishTestCase();
  }

  // Based on: https://support.snapchat.com/a/create
  public void test_useCase_takeVideoSnap() throws UiObjectNotFoundException
  {
    Log.i(tag, testCaseStartedPrefix + "use-case:take_video_snap");

    launchApp(appLaunchIconText, appPackageName, false);
    logIn(userName, password);

    longClickButtonWithResId("com.snapchat.android:id/camera_take_snap_button", "Click and hold the 'take snap' button in the lower middle to make a video with a camera.");

    clickButtonWithResId("com.snapchat.android:id/drawing_btn", "Click the 'pencil' button in the upper right corner to start drawing on the device.");

    waitForObject(new UiObject(resId("com.snapchat.android:id/color_picker_container_center")));
    clickButtonWithResId("com.snapchat.android:id/color_picker_container_center", "Pick a color by clicking in the center of the color palette that just appeared in the upper right corner.");

    swipe("com.snapchat.android:id/video", "Draw a line across the screen (displaying the taken video snap).");

    clickButtonWithResId("com.snapchat.android:id/picture_save_pic", "Save the video to gallery by clicking the 'save' button in lower left.");

    clickButtonWithResId("com.snapchat.android:id/story_button", "Click the 'add to story' button in lower left.");
    clickButtonByBoth("Add", "android:id/button1", "Click 'add' in the pop-up box to confirm I want to add the snap to my story.");

    finishTestCase();
  }

  // Based on: https://support.snapchat.com/a/find-friends
  public void test_useCase_findFriends() throws UiObjectNotFoundException
  {
    Log.i(tag, testCaseStartedPrefix + "use-case:find_friends");

    launchApp(appLaunchIconText, appPackageName, false);
    logIn(userName, password);

    // click on the "menu" button in the lower right of the camera view
    openScreenMyFriends();

    // click on the "plus friend" button in the upper right
    openScreenAddFriends();

    // click on the "contacts list" icon (notebook with a person)
    openTabContactList();

    // workaround for uiautomator bug. Without re-entering the screen, the "Allow Access" UiObject cannot be found.
    logAndPressBack();
    openScreenAddFriends();

    // click on the "allow access" button to allow access to contacts
    allowAccessToContacts();

    finishTestCase();
  }

  public void test_useCase_editFriend() throws UiObjectNotFoundException
  {
    Log.i(tag, testCaseStartedPrefix + "use-case:edit_friend");

    launchApp(appLaunchIconText, appPackageName, false);
    logIn(userName, password);

    // click on the "menu" button in the lower right of the camera view
    openScreenMyFriends();

    clickButtonWithResId("com.snapchat.android:id/myfriends_action_bar_search_button", "Click the search button in upper right to search for friends.");
    searchForFriend_abc();

    clickButtonWithResId("com.snapchat.android:id/name", "Click on the displayed 'abc' name.");
    clickSettingsCogwheel(720, 182, "Click on the cogwheel next to the friend name to display a menu allowing to add him.");
    clickButton("Add friend", "android:id/text1", "Click the 'Add friend' button in the settings popup.");

    clickButtonByBoth("abc", "com.snapchat.android:id/name", "Click on the displayed 'abc' name.");
    clickSettingsCogwheel(720, 267, "Click on the cogwheel next to the friend name to display a menu allowing to block him.");
    clickButton("Block", "android:id/text1", "Click the 'Block' button in the settings popup.");

    clickSettingsCogwheel(720, 267, "Click on the cogwheel next to the friend name to display a menu allowing to unblock him.");
    clickButton("Unblock", "android:id/text1", "Click the 'Unblock' button in the settings popup.");

    clickSettingsCogwheel(720, 267, "Click on the cogwheel next to the friend name to display a menu allowing to delete him.");
    clickButton("Delete", "android:id/text1", "Click the 'Delete' button in the settings popup.");

    finishTestCase();
  }

  // Test case for debugging using manual GUI interaction. Run with IntelliJ's "build_push_run.sh debug" config and then manually click on the GUI.
  public void test_useCase_debug() throws UiObjectNotFoundException
  {
  }

  private void clickSettingsCogwheel(int settingsCogwheelCenterX, int settingsCogwheelCenterY, String comment) throws UiObjectNotFoundException
  {
    logActionInfo(actionType_click, "com.snapchat.android:id/friend_profile_settings_button", comment);
    this.getUiDevice().click(settingsCogwheelCenterX, settingsCogwheelCenterY);
    sleepWait();
  }

  private void searchForFriend_abc() throws UiObjectNotFoundException
  {
    String text = "abc";
    String resId = "com.snapchat.android:id/my_friends_search_bar";
    UiObject textField = new UiObject(resId(resId));
    String comment = "Search for 'abc' by entering text into the auto-selected text field that appeared in upper left.";
    logActionInfo(textField, actionType_enterText + "[" + text + "]", resId, comment);
    // This is a workaround for uiObject.setText(text);, because it actually doesn't work for reasons unknown.
    enter_abc();
    sleepWait();
  }

  private void enter_abc()
  {
    // Hard-coded coordinates for Nexus 7 2012, vertical layout, Android 4.4.4, keyboard display
    sleep(100);
    this.getUiDevice().click(71, 950); // letter "a"
    sleep(100);
    this.getUiDevice().click(401, 1051); // letter "b"
    sleep(100);
    this.getUiDevice().click(256, 1051); // letter "c"
  }

  //region Snapchat-specific

  //region first required by the sendSnap use case
  private void logIn(String userName, String password) throws UiObjectNotFoundException
  {
    clickButton("LOG IN", "com.snapchat.android:id/landing_page_login_button", "Click the 'LOG IN' button on the landing page (first screen).");
    sleep(500);
    setText(userName, "com.snapchat.android:id/login_username_email", "Enter the user name.");
    sleep(500);
    setText(password, "com.snapchat.android:id/login_password", "Enter the password.");
    sleep(500);
    clickButton("LOG IN", "com.snapchat.android:id/login_button", "Click the 'LOG IN' button to actually log into the app.");
    sleepWait();
  }

  private void openScreenSendTo() throws UiObjectNotFoundException
  {
    clickButtonWithResId("com.snapchat.android:id/picture_send_pic", "Click the right arrow in the bottom right to show a list of snap recipients.");
  }


  private void checkMyselfAsFriend() throws UiObjectNotFoundException
  {
    String anchorObjectText = "(me)";
    final String resId = "com.snapchat.android:id/checkbox";
    final UiObject meCheckbox = getUiObjectWithResIdBesideUiObjectWithText(anchorObjectText, resId);
    logAndClick(meCheckbox, resId, "Check-mark myself as the sole snap recipient.");
  }

  private void checkMyStory() throws UiObjectNotFoundException
  {
    String anchorObjectText = "My story";
    final String resId = "com.snapchat.android:id/checkbox";
    final UiObject meCheckbox = getUiObjectWithResIdBesideUiObjectWithText(anchorObjectText, resId);
    logAndClick(meCheckbox, resId, "Check-mark 'My story' as the video snap recipient.");
  }

  private UiObject getUiObjectWithResIdBesideUiObjectWithText(String entryText, String resId) throws UiObjectNotFoundException
  {
    final UiObject meLabel = new UiObject(text(entryText));
    return meLabel.getFromParent(resId(resId));
  }

  private void sendSnap() throws UiObjectNotFoundException
  {
    final String id = "com.snapchat.android:id/send_to_bottom_panel_send_button";
    final UiObject sendPic = new UiObject(resId(id));
    //final UiObject sendPic = new UiObject(resId(id).childSelector(clickable()));
    logAndClick(sendPic, id, "Send the snap by clicking the right arrow in the lower right of the screen.");
  }
  //endregion

  //region first required by the addFriends use case

  private void openScreenMyFriends() throws UiObjectNotFoundException
  {
    clickButtonWithResId("com.snapchat.android:id/camera_my_friends_button", "Open the 'my friends' list by clicking the 'menu' button in the lower right corner.");
  }

  private void openScreenAddFriends() throws UiObjectNotFoundException
  {
    clickButtonWithResId("com.snapchat.android:id/myfriends_action_bar_friend_button_image", "Click the 'add friend' button in the upper right corner.");
  }

  private void openTabContactList() throws UiObjectNotFoundException
  {
    clickButtonWithResIdAndInstance("com.snapchat.android:id/add_friend_tab_icon", 1, "Show the 'contacts list' tab by clicking on the 'contacts notebook' tab icon, in the upper right part of the screen.");
  }

  private void allowAccessToContacts() throws UiObjectNotFoundException
  {
    clickButtonWithResId("com.snapchat.android:id/contacts_permission_button", "Allow access to contacts list by clicking the 'Allow Access' button.");
  }

  //endregion

  //endregion

  private void setText(String text, String resId, String comment) throws UiObjectNotFoundException
  {
    final UiObject textField = new UiObject(resId(resId));
    logAndSetText(textField, text, resId, comment);
  }

  private void clickButton(String buttonText, String resId, String comment) throws UiObjectNotFoundException
  {
    final UiObject button = new UiObject(text(buttonText));
    logAndClick(button, resId, comment);
  }

  private void clickButtonByBoth(String buttonText, String resId, String comment) throws UiObjectNotFoundException
  {
    final UiObject button = new UiObject(text(buttonText).resourceId(resId));
    logAndClick(button, resId, comment);
  }

  private void clickButtonWithResId(String buttonResId, String comment) throws UiObjectNotFoundException
  {
    final UiObject button = new UiObject(resId(buttonResId));
    logAndClick(button, buttonResId, comment);
  }


  private void clickButtonWithResId(String buttonResId, String comment, int xCenterOffset, int yCenterOffset) throws UiObjectNotFoundException
  {
    final UiObject button = new UiObject(resId(buttonResId));
    int x = button.getVisibleBounds().centerX();
    int y = button.getVisibleBounds().centerY();
    logAndClick(button, buttonResId, comment, x + xCenterOffset, y + yCenterOffset);
  }


  private void longClickButtonWithResId(String buttonResId, String comment) throws UiObjectNotFoundException
  {
    final UiObject button = new UiObject(resId(buttonResId));
    int x = button.getVisibleBounds().centerX();
    int y = button.getVisibleBounds().centerY();
    logAndLongClick(button, buttonResId, comment, x, y, x, y, true);
  }

  private void longClickButtonWithText(String buttonText, String buttonResId, String comment) throws UiObjectNotFoundException
  {
    final UiObject button = new UiObject(text(buttonText));
    int x = button.getVisibleBounds().centerX();
    int y = button.getVisibleBounds().centerY();
    logAndLongClick(button, buttonResId, comment, x, y, x, y, true);
  }

  private void swipe(String resId, String comment) throws UiObjectNotFoundException
  {
    final UiObject uiObject = new UiObject(resId(resId));
    int x = uiObject.getVisibleBounds().centerX();
    int y = uiObject.getVisibleBounds().centerY();
    logAndLongClick(uiObject, resId, comment, x - 100, y - 100, x + 100, y + 100, false);
  }

  private void clickButtonWithResIdAndInstance(String buttonResId, int instance, String comment) throws UiObjectNotFoundException
  {
    final UiObject button = new UiObject(resId(buttonResId).instance(instance));
    logAndClick(button, buttonResId, comment);
  }

  private UiSelector clickable()
  {
    return new UiSelector().clickable(true);
  }

  private UiSelector resId(String id)
  {
    return new UiSelector().resourceId(id);
  }

  private UiSelector text(String text)
  {
    return new UiSelector().textContains(text);
  }

  private void logAndClick(UiObject uiObject, String resId, String comment, int x, int y) throws UiObjectNotFoundException
  {
    logActionInfo(uiObject, actionType_click, resId, comment);
    boolean clickResult = this.getUiDevice().click(x, y);
    if (!clickResult)
      Log.w(diagTag, "Button click failed! Comment: " + comment);
    sleepWait();
  }


  private void logAndLongClick(UiObject uiObject, String resId, String comment, int start_x, int start_y, int end_x, int end_y, boolean sleepWait) throws UiObjectNotFoundException
  {
    logActionInfo(uiObject, actionType_longClick, resId, comment);
    getUiDevice().swipe(start_x, start_y, end_x, end_y, 100); // 100 ~ 2s. Empirical evaluation.
    if (sleepWait)
      sleepWait();
  }

  private void logAndSetText(UiObject uiObject, String text, String resId, String comment) throws UiObjectNotFoundException
  {
    // WISH !!! DUPLICATION WARNING !!!
    // With org.droidmate.utils.UiaTestCaseLogsProcessor it has hardcoded parsing of this format.
    logActionInfo(uiObject, actionType_enterText + "[" + text + "]", resId, comment);
    uiObject.setText(text);
    sleepWait();
  }

  private void logAndPressBack()
  {
    Log.i(tag, "actionType: " + actionType_pressBack + " comment: Press the 'back' button.");
    this.getUiDevice().pressBack();
    sleepWait();
  }

  private void logActionInfo(String actionType, String resId, String comment) throws UiObjectNotFoundException
  {
    Log.i(tag,
      "actionType: " + actionType +
        " comment: " + comment +
        " index: " + "-1" +
        " text: " + "unknown" +
        " resourceId: " + resId +
        " className: " + "unknown" +
        " packageName: " + "unknown" +
        " contentDesc: " + "unknown" +
        " checkable: " + "false" +
        " checked: " + "false" +
        " clickable: " + "false" +
        " enabled: " + "true" +
        " focusable: " + "false" +
        " focused: " + "false" +
        " scrollable: " + "false" +
        " longClickable: " + "false" +
        " password: " + "false" +
        " selected: " + "unknown" +
        " bounds: " + "[0,0][0,0]");
  }

  @SuppressWarnings("UnusedDeclaration") // Method for debugging
  private void waitForObject(UiObject uiObject)
  {
    boolean objectAvailable = false;
    int attemptsLeft = 10;
    while (!objectAvailable && attemptsLeft > 0)
    {
      attemptsLeft--;
      try
      {
        uiObject.getText();
        objectAvailable = true;

      } catch (UiObjectNotFoundException e)
      {
        if (attemptsLeft > 0)
        {
          Log.d(diagTag, "Waiting for UiObject. Attempts left: " + attemptsLeft);
          this.sleep(3000);
        } else
        {
          Log.d(diagTag, "All attempts to get UiObject exhausted.");
        }
      }
    }
    if (!objectAvailable)
      throw new RuntimeException("Exhausted all attempts to make ui object available.");
  }

  @SuppressWarnings("UnusedDeclaration") // Method for debugging
  private void tryGetText(UiSelector selector, String msg)
  {
    try
    {
      Log.w(diagTag, msg);
      new UiObject(selector).getText();
      Log.w(diagTag, "success");

    } catch (UiObjectNotFoundException e)
    {
      Log.w(diagTag, "failure");
      Log.w(diagTag, e);
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }
  //endregion

}
