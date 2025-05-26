package com.fatec.livraria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@ComponentScan(basePackages = "com.fatec.livraria")
public class LivrariaApplication {

	public static void main(String[] args) {
		
		//carrega as variáveis do .env
		Dotenv dotenv = Dotenv.load();
		System.setProperty("GEMINI_API_KEY", dotenv.get("GEMINI_API_KEY"));

		SpringApplication.run(LivrariaApplication.class, args);
	}

}
