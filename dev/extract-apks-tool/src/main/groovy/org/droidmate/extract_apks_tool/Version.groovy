package org.droidmate.extract_apks_tool

class Version implements IVersion, Comparable<Version>
{
  Integer versionCode
  String string

  /**
   * Version code is expected to come from details.json.
   * More about versionCode: see android:versionCode in http://developer.android.com/tools/publishing/versioning.html
   */
  Version(int versionCode, String string)
  {
    this.versionCode = versionCode
    this.string = string
  }

  @Override
  int compareTo(Version that) {
    return this.versionCode.compareTo(that.versionCode)
  }
}
