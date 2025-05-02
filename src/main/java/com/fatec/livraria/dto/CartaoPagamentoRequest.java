package com.fatec.livraria.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartaoPagamentoRequest {
    @NotNull(message = "O ID do cartão é obrigatório.")
    private Integer cartaoId;

    @NotNull(message = "O valor do pagamento é obrigatório.")
    @DecimalMin(value = "10.00", message = "O valor mínimo para cada cartão é 10 reais.")
    private BigDecimal valor;
}
