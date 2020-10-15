package com.amolrang.modume.config;

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
		log.info("권한접근승인여부");
		auth.userDetailsService(userService).passwordEncoder(encoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		//로그아웃추가
		http.logout().invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutSuccessUrl("/");
		
		//권한 필요한 경로 추가
		// /admin 주소는 ADMIN 권한을 가진 사람만 접속 가능
		http.authorizeRequests().antMatchers("/admin").hasRole("ADMIN"); 
		
		//권한 필요없는 경로 추가
		// permitAll은 모든 권한 다 됨
		//
		http.formLogin().loginPage("/login").defaultSuccessUrl("/").loginProcessingUrl("/loginAction").permitAll();
		http.authorizeRequests().antMatchers("/main").permitAll();
		http.authorizeRequests().antMatchers("/join").permitAll();
		http.authorizeRequests().antMatchers("/").permitAll();
		
		//권한없이 접근한 페이지로 보내는 곳
		http.exceptionHandling().accessDeniedPage("/denied");
	}
	
	// 패스워드 암호화
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
