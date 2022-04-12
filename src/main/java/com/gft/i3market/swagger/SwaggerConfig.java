package com.gft.i3market.swagger;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.models.Contact;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableWebMvc
@EnableSwagger2
@Component
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class SwaggerConfig implements WebMvcConfigurer {//extends WebMvcConfigurationSupport {

	 // api documentation at http://localhost:8080/v2/api-docs
	 // gui at http://localhost:8080/swagger-ui.html#/
	
	 @Bean 
	 public Docket api() { return new Docket(DocumentationType.SWAGGER_12)
			 .select() .apis(RequestHandlerSelectors.any())
			 .paths(PathSelectors.any())
			 .apis(RequestHandlerSelectors.basePackage("com.gft.i3market.controllers"))
			 .build() .apiInfo(this.apiInfo()) .useDefaultResponseMessages(false); }
	
	 
	 @Override
	 public void addResourceHandlers(ResourceHandlerRegistry registry) {
			registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
			registry.addResourceHandler("/swagger-ui.html**").addResourceLocations("classpath:/META-INF/resources/");
			registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	 }
	 
	 @Override
	 public void addViewControllers(ViewControllerRegistry registry) {
	     registry.addRedirectViewController("/api/v2/api-docs", "/v2/api-docs");
	     registry.addRedirectViewController("/api/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui");
	     registry.addRedirectViewController("/api/swagger-resources/configuration/security", "/swagger-resources/configuration/security");
	     registry.addRedirectViewController("/api/swagger-resources", "/swagger-resources");
	 }
	
	private ApiInfo apiInfo() {
		ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder();
		apiInfoBuilder.title("REST API");
		apiInfoBuilder.description("REST API Generation");
		apiInfoBuilder.version("1.0.0");
		apiInfoBuilder.license("GNU GENERAL PUBLIC LICENSE, Version 3");
		apiInfoBuilder.licenseUrl("https://www.gnu.org/licenses/gpl-3.0.en.html");
		return apiInfoBuilder.build();
	}

}