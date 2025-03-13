package com.fatec.livraria.controller;

import java.util.List;
import com.fatec.livraria.service.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatec.livraria.entity.Cliente;

@Controller
public class PageController {

    private ClienteService clienteService;
    
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
    
    //listagem automatica das páginas
    @GetMapping("/{pagina:(?!api).*}") // Evita conflitos com APIs, se houver
    public String renderizarPagina(@PathVariable String pagina) {
        return pagina; // Busca "templates/{pagina}.html"
    }

    @GetMapping("/{dir}/{pagina}") // Para páginas dentro de subpastas
    public String renderizarPaginaComSubpasta(@PathVariable String dir, @PathVariable String pagina) {
        return dir + "/" + pagina; // Busca "templates/{dir}/{pagina}.html"
    }

    @GetMapping("/{dir1}/{dir2}/{pagina}") // Para páginas com duas subpastas
    public String renderizarPaginaComDuasSubpastas(@PathVariable String dir1, @PathVariable String dir2, @PathVariable String pagina) {
        return dir1 + "/" + dir2 + "/" + pagina; // Busca "templates/{dir1}/{dir2}/{pagina}.html"
    }
}
