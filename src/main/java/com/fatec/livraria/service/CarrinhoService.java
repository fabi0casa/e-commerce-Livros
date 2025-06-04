package com.fatec.livraria.service;

import com.fatec.livraria.entity.Carrinho;
import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.entity.Livro;
import com.fatec.livraria.repository.CarrinhoRepository;
import com.fatec.livraria.repository.ClienteRepository;
import com.fatec.livraria.repository.LivroRepository;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final ClienteRepository clienteRepository;
    private final LivroRepository livroRepository;

    public CarrinhoService(CarrinhoRepository carrinhoRepository,
                           ClienteRepository clienteRepository,
                           LivroRepository livroRepository) {
        this.carrinhoRepository = carrinhoRepository;
        this.clienteRepository = clienteRepository;
        this.livroRepository = livroRepository;
    }

    private Optional<Cliente> getClienteDaSessao(HttpSession session) {
        Integer clienteId = (Integer) session.getAttribute("clienteId");
        if (clienteId == null) return Optional.empty();
        return clienteRepository.findById(clienteId);
    }

    private Carrinho validarPertinenciaECapturar(Integer carrinhoId, HttpSession session) {
        Cliente cliente = getClienteDaSessao(session)
                .orElseThrow(() -> new RuntimeException("Cliente não autenticado"));

        Carrinho carrinho = carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new RuntimeException("Item do carrinho não encontrado"));

        if (!carrinho.getCliente().getId().equals(cliente.getId())) {
            throw new RuntimeException("Este item não pertence ao cliente logado.");
        }

        return carrinho;
    }

    public Optional<Integer> calcularQuantidadeTotal(HttpSession session) {
        return getClienteDaSessao(session)
                .map(cliente -> {
                    List<Carrinho> itens = carrinhoRepository.findByCliente(cliente);
                    return itens.stream().mapToInt(Carrinho::getQuantidade).sum();
                });
    }    

    public Optional<List<Carrinho>> listarCarrinho(HttpSession session) {
        return getClienteDaSessao(session)
                .map(cliente -> carrinhoRepository.findByCliente(cliente));
    }

    public Optional<Carrinho> adicionarAoCarrinho(HttpSession session, Integer livroId, Integer quantidade) {
        Optional<Cliente> clienteOpt = getClienteDaSessao(session);
        if (clienteOpt.isEmpty()) return Optional.empty();

        Cliente cliente = clienteOpt.get();
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        List<Carrinho> itensCarrinho = carrinhoRepository.findByCliente(cliente);
        Optional<Carrinho> itemExistente = itensCarrinho.stream()
                .filter(c -> c.getLivro().getId().equals(livroId))
                .findFirst();

        if (itemExistente.isPresent()) {
            Carrinho carrinho = itemExistente.get();
            carrinho.setQuantidade(carrinho.getQuantidade() + quantidade);
            return Optional.of(carrinhoRepository.save(carrinho));
        }

        Carrinho novoItem = new Carrinho();
        novoItem.setCliente(cliente);
        novoItem.setLivro(livro);
        novoItem.setQuantidade(quantidade);
        novoItem.setData(new Date());

        return Optional.of(carrinhoRepository.save(novoItem));
    }

    public Carrinho atualizarQuantidade(Integer carrinhoId, Integer novaQuantidade, HttpSession session) {
        Carrinho carrinho = validarPertinenciaECapturar(carrinhoId, session);
        carrinho.setQuantidade(novaQuantidade);
        return carrinhoRepository.save(carrinho);
    }

    public void removerDoCarrinho(Integer carrinhoId, HttpSession session) {
        Carrinho carrinho = validarPertinenciaECapturar(carrinhoId, session);
        carrinhoRepository.delete(carrinho);
    }

    public boolean limparCarrinhoDoCliente(HttpSession session) {
        Optional<Cliente> clienteOpt = getClienteDaSessao(session);
        if (clienteOpt.isEmpty()) return false;
        carrinhoRepository.deleteAllByClienteId(clienteOpt.get().getId());
        return true;
    }
}

