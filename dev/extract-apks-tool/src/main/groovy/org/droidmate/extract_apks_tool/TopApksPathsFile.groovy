package org.droidmate.extract_apks_tool

import groovy.util.logging.Slf4j

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

@Slf4j
class TopApksPathsFile implements ITopApksPathsFile
{

  public static final String fileNameFormatString = "apks_paths_top%d.txt"

  final Collection<IApk> apks
  final int              top

  @Delegate
  Path path

  TopApksPathsFile(Collection<IApk> apks, int top, Path dir)
  {
    assert apks != null
    assert top >= 1
    assert Files.isDirectory(dir)

    this.apks = apks
    this.top = top
    this.path = dir.resolve(String.format(fileNameFormatString, top))

    assert !(Files.exists(this.path)) || Files.isRegularFile(this.path)
  }

  TopApksPathsFile(Path dir, Integer top)
  {
    assert Files.isDirectory(dir)
    assert top >= 1

    this.path = dir.resolve("apks_paths_top${top}.txt")

    assert Files.isRegularFile(this.path)

    List<String> apksPathsStrings = this.path.readLines(StandardCharsets.UTF_8.toString())

    int pathsCount = apksPathsStrings.size()
    log.info("Processing apks paths. Paths to process: " + pathsCount)
    IProgress progress = new Progress(pathsCount, /* percentage part size */ 1)

    List<Apk> apks = apksPathsStrings
      .findResults { String apkPath ->

      progress.advance()
      if (progress.nextPartReached)
        log.info("Processsing path no. ${progress.currentItem} out of ${progress.total} (${progress.currentPercentage}): ${apkPath.toString()}")

      Path apkDirPath = dir.fileSystem.getPath(apkPath).parent
      def isDir = Files.isDirectory(apkDirPath)

      if (!isDir)
      {
        log.warn("Skipping app, as its dir path doesn't point to a dir: ${apkDirPath.toString()}")
        return null
      }

      return new Apk(new ApkDir(apkDirPath))
    }

    log.info("All apks path processed.")

    this.apks = apks.findAll {!it.malformed}
    this.apks.assertEveryInTop(top)
  }


  @Override
  void writeOut()
  {
    String pathsList = this.apks.inTop(this.top).collect {it ->
      it.path.toAbsolutePath().toString()
    }.join("\n")
    this.path.write(pathsList, StandardCharsets.UTF_8.toString())
  }

}
