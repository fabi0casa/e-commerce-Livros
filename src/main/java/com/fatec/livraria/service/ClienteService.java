package com.fatec.livraria.service;

import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Integer id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> buscarPorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    public Cliente salvarCliente(Cliente cliente) throws Exception {
        try {
            return clienteRepository.save(cliente);
        } catch (DataIntegrityViolationException e) {
            throw new Exception("CPF ou E-mail já cadastrado!");
        }
    }

    public Cliente atualizarCliente(Cliente cliente) throws Exception {
        if (clienteRepository.existsById(cliente.getId())) {
            return clienteRepository.save(cliente);
        }
        throw new Exception("Cliente não encontrado!");
    }

    public void excluirCliente(Integer id) {
        clienteRepository.deleteById(id);
    }
}
