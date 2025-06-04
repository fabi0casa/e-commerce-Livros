package com.fatec.livraria.service;

import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.repository.ClienteRepository;
import com.fatec.livraria.specification.ClienteSpecification;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fatec.livraria.dto.request.AlterarSenhaRequest;
import com.fatec.livraria.dto.request.AtualizarClienteRequest;
import com.fatec.livraria.dto.request.ClienteRequest;
import com.fatec.livraria.dto.request.EnderecoRequest;
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
        return clienteRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Optional<Cliente> buscarPorId(Integer id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> buscarClienteLogado(HttpSession session) {
        Integer clienteId = (Integer) session.getAttribute("clienteId");
        
        if (clienteId == null) {
            return Optional.empty();
        }

        return clienteRepository.findById(clienteId);
    }

    public void cadastrarNovoCliente(ClienteRequest clienteRequest) throws Exception {
        clienteValidator.validarCliente(clienteRequest);

        for (EnderecoRequest enderecoRequest : clienteRequest.getEnderecos()) {
            if (enderecoRequest.getResidencial() == null) {
                enderecoRequest.setResidencial(true);
            }
            enderecoValidator.validarEndereco(enderecoRequest);
        }

        if (clienteRepository.existsByCpf(clienteRequest.getCpf())) {
            throw new ConstraintViolationException("CPF já cadastrado!", null);
        }

        if (clienteRepository.existsByEmail(clienteRequest.getEmail())) {
            throw new ConstraintViolationException("E-mail já cadastrado!", null);
        }

        Cliente cliente = new Cliente();
        cliente.setNome(clienteRequest.getNome());
        cliente.setDataNascimento(clienteRequest.getDataNascimento());
        cliente.setCpf(clienteRequest.getCpf());
        cliente.setGenero(clienteRequest.getGenero());
        cliente.setEmail(clienteRequest.getEmail());
        cliente.setSenha(clienteRequest.getSenha());
        cliente.setTelefone(clienteRequest.getTelefone());
        cliente.setRanking(0);

        List<Endereco> enderecos = new ArrayList<>();
        for (EnderecoRequest enderecoRequest : clienteRequest.getEnderecos()) {
            Endereco endereco = enderecoService.converterRequestParaEndereco(enderecoRequest);
            endereco.setCliente(cliente);
            enderecos.add(endereco);
        }

        cliente.setEnderecos(enderecos);

        clienteRepository.save(cliente);
    }

    public void atualizarDadosCliente(AtualizarClienteRequest clienteRequest) throws Exception {
        Cliente cliente = clienteRepository.findById(clienteRequest.getId())
                .orElseThrow(() -> new Exception("Cliente não encontrado!"));

        clienteValidator.validarAtualizacaoCliente(clienteRequest);

        cliente.setNome(clienteRequest.getNome());
        cliente.setDataNascimento(clienteRequest.getDataNascimento());
        cliente.setCpf(clienteRequest.getCpf());
        cliente.setGenero(clienteRequest.getGenero());
        cliente.setEmail(clienteRequest.getEmail());
        cliente.setTelefone(clienteRequest.getTelefone());

        clienteRepository.save(cliente);
    }

    public void alterarSenha(AlterarSenhaRequest dto) throws Exception {
        Cliente cliente = clienteRepository.findById(dto.getId())
                .orElseThrow(() -> new Exception("Cliente não encontrado!"));

        clienteValidator.validarAlteracaoSenha(dto);

        if (!cliente.getSenha().equals(dto.getSenhaAtual())) {
            throw new ConstraintViolationException("Senha atual incorreta", null);
        }

        cliente.setSenha(dto.getNovaSenha());
        clienteRepository.save(cliente);
    }

    public void adicionarEnderecoAoCliente(int clienteId, EnderecoRequest enderecoRequest) throws Exception {
        enderecoValidator.validarEndereco(enderecoRequest);

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        Endereco endereco = enderecoService.converterRequestParaEndereco(enderecoRequest);
        endereco.setCliente(cliente);
        cliente.getEnderecos().add(endereco);

        enderecoService.salvar(endereco);
    }

    public void adicionarEnderecoAoClienteLogado(EnderecoRequest enderecoRequest, HttpSession session) throws Exception {
        enderecoValidator.validarEndereco(enderecoRequest);
    
        Cliente cliente = buscarPorId((Integer) session.getAttribute("clienteId"))
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (cliente == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cliente não está logado");
    
        // Busca atualizada do cliente no banco (opcional se necessário)
        Cliente clienteCompleto = clienteRepository.findById(cliente.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    
        Endereco endereco = enderecoService.converterRequestParaEndereco(enderecoRequest);
        endereco.setCliente(clienteCompleto);
        clienteCompleto.getEnderecos().add(endereco);
    
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
        return clienteRepository.findAll(ClienteSpecification.filtrarClientes(nome, cpf, telefone, email, dataNascimento, genero), Sort.by(Sort.Direction.DESC, "id"));
    }

    public String gerarContextoCliente(Integer clienteId) {
        Optional<Cliente> clienteOpt = buscarPorId(clienteId);
        if (clienteOpt.isEmpty()) {
            return "Usuário não encontrado.";
        }
        Cliente cliente = clienteOpt.get();
        String dataNascimento = new SimpleDateFormat("yyyy-MM-dd").format(cliente.getDataNascimento());
    
        return "Nome: " + cliente.getNome() + "\n" +
               "Data de Nascimento: " + dataNascimento + "\n" +
               "Gênero: " + cliente.getGenero();
    }
    
}

