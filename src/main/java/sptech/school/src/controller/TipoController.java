package sptech.school.src.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.src.dto.tipo.TipoRequestDto;
import sptech.school.src.dto.tipo.TipoResponseDto;
import sptech.school.src.service.TipoService;

@RestController
@RequestMapping("/tipos")
public class TipoController {

    private final TipoService service;

    public TipoController(TipoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TipoResponseDto> criar(@RequestBody @Valid TipoRequestDto requestDto) {
        return ResponseEntity.status(201).body(service.criar(requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Null> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.status(204).build();
    }
}

