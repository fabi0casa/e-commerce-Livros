package com.fatec.livraria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class LivrariaApplication {

    static {
        // Carregando variáveis de ambiente antes da inicialização do programa
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USER", dotenv.get("DB_USER"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
    }

	public static void main(String[] args) {
		SpringApplication.run(LivrariaApplication.class, args);
	}

}
