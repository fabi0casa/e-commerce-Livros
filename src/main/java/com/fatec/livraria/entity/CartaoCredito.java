package com.fatec.livraria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CARTOES_DE_CREDITO")
@Getter
@Setter
public class CartaoCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crt_id")
    private Integer id;

    @NotBlank(message = "O número do cartão é obrigatório.")
    @Column(name = "crt_numero", nullable = false, length = 45)
    private String numero;

    @NotBlank(message = "O nome impresso no cartão é obrigatório.")
    @Column(name = "crt_nome_impresso", nullable = false, length = 45)
    private String nomeImpresso;

    @NotBlank(message = "O código de segurança é obrigatório.")
    @Column(name = "crt_codigo_seguranca", nullable = false, length = 20)
    private String codigoSeguranca;

    @NotNull(message = "É necessário definir se o cartão é preferencial.")
    @Column(name = "car_isPreferencial", nullable = false)
    private Boolean isPreferencial;

    @NotNull(message = "O cliente é obrigatório.")
    @ManyToOne
    @JoinColumn(name = "crt_cli_id", nullable = false)
    private Cliente cliente;

    @NotNull(message = "A bandeira do cartão é obrigatória.")
    @ManyToOne
    @JoinColumn(name = "crt_ban_id", nullable = false)
    private Bandeira bandeira;
}

