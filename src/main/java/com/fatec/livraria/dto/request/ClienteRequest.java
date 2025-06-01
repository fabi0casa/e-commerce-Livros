package com.fatec.livraria.dto.request;

import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteRequest {

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

    @Min(value = 0, message = "O ranking deve ser no mínimo 0.")
    private int ranking;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]+$",
        message = "A senha deve conter pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial."
    )
    private String senha;

    @NotNull(message = "O endereço é obrigatório.")
    @Size(min = 1, message = "Pelo menos um endereço deve ser informado.")
    private List<EnderecoRequest> enderecos;
}
