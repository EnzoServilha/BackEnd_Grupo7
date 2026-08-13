package sptech.school.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.exception.MovimentacaoNaoEncontrada;
import sptech.school.repository.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("MovimentacaoEstoqueService")
class MovimentacaoEstoqueServiceTest {

    @Mock
    private MovimentacaoRepository movimentacaoRepository;

    @InjectMocks
    private MovimentacaoEstoqueService service;

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
