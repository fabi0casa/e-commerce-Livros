package com.fatec.livraria.repository;

import com.fatec.livraria.entity.Categoria;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    List<Categoria> findByNomeContainingIgnoreCase(String nome);
}
