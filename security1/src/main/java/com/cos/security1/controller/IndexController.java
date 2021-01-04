package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // View를 return함
public class IndexController {
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
	
	@GetMapping("/user")
	public @ResponseBody String user() {
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
	@GetMapping("/login")
	public String login() {
		return "loginForm";
	}
	
	@GetMapping("/join")
	public @ResponseBody String join() {
		return "join";
	}
	
	@GetMapping("/joinProc")
	public @ResponseBody String joinProc() {
		return "회원가입 완료됨";
	}
}
