package org.droidmate.extract_apks_tool

import groovy.util.logging.Slf4j

import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream

@Slf4j
class ApkDir implements IApkDir
{

  @Delegate
  Path dir

  Path apk
  Path detailsJson
  Path rankInfoJson

  ApkDir(Path dir)
  {
    this.dir = dir

    Stream<Path> filesStream = Files.list(this.dir)
    List<Path> files = filesStream.collect()
    filesStream.close()

    this.apk = files.findSingle {it.fileName.toString().endsWith(".apk")}
    this.detailsJson = dir.resolve("details.json")
    this.rankInfoJson = dir.resolve("rank-info.json")

    assert apk != null
    assert detailsJson != null
    assert rankInfoJson != null

    assert Files.isDirectory(dir)
    assert Files.isRegularFile(apk)
  }
}
