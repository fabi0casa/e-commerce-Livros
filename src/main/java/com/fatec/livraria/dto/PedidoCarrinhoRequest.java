package com.fatec.livraria.dto;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class PedidoCarrinhoRequest {
    private Integer enderecoId;
    private List<Integer> cuponsIds;

    @Size(min = 1, message = "É necessário fornecer pelo menos um cartão de pagamento.")
    private List<@Valid CartaoPagamentoRequest> cartoes;
}

