package sptech.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
        import sptech.school.dto.itensNaMovimentacao.ItensNaMovimentacaoResponseDto;
import sptech.school.entity.ItensNaMovimentacao;
import sptech.school.entity.Periodo;
import sptech.school.service.ItensSimilaresService;
import sptech.school.service.PeriodoService;

import java.util.List;

@RestController
@RequestMapping("/periodos")
public class PeriodoController {
    private final PeriodoService periodoService;

    public PeriodoController(PeriodoService periodoService) {
        this.periodoService = periodoService;
    }

    @GetMapping()
    public ResponseEntity<List<Periodo>> buscarPeriodos (){
        return ResponseEntity.status(200).body(periodoService.buscarTodosPeriodos());
    }

    @GetMapping("/ultimo")
    public ResponseEntity<Periodo> buscarUltimoPeriodo (){
        return ResponseEntity.status(200).body(periodoService.buscarUltimoPeriodo());
    }

    @PutMapping("/fechar")
    public ResponseEntity<Periodo> fecharPeriodo (){
        Periodo periodoAtual = periodoService.buscarUltimoPeriodo();

        Integer qtd = periodoService.contarEstoque(periodoAtual.getId());

        return ResponseEntity.status(200).body(periodoService.fecharEstoque(periodoAtual.getId(), qtd));
    }

    @PostMapping()
    public ResponseEntity<Periodo> criarNovoPeriodo (@RequestBody String descricao){
        return ResponseEntity.status(200).body(periodoService.cadastrarPeriodo(descricao));
    }

    @PutMapping("/atualizarPeriodo/{id}")
    public List<ItensNaMovimentacao> atualizarPeriodo (@PathVariable Long id){
        return periodoService.transferirSaldoParaNovoPeriodo(id);
    }
}
