package com.fatec.livraria.service;

import com.fatec.livraria.entity.Categoria;
import com.fatec.livraria.entity.Livro;
import com.fatec.livraria.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }

    public Optional<Livro> buscarPorId(Integer id) {
        return livroRepository.findById(id);
    }

    public List<Livro> buscarPorNome(String nome) {
        return livroRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Livro> buscarPorIds(List<Integer> ids) {
        return livroRepository.findAllById(ids);
    }

    public String gerarContextoLivros() {
        List<Livro> livros = listarTodos();
    
        String contexto = livros.stream()
                .filter(livro -> livro.getEstoque() > 0)
                .map(livro -> {
                    String categorias = livro.getCategorias().stream()
                            .map(Categoria::getNome)
                            .collect(Collectors.joining(", "));
    
                    return "- \"" + livro.getNome() + "\" de " + livro.getAutor().getNome() +
                           " (Editora: " + livro.getEditora().getNome() +
                           ", Ano: " + livro.getAnoPublicacao() +
                           ", Nº Páginas: " + livro.getNumPaginas() +
                           ", Preço: R$ " + livro.getPrecoVenda() +
                           ", Grupo: " + livro.getGrupoPrecificacao().getNome() +
                           ", Categorias: " + categorias + ").\n" +
                           "Sinopse: " + livro.getSinopse() + "\n" +
                           "Link: /anuncio?livroId=" + livro.getId();
                })
                .collect(Collectors.joining("\n\n"));
    
        return contexto.isEmpty() ? "Nenhum livro disponível no sistema." : contexto;
    }    

}
