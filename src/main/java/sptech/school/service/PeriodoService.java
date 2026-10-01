package sptech.school.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sptech.school.dto.periodo.FechamentoPeriodoResponseDto;
import sptech.school.dto.periodo.PeriodoQtdPecasDTO;
import sptech.school.entity.Item;
import sptech.school.entity.ItensNaMovimentacao;
import sptech.school.entity.MovimentacaoEstoque;
import sptech.school.entity.Periodo;
import sptech.school.entity.Status;
import sptech.school.entity.Tipo;
import sptech.school.entity.Usuario;
import sptech.school.exception.EntidadeConflitanteException;
import sptech.school.mapper.PeriodoMapper;
import sptech.school.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PeriodoService {

    private final TipoRepository tipoRepository;
    private final PeriodoRepository periodoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    private final ItensNaMovimentacaoRepository itensNaMovimentacaoRepository;
    private final ItemRepository itemRepository;
    private final UsuarioRepository usuarioRepository;
    private final StatusRepository statusRepository;


    public PeriodoService(TipoRepository tipoRepository, PeriodoRepository periodoRepository, MovimentacaoEstoqueRepository movimentacaoEstoqueRepository, ItensNaMovimentacaoRepository itensNaMovimentacaoRepository, ItemRepository itemRepository, UsuarioRepository usuarioRepository, StatusRepository statusRepository) {
        this.tipoRepository = tipoRepository;
        this.periodoRepository = periodoRepository;
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
        this.itensNaMovimentacaoRepository = itensNaMovimentacaoRepository;
        this.itemRepository = itemRepository;
        this.usuarioRepository = usuarioRepository;
        this.statusRepository = statusRepository;
    }

    public Periodo buscarUltimoPeriodo() {
        Periodo periodo = periodoRepository.findFirstByOrderByIdDesc();
        if (periodo == null){
            throw new EntidadeConflitanteException("Erro ao buscar o ultimo período");
        }
        return periodo;
    }

    public List<Periodo> buscarTodosPeriodos(){
        List<Periodo> periodo = periodoRepository.findAllByOrderByIdDesc();
        if (periodo == null){
            throw new EntidadeConflitanteException("Erro ao buscar o ultimo período");
        }
        return periodo;
    }

    @Transactional
    public Periodo cadastrarPeriodo(String descricao){
        if (periodoRepository.findFirstByFechadoFalseOrderByIdDesc().isPresent()) {
            throw new EntidadeConflitanteException("Já existe um período aberto");
        }
        Periodo periodo = new Periodo();

        periodo.setAnotacao(descricao);

        return periodoRepository.save(periodo);
    }

    public Integer contarEstoque (Integer id){

        return periodoRepository.pegarTotalDePecasDoPeriodo(id);

    }

    @Transactional
    public List<PeriodoQtdPecasDTO> consultarEstoqueAtual() {
        Periodo periodoAberto = buscarPeriodoAberto();
        return periodoRepository.pegarSaldoPorItemDoPeriodo(periodoAberto.getId());
    }

    @Transactional
    public FechamentoPeriodoResponseDto fecharPeriodo(Long idUsuario, String descricaoNovoPeriodo) {
        Periodo periodoAtual = buscarPeriodoAberto();
        List<PeriodoQtdPecasDTO> saldos = periodoRepository.pegarSaldoPorItemDoPeriodo(periodoAtual.getId());
        int quantidadeTotal = saldos.stream().mapToInt(PeriodoQtdPecasDTO::getQtd).sum();

        periodoAtual.setQtdPecas(quantidadeTotal);
        periodoAtual.setFechado(true);
        periodoAtual.setDataFechamento(LocalDateTime.now());
        periodoRepository.save(periodoAtual);

        Periodo novoPeriodo = new Periodo();
        novoPeriodo.setAnotacao(descricaoNovoPeriodo);
        novoPeriodo.setFechado(false);
        novoPeriodo = periodoRepository.save(novoPeriodo);

        List<MovimentacaoEstoque> cotacoesPendentes = movimentacaoEstoqueRepository
            .findAllByPeriodoIdAndTipoNomeAndStatusNome(periodoAtual.getId(), "COTACAO", "PENDENTE");
        for (MovimentacaoEstoque cotacao : cotacoesPendentes) {
            cotacao.setPeriodo(novoPeriodo);
        }
        movimentacaoEstoqueRepository.saveAll(cotacoesPendentes);

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntidadeConflitanteException("Usuário responsável pelo fechamento não encontrado"));
        Tipo tipoAjuste = tipoRepository.findByNome("AJUSTE")
                .orElseThrow(() -> new EntidadeConflitanteException("Tipo AJUSTE não encontrado"));
        Status statusConcluido = statusRepository.findByNome("CONCLUIDO")
                .orElseThrow(() -> new EntidadeConflitanteException("Status CONCLUIDO não encontrado"));

        MovimentacaoEstoque movimentacaoAjuste = new MovimentacaoEstoque();
        movimentacaoAjuste.setPeriodo(novoPeriodo);
        movimentacaoAjuste.setUsuario(usuario);
        movimentacaoAjuste.setTipo(tipoAjuste);
        movimentacaoAjuste.setStatus(statusConcluido);
        movimentacaoAjuste.setDataMovimentacao(LocalDateTime.now());
        movimentacaoAjuste.setObservacoes("Saldo inicial transferido do período " + periodoAtual.getId());
        movimentacaoAjuste = movimentacaoEstoqueRepository.save(movimentacaoAjuste);

        List<ItensNaMovimentacao> itensAjuste = new ArrayList<>();
        for (PeriodoQtdPecasDTO saldo : saldos) {
            Item item = itemRepository.findById(saldo.getId())
                    .orElseThrow(() -> new EntidadeConflitanteException("Item do saldo não encontrado"));
            ItensNaMovimentacao itemAjuste = new ItensNaMovimentacao();
            itemAjuste.setMovimentacaoEstoque(movimentacaoAjuste);
            itemAjuste.setItem(item);
            itemAjuste.setQtd(saldo.getQtd());
            itensAjuste.add(itemAjuste);
        }
        itensNaMovimentacaoRepository.saveAll(itensAjuste);

        return new FechamentoPeriodoResponseDto(
                PeriodoMapper.toResponseDto(periodoAtual),
                PeriodoMapper.toResponseDto(novoPeriodo),
                movimentacaoAjuste.getId(),
                saldos
        );
    }

        @Transactional
        public Periodo rollbackPeriodo() {
        Periodo periodoAtual = buscarPeriodoAberto();
        Periodo periodoAnterior = periodoRepository.findFirstByFechadoTrueOrderByIdDesc()
            .orElseThrow(() -> new EntidadeConflitanteException("Não há período anterior para restaurar"));

        List<MovimentacaoEstoque> movimentacoesAtuais = movimentacaoEstoqueRepository
            .findAllByPeriodoId(periodoAtual.getId());
        String observacaoAjuste = "Saldo inicial transferido do período " + periodoAnterior.getId();

        List<MovimentacaoEstoque> ajustesAutomaticos = movimentacoesAtuais.stream()
            .filter(movimentacao -> ehAjusteAutomatico(movimentacao, observacaoAjuste))
            .toList();
        if (ajustesAutomaticos.size() != 1) {
            throw new EntidadeConflitanteException("O período atual não possui um único ajuste automático de abertura");
        }

        boolean possuiMovimentacaoPosterior = movimentacoesAtuais.stream()
            .anyMatch(movimentacao -> !ehAjusteAutomatico(movimentacao, observacaoAjuste)
                && !ehCotacaoPendente(movimentacao));
        if (possuiMovimentacaoPosterior) {
            throw new EntidadeConflitanteException(
                "O período atual possui movimentações e não pode ser removido");
        }

        List<MovimentacaoEstoque> cotacoesPendentes = movimentacoesAtuais.stream()
            .filter(this::ehCotacaoPendente)
            .toList();
        for (MovimentacaoEstoque cotacao : cotacoesPendentes) {
            cotacao.setPeriodo(periodoAnterior);
        }
        movimentacaoEstoqueRepository.saveAll(cotacoesPendentes);

        MovimentacaoEstoque ajusteAutomatico = ajustesAutomaticos.get(0);
        itensNaMovimentacaoRepository.deleteAllByMovimentacaoEstoqueId(ajusteAutomatico.getId());
        movimentacaoEstoqueRepository.delete(ajusteAutomatico);
        periodoRepository.delete(periodoAtual);

        periodoAnterior.setFechado(false);
        periodoAnterior.setDataFechamento(null);
        periodoAnterior.setQtdPecas(null);
        return periodoRepository.save(periodoAnterior);
        }

        private boolean ehAjusteAutomatico(MovimentacaoEstoque movimentacao, String observacaoEsperada) {
        return movimentacao.getTipo() != null
            && "AJUSTE".equals(movimentacao.getTipo().getNome())
            && observacaoEsperada.equals(movimentacao.getObservacoes());
        }

        private boolean ehCotacaoPendente(MovimentacaoEstoque movimentacao) {
        return movimentacao.getTipo() != null
            && "COTACAO".equals(movimentacao.getTipo().getNome())
            && movimentacao.getStatus() != null
            && "PENDENTE".equals(movimentacao.getStatus().getNome());
        }

    private Periodo buscarPeriodoAberto() {
        return periodoRepository.findFirstByFechadoFalseOrderByIdDesc()
                .orElseThrow(() -> new EntidadeConflitanteException("Não há período aberto"));
    }
}

