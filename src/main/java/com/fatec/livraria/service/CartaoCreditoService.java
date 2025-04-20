package com.fatec.livraria.service;

import org.springframework.stereotype.Service;

import com.fatec.livraria.dto.CartaoDTO;
import com.fatec.livraria.entity.Bandeira;
import com.fatec.livraria.entity.CartaoCredito;
import com.fatec.livraria.entity.Cliente;
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

    @Autowired
    private BandeiraService bandeiraService;

    @Autowired
    private ClienteService clienteService;

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

    public void adicionarCartao(CartaoDTO cartaoDTO) {
        // Validações de formato
        if (cartaoDTO.getNumeroCartao().length() < 13 || cartaoDTO.getNumeroCartao().length() > 19) {
            throw new IllegalArgumentException("Número do cartão inválido. O número deve ter entre 13 e 19 dígitos.");
        }

        if (cartaoDTO.getCodigoSeguranca().length() < 3 || cartaoDTO.getCodigoSeguranca().length() > 4) {
            throw new IllegalArgumentException("Código de segurança inválido.");
        }

        // Validações de existência
        Cliente cliente = clienteService.buscarPorId(cartaoDTO.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));

        Bandeira bandeira = bandeiraService.getBandeiraById(cartaoDTO.getBandeiraId())
                .orElseThrow(() -> new IllegalArgumentException("Bandeira do cartão não encontrada."));

        // Se for preferencial, remover dos outros
        if (cartaoDTO.isPreferencial()) {
            removerPreferencialDosOutrosCartoes(cartaoDTO.getClienteId());
        }

        CartaoCredito cartao = new CartaoCredito();
        cartao.setNumeroCartao(cartaoDTO.getNumeroCartao());
        cartao.setNomeImpresso(cartaoDTO.getNomeImpresso());
        cartao.setCodigoSeguranca(cartaoDTO.getCodigoSeguranca());
        cartao.setPreferencial(cartaoDTO.isPreferencial());
        cartao.setCliente(cliente);
        cartao.setBandeira(bandeira);

        cartaoCreditoRepository.save(cartao);
    }
}
