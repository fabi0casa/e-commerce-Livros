package com.fatec.livraria.service;

import com.fatec.livraria.dto.EnderecoDTO;
import com.fatec.livraria.entity.Endereco;
import com.fatec.livraria.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {
    @Autowired
    private EnderecoRepository enderecoRepository;

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

    public Endereco salvar(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }

    public void excluir(Integer id) {
        enderecoRepository.deleteById(id);
    }

    public void atualizarEndereco(int enderecoId, EnderecoDTO enderecoDTO) {
        Endereco enderecoExistente = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado"));

        // Validação dos dados com o validador customizado
        enderecoValidator.validarEndereco(enderecoDTO);

        // Atualização dos campos
        enderecoExistente.setTipo(enderecoDTO.getTipo());
        enderecoExistente.setLogradouro(enderecoDTO.getLogradouro());
        enderecoExistente.setNumero(enderecoDTO.getNumero());
        enderecoExistente.setBairro(enderecoDTO.getBairro());
        enderecoExistente.setCep(enderecoDTO.getCep());
        enderecoExistente.setCidade(enderecoDTO.getCidade());
        enderecoExistente.setEstado(enderecoDTO.getEstado());
        enderecoExistente.setPais(enderecoDTO.getPais());
        enderecoExistente.setObservacoes(enderecoDTO.getObservacoes());
        enderecoExistente.setResidencial(enderecoDTO.getResidencial());
        enderecoExistente.setEntrega(enderecoDTO.getEntrega());
        enderecoExistente.setCobranca(enderecoDTO.getCobranca());

        enderecoExistente.gerarFraseIdentificadora();

        enderecoRepository.save(enderecoExistente);
    }
}
