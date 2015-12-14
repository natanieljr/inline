package org.droidmate.extract_apks_tool

class DataInAlignedColumns implements IDataInAlignedColumns
{

  String string

  DataInAlignedColumns(List<List> dataRows, List<Justification> justification, List<String> separators = null)
  {
    if (dataRows.empty)
    {
      this.string = ""
      return
    }

    assert dataRows.collect {it.size()}.unique().size() == 1:
      "All rows have to be the same length, yet the rows have lengths: ${dataRows.collect {it.size()}}"

    assert justification.size() == dataRows[0].size()
    assert separators == null || separators.size() == dataRows[0].size() - 1

    int columnIndex = 0
    List<List<String>> alignedDataRowsStrings = dataRows
      .transpose()
      .collect {List column -> justifyColumn(column, justification[columnIndex++])}
      .transpose()

    List<String> mergedRows
    if (separators == null)
      mergedRows = alignedDataRowsStrings.collect {List<String> row -> row.join(" ")
      }
    else
    {
      mergedRows = alignedDataRowsStrings.collect {List<String> row ->
        columnIndex = 0
        row.inject { String mergedRow, String nextRow ->
          mergedRow + separators[columnIndex++] + nextRow
        }
      }
    }
    this.string = mergedRows.join("\n")
  }

  private List<String> justifyColumn(List column, Justification justification)
  {
    List<String> stringsColumn = column.collect {it.toString()}
    int longest = stringsColumn.collect {it.length()}.max()
    String justificationSign = justification == Justification.Right ? "" : "-"
    stringsColumn = stringsColumn.collect {String.format("%$justificationSign${longest}s", it)}
    return stringsColumn
  }

  @Override
  String toString()
  {

    return string
  }
}

