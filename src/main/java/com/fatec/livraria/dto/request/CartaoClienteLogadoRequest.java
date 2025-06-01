package com.fatec.livraria.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartaoClienteLogadoRequest {

    @NotBlank(message = "O número do cartão não pode estar vazio.")
    @Pattern(regexp = "\\d{13,19}", message = "Número do cartão inválido. Deve conter entre 13 e 19 dígitos.")
    private String numeroCartao;

    @NotBlank(message = "O nome impresso no cartão é obrigatório.")
    private String nomeImpresso;

    @NotNull(message = "O ID da bandeira do cartão é obrigatório.")
    private Integer bandeiraId;    

    @NotBlank(message = "O código de segurança é obrigatório.")
    @Pattern(regexp = "\\d{3,4}", message = "Código de segurança inválido. Deve ter 3 ou 4 dígitos.")
    private String codigoSeguranca;

    private boolean preferencial;
}