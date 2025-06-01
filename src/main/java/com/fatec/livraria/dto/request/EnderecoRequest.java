package com.fatec.livraria.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoRequest {

    @NotBlank(message = "O tipo de logradouro é obrigatório.")
    @Size(min = 3, max = 50, message = "O tipo de logradouro deve ter entre 3 e 50 caracteres.")
    private String tipo;

    @NotBlank(message = "O logradouro é obrigatório.")
    @Size(min = 3, max = 100, message = "O logradouro deve ter entre 3 e 100 caracteres.")
    private String logradouro;

    @NotBlank(message = "O número é obrigatório.")
    @Pattern(regexp = "^[0-9A-Za-z]+$", message = "O número deve conter apenas números e letras.")
    private String numero;

    @NotBlank(message = "O bairro é obrigatório.")
    @Size(min = 3, max = 50, message = "O bairro deve ter entre 3 e 50 caracteres.")
    private String bairro;

    @NotBlank(message = "O CEP é obrigatório.")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP inválido. Formato esperado: 00000-000")
    private String cep;

    @NotBlank(message = "A cidade é obrigatória.")
    @Size(min = 2, max = 50, message = "A cidade deve ter entre 2 e 50 caracteres.")
    private String cidade;

    @NotBlank(message = "O estado é obrigatório.")
    @Size(min = 2, max = 50, message = "O estado deve ter entre 2 e 50 caracteres.")
    private String estado;

    @NotBlank(message = "O país é obrigatório.")
    @Size(min = 2, max = 50, message = "O país deve ter entre 2 e 50 caracteres.")
    private String pais;

    private String observacoes; 

    private String fraseIdentificadora;

    @NotNull(message = "Deve informar se o endereço é residencial ou não.")
    private Boolean residencial;

    @NotNull(message = "Deve informar se o endereço é de entrega ou não.")
    private Boolean entrega;

    @NotNull(message = "Deve informar se o endereço é de cobrança ou não.")
    private Boolean cobranca;
}
