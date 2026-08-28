package sptech.school.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.movimentacaoEstoque.FechamentoCotacaoRequestDto;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueRequestDto;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueResponseDto;
import sptech.school.service.MovimentacaoEstoqueService;

import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService service;

    public MovimentacaoEstoqueController(MovimentacaoEstoqueService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoEstoqueResponseDto> buscarPorId(@PathVariable Integer id){
        return ResponseEntity.status(200).body(service.buscarPorId(id));
    }

    @GetMapping("/periodo/{idPeriodo}")
    public ResponseEntity<List<MovimentacaoEstoqueResponseDto>> listarPorPeriodoAtual(@PathVariable Integer idPeriodo){
        return ResponseEntity.status(200).body(service.listarPorPeriodoAtual(idPeriodo));
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
    public ResponseEntity<MovimentacaoEstoqueResponseDto> criar(@RequestBody @Valid MovimentacaoEstoqueRequestDto requestDto, Authentication authentication){
        return ResponseEntity.status(201).body(service.criar(requestDto, authentication.getName()));
    }

    @PostMapping("/cotacoes/{id}/fechamento")
    public ResponseEntity<MovimentacaoEstoqueResponseDto> fecharCotacao(
            @PathVariable Integer id,
            @RequestBody @Valid FechamentoCotacaoRequestDto requestDto,
            Authentication authentication) {
        return ResponseEntity.status(201).body(service.fecharCotacao(id, requestDto, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimentacaoEstoqueResponseDto> editar(@RequestBody @Valid MovimentacaoEstoqueRequestDto requestDto, @PathVariable Integer id, Authentication authentication){
        return ResponseEntity.status(200).body(service.editar(requestDto, id, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Null> deletar(@PathVariable Integer id){

        service.cancelar(id);

        return ResponseEntity.status(204).build();
    }

    @PatchMapping("/{id}/cancelamento")
    public ResponseEntity<Void> cancelar(@PathVariable Integer id) {
        service.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}
