package com.fatec.livraria.dto.response;

import java.math.BigDecimal;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LivroResumoResponse {
    private Integer id;
    private String nome;
    private String caminhoImagem;
    private BigDecimal precoVenda;
}
