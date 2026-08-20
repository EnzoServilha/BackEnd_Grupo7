package sptech.school.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.categoria.CategoriaRequestDto;
import sptech.school.dto.categoria.CategoriaResponseDto;
import sptech.school.dto.usuario.UsuarioResponseDto;
import sptech.school.service.CategoriaService;
import sptech.school.service.UsuarioService;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private CategoriaService service;
    private final UsuarioService usuarioService;

    public CategoriaController(CategoriaService service, UsuarioService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
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
