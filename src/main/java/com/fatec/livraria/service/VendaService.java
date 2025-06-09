package com.fatec.livraria.service;

import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.entity.Livro;
import com.fatec.livraria.entity.Pedido;
import com.fatec.livraria.entity.Venda;
import com.fatec.livraria.repository.ClienteRepository;
import com.fatec.livraria.repository.LivroRepository;
import com.fatec.livraria.repository.VendaRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static java.util.Map.entry;

import java.math.BigDecimal;
import java.util.*;

@Service
public class VendaService {

    @Autowired private VendaRepository vendaRepository;
    @Autowired private LivroService livroService;
    @Autowired private CupomService cupomService;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private LivroRepository livroRepository;
    @Autowired private NotificacaoService notificacaoService;

    public void criarVenda(Livro livro, Pedido pedido) {
        Venda venda = new Venda();
        venda.setStatus("Em Processamento");
        venda.setValor(livro.getPrecoVenda()); // já é BigDecimal
        venda.setLivro(livro);
        venda.setPedido(pedido);

        vendaRepository.save(venda);
    }
    
    public void criarVendasEmLote(List<Venda> vendas) {
        vendaRepository.saveAll(vendas);
    }

    public void atualizarStatus(List<Integer> vendaIds, String novoStatus, Boolean retornarEstoque, HttpSession session) {
        Integer clienteId = (Integer) session.getAttribute("clienteId");
        if (clienteId == null) {
            throw new IllegalStateException("Cliente não está logado.");
        }
    
        Cliente clienteLogado = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));
    
        boolean isAdmin = Boolean.TRUE.equals(clienteLogado.isAdmin());

        List<Venda> vendas = vendaRepository.findAllById(vendaIds);

        BigDecimal valorTotal = BigDecimal.ZERO;
        Cliente cliente = null;
        boolean gerarCupom = false;
        String tipoCupom = "";

        Map<Integer, Integer> livrosParaAtualizar = new HashMap<>();

        for (Venda venda : vendas) {
            String statusAtual = venda.getStatus();

            validarTransicaoStatus(statusAtual, novoStatus);

            if (!isAdmin) validarTransicaoCliente(venda, novoStatus, clienteId);

            venda.setStatus(novoStatus);

            if (novoStatus.equals("Troca Aceita") || novoStatus.equals("Devolução Aceita")) {
                valorTotal = valorTotal.add(venda.getValor());
                cliente = venda.getPedido().getCliente();
                gerarCupom = true;
                tipoCupom = novoStatus.equals("Troca Aceita") ? "Troca" : "Devolução";

                if (Boolean.TRUE.equals(retornarEstoque)) {
                    Livro livro = venda.getLivro();
                    livrosParaAtualizar.merge(livro.getId(), 1, Integer::sum);
                }
            }
        }

        vendaRepository.saveAll(vendas);

        if (!livrosParaAtualizar.isEmpty()) {
            List<Livro> livros = livroRepository.findAllById(livrosParaAtualizar.keySet());
            for (Livro livro : livros) {
                Integer quantidade = livrosParaAtualizar.get(livro.getId());
                livro.setEstoque(livro.getEstoque() + quantidade);
            }
            livroRepository.saveAll(livros);
        }

        if (gerarCupom && cliente != null && valorTotal.compareTo(BigDecimal.ZERO) > 0) {
            cupomService.gerarCupom(valorTotal, tipoCupom, cliente);
        }

        notificarMudancaStatus(novoStatus, vendas.get(0).getPedido().getCliente());
    }

    private void validarTransicaoStatus(String statusAtual, String novoStatus) {
        Set<String> statusValidos = Set.of(
            "Em Processamento", "Reprovado", "Aprovado", "Cancelado",
            "Em Transporte", "Entregue",
            "Troca Solicitada", "Troca Recusada", "Troca Aceita", "Troca Concluida",
            "Devolução Solicitada", "Devolução Recusada", "Devolução Aceita", "Devolução Concluida"
        );

        Map<String, List<String>> transicoesPermitidas = Map.of(
            "Em Processamento", List.of("Aprovado", "Reprovado", "Cancelado"),
            "Aprovado", List.of("Em Transporte", "Cancelado"),
            "Em Transporte", List.of("Cancelado", "Entregue"),
            "Entregue", List.of("Troca Solicitada", "Devolução Solicitada"),
            "Troca Solicitada", List.of("Troca Aceita", "Troca Recusada"),
            "Troca Aceita", List.of("Troca Concluida"),
            "Devolução Solicitada", List.of("Devolução Aceita", "Devolução Recusada"),
            "Devolução Aceita", List.of("Devolução Concluida")
        );

        if (!statusValidos.contains(novoStatus)) {
            throw new IllegalArgumentException("Status inválido: " + novoStatus);
        }

        List<String> proximosValidos = transicoesPermitidas.getOrDefault(statusAtual, List.of());

        if (!proximosValidos.contains(novoStatus)) {
            throw new IllegalArgumentException(String.format(
                "Transição de status não permitida: %s → %s", statusAtual, novoStatus
            ));
        }
    }

    private void validarTransicaoCliente(Venda venda, String novoStatus, Integer clienteId) {
        String statusAtual = venda.getStatus();
        Cliente clientePedido = venda.getPedido().getCliente();
    
        if (!clientePedido.getId().equals(clienteId)) {
            throw new SecurityException("Você não tem permissão para alterar este pedido.");
        }
    
        // Regras permitidas para cliente comum
        boolean permitido = switch (statusAtual) {
            case "Em Processamento", "Aprovado", "Em Transporte" -> novoStatus.equals("Cancelado");
            case "Entregue" -> novoStatus.equals("Troca Solicitada") || novoStatus.equals("Devolução Solicitada");
            default -> false;
        };
    
        if (!permitido) {
            throw new IllegalArgumentException(String.format(
                "Clientes não podem alterar de %s para %s.", statusAtual, novoStatus
            ));
        }
    }

    private void notificarMudancaStatus(String novoStatus, Cliente cliente) {
        Map<String, String[]> mensagens = Map.ofEntries(
            entry("Aprovado", new String[]{"✅ Pedido Aprovado", "Seu pedido foi aprovado e logo será enviado!"}),
            entry("Reprovado", new String[]{"❌ Pedido Reprovado", "Infelizmente seu pedido não foi aprovado. Verifique os detalhes na sua conta."}),
            entry("Cancelado", new String[]{"✖️ Pedido Cancelado", "Você cancelou o seu pedido. Esperamos vê-lo em breve novamente!"}),
            entry("Em Transporte", new String[]{"📦 Pedido a Caminho", "Seu pedido está a caminho! Fique de olho no rastreio."}),
            entry("Entregue", new String[]{"📬 Pedido Entregue", "Seu pedido foi entregue! Esperamos que goste."}),
            entry("Troca Solicitada", new String[]{"🔄 Troca Solicitada", "Você solicitou uma troca. Estamos analisando seu pedido."}),
            entry("Troca Aceita", new String[]{"💎 Troca Aceita", "Sua solicitação de troca foi aceita. Em breve daremos continuidade."}),
            entry("Troca Recusada", new String[]{"😵 Troca Recusada", "Sua solicitação de troca foi recusada. Verifique mais detalhes em Meus Pedidos."}),
            entry("Troca Concluida", new String[]{"☑️ Troca Concluída", "Sua troca foi concluída com sucesso!"}),
            entry("Devolução Solicitada", new String[]{"↩️ Devolução Solicitada", "Você solicitou uma devolução. Analisaremos o pedido."}),
            entry("Devolução Aceita", new String[]{"👑 Devolução Aceita", "Sua devolução foi aceita. O valor será devolvido conforme política."}),
            entry("Devolução Recusada", new String[]{"😵‍💫 Devolução Recusada", "Sua devolução foi recusada. Verifique mais detalhes em Meus Pedidos."}),
            entry("Devolução Concluida", new String[]{"☑️ Devolução Concluída", "A devolução foi concluída. Esperamos vê-lo novamente!"})
        );
    
        String[] mensagem = mensagens.get(novoStatus);
        if (mensagem != null) {
            notificacaoService.criarNotificacao(mensagem[0], mensagem[1], cliente.getId());
        }
    }
    
}
