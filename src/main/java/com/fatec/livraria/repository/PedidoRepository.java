package com.fatec.livraria.repository;

import com.fatec.livraria.entity.Pedido;
import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.entity.Endereco;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByCliente(Cliente cliente, Sort sort);
    boolean existsByCodigo(String codigo);
    Optional<Pedido> findByCodigo(String codigo);
    Optional<Pedido> findByCodigoAndClienteId(String codigo, Integer clienteId);
    long countByEndereco(Endereco endereco);
    boolean existsByEndereco(Endereco endereco);
    
    @Query("SELECT COALESCE(SUM(p.valor), 0) FROM Pedido p WHERE p.dataCriacao BETWEEN :inicio AND :fim")
    BigDecimal findTotalVendasPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    long countByDataCriacaoBetween(LocalDate inicio, LocalDate fim);

}
