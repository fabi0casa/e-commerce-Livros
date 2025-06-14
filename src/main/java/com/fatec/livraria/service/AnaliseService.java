package com.fatec.livraria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fatec.livraria.dto.response.AnaliseResponse;
import com.fatec.livraria.dto.response.NomeIdResponse;
import com.fatec.livraria.entity.Categoria;
import com.fatec.livraria.entity.Livro;
import com.fatec.livraria.repository.LivroRepository;
import com.fatec.livraria.repository.VendaRepository;
import com.fatec.livraria.repository.CategoriaRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnaliseService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private VendaRepository vendaRepository;

    public List<NomeIdResponse> analisarLivros(String nome) {
        List<Livro> livros = (nome != null && !nome.isBlank())
            ? livroRepository.findByNomeContainingIgnoreCase(nome)
            : livroRepository.findAll();

        return livros.stream()
            .map(livro -> {
                return new NomeIdResponse(livro.getId(), livro.getNome());
            })
            .toList();
    }

    public List<NomeIdResponse> analisarCategorias(String nome) {
        List<Categoria> categorias = (nome != null && !nome.isBlank())
            ? categoriaRepository.findByNomeContainingIgnoreCase(nome)
            : categoriaRepository.findAll();

        return categorias.stream()
            .map(cat -> {
                return new NomeIdResponse(cat.getId(), cat.getNome());
            })
            .toList();
    }

    public List<AnaliseResponse> analisarVendasLivroPorData(List<Integer> livroIds, LocalDate dataInicio, LocalDate dataFim) {
         List<AnaliseResponse> resultado = new ArrayList<>();

        for (Integer id : livroIds) {
            Livro livro = livroRepository.findById(id).orElseThrow();
            List<AnaliseResponse.DadoTemporal> dados = new ArrayList<>();

            LocalDate atual = dataInicio;
            while (!atual.isAfter(dataFim)) {
                LocalDate diaSeguinte = atual.plusDays(1);
                Long total = vendaRepository.countVendasLivroNoPeriodo(id, atual, diaSeguinte.minusDays(1));

                AnaliseResponse.DadoTemporal d = new AnaliseResponse.DadoTemporal();
                d.setData(atual);
                d.setTotal(total);
                dados.add(d);

                atual = diaSeguinte;
            }

            AnaliseResponse r = new AnaliseResponse();
            r.setNome(livro.getNome());
            r.setDados(dados);

            resultado.add(r);
        }

        return resultado;
    }

    public List<AnaliseResponse> analisarVendasCategoriaPorData(List<Integer> categoriaIds, LocalDate dataInicio, LocalDate dataFim) {
        List<AnaliseResponse> resultado = new ArrayList<>();

        for (Integer id : categoriaIds) {
            Categoria categoria = categoriaRepository.findById(id).orElseThrow();
            List<AnaliseResponse.DadoTemporal> dados = new ArrayList<>();

            LocalDate atual = dataInicio;
            while (!atual.isAfter(dataFim)) {
                LocalDate diaSeguinte = atual.plusDays(1);
                Long total = vendaRepository.countVendasCategoriaNoPeriodo(id, atual, diaSeguinte.minusDays(1));

                AnaliseResponse.DadoTemporal d = new AnaliseResponse.DadoTemporal();
                d.setData(atual);
                d.setTotal(total);
                dados.add(d);

                atual = diaSeguinte;
            }

            AnaliseResponse r = new AnaliseResponse();
            r.setNome(categoria.getNome());
            r.setDados(dados);
            resultado.add(r);
        }

        return resultado;
    }
}
