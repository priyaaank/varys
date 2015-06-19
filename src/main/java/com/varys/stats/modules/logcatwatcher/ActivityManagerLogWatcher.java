package com.varys.stats.modules.logcatwatcher;

import com.varys.commandline.ShellCommand;

public class ActivityManagerLogWatcher extends AndroidLogWatcher{

  @Override
  protected ShellCommand getLogCommand() {
    ShellCommand logCatCommand = super.getLogCommand();
    ShellCommand.CommandArgument activityManagerInfoLogs = new ShellCommand.CommandArgument("ActivityManager",":","I");
    ShellCommand.CommandArgument suppressOtherLogs = new ShellCommand.CommandArgument("*",":","S");
    logCatCommand.addArguments(activityManagerInfoLogs, suppressOtherLogs);

    return logCatCommand;
  }

}
