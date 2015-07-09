package com.varys.device.probing;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeviceClassifierTest {

  private DeviceClassifier deviceClassifier;

  @Before
  public void setUp() throws Exception {
    deviceClassifier = new DeviceClassifier();
  }

  @Test
  public void shouldIdentifyAnEmulator() throws Exception {
    String deviceListingOutput = "emulator-5554   device";

    Device device = deviceClassifier.identify(deviceListingOutput);

    assertEquals("emulator-5554", device.toString());
  }

  @Test
  public void shouldIdentifyAGenymotionEmulator() throws Exception {
    String deviceListingOutput = "192.168.132.12:5554   device";

    Device device = deviceClassifier.identify(deviceListingOutput);

    assertEquals("192.168.132.12:5554", device.toString());
  }

  @Test
  public void shouldIdentifyAPhysicalDevice() throws Exception {
    String deviceListingOutput = "08bdd7ce031f9362    device";

    Device device = deviceClassifier.identify(deviceListingOutput);

    assertEquals("08bdd7ce031f9362", device.toString());
  }

  @Test
  public void shouldReturnNullWhenDeviceCantBeIdentified() throws Exception {
    String deviceListingOutput = "blahblahblah    device";

    Device device = deviceClassifier.identify(deviceListingOutput);

    assertNull(device);
  }
}