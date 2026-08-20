package sptech.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.periodo.PeriodoQtdPecasDTO;
import sptech.school.dto.periodo.PeriodoResponseDto;
import sptech.school.dto.usuario.UsuarioResponseDto;
import sptech.school.entity.ItensNaMovimentacao;
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
    public ResponseEntity<Periodo> fecharPeriodo (){
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

//        Periodo periodoAtual = periodoService.buscarUltimoPeriodo();
//
//        Integer qtd = periodoService.contarEstoque(periodoAtual.getId());

        return ResponseEntity.status(200).body(periodoService.fecharEstoque());
    }

    @PostMapping()
    public ResponseEntity<Periodo> criarNovoPeriodo (@RequestBody String descricao){
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        return ResponseEntity.status(200).body(periodoService.cadastrarPeriodo(descricao));
    }

    @PutMapping("/atualizarPeriodo/{id}")
    public ResponseEntity<List<PeriodoQtdPecasDTO>> atualizarPeriodo (@PathVariable Long id){
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        return ResponseEntity.ok(periodoService.transferirSaldoParaNovoPeriodo(id));
    }
}
