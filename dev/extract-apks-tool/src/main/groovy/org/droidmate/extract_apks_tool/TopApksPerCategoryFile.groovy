package org.droidmate.extract_apks_tool

import com.google.common.base.Objects
import groovy.transform.Canonical
import groovy.util.logging.Slf4j

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate

import static java.lang.String.format

@Slf4j
class TopApksPerCategoryFile implements ITopApksPerCategoryFile
{

  public static final String fileNameFormatString = "apks_perCategory%d_inTop%d_minDate%s_maxDate%s.txt"

  private final Path      path
  private final int       topApksPerCategoryCount
  private final LocalDate minDate
  private final LocalDate maxDate
  private final int       top

  TopApksPerCategoryFile(Path dir, int topApksPerCategoryCount, int top, LocalDate minDate, LocalDate maxDate)
  {
    this.path = dir.resolve(format(fileNameFormatString, topApksPerCategoryCount, top, minDate, maxDate))
    this.topApksPerCategoryCount = topApksPerCategoryCount
    this.top = top
    this.minDate = minDate
    this.maxDate = maxDate

    assert Files.isDirectory(dir)
    assert !(Files.exists(this.path)) || Files.isRegularFile(this.path)
    assert topApksPerCategoryCount > 0
    assert top > 0
  }

  @Override
  void writeOut(Collection<IApk> apks)
  {
    Tuple3<String, LocalDate, LocalDate> dates = computeMinMaxDates(apks, this.minDate, this.maxDate)

    List<Row> categoriesTopApksRows = computeCategoriesTopApksRows(apks, this.top, dates.second, dates.third, this.topApksPerCategoryCount)

    log.info("Bounds of the considered date range: ")
    log.info(dates.first[0..-2]) // [0..-2] removes the new line character at the end.

    this.path.write(dates.first + computeCategoriesTopApksRowsString(categoriesTopApksRows.collect {it.toList()}), StandardCharsets.UTF_8.toString())

    log.info("Written out to file ${this.path.toString()}")
  }

  private
  static Tuple3<String, LocalDate, LocalDate> computeMinMaxDates(Collection<IApk> apks, LocalDate minDate, LocalDate maxDate)
  {
    LocalDate outMinDate
    LocalDate outMaxDate
    String minDateInferredString
    String maxDateInferredString

    if (minDate != null)
    {
      outMinDate = minDate
      minDateInferredString = ""
    } else
    {
      outMinDate = apks.minDate
      minDateInferredString = " (inferred from apk metadata) "
    }
    if (maxDate != null)
    {
      outMaxDate = maxDate
      maxDateInferredString = ""
    } else
    {
      outMaxDate = apks.maxDate
      maxDateInferredString = " (inferred from apk metadata) "
    }

    String minDateLine = "# minDate: $outMinDate$minDateInferredString\n"
    String maxDateLine = "# maxDate: $outMaxDate$maxDateInferredString\n"

    return new Tuple3(minDateLine + maxDateLine, outMinDate, outMaxDate)
  }

  private static String computeCategoriesTopApksRowsString(List<List> categoriesTopApksRows)
  {
    String categoriesTopApksRowsString = new DataInAlignedColumns(
      categoriesTopApksRows,
      [Justification.Left, Justification.Right, Justification.Right, Justification.Left, Justification.Left],
      [" | ", " | ", " | ", " | "]
    ).toString()
    return categoriesTopApksRowsString
  }

  public
  static List<Row> computeCategoriesTopApksRows(Collection<IApk> apks, int top, LocalDate minDate, LocalDate maxDate, int topApksPerCategoryCount)
  {
    Map<ICategory, Collection<IApk>> categoryMap = apks.findAll {!it.malformed}.groupByCategory()

    categoryMap = (Map<ICategory, Collection<IApk>>) categoryMap.collectEntries {category, apksPerCategory ->

      Collection<IApk> filteredApksPerCategory = apksPerCategory
        .highestVersions()
        .inCategoryTop(category, top)
        .topInDateRange(category, minDate, maxDate)
        .sortByTop(category)
        .take(topApksPerCategoryCount)

      return [category, filteredApksPerCategory]
    }

    List<Row> rows = categoryMap
      .sort {e1, e2 -> e1.key.string <=> e2.key.string}
      .sort {e1, e2 -> e1.key.topFreeString <=> e2.key.topFreeString}
      .collect {category, apksPerCategory ->

      List<Row> rowsOfCategory = apksPerCategory.collect {IApk apk ->
        Tuple2<Integer, LocalDate> minRankAndDate = apk.getMinRankWithDateForCategory(category)
        new Row(category.string, category.topFreeString, minRankAndDate.first, minRankAndDate.second, apk.pathString)
      }

      if (rowsOfCategory.size() < topApksPerCategoryCount)
      {
        log.warn("! Got only ${rowsOfCategory.size()} top-ranked apks for category " +
          "${format("%-26s", category.string)} > ${format("%12s", category.topFreeString)} " +
          "while required ${topApksPerCategoryCount}.")

        (1..(topApksPerCategoryCount - rowsOfCategory.size())).each {
          rowsOfCategory << new Row(category.string, category.topFreeString, -1, null, "MISSING ENTRY!")
        }
      }

      return rowsOfCategory

    }
    .shallowFlatten()
//    .inject([] as List) {List<Row> rows, List<Row> rowsOfCategory -> rows + rowsOfCategory}

    return rows
  }

  @Canonical
  static class Row
  {

    String    categoryString
    String    categoryTopFreeString
    int       rank
    LocalDate dateOfRank
    String    path

    List toList()
    {
      return [categoryString, categoryTopFreeString, rank, dateOfRank, path]
    }

    @Override
    public String toString()
    {
      return Objects.toStringHelper(this)
        .addValue(categoryString)
        .addValue(categoryTopFreeString)
        .addValue(rank)
        .addValue(dateOfRank)
        .addValue(path)
        .toString()
    }
  }
}
