package org.droidmate.extract_apks_tool

import java.time.LocalDate

interface IConfiguration
{
  List<Integer> getTop()

  Integer getPerCategory()

  boolean getContainsTop()

  boolean getContainsPerCategory()

  boolean getContainsCopy()

  LocalDate getMinDate()

  LocalDate getMaxDate()

  void printHelp(String prefix)


}