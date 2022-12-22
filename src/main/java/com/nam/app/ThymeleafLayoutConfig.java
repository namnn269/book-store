package com.nam.app;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import nz.net.ultraq.thymeleaf.layoutdialect.decorators.strategies.GroupingStrategy;

@Configuration
@ConditionalOnClass(name = "nz.net.ultraq.thymeleaf.LayoutDialect")
public class ThymeleafLayoutConfig {

	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect(new GroupingStrategy());
	}

	@Bean
	public TemplateEngine engine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.addDialect(new LayoutDialect(new GroupingStrategy()));
		return engine;
	}
}
 