package com.fatec.livraria.service;

import com.fatec.livraria.entity.Notificacao;
import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.repository.ClienteRepository;
import com.fatec.livraria.repository.NotificacaoRepository;

import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final ClienteRepository clienteRepository;

    public List<Notificacao> buscarNaoVistasPorCliente(HttpSession session) {

        Cliente cliente = clienteRepository.findById((Integer) session.getAttribute("clienteId"))
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        return notificacaoRepository.findByClienteAndIsVistoFalse(cliente);
    }

    public void criarNotificacao(String titulo, String descricao, Integer clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Notificacao notificacao = new Notificacao();
        notificacao.setTitulo(titulo);
        notificacao.setDescricao(descricao);
        notificacao.setCliente(cliente);
        notificacao.setVisto(false);

        notificacaoRepository.save(notificacao);
    }

    public Notificacao marcarComoVisto(Integer id, HttpSession session) {
        Cliente cliente = clienteRepository.findById((Integer) session.getAttribute("clienteId"))
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    
        Notificacao notificacao = notificacaoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));
    
        if (!notificacao.getCliente().getId().equals(cliente.getId())) {
            throw new RuntimeException("Você não tem permissão para alterar esta notificação.");
        }
    
        notificacao.setVisto(true);
        return notificacaoRepository.save(notificacao);
    }    
}
