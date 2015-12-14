package org.droidmate.extract_apks_tool

import java.nio.file.Path

interface IApkDir extends Path
{

  Path getApk()

  Path getDetailsJson()

  Path getRankInfoJson()
}