package org.droidmate.extract_apks_tool

import java.nio.file.Path
import java.time.LocalDate

interface IApk
{
  Path getPath()

  boolean getMalformed()

  Set<ICategory> getCategories()

  boolean wasEverInTop(int top)

  Tuple2<Integer, LocalDate> getMinRankWithDateForCategory(ICategory category)

  boolean containsMinRank(ICategory category, LocalDate minDate, LocalDate maxDate)

  Version getVersion()

  LocalDate getMinDateOfAnyRank()

  LocalDate getMaxDateOfAnyRank()

  String getPathString()

  String getNameWithVersion()
}