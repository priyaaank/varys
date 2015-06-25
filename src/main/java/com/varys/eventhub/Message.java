package com.varys.eventhub;

public class Message<T> {

  public EventType type;
  public T messageObject;

  public Message(EventType eventType, T messageObject) {
    this.type = eventType;
    this.messageObject = messageObject;
  }

}
