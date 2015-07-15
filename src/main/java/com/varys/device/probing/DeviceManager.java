package com.varys.device.probing;

import com.varys.commandline.CommandExecutor;
import com.varys.commandline.CommandOutputStreamReader;
import com.varys.commandline.OutputListener;
import com.varys.commandline.ShellCommand;
import com.varys.eventhub.EventHub;
import com.varys.eventhub.EventPublisher;
import com.varys.eventhub.EventType;
import com.varys.eventhub.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeviceManager implements OutputListener, EventPublisher {

  private ShellCommand deviceListCommand;
  private CommandExecutor commandExecutor;
  private DeviceClassifier deviceClassifier;
  private EventHub eventHub;
  List<Device> devices;
  private EventHub listener;


  public DeviceManager(CommandOutputStreamReader commandOutputStreamReader) {
    this.deviceClassifier = new DeviceClassifier();
    this.deviceListCommand = new ShellCommand("adb", new ShellCommand.CommandArgument("devices",ShellCommand.SINGLE_SPACE, "-l"));
    commandExecutor = new CommandExecutor(deviceListCommand, commandOutputStreamReader);
    commandOutputStreamReader.registerAsListener(this);
    devices = new ArrayList<Device>();
    eventHub = EventHub.getInstance();
    eventHub.registerPublisher(this);
  }

  @Override
  public void commandOutputReceived(String output) {
    devices.clear();
    new DeviceListingParser().parseAndAdd(output);
    if(listener != null) {
      Message<List<Device>> deviceMessage = new Message<List<Device>>(EventType.DEVICE_LISTING, devices);
      listener.publishMessage(EventType.DEVICE_LISTING, deviceMessage);
    }
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

  @Override
  public void registerHubAsListener(EventHub hub) {
    this.listener = hub;
  }

  @Override
  public void deRegisterHubAsListener(EventHub eventHub) {
    this.listener = null;
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
