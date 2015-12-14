Shows new app-based uiautomator. Run with "gradlew cc". This should run on device.

Info:
https://developer.android.com/tools/testing-support-library/index.html#UIAutomator
https://developer.android.com/training/testing/ui-testing/uiautomator-testing.html#setup

https://plus.google.com/+AndroidDevelopers/posts/WCWANrPkRxg
  Allen Hair
  Mar 13, 2015+
  5
  4
  5
   
  +Artem Zinnatullin The main difference with the Instrumentation-based approach is that you build your UIAutomator tests as an APK and launch them with 'am instrument...' like any other Instrumentation test.

  Previously you had to compile your tests as a .jar file, push the jar to /data/local/tmp/ and run 'uiautomator runtest...'.

  Additionally, as an Instrumentation-based test you have access to the Context object, and can send Intents just like a regular app or Instrumentation test. This was not possible with the shell-based version.?