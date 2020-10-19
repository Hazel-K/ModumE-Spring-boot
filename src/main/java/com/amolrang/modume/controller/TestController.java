package com.amolrang.modume.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amolrang.modume.model.ChatMessage;
import com.amolrang.modume.service.TestService;
import com.amolrang.modume.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class TestController {
	
	@Autowired
	private TestService service;
	
	@RequestMapping(value = "/test", produces="text/plain;charset=UTF-8")
	public String test() {
		String result = "test";
		log.info(result);
		return String.format("%s", result);
	}
	
	@Autowired
	UserService userService;

	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/public")
	public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
		//log로 데이터를 뽑고 싶을때 {} 꼭 쓰기
		log.info("chatMessage : {}" + chatMessage.toString());
		return chatMessage;
	}
	
	@MessageMapping("/chat.addUser")
	@SendTo("/topic/public")
	public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor ) {
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		log.info("chatMessage : {}" + chatMessage.toString());
		return chatMessage;
	}
 }
