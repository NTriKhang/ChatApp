package com.example.ChatApp.Config;

import org.springframework.context.annotation.Configuration;

import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		System.out.println("init connect");
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
	@Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
		System.out.println("on send socket");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
		registry.enableSimpleBroker("/user"); 
    }      


}
