package sptech.school.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.cliente.ClienteRequestDto;
import sptech.school.dto.cliente.ClienteResponseDto;
import sptech.school.dto.usuario.UsuarioResponseDto;
import sptech.school.entity.Cliente;
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
    public ResponseEntity<List<ClienteResponseDto>> listar(
            @RequestParam(defaultValue = "nomeEmpresa") String ordenacao,
            @RequestParam(defaultValue = "ASC") String direcao) {

        Sort.Direction dir = Sort.Direction.fromString(direcao);

        Sort sort = Sort.by(dir, ordenacao).and(Sort.by(Sort.Direction.ASC, "id"));

        return ResponseEntity.ok(clienteService.listarTodos(sort));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteResponseDto>> pesquisarEFiltrar(
            @RequestParam(required = false) String nomeEmpresa,
            @RequestParam(required = false) String nomeContato,
            @RequestParam(required = false) String telefone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String localizacao
    ){
        List<ClienteResponseDto> resultados = clienteService.pesquisar(
                nomeEmpresa, nomeContato, telefone, email, localizacao
        );

        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/administracao")
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
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        ClienteResponseDto salvo = clienteService.cadastrar(request);

        return ResponseEntity.status(201).body(salvo);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid ClienteRequestDto request
    ) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        ClienteResponseDto atualizado = clienteService.atualizar(request, id);

        return ResponseEntity.ok(atualizado);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id, Authentication authentication) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        clienteService.desativar(id, usuarioService.buscarAtivoPorEmail(authentication.getName()));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desativacao")
    public ResponseEntity<Void> desativar(@PathVariable Integer id, Authentication authentication) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        clienteService.desativar(id, usuarioService.buscarAtivoPorEmail(authentication.getName()));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativacao")
    public ResponseEntity<Void> reativar(@PathVariable Integer id) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        clienteService.reativar(id);
        return ResponseEntity.noContent().build();
    }

}
