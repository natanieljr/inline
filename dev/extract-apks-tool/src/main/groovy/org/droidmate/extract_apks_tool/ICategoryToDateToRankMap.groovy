package org.droidmate.extract_apks_tool

import java.time.LocalDate

interface ICategoryToDateToRankMap
{
  Integer getMinRank()

  Set<ICategory> getCategories()

  Tuple2<Integer, LocalDate> getMinRankWithDate(ICategory category)

  boolean containsMinRank(ICategory category, LocalDate minDate, LocalDate maxDate)
}