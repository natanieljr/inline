package org.droidmate.extract_apks_tool

import com.google.common.base.Objects
import groovy.util.logging.Slf4j
import joptsimple.BuiltinHelpFormatter
import joptsimple.OptionParser
import joptsimple.OptionSet
import joptsimple.OptionSpec
import joptsimple.util.DateConverter

import java.time.LocalDate
import java.time.ZoneId

@Slf4j
class Configuration implements IConfiguration
{

  public static final String pn_top         = "-top"
  public static final String pn_perCategory = "-perCategory"
  public static final String pn_minDate     = "-minDate"
  public static final String pn_maxDate     = "-maxDate"
  public static final String pn_copy        = "-copy"

  List<Integer> top
  Integer       perCategory
  LocalDate     minDate
  LocalDate     maxDate
  Boolean       copy

  private OptionParser parser

  Configuration(String[] args)
  {
    this.parser = new OptionParser()

    def help = new BuiltinHelpFormatter(120, 2)
    parser.formatHelpWith(help)

    OptionSpec<Integer> topSpec = parser
      .accepts(pn_top.drop(1) as String,
      "Provide either by itself, or together with ${pn_perCategory}. When provided by itself, multiple, comma separated " +
        "values can be given."
    )
      .withRequiredArg()
      .ofType(Integer.class)
      .withValuesSeparatedBy(",")

    OptionSpec<Integer> perCategorySpec = parser
      .accepts(pn_perCategory.drop(1) as String,
      "Also requires persence of one value of ${pn_top}. Optionally you can add ${pn_minDate} and ${pn_maxDate}."
    )
      .withRequiredArg()
      .ofType(Integer.class)

    OptionSpec<Date> minDateSpec = parser
      .accepts(pn_minDate.drop(1) as String,
      "Use with $pn_perCategory. When not provided, means \"no constraint on date\".")
      .withRequiredArg()
      .withValuesConvertedBy(DateConverter.datePattern("yyyy-MM-dd"))

    OptionSpec<Date> maxDateSpec = parser
      .accepts(pn_maxDate.drop(1) as String,
      "Use with $pn_perCategory. When not provided, means \"no constraint on date\".")
      .withRequiredArg()
      .withValuesConvertedBy(DateConverter.datePattern("yyyy-MM-dd"))

    OptionSpec copySpec = parser.accepts(pn_copy.drop(1) as String,
      "Use by itself. Provide $CopyCommand.fileName_apkPathsToCopy file with paths to copy into $CopyCommand.dirName_target")

    OptionSet options = parser.parse(args)

    this.top = options.valuesOf(topSpec)
    this.perCategory = options.valueOf(perCategorySpec)
    this.minDate = toLocalDate(options.valueOf(minDateSpec))
    this.maxDate = toLocalDate(options.valueOf(maxDateSpec))
    this.copy = options.has(copySpec)
  }

  LocalDate toLocalDate(Date date)
  {
    if (date == null)
      return null

    // Adapted from: http://stackoverflow.com/a/21242111/986533
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

  }

  @Override
  boolean getContainsTop()
  {
    !this.top.empty
  }

  @Override
  boolean getContainsPerCategory()
  {
    return this.perCategory != null
  }

  @Override
  boolean getContainsCopy()
  {
    this.copy
  }

  @Override
  void printHelp(String prefix)
  {
    String toolDescription = """\
This tool is used for finding paths of "top X apps per category" in ${ApksRepo.apksRootDir}.

Step 1:
First, run it with "${pn_top}=T", where T is an integer, to generate a .txt file in current working dir,
a "top-T file", containing paths to apps which have ever been in the T lowest ranked apps in at least one category.
You can generate multiple such files at once, by providing multiple values, comma separated.

Step 2:
Next, run the tool with ${pn_perCategory}=PC ${pn_top}=T [${pn_minDate}=Dmin ${pn_maxDate}=Dmax]
Note the last options are not required. The tool will read the paths of apks to process from a "top-T file" generated in previous
run. It will then generate a .txt file to current working dir. The file will contain, for each category found in these apps,
a list of at most PC apps, being in top T apps for given category, in date range from Dmin to Dmax.

If multiple versions of given app are present, for each category only the newest one is considered, as determined by the app's
versionCode.

If there will be less than PC apps found for some category that are in T, warning will be issued.
In such case try to provide bigger T, expand Dmin and Dmax, or skip Dmin and Dmax completely.

Step 3:
Using your favorite text processing tool, extract from the file resulting from step 2 the paths of apks you want to copy, and put
them into ${CopyCommand.fileName_apkPathsToCopy}. Then run the tool with ${pn_copy}. It will take all the apk paths in the file
and copy them to the ${CopyCommand.dirName_target} dir, from which you can easily copy them in batch wherever you want.
"""

    def writer = new CharArrayWriter()
    writer.write("$prefix\n\n$toolDescription\n\n")
    parser.printHelpOn(writer)
    log.info(writer.toString())

  }

  @Override
  public String toString()
  {
    return Objects.toStringHelper(this)
      .add("perCategory", perCategory)
      .add("top", top)
      .add("maxDate", maxDate)
      .add("minDate", minDate)
      .add("copy", copy)
      .toString()
  }
}
