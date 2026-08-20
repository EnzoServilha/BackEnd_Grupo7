package sptech.school.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueRequestDto;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueResponseDto;
import sptech.school.dto.usuario.UsuarioResponseDto;
import sptech.school.service.MovimentacaoEstoqueService;
import sptech.school.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService service;
    private final UsuarioService usuarioService;

    public MovimentacaoEstoqueController(MovimentacaoEstoqueService service, UsuarioService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoEstoqueResponseDto> buscarPorId(@PathVariable Integer id){
        return ResponseEntity.status(200).body(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<MovimentacaoEstoqueResponseDto>> listar(){
        return ResponseEntity.status(200).body(service.listar());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<MovimentacaoEstoqueResponseDto>> buscarPorTipo(@PathVariable String tipo){
        return ResponseEntity.status(200).body(service.buscarPorTipo(tipo));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MovimentacaoEstoqueResponseDto>> buscarPorStatus(@PathVariable String status){
        return ResponseEntity.status(200).body(service.buscarPorStatus(status));
    }

    @PostMapping
    public ResponseEntity<MovimentacaoEstoqueResponseDto> criar(@RequestBody @Valid MovimentacaoEstoqueRequestDto requestDto){
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        return ResponseEntity.status(201).body(service.criar(requestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimentacaoEstoqueResponseDto> editar(@RequestBody @Valid MovimentacaoEstoqueRequestDto requestDto, @PathVariable Integer id){
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        return ResponseEntity.status(200).body(service.editar(requestDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Null> deletar(@PathVariable Integer id){
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        service.deletar(id);

        return ResponseEntity.status(200).build();
    }
}
