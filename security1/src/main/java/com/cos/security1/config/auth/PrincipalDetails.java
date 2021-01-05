package com.cos.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cos.security1.model.User;

import lombok.Data;

// security가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킴
// 로그인 진행이 완료가 되면 security session을 만들어줌(Security ContextHolder에 session 정보를 저장함) 
// Security Session 영역에 저장할 정보는 Authentication 타입의 객체여야 함
// Authentication 안에 User 정보가 있어야 함
// User Object의 타입은 UserDetails 타입의 객체여야 함
// Security Session -> Authentication -> UserDetails

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

	private User user; // 컴포지션
	private Map<String, Object> attributes;
	
	// 일반 로그인
	public PrincipalDetails(User user) {
		this.user = user;
	}

	// OAuth 로그인
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}
	
	// 해당 User의 권한을 return함
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// user.getRole() 이지만 return 타입이 String이므로 return할 수 없음
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	// 계정 만료 여부
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 계정 잠김 여부
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 비밀번호 만료 여부
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 계정 활성화 여부
	@Override
	public boolean isEnabled() {
		// ex) 사이트에서 1년 동안 로그인을 안 한 경우 휴먼 계정으로 하기로 함
		// 현재시간 - user.getLoginDate()가 1년을 초과하면 return false로 설정 
		return true;
	}

	// Oauth2User interface
	
	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return null;
	}

}
