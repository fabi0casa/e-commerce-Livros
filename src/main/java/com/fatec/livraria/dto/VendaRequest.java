package com.fatec.livraria.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendaRequest {

    @NotNull(message = "O ID do livro é obrigatório.")
    private Integer livroId;

    @NotNull(message = "O valor da venda é obrigatório.")
    @Positive(message = "O valor da venda deve ser maior que zero.")
    private Double valor;
}
