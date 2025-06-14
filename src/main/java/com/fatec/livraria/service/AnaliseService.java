package com.fatec.livraria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fatec.livraria.dto.response.AnaliseResponse;
import com.fatec.livraria.entity.Categoria;
import com.fatec.livraria.entity.Livro;
import com.fatec.livraria.repository.LivroRepository;
import com.fatec.livraria.repository.VendaRepository;
import com.fatec.livraria.repository.CategoriaRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class AnaliseService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private VendaRepository vendaRepository;

    public List<AnaliseResponse> analisarLivros(String nome, LocalDate dataInicio, LocalDate dataFim) {
        List<Livro> livros = (nome != null && !nome.isBlank())
            ? livroRepository.findByNomeContainingIgnoreCase(nome)
            : livroRepository.findAll();

        return livros.stream()
            .map(livro -> {
                Long total = vendaRepository.countByLivroAndPeriodo(livro.getId(), dataInicio, dataFim);
                return new AnaliseResponse(livro.getId(), livro.getNome(), total);
            })
            .toList();
    }

    public List<AnaliseResponse> analisarCategorias(String nome, LocalDate dataInicio, LocalDate dataFim) {
        List<Categoria> categorias = (nome != null && !nome.isBlank())
            ? categoriaRepository.findByNomeContainingIgnoreCase(nome)
            : categoriaRepository.findAll();

        return categorias.stream()
            .map(cat -> {
                Long total = vendaRepository.countByCategoriaAndPeriodo(cat.getId(), dataInicio, dataFim);
                return new AnaliseResponse(cat.getId(), cat.getNome(), total);
            })
            .toList();
    }
}
