package com.cos.security1.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	// 구글로부터 받은 userRequest 데이터에 대해 후처리되는 메소드
	// method 종료 시 @AuthenticationPrincipal 어노테이션이 만들어짐
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// registrationId로 어떤 OAuth로 로그인했는지 확인 가능
		System.out.println("getClientRegistration : " + userRequest.getClientRegistration());
		System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());

		OAuth2User oauth2User = super.loadUser(userRequest);
		// 구글 로그인 버튼 클릭 -> 구글 로그인 페이지 -> 로그인 완료 -> code를 리턴 받음(OAuth-client library가)
		// -> code를 통해 Access Token 요청
		// userRequest 정보를 통해 loadUser 함수를 호출하고 구글로부터 회원 프로필을 받음
		System.out.println("getAttributes : " + oauth2User.getAttributes());
		
		// <회원가입>
		// 받아온 정보로 username은 google_sub, password는 암호화한 getinthere, email은 그대로 사용해서 회원가입시킴
		// role은 ROLE_USER
		// 어차피 구글 로그인 폼으로 로그인을 할 거라서 password는 아무거나 해도 상관 없음
		// 일반 로그인인지 구글 로그인인지 구분하기 위해 User 테이블에 컬럼을 더 추가함

		String provider = userRequest.getClientRegistration().getRegistrationId(); // google
		String providerId = oauth2User.getAttribute("sub"); 
		String username = provider + "_" + providerId;
		String password = bCryptPasswordEncoder.encode("getinthere");
		String email = oauth2User.getAttribute("email");
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) {
			System.out.println("구글 로그인이 최초입니다.");
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
		} else {
			System.out.println("이미 구글 로그인을 한 적이 있습니다.");
		}
		
		return new PrincipalDetails(userEntity, oauth2User.getAttributes());
	}

}
