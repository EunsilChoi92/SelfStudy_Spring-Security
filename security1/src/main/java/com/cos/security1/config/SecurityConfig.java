package com.cos.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;

@Configuration
@EnableWebSecurity	// 웹 시큐리티 활성화
					// spring security filter(SecurityConfig,java)가 기본 필터 체인에 등록이 됨

// Secured 어노테이션 활성화, PreAuthorize 어노테이션 활성화
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true) 
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalOauth2UserService principalDetailService;

	// 해당 메서드의 리턴되는 object를 IoC로 등록해줌
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated()
			.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/loginForm")
			.loginProcessingUrl("/login") // login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해줌
			.defaultSuccessUrl("/") // 특정 페이지로 가기 위해 로그인을 할 경우, 로그인에 성공하면 index 페이지가 아나리
									// 원래 가려고 했던 페이지를 자동으로 띄워줌
			
			// <구글 로그인>
			// 1. 코드 받기(인증) 2. 엑세스 토큰 받기(권한) 3. 사용자 프로필 정보를 가져옴 
			// 4-1. 그 정보를 토대로 자동으로 회원가입을 시키기도 함
			// 4-2. 구글 정보가 모자라다면 추가로 정보를 받아 회원가입을 시킬 수도 있음
			// Tip. 구글 로그인이 완료되면 코드가 아니라 엑세트 토큰과 사용자 프로필 정보를 바로 받을 수 있음 
			.and()
			.oauth2Login()
			.loginPage("/loginForm") // 구글 로그인이 완료된 뒤의 후처리가 필요함
			.userInfoEndpoint()
			.userService(principalDetailService); // 타입은 Oauth2UserService가 되어야 함;
	}
}
