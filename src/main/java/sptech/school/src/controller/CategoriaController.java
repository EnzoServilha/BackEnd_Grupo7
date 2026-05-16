package sptech.school.src.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.src.dto.categoria.CategoriaRequestDto;
import sptech.school.src.dto.categoria.CategoriaResponseDto;
import sptech.school.src.service.CategoriaService;

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

    @GetMapping("/porId/{id}")
    public ResponseEntity<CategoriaResponseDto> buscarPorId(@PathVariable Integer id){
        return ResponseEntity.status(200).body(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDto> criar(@RequestBody @Valid CategoriaRequestDto requestDto){
        return ResponseEntity.status(200).body(service.criar(requestDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Null> deletar(@PathVariable Integer id){

        service.deletar(id);

        return ResponseEntity.status(204).build();
    }
}
