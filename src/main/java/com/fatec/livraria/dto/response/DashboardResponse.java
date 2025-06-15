package com.fatec.livraria.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private Integer totalLivros;
    private Integer totalClientes;
    private BigDecimal vendasMes;
    private BigDecimal ticketMedio;
    private List<BigDecimal> receitaUltimos30Dias;
    private Map<String, Long> pedidosPorStatus;
    private List<LabelValorResponse> topLivros;
    private List<LabelValorResponse> categoriasMaisVendidas;
}
