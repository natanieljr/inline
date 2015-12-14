package org.droidmate.extract_apks_tool

import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4)
public class IterableAndMapExtensionsTest
{
  @Test
  void "flips keys nesting"()
  {
    assert [
      a: [x: 1, y: 2],
      b: [x: 3, y: 4]
    ].flipKeysNesting() == [
      x: [a: 1, b: 3],
      y: [a: 2, b: 4]
    ]
  }
}