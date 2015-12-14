package org.droidmate.extract_apks_tool

import groovy.util.logging.Slf4j

import java.nio.file.FileSystem
import java.nio.file.FileSystems

@Slf4j
class ExtractApksToolMain
{

  /**
   * <p>
   * For help, see source of:
   * {@link org.droidmate.extract_apks_tool.Configuration#printHelp}
   *
   * </p><p>
   * Example run configs on exsyst server:
   *
   * </p><p>
   * Step 1:
   * <pre>jamrozik@exsyst:/scratch/jamrozik/extract-apks-tool/bin$ nohup ./extract-apks-tool -top=1,2,3,4,5,6,7,8,9,10,15,20,25,30,35,40,45,50,60,70,80,90,100</pre>
   *
   * </p><p>
   * Step 2:
   * <pre>jamrozik@exsyst:/scratch/jamrozik/extract-apks-tool/bin$ ./extract-apks-tool -perCategory=5 -top=5</pre>
   *
   * </p><p>
   * Step 2.5:
   *
   * </p><p>
   * Manually extract apk paths you wish to copy from the file output in step 2 and put it into file required by step 3, as explained in help linked above.
   *
   * </p><p>
   * Step 3:
   * <pre>jamrozik@exsyst:/scratch/jamrozik/extract-apks-tool/bin$ ./extract-apks-tool -copy</pre>
   *
   * </p><p>
   * Step 4:
   *
   * </p><p>
   * Pull the apks into local machine from the step 3 output dir. On windows, use WinSCP.
   *
   * </p>
   */
  public static void main(String[] args)
  {
    main(FileSystems.getDefault(), new Configuration(args))
  }

  public static void main(FileSystem fs, IConfiguration cfg)
  {
    log.info("Running with $cfg")

    if (cfg.containsCopy)
    {
      new CopyCommand().execute(fs)

    } else if (cfg.containsPerCategory)
    {
      int top = cfg.top.findSingle()

      ITopApksPathsFile topApksPathsFile = new TopApksPathsFile(fs.getPath("."), top)
      ITopApksPerCategoryFile file = new TopApksPerCategoryFile(fs.getPath("."), cfg.perCategory, top, cfg.minDate, cfg.maxDate)
      file.writeOut(topApksPathsFile.apks)

    } else if (cfg.containsTop)
    {
      assert cfg.minDate == null: "Cannot provide ${Configuration.pn_minDate} option together with ${Configuration.pn_top}"
      assert cfg.maxDate == null: "Cannot provide ${Configuration.pn_maxDate} option together with ${Configuration.pn_top}"

      ITopApksPathsFiles files = new TopApksPathsFiles(fs, new ApksRepo(fs), cfg.top)
      files.writeOut()
    } else
    {
      cfg.printHelp("No cmd line options provided. Printing help.")
    }

    log.info("extract-apks-tool finished running successfully.")
  }
}
