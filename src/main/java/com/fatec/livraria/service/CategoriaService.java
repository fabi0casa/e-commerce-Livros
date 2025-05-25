package com.fatec.livraria.service;

import com.fatec.livraria.entity.Categoria;
import com.fatec.livraria.repository.CategoriaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @PostConstruct
    public void inicializarCategorias() {
        if (categoriaRepository.count() == 0) {
            List<Categoria> categorias = List.of(
                new Categoria(null, "Comédia"),
                new Categoria(null, "Ficção"),
                new Categoria(null, "Fantasia"),
                new Categoria(null, "História"),
                new Categoria(null, "Literatura Brasileira"),
                new Categoria(null, "Infantojuvenil"),
                new Categoria(null, "Religião"),
                new Categoria(null, "Didático")
            );
            categoriaRepository.saveAll(categorias);
            System.out.println("✅ Categorias cadastradas automaticamente.");
        }
    }
}
