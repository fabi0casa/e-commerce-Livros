package com.fatec.livraria.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/chat")
public class ChatbotController {

    private final String apiKey = System.getProperty("GEMINI_API_KEY");
    private final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-2.0-flash-lite:generateContent?key=";
    private final RestTemplate restTemplate = new RestTemplate();

    private final String CONTEXTO = """
        Você é uma assistente virtual de um e-commerce de livros. 
        Seja educada, clara e útil. Ajude com pedidos, recomendações, autores e editoras. 
        Quando não souber a resposta, diga "Não sei" em vez de inventar.
        """;

    @PostMapping
    public ResponseEntity<?> conversar(@RequestBody Map<String, String> payload) {
        System.out.println("Requisição recebida no backend: " + payload);

        String mensagem = payload.get("mensagem");
        if (mensagem == null || mensagem.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Mensagem é obrigatória.");
        }

        try {
            String resposta = chamarGemini(mensagem);
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

    private String chamarGemini(String mensagemUsuario) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("API KEY não está definida. Use -DGEMINI_API_KEY=...");
        }

        // Corpo da requisição
        Map<String, Object> userContent = new HashMap<>();
        userContent.put("role", "user");
        userContent.put("parts", List.of(Map.of("text", CONTEXTO + "\n\nUsuário: " + mensagemUsuario)));

        Map<String, Object> body = new HashMap<>();
        body.put("contents", List.of(userContent));

        // Cabeçalhos
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        String url = GEMINI_URL + apiKey;

        System.out.println("Enviando requisição para Gemini...");

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        System.out.println("Resposta completa do Gemini: " + response.getBody());

        List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
        if (candidates != null && !candidates.isEmpty()) {
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, String>> respostaParts = (List<Map<String, String>>) content.get("parts");
            return respostaParts.get(0).get("text");
        }

        return "Desculpe, não consegui gerar uma resposta.";
    }
}
