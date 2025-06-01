package com.fatec.livraria.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PedidoRequest {

    @NotNull(message = "O ID do endereço é obrigatório.")
    private Integer enderecoId;

    @NotNull(message = "A lista de vendas não pode ser nula.")
    @Size(min = 1, message = "O pedido deve conter pelo menos uma venda.")
    private List<@Valid VendaRequest> vendas;

    private List<Integer> cuponsIds;

    @Size(min = 1, message = "É necessário fornecer pelo menos um cartão de pagamento.")
    private List<@Valid CartaoPagamentoRequest> cartoes;
}
