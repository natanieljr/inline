package org.droidmate.extract_apks_tool

import com.google.common.base.Objects
import groovy.transform.Canonical
import groovy.transform.Memoized
import groovy.util.logging.Slf4j
import org.apache.commons.io.FilenameUtils

import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Slf4j
@Canonical
class Apk implements IApk
{

  Path path

  Version version

  boolean malformed

  Map<LocalDate, Map<ICategory, Integer>> dateToCategoryToRank

  LocalDate getMinDateOfAnyRank() {this.dateToCategoryToRank.keySet().min()}

  LocalDate getMaxDateOfAnyRank() {this.dateToCategoryToRank.keySet().max()}

  Apk(IApkDir dir)
  {
    this.path = dir.apk

    if (Files.size(this.path) == 0)
      this.malformed = true

    if (!this.malformed)
      this.version = parseDetailsJson(dir)

    if (!this.malformed)
      this.malformed = isBlacklisted()

    if (!this.malformed)
      this.dateToCategoryToRank = parseRankInfo(dir)
  }

  private boolean isBlacklisted()
  {

    if ((this.baseName == "com.caynax.a6w") && (this.version.string == "8.1"))
    {
      // aapt dump badging fails:
      // ERROR: dump failed because no AndroidManifest.xml found

      assert Files.size(this.path) <= 5*1024

      return true
    }

    return false
  }

  Apk(Path path, Version version, boolean malformed, Map<LocalDate, Map<ICategory, Integer>> dateToCategoryToRank)
  {
    this.path = path
    this.version = version
    this.malformed = malformed
    this.dateToCategoryToRank = dateToCategoryToRank
  }

  @Override
  Set<ICategory> getCategories()
  {
    return this.categoryToRanks.categories
  }

  @Override
  boolean wasEverInTop(int top)
  {
    if (this.malformed)
      return false

    Integer minRank = this.categoryToRanks.minRank

    if (minRank == null)
      return false

    return minRank <= top
  }


  @Override
  Tuple2<Integer, LocalDate> getMinRankWithDateForCategory(ICategory category)
  {
    this.categoryToRanks.getMinRankWithDate(category)
  }

  @Override
  boolean containsMinRank(ICategory category, LocalDate minDate, LocalDate maxDate)
  {
    this.categoryToRanks.containsMinRank(category, minDate, maxDate)
  }


  @Override
  String getPathString()
  {
    this.path.toString()
  }

  private getName()
  {
    this.path.fileName.toString()
  }

  private getBaseName()
  {
    FilenameUtils.getBaseName(this.name)
  }

  private getExtension()
  {
    FilenameUtils.getExtension(this.name)
  }

  @Override
  String getNameWithVersion()
  {
    this.baseName + "_v" + this.version.string + "." + this.extension
  }

  @Override
  public String toString()
  {
    return Objects.toStringHelper(this)
      .add("path", path)
      .toString();
  }

  private Version parseDetailsJson(IApkDir dir)
  {
    Map detailsJson
    (detailsJson, malformed) = new JsonFile(dir.detailsJson).get()
    if (this.malformed)
      return null
    assert detailsJson != null

    String versionString = detailsJson?.details?.appDetails?.versionString as String
    if (versionString == null)
    {
      log.debug("! Missing versionString in ${dir.detailsJson.toString()}")
      this.malformed = true
      return null
    }

    Integer versionCode = detailsJson?.details?.appDetails?.versionCode as Integer
    Integer majorVersionNumber
    if (versionCode == null)
    {
      majorVersionNumber = detailsJson?.details?.appDetails?.majorVersionNumber as Integer
      if (majorVersionNumber == null)
      {
        log.debug("! Missing versionCode and majorVersionNumber in ${dir.detailsJson.toString()}")
        this.malformed = true
        return null
      } else
        log.debug("! Missing versionCode in ${dir.detailsJson.toString()}. Using instead majorVersionNumber of $majorVersionNumber")
    }

    assert versionCode != null || majorVersionNumber != null

    if (versionCode != null)
      return new Version(versionCode, versionString)
    else if (majorVersionNumber != null)
      return new Version(majorVersionNumber, versionString)
    else
    {
      assert false
      return null // impossible, just to make static analysis happy
    }
  }

  private Map<LocalDate, Map<ICategory, Integer>> parseRankInfo(IApkDir dir)
  {
    Map rankInfoJson
    (rankInfoJson, malformed) = new JsonFile(dir.rankInfoJson).get()
    if (this.malformed)
      return (Map<LocalDate, Map<ICategory, Integer>>) [:]
    assert rankInfoJson != null

    Map<LocalDate, Map<ICategory, Integer>> dateToCategoryToRank = extractDateToCategoryToRank(rankInfoJson)

    return dateToCategoryToRank
  }

  /**
   * Example of (jsonDate + ":" + jsonCategoryToRank)
   *
   *   2014-05-18:[Tools > Top Free:115, Widgets > Top New Free:4]
   */
  private Map<LocalDate, Map<ICategory, Integer>> extractDateToCategoryToRank(Map jsonDateToCategoryToRank)
  {
    //noinspection GroovyAssignabilityCheck
    return jsonDateToCategoryToRank.collectEntries {String jsonDate, Map jsonCategoryToRank ->

      assert jsonDate.size() > 0
      assert jsonCategoryToRank.size() >= 1

      LocalDate date = LocalDate.parse(jsonDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

      //noinspection GroovyAssignabilityCheck
      Map<ICategory, Integer> categoryToRank = jsonCategoryToRank
        .collectEntries {String category, int rank -> [new Category(category), rank]}

      return [date, categoryToRank]
    }
  }

  @Memoized
  private ICategoryToDateToRankMap getCategoryToRanks()
  {
    new CategoryToDateToRankMap(collectCategoryToDateToRank(this.dateToCategoryToRank))
  }

  private Map<ICategory, Map<LocalDate, Integer>> collectCategoryToDateToRank(Map<LocalDate, Map<ICategory, Integer>> dateToCategoryToRank)
  {
    dateToCategoryToRank.flipKeysNesting()

  }

}