package com.fatec.livraria.service;

import com.fatec.livraria.entity.Endereco;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EnderecoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Endereco.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Endereco endereco = (Endereco) target;

        if (endereco.getCep() == null || !endereco.getCep().matches("\\d{5}-\\d{3}")) {
            errors.rejectValue("cep", "Formato inválido. Use XXXXX-XXX.");
        }

        if (endereco.getLogradouro() == null || endereco.getLogradouro().trim().isEmpty()) {
            errors.rejectValue("logradouro", "O logradouro não pode estar vazio.");
        }

        if (endereco.getNumero() == null || endereco.getNumero().trim().isEmpty()) {
            errors.rejectValue("numero", "O número não pode estar vazio.");
        }
    }
}
