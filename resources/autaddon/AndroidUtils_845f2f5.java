/*
 Copyright (c) 2013 Saarland University Software Engineering Chair. All right reserved.

 Author: Konrad Jamrozik, jamrozik@st.cs.uni-saarland.de

 This file is part of the "DroidMate" project.

 www.droidmate.org
 */

package org.droidmate.autaddon;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.util.LogPrinter;

public class AndroidUtils {

  public static final String DEBUG_TAG = "DEBUG";

  public static ActivityInfo[] getActivitiesInfo(Activity activity) throws AutAddonException {

    String packageName = activity.getPackageName();
    PackageManager pm = activity.getPackageManager();
    PackageInfo packageInfo;
    ActivityInfo[] activitiesInfo;

    try {
      packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
    } catch (NameNotFoundException e) {
      throw new AutAddonException(String.format("Failed to get activities info for package %s", packageName), e);
    }

    activitiesInfo = packageInfo.activities;
    return activitiesInfo;
  }

}
