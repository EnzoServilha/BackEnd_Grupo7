package sptech.school.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.categoria.CategoriaRequestDto;
import sptech.school.dto.categoria.CategoriaResponseDto;
import sptech.school.service.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping("/{nome}")
    public ResponseEntity<CategoriaResponseDto> buscarPorNome(@PathVariable String nome){
        return ResponseEntity.status(200).body(service.buscarPorNome(nome));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDto> criar(@RequestBody @Valid CategoriaRequestDto requestDto){
        return ResponseEntity.status(200).body(service.criar(requestDto));
    }

    @PutMapping("/associar/{fornecedorId}/{categoriaId}")
    public ResponseEntity<CategoriaResponseDto> associarCategoriaaFornecedor(@PathVariable Integer fornecedorId, @PathVariable Integer categoriaId){
        return ResponseEntity.status(200).body(service.associarCategoriaaFornecedor(categoriaId,fornecedorId));
    }

    @PutMapping("/desassociar/{fornecedorId}/{categoriaId}")
    public ResponseEntity<CategoriaResponseDto> desassociarCategoriaaFornecedor(@PathVariable Integer fornecedorId, @PathVariable Integer categoriaId){
        return ResponseEntity.status(200).body(service.desassociarCategoriaaFornecedor(categoriaId,fornecedorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Null> deletar(@PathVariable Integer id){

        service.deletar(id);

        return ResponseEntity.status(204).build();
    }
}
