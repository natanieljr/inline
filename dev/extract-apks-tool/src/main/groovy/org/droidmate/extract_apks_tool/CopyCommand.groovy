package org.droidmate.extract_apks_tool

import groovy.util.logging.Slf4j

import java.nio.file.FileSystem
import java.nio.file.Files
import java.nio.file.Path

@Slf4j
class CopyCommand implements ICopyCommand
{

  public static final String fileName_apkPathsToCopy = "./paths_to_copy.txt"
  public static final String dirName_target          = "./copied_apks"

  @Override
  void execute(FileSystem fs)
  {
    Path apkPathsToCopyFile = fs.getPath(fileName_apkPathsToCopy)
    Path targetDir = fs.getPath(dirName_target)
    assert Files.isRegularFile(apkPathsToCopyFile)
    assert !Files.exists(targetDir) || Files.isDirectory(targetDir)

    if (Files.isDirectory(targetDir))
      targetDir.deleteDir()

    Files.createDirectory(targetDir)
    assert Files.isDirectory(targetDir)

    List<IApk> apks = []
    apkPathsToCopyFile.eachLine {
      Path apkPath = fs.getPath(it.trim())
      Apk apk = new Apk(new ApkDir(apkPath.parent))
      assert !apk.malformed
      apks += apk
    }

    int unfilteredApksSize = apks.size()
    apks = apks.unique {it.nameWithVersion}

    if (unfilteredApksSize > apks.size())
    {
      log.warn("Detected multiple paths pointing to the same apk, same version. Removed the duplicate paths. " +
        "Reduced the number of copied apks by ${unfilteredApksSize - apks.size()} from $unfilteredApksSize to ${apks.size()}.")
    }

    apks.each {Files.copy(it.path, targetDir.resolve(it.nameWithVersion))}

    targetDir.eachFile {Path it -> assert it.fileName.toString().endsWith(".apk")}

    assert targetDir.filesCount == apks.size()

    log.info("Successfully copied ${targetDir.filesCount} apks.")
  }

}
