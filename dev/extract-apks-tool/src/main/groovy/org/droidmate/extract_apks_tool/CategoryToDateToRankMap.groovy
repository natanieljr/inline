package org.droidmate.extract_apks_tool

import groovy.util.logging.Slf4j

import java.time.LocalDate

@Slf4j
class CategoryToDateToRankMap implements ICategoryToDateToRankMap
{
  Map<ICategory, Map<LocalDate, Integer>> map = [:]

  CategoryToDateToRankMap(Map<ICategory, Map<LocalDate, Integer>> map)
  {
    assert map != null
    this.map = map
  }

  @Override
  Set<ICategory> getCategories()
  {
    return map.keySet()
  }

  @Override
  boolean containsMinRank(ICategory category, LocalDate minDate, LocalDate maxDate)
  {
    assert category != null
    assert minDate != null
    assert maxDate != null

    this.map[category].any {LocalDate date, int rank ->
      date >= minDate && date <= maxDate
    }
  }

  @Override
  Integer getMinRank()
  {
    this.map
      .values()
      .collect { it.values()}
      .flatten()
      .min() as Integer
  }

  @Override
  Tuple2<Integer, LocalDate> getMinRankWithDate(ICategory category)
  {
    assert category != null
    assert this.map.containsKey(category)

    assert !this.map[category].isEmpty()

    Map.Entry<LocalDate, Integer> dateAndRank = this.map[category].min { it.value }

    new Tuple2<Integer, LocalDate>(dateAndRank.value, dateAndRank.key)
  }

}
