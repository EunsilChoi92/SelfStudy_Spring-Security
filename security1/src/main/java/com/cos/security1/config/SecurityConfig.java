package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity	// 웹 시큐리티 활성화
					// spring security filter(SecurityConfig,java)가 기본 필터 체인에 등록이 됨

// Secured 어노테이션 활성화, PreAuthorize 어노테이션 활성화
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true) 
public class SecurityConfig extends WebSecurityConfigurerAdapter{

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
			.defaultSuccessUrl("/"); // 특정 페이지로 가기 위해 로그인을 할 경우, 로그인에 성공하면 index 페이지가 아나리
									// 원래 가려고 했던 페이지를 자동으로 띄워줌
	}
}
