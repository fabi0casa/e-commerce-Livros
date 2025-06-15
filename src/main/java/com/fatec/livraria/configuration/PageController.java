package com.fatec.livraria.configuration;
import com.fatec.livraria.service.ClienteService;
import jakarta.servlet.http.HttpSession;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

import com.fatec.livraria.entity.Cliente;

@Controller
public class PageController {

    private ClienteService clienteService;

    // Mapeia URLs personalizadas para caminhos reais dos templates
    private static final Map<String, String> rotasPersonalizadas = Map.ofEntries(
        Map.entry("home", "cliente/home"),
        Map.entry("carrinho", "cliente/carrinho"),
        Map.entry("conta", "cliente/conta"),
        Map.entry("buscar", "cliente/buscar"),
        Map.entry("login", "cliente/login"),
        Map.entry("anuncio", "cliente/anuncio"),
        Map.entry("pagamento", "cliente/pagamento/comprar"),
        Map.entry("pagar-carrinho", "cliente/pagamento/comprarDoCarrinho"),
        Map.entry("novo-cartao", "cliente/pagamento/novoCartao"),
        Map.entry("novo-endereco", "cliente/pagamento/novoEndereco"),

        Map.entry("dashboard", "dashboard"),
        Map.entry("analise", "administrador/analise-livros/analise"),
        Map.entry("estoque", "administrador/controle-estoque/gerenciarEstoque"),
        Map.entry("entrada-estoque", "administrador/controle-estoque/novaEntradaEstoque"),
        Map.entry("gerenciar-pedidos", "administrador/gerenciamento-vendas/gerenciarVendas"),
        Map.entry("logs", "administrador/logs/logs"),

        Map.entry("gerenciar-clientes", "administrador/gerenciar-clientes/gerenciarClientes"),
        Map.entry("ver-transacoes-cliente", "administrador/gerenciar-clientes/transacoes"),
        Map.entry("enderecos-cliente", "administrador/gerenciar-clientes/listarEndereco"),
        Map.entry("cartoes-cliente", "administrador/gerenciar-clientes/listarCartoes"),
        Map.entry("editar-endereco-cliente", "administrador/gerenciar-clientes/editarEndereco"),
        Map.entry("editar-cartao-cliente", "administrador/gerenciar-clientes/editarCartao"),
        Map.entry("editar-cliente", "administrador/gerenciar-clientes/editarCliente"),
        Map.entry("criar-endereco-cliente", "administrador/gerenciar-clientes/cadastrarEndereco"),
        Map.entry("criar-cartao-cliente", "administrador/gerenciar-clientes/cadastrarCartao"),
        Map.entry("alterar-senha-cliente", "administrador/gerenciar-clientes/alterarSenha"),

        Map.entry("erro", "erro/erro")
    );

    private static final Set<String> rotasPrivadas = Set.of(
        "carrinho", "conta", "pagamento", "pagar-carrinho", "novo-cartao", "novo-endereco"
    );

    private static final Set<String> rotasAdmin = Set.of(
        "dashboard", "analise", "estoque", "entrada-estoque", "gerenciar-pedidos", "logs",
        "gerenciar-clientes", "ver-transacoes-cliente", "enderecos-cliente", "editar-endereco-cliente",
        "editar-cliente", "criar-endereco-cliente", "criar-cartao-cliente", "alterar-senha-cliente",
        "cartoes-cliente", "editar-cartao-cliente"
    );


    @Autowired
    public PageController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
    
    //feito separadamente pois usa Thymeleaf
    @GetMapping("/cadastrar-cliente")
    public String exibirFormularioCadastro(Model model, HttpSession session) {
        Integer clienteId = (Integer) session.getAttribute("clienteId");
        if (clienteId == null) return "redirect:/login";

        Cliente cliente = clienteService.buscarPorId(clienteId).orElse(null);
        if (cliente == null || !cliente.isAdmin()) return "redirect:/home";
        
        model.addAttribute("cliente", new Cliente());
        return "administrador/gerenciar-clientes/cadastrarCliente";
    }

    @GetMapping("/cadastro")
    public String exibirTelaCadastroDoCliente(Model model, HttpSession session) {
        model.addAttribute("cliente", new Cliente());
        return "cliente/cadastro";
    }
    
    @GetMapping("/{rota}")
    public String renderizarRotaPersonalizada(@PathVariable String rota, Model model, HttpSession session) {
        adicionarClienteNoModelo(model, session);
    
        String template = rotasPersonalizadas.get(rota);
        if (template == null) return "erro/erro";
    
        Integer clienteId = (Integer) session.getAttribute("clienteId");

        if (rotasPrivadas.contains(rota) && clienteId == null) return "redirect:/login";

        if (rotasAdmin.contains(rota)) {
            if (clienteId == null) return "redirect:/login";

            Cliente cliente = clienteService.buscarPorId(clienteId).orElse(null);
            if (cliente == null || !cliente.isAdmin()) return "redirect:/home";
        }
    
        return template;
    }
    

    @GetMapping("/")
    public String redirecionarParaHome() {
        return "redirect:/home";
    }
    
    private void adicionarClienteNoModelo(Model model, HttpSession session) {
        Integer clienteId = (Integer) session.getAttribute("clienteId");
        model.addAttribute("clienteId", clienteId);
    }
}
