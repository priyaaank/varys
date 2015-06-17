package com.varys.stats.modules.logcatwatcher;

import com.varys.commandline.CommandExecutor;
import com.varys.commandline.ShellCommand;

import java.io.IOException;

public class AndroidLogWatcher {

  private ShellCommand logCommand = new ShellCommand("adb", new ShellCommand.CommandArgument("logcat"));
  private CommandExecutor executor;

  public AndroidLogWatcher() {
    executor = new CommandExecutor(logCommand);
  }

  public void run() {
    try {

      executor.execute();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    new AndroidLogWatcher().run();
  }

}
