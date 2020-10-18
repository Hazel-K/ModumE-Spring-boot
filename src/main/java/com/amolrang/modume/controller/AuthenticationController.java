package com.amolrang.modume.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.amolrang.modume.model.UserModel;
import com.amolrang.modume.service.UserService;
import com.amolrang.modume.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AuthenticationController {
	@Autowired
	private UserService userService;
	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model, Principal principal) {
		log.info("로그인페이지 GET접근 :{}", principal);
		model.addAttribute(StringUtils.TitleKey(), "로그인페이지");
		return StringUtils.LoginURLValue();
	}

	@RequestMapping(value = "/login_success", method = RequestMethod.GET)
	public String login_success(Model model, OAuth2AuthenticationToken authentication, RedirectAttributes ra) {
		log.info("로그인 성공 페이지 GET접근 :{}", authentication);
		model.addAttribute(StringUtils.TitleKey(), "로그인 성공 페이지");

		OAuth2AuthorizedClient client = authorizedClientService
				.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
		log.info("access token:{}", client.getAccessToken().getTokenValue());

		String userInfoEndpointUri = client.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
		log.info("userInfoEndpointUri:{}", userInfoEndpointUri);
		// api 유저정보 요청
		if (!StringUtils.isEmpty(userInfoEndpointUri)) {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());

			// 유저정보 조회
			HttpEntity entity = new HttpEntity(headers);
			ResponseEntity<Map> response = restTemplate.exchange(
					siteUrlCustom(authentication.getAuthorizedClientRegistrationId(), userInfoEndpointUri),
					HttpMethod.GET, entity, Map.class);
			Map userAttributes = response.getBody();

			log.info("response:{}", response);
			model.addAttribute("userInfo", userAttributes);
			ra.addFlashAttribute("userInfo", userAttributes);
		}
		return "redirect:/main";
	}

	protected String siteUrlCustom(String site, String baseUrl) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl);

		switch (site) {
		case "facebook":
			uriBuilder.queryParam("fields", "name,email,picture,locale");
			break;

		default:
			uriBuilder.query("");
		}

		return uriBuilder.toUriString();
	}

	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public String join(Model model) {
		log.info("회원가입 get 접근");
		model.addAttribute(StringUtils.TitleKey(), "회원가입페이지");
		model.addAttribute(StringUtils.LoginURLKey(), StringUtils.LoginURLValue());
		return StringUtils.JoinURLValue();
	}

	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public String joinAction(Model model, UserModel userModel) {
		log.info("회원가입 post 접근");
		userService.save(userModel, "ROLE_MEMBER");
		return "redirect:" + StringUtils.LoginURLValue();
	}

	@RequestMapping(value = "/denied")
	public String denied(Model model) {
		model.addAttribute(StringUtils.TitleKey(), "거부 당함");
		return StringUtils.Denied();
	}
}
