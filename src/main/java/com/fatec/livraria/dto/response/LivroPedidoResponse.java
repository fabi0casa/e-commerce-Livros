package com.fatec.livraria.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LivroPedidoResponse {
    private String nome;
    private String caminhoImagem;
}
