package com.fatec.livraria.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PedidoRequest {
    private Integer clienteId;
    private Integer enderecoId;
    private String formaPagamento;
    private List<VendaRequest> vendas;
}
