package sptech.school.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.exception.ItemNaMovimentacaoNaoEncontrado;
import sptech.school.repository.ItensNaMovimentacaoRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ItemNaMovimentacaoService")
class ItemNaMovimentacaoServiceTest {

    @Mock
    private ItensNaMovimentacaoRepository itemMovimentacaoRepository;

    @InjectMocks
    private ItemNaMovimentacaoService service;

    @Nested
    @DisplayName("testes de deletar")
    class DeletarTests {

        @Test
        @DisplayName("Deve lançar ItemNaMovimentacaoNaoEncontrado ao tentar deletar id inexistente")
        void testandoLancarExcecaoComIdInexistenteAoDeletar() {
            when(itemMovimentacaoRepository.findById(99)).thenReturn(Optional.empty());

            assertThrows(ItemNaMovimentacaoNaoEncontrado.class, () -> service.deletar(99));

            verify(itemMovimentacaoRepository).findById(99);
        }
    }
}
