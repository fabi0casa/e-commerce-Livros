package com.fatec.livraria.controller;

import com.fatec.livraria.entity.Notificacao;
import com.fatec.livraria.service.NotificacaoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    // Retorna todas as notificações não vistas do cliente logado
    @GetMapping("/nao-vistas")
    public List<Notificacao> getNotificacoesNaoVistas(HttpSession session) {
        return notificacaoService.buscarNaoVistasPorCliente(session);
    }

    // Marca uma notificação como vista
    @PutMapping("/{id}/marcar-visto")
    public Notificacao marcarComoVisto(@PathVariable Integer id, HttpSession session) {
        return notificacaoService.marcarComoVisto(id, session);
    }
}
