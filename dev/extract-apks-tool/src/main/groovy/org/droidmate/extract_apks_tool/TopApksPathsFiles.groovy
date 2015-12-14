package org.droidmate.extract_apks_tool

import java.nio.file.FileSystem

class TopApksPathsFiles implements ITopApksPathsFiles
{

  private FileSystem    fs
  private ApksRepo      apksRepo
  private List<Integer> topList

  TopApksPathsFiles(FileSystem fs, ApksRepo apksRepo, List<Integer> topList)
  {
    this.fs = fs
    this.apksRepo = apksRepo
    this.topList = topList
  }

  void writeOut()
  {
    this.topList.each {Integer top ->
      ITopApksPathsFile topApksPathsFile = new TopApksPathsFile(this.apksRepo.apks, top, this.fs.getPath("."))
      topApksPathsFile.writeOut()
    }
  }
}
