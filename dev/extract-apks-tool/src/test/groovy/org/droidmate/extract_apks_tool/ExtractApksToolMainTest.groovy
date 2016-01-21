package org.droidmate.extract_apks_tool

import com.github.konrad_jamrozik.ResourcePath
import com.google.common.jimfs.Jimfs
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters

import java.nio.file.FileSystem
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4)
public class ExtractApksToolMainTest
{

  @Test
  void "Runs"()
  {
    FileSystem fs = Jimfs.newFileSystem(com.google.common.jimfs.Configuration.unix())
    Path apksRootDir = fs.getPath(ApksRepo.apksRootDir)
    copyDirsWithApksInFileTree(new ResourcePath("apks").path, apksRootDir)

    // Act 1
    ExtractApksToolMain.main(fs, new Configuration([Configuration.pn_top, "1,100"] as String[]))

    new TopApksPathsFileAsserter(fs, 1).assert()
    new TopApksPathsFileAsserter(fs, 100).assert()

    // Act 2
    ExtractApksToolMain.main(fs, new Configuration([Configuration.pn_perCategory, "1", Configuration.pn_top, "1", Configuration.pn_minDate, "2014-01-01"] as String[]))

    new TopApksPerCategoryFileAsserter(fs, 1, 1, LocalDate.parse("2014-01-01"), null).assert()
  }

  @Test
  void scratchpadT()
  {
    assert [[1],[3,[6]],[[6],[34,7]]].shallowFlatten() == [1,3,[6],[6],[34,7]]
  }

  void copyDirsWithApksInFileTree(Path srcRoot, Path targetRoot)
  {
    srcRoot.traverse(
      [nameFilter: ~/.*\.apk/] as Map<String, Object>)
      {
        Path apkDir = (it as Path).parent
        copyDirWithContents(apkDir, srcRoot, targetRoot)
      }
  }

  private static void copyDirWithContents(Path srcDir, Path srcRoot, Path targetRoot)
  {
    Path targetDir = mapToTarget(srcDir, srcRoot, targetRoot)

    Files.createDirectories(targetDir)
    srcDir.traverse {Path it ->
      Path target = mapToTarget(it, srcRoot, targetRoot)

      assert Files.isRegularFile(it)
      Files.copy(it as Path, target)
    }
  }

  private static Path mapToTarget(Path path, Path srcRoot, Path targetRoot)
  {
    return targetRoot.resolve(srcRoot.relativize(path).toString().replace(File.separator, "/"))
  }
}