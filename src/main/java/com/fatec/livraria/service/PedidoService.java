package com.fatec.livraria.service;

import com.fatec.livraria.dto.PedidoCarrinhoRequest;
import com.fatec.livraria.dto.PedidoRequest;
import com.fatec.livraria.dto.VendaRequest;
import com.fatec.livraria.entity.Pedido;
import com.fatec.livraria.entity.Venda;
import com.fatec.livraria.entity.Carrinho;
import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.entity.Cupom;
import com.fatec.livraria.entity.Livro;
import com.fatec.livraria.repository.PedidoRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PedidoService {

    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private ClienteService clienteService;
    @Autowired private EnderecoService enderecoService;
    @Autowired private LivroService livroService;
    @Autowired private VendaService vendaService;
    @Autowired private CupomService cupomService;
    @Autowired private CarrinhoService carrinhoService;

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }    

    public Pedido buscarPorCodigo(String codigo, Integer clienteId) {
        if (clienteId != null) {
            return pedidoRepository.findByCodigoAndClienteId(codigo, clienteId)
                .orElseThrow(() -> new RuntimeException("Pedido com código " + codigo + " e clienteId " + clienteId + " não encontrado"));
        } else {
            return pedidoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Pedido com código " + codigo + " não encontrado"));
        }
    }    
    
    public List<Pedido> listarPorClienteId(Integer clienteId) {
        Cliente cliente = clienteService.buscarPorId(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
                return pedidoRepository.findByCliente(cliente, Sort.by(Sort.Direction.DESC, "id"));
    }

    @Transactional
    public Pedido criarPedidoComVendas(PedidoRequest request) {
        Cliente cliente = clienteService.buscarPorId(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        var endereco = enderecoService.buscarPorId(request.getEnderecoId())
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

        List<Integer> livroIds = request.getVendas().stream()
                .map(VendaRequest::getLivroId)
                .distinct()
                .toList();

        List<Livro> livros = livroService.buscarPorIds(livroIds); 

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setEndereco(endereco);
        pedido.setFormaPagamento(request.getFormaPagamento());
        pedido.setCodigo(gerarCodigoPedido());
        pedido.setValor(BigDecimal.valueOf(0));

        BigDecimal valorTotal = BigDecimal.ZERO;

        pedido = pedidoRepository.save(pedido);

        List<Venda> vendasParaSalvar = new ArrayList<>();

        for (VendaRequest vendaReq : request.getVendas()) {
            Livro livro = livros.stream()
                    .filter(l -> l.getId().equals(vendaReq.getLivroId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Livro não encontrado: " + vendaReq.getLivroId()));

            for (int i = 0; i < vendaReq.getQuantidade(); i++) {
                Venda venda = new Venda();
                venda.setStatus("Em Processamento");
                venda.setValor(livro.getPrecoVenda());
                venda.setLivro(livro);
                venda.setPedido(pedido);

                vendasParaSalvar.add(venda);

                valorTotal = valorTotal.add(livro.getPrecoVenda());
            }
        }

        vendaService.criarVendasEmLote(vendasParaSalvar);

        //Cupons
        BigDecimal valorDescontos = BigDecimal.ZERO;

        if (request.getCuponsIds() != null && !request.getCuponsIds().isEmpty()) {
            List<Cupom> cupons = cupomService.buscarPorIds(request.getCuponsIds());

            BigDecimal valorRestantePedido = valorTotal;

            for (Cupom cupom : cupons) {
                if (valorRestantePedido.compareTo(BigDecimal.ZERO) <= 0) {
                    // Pedido já pago, nem processa mais cupons
                    break;
                }

                BigDecimal valorCupom = cupom.getValor();

                if (valorCupom.compareTo(valorRestantePedido) <= 0) {
                    // Cupom é menor ou igual ao que falta pagar -> usa o cupom todo
                    valorDescontos = valorDescontos.add(valorCupom);
                    valorRestantePedido = valorRestantePedido.subtract(valorCupom);

                    // Exclui o cupom porque foi usado todo
                    cupomService.excluirCupom(cupom.getId());

                } else {
                    // Cupom é maior que o necessário -> usa só uma parte e gera troco
                    valorDescontos = valorDescontos.add(valorRestantePedido);

                    BigDecimal troco = valorCupom.subtract(valorRestantePedido);

                    // Gera um novo cupom para o cliente com o valor do troco
                    cupomService.gerarCupom(troco, "Troco", cliente);

                    // Exclui o cupom original porque foi parcialmente usado
                    cupomService.excluirCupom(cupom.getId());

                    valorRestantePedido = BigDecimal.ZERO; // Pedido pago, para de processar
                }
            }
        }

        BigDecimal valorFinal = valorTotal.subtract(valorDescontos);

        if (valorFinal.compareTo(BigDecimal.ZERO) < 0) {
            valorFinal = BigDecimal.ZERO;
        }

        pedido.setValor(valorTotal);
        pedido = pedidoRepository.save(pedido);

        return pedido;
    }

    @Transactional
    public Pedido criarPedidoDoCarrinho(PedidoCarrinhoRequest request) {
        Cliente cliente = clienteService.buscarPorId(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        var endereco = enderecoService.buscarPorId(request.getEnderecoId())
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

        List<Carrinho> itensCarrinho = carrinhoService.listarCarrinho(cliente.getId());

        if (itensCarrinho.isEmpty()) {
            throw new RuntimeException("Carrinho vazio");
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setEndereco(endereco);
        pedido.setFormaPagamento(request.getFormaPagamento());
        pedido.setCodigo(gerarCodigoPedido());
        pedido.setValor(BigDecimal.ZERO);

        BigDecimal valorTotal = BigDecimal.ZERO;

        pedido = pedidoRepository.save(pedido);

        List<Venda> vendasParaSalvar = new ArrayList<>();

        for (Carrinho item : itensCarrinho) {
            Livro livro = item.getLivro();
            int quantidade = item.getQuantidade();

            for (int i = 0; i < quantidade; i++) {
                Venda venda = new Venda();
                venda.setStatus("Em Processamento");
                venda.setValor(livro.getPrecoVenda());
                venda.setLivro(livro);
                venda.setPedido(pedido);

                vendasParaSalvar.add(venda);

                valorTotal = valorTotal.add(livro.getPrecoVenda());
            }
        }

        vendaService.criarVendasEmLote(vendasParaSalvar);

        // Cupons
        BigDecimal valorDescontos = BigDecimal.ZERO;

        if (request.getCuponsIds() != null && !request.getCuponsIds().isEmpty()) {
            List<Cupom> cupons = cupomService.buscarPorIds(request.getCuponsIds());

            BigDecimal valorRestantePedido = valorTotal;

            for (Cupom cupom : cupons) {
                if (valorRestantePedido.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }

                BigDecimal valorCupom = cupom.getValor();

                if (valorCupom.compareTo(valorRestantePedido) <= 0) {
                    valorDescontos = valorDescontos.add(valorCupom);
                    valorRestantePedido = valorRestantePedido.subtract(valorCupom);

                    cupomService.excluirCupom(cupom.getId());

                } else {
                    valorDescontos = valorDescontos.add(valorRestantePedido);

                    BigDecimal troco = valorCupom.subtract(valorRestantePedido);

                    cupomService.gerarCupom(troco, "Troco", cliente);

                    cupomService.excluirCupom(cupom.getId());

                    valorRestantePedido = BigDecimal.ZERO;
                }
            }
        }

        BigDecimal valorFinal = valorTotal.subtract(valorDescontos);

        if (valorFinal.compareTo(BigDecimal.ZERO) < 0) {
            valorFinal = BigDecimal.ZERO;
        }

        pedido.setValor(valorFinal);
        pedido = pedidoRepository.save(pedido);

        // Após criar o pedido, limpar o carrinho do cliente
        carrinhoService.limparCarrinhoDoCliente(cliente.getId().longValue());

        return pedido;
    }


    public void atualizarStatusVendas(List<Integer> vendaId, String novoStatus) {
        vendaService.atualizarStatus(vendaId, novoStatus);
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
