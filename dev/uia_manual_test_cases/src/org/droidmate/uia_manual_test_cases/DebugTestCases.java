package org.droidmate.uia_manual_test_cases;

import android.view.KeyEvent;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;

public class DebugTestCases extends TestCases
{

  public void test_useCase_debug() throws UiObjectNotFoundException
  {
//    final UiObject uiObject = new UiObject(
//      new UiSelector()
//        .resourceId("com.estrongs.android.taskmanager:id/taskman_list_item_button")
//        .index(3)
//        .className("android.widget.ImageView")
//        .clickable(true)
//    );

//    System.out.println("Exists: "+uiObject.exists());

    UiObject uiObject = new UiObject(
      new UiSelector().resourceId("org.mozilla.firefox:id/url_bar_title").className("android.widget.TextView").index(1)
      //new UiSelector().text("Enter Search or Address")
    );
    System.out.println("DEB exists: " + uiObject.exists());
    if (uiObject.exists())
    {
      uiObject.click();
    } else
    {
      uiObject = new UiObject(
        new UiSelector().resourceId("org.mozilla.firefox:id/url_edit_text")
      );
      if (uiObject.exists())
        uiObject.setText("Abc");
      else
        System.out.println("DOESN'T!");
    }


    //.setText("ha");
      //pressDoneOnNativeKeyboardNexus7_2014_vertical();

  }



}
