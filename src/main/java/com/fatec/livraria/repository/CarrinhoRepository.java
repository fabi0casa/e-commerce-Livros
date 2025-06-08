package com.fatec.livraria.repository;

import com.fatec.livraria.entity.Carrinho;
import com.fatec.livraria.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Integer> {
    List<Carrinho> findByCliente(Cliente cliente);
    void deleteAllByClienteId(Integer clienteId);
    void deleteByDataBefore(Date limite);
    List<Carrinho> findByDataBefore(Date limite);
}

