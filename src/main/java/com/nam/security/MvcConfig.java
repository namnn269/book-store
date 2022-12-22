package com.nam.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("/signin-up/loginform");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

//		registry.addResourceHandler("/img/**", "/css/**","/js/**")
//				.addResourceLocations("classpath:/static/img/",
//				"classpath:/static/css/","classpath:/static/js/");
		
		registry.addResourceHandler("/**")
		.addResourceLocations("classpath:/static/");

	}
	
	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect();
	}
	
	@Bean
	public TemplateEngine engine() {
		SpringTemplateEngine engine=new SpringTemplateEngine();
		engine.addDialect(new LayoutDialect());
		return engine;
	}

}
