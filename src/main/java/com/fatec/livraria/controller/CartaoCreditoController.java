package com.fatec.livraria.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fatec.livraria.dto.CartaoDTO;
import com.fatec.livraria.entity.Bandeira;
import com.fatec.livraria.entity.CartaoCredito;
import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.service.BandeiraService;
import com.fatec.livraria.service.CartaoCreditoService;
import com.fatec.livraria.service.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/cartoes")
public class CartaoCreditoController {

    @Autowired
    private CartaoCreditoService cartaoCreditoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private BandeiraService bandeiraService;

    @GetMapping
    public ResponseEntity<List<CartaoCredito>> getAllCartoes() {
        List<CartaoCredito> cartoes = cartaoCreditoService.getAllCartoes();
        return ResponseEntity.ok(cartoes);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CartaoCredito>> getCartaoByCliente(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(cartaoCreditoService.getCartaoByClienteId(clienteId));
    }


    @GetMapping("/{id}")
    public ResponseEntity<CartaoCredito> getCartaoById(@PathVariable Integer id) {
        return cartaoCreditoService.getCartaoById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<?> adicionarCartao(@RequestBody CartaoDTO cartaoDTO) {
        try {
            // Validações básicas
            if (cartaoDTO.getNumeroCartao().length() < 13 || cartaoDTO.getNumeroCartao().length() > 19) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", "Número do cartão inválido."));
            }

            if (cartaoDTO.getCodigoSeguranca().length() < 3 || cartaoDTO.getCodigoSeguranca().length() > 4) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", "Código de segurança inválido."));
            }

            Optional<Cliente> clienteOpt = clienteService.buscarPorId(cartaoDTO.getClienteId());
            if (clienteOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", "Cliente não encontrado."));
            }
            
            Optional<Bandeira> bandeiraOpt = bandeiraService.getBandeiraById(cartaoDTO.getBandeiraId());
            if (bandeiraOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", "Bandeira do cartão não encontrada."));
            }
            
            CartaoCredito cartao = new CartaoCredito();
            cartao.setNumeroCartao(cartaoDTO.getNumeroCartao());
            cartao.setNomeImpresso(cartaoDTO.getNomeImpresso());
            cartao.setBandeira(bandeiraOpt.get()); // Obtendo o objeto dentro do Optional
            cartao.setCodigoSeguranca(cartaoDTO.getCodigoSeguranca());
            cartao.setPreferencial(cartaoDTO.isPreferencial());
            cartao.setCliente(clienteOpt.get()); // Obtendo o objeto dentro do Optional
            
            cartaoCreditoService.salvarCartao(cartaoDTO.getClienteId(), cartao);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensagem", "Cartão cadastrado com sucesso!"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro ao cadastrar cartão."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCartao(@PathVariable Integer id) {
        cartaoCreditoService.deletarCartao(id);
        return ResponseEntity.noContent().build();
    }
}

