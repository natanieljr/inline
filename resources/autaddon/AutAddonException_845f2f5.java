/*
 Copyright (c) 2013 Saarland University Software Engineering Chair. All right reserved.

 Author: Konrad Jamrozik, jamrozik@st.cs.uni-saarland.de

 This file is part of the "DroidMate" project.

 www.droidmate.org
 */

package org.droidmate.autaddon;

public class AutAddonException extends Exception {

  private static final long serialVersionUID = 9105959176880297555L;

  public AutAddonException() {
    super();
  }

  public AutAddonException(String detailMessage, Throwable throwable) {
    super(detailMessage, throwable);
  }

  public AutAddonException(String detailMessage) {
    super(detailMessage);
  }

  public AutAddonException(Throwable throwable) {
    super(throwable);
  }

}
