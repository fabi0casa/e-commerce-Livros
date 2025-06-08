package com.fatec.livraria.scheduling;

import com.fatec.livraria.service.CarrinhoService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarrinhoScheduler {

    private final CarrinhoService carrinhoService;

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void limparCarrinhosAntigos() {
        carrinhoService.removerItensAntigosDoCarrinho();
    }
}
