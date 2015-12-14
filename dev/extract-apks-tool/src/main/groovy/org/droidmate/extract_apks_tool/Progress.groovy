package org.droidmate.extract_apks_tool

class Progress implements IProgress
{
  final int total
  final int percentagePartSize

  int currentVal = 0


  Progress(int total, int percentagePartSize)
  {
    this.total = total
    this.percentagePartSize = percentagePartSize
  }

  @Override
  String getCurrentPercentage()
  {
    return String.format("%6.2f%%", getCurrentPercentageDouble())
  }

  @Override
  String getCurrentItem()
  {
    int totalWidth = String.valueOf(total).length()
    return String.format("%${totalWidth}d", currentVal)
  }

  private double getCurrentPercentageDouble()
  {
    return (double) ((currentVal / total) * 100)
  }

  private double getLastPercentage()
  {
    return (double) (((currentVal-1) / total) * 100)
  }


  @Override
  void advance()
  {
    this.currentVal+=1
  }

  @Override
  boolean getNextPartReached()
  {
    return (currentPercentageDouble >= currentPercentageRoundedDownToPartSize) && (lastPercentage < currentPercentageRoundedDownToPartSize)
  }

  int getCurrentPercentageRoundedDownToPartSize()
  {
    return Math.floorDiv(((int)Math.floor(currentPercentageDouble)), percentagePartSize) * percentagePartSize
  }
}
