package com.fatec.livraria.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LabelValorResponse {
    private String label;
    private Long valor;
}
