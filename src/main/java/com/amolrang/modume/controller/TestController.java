package com.amolrang.modume.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.InputStreamEditor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amolrang.modume.config.DatabaseCoinfiguration;
import com.amolrang.modume.mapper.UserMapper;
import com.amolrang.modume.model.ChatMessage;
import com.amolrang.modume.model.UserModel;
import com.amolrang.modume.service.TestService;
import com.amolrang.modume.service.UserService;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class TestController {

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
	
	
	@Autowired
	private TestService service;
	
	@Autowired
	UserService userService;

	@Autowired
	UserMapper userMapper;
	
	
	//ADMIN 계정 부여
	@GetMapping("/create")
	public UserModel create(){
		UserModel userModel = new UserModel();
		userModel.setId("admin");
		userModel.setPassword("1234");
		userService.save(userModel, "ROLE_ADMIN");
		return userModel;
	}

	@RequestMapping(value = "/admin", produces = "text/plain;charset=UTF-8")
	public String admin() {
		String result ="";
		result = "권한접근";
		return String.format("%s", result);
	}
}
