package com.fatec.livraria.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoCompletoResponse {
    private Integer id;
    private String codigo;
    private BigDecimal valor;
    private String formaPagamento;
    private String dataCriacao;
    private String nomeCliente;
    private String fraseIdentificadoraEndereco;
    private List<VendaPedidoResponse> vendas;
}
