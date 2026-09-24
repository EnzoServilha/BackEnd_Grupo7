package sptech.school.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.cliente.ClienteRequestDto;
import sptech.school.dto.cliente.ClienteResponseDtoPaginacao;
import sptech.school.dto.usuario.UsuarioResponseDto;
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
    public ResponseEntity<ClienteResponseDtoPaginacao> listar(
            //Configura o pagebla
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho,
            @RequestParam(defaultValue = "nomeEmpresa") String ordenacao,
            @RequestParam(defaultValue = "ASC") String direcao) {

        // Converte a string "ASC" ou "DESC" em enum
        Sort.Direction dir = Sort.Direction.fromString(direcao);

        // Define ordenação primária pelo campo escolhido + id como critério de desempate
        Sort sort = Sort.by(dir, ordenacao).and(Sort.by(Sort.Direction.ASC, "id"));

        // Cria o objeto de paginação
        Pageable pageable = PageRequest.of(pagina, tamanho, sort);

        // Chama o service que busca no banco e usa o ClienteMapper
        ClienteResponseDtoPaginacao response = clienteService.listarTodos(pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/administracao")
    public ResponseEntity<List<ClienteResponseDtoPaginacao>> listarAdministrativo(
            @RequestParam(defaultValue = "todos") String ativo) {
        return ResponseEntity.ok(ClienteMapper.toResponseDtoList(clienteService.listarAdministrativo(ativo)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDtoPaginacao> buscarPorId(@PathVariable Integer id) {
        Cliente clienteEncontrado = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ClienteMapper.toResponseDto(clienteEncontrado));
    }


    @PostMapping
    public ResponseEntity<ClienteResponseDtoPaginacao> cadastrar(@RequestBody @Valid ClienteRequestDto request) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        ClienteResponseDtoPaginacao salvo = clienteService.cadastrar(request);

        return ResponseEntity.status(201).body(salvo);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDtoPaginacao> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid ClienteRequestDto request
    ) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        ClienteResponseDtoPaginacao atualizado = clienteService.atualizar( request, id);

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
