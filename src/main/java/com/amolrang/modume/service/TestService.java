package com.amolrang.modume.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.amolrang.modume.mapper.TestMapper;
import com.amolrang.modume.model.ChatMessage;
import com.amolrang.modume.model.MessageType;

@Component
public class TestService {
	private static final Logger logger = LoggerFactory.getLogger(TestService.class);
	
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	
	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		logger.info("Received a new web socket connection");
	}
	
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headAccessor = StompHeaderAccessor.wrap(event.getMessage());
		
		String username = (String)headAccessor.getSessionAttributes().get("username");
		
		if(username != null) {
			logger.info("User Disconnected :" + username);
			
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setType(MessageType.LEAVE);
			chatMessage.setSender(username);
			
			messagingTemplate.convertAndSend("/topic/public",chatMessage);
		}
	}
	
	
//	@Autowired
//	private TestMapper mapper;
//
//	public String getMembers() {
//		return null;
//	}
}
