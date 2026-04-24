package sptech.school.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.categoria.CategoriaRequestDto;
import sptech.school.dto.categoria.CategoriaResponseDto;
import sptech.school.dto.marca.MarcaRequestDto;
import sptech.school.dto.marca.MarcaResponseDto;
import sptech.school.service.CategoriaService;
import sptech.school.service.MarcaService;

@RestController
@RequestMapping("/marcas")
public class MarcaController {

    private MarcaService service;

    public MarcaController(MarcaService service) {
        this.service = service;
    }

    @GetMapping("/{nome}")
    public ResponseEntity<MarcaResponseDto> buscarPorNome(@PathVariable String nome){
        return ResponseEntity.status(200).body(service.buscarPorNome(nome));
    }

    @PostMapping
    public ResponseEntity<MarcaResponseDto> criar(@RequestBody @Valid MarcaRequestDto requestDto){
        return ResponseEntity.status(200).body(service.criar(requestDto));
    }

    @PutMapping("/associar/{fornecedorId}/{categoriaId}")
    public ResponseEntity<MarcaResponseDto> associarCategoriaaFornecedor(@PathVariable Integer fornecedorId, @PathVariable Integer categoriaId){
        return ResponseEntity.status(200).body(service.associarMarcaaFornecedor(categoriaId,fornecedorId));
    }

    @PutMapping("/desassociar/{fornecedorId}/{categoriaId}")
    public ResponseEntity<MarcaResponseDto> desassociarCategoriaaFornecedor(@PathVariable Integer fornecedorId, @PathVariable Integer categoriaId){
        return ResponseEntity.status(200).body(service.desassociarMarcaaFornecedor(categoriaId,fornecedorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Null> deletar(@PathVariable Integer id){

        service.deletar(id);

        return ResponseEntity.status(204).build();
    }
}
