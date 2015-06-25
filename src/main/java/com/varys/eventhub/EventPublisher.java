package com.varys.eventhub;

public interface EventPublisher {

  void registerHubAsListener(EventHub hub);

  void deRegisterHubAsListener(EventHub eventHub);
}
