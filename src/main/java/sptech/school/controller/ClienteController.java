package sptech.school.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.cliente.ClienteRequestDto;
import sptech.school.dto.cliente.ClienteResponseDto;
import sptech.school.entity.*;
import sptech.school.mapper.ClienteMapper;
import sptech.school.service.ClienteService;
import sptech.school.service.UsuarioService;

import java.util.List;
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private ClienteService clienteService;
    private final UsuarioService usuarioService;

    public ClienteController(ClienteService clienteService, UsuarioService usuarioService) {
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
    }


    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> listarTodos() {
        List<Cliente> clientes = clienteService.listarTodos();
        if (clientes.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(ClienteMapper.toResponseDtoList(clientes));
    }

    @GetMapping("/administracao")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClienteResponseDto>> listarAdministrativo(
            @RequestParam(defaultValue = "todos") String ativo) {
        return ResponseEntity.ok(ClienteMapper.toResponseDtoList(clienteService.listarAdministrativo(ativo)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> buscarPorId(@PathVariable Integer id) {
        Cliente clienteEncontrado = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ClienteMapper.toResponseDto(clienteEncontrado));
    }


    @PostMapping
    public ResponseEntity<ClienteResponseDto> cadastrar(@RequestBody @Valid ClienteRequestDto request) {

        ClienteResponseDto salvo = clienteService.cadastrar(request);

        return ResponseEntity.status(201).body(salvo);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid ClienteRequestDto request
    ) {

        ClienteResponseDto atualizado = clienteService.atualizar( request, id);

        return ResponseEntity.ok(atualizado);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id, Authentication authentication) {
        clienteService.desativar(id, usuarioService.buscarAtivoPorEmail(authentication.getName()));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desativacao")
    public ResponseEntity<Void> desativar(@PathVariable Integer id, Authentication authentication) {
        clienteService.desativar(id, usuarioService.buscarAtivoPorEmail(authentication.getName()));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativacao")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> reativar(@PathVariable Integer id) {
        clienteService.reativar(id);
        return ResponseEntity.noContent().build();
    }

}
