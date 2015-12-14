package org.droidmate.extract_apks_tool

class ApksWithHighestVersionCode implements IApksWithHighestVersionCode
{

  @Delegate
  Collection<IApk> apks

  ApksWithHighestVersionCode(Collection<IApk> apks)
  {
    this.apks = apks
      .groupBy {it.path.fileName.toString()}
      .collect {String apkName, List<IApk> apksWithSameName ->
      apksWithSameName.max {it.version}
    }

    assert this.apks.noDuplicates {it.path.fileName.toString()}
  }
}
