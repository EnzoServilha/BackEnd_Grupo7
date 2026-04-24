package sptech.school.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.categoria.CategoriaRequestDto;
import sptech.school.dto.fornecedor.FornecedorRequestDto;
import sptech.school.dto.fornecedor.FornecedorResponseDto;
import sptech.school.dto.marca.MarcaResponseDto;
import sptech.school.service.FornecedorService;

import java.util.List;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

    private final FornecedorService service;

    public FornecedorController(FornecedorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<FornecedorResponseDto>> listar(){
        return ResponseEntity.status(200).body(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponseDto> buscarPorId(@PathVariable Integer id){
        return ResponseEntity.status(200).body(service.buscarPorId(id));
    }

    @GetMapping("/buscarPorNome/{nome}")
    public  ResponseEntity<List<FornecedorResponseDto>> buscarPorNomeContato(@PathVariable String nome){
        return ResponseEntity.status(200).body(service.buscarPorNomeContato(nome));
    }

    @GetMapping("/buscarPorEmpresa/{empresa}")
    public ResponseEntity<List<FornecedorResponseDto>> buscarPorNomeEmpresa(@PathVariable String empresa){
        return ResponseEntity.status(200).body(service.buscarPorNomeEmpresa(empresa));
    }

    @GetMapping("/buscarPorCategoria")
    public  ResponseEntity<List<FornecedorResponseDto>> listarPorCategoria(@RequestBody CategoriaRequestDto categoria){
        return ResponseEntity.status(200).body(service.listarPorCategoria(categoria));
    }

    @GetMapping("/buscarPorFabricante/{idFornecedor}")
    public  ResponseEntity<List<MarcaResponseDto>> listarFabricantesPorFornecedorId(@PathVariable Integer idFornecedor){
        return ResponseEntity.status(200).body(service.listarFabricantesPorFornecedorId(idFornecedor));
    }

    @PostMapping
    public ResponseEntity<FornecedorResponseDto> criar(@RequestBody @Valid FornecedorRequestDto requestDto){
        return ResponseEntity.status(201).body(service.criar(requestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FornecedorResponseDto> atualizar(@RequestBody @Valid FornecedorRequestDto requestDto, @PathVariable Integer id){
        return ResponseEntity.status(200).body(service.atualizar(requestDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Null> deletar(@PathVariable Integer id){

        service.deletar(id);

        return ResponseEntity.status(204).build();
    }
}
