package com.amolrang.modume.api;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.amolrang.modume.model.UserModel;
import com.amolrang.modume.service.UserService;
import com.amolrang.modume.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

@Slf4j
@Service
public class CallApi {
	@Autowired
	private UserService userService;
	public Map CallUserInfoToJson(OAuth2AuthenticationToken authentication,OAuth2AuthorizedClientService auth2AuthorizedClientService) {
		OAuth2AuthorizedClient client = auth2AuthorizedClientService
				.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
		log.info("access token:{}", client.getAccessToken().getTokenValue());
		String userInfoEndpointUri = client.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
		
		if (!StringUtils.isEmpty(userInfoEndpointUri)) {
			
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());

			// 유저정보 조회
			HttpEntity entity = new HttpEntity(headers);
			ResponseEntity<Map> response = restTemplate.exchange(
					StringUtils.siteUrlCustom(authentication.getAuthorizedClientRegistrationId(), userInfoEndpointUri),
					HttpMethod.GET, entity, Map.class);

			log.info("response:{}", response);
			log.info("userInfo{}", response.getBody());
			
			Map userInfo = response.getBody();
			
			switch(authentication.getAuthorizedClientRegistrationId()) {
			case "naver":
				Map naverUserInfo = (Map)userInfo.get("response");
				log.info("userinfo_id :{}", naverUserInfo.get("id"));
				log.info("userinfo_nickname :{}", naverUserInfo.get("nickname"));
				log.info("userinfo_email :{}", naverUserInfo.get("email"));
				//json obj
				//obj => 추출
				//return할때 json으로 정리해서 보내기.
				return null;
			case "kakao":
				Map kakaoInfo = (Map)userInfo.get("kakao_account");
				Map kakaoUserInfo = (Map)kakaoInfo.get("profile");
				log.info("userinfo_id :{}", userInfo.get("id"));
				log.info("userinfo_nickname :{}", kakaoUserInfo.get("nickname"));
				log.info("userinfo_email :{}", kakaoInfo.get("email"));
				return null;
			case "google":
				log.info("userinfo_id :{}", userInfo.get("sub"));
				log.info("userinfo_name :{}", userInfo.get("name"));
				log.info("userinfo_email :{}", userInfo.get("email"));
				return null;
			}
			return response.getBody();
		}
		return null;
	}
}
