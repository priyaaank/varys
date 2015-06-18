package com.varys.stats.modules.logcatwatcher;

public class TestClass implements AndroidLogWatcher.LogListener {

  @Override
  public void receivedLog(String logStatement) {
    System.out.println(logStatement);
  }

  public static void main(String[] args) {
    TestClass listener = new TestClass();
    ActivityManagerLogWatcher activityManagerLogWatcher = new ActivityManagerLogWatcher();
    activityManagerLogWatcher.registerListener(listener);
    activityManagerLogWatcher.watch();
  }

}
