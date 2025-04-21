package com.fatec.livraria.service;

import com.fatec.livraria.dto.PedidoRequest;
import com.fatec.livraria.dto.VendaRequest;
import com.fatec.livraria.entity.Pedido;
import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.entity.Venda;
import com.fatec.livraria.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PedidoService {

    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private ClienteService clienteService;
    @Autowired private EnderecoService enderecoService;
    @Autowired private LivroService livroService;
    @Autowired private VendaService vendaService;

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPorClienteId(Integer clienteId) {
        Cliente cliente = clienteService.buscarPorId(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return pedidoRepository.findByCliente(cliente);
    }

    public Pedido criarPedidoComVendas(PedidoRequest request) {
        Cliente cliente = clienteService.buscarPorId(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        var endereco = enderecoService.buscarPorId(request.getEnderecoId())
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setEndereco(endereco);
        pedido.setFormaPagamento(request.getFormaPagamento());
        pedido.setCodigo(gerarCodigoPedido());

        pedido = pedidoRepository.save(pedido);

        for (VendaRequest vendaReq : request.getVendas()) {
            vendaService.criarVenda(vendaReq, pedido);
        }

        return pedido;
    }

    public Venda atualizarStatusVenda(Integer vendaId, String novoStatus) {
        return vendaService.atualizarStatus(vendaId, novoStatus);
    }

    private String gerarCodigoPedido() {
        String codigo;
        do {
            String data = new SimpleDateFormat("yyyyMMdd").format(new Date());
            int aleatorio = (int) (Math.random() * 90000000) + 10000000;
            codigo = data + aleatorio;
        } while (pedidoRepository.existsByCodigo(codigo));
        return codigo;
    }
}
