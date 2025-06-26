package com.fatec.livraria.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LivroDetalhadoResponse {
    private Integer id;
    private String nome;
    private String caminhoImagem;
    private BigDecimal precoVenda;
    private String autor;
    private String editora;
    private Integer anoPublicacao;
    private String edicao;
    private Integer numPaginas;
    private String sinopse;
    private List<String> categorias;
}
