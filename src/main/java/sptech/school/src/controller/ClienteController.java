package sptech.school.src.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.src.dto.cliente.ClienteRequestDto;
import sptech.school.src.dto.cliente.ClienteResponseDto;
import sptech.school.src.entity.*;
import sptech.school.src.mapper.ClienteMapper;
import sptech.school.src.service.ClienteService;

import java.util.List;
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }


    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> listarTodos() {
        List<Cliente> clientes = clienteService.listarTodos();
        if (clientes.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(ClienteMapper.toResponseDtoList(clientes));
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
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
