package com.amolrang.modume.api;

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

import com.amolrang.modume.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CallApi {
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
			switch(authentication.getAuthorizedClientRegistrationId()) {
			case "naver":
				response.getBody();
				//json obj
				//obj => 추출
				//return할때 json으로 정리해서 보내기.
				return null;
			}
			return response.getBody();
		}
		return null;
	}
}
