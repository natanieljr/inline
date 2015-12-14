package org.droidmate.extract_apks_tool

import java.nio.file.Files
import java.nio.file.Path

class PathExtensions
{
  // KJA to move to github/utilities
  public static int getFilesCount(Path self)
  {
    assert Files.isDirectory(self)
    Files.newDirectoryStream(self).size()
  }
}
