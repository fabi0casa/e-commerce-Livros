package com.fatec.livraria.repository;

import com.fatec.livraria.entity.Pedido;
import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.entity.Endereco;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByCliente(Cliente cliente, Sort sort);
    boolean existsByCodigo(String codigo);
    Optional<Pedido> findByCodigo(String codigo);
    Optional<Pedido> findByCodigoAndClienteId(String codigo, Integer clienteId);
    long countByEndereco(Endereco endereco);
}
