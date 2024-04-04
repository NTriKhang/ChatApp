/*
 * package com.example.ChatApp.Event;
 * 
 * import org.springframework.context.event.EventListener; import
 * org.springframework.messaging.simp.SimpMessageHeaderAccessor; import
 * org.springframework.web.socket.messaging.SessionConnectEvent;
 * 
 * public class PresenceEventListener {
 * 
 * @EventListener private void handleSessionConnected(SessionConnectEvent event)
 * { SimpMessageHeaderAccessor headers =
 * SimpMessageHeaderAccessor.wrap(event.getMessage()); String username =
 * headers.getUser().getName();
 * 
 * 
 * LoginEvent loginEvent = new LoginEvent(username);
 * messagingTemplate.convertAndSend(loginDestination, loginEvent);
 * 
 * 
 * // We store the session as we need to be idempotent in the disconnect event
 * processing participantRepository.add(headers.getSessionId(), loginEvent); }
 * 
 * }
 */