package com.varys.stats.modules.logcatwatcher;

import com.varys.eventhub.EventHub;
import com.varys.eventhub.EventListener;
import com.varys.eventhub.EventType;
import com.varys.eventhub.Message;

public class TestClass implements EventListener {

  public static void main(String[] args) {
    EventHub instance = EventHub.getInstance();
    TestClass listener = new TestClass();
    ActivityManagerLogWatcher activityManagerLogWatcher = new ActivityManagerLogWatcher();
    instance.registerFor(EventType.ANDROID_LOG, listener);
    activityManagerLogWatcher.watch();
  }

  @Override
  public <String> void received(Message<String> message) {
    System.out.println(message.messageObject);
  }
}
