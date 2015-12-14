package org.droidmate.extract_apks_tool

import groovy.json.JsonException
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

@Slf4j
class JsonFile implements IJsonFile
{

  private Path jsonFile

  JsonFile(Path jsonFile)
  {
    assert jsonFile != null
    assert jsonFile.fileName.toString().endsWith(".json")

    this.jsonFile = jsonFile
  }

  /**
   * Returns a pair of (parsed json, malformed status). If the file is valid, the malformed status is false. If the file is
   * invalid for any reason, the parsed json is null and malformed status is true.
   * @return
   */
  public Tuple2<Map, Boolean> get()
  {
    if (!Files.isRegularFile(jsonFile))
    {
      log.debug("! File doesn't exist: ${this.jsonFile.toString()}")
      return new Tuple2(null, true)
    }
    assert Files.isRegularFile(jsonFile)

    String text
    try
    {
      text = jsonFile.getText(StandardCharsets.UTF_8.toString())
    } catch (IOException e)
    {
      log.debug("! Failed to get text from ${jsonFile.toString()}. The exception: $e")
      return new Tuple2(null, true)
    }
    assert text != null

    if (text == "")
    {
      log.debug("! Empty text in ${jsonFile.toString()}")
      return new Tuple2(null, true)
    }
    assert text.size() > 0

    Map json
    JsonSlurper slurper = new JsonSlurper()
    try
    {
      json = slurper.parseText(text) as Map
    } catch (JsonException e)
    {
      log.debug("! Malformed text in ${jsonFile.toString()}. The exception: $e")
      return new Tuple2(null, true)
    }

    assert json != null
    return new Tuple2(json, false)
  }
}
