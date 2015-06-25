package com.varys.device.probing;

public class Emulator {

  private String name;
  private String serialNumber;
  private int runningOnPort;

  public Emulator(String name, String serialNumber, int runningOnPort) {
    this.name = name;
    this.serialNumber = serialNumber;
    this.runningOnPort = runningOnPort;
  }

  @Override
  public String toString() {
    return serialNumber;
  }
}
