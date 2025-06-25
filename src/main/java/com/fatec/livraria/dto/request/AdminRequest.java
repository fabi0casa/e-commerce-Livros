package com.fatec.livraria.dto.request;

import java.util.Date;
import java.util.List;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminRequest {
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String senha;
    private String genero;
    private Date dataNascimento;
    private Boolean admin;
}
