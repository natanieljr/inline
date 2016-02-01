import groovy.transform.Immutable

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.regex.Matcher
import java.util.regex.Pattern

Path summary = Paths.get("../summary.txt")

assert Files.isRegularFile(summary)

String name
int time
int actions
int resets
int apis
int eventApiPairs

List<RunDescriptor> runs = []

summary.readLines().each {

  if (it.startsWith("droidmate-run:"))
    name = it - "droidmate-run:"

  if (it.startsWith("Total run time:"))
  {
    List<Integer> numbers = extractNumbers(it)
    time = numbers[0]*60 + numbers[1]
  }

  if (it.startsWith("Total actions count:"))
    actions = extractNumbers(it)[0]

  if (it.startsWith("Total resets count:"))
    resets = extractNumbers(it)[0]

  if (it.startsWith("Unique API calls count observed in the run:"))
    apis = extractNumbers(it)[0]

  if (it.startsWith("Unique [API call, event] pairs count observed in the run:"))
  {
    eventApiPairs = extractNumbers(it)[0]
    runs += new RunDescriptor(name, time,actions,resets, apis, eventApiPairs)
  }
}

println (["name","seconds","actions","in_that_resets","unique_apis","unique_event_apis"].join(";"))
runs.each { run ->
    println ([run.name,run.time,run.actions,run.resets,run.apis,run.eventApiPairs].join(";"))
}



List<Integer> extractNumbers(String str)
{
  Pattern p = Pattern.compile("-?\\d+")
  Matcher m = p.matcher(str)
  List<Integer> out = []
  while (m.find())
  {
    out += m.group() as Integer
  }
  return out
}

@Immutable
class RunDescriptor
{
  String name
  int time
  int actions
  int resets
  int apis
  int eventApiPairs
}
