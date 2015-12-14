package org.droidmate.extract_apks_tool

import com.google.common.jimfs.Jimfs
import org.junit.FixMethodOrder
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters

import java.nio.charset.StandardCharsets
import java.nio.file.FileSystem
import java.nio.file.Files
import java.nio.file.Path

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4)
public class CopyCommandTest
{
  @Ignore("broken, see KJA todo")
  @Test
  void "executes"()
  {
    // KJA fix the test +
    // add two apps with the same name but different vers +
    // one of them occurring twice
    FileSystem fs = Jimfs.newFileSystem(com.google.common.jimfs.Configuration.unix())
    Path pathsToCopyFile = fs.getPath(CopyCommand.fileName_apkPathsToCopy)
    Path apk1Path = fs.getPath("/work/apks/nested_dir/app.apk")
    Path apk2Path = fs.getPath("/work/apks/other_nested_dir/yet_another_dir/other_apk.apk")
    Files.createDirectories(apk1Path.parent)
    Files.createDirectories(apk2Path.parent)
    Files.createFile(apk1Path)
    Files.createFile(apk2Path)
    pathsToCopyFile.write(apk1Path.toString(), StandardCharsets.UTF_8.toString())
    pathsToCopyFile.withPrintWriter(StandardCharsets.UTF_8.toString()) {
      it.println apk1Path
      it.println apk2Path
    }

    // Act 1
    new CopyCommand().execute(fs)

    Files.delete(apk1Path)
    Path apk3Path = apk1Path.parent.resolve("third_apk.apk")
    Files.createFile(apk3Path)
    pathsToCopyFile.withPrintWriter(StandardCharsets.UTF_8.toString()) {
      it.println apk2Path
      it.println apk3Path
    }

    // Act 2
    new CopyCommand().execute(fs)
  }
}