package com.fatec.livraria.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusQuantidadeResponse {
    private String status;
    private Long total;
}
