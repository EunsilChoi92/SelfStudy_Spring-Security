package com.cos.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Controller // View를 return함
public class IndexController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(Authentication authentication
			, @AuthenticationPrincipal PrincipalDetails userDetails) { // DI(의존성 주입)
		// @AuthenticationPrincipal 을 통해서 세션 정보에 접근할 수 있음
		System.out.println("/test/login ==================");
		
		// 첫 번째 방법
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("authentication : " + principalDetails.getUser());
		
		// 두 번째 방법
		System.out.println("userDetails : " + userDetails.getUser());
		
		return "세션 정보 확인하기";
	}
	
	// 소셜 로그인을 할 때에는 PrincipalDetails이 아니라 OAuth2User로 casting 해야 함
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOAuthLogin(Authentication authentication
			, @AuthenticationPrincipal OAuth2User oauth) {
		System.out.println("/test/oauth/login ==================");
		
		// 첫 번째 방법
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		System.out.println("authentication : " + oAuth2User.getAttributes());
		
		// 두 번째 방법
		System.out.println("oauth2User : " + oauth.getAttributes());
		
		
		return "세션 정보 확인하기";
	}
	
	// localhost:8080/
	// localhost:8080
	@GetMapping({"","/"})
	public String index() {
		// Mustache : 기본폴더 -> src/main/resources/
		// view resolver 설정	: templates(prefix), .mustache(suffix)
		//                 	: application.yml에서 설정(생략 가능)
		return "index"; // src/main/resources/templates/index.mustache를 찾음
		// view resolver를 오버라이딩 해서 index.html 파일과 매핑되도록 함
	}
	
	// OAuth 로그인, 일반 로그인 둘 다 PrincipalDetails로 받을 수 있음
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("principalDetails : " + principalDetails.getUser());
		return "user";
	}
	
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}
	
	// spring security의 login 창이 뜸	-> SecurityConfig 파일 생성 후 작동 안 함
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@PostMapping("/join")
	public String join(User user) {
		System.out.println(user);
		user.setRole("ROLE_USER");
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		userRepository.save(user); // 회원가입은 잘 되나 패스워드 암호화가 되지 않아 시큐리티 로그인을 할 수 없음
		return "redirect:/loginForm";
	}
	
	@Secured("ROLE_ADMIN") // 특정 메소드에 간편하게 제한을 두고 싶은 경우 사용(하나만)
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	// hasRole 문법을 써서 복수 제한 가능, 메소드 실행 전에 실행됨(PostAuthorie도 사용 가능)
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") 
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터 정보";
	}
	
}
