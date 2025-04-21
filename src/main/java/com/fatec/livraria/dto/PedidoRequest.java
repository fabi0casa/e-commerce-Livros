package com.fatec.livraria.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PedidoRequest {

    @NotNull(message = "O ID do cliente é obrigatório.")
    private Integer clienteId;

    @NotNull(message = "O ID do endereço é obrigatório.")
    private Integer enderecoId;

    @NotBlank(message = "A forma de pagamento é obrigatória.")
    @Pattern(
        regexp = "Cartão de Crédito|Cupom|Cartão \\+ Cupom",
        message = "Forma de pagamento inválida. Deve ser 'Cartão de Crédito', 'Cupom' ou 'Cartão + Cupom'."
    )
    private String formaPagamento;

    @NotNull(message = "A lista de vendas não pode ser nula.")
    @Size(min = 1, message = "O pedido deve conter pelo menos uma venda.")
    private List<@Valid VendaRequest> vendas;
}
