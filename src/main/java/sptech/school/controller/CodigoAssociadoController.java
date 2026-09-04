package sptech.school.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.codigoAssociado.CodigoAssociadoRequestDto;
import sptech.school.dto.codigoAssociado.CodigoAssociadoResponseDto;
import sptech.school.dto.usuario.UsuarioResponseDto;
import sptech.school.entity.CodigoAssociado;
import sptech.school.mapper.CodigoAssociadoMapper;
import sptech.school.service.CodigoAssociadoService;
import sptech.school.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/codigos-associados")
@Validated
public class CodigoAssociadoController {

    private final CodigoAssociadoService codigoAssociadoService;
    private final UsuarioService usuarioService;

    public CodigoAssociadoController(CodigoAssociadoService codigoAssociadoService, UsuarioService usuarioService) {
        this.codigoAssociadoService = codigoAssociadoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<CodigoAssociadoResponseDto>> listarTodos() {
        List<CodigoAssociado> codigos = codigoAssociadoService.listarTodos();
        if (codigos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(CodigoAssociadoMapper.toResponseDtoList(codigos));
    }

    @GetMapping("/administracao")
    public ResponseEntity<List<CodigoAssociadoResponseDto>> listarAdministrativo(
            @RequestParam(defaultValue = "todos") String ativo) {
        return ResponseEntity.ok(CodigoAssociadoMapper.toResponseDtoList(
                codigoAssociadoService.listarAdministrativo(ativo)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CodigoAssociadoResponseDto> buscarPorId(@PathVariable Integer id) {
        CodigoAssociado codigo = codigoAssociadoService.buscarPorId(id);
        return ResponseEntity.ok(CodigoAssociadoMapper.toResponseDto(codigo));
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<List<CodigoAssociadoResponseDto>> pesquisarPorCodigo(
            @RequestParam
            @NotBlank
            @Size(max = 100)
            @Pattern(regexp = "^[\\p{L}\\p{N}\\s._@-]+$")
            String codigo) {
        List<CodigoAssociado> codigos = codigoAssociadoService.pesquisarPorCodigo(codigo);
        if (codigos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(CodigoAssociadoMapper.toResponseDtoList(codigos));
    }

    @PostMapping
    public ResponseEntity<CodigoAssociadoResponseDto> cadastrar(@RequestBody @Valid CodigoAssociadoRequestDto request) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        return ResponseEntity.created(null).body(codigoAssociadoService.cadastrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CodigoAssociadoResponseDto> atualizar(@PathVariable Integer id,
                                                                @RequestBody @Valid CodigoAssociadoRequestDto request) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        return ResponseEntity.ok(codigoAssociadoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id, Authentication authentication) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        codigoAssociadoService.desativar(id, usuarioService.buscarAtivoPorEmail(authentication.getName()));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desativacao")
    public ResponseEntity<Void> desativar(@PathVariable Integer id, Authentication authentication) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        codigoAssociadoService.desativar(id, usuarioService.buscarAtivoPorEmail(authentication.getName()));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativacao")
    public ResponseEntity<Void> reativar(@PathVariable Integer id) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        codigoAssociadoService.reativar(id);
        return ResponseEntity.noContent().build();
    }
}
