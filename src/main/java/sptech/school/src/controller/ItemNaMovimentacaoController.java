package sptech.school.src.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.src.dto.itensNaMovimentacao.ItensNaMovimentacaoRequestDto;
import sptech.school.src.dto.itensNaMovimentacao.ItensNaMovimentacaoResponseDto;
import sptech.school.src.service.ItemNaMovimentacaoService;

import java.util.List;

@RestController
@RequestMapping("/itensNaMovimentacao")
public class ItemNaMovimentacaoController {

    private final ItemNaMovimentacaoService service;

    public ItemNaMovimentacaoController(ItemNaMovimentacaoService service) {
        this.service = service;
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
        return ResponseEntity.status(201).body(service.criar(requestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItensNaMovimentacaoResponseDto> editar(@RequestBody @Valid ItensNaMovimentacaoRequestDto requestDto, @PathVariable Integer id){
        return ResponseEntity.status(200).body(service.editar(requestDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Null> deletar(@PathVariable Integer id){
        service.deletar(id);

        return ResponseEntity.status(204).build();
    }
}
