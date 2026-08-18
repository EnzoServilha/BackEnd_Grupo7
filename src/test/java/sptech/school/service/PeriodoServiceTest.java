package sptech.school.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.dto.periodo.FechamentoPeriodoResponseDto;
import sptech.school.dto.periodo.PeriodoQtdPecasDTO;
import sptech.school.dto.periodo.PeriodoResponseDto;
import sptech.school.entity.*;
import sptech.school.exception.EntidadeConflitanteException;
import sptech.school.mapper.PeriodoMapper;
import sptech.school.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PeriodoService")
class PeriodoServiceTest {
    @Mock
    private PeriodoRepository periodoRepository;
    @Mock
    private TipoRepository tipoRepository;
    @Mock
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    @Mock
    private ItensNaMovimentacaoRepository itensNaMovimentacaoRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private StatusRepository statusRepository;

    @InjectMocks
    private PeriodoService periodoService;

    @Nested
    @DisplayName("Buscar ultimo período")
    class BuscarUltimoPeriodoTest {

        @Test
        @DisplayName("Deve retornar último período")
        void buscarUltimoPeriodoComSucesso() {
            Periodo periodo = new Periodo();
            periodo.setId(1);
            periodo.setQtdPecas(30);

            when(periodoRepository.findFirstByOrderByIdDesc()).thenReturn(periodo);

            PeriodoResponseDto response = PeriodoMapper.toResponseDto(periodoService.buscarUltimoPeriodo());

            assertEquals(periodo.getId(), response.getId());
            assertEquals(periodo.getQtdPecas(), response.getQtdPecas());
        }

        @Test
        @DisplayName("Deve lançar exceção quando o período vier null")
        void buscarUltimoPeriodoNull() {
            Periodo periodo = null;

            when(periodoRepository.findFirstByOrderByIdDesc()).thenReturn(periodo);

            assertThrows(EntidadeConflitanteException.class,
                    () -> periodoService.buscarUltimoPeriodo());

        }
    }

    @Nested
    @DisplayName("Fechar período")
    class FecharPeriodoTest {
        @Test
        @DisplayName("Deve fechar, criar novo período e transferir o saldo por item")
        void fecharPeriodoComSucesso() {
            Periodo periodoAberto = new Periodo();
            periodoAberto.setId(2);
            periodoAberto.setFechado(false);
            List<PeriodoQtdPecasDTO> saldos = List.of(
                    new PeriodoQtdPecasDTO(1, 8),
                    new PeriodoQtdPecasDTO(2, 4)
            );
            Usuario usuario = new Usuario();
            usuario.setId(1L);
            Tipo ajuste = new Tipo();
            ajuste.setNome("AJUSTE");
            Status concluido = new Status();
            concluido.setNome("CONCLUIDO");
            Item item1 = new Item();
            item1.setId(1);
            Item item2 = new Item();
            item2.setId(2);
            MovimentacaoEstoque cotacaoPendente = new MovimentacaoEstoque();
            cotacaoPendente.setId(20);
            cotacaoPendente.setPeriodo(periodoAberto);

            when(periodoRepository.findFirstByFechadoFalseOrderByIdDesc()).thenReturn(Optional.of(periodoAberto));
            when(periodoRepository.pegarSaldoPorItemDoPeriodo(2)).thenReturn(saldos);
            when(periodoRepository.save(any(Periodo.class))).thenAnswer(invocation -> {
                Periodo periodo = invocation.getArgument(0);
                if (periodo.getId() == null) periodo.setId(3);
                return periodo;
            });
            when(movimentacaoEstoqueRepository.findAllByPeriodoIdAndTipoNomeAndStatusNome(
                    2, "COTACAO", "PENDENTE")).thenReturn(List.of(cotacaoPendente));
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(tipoRepository.findByNome("AJUSTE")).thenReturn(Optional.of(ajuste));
            when(statusRepository.findByNome("CONCLUIDO")).thenReturn(Optional.of(concluido));
            when(movimentacaoEstoqueRepository.save(any(MovimentacaoEstoque.class))).thenAnswer(invocation -> {
                MovimentacaoEstoque movimentacao = invocation.getArgument(0);
                movimentacao.setId(10);
                return movimentacao;
            });
            when(itemRepository.findById(1)).thenReturn(Optional.of(item1));
            when(itemRepository.findById(2)).thenReturn(Optional.of(item2));

            FechamentoPeriodoResponseDto response = periodoService.fecharPeriodo(1L, "Novo período");

            assertTrue(periodoAberto.getFechado());
            assertNotNull(periodoAberto.getDataFechamento());
            assertEquals(12, periodoAberto.getQtdPecas());
            assertEquals(3, response.novoPeriodo().getId());
            assertFalse(response.novoPeriodo().getFechado());
            assertEquals(10, response.movimentacaoAjusteId());
            assertEquals(saldos, response.saldosTransferidos());
            assertEquals(3, cotacaoPendente.getPeriodo().getId());
            verify(movimentacaoEstoqueRepository).saveAll(List.of(cotacaoPendente));

            ArgumentCaptor<MovimentacaoEstoque> movimentacaoCaptor = ArgumentCaptor.forClass(MovimentacaoEstoque.class);
            verify(movimentacaoEstoqueRepository).save(movimentacaoCaptor.capture());
            assertEquals("AJUSTE", movimentacaoCaptor.getValue().getTipo().getNome());
            assertEquals("CONCLUIDO", movimentacaoCaptor.getValue().getStatus().getNome());
            assertEquals(3, movimentacaoCaptor.getValue().getPeriodo().getId());

            @SuppressWarnings("unchecked")
            ArgumentCaptor<List<ItensNaMovimentacao>> itensCaptor = ArgumentCaptor.forClass(List.class);
            verify(itensNaMovimentacaoRepository).saveAll(itensCaptor.capture());
            assertEquals(2, itensCaptor.getValue().size());
            assertEquals(8, itensCaptor.getValue().get(0).getQtd());
            assertEquals(4, itensCaptor.getValue().get(1).getQtd());
        }

