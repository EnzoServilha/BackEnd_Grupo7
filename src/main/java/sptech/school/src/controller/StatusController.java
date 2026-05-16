package sptech.school.src.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.src.dto.status.StatusRequestDto;
import sptech.school.src.dto.status.StatusResponseDto;
import sptech.school.src.service.StatusService;

@RestController
@RequestMapping("/status")
public class StatusController {

    private final StatusService service;

    public StatusController(StatusService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<StatusResponseDto> criar(@RequestBody @Valid StatusRequestDto requestDto) {
        return ResponseEntity.status(201).body(service.criar(requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Null> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.status(204).build();
    }
}

