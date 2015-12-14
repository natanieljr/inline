package org.droidmate.extract_apks_tool

import groovy.util.logging.Slf4j

import java.nio.file.FileSystem
import java.nio.file.Files
import java.nio.file.Path

@Slf4j
class ApksRepo implements IApksRepo
{
  public static final String apksRootDir = "/jacobs/APKs"

  private final Path path

  @Lazy
  Collection<IApk> apks = this.scanForApks()

  ApksRepo(FileSystem fs)
  {
    this.path = fs.getPath(apksRootDir)
    assert Files.exists(path)
  }

  private Collection<IApk> scanForApks()
  {
    Collection<IApk> apks = []
    int dirsToScanCount = Files.list(this.path).count()
    log.info("Scanning for all available apks. Dirs to scan count: " + dirsToScanCount)
    IProgress progress = new Progress(dirsToScanCount, /* percentage part size */ 1)

    this.path.traverse(
      [
        filter: {Path it ->
          (it.fileName.toString().endsWith(".apk") && Files.isRegularFile(it)) || it.relativize(this.path).nameCount <= 1
        },
      ] as Map<String, Object>)
      {
        Path p = it as Path

        if (p.relativize(this.path).nameCount <= 1)
        {
          progress.advance()
          if (progress.nextPartReached)
            log.info("Scanning dir no. ${progress.currentItem} out of ${progress.total} (${progress.currentPercentage}): ${p.toString()}")
        }

        if (p.fileName.toString().endsWith(".apk"))
        {
          apks += new Apk(new ApkDir(p.parent))
          if (apks.size() % 100 == 0)
            log.info("So far ${apks.size()} apks were found.")
        }

        // Uncomment to limit the apks to be processed.
//        if (apks.size() == 2000)
//          return FileVisitResult.TERMINATE
      }

    log.info("In total ${apks.size()} apks were found.")
    return apks
  }
}
