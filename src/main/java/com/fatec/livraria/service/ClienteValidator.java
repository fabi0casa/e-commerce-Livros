package com.fatec.livraria.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fatec.livraria.dto.AlterarSenhaDTO;
import com.fatec.livraria.dto.AtualizarClienteDTO;
import com.fatec.livraria.dto.ClienteDTO;

@Component
public class ClienteValidator {
    private final Validator validator;

    @Autowired
    public ClienteValidator(Validator validator) {
        this.validator = validator;
    }

    public void validarCliente(ClienteDTO clienteDTO) {
        Set<ConstraintViolation<ClienteDTO>> violacoes = validator.validate(clienteDTO);
        if (!violacoes.isEmpty()) {
            StringBuilder mensagemErro = new StringBuilder();
            for (ConstraintViolation<ClienteDTO> violacao : violacoes) {
                mensagemErro.append(violacao.getMessage()).append("\n");
            }
            throw new ConstraintViolationException(mensagemErro.toString(), violacoes);
        }
    }

    public void validarAtualizacaoCliente(AtualizarClienteDTO clienteDTO) {
        Set<ConstraintViolation<AtualizarClienteDTO>> violacoes = validator.validate(clienteDTO);
        if (!violacoes.isEmpty()) {
            StringBuilder mensagemErro = new StringBuilder();
            for (ConstraintViolation<AtualizarClienteDTO> violacao : violacoes) {
                mensagemErro.append(violacao.getMessage()).append("\n");
            }
            throw new ConstraintViolationException(mensagemErro.toString(), violacoes);
        }
    }   

    public void validarAlteracaoSenha(AlterarSenhaDTO alterarSenhaDTO) {
        Set<ConstraintViolation<AlterarSenhaDTO>> violacoes = validator.validate(alterarSenhaDTO);
        if (!violacoes.isEmpty()) {
            StringBuilder mensagemErro = new StringBuilder();
            for (ConstraintViolation<AlterarSenhaDTO> violacao : violacoes) {
                mensagemErro.append(violacao.getMessage()).append("\n");
            }
            throw new ConstraintViolationException(mensagemErro.toString(), violacoes);
        }
    }

}
