package com.fatec.livraria.controller;

import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.entity.Livro;
import com.fatec.livraria.entity.Pedido;
import com.fatec.livraria.service.ClienteService;
import com.fatec.livraria.service.LivroService;
import com.fatec.livraria.service.PedidoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
public class ChatbotController {

    private final String apiKey = System.getProperty("GEMINI_API_KEY");

    // verificar os limites de cotas/quotas aqui: https://ai.google.dev/gemini-api/docs/rate-limits%3Fhl=pt-br%23free-tier_1
    private final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-2.0-flash-lite:generateContent?key=";
    private final RestTemplate restTemplate = new RestTemplate();

    private final String CONTEXTO_BASE = """
        Você é uma assistente virtual de um e-commerce de livros.
        Seja educada, clara e útil. Ajude com pedidos, recomendações, autores e editoras.
        Quando não souber a resposta, diga "Não sei" em vez de inventar.
        """;

    @Autowired private LivroService livroService;
    @Autowired private ClienteService clienteService;
    @Autowired private PedidoService pedidoService;

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
            String resposta = chamarGemini(mensagem, session);
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

    private String chamarGemini(String mensagemUsuario, HttpSession session) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("API KEY não está definida. Use -DGEMINI_API_KEY=...");
        }
    
        // Recupera ou cria histórico da sessão
        List<String> historico = (List<String>) session.getAttribute("chatHistorico");
        if (historico == null) {
            historico = new ArrayList<>();
            session.setAttribute("chatHistorico", historico);
        }
    
        // Adiciona nova mensagem ao histórico
        historico.add("Usuário: " + mensagemUsuario);
    
        // Monta o histórico em texto
        String historicoTexto = String.join("\n", historico);
    
        StringBuilder contexto = new StringBuilder();
        contexto.append(CONTEXTO_BASE)
                .append("\n\nLivros disponíveis no sistema:\n")
                .append(livroService.gerarContextoLivros());
    
        Integer clienteId = (Integer) session.getAttribute("clienteId");
        if (clienteId != null) {
            contexto.append("\n\nDados do usuário logado:\n")
                    .append(clienteService.gerarContextoCliente(clienteId))
                    .append("\n\n")
                    .append(pedidoService.gerarContextoPedidos(clienteId));
        }
    
        // Prepara conteúdo completo com contexto e histórico
        String conteudoFinal = contexto + "\n\nHistórico da conversa:\n" + historicoTexto;
    
        Map<String, Object> userContent = new HashMap<>();
        userContent.put("role", "user");
        userContent.put("parts", List.of(Map.of("text", conteudoFinal)));
    
        Map<String, Object> body = new HashMap<>();
        body.put("contents", List.of(userContent));
    
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
            String resposta = respostaParts.get(0).get("text");
    
            // Salva a resposta no histórico também
            historico.add("Assistente: " + resposta);
    
            return resposta;
        }
    
        return "Desculpe, não consegui gerar uma resposta.";
    }    
}
