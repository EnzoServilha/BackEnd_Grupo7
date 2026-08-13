package sptech.school.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueRequestDto;
import sptech.school.entity.MovimentacaoEstoque;
import sptech.school.entity.Periodo;
import sptech.school.entity.Status;
import sptech.school.entity.Usuario;
import sptech.school.exception.EntidadeConflitanteException;
import sptech.school.exception.MovimentacaoNaoEncontrada;
import sptech.school.repository.*;

import java.time.LocalDateTime;
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
    @DisplayName("testes de deletar")
    class DeletarTests {

        @Test
        @DisplayName("Deve lançar MovimentacaoNaoEncontrada ao tentar deletar id inexistente")
        void testandoLancarExcecaoComIdInexistenteAoDeletar() {
            when(movimentacaoRepository.findById(99)).thenReturn(Optional.empty());

            assertThrows(MovimentacaoNaoEncontrada.class, () -> service.deletar(99));

            verify(movimentacaoRepository).findById(99);
        }
    }
}
