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
import java.time.temporal.ChronoUnit;
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
        long dias = ChronoUnit.DAYS.between(dataInicio, dataFim);
    
        for (Integer id : livroIds) {
            Livro livro = livroRepository.findById(id).orElseThrow();
            List<AnaliseResponse.DadoTemporal> dados = new ArrayList<>();
    
            LocalDate atual = dataInicio;
            while (!atual.isAfter(dataFim)) {
                LocalDate proximaData;
                if (dias <= 90) {
                    proximaData = atual.plusDays(1);
                } else if (dias < 365 * 3) {
                    proximaData = atual.plusMonths(1);
                } else if (dias < 365 * 8) {
                    proximaData = atual.plusMonths(6);
                } else {
                    proximaData = atual.plusYears(1);
                }
    
                LocalDate fimPeriodo = proximaData.minusDays(1);
                if (fimPeriodo.isAfter(dataFim)) fimPeriodo = dataFim;
    
                Long total = vendaRepository.countVendasLivroNoPeriodo(id, atual, fimPeriodo);
                String label = gerarLabel(atual, fimPeriodo, dias);
    
                dados.add(new AnaliseResponse.DadoTemporal(label, total));
    
                atual = proximaData;
            }
    
            resultado.add(new AnaliseResponse(livro.getNome(), dados));
        }
    
        return resultado;
    }
    
    public List<AnaliseResponse> analisarVendasCategoriaPorData(List<Integer> categoriaIds, LocalDate dataInicio, LocalDate dataFim) {
        List<AnaliseResponse> resultado = new ArrayList<>();
        long dias = ChronoUnit.DAYS.between(dataInicio, dataFim);
    
        for (Integer id : categoriaIds) {
            Categoria categoria = categoriaRepository.findById(id).orElseThrow();
            List<AnaliseResponse.DadoTemporal> dados = new ArrayList<>();
    
            LocalDate atual = dataInicio;
            while (!atual.isAfter(dataFim)) {
                LocalDate proximaData;
                if (dias <= 90) {
                    proximaData = atual.plusDays(1);
                } else if (dias < 365 * 3) {
                    proximaData = atual.plusMonths(1);
                } else if (dias < 365 * 8) {
                    proximaData = atual.plusMonths(6);
                } else {
                    proximaData = atual.plusYears(1);
                }
    
                LocalDate fimPeriodo = proximaData.minusDays(1);
                if (fimPeriodo.isAfter(dataFim)) fimPeriodo = dataFim;
    
                Long total = vendaRepository.countVendasCategoriaNoPeriodo(id, atual, fimPeriodo);
                String label = gerarLabel(atual, fimPeriodo, dias);
    
                dados.add(new AnaliseResponse.DadoTemporal(label, total));
    
                atual = proximaData;
            }
    
            resultado.add(new AnaliseResponse(categoria.getNome(), dados));
        }
    
        return resultado;
    }
    
    private String gerarLabel(LocalDate inicio, LocalDate fim, long dias) {
        if (dias <= 90) {
            return inicio.toString(); // formato yyyy-MM-dd
        } else if (dias < 365 * 3) {
            return String.format("%02d/%d", inicio.getMonthValue(), inicio.getYear()); // ex: 06/2025
        } else if (dias < 365 * 8) {
            return (inicio.getMonthValue() <= 6 ? "1º" : "2º") + " semestre de " + inicio.getYear();
        } else {
            return String.valueOf(inicio.getYear());
        }
    }
    
}
