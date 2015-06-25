package com.varys.eventhub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventHub {

  private static EventHub singleInstance;
  private Map<EventType, List<EventListener>> listeners;

  private EventHub() {
    listeners = new HashMap<EventType, List<EventListener>>();
  }

  public static EventHub getInstance() {
    if(singleInstance == null) {
      singleInstance = new EventHub();
    }
    return singleInstance;
  }

  public void registerPublisher(EventPublisher publisher) {
    if(publisher == null) return;

    publisher.registerHubAsListener(this);
  }

  public void deRegisterPublisher(EventPublisher publisher) {
    if(publisher == null) return;

    publisher.deRegisterHubAsListener(this);
  }

  public <T> void publishMessage(EventType eventType, Message<T> message) {
    if(message == null || listeners == null ||
        !listeners.containsKey(eventType) || listeners.get(eventType) == null ||
        listeners.get(eventType).size() == 0) return;

    for(EventListener listener : listeners.get(eventType)) {
      listener.received(message);
    }
  }

  public void registerFor(EventType eventType, EventListener listener) {
    if(listener == null) return;
    if(eventType == EventType.ALL) {
      registerForAllEvents(listener);
      return;
    }

    registerForSingleEvent(eventType, listener);
  }

  public void deRegisterFor(EventType eventType, EventListener listener) {
    if(listener == null) return;
    if(eventType == EventType.ALL) {
      deRegisterForAllEvents(listener);
      return;
    }

    deRegisterForSingleEvent(eventType, listener);
  }

  private void deRegisterForAllEvents(EventListener listener) {
    for(EventType eventType : EventType.allEventTypes()) {
      deRegisterForSingleEvent(eventType, listener);
    }
  }

  private void registerForAllEvents(EventListener listener) {
    for(EventType eventType : EventType.allEventTypes()) {
      registerForSingleEvent(eventType, listener);
    }
  }

  private void deRegisterForSingleEvent(EventType eventType, EventListener listener) {
    if(listeners.get(eventType) == null || listeners.get(eventType).size() == 0) return;
    if(listeners.get(eventType).contains(listener)) listeners.get(eventType).remove(listener);
  }

  private void registerForSingleEvent(EventType eventType, EventListener listener) {
    if(listeners.get(eventType) == null) listeners.put(eventType, new ArrayList<EventListener>());
    if(!listeners.get(eventType).contains(listener)) listeners.get(eventType).add(listener);
  }

}
