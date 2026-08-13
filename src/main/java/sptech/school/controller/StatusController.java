package sptech.school.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.status.StatusRequestDto;
import sptech.school.dto.status.StatusResponseDto;
import sptech.school.service.StatusService;

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
}

