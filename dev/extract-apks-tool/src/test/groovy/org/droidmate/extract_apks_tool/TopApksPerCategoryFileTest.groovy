package org.droidmate.extract_apks_tool

import com.google.common.jimfs.Jimfs
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters

import java.nio.file.FileSystem
import java.time.LocalDate

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4)
public class TopApksPerCategoryFileTest
{

  @Test
  void "computesCategoriesTopApksRows"()
  {
    FileSystem fs = Jimfs.newFileSystem(com.google.common.jimfs.Configuration.unix())

    int top = 10
    int topApksPerCategory = 5

    Version vc1 = new Version(1, "1.0")
    Version vc2 = new Version(2, "2.0")

    ICategory c1tf = new Category("C1 > Top Free")
    ICategory c1tnf = new Category("C1 > Top New Free")
    ICategory c2tf = new Category("C2 > Top Free")
    ICategory c2tnf = new Category("C2 > Top New Free")

    LocalDate minDate = LocalDate.parse("2014-01-01")
    LocalDate maxDate = minDate.plusDays(100)
    LocalDate valDate1 = minDate.plusDays(1)
    LocalDate valDate2 = minDate.plusDays(2)
    LocalDate valDate3 = minDate.plusDays(3)
    LocalDate future = maxDate.plusDays(1)
    LocalDate past = minDate.minusDays(1)

    assert valDate1.isBefore(maxDate)
    assert valDate2.isBefore(maxDate)

    Collection<IApk> apks = [
      // @formatter:off
      new Apk(fs.getPath("delete/v-low/sameName.apk")  ,vc1, false, [(valDate1): [(c1tf): top]]),
      new Apk(fs.getPath("retain/v-high/sameName.apk") ,vc2, false, [(valDate1): [(c1tf): top - 3]]),
      new Apk(fs.getPath("delete/tooHighRank.apk")     ,vc1, false, [(valDate1): [(c1tf): top + 1]]),
      new Apk(fs.getPath("retain/lowestRank.apk")      ,vc1, false, [(valDate1): [(c1tf): 1]]),
      new Apk(fs.getPath("retain/many1.apk")           ,vc1, false, [(valDate3): [(c2tf): 2, (c1tf): 1],
                                                                     (valDate1): [(c1tnf):3, (c2tf):4, (c2tnf):5]]),
      new Apk(fs.getPath("delete/malformed.apk")       ,vc1, true,  [(valDate1): [(c1tf): 1]]),
      new Apk(fs.getPath("delete/future.apk")          ,vc1, false, [(future):   [(c1tf): top - 1]]),
      new Apk(fs.getPath("retain/maxDate.apk")         ,vc1, false, [(maxDate):  [(c1tf): top - 1]]),
      new Apk(fs.getPath("retain/many2.apk")           ,vc1, false, [(valDate2): [(c2tnf): 2]]),
      new Apk(fs.getPath("retain/minDate.apk")         ,vc1, false, [(minDate):  [(c1tf): top - 2]]),
      new Apk(fs.getPath("delete/past.apk")            ,vc1, false, [(past):     [(c1tf): top]]),
      // @formatter:on
    ]

    List params = [apks, top, minDate, maxDate, topApksPerCategory]

    // Act
    List<TopApksPerCategoryFile.Row> rows = TopApksPerCategoryFile.computeCategoriesTopApksRows(*params)

    rows.each {println it}

    rows = rows.findAll {!it.path.startsWith("MISSING")}
    rows.each {assert it.path.startsWith("retain")}
    assert rows.first().path.contains("lowestRank.apk")

    assert rows == rows.sort(false) {it.rank}.sort(false) {it.categoryString}.sort(false) {it.categoryTopFreeString}

    assert rows.size() == 9
  }

}
