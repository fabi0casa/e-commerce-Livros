package com.fatec.livraria.service;

import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.entity.Log;
import com.fatec.livraria.repository.ClienteRepository;
import com.fatec.livraria.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public void registrarNoLogs(Integer clienteId, String descricao) {
        String usuario;

        if (clienteId == null) {
            usuario = System.getProperty("user.name"); // usuário do SO
        } else {
            Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);
            if (clienteOpt.isPresent()) {
                Cliente cliente = clienteOpt.get();
                String tipo = cliente.isAdmin() ? "(admin)" : "(cliente)";
                usuario = tipo + " " + cliente.getNome();
            } else {
                usuario = "(desconhecido)";
            }
        }

        Log log = new Log();
        log.setUsuario(usuario);
        log.setDescricao(descricao);
        log.setDataHora(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        logRepository.save(log);
    }

    public List<Log> listarTodos() {
        return logRepository.findAll();
    }
}
