package org.droidmate.extract_apks_tool

interface IProgress
{

  String getCurrentPercentage()

  int getTotal()

  String getCurrentItem()

  void advance()

  boolean getNextPartReached()
}

