package org.droidmate.uia_manual_test_cases;

import android.util.Log;

import java.io.File;

public class Utils
{
  static void checkExists(String tag, File file)
  {
    if (!file.isFile())
    {
      String msg = "Expected that following path will point to a file, but it doesn't: " + file.getPath();
      Log.e(tag, "Exception: " + msg);
      throw new RuntimeException(msg);
    }
  }
}
