package com.varys.device.probing;

import com.varys.commandline.CommandOutputStreamReader;
import com.varys.eventhub.EventHub;
import com.varys.eventhub.EventType;
import com.varys.eventhub.Message;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EventHub.class)
public class DeviceManagerTest {

  private DeviceManager deviceManager;

  @Mock
  private CommandOutputStreamReader commandOutputStreamReader;

  @Mock
  private EventHub hub;

  @Before
  public void setUp() throws Exception {
    doNothing().when(commandOutputStreamReader).registerAsListener(any(DeviceManager.class));
    doNothing().when(commandOutputStreamReader).handleCommandOutput(any(Process.class), any(InputStream.class));
    deviceManager = new DeviceManager(commandOutputStreamReader);
  }

  @Test
  public void shouldReturnAListIfEmulatorsWhenRunning() {
    //given
    StringBuilder commandLineOutput = new StringBuilder();
    commandLineOutput.append("List of devices attached\n")
        .append("emulator-5554          device product:sdk_phone_x86 model:Android_SDK_built_for_x86 device:generic_x86\n")
        .append("emulator-5558          device product:sdk_phone_x86 model:Android_SDK_built_for_x86 device:generic_x86\n");
    deviceManager.commandOutputReceived(commandLineOutput.toString());

    //when
    List<Device> runningDevices = deviceManager.attachedDevices();

    //then
    assertEquals(2, runningDevices.size());
  }

  @Test
  public void shouldBroadcastDeviceListingOnceObtained() throws Exception {
    //given
    PowerMockito.mockStatic(EventHub.class);
    when(EventHub.getInstance()).thenReturn(hub);
    deviceManager.registerHubAsListener(hub);
    StringBuilder commandLineOutput = new StringBuilder();
    commandLineOutput.append("List of devices attached\n")
        .append("emulator-5554          device product:sdk_phone_x86 model:Android_SDK_built_for_x86 device:generic_x86\n")
        .append("emulator-5558          device product:sdk_phone_x86 model:Android_SDK_built_for_x86 device:generic_x86\n");
    deviceManager.commandOutputReceived(commandLineOutput.toString());
    ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);

    //when
    List<Device> runningDevices = deviceManager.attachedDevices();

    //then
    verify(hub).publishMessage(eq(EventType.DEVICE_LISTING), captor.capture());
    assertEquals(runningDevices, captor.getValue().messageObject);
  }
}