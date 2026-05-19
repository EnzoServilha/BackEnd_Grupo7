package sptech.school.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.permissao.PermissaoRequestDto;
import sptech.school.dto.permissao.PermissaoResponseDto;
import sptech.school.service.PermissaoService;

import java.util.List;

@RestController
@RequestMapping("/permissoes")
public class PermissaoController {
    private final PermissaoService permissaoService;

    public PermissaoController(PermissaoService permissaoService) {
        this.permissaoService = permissaoService;
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PermissaoResponseDto> criar(@RequestBody @Valid PermissaoRequestDto dto) {
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
        return ResponseEntity.ok(permissaoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        permissaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
