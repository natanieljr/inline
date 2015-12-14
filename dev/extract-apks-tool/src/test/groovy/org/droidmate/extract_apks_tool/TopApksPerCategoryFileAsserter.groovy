package org.droidmate.extract_apks_tool

import java.nio.file.FileSystem
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate

import static java.lang.String.format

class TopApksPerCategoryFileAsserter
{


  private FileSystem fs
  private int        topApksPerCategoryCount
  private int        top
  private LocalDate  minDate
  private LocalDate  maxDate

  TopApksPerCategoryFileAsserter(FileSystem fs, int topApksPerCategoryCount, int top, LocalDate minDate, LocalDate maxDate)
  {
    this.fs = fs
    this.topApksPerCategoryCount = topApksPerCategoryCount
    this.top = top
    this.minDate = minDate
    this.maxDate = maxDate
  }

  void "assert"()
  {
    Path topApksPerCategoryPathsFile = fs.getPath(format(TopApksPerCategoryFile.fileNameFormatString, this.topApksPerCategoryCount, this.top, this.minDate, this.maxDate))
    assert Files.size(topApksPerCategoryPathsFile) > 0
    //noinspection GroovyAssignabilityCheck
    topApksPerCategoryPathsFile.eachLine {String line ->
      assert line.startsWith("#") || [".apk", "ENTRY!"].any {line.trim().endsWith(it)}
    }

    println "----------------"
    println "Text of ${topApksPerCategoryPathsFile.toString()}"
    println "----------------"
    println topApksPerCategoryPathsFile.text
    println "----------------"

  }
}