        @Test
        @DisplayName("Não deve criar movimentação quando não houver período aberto")
        void fecharPeriodoSemPeriodoAberto() {
            when(periodoRepository.findFirstByFechadoFalseOrderByIdDesc()).thenReturn(Optional.empty());

            assertThrows(EntidadeConflitanteException.class,
                    () -> periodoService.fecharPeriodo(1L, "Novo período"));
            verifyNoInteractions(movimentacaoEstoqueRepository, itensNaMovimentacaoRepository);
        }
    }

    @Nested
    @DisplayName("Rollback de período")
    class RollbackPeriodoTest {

        @Test
        @DisplayName("Deve remover período atual, ajuste automático e reabrir período anterior")
        void rollbackPeriodoComSucesso() {
            Periodo periodoAnterior = periodo(2, true);
            periodoAnterior.setQtdPecas(12);
            periodoAnterior.setDataFechamento(LocalDateTime.now());
            Periodo periodoAtual = periodo(3, false);

            MovimentacaoEstoque ajuste = movimentacao(
                    10, periodoAtual, "AJUSTE", "CONCLUIDO",
                    "Saldo inicial transferido do período 2");
            MovimentacaoEstoque cotacao = movimentacao(
                    20, periodoAtual, "COTACAO", "PENDENTE", null);

            when(periodoRepository.findFirstByFechadoFalseOrderByIdDesc()).thenReturn(Optional.of(periodoAtual));
            when(periodoRepository.findFirstByFechadoTrueOrderByIdDesc()).thenReturn(Optional.of(periodoAnterior));
            when(movimentacaoEstoqueRepository.findAllByPeriodoId(3)).thenReturn(List.of(ajuste, cotacao));
            when(periodoRepository.save(periodoAnterior)).thenReturn(periodoAnterior);

            Periodo restaurado = periodoService.rollbackPeriodo();

            assertSame(periodoAnterior, restaurado);
            assertFalse(restaurado.getFechado());
            assertNull(restaurado.getDataFechamento());
            assertNull(restaurado.getQtdPecas());
            assertSame(periodoAnterior, cotacao.getPeriodo());
            verify(movimentacaoEstoqueRepository).saveAll(List.of(cotacao));
            verify(itensNaMovimentacaoRepository).deleteAllByMovimentacaoEstoqueId(10);
            verify(movimentacaoEstoqueRepository).delete(ajuste);
            verify(periodoRepository).delete(periodoAtual);
        }

