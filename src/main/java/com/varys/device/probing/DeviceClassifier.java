package com.varys.device.probing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceClassifier {

  private final String emulatorRegex = "(emulator-\\d{4})\\s+.*";
  private final String genymotionRegex = "(\\d{2,3}\\.\\d{2,3}\\.\\d{2,3}\\.\\d{2,3}:\\d{4})\\s+.*";
  private final String physicalDeviceRegex = "([0-9a-fA-F]{16})\\s+.*";

  public Device identify(String deviceListingOutput) {
    DeviceCreator creator = null;
    if (matchesWithPattern(physicalDeviceRegex, deviceListingOutput)) creator = new PhysicalDeviceCreator();
    if (matchesWithPattern(emulatorRegex, deviceListingOutput)) creator = new EmulatorCreator();
    if (matchesWithPattern(genymotionRegex, deviceListingOutput)) creator = new GenymotionCreator();

    if (creator == null) return null;

    return creator.createFromEntry(deviceListingOutput);
  }

  private boolean matchesWithPattern(String devicePatternToMatch, String deviceListingOutput) {
    Pattern devicePattern = Pattern.compile(devicePatternToMatch);
    Matcher emulatorMatcher = devicePattern.matcher(deviceListingOutput);
    return emulatorMatcher.matches();
  }

  private interface DeviceCreator {
    Device createFromEntry(String deviceEntry);
  }

  private class EmulatorCreator implements DeviceCreator {

    @Override
    public Device createFromEntry(String deviceEntry) {
      Pattern devicePattern = Pattern.compile(emulatorRegex);
      Matcher emulatorMatcher = devicePattern.matcher(deviceEntry);

      if (emulatorMatcher.matches()) {
        String deviceSerialNumber = emulatorMatcher.group(1);
        return new Device(deviceSerialNumber);
      }
      return null;
    }

  }

  private class GenymotionCreator implements DeviceCreator {

    @Override
    public Device createFromEntry(String deviceEntry) {
      Pattern devicePattern = Pattern.compile(genymotionRegex);
      Matcher emulatorMatcher = devicePattern.matcher(deviceEntry);
      if (emulatorMatcher.matches()) {
        String deviceSerialNumber = emulatorMatcher.group(1);
        return new Device(deviceSerialNumber);
      }
      return null;
    }

  }

  private class PhysicalDeviceCreator implements DeviceCreator {

    @Override
    public Device createFromEntry(String deviceEntry) {
      Pattern devicePattern = Pattern.compile(physicalDeviceRegex);
      Matcher emulatorMatcher = devicePattern.matcher(deviceEntry);

      if (emulatorMatcher.matches()) {
        String deviceSerialNumber = emulatorMatcher.group(1);
        return new Device(deviceSerialNumber);
      }
      return null;
    }

  }


}
