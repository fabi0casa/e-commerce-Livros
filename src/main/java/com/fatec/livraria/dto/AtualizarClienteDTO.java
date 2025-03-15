package com.fatec.livraria.dto;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AtualizarClienteDTO {

    // ✅ ID obrigatório para identificar o cliente a ser atualizado
    @NotNull(message = "O ID do cliente é obrigatório.")
    private int id;

    @NotBlank(message = "O nome não pode estar vazio.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    private String nome;

    @NotNull(message = "A data de nascimento é obrigatória.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataNascimento;

    @NotBlank(message = "O CPF é obrigatório.")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF inválido. Formato esperado: 000.000.000-00")
    private String cpf;

    @NotBlank(message = "O gênero é obrigatório.")
    @Pattern(regexp = "^(masculino|feminino|outro|nao_informar)$", message = "Gênero inválido.")
    private String genero;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "E-mail inválido. Formato esperado: exemplo@email.com")
    private String email;

    @NotBlank(message = "O telefone é obrigatório.")
    @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "Telefone inválido. Formato esperado: (XX) XXXXX-XXXX")
    private String telefone;
}

