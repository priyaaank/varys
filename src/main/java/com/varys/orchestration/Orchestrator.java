package com.varys.orchestration;

import com.varys.commandline.CommandOutputStreamReader;
import com.varys.device.probing.DeviceManager;

import java.util.Timer;
import java.util.TimerTask;

public class Orchestrator {

  DeviceManager deviceManager;
  private Timer deviceManagerTask;

  public Orchestrator() {
    deviceManager = new DeviceManager(new CommandOutputStreamReader());
    deviceManagerTask = new Timer();
  }

  public void beginMonitoring() {
    deviceManagerTask.schedule(getDeviceManagerTask(), 1000, 5000);
  }

  public void stopMonitoring() {
    deviceManagerTask.cancel();
  }

  private TimerTask getDeviceManagerTask() {
    return new TimerTask() {
      @Override
      public void run() {
        deviceManager.refreshList();
      }
    };
  }

}
