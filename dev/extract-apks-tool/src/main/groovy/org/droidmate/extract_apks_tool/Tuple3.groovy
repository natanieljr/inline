package org.droidmate.extract_apks_tool

public class Tuple3<T1, T2, T3> extends Tuple
{

  public Tuple3(T1 first, T2 second, T3 third)
  {
    super([first, second, third])
  }

  public T1 getFirst()
  {
    return (T1) get(0)
  }

  public T2 getSecond()
  {
    return (T2) get(1)
  }

  public T3 getThird()
  {
    return (T3) get(2)
  }
}