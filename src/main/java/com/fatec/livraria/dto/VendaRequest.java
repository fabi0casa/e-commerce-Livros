package com.fatec.livraria.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendaRequest {
    private String formaPagamento;
    private Integer livroId;
    private Double valor;
}
