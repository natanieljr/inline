package org.droidmate.uia_manual_test_cases;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build;
import android.util.Pair;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.Assert.assertTrue;

public class TestCaseStep
{
  static String actionTypeSpec_click                     = "c";
  static String actionTypeSpec_clickOnCoordsPrefix       = "c[";
  static String actionTypeSpec_wait_and_click            = "wait_c";
  static String actionTypeSpec_wait_and_enterText_prefix = "wait_txt:";
  static String actionTypeSpec_enterTextPrefix           = "txt:";

  public final String sourceSpec;


  public String  testCaseName;
  public int     stepIndex;
  public String  actionType;
  final  boolean wait;
  public String  comment;
  public int     x;
  public int     y;

  public ViewNodeAttributes view;

  public String actionPayload_textToEnter = null;

  public TestCaseStep(String spec, String index, String text, String resourceId, String className, String packageName, String contentDesc, String checkable, String clickable, String enabled, String focusable, String scrollable, String longClickable, String password, String bounds)
  {
    this.sourceSpec = spec;

    final String[] droidmateAttrProps = spec.split("\\|");

    assertTrue("droidmateAttrProps.length >= 4. Actual length: " + droidmateAttrProps.length, droidmateAttrProps.length >= 4);

    this.testCaseName = droidmateAttrProps[0];
    this.stepIndex = Integer.parseInt(droidmateAttrProps[1]);

    this.actionType = parseActionType(droidmateAttrProps[2], spec);
    this.wait = parseWaitFromActionType(droidmateAttrProps[2]);

    this.comment = droidmateAttrProps[3];

    Rect boundsRect = parseBounds(bounds);
    Pair<Integer, Integer> clickCoords = parseClickCoords(droidmateAttrProps[2], boundsRect);
    this.x = clickCoords.first;
    this.y = clickCoords.second;

    this.view = new ViewNodeAttributes(index, text, resourceId, className, packageName, contentDesc, checkable, clickable, enabled, focusable, scrollable, longClickable, password, boundsRect);
  }

  private String parseActionType(String actionTypeSpec, String spec)
  {
    final String actionType;
    if (actionTypeSpec.startsWith(TestCaseStep.actionTypeSpec_enterTextPrefix) || actionTypeSpec.startsWith(TestCaseStep.actionTypeSpec_wait_and_enterText_prefix))
    {
      String textToEnter;
      if (actionTypeSpec.startsWith(TestCaseStep.actionTypeSpec_enterTextPrefix))
        textToEnter = actionTypeSpec.substring(TestCaseStep.actionTypeSpec_enterTextPrefix.length());
      else if (actionTypeSpec.startsWith(TestCaseStep.actionTypeSpec_wait_and_enterText_prefix))
        textToEnter = actionTypeSpec.substring(TestCaseStep.actionTypeSpec_wait_and_enterText_prefix.length());
      else throw new AssertionError("Unexpected If/else fallthrough");

      this.actionPayload_textToEnter = textToEnter;

      actionType = TestCases.actionType_enterText + "[" + textToEnter + "]";
    } else
    {
      if (actionTypeSpec.equals(TestCaseStep.actionTypeSpec_click))
        actionType = TestCases.actionType_click;
      else if (actionTypeSpec.equals(TestCaseStep.actionTypeSpec_wait_and_click))
        actionType = TestCases.actionType_click;
      else if (actionTypeSpec.startsWith(TestCaseStep.actionTypeSpec_clickOnCoordsPrefix))
        actionType = TestCases.actionType_click;
      else
        throw new AssertionError("Unknown action type in test case step spec of: " + spec);
    }
    return actionType;
  }

  @TargetApi(Build.VERSION_CODES.ECLAIR)
  private Pair<Integer, Integer> parseClickCoords(String actionTypeSpec, Rect boundsRect)
  {
    if (actionTypeSpec.startsWith(TestCaseStep.actionTypeSpec_clickOnCoordsPrefix))
    {
      Matcher coordsMatcher = Pattern.compile(".*\\[(\\p{Digit}+),(-?\\p{Digit}+)\\]").matcher(actionTypeSpec);

      if (!coordsMatcher.matches())
        throw new AssertionError("Expected, but failed, to regex-match two coordinates to action type starting with " +
          "TestCaseStep.actionTypeSpec_clickOnCoordsPrefix i.e. with " + TestCaseStep.actionTypeSpec_clickOnCoordsPrefix);

      if (coordsMatcher.groupCount() != 2)
        throw new AssertionError("Expected, but failed,  to regex-match exactly 2 coordinates to action type starting with " +
          "TestCaseStep.actionTypeSpec_clickOnCoordsPrefix i.e. with " + TestCaseStep.actionTypeSpec_clickOnCoordsPrefix + ". " +
          "Instead, matched not 2 groups, but " + coordsMatcher.groupCount());

      Integer x = Integer.valueOf(coordsMatcher.group(1));
      Integer y = Integer.valueOf(coordsMatcher.group(2));
      return new Pair<Integer, Integer>(x, y);
    } else
    {
      return new Pair<Integer, Integer>(boundsRect.centerX(), boundsRect.centerY());
    }
  }

  private boolean parseWaitFromActionType(String actionTypeSpec)
  {
    return actionTypeSpec.equals(TestCaseStep.actionTypeSpec_wait_and_click) || actionTypeSpec.startsWith(TestCaseStep.actionTypeSpec_wait_and_enterText_prefix);
  }

  /**
   * <p>
   * Parses into a {@link Rect} the {@code bounds} string, having format as output by
   * {@code android.graphics.Rect #toShortString(java.lang.StringBuilder)},
   * that is having form {@code [left,top][right,bottom]}
   *
   * </p><p>
   * Such rectangle bounds format is being used internally by<br/>
   * {@code com.android.uiautomator.core.UiDevice #dumpWindowHierarchy(java.lang.String)}
   *
   * </p>
   */
  public static Rect parseBounds(String bounds)
  {
    Matcher boundsMatcher = Pattern.compile("\\[(-?\\p{Digit}+),(-?\\p{Digit}+)\\]\\[(-?\\p{Digit}+),(-?\\p{Digit}+)\\]").matcher(bounds);
    if (!boundsMatcher.matches())
      throw new RuntimeException("The window hierarchy bounds matcher was unable to match " + bounds + " against the regex");

    if (boundsMatcher.groupCount() != 4)
      throw new RuntimeException("The window hierarchy bounds matcher was expected to match 4 groups, instead it matched: " + boundsMatcher.groupCount());

    int left = Integer.parseInt(boundsMatcher.group(1));
    int top = Integer.parseInt(boundsMatcher.group(2));
    int right = Integer.parseInt(boundsMatcher.group(3));
    int bottom = Integer.parseInt(boundsMatcher.group(4));

    return new Rect(left, top, right, bottom);
  }

  @Override
  public String toString()
  {
    return "TestCaseStep{" +
      "testCaseName='" + testCaseName + '\'' +
      ", stepIndex=" + stepIndex +
      ", actionType='" + actionType + '\'' +
      ", comment='" + comment + '\'' +
      ", x=" + x +
      ", y=" + y +
      ", view=" + view +
      '}';
  }
}
