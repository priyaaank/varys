package com.varys.device.probing;

public class Device {

  private String serialNumber;

  public Device(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  @Override
  public String toString() {
    return serialNumber;
  }
}
