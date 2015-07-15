package com.varys.eventhub;

import java.util.ArrayList;
import java.util.List;

public enum EventType {
  ALL,
  ACTIVITY_MANAGER_LOG,
  ANDROID_LOG, DEVICE_LISTING;


  public static List<EventType> allEventTypes() {
    List<EventType> allEventTypes = new ArrayList<EventType>();
    for(EventType eventType : values()) {
      if(eventType != ALL) allEventTypes.add(eventType);
    }
    return allEventTypes;
  }
}