        @Test
        @DisplayName("Não deve remover período atual quando houver venda")
        void naoDeveRemoverPeriodoComVenda() {
            Periodo periodoAnterior = periodo(2, true);
            Periodo periodoAtual = periodo(3, false);
            MovimentacaoEstoque ajuste = movimentacao(
                    10, periodoAtual, "AJUSTE", "CONCLUIDO",
                    "Saldo inicial transferido do período 2");
            MovimentacaoEstoque venda = movimentacao(
                    30, periodoAtual, "SAIDA", "CONCLUIDO", null);

            when(periodoRepository.findFirstByFechadoFalseOrderByIdDesc()).thenReturn(Optional.of(periodoAtual));
            when(periodoRepository.findFirstByFechadoTrueOrderByIdDesc()).thenReturn(Optional.of(periodoAnterior));
            when(movimentacaoEstoqueRepository.findAllByPeriodoId(3)).thenReturn(List.of(ajuste, venda));

            assertThrows(EntidadeConflitanteException.class, () -> periodoService.rollbackPeriodo());

            verify(movimentacaoEstoqueRepository, never()).delete(any());
            verify(periodoRepository, never()).delete(any());
            verifyNoInteractions(itensNaMovimentacaoRepository);
        }

        private Periodo periodo(Integer id, boolean fechado) {
            Periodo periodo = new Periodo();
            periodo.setId(id);
            periodo.setFechado(fechado);
            return periodo;
        }

        private MovimentacaoEstoque movimentacao(Integer id, Periodo periodo, String tipoNome,
                                                  String statusNome, String observacoes) {
            Tipo tipo = new Tipo();
            tipo.setNome(tipoNome);
            Status status = new Status();
            status.setNome(statusNome);
            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
            movimentacao.setId(id);
            movimentacao.setPeriodo(periodo);
            movimentacao.setTipo(tipo);
            movimentacao.setStatus(status);
            movimentacao.setObservacoes(observacoes);
            return movimentacao;
        }
    }

    @Nested
    @DisplayName("Cadastrar Período")
    class CadastrarPeriodoTest {

        @Test
        @DisplayName("Deve cadastrar corretamente")
        void cadastrarPeriodoComSucesso() {
            LocalDateTime agora = LocalDateTime.now();
            String descricao = "teste";
            Integer qtdPecas = 0;

            Periodo periodo = new Periodo();
            periodo.setId(1);
            periodo.setQtdPecas(qtdPecas);
            periodo.setAnotacao(descricao);
            periodo.setDataCriacao(agora);

            when(periodoRepository.findFirstByFechadoFalseOrderByIdDesc()).thenReturn(Optional.empty());
            when(periodoRepository.save(Mockito.any(Periodo.class))).thenReturn(periodo);

            Periodo periodoCadastrado = periodoService.cadastrarPeriodo(descricao);

            assertEquals(agora, periodoCadastrado.getDataCriacao());
            assertEquals(qtdPecas, periodoCadastrado.getQtdPecas());
        }

        @Test
        @DisplayName("Não deve cadastrar quando já existir período aberto")
        void cadastrarPeriodoComPeriodoAberto() {
            Periodo periodoAberto = new Periodo();
            periodoAberto.setFechado(false);
            when(periodoRepository.findFirstByFechadoFalseOrderByIdDesc())
                    .thenReturn(Optional.of(periodoAberto));

            assertThrows(EntidadeConflitanteException.class,
                    () -> periodoService.cadastrarPeriodo("Outro período"));
            verify(periodoRepository, never()).save(any(Periodo.class));
        }
    }
}