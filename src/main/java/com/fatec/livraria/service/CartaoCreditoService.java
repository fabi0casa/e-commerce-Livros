package com.fatec.livraria.service;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fatec.livraria.dto.request.CartaoClienteLogadoRequest;
import com.fatec.livraria.dto.request.CartaoRequest;
import com.fatec.livraria.dto.request.CartaoUpdateRequest;
import com.fatec.livraria.entity.Bandeira;
import com.fatec.livraria.entity.CartaoCredito;
import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.repository.CartaoCreditoRepository;
import com.fatec.livraria.repository.ClienteRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@Service
public class CartaoCreditoService {

    @Autowired private CartaoCreditoRepository cartaoCreditoRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private BandeiraService bandeiraService;
    @Autowired private ClienteService clienteService;
    @Autowired private NotificacaoService notificacaoService;

    public List<CartaoCredito> getCartaoByClienteId(Integer clienteId) {
        return cartaoCreditoRepository.findByCliente_IdOrderByPreferencialDesc(clienteId);
    }    

    public List<CartaoCredito> getAllCartoes() {
        return cartaoCreditoRepository.findAll();
    }

    public Optional<CartaoCredito> buscarPorId(Integer id) {
        return cartaoCreditoRepository.findById(id);
    }

    public CartaoCredito buscarCartaoDoClienteLogado(Integer cartaoId, Integer clienteId) {
        CartaoCredito cartao = cartaoCreditoRepository.findById(cartaoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado"));
    
        if (!cartao.getCliente().getId().equals(clienteId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem acesso a este cartão");
        }
    
        return cartao;
    }    

    public CartaoCredito salvarCartao(Integer clienteId, CartaoCredito cartaoCredito) {
        return clienteRepository.findById(clienteId).map(cliente -> {
            cartaoCredito.setCliente(cliente); // Associando o cliente ao cartão
            return cartaoCreditoRepository.save(cartaoCredito);
        }).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }


    public void deletarCartao(Integer id) {
        CartaoCredito cartao = cartaoCreditoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cartão não encontrado"));

        Cliente cliente = cartao.getCliente();
        if (cliente != null) {
            cliente.getCartoes().remove(cartao);
        }

        cartao.setCliente(null);
        cartaoCreditoRepository.delete(cartao);
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

    public void adicionarCartao(CartaoRequest cartaoRequest) {
        // Validações de formato
        if (cartaoRequest.getNumeroCartao().length() < 13 || cartaoRequest.getNumeroCartao().length() > 19) {
            throw new IllegalArgumentException("Número do cartão inválido. O número deve ter entre 13 e 19 dígitos.");
        }

        if (cartaoRequest.getCodigoSeguranca().length() < 3 || cartaoRequest.getCodigoSeguranca().length() > 4) {
            throw new IllegalArgumentException("Código de segurança inválido.");
        }

        // Validações de existência
        Cliente cliente = clienteService.buscarPorId(cartaoRequest.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));

        Bandeira bandeira = bandeiraService.getBandeiraById(cartaoRequest.getBandeiraId())
                .orElseThrow(() -> new IllegalArgumentException("Bandeira do cartão não encontrada."));

        // Se for preferencial, remover dos outros
        if (cartaoRequest.isPreferencial()) {
            removerPreferencialDosOutrosCartoes(cartaoRequest.getClienteId());
        }

        CartaoCredito cartao = new CartaoCredito();
        cartao.setNumeroCartao(cartaoRequest.getNumeroCartao());
        cartao.setNomeImpresso(cartaoRequest.getNomeImpresso());
        cartao.setCodigoSeguranca(cartaoRequest.getCodigoSeguranca());
        cartao.setPreferencial(cartaoRequest.isPreferencial());
        cartao.setCliente(cliente);
        cartao.setBandeira(bandeira);

        cartaoCreditoRepository.save(cartao);

        notificacaoService.criarNotificacao(
            "💳 Novo Cartão",
            "Um novo Cartão foi adicionado a sua conta!",
            cliente.getId()
        );
    }

    public void adicionarCartaoAoClienteLogado(CartaoClienteLogadoRequest cartaoClienteLogadoRequest, HttpSession session) {
        // Validações de formato
        if (cartaoClienteLogadoRequest.getNumeroCartao().length() < 13 || cartaoClienteLogadoRequest.getNumeroCartao().length() > 19) {
            throw new IllegalArgumentException("Número do cartão inválido. O número deve ter entre 13 e 19 dígitos.");
        }

        if (cartaoClienteLogadoRequest.getCodigoSeguranca().length() < 3 || cartaoClienteLogadoRequest.getCodigoSeguranca().length() > 4) {
            throw new IllegalArgumentException("Código de segurança inválido.");
        }

        Integer clienteId = (Integer) session.getAttribute("clienteId");

        // Validações de existência
        Cliente cliente = clienteService.buscarPorId(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Bandeira bandeira = bandeiraService.getBandeiraById(cartaoClienteLogadoRequest.getBandeiraId())
                .orElseThrow(() -> new IllegalArgumentException("Bandeira do cartão não encontrada."));

        // Se for preferencial, remover dos outros
        if (cartaoClienteLogadoRequest.isPreferencial()) {
            removerPreferencialDosOutrosCartoes(clienteId);
        }

        CartaoCredito cartao = new CartaoCredito();
        cartao.setNumeroCartao(cartaoClienteLogadoRequest.getNumeroCartao());
        cartao.setNomeImpresso(cartaoClienteLogadoRequest.getNomeImpresso());
        cartao.setCodigoSeguranca(cartaoClienteLogadoRequest.getCodigoSeguranca());
        cartao.setPreferencial(cartaoClienteLogadoRequest.isPreferencial());
        cartao.setCliente(cliente);
        cartao.setBandeira(bandeira);

        cartaoCreditoRepository.save(cartao);
        
        notificacaoService.criarNotificacao(
            "💳 Novo Cartão",
            "Um novo Cartão foi adicionado a sua conta!",
            cliente.getId()
        );
    }

    public void atualizarCartao(Integer cartaoId, CartaoUpdateRequest request, HttpSession session) {
        CartaoCredito cartao = cartaoCreditoRepository.findById(cartaoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado"));

        Integer clienteId = cartao.getCliente().getId();

        // Validações
        if (request.getNumeroCartao().length() < 13 || request.getNumeroCartao().length() > 19) {
            throw new IllegalArgumentException("Número do cartão inválido. O número deve ter entre 13 e 19 dígitos.");
        }

        if (request.getCodigoSeguranca().length() < 3 || request.getCodigoSeguranca().length() > 4) {
            throw new IllegalArgumentException("Código de segurança inválido.");
        }

        Bandeira bandeira = bandeiraService.getBandeiraById(request.getBandeiraId())
            .orElseThrow(() -> new IllegalArgumentException("Bandeira do cartão não encontrada."));

        // Atualiza os dados
        cartao.setNumeroCartao(request.getNumeroCartao());
        cartao.setNomeImpresso(request.getNomeImpresso());
        cartao.setCodigoSeguranca(request.getCodigoSeguranca());
        cartao.setBandeira(bandeira);

        // Se for preferencial, remover dos outros
        if (request.isPreferencial()) {
            removerPreferencialDosOutrosCartoes(clienteId);
            cartao.setPreferencial(true);
        } else {
            cartao.setPreferencial(false);
        }

        cartaoCreditoRepository.save(cartao);

        notificacaoService.criarNotificacao(
            "💳 Cartão Atualizado",
            "Um dos seus cartões foi atualizado.",
            clienteId
        );
    }

    public void atualizarCartaoDoClienteLogado(Integer cartaoId, CartaoUpdateRequest request, HttpSession session) {
        CartaoCredito cartao = cartaoCreditoRepository.findById(cartaoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado"));
    
        Integer clienteId = (Integer) session.getAttribute("clienteId");
    
        if (!cartao.getCliente().getId().equals(clienteId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para atualizar este cartão.");
        }
    
        // Validações
        if (request.getNumeroCartao().length() < 13 || request.getNumeroCartao().length() > 19) {
            throw new IllegalArgumentException("Número do cartão inválido. O número deve ter entre 13 e 19 dígitos.");
        }
    
        if (request.getCodigoSeguranca().length() < 3 || request.getCodigoSeguranca().length() > 4) {
            throw new IllegalArgumentException("Código de segurança inválido.");
        }
    
        Bandeira bandeira = bandeiraService.getBandeiraById(request.getBandeiraId())
            .orElseThrow(() -> new IllegalArgumentException("Bandeira do cartão não encontrada."));
    
        // Atualização
        cartao.setNumeroCartao(request.getNumeroCartao());
        cartao.setNomeImpresso(request.getNomeImpresso());
        cartao.setCodigoSeguranca(request.getCodigoSeguranca());
        cartao.setBandeira(bandeira);
    
        if (request.isPreferencial()) {
            removerPreferencialDosOutrosCartoes(clienteId);
            cartao.setPreferencial(true);
        } else {
            cartao.setPreferencial(false);
        }
    
        cartaoCreditoRepository.save(cartao);
    
        notificacaoService.criarNotificacao(
            "💳 Cartão Atualizado",
            "Seu cartão foi atualizado com sucesso.",
            clienteId
        );
    }
    
    public void deletarCartaoDoClienteLogado(Integer cartaoId, HttpSession session) {
        CartaoCredito cartao = cartaoCreditoRepository.findById(cartaoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado"));
    
        Integer clienteId = (Integer) session.getAttribute("clienteId");
    
        if (!cartao.getCliente().getId().equals(clienteId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para excluir este cartão.");
        }
    
        Cliente cliente = cartao.getCliente();
        if (cliente != null) {
            cliente.getCartoes().remove(cartao);
        }
    
        cartao.setCliente(null);
        cartaoCreditoRepository.delete(cartao);
    
        notificacaoService.criarNotificacao(
            "🗑️ Cartão Removido",
            "Um dos seus cartões foi excluído.",
            clienteId
        );
    }    

}
