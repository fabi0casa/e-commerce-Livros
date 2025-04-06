package com.fatec.livraria.service;

import org.springframework.stereotype.Service;

import com.fatec.livraria.entity.CartaoCredito;
import com.fatec.livraria.repository.CartaoCreditoRepository;
import com.fatec.livraria.repository.ClienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class CartaoCreditoService {

    @Autowired
    private CartaoCreditoRepository cartaoCreditoRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;

    public List<CartaoCredito> getCartaoByClienteId(Integer clienteId) {
        return cartaoCreditoRepository.findByCliente_Id(clienteId);
    }

    public List<CartaoCredito> getAllCartoes() {
        return cartaoCreditoRepository.findAll();
    }

    public Optional<CartaoCredito> getCartaoById(Integer id) {
        return cartaoCreditoRepository.findById(id);
    }

    public CartaoCredito salvarCartao(Integer clienteId, CartaoCredito cartaoCredito) {
        return clienteRepository.findById(clienteId).map(cliente -> {
            cartaoCredito.setCliente(cliente); // Associando o cliente ao cartão
            return cartaoCreditoRepository.save(cartaoCredito);
        }).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }


    public void deletarCartao(Integer id) {
        if (cartaoCreditoRepository.existsById(id)) {
            cartaoCreditoRepository.deleteById(id);
        }
    }

    public void removerPreferencialDosOutrosCartoes(Integer clienteId) {
        List<CartaoCredito> cartoesDoCliente = cartaoCreditoRepository.findByCliente_Id(clienteId);
        for (CartaoCredito c : cartoesDoCliente) {
            if (Boolean.TRUE.equals(c.getPreferencial())) {
                c.setPreferencial(false);
            }
        }
        cartaoCreditoRepository.saveAll(cartoesDoCliente);
    }
}
