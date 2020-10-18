package com.amolrang.modume.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import com.amolrang.modume.oauth.CommonOAuth2Provider;
import com.amolrang.modume.oauth.CustomOAuth2Provider;
import com.amolrang.modume.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/favicon.ico");
		web.ignoring().antMatchers("/css/**");
		web.ignoring().antMatchers("/js/**");
		web.ignoring().antMatchers("/img/**");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(encoder());
		log.info("auth:{}",auth);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		// 로그아웃추가
		http.logout().invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutSuccessUrl("/main");
		// 권한 필요한 경로 추가
		http.authorizeRequests().antMatchers("/admin").hasRole("ADMIN");

		// 권한 필요없는 경로 추가
		http.formLogin().loginPage("/login").usernameParameter("user_id").defaultSuccessUrl("/main")
				.permitAll();
		http.authorizeRequests().antMatchers("/main").permitAll();
		http.authorizeRequests().antMatchers("/join").permitAll();
		http.authorizeRequests().antMatchers("/").permitAll();
		http.authorizeRequests().antMatchers("/login").permitAll();
		
		http.authorizeRequests().antMatchers("/login/oauth2/**").permitAll()
		.and()
		.oauth2Login().loginPage("/login").defaultSuccessUrl("/login_success")
		.clientRegistrationRepository(clientRegistrationRepository())
		.authorizedClientService(authorizedClientService());
		// 권한없이 접근한 페이지로 보내는 곳
		http.exceptionHandling().accessDeniedPage("/denied");
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    public OAuth2AuthorizedClientService authorizedClientService(){
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
    }
	
	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {
		List<ClientRegistration> registrations = new ArrayList<>();
		registrations.add(CommonOAuth2Provider.GOOGLE.getBuilder("google").build());
		registrations.add(CustomOAuth2Provider.KAKAO.getBuilder("kakao").build());
		registrations.add(CommonOAuth2Provider.TWITCH.getBuilder("twitch").build());
		registrations.add(CustomOAuth2Provider.NAVER.getBuilder("naver").build());
		log.info("registrations:{}"+registrations);
		return new InMemoryClientRegistrationRepository(registrations);
	}
}
