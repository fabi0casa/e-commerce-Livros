package com.fatec.livraria.configuration;
import com.fatec.livraria.service.ClienteService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

import com.fatec.livraria.entity.Cliente;

@Controller
public class PageController {

    private ClienteService clienteService;
    
    @Autowired
    public PageController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
    
    @GetMapping("/administrador/gerenciar-clientes/cadastrarCliente")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("cliente", new Cliente()); // Garante que o formulário tenha um objeto Cliente
        return "administrador/gerenciar-clientes/cadastrarCliente"; // Renderiza o template do formulário na mesma URL
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

    @GetMapping("/{dir1}/{dir2}/{dir3}/{pagina}")
    public String renderizarPaginaComTresSubpastas(
            @PathVariable String dir1,
            @PathVariable String dir2,
            @PathVariable String dir3,
            @PathVariable String pagina,
            Model model,
            HttpSession session) {
        
        adicionarClienteNoModelo(model, session);
        return dir1 + "/" + dir2 + "/" + dir3 + "/" + pagina; // Busca "templates/{dir1}/{dir2}/{dir3}/{pagina}.html"
    }

    private void adicionarClienteNoModelo(Model model, HttpSession session) {
        Integer clienteId = (Integer) session.getAttribute("clienteId");
        model.addAttribute("clienteId", clienteId);
    }
}
