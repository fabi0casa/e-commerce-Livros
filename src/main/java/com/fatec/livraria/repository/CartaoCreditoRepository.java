package com.fatec.livraria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fatec.livraria.entity.CartaoCredito;

public interface CartaoCreditoRepository extends JpaRepository<CartaoCredito, Integer> {
}

