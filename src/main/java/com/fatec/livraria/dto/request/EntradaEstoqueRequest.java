package com.fatec.livraria.dto.request;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntradaEstoqueRequest {
    private Integer livroId;
    private Integer quantidadeAdicional;
    private BigDecimal novoPrecoCusto;
    private Integer grupoPrecificacaoId;
    private Integer fornecedorId;
}
