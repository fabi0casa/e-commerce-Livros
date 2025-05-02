package com.fatec.livraria.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();

        for (FieldError erro : ex.getBindingResult().getFieldErrors()) {
            erros.put(erro.getField(), erro.getDefaultMessage());
        }

        // Envia o primeiro erro encontrado como "message", ou envie a lista completa se quiser
        String primeiraMensagem = erros.values().stream().findFirst().orElse("Erro de validação.");

        Map<String, String> resposta = new HashMap<>();
        resposta.put("erro", primeiraMensagem);

        return new ResponseEntity<>(resposta, HttpStatus.BAD_REQUEST);
    }
}

