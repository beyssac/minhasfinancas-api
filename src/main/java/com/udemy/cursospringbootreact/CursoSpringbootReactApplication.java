package com.udemy.cursospringbootreact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class CursoSpringbootReactApplication{

	public static void main(String[] args) {
		SpringApplication.run(CursoSpringbootReactApplication.class, args);
	}
}
