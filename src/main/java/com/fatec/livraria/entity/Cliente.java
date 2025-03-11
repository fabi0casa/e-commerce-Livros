package com.fatec.livraria.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "CLIENTES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cli_id")
    private Integer id;

    @Column(name = "cli_nome", nullable = false, length = 45)
    private String nome;

    @Column(name = "cli_data_nascimento", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataNascimento;

    @Column(name = "cli_cpf", nullable = false, length = 20, unique = true)
    private String cpf;

    @Column(name = "cli_telefone", nullable = false, length = 20)
    private String telefone;

    @Column(name = "cli_email", nullable = false, length = 45, unique = true)
    private String email;

    @Column(name = "cli_senha", nullable = false, length = 45)
    private String senha;

    @Column(name = "cli_ranking", nullable = false)
    private Integer ranking;

    @Column(name = "cli_genero", nullable = false, length = 30)
    private String genero;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Endereco> enderecos = new ArrayList<>();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransacaoCliente> transacoes = new ArrayList<>();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartaoCredito> cartoes = new ArrayList<>();
}
