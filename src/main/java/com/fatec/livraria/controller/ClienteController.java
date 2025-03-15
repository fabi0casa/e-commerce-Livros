package com.fatec.livraria.controller;

import com.fatec.livraria.dto.AtualizarClienteDTO;
import com.fatec.livraria.dto.ClienteDTO;
import com.fatec.livraria.dto.EnderecoDTO;
import com.fatec.livraria.entity.Cliente;
import com.fatec.livraria.entity.Endereco;
import com.fatec.livraria.service.ClienteService;
import com.fatec.livraria.service.ClienteValidator;
import com.fatec.livraria.service.EnderecoValidator;

import jakarta.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteValidator clienteValidator;

    @Autowired
    private EnderecoValidator enderecoValidator;

    // Listar todos os clientes
    @GetMapping("/all")
    public ResponseEntity<List<Cliente>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    // Buscar cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Integer id) {
        return clienteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Cadastrar um novo cliente 
    @PostMapping("/add")
    public ResponseEntity<?> adicionarCliente(@ModelAttribute ClienteDTO clienteDTO, Model model, RedirectAttributes redirectAttributes) {
        try {
            // ✅ Validando o Cliente
            clienteValidator.validarCliente(clienteDTO);

            // ✅ Validando os Endereços
            for (EnderecoDTO enderecoDTO : clienteDTO.getEnderecos()) {
                if (enderecoDTO.getResidencial() == null) {
                    enderecoDTO.setResidencial(true); // Define como residencial por padrão
                }
                enderecoValidator.validarEndereco(enderecoDTO);
            }

            // Criando o cliente
            Cliente cliente = new Cliente();
            cliente.setNome(clienteDTO.getNome());
            cliente.setDataNascimento(clienteDTO.getDataNascimento());
            cliente.setCpf(clienteDTO.getCpf());
            cliente.setGenero(clienteDTO.getGenero());
            cliente.setEmail(clienteDTO.getEmail());
            cliente.setSenha(clienteDTO.getSenha());
            cliente.setTelefone(clienteDTO.getTelefone());
            cliente.setRanking(0);

            // Criando e associando os endereços
            List<Endereco> enderecos = new ArrayList<>();
            for (EnderecoDTO enderecoDTO : clienteDTO.getEnderecos()) {
                Endereco endereco = new Endereco();
                endereco.setLogradouro(enderecoDTO.getLogradouro());
                endereco.setNumero(enderecoDTO.getNumero());
                endereco.setBairro(enderecoDTO.getBairro());
                endereco.setCep(enderecoDTO.getCep());
                endereco.setCidade(enderecoDTO.getCidade());
                endereco.setEstado(enderecoDTO.getEstado());
                endereco.setPais(enderecoDTO.getPais());
                endereco.setEntrega(enderecoDTO.getEntrega());
                endereco.setCobranca(enderecoDTO.getCobranca());
                endereco.setResidencial(true);
                endereco.setTipo(enderecoDTO.getTipo());
                endereco.setObservacoes(enderecoDTO.getObservacoes());
                endereco.gerarFraseIdentificadora();

                endereco.setCliente(cliente); // Vincula ao cliente
                enderecos.add(endereco);
            }

            cliente.setEnderecos(enderecos); // Associa a lista de endereços ao cliente

            // Salvando o cliente (JPA salva os endereços automaticamente devido ao CascadeType.ALL)
            clienteService.salvarCliente(cliente);
            
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"mensagem\": \"Cliente cadastrado com sucesso!\"}");
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"erro\": \"Erro inesperado ao cadastrar cliente.\"}");
        }
    }
    
    // Atualizar cliente
    @PutMapping("/update")
    public ResponseEntity<?> atualizarCliente(@RequestBody AtualizarClienteDTO clienteDTO) {
        try {
            // Buscar o cliente pelo ID
            Optional<Cliente> optionalCliente = clienteService.buscarPorId(clienteDTO.getId());
            if (optionalCliente.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"erro\": \"Cliente não encontrado.\"}");
            }

            Cliente cliente = optionalCliente.get();

            // ✅ Validar os novos dados do cliente
            clienteValidator.validarAtualizacaoCliente(clienteDTO);

            // ✅ Atualizar apenas os campos permitidos
            cliente.setNome(clienteDTO.getNome());
            cliente.setDataNascimento(clienteDTO.getDataNascimento());
            cliente.setCpf(clienteDTO.getCpf());
            cliente.setGenero(clienteDTO.getGenero());
            cliente.setEmail(clienteDTO.getEmail());
            cliente.setTelefone(clienteDTO.getTelefone());

            // ✅ Salvar cliente atualizado
            clienteService.atualizarCliente(cliente);

            return ResponseEntity.ok("{\"mensagem\": \"Cliente atualizado com sucesso!\"}");
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"erro\": \"Erro inesperado ao atualizar cliente.\"}");
        }
    }
 

    // Deletar cliente por ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Integer id) {
        clienteService.excluirCliente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtro")
    public ResponseEntity<List<Cliente>> filtrarClientes(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String telefone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataNascimento,
            @RequestParam(required = false) String genero) {

        List<Cliente> clientes = clienteService.buscarClientesComFiltro(nome, cpf, telefone, email, dataNascimento, genero);
        return ResponseEntity.ok(clientes);
    }

}
