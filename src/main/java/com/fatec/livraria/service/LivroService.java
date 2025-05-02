package com.fatec.livraria.service;

import com.fatec.livraria.entity.Livro;
import com.fatec.livraria.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }

    public Optional<Livro> buscarPorId(Integer id) {
        return livroRepository.findById(id);
    }

    public List<Livro> buscarPorNome(String nome) {
        return livroRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Livro> buscarPorIds(List<Integer> ids) {
        return livroRepository.findAllById(ids);
    }
}
