package com.fatec.livraria.service;

import com.fatec.livraria.entity.Carrinho;
import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.entity.Livro;
import com.fatec.livraria.repository.CarrinhoRepository;
import com.fatec.livraria.repository.ClienteRepository;
import com.fatec.livraria.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final ClienteRepository clienteRepository;
    private final LivroRepository livroRepository;

    public CarrinhoService(CarrinhoRepository carrinhoRepository, ClienteRepository clienteRepository, LivroRepository livroRepository) {
        this.carrinhoRepository = carrinhoRepository;
        this.clienteRepository = clienteRepository;
        this.livroRepository = livroRepository;
    }

    // Buscar carrinho de um cliente
    public List<Carrinho> listarCarrinho(Integer clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return carrinhoRepository.findByCliente(cliente);
    }

    // Adicionar item ao carrinho
    public Carrinho adicionarAoCarrinho(Integer clienteId, Integer livroId, Integer quantidade) {
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        Livro livro = livroRepository.findById(livroId).orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        // Verifica se o item já está no carrinho
        List<Carrinho> itensCarrinho = carrinhoRepository.findByCliente(cliente);
        Optional<Carrinho> itemExistente = itensCarrinho.stream()
                .filter(c -> c.getLivro().getId().equals(livroId))
                .findFirst();

        if (itemExistente.isPresent()) {
            Carrinho carrinho = itemExistente.get();
            carrinho.setQuantidade(carrinho.getQuantidade() + quantidade);
            return carrinhoRepository.save(carrinho);
        }

        // Se não existir, cria um novo item no carrinho
        Carrinho novoItem = new Carrinho();
        novoItem.setCliente(cliente);
        novoItem.setLivro(livro);
        novoItem.setQuantidade(quantidade);
        novoItem.setData(new Date());

        return carrinhoRepository.save(novoItem);
    }

    // Atualizar quantidade do item no carrinho
    public Carrinho atualizarQuantidade(Integer carrinhoId, Integer novaQuantidade) {
        Carrinho carrinho = carrinhoRepository.findById(carrinhoId).orElseThrow(() -> new RuntimeException("Item do carrinho não encontrado"));
        carrinho.setQuantidade(novaQuantidade);
        return carrinhoRepository.save(carrinho);
    }

    // Remover item do carrinho
    public void removerDoCarrinho(Integer carrinhoId) {
        if (!carrinhoRepository.existsById(carrinhoId)) {
            throw new RuntimeException("Item do carrinho não encontrado");
        }
        carrinhoRepository.deleteById(carrinhoId);
    }
}
