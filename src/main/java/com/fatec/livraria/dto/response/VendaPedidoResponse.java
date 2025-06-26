package com.fatec.livraria.dto.response;

import java.math.BigDecimal;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VendaPedidoResponse {
    private Integer id;
    private String status;
    private BigDecimal valor;
    private LivroPedidoResponse livro;
}
