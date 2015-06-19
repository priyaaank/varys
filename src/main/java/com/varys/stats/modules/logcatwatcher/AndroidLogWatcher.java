package com.varys.stats.modules.logcatwatcher;

import com.varys.commandline.CommandExecutor;
import com.varys.commandline.CommandOutputHandler;
import com.varys.commandline.OutputListener;
import com.varys.commandline.ShellCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AndroidLogWatcher {

  private List<LogListener> listeners;

  public AndroidLogWatcher() {
    listeners = new ArrayList<LogListener>();
  }

  protected List<LogListener> getListeners() {
    return this.listeners;
  }

  public void registerListener(LogListener listener) {
    if(!listeners.contains(listener)) listeners.add(listener);
  }

  public void removeListener(LogListener listener) {
    if(listeners.contains(listener)) listeners.remove(listener);
  }

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

  private class LogBroadcaster implements CommandOutputHandler {

    @Override
    public void handleCommandOutput(Process commandProcess, InputStream commandInputStream) {
      BufferedReader reader = null;
      try {
        reader = new BufferedReader(new InputStreamReader(commandInputStream));
        logAllCommandOutput(reader);
      } catch (IOException ioException) {
        ioException.printStackTrace();
      } finally {
        closeReader(reader);
      }
    }

    private BufferedReader logAllCommandOutput(BufferedReader reader) throws IOException {
      String line = null;
      while ((line = reader.readLine()) != null) {
        for (LogListener listener : getListeners()) {
          listener.receivedLog(line);
        }
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

  public static interface LogListener {

    void receivedLog(String logStatement);

  }
}
