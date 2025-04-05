package com.fatec.livraria.controller;

import com.fatec.livraria.dto.PedidoRequest;
import com.fatec.livraria.dto.VendaRequest;
import com.fatec.livraria.entity.Pedido;
import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.entity.Venda;
import com.fatec.livraria.service.PedidoService;
import com.fatec.livraria.repository.ClienteRepository;
import com.fatec.livraria.repository.EnderecoRepository;
import com.fatec.livraria.repository.LivroRepository;
import com.fatec.livraria.repository.VendaRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private LivroRepository livroRepository;

    @GetMapping(value = "/all")
    public List<Pedido> listarTodos() {
        return pedidoService.listarTodos();
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Pedido> listarPorCliente(@PathVariable Integer clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return pedidoService.listarPorCliente(cliente);
    }

    @PostMapping(value = "/add")
    @Transactional
    public Pedido criarPedido(@RequestBody PedidoRequest request) {
        Pedido pedido = new Pedido();

        pedido.setCodigo(gerarCodigoPedido());

        pedido.setCliente(clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado")));

        pedido.setEndereco(enderecoRepository.findById(request.getEnderecoId())
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado")));

        pedido = pedidoService.salvar(pedido);

        for (VendaRequest vendaReq : request.getVendas()) {
            Venda venda = new Venda();
            venda.setDataHora(new Date());
            venda.setFormaPagamento(vendaReq.getFormaPagamento());
            venda.setStatus("Em Processamento");
            venda.setValor(vendaReq.getValor());

            venda.setLivro(livroRepository.findById(vendaReq.getLivroId())
                    .orElseThrow(() -> new RuntimeException("Livro não encontrado")));

            venda.setEndereco(pedido.getEndereco());
            venda.setPedido(pedido);

            vendaRepository.save(venda);
        }

        return pedido;
    }


    @PatchMapping("/venda/{vendaId}/status")
    public Venda atualizarStatusVenda(@PathVariable Integer vendaId, @RequestParam String status) {
        return vendaRepository.findById(vendaId).map(venda -> {
            venda.setStatus(status);
            return vendaRepository.save(venda);
        }).orElseThrow(() -> new RuntimeException("Venda não encontrada"));
    }  

    private String gerarCodigoPedido() {
        String data = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int aleatorio = (int) (Math.random() * 90000000) + 10000000; // 8 dígitos
        return data + aleatorio;
    }  
}
