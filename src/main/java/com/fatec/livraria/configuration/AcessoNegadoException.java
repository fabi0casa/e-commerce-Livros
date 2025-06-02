package com.fatec.livraria.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AcessoNegadoException extends ResponseStatusException {
    public AcessoNegadoException() {
        super(HttpStatus.FORBIDDEN, "Acesso negado: você não tem permissão para acessar este recurso.");
    }
}
