package sptech.school.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.repository.TipoRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TipoService")
class TipoServiceTest {

    @Mock
    private TipoRepository repository;

    @InjectMocks
    private TipoService service;

    @Nested
    @DisplayName("testes de deletar")
    class DeletarTests {

        @Test
        @DisplayName("Deve lançar EntidadeNaoEncontradaException ao deletar tipo com id inexistente")
        void testandoLancarExcecaoComIdInexistenteAoDeletar() {
            when(repository.existsById(99)).thenReturn(false);

            assertThrows(EntidadeNaoEncontradaException.class, () -> service.deletar(99));

            verify(repository).existsById(99);
        }
    }
}
