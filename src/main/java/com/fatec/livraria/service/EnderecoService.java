package com.fatec.livraria.service;

import com.fatec.livraria.entity.Endereco;
import com.fatec.livraria.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {
    @Autowired
    private EnderecoRepository enderecoRepository;

    public List<Endereco> getEnderecoByClienteId(Integer clienteId) {
        return enderecoRepository.findByCliente_Id(clienteId);
    }

    public List<Endereco> listarTodos() {
        return enderecoRepository.findAll();
    }

    public Optional<Endereco> buscarPorId(Integer id) {
        return enderecoRepository.findById(id);
    }

    public Endereco salvar(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }

    public void excluir(Integer id) {
        enderecoRepository.deleteById(id);
    }

    public Endereco atualizarEndereco(Endereco endereco) throws Exception {
    if (enderecoRepository.existsById(endereco.getId())) {
        return enderecoRepository.save(endereco);
    }
    throw new Exception("Endereço não encontrado!");
    }
}
