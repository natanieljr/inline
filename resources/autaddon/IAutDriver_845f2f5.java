/*
 Copyright (c) 2013 Saarland University Software Engineering Chair. All right reserved.

 Author: Konrad Jamrozik, jamrozik@st.cs.uni-saarland.de

 This file is part of the "DroidMate" project.

 www.droidmate.org
 */

package org.droidmate.autaddon;

import org.droidmate.common.DeviceCommand;
import org.droidmate.common.DeviceResponse;

public interface IAutDriver {

  public DeviceResponse executeCommand(DeviceCommand deviceCommand) throws AutAddonException;

}
