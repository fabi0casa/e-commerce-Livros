package com.fatec.livraria.repository;

import com.fatec.livraria.entity.TransacaoCliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransacaoClienteRepository extends JpaRepository<TransacaoCliente, Integer> {
    List<TransacaoCliente> findByClienteId(Integer clienteId);
}
