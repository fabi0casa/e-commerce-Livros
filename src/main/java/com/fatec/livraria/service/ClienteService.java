package com.fatec.livraria.service;

import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.repository.ClienteRepository;
import com.fatec.livraria.specification.ClienteSpecification;

import jakarta.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public Cliente salvarCliente(Cliente cliente) throws Exception {
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new ConstraintViolationException("CPF já cadastrado!", null);
        }

        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new ConstraintViolationException("E-mail já cadastrado!", null);
        }
    
        try {
            return clienteRepository.save(cliente);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace(); // Exibe o erro real no console
            throw new Exception("Erro ao salvar cliente: " + e.getMessage());
        }
    }

    public Cliente atualizarCliente(Cliente cliente) throws Exception {
        if (clienteRepository.existsById(cliente.getId())) {
            return clienteRepository.save(cliente);
        }
        throw new Exception("Cliente não encontrado!");
    }

    public Cliente atualizarSenha(int clienteId, String novaSenha) throws Exception {
        Optional<Cliente> optionalCliente = clienteRepository.findById(clienteId);
        
        if (optionalCliente.isPresent()) {
            Cliente cliente = optionalCliente.get();
            cliente.setSenha(novaSenha);
            return clienteRepository.save(cliente); // Salva apenas a senha alterada
        }
        
        throw new Exception("Cliente não encontrado!");
    }
    

    public void excluirCliente(Integer id) {
        clienteRepository.deleteById(id);
    }

    public List<Cliente> buscarClientesComFiltro(String nome, String cpf, String telefone, String email, LocalDate dataNascimento, String genero) {
        return clienteRepository.findAll(ClienteSpecification.filtrarClientes(nome, cpf, telefone, email, dataNascimento, genero));
    }
}
