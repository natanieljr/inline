package org.droidmate.uia_manual_test_cases;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;

/**
 * <p>
 * Used to read the (manually added) DroidMate uiautomator test case annotations in Android XML window dump, where the dump
 * was obtained with
 * {@code com.android.uiautomator.core.UiDevice#dumpWindowHierarchy(java.lang.String)}
 *
 * </p><p>
 * Parser API usage based on {@code http://developer.android.com/training/basics/network-ops/xml.html} accessed Apr 21, 2015.
 *
 * </p>
 */
public class WindowDumpXmlParser
{
  // We don't use namespaces
  private static final String ns                        = null;
  private static final int    testCaseSpecSize          = 4;
  private static final int    testCaseStepSpecStepIndex = 1;

  List<TestCaseStep> parsedTestCaseSteps = new ArrayList<TestCaseStep>();

  public List<TestCaseStep> parse(File windowDump)
  {
    parsedTestCaseSteps.clear();
    try
    {
      return parse(new FileInputStream(windowDump));
    } catch (XmlPullParserException e)
    {
      throw new AssertionError(e);
    } catch (IOException e)
    {
      throw new AssertionError(e);
    }
  }

  public List<TestCaseStep> parse(InputStream in) throws XmlPullParserException, IOException
  {
    try
    {
      XmlPullParser parser = Xml.newPullParser();
      parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
      parser.setInput(in, null);

      parser.nextTag();
      parser.require(XmlPullParser.START_TAG, null, "hierarchy");

      parser.nextTag();
      parser.require(XmlPullParser.START_TAG, null, "node");

      parseNode(parser);
      parser.require(XmlPullParser.END_TAG, null, "node");

      parser.nextTag();
      parser.require(XmlPullParser.END_TAG, null, "hierarchy");

      parser.next();
      parser.require(XmlPullParser.END_DOCUMENT, null, null);

      return parsedTestCaseSteps;

    } finally
    {
      in.close();
    }
  }

  private void parseNode(XmlPullParser parser) throws XmlPullParserException, IOException
  {
    parser.require(XmlPullParser.START_TAG, null, "node");

    parsedTestCaseSteps.addAll(parseDroidmateAttributeIntoTestCaseSteps(parser));

    parser.nextTag();
    while (parser.getEventType() == XmlPullParser.START_TAG)
    {
      parseNode(parser);
      parser.nextTag();
    }

    parser.require(XmlPullParser.END_TAG, null, "node");
  }

  private static List<TestCaseStep> parseDroidmateAttributeIntoTestCaseSteps(XmlPullParser parser)
  {
    final String testCaseStepSpecsString = parser.getAttributeValue(null, "droidmate");
    if (testCaseStepSpecsString != null)
    {
      List<TestCaseStep> parsedTestCaseSteps = new ArrayList<TestCaseStep>();
      List<String> testCaseStepSpecs = parseTestCaseStepSpecs(testCaseStepSpecsString);
      for (String testCaseStepSpec : testCaseStepSpecs)
      {
        parsedTestCaseSteps.add(new TestCaseStep(
          testCaseStepSpec,
          parser.getAttributeValue(null, "index"),
          parser.getAttributeValue(null, "text"),
          parser.getAttributeValue(null, "resource-id"),
          parser.getAttributeValue(null, "class"),
          parser.getAttributeValue(null, "package"),
          parser.getAttributeValue(null, "content-desc"),
          parser.getAttributeValue(null, "checkable"),
          parser.getAttributeValue(null, "clickable"),
          parser.getAttributeValue(null, "enabled"),
          parser.getAttributeValue(null, "focusable"),
          parser.getAttributeValue(null, "scrollable"),
          parser.getAttributeValue(null, "long-clickable"),
          parser.getAttributeValue(null, "password"),
          parser.getAttributeValue(null, "bounds")
        ));

      }
      assertTrue(parsedTestCaseSteps.size() > 0);
      return parsedTestCaseSteps;
    }

    return new ArrayList<TestCaseStep>();
  }

  private static List<String> parseTestCaseStepSpecs(String testCasesStepsSpecsString)
  {
    final String[] testCaseStepSpecsStringSplit = testCasesStepsSpecsString.split(";");
    assertTrue(testCasesStepsSpecsString.length() > 0);

    List<String> testCaseStepSpecs = new ArrayList<String>();
    for (String oneTestCaseStepSpecs : testCaseStepSpecsStringSplit)
      testCaseStepSpecs.addAll(buildTestCaseStepSpecsForOneTestCase(oneTestCaseStepSpecs));

    return testCaseStepSpecs;
  }

  private static List<String> buildTestCaseStepSpecsForOneTestCase(String oneTestCaseStepSpecs)
  {
    List<String> testCaseStepSpecs = new ArrayList<String>();

    final String[] oneTestCaseStepsSpecsSplit = oneTestCaseStepSpecs.split("\\|");
    assertTrue(oneTestCaseStepsSpecsSplit.length == testCaseSpecSize);

    if (oneTestCaseStepsSpecsSplit[testCaseStepSpecStepIndex].contains(","))
    {
      final String[] oneTestCaseStepIndexesSplit = oneTestCaseStepsSpecsSplit[1].split(",");

      for (String stepIndex : oneTestCaseStepIndexesSplit)
      {
        StringBuilder testCaseStepSpec = buildTestCaseStepSpecWithStepIndex(oneTestCaseStepsSpecsSplit, stepIndex);
        testCaseStepSpecs.add(testCaseStepSpec.toString());
      }

    } else
      testCaseStepSpecs.add(oneTestCaseStepSpecs);

    return testCaseStepSpecs;
  }

  private static StringBuilder buildTestCaseStepSpecWithStepIndex(String[] oneTestCaseStepsSpecsSplit, String stepIndex)
  {
    StringBuilder testCaseStepSpec = new StringBuilder();

    for (int i = 0; i < testCaseStepSpecStepIndex; i++)
      testCaseStepSpec.append(oneTestCaseStepsSpecsSplit[i]).append("|");

    testCaseStepSpec.append(stepIndex);

    for (int i = testCaseStepSpecStepIndex + 1; i < testCaseSpecSize; i++)
      testCaseStepSpec.append("|").append(oneTestCaseStepsSpecsSplit[i]);
    return testCaseStepSpec;
  }
}
