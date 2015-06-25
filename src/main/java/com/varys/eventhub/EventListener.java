package com.varys.eventhub;

public interface EventListener {

  <T> void received(Message<T> message);

}
