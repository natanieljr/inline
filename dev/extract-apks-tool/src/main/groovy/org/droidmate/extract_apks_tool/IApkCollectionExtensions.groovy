package org.droidmate.extract_apks_tool

import java.time.LocalDate

class IApkCollectionExtensions
{

  public static Collection<IApk> inTop(Collection<IApk> self, int top)
  {
    return self.findAll {it.wasEverInTop(top)}
  }

  public static LocalDate getMinDate(Collection<IApk> self)
  {
    self.collect { it.minDateOfAnyRank }.min()
  }

  public static LocalDate getMaxDate(Collection<IApk> self)
  {
    self.collect { it.maxDateOfAnyRank }.max()
  }


  public static Collection<IApk> assertEveryInTop(Collection<IApk> self, int top)
  {
    self.each {assert it.wasEverInTop(top)}
    return self
  }

  public static Map<ICategory, Collection<IApk>> groupByCategory(Collection<IApk> self)
  {
    return self.mapFrom { it.categories }
  }

  public static Collection<IApk> highestVersions(Collection<IApk> self)
  {
    def out = self
      .groupBy {it.path.fileName.toString()}
      .collect {String apkName, List<IApk> apksWithSameName ->
      apksWithSameName.max {it.version}
    }

    assert out.noDuplicates {it.path.fileName.toString()}

    return out
  }

  public static Collection<IApk> inCategoryTop(Collection<IApk> self, ICategory category, int top)
  {
    self.findAll { it.getMinRankWithDateForCategory(category).first <= top }
  }

  public static Collection<IApk> topInDateRange(Collection<IApk> self, ICategory category, LocalDate minDate, LocalDate maxDate)
  {
    self.findAll { it.containsMinRank(category, minDate, maxDate) }
  }

  public static Collection<IApk> sortByTop(Collection<IApk> self, ICategory category)
  {
    self.sort {it.getMinRankWithDateForCategory(category).first}
  }
}
