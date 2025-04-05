package com.fatec.livraria.service;

import com.fatec.livraria.entity.Pedido;
import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPorCliente(Cliente cliente) {
        return pedidoRepository.findByCliente(cliente);
    }

    public Pedido salvar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }
}
