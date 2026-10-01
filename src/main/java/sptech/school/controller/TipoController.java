package sptech.school.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.tipo.TipoRequestDto;
import sptech.school.dto.tipo.TipoResponseDto;
import sptech.school.service.TipoService;
import sptech.school.dto.usuario.UsuarioResponseDto;
import sptech.school.service.UsuarioService;

@RestController
@RequestMapping("/tipos")
public class TipoController {

    private final TipoService service;
    private final UsuarioService usuarioService;

    public TipoController(TipoService service, UsuarioService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<TipoResponseDto> criar(@RequestBody @Valid TipoRequestDto requestDto) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        return ResponseEntity.status(201).body(service.criar(requestDto));
    }
}
