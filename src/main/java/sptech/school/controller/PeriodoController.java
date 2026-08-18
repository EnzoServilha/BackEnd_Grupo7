package sptech.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.periodo.FechamentoPeriodoResponseDto;
import sptech.school.dto.periodo.PeriodoQtdPecasDTO;
import sptech.school.entity.Periodo;
import sptech.school.service.PeriodoService;
import sptech.school.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/periodos")
public class PeriodoController {
    private final PeriodoService periodoService;
    private final UsuarioService usuarioService;

    public PeriodoController(PeriodoService periodoService, UsuarioService usuarioService) {
        this.periodoService = periodoService;
        this.usuarioService = usuarioService;
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
    public ResponseEntity<FechamentoPeriodoResponseDto> fecharPeriodo(
            Authentication authentication,
            @RequestParam(defaultValue = "Período criado automaticamente após fechamento") String descricaoNovoPeriodo) {
        Long idUsuario = usuarioService.buscarAtivoPorEmail(authentication.getName()).getId();
        return ResponseEntity.ok(periodoService.fecharPeriodo(idUsuario, descricaoNovoPeriodo));
    }

    @DeleteMapping("/rollback")
    public ResponseEntity<Periodo> rollbackPeriodo() {
        return ResponseEntity.ok(periodoService.rollbackPeriodo());
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
