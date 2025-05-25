package com.fatec.livraria.configuration;
import com.fatec.livraria.service.ClienteService;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

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

        Map.entry("gerenciar-clientes", "administrador/gerenciar-clientes/gerenciarClientes"),
        Map.entry("ver-transacoes-cliente", "administrador/gerenciar-clientes/transacoes"),
        Map.entry("enderecos-cliente", "administrador/gerenciar-clientes/listarEndereco"),
        Map.entry("editar-endereco-cliente", "administrador/gerenciar-clientes/editarEndereco"),
        Map.entry("editar-cliente", "administrador/gerenciar-clientes/editarCliente"),
        Map.entry("criar-endereco-cliente", "administrador/gerenciar-clientes/cadastrarEndereco"),
        //Map.entry("cadastrar-cliente", "administrador/gerenciar-clientes/cadastrarCliente"),
        Map.entry("criar-cartao-cliente", "administrador/gerenciar-clientes/cadastrarCartao"),
        Map.entry("nova-senha-cliente", "administrador/gerenciar-clientes/alterarSenha"),

        Map.entry("erro", "erro/erro")
    );

    @Autowired
    public PageController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
    
    //feito separadamente pois usa Thymeleaf
    @GetMapping("/cadastrar-cliente")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("cliente", new Cliente()); // Garante que o formulário tenha um objeto Cliente
        return "administrador/gerenciar-clientes/cadastrarCliente"; // Renderiza o template do formulário na mesma URL
    }
    
    @GetMapping("/{rota}")
    public String renderizarRotaPersonalizada(@PathVariable String rota, Model model, HttpSession session) {
        adicionarClienteNoModelo(model, session);
        
        String template = rotasPersonalizadas.get(rota);
        if (template != null) {
            return template;
        }

        // fallback opcional (ex: tenta renderizar uma página com nome idêntico à URL)
        return rota;
    }

    @GetMapping("/")
    public String redirecionarParaHome() {
        return "redirect:/home";
    }


    /*@GetMapping("/{pagina:(?!api).*}")
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
    }*/

    private void adicionarClienteNoModelo(Model model, HttpSession session) {
        Integer clienteId = (Integer) session.getAttribute("clienteId");
        model.addAttribute("clienteId", clienteId);
    }
}
