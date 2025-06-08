package com.fatec.livraria.service;

import com.fatec.livraria.dto.request.CartaoPagamentoRequest;
import com.fatec.livraria.dto.request.PedidoCarrinhoRequest;
import com.fatec.livraria.dto.request.PedidoRequest;
import com.fatec.livraria.dto.request.VendaRequest;
import com.fatec.livraria.entity.Pedido;
import com.fatec.livraria.entity.Venda;
import com.fatec.livraria.entity.Carrinho;
import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.entity.Cupom;
import com.fatec.livraria.entity.Livro;
import com.fatec.livraria.repository.PedidoRepository;
import com.fatec.livraria.repository.LivroRepository;

import jakarta.transaction.Transactional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PedidoService {

    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private LivroRepository livroRepository;
    @Autowired private ClienteService clienteService;
    @Autowired private CartaoCreditoService cartaoService;
    @Autowired private EnderecoService enderecoService;
    @Autowired private LivroService livroService;
    @Autowired private VendaService vendaService;
    @Autowired private CupomService cupomService;
    @Autowired private CarrinhoService carrinhoService;
    @Autowired private NotificacaoService notificacaoService;

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }    

    public Pedido buscarPorCodigoDoCliente(String codigo, HttpSession session) {
        Cliente cliente = clienteService.buscarPorId((Integer) session.getAttribute("clienteId"))
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        if (cliente == null) {
            throw new RuntimeException("Cliente não está logado");
        }
    
        return pedidoRepository.findByCodigoAndClienteId(codigo, cliente.getId())
            .orElseThrow(() -> new RuntimeException("Pedido com código " + codigo + " não encontrado para " + cliente.getNome()));
    }
    
    public Pedido buscarPorCodigoComoAdmin(String codigo, HttpSession session) {
        Cliente cliente = clienteService.buscarPorId((Integer) session.getAttribute("clienteId"))
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (cliente == null || !Boolean.TRUE.equals(cliente.isAdmin())) {
            throw new RuntimeException("Acesso negado: apenas administradores podem acessar");
        }
    
        return pedidoRepository.findByCodigo(codigo)
            .orElseThrow(() -> new NoSuchElementException("Pedido com código " + codigo + " não encontrado"));
    }
    
    
    public List<Pedido> listarPorClienteId(Integer clienteId) {
        Cliente cliente = clienteService.buscarPorId(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
                return pedidoRepository.findByCliente(cliente, Sort.by(Sort.Direction.DESC, "id"));
    }

    @Transactional
    public Pedido criarPedidoComVendas(PedidoRequest request, HttpSession session) {
        Cliente cliente = clienteService.buscarPorId((Integer) session.getAttribute("clienteId"))
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
        pedido.setFormaPagamento("");
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

            if (livro.getEstoque() < vendaReq.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o livro: " + livro.getNome());
            }
        
            // Desconta do estoque
            livro.setEstoque(livro.getEstoque() - vendaReq.getQuantidade());
            livroRepository.save(livro);

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

                if (!cupom.getCliente().getId().equals(cliente.getId())) {
                    throw new RuntimeException("Cupom não pertence ao cliente.");
                }

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

        // Carttões
        BigDecimal valorFinal = valorTotal.subtract(valorDescontos);

        if (valorFinal.compareTo(BigDecimal.ZERO) < 0) {
            valorFinal = BigDecimal.ZERO;
        }

        if (valorFinal.compareTo(BigDecimal.ZERO) > 0) {

            for (CartaoPagamentoRequest cartaoReq : request.getCartoes()) {
                var cartao = cartaoService.buscarPorId(cartaoReq.getCartaoId())
                        .orElseThrow(() -> new RuntimeException("Cartão não encontrado: " + cartaoReq.getCartaoId()));
            
                if (!cartao.getCliente().getId().equals(cliente.getId())) {
                    throw new RuntimeException("Cartão de pagamento não pertence ao cliente.");
                }
            }
            
            if (request.getCartoes() == null || request.getCartoes().isEmpty()) {
                throw new RuntimeException("Pagamento com cartão é obrigatório para o valor restante.");
            }

            BigDecimal somaCartoes = request.getCartoes().stream()
                    .map(CartaoPagamentoRequest::getValor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (somaCartoes.compareTo(valorFinal) != 0) {
                throw new RuntimeException("A soma dos valores dos cartões não corresponde ao valor total do pedido.");
            }

        }

        pedido.setValor(valorFinal);

        if (valorDescontos.compareTo(BigDecimal.ZERO) == 0) {
            pedido.setFormaPagamento("Cartão de Crédito");
        } else if (valorFinal.compareTo(BigDecimal.ZERO) == 0) {
            pedido.setFormaPagamento("Cupom");
        } else {
            pedido.setFormaPagamento("Cartão + Cupom");
        }

        pedido = pedidoRepository.save(pedido);

        clienteService.buscarPorId(cliente.getId());

        notificacaoService.criarNotificacao(
            "🛍️ Pedido",
            "Sua compra foi realizada com sucesso! Você pode ver seu pedido com código #" + pedido.getCodigo() + " em 'Meus Pedidos' na página da Conta!",
            cliente.getId()
        );

        return pedido;
    }

    @Transactional
    public Pedido criarPedidoDoCarrinho(PedidoCarrinhoRequest request, HttpSession session) {
        Cliente cliente = clienteService.buscarPorId((Integer) session.getAttribute("clienteId"))
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        var endereco = enderecoService.buscarPorId(request.getEnderecoId())
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

        List<Carrinho> itensCarrinho = carrinhoService.listarCarrinho(session)
            .orElseThrow(() -> new RuntimeException("Carrinho não encontrado ou vazio"));

        if (itensCarrinho.isEmpty()) {
            throw new RuntimeException("Carrinho vazio");
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setEndereco(endereco);
        pedido.setFormaPagamento("");
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

                if (!cupom.getCliente().getId().equals(cliente.getId())) {
                    throw new RuntimeException("Cupom não pertence ao cliente.");
                }

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

        if (valorFinal.compareTo(BigDecimal.ZERO) > 0) {
            
            for (CartaoPagamentoRequest cartaoReq : request.getCartoes()) {
                var cartao = cartaoService.buscarPorId(cartaoReq.getCartaoId())
                        .orElseThrow(() -> new RuntimeException("Cartão não encontrado: " + cartaoReq.getCartaoId()));
            
                if (!cartao.getCliente().getId().equals(cliente.getId())) {
                    throw new RuntimeException("Cartão de pagamento não pertence ao cliente.");
                }
            }

            if (request.getCartoes() == null || request.getCartoes().isEmpty()) {
                throw new RuntimeException("Pagamento com cartão é obrigatório para o valor restante.");
            }

            BigDecimal somaCartoes = request.getCartoes().stream()
                    .map(CartaoPagamentoRequest::getValor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (somaCartoes.compareTo(valorFinal) != 0) {
                throw new RuntimeException("A soma dos valores dos cartões não corresponde ao valor total do pedido.");
            }

        }

        pedido.setValor(valorFinal);

        if (valorDescontos.compareTo(BigDecimal.ZERO) == 0) {
            pedido.setFormaPagamento("Cartão de Crédito");
        } else if (valorFinal.compareTo(BigDecimal.ZERO) == 0) {
            pedido.setFormaPagamento("Cupom");
        } else {
            pedido.setFormaPagamento("Cartão + Cupom");
        }

        pedido = pedidoRepository.save(pedido);

        // Após criar o pedido, limpar o carrinho do cliente
        carrinhoService.limparCarrinhoDoCliente(session);

        notificacaoService.criarNotificacao(
            "🛍️ Pedido",
            "Sua compra do carrinho foi realizada com sucesso! Você pode ver seu pedido com código #" + pedido.getCodigo() + " em 'Meus Pedidos' na página da Conta!",
            cliente.getId()
        );
        return pedido;
    }

    public void atualizarStatusVendas(List<Integer> vendaIds, String novoStatus, Boolean retornarEstoque, HttpSession session) {
        vendaService.atualizarStatus(vendaIds, novoStatus, retornarEstoque, session);
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

    public String gerarContextoPedidos(Integer clienteId) {
        List<Pedido> pedidos = listarPorClienteId(clienteId);
        if (pedidos.isEmpty()) {
            return "O usuário ainda não realizou compras.";
        }
    
        StringBuilder contexto = new StringBuilder("Histórico de compras:\n");
        for (Pedido pedido : pedidos) {
            contexto.append("Pedido #").append(pedido.getCodigo()).append(" - Livros:\n");
            pedido.getVendas().forEach(venda -> {
                Livro livro = venda.getLivro();
                contexto.append("- ").append(livro.getNome())
                        .append(" (").append(livro.getAnoPublicacao()).append("), Autor: ")
                        .append(livro.getAutor().getNome()).append("\n");
            });
        }
        return contexto.toString();
    }
    
}
