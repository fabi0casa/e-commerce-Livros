package com.fatec.livraria.dto.response;

import java.math.BigDecimal;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LivroCarrinhoResponse {
    private String nome;
    private String caminhoImagem;
    private String autor;
    private BigDecimal precoVenda;
}
