package sptech.school.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.movimentacaoEstoque.FechamentoCotacaoRequestDto;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueRequestDto;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueResponseDto;
import sptech.school.dto.usuario.UsuarioResponseDto;
import sptech.school.service.MovimentacaoEstoqueService;
import sptech.school.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
@Validated
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService service;
    private final UsuarioService usuarioService;

    public MovimentacaoEstoqueController(MovimentacaoEstoqueService service, UsuarioService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoEstoqueResponseDto> buscarPorId(@PathVariable Integer id){
        return ResponseEntity.status(200).body(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<MovimentacaoEstoqueResponseDto>> listar(){
        return ResponseEntity.status(200).body(service.listar());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<MovimentacaoEstoqueResponseDto>> buscarPorTipo(
            @PathVariable
            @NotBlank
            @Size(max = 45)
            @Pattern(regexp = "^[\\p{L}\\p{N}\\s._@-]+$")
            String tipo){
        return ResponseEntity.status(200).body(service.buscarPorTipo(tipo));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MovimentacaoEstoqueResponseDto>> buscarPorStatus(
            @PathVariable
            @NotBlank
            @Size(max = 45)
            @Pattern(regexp = "^[\\p{L}\\p{N}\\s._@-]+$")
            String status){
        return ResponseEntity.status(200).body(service.buscarPorStatus(status));
    }

    @PostMapping
    public ResponseEntity<MovimentacaoEstoqueResponseDto> criar(@RequestBody @Valid MovimentacaoEstoqueRequestDto requestDto, Authentication authentication){
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        return ResponseEntity.status(201).body(service.criar(requestDto, authentication.getName()));
    }

    @PostMapping("/cotacoes/{id}/fechamento")
    public ResponseEntity<MovimentacaoEstoqueResponseDto> fecharCotacao(
            @PathVariable Integer id,
            @RequestBody @Valid FechamentoCotacaoRequestDto requestDto,
            Authentication authentication) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        return ResponseEntity.status(201).body(service.fecharCotacao(id, requestDto, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimentacaoEstoqueResponseDto> editar(@RequestBody @Valid MovimentacaoEstoqueRequestDto requestDto, @PathVariable Integer id, Authentication authentication){
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        return ResponseEntity.status(200).body(service.editar(requestDto, id, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Null> deletar(@PathVariable Integer id){
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        service.cancelar(id);

        return ResponseEntity.status(204).build();
    }

    @PatchMapping("/{id}/cancelamento")
    public ResponseEntity<Void> cancelar(@PathVariable Integer id) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        service.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}
