package com.cos.security1.config;

import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	// mustache를 재설정할 수 있음
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		MustacheViewResolver resolver = new MustacheViewResolver();
		resolver.setCharset("UTF-8");
		resolver.setContentType("text/html; charset=UTF-8");
		// classpath는 프로젝트 경로
		resolver.setPrefix("classpath:/templates/");
		resolver.setSuffix(".html"); // .html 파일을 mustache로 인식할 수 있게 함
		
		registry.viewResolver(resolver);
	}
}
