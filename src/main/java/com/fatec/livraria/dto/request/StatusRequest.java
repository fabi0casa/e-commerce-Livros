package com.fatec.livraria.dto.request;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusRequest {
    private List<Integer> vendaIds;
    private String status;
    private Boolean retornarEstoque;
}