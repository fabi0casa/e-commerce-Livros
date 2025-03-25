package com.fatec.livraria.controller;

import java.util.List;
import com.fatec.livraria.service.ClienteService;
import com.fatec.livraria.service.TransacaoClienteService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.entity.TransacaoCliente;

@Controller
public class PageController {

    private ClienteService clienteService;

    @Autowired
    private TransacaoClienteService transacaoService;
    
    @Autowired
    public PageController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Autowired
    private ObjectMapper objectMapper; 
    
    @GetMapping("/administrador/gerenciar-clientes/gerenciarClientes")
    public String listarClientes(Model model) {
        List<Cliente> clientes = clienteService.listarTodos(); // Certifique-se de que este método retorna algo
        System.out.println("Clientes carregados: " + (clientes != null ? clientes.size() : "null"));
    
        model.addAttribute("clientes", clientes);

        try {
            model.addAttribute("clientesJson", objectMapper.writeValueAsString(clientes));
        } catch (Exception e) {
            model.addAttribute("clientesJson", "[]");
        }
        
        return "administrador/gerenciar-clientes/gerenciarClientes"; // Certifique-se de que o nome do HTML está correto
    }
    
    @GetMapping("/administrador/gerenciar-clientes/cadastrarCliente")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("cliente", new Cliente()); // Garante que o formulário tenha um objeto Cliente
        return "administrador/gerenciar-clientes/cadastrarCliente"; // Renderiza o template do formulário na mesma URL
    }

    @GetMapping("/administrador/gerenciar-clientes/transacoes")
    public String verTransacoes(@RequestParam("clienteId") int clienteId, Model model) {
        Cliente cliente = clienteService.buscarPorId(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));
    
        List<TransacaoCliente> transacoes = transacaoService.buscarPorClienteId(clienteId);
    
        model.addAttribute("cliente", cliente);
        model.addAttribute("transacoes", transacoes);
        return "/administrador/gerenciar-clientes/transacoes";
    }    
    
     @GetMapping("/{pagina:(?!api).*}")
    public String renderizarPagina(@PathVariable String pagina, Model model, HttpSession session) {
        adicionarClienteNoModelo(model, session);
        return pagina; // Busca "templates/{pagina}.html"
    }

    @GetMapping("/{dir}/{pagina}")
    public String renderizarPaginaComSubpasta(@PathVariable String dir, @PathVariable String pagina, Model model, HttpSession session) {
        adicionarClienteNoModelo(model, session);
        return dir + "/" + pagina; // Busca "templates/{dir}/{pagina}.html"
    }

    @GetMapping("/{dir1}/{dir2}/{pagina}")
    public String renderizarPaginaComDuasSubpastas(@PathVariable String dir1, @PathVariable String dir2, @PathVariable String pagina, Model model, HttpSession session) {
        adicionarClienteNoModelo(model, session);
        return dir1 + "/" + dir2 + "/" + pagina; // Busca "templates/{dir1}/{dir2}/{pagina}.html"
    }

    private void adicionarClienteNoModelo(Model model, HttpSession session) {
        Integer clienteId = (Integer) session.getAttribute("clienteId");
        model.addAttribute("clienteId", clienteId);
    }
}
