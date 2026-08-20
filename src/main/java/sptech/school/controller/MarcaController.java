package sptech.school.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.marca.MarcaRequestDto;
import sptech.school.dto.marca.MarcaResponseDto;
import sptech.school.dto.usuario.UsuarioResponseDto;
import sptech.school.service.MarcaService;
import sptech.school.service.UsuarioService;

@RestController
@RequestMapping("/marcas")
public class MarcaController {

    private MarcaService service;
    private final UsuarioService usuarioService;

    public MarcaController(MarcaService service, UsuarioService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
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
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        return ResponseEntity.status(200).body(service.criar(requestDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Null> deletar(@PathVariable Integer id){
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        service.deletar(id);

        return ResponseEntity.status(204).build();
    }
}
