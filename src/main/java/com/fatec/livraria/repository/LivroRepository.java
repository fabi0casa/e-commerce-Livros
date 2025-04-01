package com.fatec.livraria.repository;

import com.fatec.livraria.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Integer> {
    List<Livro> findByNomeContainingIgnoreCase(String nome);
}
