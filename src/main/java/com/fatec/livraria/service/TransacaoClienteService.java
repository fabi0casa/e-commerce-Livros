package com.fatec.livraria.service;

import com.fatec.livraria.entity.TransacaoCliente;
import com.fatec.livraria.repository.TransacaoClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransacaoClienteService {

    @Autowired
    private TransacaoClienteRepository transacaoClienteRepository;

    public List<TransacaoCliente> listarTodas() {
        return transacaoClienteRepository.findAll();
    }

    public Optional<TransacaoCliente> buscarPorId(Integer id) {
        return transacaoClienteRepository.findById(id);
    }

    public List<TransacaoCliente> buscarPorClienteId(Integer clienteId) {
        return transacaoClienteRepository.findByClienteId(clienteId);
    }
}
