package com.fatec.livraria.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fatec.livraria.dto.request.AlterarSenhaRequest;
import com.fatec.livraria.dto.request.AtualizarClienteRequest;
import com.fatec.livraria.dto.request.ClienteRequest;

@Component
public class ClienteValidator {
    private final Validator validator;

    @Autowired
    public ClienteValidator(Validator validator) {
        this.validator = validator;
    }

    public void validarCliente(ClienteRequest clienteRequest) {
        Set<ConstraintViolation<ClienteRequest>> violacoes = validator.validate(clienteRequest);
        if (!violacoes.isEmpty()) {
            StringBuilder mensagemErro = new StringBuilder();
            for (ConstraintViolation<ClienteRequest> violacao : violacoes) {
                mensagemErro.append(violacao.getMessage()).append("\n");
            }
            throw new ConstraintViolationException(mensagemErro.toString(), violacoes);
        }
    }

    public void validarAtualizacaoCliente(AtualizarClienteRequest clienteRequest) {
        Set<ConstraintViolation<AtualizarClienteRequest>> violacoes = validator.validate(clienteRequest);
        if (!violacoes.isEmpty()) {
            StringBuilder mensagemErro = new StringBuilder();
            for (ConstraintViolation<AtualizarClienteRequest> violacao : violacoes) {
                mensagemErro.append(violacao.getMessage()).append("\n");
            }
            throw new ConstraintViolationException(mensagemErro.toString(), violacoes);
        }
    }   

    public void validarAlteracaoSenha(AlterarSenhaRequest alterarSenhaRequest) {
        Set<ConstraintViolation<AlterarSenhaRequest>> violacoes = validator.validate(alterarSenhaRequest);
        if (!violacoes.isEmpty()) {
            StringBuilder mensagemErro = new StringBuilder();
            for (ConstraintViolation<AlterarSenhaRequest> violacao : violacoes) {
                mensagemErro.append(violacao.getMessage()).append("\n");
            }
            throw new ConstraintViolationException(mensagemErro.toString(), violacoes);
        }
    }

}
