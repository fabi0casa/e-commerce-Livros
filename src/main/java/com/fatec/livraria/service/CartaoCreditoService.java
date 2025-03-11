package com.fatec.livraria.service;

import org.springframework.stereotype.Service;

import com.fatec.livraria.entity.CartaoCredito;
import com.fatec.livraria.repository.CartaoCreditoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class CartaoCreditoService {

    @Autowired
    private CartaoCreditoRepository cartaoCreditoRepository;

    public List<CartaoCredito> getAllCartoes() {
        return cartaoCreditoRepository.findAll();
    }

    public Optional<CartaoCredito> getCartaoById(Integer id) {
        return cartaoCreditoRepository.findById(id);
    }

    public CartaoCredito salvarCartao(CartaoCredito cartaoCredito) {
        return cartaoCreditoRepository.save(cartaoCredito);
    }

    public void deletarCartao(Integer id) {
        if (cartaoCreditoRepository.existsById(id)) {
            cartaoCreditoRepository.deleteById(id);
        }
    }
}
