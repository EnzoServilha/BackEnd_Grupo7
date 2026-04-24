package sptech.school.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.itensNaMovimentacao.ItensNaMovimentacaoRequestDto;
import sptech.school.dto.itensNaMovimentacao.ItensNaMovimentacaoResponseDto;
import sptech.school.service.ItemNaMovimentacaoService;

@RestController
@RequestMapping("/itensNaMovimentacao")
public class ItemNaMovimentacaoController {

    private final ItemNaMovimentacaoService service;

    public ItemNaMovimentacaoController(ItemNaMovimentacaoService service) {
        this.service = service;
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
