package com.varys.device.probing;

import com.varys.commandline.CommandOutputStreamReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.InputStream;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;

@RunWith(MockitoJUnitRunner.class)
public class DeviceFinderTest {

  private DeviceFinder deviceFinder;

  @Mock
  private CommandOutputStreamReader commandOutputStreamReader;

  @Before
  public void setUp() throws Exception {
    doNothing().when(commandOutputStreamReader).registerAsListener(any(DeviceFinder.class));
    doNothing().when(commandOutputStreamReader).handleCommandOutput(any(Process.class), any(InputStream.class));
    deviceFinder = new DeviceFinder(commandOutputStreamReader);
  }

  @Test
  public void shouldReturnAListIfEmulatorsWhenRunning() {
    //given
    StringBuilder commandLineOutput = new StringBuilder();
    commandLineOutput.append("List of devices attached\n")
        .append("emulator-5554          device product:sdk_phone_x86 model:Android_SDK_built_for_x86 device:generic_x86\n")
        .append("emulator-5558          device product:sdk_phone_x86 model:Android_SDK_built_for_x86 device:generic_x86\n");
    deviceFinder.commandOutputReceived(commandLineOutput.toString());

    //when
    List<Emulator> runningEmulators = deviceFinder.runningEmulators();

    //then
    assertEquals(2, runningEmulators.size());
  }

}