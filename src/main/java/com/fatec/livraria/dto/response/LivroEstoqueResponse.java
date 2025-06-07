package com.fatec.livraria.dto.response;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LivroEstoqueResponse {

    private Integer livroId;
    private String nome;
    private Integer estoque;
    private BigDecimal precoCusto;
    private BigDecimal precoVenda;
    private String grupoPrecificacao;
    private BigDecimal margemLucro;
    private String fornecedor;

}
