package com.fatec.livraria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fatec.livraria.entity.CartaoCredito;

public interface CartaoCreditoRepository extends JpaRepository<CartaoCredito, Integer> {
    List<CartaoCredito> findByCliente_Id(Integer clienteId);
    List<CartaoCredito> findByCliente_IdOrderByPreferencialDesc(Integer clienteId);
}

