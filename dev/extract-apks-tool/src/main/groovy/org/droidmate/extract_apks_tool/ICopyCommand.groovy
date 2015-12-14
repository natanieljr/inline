package org.droidmate.extract_apks_tool

import java.nio.file.FileSystem

interface ICopyCommand
{
  void execute(FileSystem fs)
}