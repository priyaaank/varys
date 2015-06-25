package com.varys.eventhub;

public class Message<T> {

  public EventType type;
  public T messageObject;

}
