/*
 Copyright (c) 2013 Saarland University Software Engineering Chair. All right reserved.

 Author: Konrad Jamrozik, jamrozik@st.cs.uni-saarland.de

 This file is part of the "DroidMate" project.

 www.droidmate.org
 */

package org.droidmate.autaddon;

import android.app.Activity;
import org.droidmate.common.CommonUtils;
import org.droidmate.common.Constants;
import org.droidmate.common.DeviceCommand;
import org.droidmate.common.DeviceResponse;

@SuppressWarnings("UnusedDeclaration")
public class AutDriver implements IAutDriver
{

  private static final String thisClassName = CommonUtils.getClassNameSuffix(AutDriver.class.getName());

  @SuppressWarnings("FieldCanBeLocal")
  private final Activity activity;

  public AutDriver(Activity activity)
  {
    this.activity = activity;
  }

  @Override
  public DeviceResponse executeCommand(DeviceCommand deviceCommand) throws AutAddonException
  {

    if (deviceCommand.command.equals(Constants.DEVICE_COMMAND_STOP_AUTADDON))
      // The server will be closed after this response is sent.
      return new DeviceResponse();

    throw new AutAddonException(String.format("The command %s is not implemented yet!", deviceCommand.command));
  }

}
