package com.fatec.livraria.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendaRequest {

    @NotNull(message = "O ID do livro é obrigatório.")
    private Integer livroId;

    @NotNull(message = "A quantidade é obrigatória.")
    @Min(value = 1, message = "A quantidade mínima é 1.")
    private Integer quantidade;
}
