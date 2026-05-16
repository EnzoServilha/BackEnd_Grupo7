package sptech.school.src.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.src.dto.marca.MarcaRequestDto;
import sptech.school.src.dto.marca.MarcaResponseDto;
import sptech.school.src.service.MarcaService;

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

    @GetMapping("/porId/{id}")
    public ResponseEntity<MarcaResponseDto> buscarPorId(@PathVariable Integer id){
        return ResponseEntity.status(200).body(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<MarcaResponseDto> criar(@RequestBody @Valid MarcaRequestDto requestDto){
        return ResponseEntity.status(200).body(service.criar(requestDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Null> deletar(@PathVariable Integer id){

        service.deletar(id);

        return ResponseEntity.status(204).build();
    }
}
