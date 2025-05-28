package com.fatec.livraria.controller;

import com.fatec.livraria.service.ChatbotService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/chatbot")
public class ChatbotController {

    @Autowired private ChatbotService chatbotService;

    @PostMapping("/reset")
    public ResponseEntity<?> resetarChat(HttpSession session) {
        session.removeAttribute("chatHistorico");
        return ResponseEntity.ok(Map.of("mensagem", "Histórico do chat limpo."));
    }

    @PostMapping
    public ResponseEntity<?> conversar(@RequestBody Map<String, String> payload, HttpSession session) {
        System.out.println("Requisição recebida no backend: " + payload);

        String mensagem = payload.get("mensagem");
        if (mensagem == null || mensagem.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Mensagem é obrigatória.");
        }

        try {
            String resposta = chatbotService.conversar(mensagem, session);
            return ResponseEntity.ok(Map.of("resposta", resposta));
        } catch (HttpClientErrorException.TooManyRequests e) {
            System.err.println("Limite de requisições excedido: " + e.getResponseBodyAsString());
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Limite de requisições excedido. Por favor, tente novamente mais tarde.");
        } catch (HttpClientErrorException e) {
            System.err.println("Erro HTTP: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body("Erro ao chamar Gemini: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro interno ao chamar Gemini: " + e.getMessage());
        }
    }
}
