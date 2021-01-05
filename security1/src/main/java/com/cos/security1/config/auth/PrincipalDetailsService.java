package com.cos.security1.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

// Security 설정에서 loginProcessingUrl("/login")으로 설정해줬으므로;
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadByUsername 메소드가 실행됨
@Service
public class PrincipalDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	// Security Session(내부 Authentication(내부 UserDetails))
	// method 종료 시 @AuthenticationPrincipal 어노테이션이 만들어짐
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// parameter는 input 태그의 name이 username으로 설정되어 있어야 값을 받을 수 있음
		// 변경하고 싶으면 SecurityConfig에서 변경 가능
		
		User userEntity = userRepository.findByUsername(username);
		if(userEntity != null) {
			return new PrincipalDetails(userEntity);
		}
		
		return null;
	}
	

}
