package sptech.school.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.tipo.TipoRequestDto;
import sptech.school.dto.tipo.TipoResponseDto;
import sptech.school.service.TipoService;

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
}

