package org.droidmate.uia_manual_test_cases;

import android.graphics.Rect;

public class ViewNodeAttributes
{
  public final String text;
  public final String resourceId;
  public final String className;
  public final String contentDesc;
  public final String checkable;
  public final String checked = "";
  public final String clickable;
  public final String enabled;
  public final String focusable;
  public final String focused = "";
  public final String scrollable;
  public final String longClickable;
  public final String selected = "";
  public final String password;
  public final Rect   boundsRect;
  public final String index;
  public final String packageName;

  public ViewNodeAttributes(String index, String text, String resourceId, String className, String packageName, String contentDesc, String checkable, String clickable, String enabled, String focusable, String scrollable, String longClickable, String password, Rect boundsRect)
  {
    this.index = index;
    this.text = text;
    this.resourceId = resourceId;
    this.className = className;
    this.packageName = packageName;
    this.contentDesc = contentDesc;
    this.checkable = checkable;
    this.clickable = clickable;
    this.enabled = enabled;
    this.focusable = focusable;
    this.scrollable = scrollable;
    this.longClickable = longClickable;
    this.password = password;
    this.boundsRect = boundsRect;
  }

  @Override
  public String toString()
  {
    return "ViewNodeAttributes{" +
      "boundsRect=" + boundsRect +
      ", checkable='" + checkable + '\'' +
      ", checked='" + checked + '\'' +
      ", className='" + className + '\'' +
      ", clickable='" + clickable + '\'' +
      ", contentDescription='" + contentDesc + '\'' +
      ", enabled='" + enabled + '\'' +
      ", focusable='" + focusable + '\'' +
      ", focused='" + focused + '\'' +
      ", index='" + index + '\'' +
      ", longClickable='" + longClickable + '\'' +
      ", packageName='" + packageName + '\'' +
      ", password='" + password + '\'' +
      ", resourceId='" + resourceId + '\'' +
      ", scrollable='" + scrollable + '\'' +
      ", selected='" + selected + '\'' +
      ", text='" + text + '\'' +
      '}';
  }
}
