package com.varys.stats.modules.logcatwatcher;

import com.varys.commandline.CommandExecutor;
import com.varys.commandline.CommandOutputHandler;
import com.varys.commandline.OutputListener;
import com.varys.commandline.ShellCommand;
import com.varys.eventhub.EventHub;
import com.varys.eventhub.EventPublisher;
import com.varys.eventhub.EventType;
import com.varys.eventhub.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AndroidLogWatcher implements EventPublisher {

  private EventHub eventHub;

  public void watch() {
    CommandExecutor executor = new CommandExecutor(getLogCommand(), getLogHandler());
    try {

      executor.execute();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected ShellCommand getLogCommand() {
    return new ShellCommand("adb", new ShellCommand.CommandArgument("logcat"));
  }

  protected CommandOutputHandler getLogHandler() {
    return new LogBroadcaster();
  }

  @Override
  public void registerHubAsListener(EventHub hub) {
    this.eventHub = hub;
  }

  @Override
  public void deRegisterHubAsListener(EventHub eventHub) {
    this.eventHub = null;
  }

  protected EventType getEventType() {
    return EventType.ANDROID_LOG;
  }

  private class LogBroadcaster implements CommandOutputHandler {

    @Override
    public void handleCommandOutput(Process commandProcess, InputStream commandInputStream) {
      BufferedReader reader = null;
      EventHub.getInstance().registerPublisher(AndroidLogWatcher.this);
      try {
        reader = new BufferedReader(new InputStreamReader(commandInputStream));
        logAllCommandOutput(reader);
      } catch (IOException ioException) {
        ioException.printStackTrace();
      } finally {
        closeReader(reader);
        EventHub.getInstance().deRegisterPublisher(AndroidLogWatcher.this);
      }
    }

    private BufferedReader logAllCommandOutput(BufferedReader reader) throws IOException {
      String line = null;
      EventType eventType = getEventType();
      while ((line = reader.readLine()) != null) {
        eventHub.publishMessage(eventType, new Message<String>(eventType, line));
      }
      return reader;
    }

    private void closeReader(BufferedReader reader) {
      if(reader != null) {
        try {
          reader.close();
        } catch (IOException ioEx) {
          ioEx.printStackTrace();
        }
      }
    }
  }
}
