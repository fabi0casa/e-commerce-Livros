package com.fatec.livraria.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoResponse {
    private Integer id;
    private String codigo;

    public PedidoResponse(Integer id, String codigo) {
        this.id = id;
        this.codigo = codigo;
    }
}
