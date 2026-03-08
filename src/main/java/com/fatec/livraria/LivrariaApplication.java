package com.fatec.livraria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@ComponentScan(basePackages = "com.fatec.livraria")
@EnableScheduling
public class LivrariaApplication {

	public static void main(String[] args) {
		
		//carrega as variáveis do .env
		try {
			Dotenv dotenv = Dotenv.load()
			dotenv.entries().forEach(entry -> {
				System.setProperty(entry.getKey(), entry.getValue());
			});
		} catch (Exception ignored) {
		}

		SpringApplication.run(LivrariaApplication.class, args);
	}

}
