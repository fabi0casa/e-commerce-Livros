package com.fatec.livraria.service;

import com.fatec.livraria.dto.request.EntradaEstoqueRequest;
import com.fatec.livraria.dto.response.LivroEstoqueResponse;
import com.fatec.livraria.dto.response.NomeIdResponse;
import com.fatec.livraria.entity.Categoria;
import com.fatec.livraria.entity.Fornecedor;
import com.fatec.livraria.entity.GrupoPrecificacao;
import com.fatec.livraria.entity.Livro;
import com.fatec.livraria.repository.GrupoPrecificacaoRepository;
import com.fatec.livraria.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final GrupoPrecificacaoRepository grupoPrecificacaoRepository;

    public LivroService(LivroRepository livroRepository, GrupoPrecificacaoRepository grupoPrecificacaoRepository) {
        this.livroRepository = livroRepository;
        this.grupoPrecificacaoRepository = grupoPrecificacaoRepository;
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

    public List<LivroEstoqueResponse> listarLivrosParaEstoque() {
        return livroRepository.findAll().stream().map(livro ->
            new LivroEstoqueResponse(
                livro.getId(),
                livro.getNome(),
                livro.getEstoque(),
                livro.getPrecoCusto(),
                livro.getPrecoVenda(),
                livro.getGrupoPrecificacao().getNome(),
                livro.getGrupoPrecificacao().getMargemLucro(),
                livro.getFornecedor().getNome()
            )
        ).collect(Collectors.toList());
    }
    
    public boolean atualizarEstoque(EntradaEstoqueRequest dto) {
        Optional<Livro> livroOpt = livroRepository.findById(dto.getLivroId());
        if (livroOpt.isEmpty()) {
            return false;
        }

        Livro livro = livroOpt.get();

        livro.setEstoque(livro.getEstoque() + dto.getQuantidadeAdicional());
        if (dto.getNovoPrecoCusto().compareTo(livro.getPrecoCusto()) > 0) livro.setPrecoCusto(dto.getNovoPrecoCusto());

        Optional<GrupoPrecificacao> grupoOpt = grupoPrecificacaoRepository.findById(dto.getGrupoPrecificacaoId());
        if (grupoOpt.isEmpty()) {
            return false;
        }

        GrupoPrecificacao grupo = grupoOpt.get();
        livro.setGrupoPrecificacao(grupo);

        BigDecimal margem = grupo.getMargemLucro(); 
        BigDecimal fator = margem.divide(BigDecimal.valueOf(100)).add(BigDecimal.ONE);
        BigDecimal novoPrecoVenda = livro.getPrecoCusto().multiply(fator).setScale(2, RoundingMode.HALF_UP);
        livro.setPrecoVenda(novoPrecoVenda);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(dto.getFornecedorId());
        livro.setFornecedor(fornecedor);

        livroRepository.save(livro);

        return true;
    }


    public List<NomeIdResponse> listarNomeEId() {
        return livroRepository.findAll()
            .stream()
            .map(f -> new NomeIdResponse(f.getId(), f.getNome()))
            .toList();
    }

    public Optional<NomeIdResponse> buscarNomePorId(Integer id) {
        return livroRepository.findById(id)
            .map(livro -> new NomeIdResponse(livro.getId(), livro.getNome()));
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
