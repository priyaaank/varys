package com.varys;

import com.varys.eventhub.EventHub;
import com.varys.eventhub.EventListener;
import com.varys.eventhub.EventType;
import com.varys.eventhub.Message;
import com.varys.stats.modules.logcatwatcher.ActivityManagerLogWatcher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CoreApp implements EventListener {

  public CoreApp() {

  }

  public static void main(String[] args) {
    ExecutorService executor = Executors.newFixedThreadPool(10);
    EventHub.getInstance().registerFor(EventType.ACTIVITY_MANAGER_LOG, new CoreApp());
    ActivityManagerLogWatcher activityManagerLogWatcher = new ActivityManagerLogWatcher();
    activityManagerLogWatcher.watch();

    executor.execute(new Executor("Alpha"));
    executor.execute(new Executor("Beta"));
  }

  @Override
  public <T> void received(Message<T> message) {
    System.out.println("From Core App: " + message.messageObject);
  }

  public static class Executor implements Runnable, EventListener{

    private String name;

    public Executor(String name) {
      this.name = name;
    }

    @Override
    public void run() {
      EventHub.getInstance().registerFor(EventType.ACTIVITY_MANAGER_LOG, this);
      try {
        while(true) {
          //do nothing
        }
      } finally {
        EventHub.getInstance().deRegisterFor(EventType.ACTIVITY_MANAGER_LOG, this);
      }
    }

    @Override
    public <String> void received(Message<String> message) {
      System.out.println("From Thread: [" + name + "]" + message.messageObject);
    }

  }




}
