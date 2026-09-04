package sptech.school.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.permissao.PermissaoRequestDto;
import sptech.school.dto.permissao.PermissaoResponseDto;
import sptech.school.service.PermissaoService;
import sptech.school.dto.usuario.UsuarioResponseDto;
import sptech.school.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/permissoes")
public class PermissaoController {
    private final PermissaoService permissaoService;
    private final UsuarioService usuarioService;

    public PermissaoController(PermissaoService permissaoService, UsuarioService usuarioService) {
        this.permissaoService = permissaoService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PermissaoResponseDto> criar(@RequestBody @Valid PermissaoRequestDto dto) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        return ResponseEntity.status(201).body(permissaoService.criar(dto));
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PermissaoResponseDto>> listarTodos() {
        List<PermissaoResponseDto> permissoes = permissaoService.listarTodos();
        if (permissoes.isEmpty()) return ResponseEntity.status(204).build();
        return ResponseEntity.ok(permissoes);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PermissaoResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(permissaoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PermissaoResponseDto> atualizar(@PathVariable Integer id, @RequestBody @Valid PermissaoRequestDto dto) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        return ResponseEntity.ok(permissaoService.atualizar(id, dto));
    }
}
