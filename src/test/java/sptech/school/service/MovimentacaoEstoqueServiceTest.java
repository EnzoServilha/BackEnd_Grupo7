package sptech.school.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueRequestDto;
import sptech.school.dto.movimentacaoEstoque.FechamentoCotacaoRequestDto;
import sptech.school.dto.movimentacaoEstoque.ItemFechamentoCotacaoRequestDto;
import sptech.school.entity.Item;
import sptech.school.entity.ItensNaMovimentacao;
import sptech.school.entity.MovimentacaoEstoque;
import sptech.school.entity.Periodo;
import sptech.school.entity.Status;
import sptech.school.entity.Tipo;
import sptech.school.entity.Usuario;
import sptech.school.exception.EntidadeConflitanteException;
import sptech.school.exception.MovimentacaoNaoEncontrada;
import sptech.school.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MovimentacaoEstoqueService")
class MovimentacaoEstoqueServiceTest {

    @Mock
    private MovimentacaoRepository movimentacaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PeriodoRepository periodoRepository;

    @Mock
    private TipoRepository tipoRepository;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private ItensNaMovimentacaoRepository itensNaMovimentacaoRepository;

    @InjectMocks
    private MovimentacaoEstoqueService service;

    private MovimentacaoEstoqueRequestDto criarRequest(Integer periodoId) {
        return new MovimentacaoEstoqueRequestDto(
                null, null, null, null, "Observação atualizada",
                null, null, null, null, null, "NF-1", periodoId);
    }

    @Nested
    @DisplayName("criar() e editar()")
    class PersistenciaTests {

