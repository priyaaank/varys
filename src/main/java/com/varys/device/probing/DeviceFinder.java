package com.varys.device.probing;

import com.varys.commandline.CommandExecutor;
import com.varys.commandline.CommandOutputStreamReader;
import com.varys.commandline.OutputListener;
import com.varys.commandline.ShellCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeviceFinder implements OutputListener {

  private CommandOutputStreamReader commandOutputStreamReader;
  private ShellCommand deviceListCommand;
  private CommandExecutor commandExecutor;
  List<Emulator> emulators;

  public DeviceFinder(CommandOutputStreamReader commandOutputStreamReader) {
    this.commandOutputStreamReader = commandOutputStreamReader;
    this.deviceListCommand = new ShellCommand("adb", new ShellCommand.CommandArgument("devices",ShellCommand.SINGLE_SPACE, "-l"));
    commandExecutor = new CommandExecutor(deviceListCommand, commandOutputStreamReader);
    commandOutputStreamReader.registerAsListener(this);
    emulators = new ArrayList<Emulator>();
  }

  @Override
  public void commandOutputReceived(String output) {
    new DeviceListingParser().parse(output);
    for (Emulator emulator : runningEmulators()) {
      System.out.println(emulator.toString());
    };
  }

  public void probe() {
    try {

      Process process = commandExecutor.execute();
      process.waitFor();

    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public List<Emulator> runningEmulators() {
    return emulators;
  }

  public enum DeviceType {
    EMULATOR,
    DEVICE,
    UNKNOWN
  }

  private class DeviceListingParser {

    void parse(String deviceListingLog) {
      String[] lines = deviceListingLog.split("\n");
      for (String line : lines) {
        DeviceType deviceType = getDeviceType(line);
        addDeviceFromLine(deviceType, line);
      }
    }

    private void addDeviceFromLine(DeviceType deviceType, String line) {
      switch(deviceType) {
        case EMULATOR:
          emulators.add(convertLineToEmulator(line));
          break;
        case DEVICE:
          break;
        default:
          break;
      }
    }

    private Emulator convertLineToEmulator(String emulatorInfo) {
      String[] emulatorDetails = emulatorInfo.split(" ");
      String serialNumber = emulatorDetails[0];
      String name = serialNumber;
      int portRunningOn = Integer.valueOf(serialNumber.split("-")[1]);

      return new Emulator(name, serialNumber, portRunningOn);
    }

    private DeviceType getDeviceType(String line) {
      if(line == null || line.trim().length() == 0) return DeviceType.UNKNOWN;
      if(line.startsWith("emulator")) return DeviceType.EMULATOR;
      return DeviceType.UNKNOWN;
    }

  }

  public static void main(String[] args) {
    CommandOutputStreamReader commandOutputReader = new CommandOutputStreamReader();
    DeviceFinder deviceFinder = new DeviceFinder(commandOutputReader);
    deviceFinder.probe();
  }
}
