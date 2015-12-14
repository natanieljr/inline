package org.droidmate.extract_apks_tool

import com.google.common.base.Objects
import groovy.transform.Canonical

@Canonical
class Category implements ICategory
{

  String string

  String topFreeString

  Category(String categoryString)
  {
    if (categoryString.contains("> Top Free"))
    {
      this.topFreeString = "Top Free"
    } else if (categoryString.contains("> Top New Free"))
    {
      this.topFreeString = "Top New Free"
    } else
      assert false

    this.string = (categoryString - "> Top Free" - "> Top New Free").trim()
  }

  @Override
  public String toString()
  {
    return Objects.toStringHelper(this)
      .add("string", string)
      .add("topFreeString", topFreeString)
      .toString();
  }
}