        @Test
        @DisplayName("Deve rejeitar criação em período fechado")
        void deveRejeitarPeriodoFechado() {
            Usuario usuario = new Usuario();
            Periodo periodo = new Periodo();
            periodo.setFechado(true);

            when(usuarioRepository.findByEmailAndAtivoTrue("usuario@teste.com"))
                    .thenReturn(Optional.of(usuario));
            when(periodoRepository.findById(1)).thenReturn(Optional.of(periodo));

            assertThrows(EntidadeConflitanteException.class,
                    () -> service.criar(criarRequest(1), "usuario@teste.com"));

            verify(movimentacaoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve preservar data original e usar usuário autenticado na edição")
        void devePreservarDataEUsarUsuarioAutenticado() {
            LocalDateTime dataOriginal = LocalDateTime.of(2026, 1, 10, 9, 30);
            Status pendente = new Status();
            pendente.setNome("PENDENTE");
            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
            movimentacao.setId(10);
            movimentacao.setStatus(pendente);
            movimentacao.setDataMovimentacao(dataOriginal);

            Usuario usuarioAutenticado = new Usuario();
            usuarioAutenticado.setId(7L);
            Periodo periodoAberto = new Periodo();
            periodoAberto.setId(2);
            periodoAberto.setFechado(false);

            when(movimentacaoRepository.findById(10)).thenReturn(Optional.of(movimentacao));
            when(usuarioRepository.findByEmailAndAtivoTrue("usuario@teste.com"))
                    .thenReturn(Optional.of(usuarioAutenticado));
            when(periodoRepository.findById(2)).thenReturn(Optional.of(periodoAberto));
            when(movimentacaoRepository.save(movimentacao)).thenReturn(movimentacao);

            service.editar(criarRequest(2), 10, "usuario@teste.com");

            assertEquals(dataOriginal, movimentacao.getDataMovimentacao());
            assertSame(usuarioAutenticado, movimentacao.getUsuario());
            assertSame(periodoAberto, movimentacao.getPeriodo());
            verify(movimentacaoRepository).save(movimentacao);
        }

        @Test
        @DisplayName("Deve rejeitar edição de cotação pendente")
        void deveRejeitarEdicaoDeCotacao() {
            MovimentacaoEstoque cotacao = new MovimentacaoEstoque();
            cotacao.setTipo(tipo("COTACAO"));
            cotacao.setStatus(status("PENDENTE"));
            when(movimentacaoRepository.findById(10)).thenReturn(Optional.of(cotacao));

            assertThrows(EntidadeConflitanteException.class,
                    () -> service.editar(criarRequest(2), 10, "usuario@teste.com"));

            verify(movimentacaoRepository, never()).save(any());
            verifyNoInteractions(usuarioRepository, periodoRepository);
        }
    }

    @Nested
    @DisplayName("buscarPorId()")
    class BuscarPorIdTests {

        @Test
        @DisplayName("Deve lançar MovimentacaoNaoEncontrada quando o id não existir")
        void testandoLancarExcecaoComIdInexistenteAoBuscar() {
            when(movimentacaoRepository.findById(99)).thenReturn(Optional.empty());

            assertThrows(MovimentacaoNaoEncontrada.class, () -> service.buscarPorId(99));

            verify(movimentacaoRepository).findById(99);
        }
    }

    @Nested
    @DisplayName("fecharCotacao()")
    class FecharCotacaoTests {

        @Test
        @DisplayName("Deve concluir parcialmente quando vender menos que o cotado")
        void deveConcluirParcialmente() {
            MovimentacaoEstoque cotacao = criarCotacaoPendente(5);
            Usuario usuario = new Usuario();
            Tipo saida = tipo("SAIDA");
            Status concluido = status("CONCLUIDO");
            Status concluidoParcial = status("CONCLUIDO PARCIAL");
            Periodo periodo = periodoAberto(2);

            when(movimentacaoRepository.buscarPorIdComBloqueio(10)).thenReturn(Optional.of(cotacao));
            when(usuarioRepository.findByEmailAndAtivoTrue("usuario@teste.com")).thenReturn(Optional.of(usuario));
            when(tipoRepository.findByNome("SAIDA")).thenReturn(Optional.of(saida));
            when(statusRepository.findByNome("CONCLUIDO")).thenReturn(Optional.of(concluido));
            when(statusRepository.findByNome("CONCLUIDO PARCIAL")).thenReturn(Optional.of(concluidoParcial));
            when(periodoRepository.buscarPeriodoAbertoComBloqueio(2)).thenReturn(Optional.of(periodo));
            when(periodoRepository.pegarSaldoDisponivelDoItem(2, 1)).thenReturn(10L);
            when(movimentacaoRepository.save(any(MovimentacaoEstoque.class))).thenAnswer(invocation -> {
                MovimentacaoEstoque movimentacao = invocation.getArgument(0);
                if (movimentacao.getId() == null) {
                    movimentacao.setId(20);
                }
                return movimentacao;
            });

            var response = service.fecharCotacao(10, requestFechamento(3), "usuario@teste.com");

            assertEquals("SAIDA", response.tipo().nome());
            assertEquals("CONCLUIDO", response.status().nome());
            assertEquals(10, response.movimentacaoOriginalId());
            assertEquals(3, response.qtdItens());
            assertSame(concluidoParcial, cotacao.getStatus());
            verify(itensNaMovimentacaoRepository).saveAll(any());
            verify(movimentacaoRepository, times(2)).save(any(MovimentacaoEstoque.class));
        }

        @Test
        @DisplayName("Deve concluir integralmente quando vender toda a quantidade cotada")
        void deveConcluirIntegralmente() {
            MovimentacaoEstoque cotacao = criarCotacaoPendente(5);
            Status concluido = status("CONCLUIDO");

            when(movimentacaoRepository.buscarPorIdComBloqueio(10)).thenReturn(Optional.of(cotacao));
            when(usuarioRepository.findByEmailAndAtivoTrue("usuario@teste.com")).thenReturn(Optional.of(new Usuario()));
            when(tipoRepository.findByNome("SAIDA")).thenReturn(Optional.of(tipo("SAIDA")));
            when(statusRepository.findByNome("CONCLUIDO")).thenReturn(Optional.of(concluido));
            when(periodoRepository.buscarPeriodoAbertoComBloqueio(2)).thenReturn(Optional.of(periodoAberto(2)));
            when(periodoRepository.pegarSaldoDisponivelDoItem(2, 1)).thenReturn(5L);
            when(movimentacaoRepository.save(any(MovimentacaoEstoque.class))).thenAnswer(invocation -> invocation.getArgument(0));

            service.fecharCotacao(10, requestFechamento(5), "usuario@teste.com");

            assertSame(concluido, cotacao.getStatus());
        }

        @Test
        @DisplayName("Deve rejeitar quantidade superior à cotada")
        void deveRejeitarQuantidadeSuperior() {
            MovimentacaoEstoque cotacao = criarCotacaoPendente(5);
            when(periodoRepository.buscarPeriodoAbertoComBloqueio(2))
                    .thenReturn(Optional.of(periodoAberto(2)));
            when(movimentacaoRepository.buscarPorIdComBloqueio(10)).thenReturn(Optional.of(cotacao));

            assertThrows(EntidadeConflitanteException.class,
                    () -> service.fecharCotacao(10, requestFechamento(6), "usuario@teste.com"));

            verify(movimentacaoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve rejeitar fechamento quando o estoque for insuficiente")
        void deveRejeitarEstoqueInsuficiente() {
            MovimentacaoEstoque cotacao = criarCotacaoPendente(5);
            when(movimentacaoRepository.buscarPorIdComBloqueio(10)).thenReturn(Optional.of(cotacao));
            when(periodoRepository.buscarPeriodoAbertoComBloqueio(2))
                    .thenReturn(Optional.of(periodoAberto(2)));
            when(periodoRepository.pegarSaldoDisponivelDoItem(2, 1)).thenReturn(2L);

            EntidadeConflitanteException exception = assertThrows(EntidadeConflitanteException.class,
                    () -> service.fecharCotacao(10, requestFechamento(3), "usuario@teste.com"));

            assertEquals("Estoque insuficiente para o item 1. Quantidade solicitada: 3. "
                    + "Quantidade disponível: 2", exception.getMessage());
            verify(movimentacaoRepository, never()).save(any());
            verify(itensNaMovimentacaoRepository, never()).saveAll(any());
            verifyNoInteractions(usuarioRepository);
        }

        @Test
        @DisplayName("Deve rejeitar fechamento quando não houver estoque")
        void deveRejeitarEstoqueZero() {
            MovimentacaoEstoque cotacao = criarCotacaoPendente(5);
            when(movimentacaoRepository.buscarPorIdComBloqueio(10)).thenReturn(Optional.of(cotacao));
            when(periodoRepository.buscarPeriodoAbertoComBloqueio(2))
                    .thenReturn(Optional.of(periodoAberto(2)));
            when(periodoRepository.pegarSaldoDisponivelDoItem(2, 1)).thenReturn(0L);

            assertThrows(EntidadeConflitanteException.class,
                    () -> service.fecharCotacao(10, requestFechamento(1), "usuario@teste.com"));

            verify(movimentacaoRepository, never()).save(any());
            verify(itensNaMovimentacaoRepository, never()).saveAll(any());
        }

        private MovimentacaoEstoque criarCotacaoPendente(int quantidade) {
            Item item = new Item();
            item.setId(1);
            MovimentacaoEstoque cotacao = new MovimentacaoEstoque();
            cotacao.setId(10);
            cotacao.setTipo(tipo("COTACAO"));
            cotacao.setStatus(status("PENDENTE"));

            ItensNaMovimentacao itemCotado = new ItensNaMovimentacao();
            itemCotado.setItem(item);
            itemCotado.setQtd(quantidade);
            itemCotado.setPrecoUnitario(BigDecimal.TEN);
            itemCotado.setMovimentacaoEstoque(cotacao);
            cotacao.setItens(List.of(itemCotado));
            return cotacao;
        }

        private FechamentoCotacaoRequestDto requestFechamento(int quantidade) {
            return new FechamentoCotacaoRequestDto(
                    List.of(new ItemFechamentoCotacaoRequestDto(1, quantidade, BigDecimal.TEN)),
                    null, null, null, null, null, "NF-1", 2);
        }

        private Tipo tipo(String nome) {
            Tipo tipo = new Tipo();
            tipo.setNome(nome);
            return tipo;
        }

        private Status status(String nome) {
            Status status = new Status();
            status.setNome(nome);
            return status;
        }

        private Periodo periodoAberto(Integer id) {
            Periodo periodo = new Periodo();
            periodo.setId(id);
            periodo.setFechado(false);
            return periodo;
        }
    }

    @Nested
    @DisplayName("testes de deletar")
    class DeletarTests {

        @Test
        @DisplayName("Deve lançar MovimentacaoNaoEncontrada ao tentar deletar id inexistente")
        void testandoLancarExcecaoComIdInexistenteAoDeletar() {
            when(movimentacaoRepository.findById(99)).thenReturn(Optional.empty());

            assertThrows(MovimentacaoNaoEncontrada.class, () -> service.deletar(99));

            verify(movimentacaoRepository).findById(99);
        }

        @Test
        @DisplayName("Deve permitir cancelar cotação pendente sem alterar seu conteúdo")
        void devePermitirCancelarCotacaoPendente() {
            MovimentacaoEstoque cotacao = new MovimentacaoEstoque();
            cotacao.setTipo(tipo("COTACAO"));
            cotacao.setStatus(status("PENDENTE"));
            Status cancelado = status("CANCELADO");
            when(movimentacaoRepository.findById(10)).thenReturn(Optional.of(cotacao));
            when(statusRepository.findByNome("CANCELADO")).thenReturn(Optional.of(cancelado));

            service.cancelar(10);

            assertSame(cancelado, cotacao.getStatus());
            verify(movimentacaoRepository).save(cotacao);
        }
    }

    private Tipo tipo(String nome) {
        Tipo tipo = new Tipo();
        tipo.setNome(nome);
        return tipo;
    }

    private Status status(String nome) {
        Status status = new Status();
        status.setNome(nome);
        return status;
    }
}
