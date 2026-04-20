package sptech.school.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.endereco.EnderecoRequestDto;
import sptech.school.dto.endereco.EnderecoResponseDto;
import sptech.school.dto.endereco.EnderecoRequestDto;
import sptech.school.dto.endereco.EnderecoResponseDto;
import sptech.school.entity.CodigoAssociado;
import sptech.school.entity.Endereco;
import sptech.school.entity.Endereco;
import sptech.school.mapper.EnderecoMapper;
import sptech.school.mapper.EnderecoMapper;
import sptech.school.service.ClienteService;
import sptech.school.service.EnderecoService;
import sptech.school.service.FabricanteService;
import sptech.school.service.FornecedorService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    private EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    // ----------------------------------------------------------------------------------------------------
    // Listar todos os endereços
    // ----------------------------------------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<EnderecoResponseDto>> listarTodos() {
        List<Endereco> enderecos = enderecoService.listarTodos();
        if (enderecos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(EnderecoMapper.toResponseDtoList(enderecos));
    }

    // ----------------------------------------------------------------------------------------------------
    // Buscar endereço por ID
    // ----------------------------------------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponseDto> buscarPorId(@PathVariable Integer id) {
        Endereco endereco = enderecoService.buscarPorId(id);
        return ResponseEntity.ok(EnderecoMapper.toResponseDto(endereco));
    }

    // ----------------------------------------------------------------------------------------------------
    // TODO: Cadastrar endereço
    // ----------------------------------------------------------------------------------------------------

    // ----------------------------------------------------------------------------------------------------
    // TODO: Atualizar endereço por ID
    // ----------------------------------------------------------------------------------------------------
//    @PostMapping
//    public ResponseEntity<EnderecoResponseDto> cadastrar(@RequestBody @Valid EnderecoRequestDto request) {
//        Endereco endereco = EnderecoMapper.toEntity(request);
//        Endereco salvo = enderecoService.cadastrar(endereco, request.codigosAssociadosIds(), request.itensSimilaresIds());
//        return ResponseEntity.created(null).body(EnderecoMapper.toResponseDto(salvo));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<EnderecoResponseDto> atualizar(@PathVariable Integer id, @RequestBody @Valid EnderecoRequestDto request) {
//        Endereco endereco = EnderecoMapper.toEntity(request);
//        Endereco atualizado = enderecoService.atualizar(id, endereco);
//        return ResponseEntity.ok(EnderecoMapper.toResponseDto(atualizado));
//    }

    // ----------------------------------------------------------------------------------------------------
    // Deletar endereço por ID
    // ----------------------------------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        enderecoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}