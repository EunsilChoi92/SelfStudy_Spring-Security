package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}
