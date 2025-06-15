package com.fatec.livraria.entity;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CARTOES_DE_CREDITO")
@Getter
@Setter
@Audited
public class CartaoCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crt_id")
    private Integer id;

    @Column(name = "crt_numero", nullable = false, length = 45)
    private String numeroCartao;

    @Column(name = "crt_nome_impresso", nullable = false, length = 45)
    private String nomeImpresso;

    @Column(name = "crt_codigo_seguranca", nullable = false, length = 20)
    private String codigoSeguranca;

    @Column(name = "car_isPreferencial", nullable = false)
    private Boolean preferencial;

    @ManyToOne
    @JoinColumn(name = "crt_cli_id", nullable = false)
    @JsonIgnore
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "crt_ban_id", nullable = false)
    private Bandeira bandeira;
}

