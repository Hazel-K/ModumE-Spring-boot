package com.amolrang.modume.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.amolrang.modume.repository.MemberDTO;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	/**
	 * 인증 매니저는 인증과 관련된 모든 정보를 UserDetails 라는 타입으로 변환하는데 인증되는 방식을 수정하고 싶을때
	 * UserDetailsService 라는 인터페이스를 구현함으로써 인증매니저에 연결.
	 *
	 * 여기서는 DB에 저장되어 있는 유저정보를 조회하여 UserDetails 을 구성한다.
	 */

	// 시큐리티의 내용 외 파라미터를 추가하고 싶을 때, 아래 사용
	// 제약조건: Controller 에서 Auth를 점검할 때, UserCustom 으로 받아야 함.
	// 예) (변경 전) @AuthenticationPrincipal User user => (변경 후)
	// @AuthenticationPrincipal UserCustom user

	boolean enabled = true;
	boolean accountNonExpired = true;
	boolean credentialsNonExpired = true;
	boolean accountNonLocked = true;

	@Override
	public UserDetails loadUserByUsername(String user_email) throws UsernameNotFoundException {
		return null;
	}

	// DB에 등록된 권한에 따라 유저권한 부여 user_role
	private static Collection<? extends GrantedAuthority> authorities(MemberDTO memberDTO) {
		Collection<SimpleGrantedAuthority> authorities = new ArrayList();
		// DB에 저장한 USER_ROLE 이 1이면 ADMIN 권한, 아니면 ROLE_USER 로 준다.
		if (memberDTO.getUser_role().equals("1")) {
			authorities.add(new SimpleGrantedAuthority("ADMIN"));
		} else {
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
		return authorities;
	}
}