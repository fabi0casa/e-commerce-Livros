package com.fatec.livraria.dto.request;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartaoUpdateRequest {
    private String numeroCartao;
    private String nomeImpresso;
    private String codigoSeguranca;
    private Integer bandeiraId;
    private boolean preferencial;
}

