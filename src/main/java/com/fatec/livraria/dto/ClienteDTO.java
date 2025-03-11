package com.fatec.livraria.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {
    private String nome;

    @DateTimeFormat(pattern = "yyyy-MM-dd") // Define o formato esperado pelo Spring
    private Date dataNascimento;

    private String cpf;
    private String genero;
    private String email;
    private String telefone;
    private int ranking;
    private String senha;
    private List<EnderecoDTO> enderecos;
}
