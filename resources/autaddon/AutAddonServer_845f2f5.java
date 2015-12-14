/*
 Copyright (c) 2013 Saarland University Software Engineering Chair. All right reserved.

 Author: Konrad Jamrozik, jamrozik@st.cs.uni-saarland.de

 This file is part of the "DroidMate" project.

 www.droidmate.org
 */

package org.droidmate.autaddon;

import org.droidmate.common.CommonUtils;
import org.droidmate.common.Constants;
import org.droidmate.common.DeviceCommand;
import org.droidmate.common.DeviceResponse;
import org.droidmate.common.SerializableTCPServerBase;

import android.util.Log;

public class AutAddonServer extends SerializableTCPServerBase<DeviceCommand, DeviceResponse> {

  private IAutDriver autDriver;

  private final static String thisClassName = CommonUtils.getClassNameSuffix(AutAddonServer.class.getName());

  public AutAddonServer(IAutDriver autDriver) {
    super(Constants.AUTADDON_SERVER_START_TAG, Constants.AUTADDON_SERVER_START_MSG);
    this.autDriver = autDriver;
  }

  @Override
  protected DeviceResponse OnServerRequest(DeviceCommand deviceCommand, Exception serverBaseEx) {

    try {
      if (serverBaseEx != null)
        throw serverBaseEx;

      assert deviceCommand != null;

      return autDriver.executeCommand(deviceCommand);
    } catch (AutAddonException e)
    {
      Log.e(thisClassName, String.format("Failed to execute command %s and thus, obtain appropriate DeviceResponse. " +
        "Returning exception-DeviceResponse.", deviceCommand), e);

      DeviceResponse exceptionDeviceResponse = new DeviceResponse();
      exceptionDeviceResponse.throwable = e;

      return exceptionDeviceResponse;
    } catch (Throwable t)
    {
      Log.wtf(thisClassName, String.format(
        "Failed, with a non-"+AutAddonException.class.getSimpleName()+" (!), to execute command %s and thus, " +
          "obtain appropriate DeviceResponse. Returning throwable-DeviceResponse.", deviceCommand), t);

      DeviceResponse throwableDeviceResponse = new DeviceResponse();
      throwableDeviceResponse.throwable = t;

      return throwableDeviceResponse;
    }
  }

  @Override
  protected boolean shouldCloseServerSocket(DeviceCommand deviceCommand) {

    return deviceCommand == null || deviceCommand.command.equals(Constants.DEVICE_COMMAND_STOP_AUTADDON);

  }

}
