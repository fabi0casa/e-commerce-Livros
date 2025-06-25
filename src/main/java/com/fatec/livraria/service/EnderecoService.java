package com.fatec.livraria.service;

import com.fatec.livraria.dto.request.EnderecoRequest;
import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.entity.Endereco;
import com.fatec.livraria.repository.EnderecoRepository;
import com.fatec.livraria.repository.PedidoRepository;
import com.fatec.livraria.repository.ClienteRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {
    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private EnderecoValidator enderecoValidator;

    public List<Endereco> getEnderecoByClienteId(Integer clienteId) {
        return enderecoRepository.findByCliente_Id(clienteId);
    }

    public List<Endereco> listarTodos() {
        return enderecoRepository.findAll();
    }

    public Optional<Endereco> buscarPorId(Integer id) {
        return enderecoRepository.findById(id);
    }

    public Endereco buscarEnderecoDoClienteLogado(Integer enderecoId, Integer clienteId) {
        Endereco endereco = enderecoRepository.findById(enderecoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado"));
    
        if (!endereco.getCliente().getId().equals(clienteId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem acesso a este endereço");
        }
    
        return endereco;
    }    

    public Endereco salvar(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }

    @Transactional
    public void excluir(Integer id) {
        Endereco endereco = enderecoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado"));
    
        boolean temPedido = pedidoRepository.existsByEndereco(endereco);
    
        Cliente cliente = endereco.getCliente();
        if (cliente != null) {
            if (temPedido) {
                endereco.setCliente(null);
                enderecoRepository.save(endereco);
            } else {
                cliente.getEnderecos().removeIf(e -> e.getId().equals(endereco.getId()));
                clienteRepository.save(cliente);
            }
        }

        if (!temPedido && cliente == null) {
            enderecoRepository.delete(endereco);
        }
    }    
    
    public void atualizarEndereco(int enderecoId, EnderecoRequest enderecoRequest) {
        Endereco enderecoExistente = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado"));

        // Validação dos dados com o validador customizado
        enderecoValidator.validarEndereco(enderecoRequest);

        // Atualização dos campos
        enderecoExistente.setTipo(enderecoRequest.getTipo());
        enderecoExistente.setLogradouro(enderecoRequest.getLogradouro());
        enderecoExistente.setNumero(enderecoRequest.getNumero());
        enderecoExistente.setBairro(enderecoRequest.getBairro());
        enderecoExistente.setCep(enderecoRequest.getCep());
        enderecoExistente.setCidade(enderecoRequest.getCidade());
        enderecoExistente.setEstado(enderecoRequest.getEstado());
        enderecoExistente.setPais(enderecoRequest.getPais());
        enderecoExistente.setObservacoes(enderecoRequest.getObservacoes());
        enderecoExistente.setResidencial(enderecoRequest.getResidencial());
        enderecoExistente.setEntrega(enderecoRequest.getEntrega());
        enderecoExistente.setCobranca(enderecoRequest.getCobranca());

        enderecoExistente.gerarFraseIdentificadora();

        enderecoRepository.save(enderecoExistente);
    }
    
    public Endereco converterRequestParaEndereco(EnderecoRequest dto) {
        Endereco endereco = new Endereco();
        endereco.setTipo(dto.getTipo());
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setNumero(dto.getNumero());
        endereco.setBairro(dto.getBairro());
        endereco.setCep(dto.getCep());
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());
        endereco.setPais(dto.getPais());
        endereco.setObservacoes(dto.getObservacoes());
        endereco.setResidencial(dto.getResidencial());
        endereco.setEntrega(dto.getEntrega());
        endereco.setCobranca(dto.getCobranca());
        endereco.gerarFraseIdentificadora();
        return endereco;
    }

    public void atualizarEnderecoDoCliente(int enderecoId, EnderecoRequest enderecoRequest, int clienteId) {
        Endereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado"));
    
        if (endereco.getCliente() == null || !endereco.getCliente().getId().equals(clienteId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para alterar este endereço.");
        }
    
        enderecoValidator.validarEndereco(enderecoRequest);
    
        endereco.setTipo(enderecoRequest.getTipo());
        endereco.setLogradouro(enderecoRequest.getLogradouro());
        endereco.setNumero(enderecoRequest.getNumero());
        endereco.setBairro(enderecoRequest.getBairro());
        endereco.setCep(enderecoRequest.getCep());
        endereco.setCidade(enderecoRequest.getCidade());
        endereco.setEstado(enderecoRequest.getEstado());
        endereco.setPais(enderecoRequest.getPais());
        endereco.setObservacoes(enderecoRequest.getObservacoes());
        endereco.setResidencial(enderecoRequest.getResidencial());
        endereco.setEntrega(enderecoRequest.getEntrega());
        endereco.setCobranca(enderecoRequest.getCobranca());
    
        endereco.gerarFraseIdentificadora();
    
        enderecoRepository.save(endereco);
    }
    
    @Transactional
    public void excluirEnderecoDoCliente(Integer enderecoId, Integer clienteId) {
        Endereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado"));
    
        if (endereco.getCliente() == null || !endereco.getCliente().getId().equals(clienteId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para excluir este endereço.");
        }
    
        boolean temPedido = pedidoRepository.existsByEndereco(endereco);
    
        Cliente cliente = endereco.getCliente();
    
        if (temPedido) {
            endereco.setCliente(null);
            enderecoRepository.save(endereco);
        } else {
            cliente.getEnderecos().removeIf(e -> e.getId().equals(endereco.getId()));
            clienteRepository.save(cliente);
        }
    
        if (!temPedido && cliente == null) {
            enderecoRepository.delete(endereco);
        }
    }    
    
}
