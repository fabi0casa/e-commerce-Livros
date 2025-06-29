package com.fatec.livraria.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarrinhoResponse {
    private Integer id;
    private Integer quantidade;
    private LivroCarrinhoResponse livro;
}
