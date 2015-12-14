package org.droidmate.uieventstologcat;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;


/**
 * <p>
 * Listens to {@link AccessibilityEvent}s corresponding to user actions of clicking or entering text.
 *
 * </p><p>
 * The service represented by this class is to be deployed on a device being used for manual exploration.
 * The goal of manual exploration is to gather for further processing timestamps of called APIs and timestamps of user actions,
 * both output to logcat during the exploration. This service outputs the logcat logs with timestamps of user actions,
 * while the logcat logs with called APIs timestamps are provided by the monitor inlined to the explored apk.
 *
 * </p><p>
 * The class body is adapted from {@code https://gist.github.com/qihnus/1909616}.
 *
 * </p>
 */
public class UIEventsToLogcatOutputter extends AccessibilityService
{
  // !!! DUPLICATION WARNING !!!
  // Duplicate: org.droidmate.common_android.Constants#uiEventTag
  static final String tag = "UIEventsToLogcat";


  private String getEventType(AccessibilityEvent event)
  {
    switch (event.getEventType())
    {
      case AccessibilityEvent.TYPE_VIEW_CLICKED:
        return "TYPE_VIEW_CLICKED";
      case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
        return "TYPE_VIEW_LONG_CLICKED";
      case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
        return "TYPE_VIEW_TEXT_CHANGED";
      default:
        return "unknown:" + event.getEventType();
    }
  }

  private String getEventText(AccessibilityEvent event)
  {
    StringBuilder sb = new StringBuilder();
    for (CharSequence s : event.getText())
    {
      sb.append(s);
    }
    return sb.toString();
  }

  @Override
  public void onAccessibilityEvent(AccessibilityEvent event)
  {
    if (!(getEventType(event).startsWith("unknown")))
    {
      assert event.getSource() == null;
      Log.i(tag, String.format(
        "text: %s class: %s package: %s type: %s enabled: %s scrollable: %s content-desc: %s password: %s",
        getEventText(event),
        event.getClassName(),
        event.getPackageName(),
        getEventType(event),
        event.isEnabled(),
        event.isScrollable(),
        event.getContentDescription(),
        event.isPassword()
      ));
    }
  }

  @Override
  public void onInterrupt()
  {
    Log.v(tag, "onInterrupt");
  }

  @Override
  protected void onServiceConnected()
  {
    super.onServiceConnected();
    Log.v(tag, "onServiceConnected");
    AccessibilityServiceInfo info = new AccessibilityServiceInfo();
    info.flags = AccessibilityServiceInfo.DEFAULT;
    info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
    info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
    setServiceInfo(info);
  }

}
