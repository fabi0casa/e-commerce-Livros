package com.fatec.livraria.service;

import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.repository.ClienteRepository;
import com.fatec.livraria.specification.ClienteSpecification;

import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fatec.livraria.dto.AlterarSenhaDTO;
import com.fatec.livraria.dto.AtualizarClienteDTO;
import com.fatec.livraria.dto.ClienteDTO;
import com.fatec.livraria.dto.EnderecoDTO;
import com.fatec.livraria.entity.Endereco;
import com.fatec.livraria.entity.Pedido;
import com.fatec.livraria.repository.PedidoRepository;
import com.fatec.livraria.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired private ClienteRepository clienteRepository;
    @Autowired private EnderecoService enderecoService;
    @Autowired private ClienteValidator clienteValidator;
    @Autowired private EnderecoValidator enderecoValidator;
    @Autowired private PedidoRepository pedidoRepository;

    @PostConstruct
    public void criarUsuarioRootSeNaoExistir() {
        if (clienteRepository.count() == 0) {
            Cliente root = new Cliente();
            root.setNome("Root");
            root.setCpf("000.000.000-00"); // CPF fictício
            root.setTelefone("(00) 00000-0000");
            root.setEmail("root@admin.com");
            root.setSenha("root");
            root.setRanking(0);
            root.setGenero("outro");
            root.setAdmin(true);
            
            try {
                Date data = new SimpleDateFormat("yyyy-MM-dd").parse("1970-01-01");
                root.setDataNascimento(data);
            } catch (Exception e) {
                root.setDataNascimento(new Date());
            }

            clienteRepository.save(root);
            System.out.println("[INFO] Usuário root criado automaticamente.");
        }
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Integer id) {
        return clienteRepository.findById(id);
    }

    public void cadastrarNovoCliente(ClienteDTO clienteDTO) throws Exception {
        clienteValidator.validarCliente(clienteDTO);

        for (EnderecoDTO enderecoDTO : clienteDTO.getEnderecos()) {
            if (enderecoDTO.getResidencial() == null) {
                enderecoDTO.setResidencial(true);
            }
            enderecoValidator.validarEndereco(enderecoDTO);
        }

        if (clienteRepository.existsByCpf(clienteDTO.getCpf())) {
            throw new ConstraintViolationException("CPF já cadastrado!", null);
        }

        if (clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new ConstraintViolationException("E-mail já cadastrado!", null);
        }

        Cliente cliente = new Cliente();
        cliente.setNome(clienteDTO.getNome());
        cliente.setDataNascimento(clienteDTO.getDataNascimento());
        cliente.setCpf(clienteDTO.getCpf());
        cliente.setGenero(clienteDTO.getGenero());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setSenha(clienteDTO.getSenha());
        cliente.setTelefone(clienteDTO.getTelefone());
        cliente.setRanking(0);

        List<Endereco> enderecos = new ArrayList<>();
        for (EnderecoDTO enderecoDTO : clienteDTO.getEnderecos()) {
            Endereco endereco = enderecoService.converterDTOParaEndereco(enderecoDTO);
            endereco.setCliente(cliente);
            enderecos.add(endereco);
        }

        cliente.setEnderecos(enderecos);

        clienteRepository.save(cliente);
    }

    public void atualizarDadosCliente(AtualizarClienteDTO clienteDTO) throws Exception {
        Cliente cliente = clienteRepository.findById(clienteDTO.getId())
                .orElseThrow(() -> new Exception("Cliente não encontrado!"));

        clienteValidator.validarAtualizacaoCliente(clienteDTO);

        cliente.setNome(clienteDTO.getNome());
        cliente.setDataNascimento(clienteDTO.getDataNascimento());
        cliente.setCpf(clienteDTO.getCpf());
        cliente.setGenero(clienteDTO.getGenero());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setTelefone(clienteDTO.getTelefone());

        clienteRepository.save(cliente);
    }

    public void alterarSenha(AlterarSenhaDTO dto) throws Exception {
        Cliente cliente = clienteRepository.findById(dto.getId())
                .orElseThrow(() -> new Exception("Cliente não encontrado!"));

        clienteValidator.validarAlteracaoSenha(dto);

        if (!cliente.getSenha().equals(dto.getSenhaAtual())) {
            throw new ConstraintViolationException("Senha atual incorreta", null);
        }

        cliente.setSenha(dto.getNovaSenha());
        clienteRepository.save(cliente);
    }

    public void adicionarEnderecoAoCliente(int clienteId, EnderecoDTO enderecoDTO) throws Exception {
        enderecoValidator.validarEndereco(enderecoDTO);

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        Endereco endereco = enderecoService.converterDTOParaEndereco(enderecoDTO);
        endereco.setCliente(cliente);
        cliente.getEnderecos().add(endereco);

        enderecoService.salvar(endereco);
    }

    public void excluirCliente(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    
        for (Pedido pedido : cliente.getPedidos()) {
            // força o carregamento e a exclusão em cascata das vendas
            pedido.getVendas().clear();
        }
    
        clienteRepository.delete(cliente);
    }
    

    public List<Cliente> buscarClientesComFiltro(String nome, String cpf, String telefone, String email, LocalDate dataNascimento, String genero) {
        return clienteRepository.findAll(ClienteSpecification.filtrarClientes(nome, cpf, telefone, email, dataNascimento, genero));
    }
}

