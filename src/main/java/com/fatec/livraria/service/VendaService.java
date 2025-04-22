package com.fatec.livraria.service;

import com.fatec.livraria.dto.VendaRequest;
import com.fatec.livraria.entity.Pedido;
import com.fatec.livraria.entity.Venda;
import com.fatec.livraria.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private LivroService livroService;

    @Autowired
    private CupomService cupomService;

    public void criarVenda(VendaRequest vendaReq, Pedido pedido) {
        var livro = livroService.buscarPorId(vendaReq.getLivroId())
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        Venda venda = new Venda();
        venda.setStatus("Em Processamento");
        venda.setValor(vendaReq.getValor());
        venda.setLivro(livro);
        venda.setPedido(pedido);

        vendaRepository.save(venda);
    }

    public Venda atualizarStatus(Integer vendaId, String novoStatus) {
        // Lista de status válidos
        Set<String> statusValidos = Set.of(
            "Em Processamento", "Reprovado", "Aprovado", "Cancelado",
            "Em Transporte", "Entregue",
            "Troca Solicitada", "Troca Recusada", "Troca Aceita", "Troca Concluida",
            "Devolução Solicitada", "Devolução Recusada", "Devolução Aceita", "Devolução Concluida"
        );
    
        // Mapa de transições válidas
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
    
        return vendaRepository.findById(vendaId).map(venda -> {
            String statusAtual = venda.getStatus();
    
            if (!statusValidos.contains(novoStatus)) {
                throw new IllegalArgumentException("Status inválido: " + novoStatus);
            }
    
            List<String> proximosValidos = transicoesPermitidas.getOrDefault(statusAtual, List.of());
    
            if (!proximosValidos.contains(novoStatus)) {
                throw new IllegalArgumentException(String.format(
                    "Transição de status não permitida: %s → %s", statusAtual, novoStatus
                ));
            }
    
            venda.setStatus(novoStatus);

            if (novoStatus.equals("Troca Aceita") || novoStatus.equals("Devolução Aceita")) {
                var cliente = venda.getPedido().getCliente();
                var valor = BigDecimal.valueOf(venda.getValor());

                String tipo = novoStatus.equals("Troca Aceita") ? "Troca" : "Devolução";
                cupomService.gerarCupom(valor, tipo, cliente);
            }

            return vendaRepository.save(venda);
        }).orElseThrow(() -> new RuntimeException("Venda não encontrada"));
    }
}
