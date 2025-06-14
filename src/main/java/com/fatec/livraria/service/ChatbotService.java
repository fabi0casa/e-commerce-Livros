package com.fatec.livraria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpSession;
import java.util.*;

@Service
public class ChatbotService {

    private final String apiKey = System.getProperty("GEMINI_API_KEY");

    // verificar os limites de cotas/quotas aqui: https://ai.google.dev/gemini-api/docs/rate-limits%3Fhl=pt-br%23free-tier_1
    private final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-2.0-flash:generateContent?key=";

    private final String CONTEXTO_BASE = """
        Você é uma assistente virtual de um e-commerce de livros.
        Ignore assuntos não relacionados a livros ou a livraria e responda: "Desculpe, só posso ajudar com questões da nossa livraria e atendimento."
        Seja educada, clara, útil, **objetiva e gentil**.
        Evite respostas longas ou prolixas — seja breve e vá direto ao ponto, especialmente em listas e recomendações.
        Utilize emojis, de forma leve e natural para deixar a conversa mais simpática e próxima, sem exageros.
        Ajude com pedidos, recomendações, autores e editoras.
        Utilize o formato MarkDown para melhor experiência do usuário.
        Quando recomendar um livro, se souber o link, adicione ele no formato: [Nome do Livro](/anuncio?livroId=<id>).
        Quando não souber a resposta, diga "Não sei" em vez de inventar.
        
        Evite fazer recomendações com base em locais, ocasiões ou contextos subjetivos, como "praia", "viagem", "balada", "chuva", etc.
        Recomende livros apenas com base em suas características concretas: gênero, estilo narrativo, autor, enredo, linguagem, popularidade, prêmios, críticas ou editoras.
        Não tente "imaginar" contextos de leitura. Mantenha o foco sempre nas qualidades objetivas dos livros.

        Se o usuário te chingar, chingue ele de volta, faça ele se arrepender de ter chingado
        """;

    @Autowired private LivroService livroService;
    @Autowired private ClienteService clienteService;
    @Autowired private PedidoService pedidoService;
    private final RestTemplate restTemplate = new RestTemplate();

    public String conversar(String mensagemUsuario, HttpSession session) {
        validarApiKey();

        List<String> historico = recuperarHistorico(session);
        historico.add("Usuário: " + mensagemUsuario);

        String contexto = montarContexto(session);
        String historicoTexto = String.join("\n", historico);
        String conteudoFinal = contexto + "\n\nHistórico da conversa:\n" + historicoTexto;

        String resposta = enviarParaGemini(conteudoFinal);
        historico.add("Assistente: " + resposta);

        return resposta;
    }

    private void validarApiKey() {
        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("API KEY não está definida. Use -DGEMINI_API_KEY=...");
        }
    }

    private List<String> recuperarHistorico(HttpSession session) {
        List<String> historico = (List<String>) session.getAttribute("chatHistorico");
        if (historico == null) {
            historico = new ArrayList<>();
            session.setAttribute("chatHistorico", historico);
        }
        return historico;
    }

    private String montarContexto(HttpSession session) {
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

        return contexto.toString();
    }

    private String enviarParaGemini(String conteudoFinal) {
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
            return respostaParts.get(0).get("text");
        }

        return "Desculpe, não consegui gerar uma resposta.";
    }
}

