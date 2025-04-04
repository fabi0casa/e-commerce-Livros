package com.fatec.livraria.service;

import com.fatec.livraria.entity.Autor;
import com.fatec.livraria.repository.AutorRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorService {
    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @PostConstruct
    public void inicializarAutores() {
        if (autorRepository.count() == 0) {
            List<Autor> autores = List.of(
                new Autor(null, "J.R.R. Tolkien"),
                new Autor(null, "Johan Konings"),
                new Autor(null, "Euclides"),
                new Autor(null, "Jeff Kinney"),
                new Autor(null, "Antoine de Saint-Exupéry"),
                new Autor(null, "George Orwell"),
                new Autor(null, "Mojang Studios"),
                new Autor(null, "Vários Autores"),
                new Autor(null, "Machado de Assis"),
                new Autor(null, "Editora Europa"),
                new Autor(null, "J.K. Rowling"),
                new Autor(null, "Dan Brown"),
                new Autor(null, "Yuval Noah Harari"),
                new Autor(null, "William P. Young")
            );
            autorRepository.saveAll(autores);
            System.out.println("✅ Autores cadastrados automaticamente.");
        }
    }
}
