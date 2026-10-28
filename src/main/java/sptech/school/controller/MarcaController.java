package sptech.school.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.marca.MarcaRequestDto;
import sptech.school.dto.marca.MarcaResponseDto;
import sptech.school.dto.usuario.UsuarioResponseDto;
import sptech.school.service.MarcaService;
import sptech.school.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/marcas")
@Validated
public class MarcaController {

    private final MarcaService service;
    private final UsuarioService usuarioService;

    public MarcaController(MarcaService service, UsuarioService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/administracao")
    public ResponseEntity<List<MarcaResponseDto>> listarAdministrativo(
            @RequestParam(defaultValue = "todos") String ativo) {
        return ResponseEntity.ok(service.listarAdministrativo(ativo));
    }

    // Autocomplete: sempre devolve lista (vazia se nada encontrado), nunca corpo nulo.
    @GetMapping("/pesquisa")
    public ResponseEntity<List<MarcaResponseDto>> pesquisar(
            @RequestParam
            @NotBlank
            @Size(max = 100)
            @Pattern(regexp = "^[\\p{L}\\p{N}\\s._@-]+$")
            String termo) {
        return ResponseEntity.ok(service.pesquisar(termo));
    }

    // Mantido por compatibilidade: agora responde 404 quando não existe (em vez de 200 vazio).
    @GetMapping("/{nome}")
    public ResponseEntity<MarcaResponseDto> buscarPorNome(
            @PathVariable
            @NotBlank
            @Size(max = 100)
            @Pattern(regexp = "^[\\p{L}\\p{N}\\s._@-]+$")
            String nome) {
        MarcaResponseDto dto = service.buscarPorNome(nome);
        return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }

    @GetMapping("/porId/{id}")
    public ResponseEntity<MarcaResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // Idempotente: se a marca já existe (mesmo inativa), reaproveita/reativa; senão cria.
    @PostMapping
    public ResponseEntity<MarcaResponseDto> criar(@RequestBody @Valid MarcaRequestDto requestDto) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        return ResponseEntity.status(201).body(service.criar(requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id, Authentication authentication) {
        return desativar(id, authentication);
    }

    @PatchMapping("/{id}/desativacao")
    public ResponseEntity<Void> desativar(@PathVariable Integer id, Authentication authentication) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        service.desativar(id, usuarioService.buscarAtivoPorEmail(authentication.getName()));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativacao")
    public ResponseEntity<Void> reativar(@PathVariable Integer id) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        service.reativar(id);
        return ResponseEntity.noContent().build();
    }
}