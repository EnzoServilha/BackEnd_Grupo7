package sptech.school.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.itensNaMovimentacao.ItensNaMovimentacaoRequestDto;
import sptech.school.dto.itensNaMovimentacao.ItensNaMovimentacaoResponseDto;
import sptech.school.service.ItemNaMovimentacaoService;
import sptech.school.dto.usuario.UsuarioResponseDto;
import sptech.school.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/itensNaMovimentacao")
public class ItemNaMovimentacaoController {

    private final ItemNaMovimentacaoService service;
    private final UsuarioService usuarioService;

    public ItemNaMovimentacaoController(ItemNaMovimentacaoService service, UsuarioService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }


    @GetMapping
    public ResponseEntity<List<ItensNaMovimentacaoResponseDto>> listar(){
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<List<ItensNaMovimentacaoResponseDto>> listarPorItem(@PathVariable Integer id){
        return ResponseEntity.ok(service.listarPorItem(id));
    }

    @GetMapping("/movimentacao/{id}")
    public ResponseEntity<List<ItensNaMovimentacaoResponseDto>> listarPorMovimentacao(@PathVariable Integer id) {
        return ResponseEntity.ok(service.listarPorMovimentacao(id));
    }

    @PostMapping
    public ResponseEntity<ItensNaMovimentacaoResponseDto> criar(@RequestBody @Valid ItensNaMovimentacaoRequestDto requestDto){
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        return ResponseEntity.status(201).body(service.criar(requestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItensNaMovimentacaoResponseDto> editar(@RequestBody @Valid ItensNaMovimentacaoRequestDto requestDto, @PathVariable Integer id){
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        return ResponseEntity.status(200).body(service.editar(requestDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Null> deletar(@PathVariable Integer id){
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        service.deletar(id);

        return ResponseEntity.status(204).build();
    }
}
