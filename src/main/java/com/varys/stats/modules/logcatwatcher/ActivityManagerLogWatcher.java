package com.varys.stats.modules.logcatwatcher;

import com.varys.commandline.ShellCommand;

public class ActivityManagerLogWatcher extends AndroidLogWatcher{

  @Override
  protected ShellCommand getLogCommand() {
    ShellCommand baseCommand = super.getLogCommand();
    ShellCommand.CommandArgument activityManagerFilter = new ShellCommand.CommandArgument("ActivityManager",":","I");
    ShellCommand.CommandArgument suppressOtherLogs = new ShellCommand.CommandArgument("*",":","S");
    baseCommand.addArguments(activityManagerFilter, suppressOtherLogs);

    return baseCommand;
  }

}
