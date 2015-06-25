package com.varys.eventhub;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static com.varys.eventhub.EventType.ACTIVITY_MANAGER_LOG;
import static com.varys.eventhub.EventType.ALL;
import static com.varys.eventhub.EventType.ANDROID_LOG;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EventHubTest {

  private EventHub hub;

  @Mock
  private EventPublisher publisher;

  @Mock
  private EventListener listener;

  @Before
  public void setUp() throws Exception {
    hub = EventHub.getInstance();
  }

  @Test
  public void shouldRegisterANewPublisher() throws Exception {
    //when
    hub.registerPublisher(publisher);

    //then
    verify(publisher).registerHubAsListener(hub);
  }

  @Test
  public void shouldDeRegisterARegisteredPublisher() throws Exception {
    //when
    hub.deRegisterPublisher(publisher);

    //then
    verify(publisher).deRegisterHubAsListener(hub);
  }

  @Test
  public void shouldBroadcastMessageToRegisteredListener() throws Exception {
    //given
    hub.registerFor(ACTIVITY_MANAGER_LOG, listener);

    //when
    Message<FakeMessage> message = new Message<FakeMessage>();
    hub.publishMessage(ACTIVITY_MANAGER_LOG, message);

    //then
    verify(listener).received(message);
  }


  @Test
  public void shouldBroadcastMessageToMultipleRegisteredListener() throws Exception {
    //given
    EventListener additionalListener = mock(EventListener.class);
    hub.registerFor(ACTIVITY_MANAGER_LOG, listener);
    hub.registerFor(ACTIVITY_MANAGER_LOG, additionalListener);

    //when
    Message<FakeMessage> message = new Message<FakeMessage>();
    hub.publishMessage(ACTIVITY_MANAGER_LOG, message);

    //then
    verify(listener).received(message);
    verify(additionalListener).received(message);
  }

  @Test
  public void shouldNotBroadcastMessageToListenerThatHasDeRegistered() throws Exception {
    //given
    hub.registerFor(ACTIVITY_MANAGER_LOG, listener);
    hub.deRegisterFor(ACTIVITY_MANAGER_LOG, listener);

    //when
    Message<FakeMessage> message = new Message<FakeMessage>();
    hub.publishMessage(ACTIVITY_MANAGER_LOG, message);

    //then
    verify(listener, never()).received(message);
  }

  @Test
  public void shouldNotAllowAMessageToBeBroadcastToAllEventsDirectly() throws Exception {
    //given
    hub.registerFor(ALL, listener);

    //when
    Message<FakeMessage> message = new Message<FakeMessage>();
    hub.publishMessage(ALL, message);

    //then
    verify(listener, never()).received(message);
  }

  @Test
  public void shouldNotPublishMessagesThatAreNull() throws Exception {
    //given
    hub.registerFor(ACTIVITY_MANAGER_LOG, listener);

    //when
    hub.publishMessage(ACTIVITY_MANAGER_LOG, null);

    //then
    verify(listener, never()).received(Matchers.<Message<FakeMessage>>any());
  }

  @Test
  public void shouldNotPublishMessagesToSubscribersOfOtherEvents() throws Exception {
    //given
    hub.registerFor(ACTIVITY_MANAGER_LOG, listener);

    //when
    Message<FakeMessage> message = new Message<FakeMessage>();
    hub.publishMessage(ANDROID_LOG, message);

    //then
    verify(listener, never()).received(message);
  }

  class FakeMessage {
    String message = "message";
  }

}