package com.fatec.livraria.service;

import com.fatec.livraria.dto.request.EnderecoRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class EnderecoValidator {
    private final Validator validator;

    @Autowired
    public EnderecoValidator(Validator validator) {
        this.validator = validator;
    }

    public void validarEndereco(EnderecoRequest enderecoRequest) {
        Set<ConstraintViolation<EnderecoRequest>> violacoes = validator.validate(enderecoRequest);
        if (!violacoes.isEmpty()) {
            StringBuilder mensagemErro = new StringBuilder();
            for (ConstraintViolation<EnderecoRequest> violacao : violacoes) {
                mensagemErro.append(violacao.getMessage()).append("\n");
            }
            throw new ConstraintViolationException(mensagemErro.toString(), violacoes);
        }
    }
}
