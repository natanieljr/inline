package org.droidmate.extract_apks_tool

import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam
import groovy.transform.stc.FromString

class IterableAndMapExtensions
{

  public static <T> T findSingle(Iterable<T> self, @ClosureParams(FirstParam.FirstGenericType.class) Closure closure)
  {
    def result = self.findAll(closure)
    assert result.size() == 1
    return result[0]
  }

  public static <T> T findSingle(Iterable<T> self)
  {
    assert self.size() == 1
    return self[0]
  }

  public static <T> List<T> shallowFlatten(Iterable<Iterable<T>> self)
  {
    return self.inject([] as List<T>) {List<T> flattened, Iterable<T> item -> flattened + item}
  }

  public static <T> boolean isSortedBy(Iterable<T> self,
                                       @ClosureParams(FirstParam.FirstGenericType.class) Closure<Comparable> closure)
  {
    if (self.size() == 0)
      return true

    Iterable<Comparable> comparableList = self.collect {closure(it)}

    boolean result = true
    comparableList.inject {Comparable prev, Comparable curr ->
      if (prev.compareTo(curr) > 0)
        result = false
      return curr
    }

    return result
  }

  public static <T> boolean noDuplicates(Iterable<T> self,
                                         @ClosureParams(value = FromString.class, options = ["T", "T,T"]) Closure condition)
  {
    return self.size() == self.toUnique(condition).size()
  }

  public static <K1, K2, E> Map<K2, Map<K1, E>> flipKeysNesting(Map<K1, Map<K2, E>> self)
  {
    // Running example.
    // self:
    // a: [x: 1, y: 2]
    // b: [x: 3, y: 4]

    Collection<Map<K2, Map<K1, E>>> coll = self.collect {k1, nestedMap ->
      (Map<K2, Map<K1, E>>) nestedMap.collectEntries {k2, e -> [(k2): [(k1): e]]}
    }
    // coll:
    // x: [a: 1]
    // y: [a: 2]
    // x: [b: 3]
    // y: [b: 4]

    Map<K2, Collection<Map<K1,E>>> map = coll.groupValues()
    // map:
    // x: [[a: 1], [b: 3]]
    // y: [[a: 2], [b: 4]]

    (Map<K2, Map<K1,E>>) map.collectEntries { k2, collOfMap ->
      [(k2): collOfMap.collectEntries { it }]
    }
    // x: [a: 1, b: 3]
    // y: [a: 2, b: 4]

  }

  public static <K, E> Map<K, Collection<E>> mapFrom(Iterable<E> self,
                                                     @ClosureParams(FirstParam.FirstGenericType.class) Closure<Set<K>> extractKeys)
  {
    List<Map<K, E>> list = self.collect {E e ->
      extractKeys(e).collectEntries {K key -> [key, e]} as Map<K, E>
    }
    .findAll {!it.isEmpty()}

    list.each {Map<K, E> map -> assert map.values().unique(false).size() == 1}

    Map<K, Collection<E>> map = list.groupValues()

    return map
  }

  public static <K, E> Map<K, Collection<E>> groupValues(Iterable<Map<K, E>> self)
  {
    self.inject([:] as Map<K, Collection<E>>) {Map<K, Collection<E>> mergedMap, Map<K, E> map ->

      map.each {K key, E e ->

        mergedMap.putIfAbsent(key, [])
        mergedMap.get(key).add(e)
      }

      mergedMap
    }
  }

}
