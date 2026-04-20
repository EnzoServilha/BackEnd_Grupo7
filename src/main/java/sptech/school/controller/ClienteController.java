package sptech.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.cliente.ClienteResponseDto;
import sptech.school.entity.Cliente;
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
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ClienteMapper.toResponseDto(cliente));
    }

    // ----------------------------------------------------------------------------------------------------
    // TODO: Cadastrar cliente
    // ----------------------------------------------------------------------------------------------------

    // ----------------------------------------------------------------------------------------------------
    // TODO: Atualizar cliente por ID
    // ----------------------------------------------------------------------------------------------------

    // ----------------------------------------------------------------------------------------------------
    // Deletar cliente por ID
    // ----------------------------------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ----------------------------------------------------------------------------------------------------
    // ENDPOINT NOVO
    // --- Cartões da tela de contato de cliente ---
    // Buscar lista de cartoes informações:
    // - Nome da empresa (nome do cliente)
    // - (ignorar porque não tem no banco) Nome do contato
    // - Telefone
    // - E-mail
    // - Estado + "-" + Cidade
    // ----------------------------------------------------------------------------------------------------


    // mais métodos provavelmente vão ser necessários
}
