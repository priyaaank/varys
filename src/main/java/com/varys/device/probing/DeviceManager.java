package com.varys.device.probing;

import com.varys.commandline.CommandExecutor;
import com.varys.commandline.CommandOutputStreamReader;
import com.varys.commandline.OutputListener;
import com.varys.commandline.ShellCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeviceManager implements OutputListener {

  private CommandOutputStreamReader commandOutputStreamReader;
  private ShellCommand deviceListCommand;
  private CommandExecutor commandExecutor;
  private DeviceClassifier deviceClassifier;
  List<Device> devices;


  public DeviceManager(CommandOutputStreamReader commandOutputStreamReader) {
    this.deviceClassifier = new DeviceClassifier();
    this.commandOutputStreamReader = commandOutputStreamReader;
    this.deviceListCommand = new ShellCommand("adb", new ShellCommand.CommandArgument("devices",ShellCommand.SINGLE_SPACE, "-l"));
    commandExecutor = new CommandExecutor(deviceListCommand, commandOutputStreamReader);
    commandOutputStreamReader.registerAsListener(this);
    devices = new ArrayList<Device>();
  }

  @Override
  public void commandOutputReceived(String output) {
    new DeviceListingParser().parseAndAdd(output);
  }

  public void refreshList() {
    try {

      Process process = commandExecutor.execute();
      process.waitFor();

    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public List<Device> attachedDevices() {
    return devices;
  }


  private class DeviceListingParser {

    void parseAndAdd(String deviceListingLog) {
      String[] lines = deviceListingLog.split("\n");
      for (String line : lines) {
        Device identifiedDevice = deviceClassifier.identify(line);
        if(identifiedDevice != null) devices.add(identifiedDevice);
      }
    }

  }

}
