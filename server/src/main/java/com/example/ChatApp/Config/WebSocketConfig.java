package com.example.ChatApp.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import jakarta.annotation.PostConstruct;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		System.out.println("init connect");
        registry.addEndpoint("/initSocket").setAllowedOriginPatterns("*").withSockJS();
    }
	@Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
		System.out.println("on send socket");
        registry.setApplicationDestinationPrefixes("/socket");
        registry.setUserDestinationPrefix("/user");
        registry.enableSimpleBroker("/chatroom");
    }

    @Autowired
  private MappingMongoConverter mongoConverter;

  @PostConstruct
  public void setUpMongoEscapeCharacterAndTypeMapperConversion() {
      mongoConverter.setMapKeyDotReplacement("_");
      
      // This will remove _class: key
      mongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
  }


}
