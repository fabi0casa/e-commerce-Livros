package com.fatec.livraria.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ENDEREÇOS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "end_id")
    private Integer id;

    @Column(name = "end_tipo", nullable = false, length = 45)
    private String tipo;

    @Column(name = "end_logradouro", nullable = false, length = 45)
    private String logradouro;

    @Column(name = "end_numero", nullable = false, length = 10)
    private String numero;

    @Column(name = "end_bairro", nullable = false, length = 45)
    private String bairro;

    @Column(name = "end_cep", nullable = false, length = 10)
    private String cep;

    @Column(name = "end_cidade", nullable = false, length = 45)
    private String cidade;

    @Column(name = "end_estado", nullable = false, length = 45)
    private String estado;

    @Column(name = "end_pais", nullable = false, length = 45)
    private String pais;

    @Column(name = "end_observacoes", length = 255)
    private String observacoes;

    @Column(name = "end_frase_identificadora", nullable = false, length = 125)
    private String fraseIdentificadora;

    @Column(name = "end_isResidencial", nullable = false)
    private Boolean residencial;

    @Column(name = "end_isEntrega", nullable = false)
    private Boolean entrega;

    @Column(name = "end_isCobranca", nullable = false)
    private Boolean cobranca;

    @ManyToOne
    @JoinColumn(name = "end_cli_id", nullable = false)
    @JsonIgnore
    private Cliente cliente;

    public void gerarFraseIdentificadora() {
        this.fraseIdentificadora = String.format("%s, %s - %s, %s", logradouro, numero, cidade, estado);
    }
    
}
