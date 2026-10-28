package sptech.school.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.dashboard.CotacoesPorcentagemDto;
import sptech.school.dto.dashboard.DashboardMensalDto;
import sptech.school.dto.dashboard.FornecedorEstatisticasDto;
import sptech.school.dto.movimentacaoEstoque.FechamentoCotacaoRequestDto;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueRequestDto;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoquePaginacaoResponseDto;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueResponseDto;
import sptech.school.dto.usuario.UsuarioResponseDto;
import sptech.school.service.MovimentacaoEstoqueService;
import sptech.school.service.UsuarioService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
@Validated
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

    @GetMapping("/periodo/{idPeriodo}")
    public ResponseEntity<MovimentacaoEstoquePaginacaoResponseDto> listarPorPeriodoAtual(
            @PathVariable Integer idPeriodo,
            @RequestParam(defaultValue = "0") @Min(0) int offset,
            @RequestParam(defaultValue = "10") @Min(1) int limit
    ){
        return ResponseEntity.ok(service.listarPorPeriodoAtual(idPeriodo, offset, limit));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<MovimentacaoEstoqueResponseDto>> buscarPorTipo(
            @PathVariable
            @NotBlank
            @Size(max = 45)
            @Pattern(regexp = "^[\\p{L}\\p{N}\\s._@-]+$")
            String tipo){
        return ResponseEntity.status(200).body(service.buscarPorTipo(tipo));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MovimentacaoEstoqueResponseDto>> buscarPorStatus(
            @PathVariable
            @NotBlank
            @Size(max = 45)
            @Pattern(regexp = "^[\\p{L}\\p{N}\\s._@-]+$")
            String status){
        return ResponseEntity.status(200).body(service.buscarPorStatus(status));
    }

    @PostMapping
    public ResponseEntity<MovimentacaoEstoqueResponseDto> criar(@RequestBody @Valid MovimentacaoEstoqueRequestDto requestDto, Authentication authentication){
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        return ResponseEntity.status(201).body(service.criar(requestDto, authentication.getName()));
    }

    @PostMapping("/cotacoes/{id}/fechamento")
    public ResponseEntity<MovimentacaoEstoqueResponseDto> fecharCotacao(
            @PathVariable Integer id,
            @RequestBody @Valid FechamentoCotacaoRequestDto requestDto,
            Authentication authentication) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        return ResponseEntity.status(201).body(service.fecharCotacao(id, requestDto, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimentacaoEstoqueResponseDto> editar(@RequestBody @Valid MovimentacaoEstoqueRequestDto requestDto, @PathVariable Integer id, Authentication authentication){
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        return ResponseEntity.status(200).body(service.editar(requestDto, id, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Null> deletar(@PathVariable Integer id){
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        service.cancelar(id);

        return ResponseEntity.status(204).build();
    }

    @PatchMapping("/{id}/cancelamento")
    public ResponseEntity<Void> cancelar(@PathVariable Integer id) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        service.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<MovimentacaoEstoqueResponseDto>> pesquisarEFiltrar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDate dataEntrega,
            @RequestParam(required = false) LocalDate dataPrevista,
            @RequestParam(required = false) LocalDate dataMovimentacao,
            @RequestParam(required = false) Boolean pagadorFrete
    ) {
        List<MovimentacaoEstoqueResponseDto> resultados = service.pesquisarFiltro(
                tipo, status, dataEntrega, dataPrevista, dataMovimentacao, pagadorFrete
        );
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/{id}/fornecedores-estatisticas")
    public ResponseEntity<List<FornecedorEstatisticasDto>> getEstatisticasFornecedores(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(service.obterEstatisticasFornecedores(id));
    }

    @GetMapping("/{id}/status-cotacoes")
    public ResponseEntity<CotacoesPorcentagemDto> getEstatisticasCotacoes(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(service.obterEstatisticasCotacoes(id));
    }

    @GetMapping("/{id}/dados-mensais")
    public ResponseEntity<List<DashboardMensalDto>> getDadosMensais(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(service.obterEstatisticasMensais(id));
    }
    }
