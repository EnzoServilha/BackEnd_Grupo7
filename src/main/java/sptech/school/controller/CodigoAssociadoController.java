package sptech.school.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.codigoAssociado.CodigoAssociadoRequestDto;
import sptech.school.dto.codigoAssociado.CodigoAssociadoResponseDto;
import sptech.school.entity.CodigoAssociado;
import sptech.school.mapper.CodigoAssociadoMapper;
import sptech.school.service.CodigoAssociadoService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/codigos-associados")
public class CodigoAssociadoController {

    private final CodigoAssociadoService codigoAssociadoService;

    public CodigoAssociadoController(CodigoAssociadoService codigoAssociadoService) {
        this.codigoAssociadoService = codigoAssociadoService;
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
        CodigoAssociado entity = CodigoAssociadoMapper.toEntity(request);
        CodigoAssociado salvo = codigoAssociadoService.cadastrar(entity);
        return ResponseEntity.created(URI.create("/codigos-associados/" + salvo.getId()))
                .body(CodigoAssociadoMapper.toResponseDto(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CodigoAssociadoResponseDto> atualizar(@PathVariable Integer id, @RequestBody @Valid CodigoAssociadoRequestDto request) {
        CodigoAssociado entity = CodigoAssociadoMapper.toEntity(request);
        CodigoAssociado atualizado = codigoAssociadoService.atualizar(id, entity);
        return ResponseEntity.ok(CodigoAssociadoMapper.toResponseDto(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        codigoAssociadoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
