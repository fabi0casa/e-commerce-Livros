package com.fatec.livraria.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnaliseResponse {
    private String nome;
    private List<DadoTemporal> dados;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DadoTemporal {
        private String label;
        private Long total;
    }
}
