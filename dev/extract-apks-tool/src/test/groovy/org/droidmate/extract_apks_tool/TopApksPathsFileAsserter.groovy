package org.droidmate.extract_apks_tool

import java.nio.charset.StandardCharsets
import java.nio.file.FileSystem
import java.nio.file.Files
import java.nio.file.Path

class TopApksPathsFileAsserter
{

  private FileSystem fs
  private int        top

  TopApksPathsFileAsserter(FileSystem fs, int top)
  {
    this.fs = fs
    this.top = top
  }

  void "assert"()
  {
    Path topApksPathsFile = fs.getPath(String.format(TopApksPathsFile.fileNameFormatString, top))
    assert Files.size(topApksPathsFile) > 0
    //noinspection GroovyAssignabilityCheck
    topApksPathsFile.eachLine(StandardCharsets.UTF_8.toString()) {String line ->
      assert line.endsWith(".apk")
      assert Files.isRegularFile(fs.getPath(line))
    }

    println "----------------"
    println "Text of ${topApksPathsFile.toString()}"
    println "----------------"
    println topApksPathsFile.text
    println "----------------"

  }
}
