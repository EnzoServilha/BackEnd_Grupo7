package sptech.school.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.cliente.ClienteRequestDto;
import sptech.school.dto.cliente.ClienteResponseDto;
import sptech.school.entity.*;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.mapper.ClienteMapper;
import sptech.school.service.ClienteService;

import java.util.List;
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // ----------------------------------------------------------------------------------------------------
    // Listar todos os clientes
    // ----------------------------------------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> listarTodos() {
        List<Cliente> clientes = clienteService.listarTodos();
        if (clientes.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(ClienteMapper.toResponseDtoList(clientes));
    }

    // ----------------------------------------------------------------------------------------------------
    // Buscar endereço por ID
    // ----------------------------------------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> buscarPorId(@PathVariable Integer id) {
        Cliente clienteEncontrado = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ClienteMapper.toResponseDto(clienteEncontrado));
    }

    // ----------------------------------------------------------------------------------------------------
    // Cadastrar cliente
    // ----------------------------------------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<ClienteResponseDto> cadastrar(@RequestBody @Valid ClienteRequestDto request) {

        Cliente cliente = ClienteMapper.toEntity(request);

        Cliente salvo = clienteService.cadastrar(cliente);

        return ResponseEntity.status(201).body(ClienteMapper.toResponseDto(salvo));
    }

    // ----------------------------------------------------------------------------------------------------
    // Atualizar cliente por ID
    // ----------------------------------------------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid ClienteRequestDto request
    ) {
        Cliente cliente = ClienteMapper.toEntity(request);

        Cliente atualizado = clienteService.atualizar(id, cliente);

        return ResponseEntity.ok(ClienteMapper.toResponseDto(atualizado));
    }

    // ----------------------------------------------------------------------------------------------------
    // Deletar cliente por ID
    // ----------------------------------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // métodos mais especificados:

    // ----------------------------------------------------------------------------------------------------
    // Contar clientes
    // ----------------------------------------------------------------------------------------------------
    @GetMapping("/qtd")
    public ResponseEntity<Long> contarClientes() { return ResponseEntity.ok(clienteService.contarClientes()); }


    // ----------------------------------------------------------------------------------------------------
    // Listar movimentacões no estoque por cliente
    // ----------------------------------------------------------------------------------------------------
    @GetMapping("/{id}/movimentacoes")
    public ResponseEntity<List<MovimentacaoEstoque>> listarMovimentacoes(@PathVariable Integer id) {

        List<MovimentacaoEstoque> lista = clienteService.listarMovimentacoesPorCliente(id);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    // ----------------------------------------------------------------------------------------------------
    // Buscar cliente por nome da empresa OU contato
    // ----------------------------------------------------------------------------------------------------
    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteResponseDto>> buscar(@RequestParam String termo) {

        List<Cliente> clientes = clienteService.buscarPorNomeOuContato(termo);

        if (clientes.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(ClienteMapper.toResponseDtoList(clientes));
    }

    // ----------------------------------------------------------------------------------------------------
    // Buscar cliente por CPF/CNPJ
    // ----------------------------------------------------------------------------------------------------
    @GetMapping("/cpf-cnpj/{cpfCnpj}")
    public ResponseEntity<ClienteResponseDto> buscarPorCpfCnpj(@PathVariable String cpfCnpj) {

        Cliente cliente = clienteService.buscarPorCpfCnpj(cpfCnpj);

        return ResponseEntity.ok(ClienteMapper.toResponseDto(cliente));
    }
    // ----------------------------------------------------------------------------------------------------
    // Buscar cliente por cidade
    // ----------------------------------------------------------------------------------------------------
    @GetMapping("/cidade")
    public ResponseEntity<List<ClienteResponseDto>> buscarPorCidade(@RequestParam String cidade) {

        List<Cliente> clientes = clienteService.buscarPorCidade(cidade);

        if (clientes.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(ClienteMapper.toResponseDtoList(clientes));
    }

    // ----------------------------------------------------------------------------------------------------
    // Buscar cliente por estado
    // ----------------------------------------------------------------------------------------------------
    @GetMapping("/estado")
    public ResponseEntity<List<ClienteResponseDto>> buscarPorUf(@RequestParam String uf) {

        List<Cliente> clientes = clienteService.buscarPorUf(uf);

        if (clientes.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(ClienteMapper.toResponseDtoList(clientes));
    }


    // mais métodos aqui se necessário aqui
}
