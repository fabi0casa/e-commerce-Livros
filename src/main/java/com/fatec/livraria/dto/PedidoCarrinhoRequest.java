package com.fatec.livraria.dto;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class PedidoCarrinhoRequest {
    private Integer clienteId;
    private Integer enderecoId;
    private List<Integer> cuponsIds;
}

