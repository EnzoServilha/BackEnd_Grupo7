package sptech.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.periodo.FechamentoPeriodoResponseDto;
import sptech.school.dto.periodo.PeriodoQtdPecasDTO;
import sptech.school.entity.Periodo;
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

    @PutMapping("/fechar/{idUsuario}")
    public ResponseEntity<FechamentoPeriodoResponseDto> fecharPeriodo(
            @PathVariable Long idUsuario,
            @RequestParam(defaultValue = "Período criado automaticamente após fechamento") String descricaoNovoPeriodo) {
        return ResponseEntity.ok(periodoService.fecharPeriodo(idUsuario, descricaoNovoPeriodo));
    }

    @PostMapping()
    public ResponseEntity<Periodo> criarNovoPeriodo (@RequestBody String descricao){
        return ResponseEntity.status(200).body(periodoService.cadastrarPeriodo(descricao));
    }

    @GetMapping("/estoque-atual")
    public ResponseEntity<List<PeriodoQtdPecasDTO>> consultarEstoqueAtual() {
        return ResponseEntity.ok(periodoService.consultarEstoqueAtual());
    }
}
