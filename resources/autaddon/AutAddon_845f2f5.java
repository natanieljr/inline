/*
 Copyright (c) 2013 Saarland University Software Engineering Chair. All right reserved.

 Author: Konrad Jamrozik, jamrozik@st.cs.uni-saarland.de

 This file is part of the "DroidMate" project.

 www.droidmate.org
 */

package org.droidmate.autaddon;

import android.app.Activity;
import android.util.Log;
import org.droidmate.common.Constants;

import java.util.ArrayList;
import java.util.List;

public class AutAddon
{

  private final static String thisClassName = getClassNameSuffix(AutAddon.class.getName());
  /**
   * Instance of Activity under test, required to obtain the GUI model for analysis.
   */
  private static Activity activityUnderTest;

  // Temporary fix (see to-do in setActivityUnderTest())
  private static boolean      serverStarted   = false;
  private static List<String> currentCoverage = new ArrayList<String>();

  public static Activity getActivityUnderTest()
  {
    return activityUnderTest;
  }

  /**
   * This method shall be called by AUT Activity's onCreate() callback inserted during instrumentation.
   *
   * @throws AutAddonException
   */
  public static void setActivityUnderTest(Activity activityUnderTest) throws AutAddonException
  {
    Log.d(thisClassName,
      "Calling AutAddon.setActivityUnderTest() on activity: " + activityUnderTest.getLocalClassName());

    AutAddon.activityUnderTest = activityUnderTest;

    IAutDriver autDriver = new AutDriver(activityUnderTest);
    AutAddonServer autAddonServer = new AutAddonServer(autDriver);

    // This is a temporary fix. The bug is: when the application gui is automatically tested and different
    // activity gets executed, this method is called again (because onCreate of all activities is instrumented), so the
    // server attempts to start again, but it is already bound, so java.net.BindException (Address already in use)
    // is thrown.
    if (!serverStarted)
    {
      Log.d(thisClassName, "Starting AutAddonServer.");
      try
      {
        autAddonServer.start(Constants.AUTADDON_SERVER_PORT);
        serverStarted = true;
      } catch (InterruptedException e)
      {
        Log.e(thisClassName, "Starting AutAddonServer failed.", e);
      }
      Log.d(thisClassName, "Starting AutAddonServer succeeded.");
    }

  }

  /**
   * <p>
   * Called from code instrumented during instrumentation.
   *
   * </p><p>
   * <i>This doc was last reviewed on 09 Oct 2013.</i>
   * </p>
   */
  @SuppressWarnings("UnusedDeclaration") // The method is actually used, but by instrumentation.
  public static void logCoverage(String log) throws AutAddonException
  {
    currentCoverage.add(log);
  }

  public static String getClassNameSuffix(String fullClassName)
  {
    return fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
  }

  public static List<String> getAndResetCurrentCoverage()
  {
    List<String> returnedCoverage = currentCoverage;
    currentCoverage = new ArrayList<String>();
    return returnedCoverage;
  }
}
