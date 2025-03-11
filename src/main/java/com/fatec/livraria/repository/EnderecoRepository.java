package com.fatec.livraria.repository;

import com.fatec.livraria.entity.Endereco;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer>{
    List<Endereco> findByCliente_Id(Integer clienteId);
}
