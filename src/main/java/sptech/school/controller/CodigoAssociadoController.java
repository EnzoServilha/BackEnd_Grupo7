package sptech.school.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public ResponseEntity<CodigoAssociadoResponseDto> buscarPorId(@PathVariable Integer id) {
        CodigoAssociado codigo = codigoAssociadoService.buscarPorId(id);
        return ResponseEntity.ok(CodigoAssociadoMapper.toResponseDto(codigo));
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<List<CodigoAssociadoResponseDto>> pesquisarPorCodigo(@RequestParam String codigo) {
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
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        codigoAssociadoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
