package com.amolrang.modume.utils;

import org.springframework.lang.Nullable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtils extends org.springframework.util.StringUtils{
	//타이틀 화면 키,
	public final static String TitleKey() {return "title";}
	
	//로그인 화면 키,값
	public final static String LoginURLKey() {return "login";}
	public final static String LoginURLValue() {return "/login";}
	
	//회원가입 화면 키,값
	public final static String JoinURLKey() {return "join";}
	public final static String JoinURLValue() {return "/join";}

	//거부 당한 경로,
	public final static String Denied() {return "/denied";}
}
