package sptech.school.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.dto.itensNaMovimentacao.ItensNaMovimentacaoRequestDto;
import sptech.school.entity.ItensNaMovimentacao;
import sptech.school.entity.MovimentacaoEstoque;
import sptech.school.entity.Status;
import sptech.school.entity.Tipo;
import sptech.school.exception.EntidadeConflitanteException;
import sptech.school.exception.ItemNaMovimentacaoNaoEncontrado;
import sptech.school.repository.ItensNaMovimentacaoRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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

        @Test
        @DisplayName("Deve rejeitar exclusão de item de cotação")
        void deveRejeitarExclusaoDeItemDeCotacao() {
            ItensNaMovimentacao itemCotado = itemDeCotacaoPendente();
            when(itemMovimentacaoRepository.findById(1)).thenReturn(Optional.of(itemCotado));

            assertThrows(EntidadeConflitanteException.class, () -> service.deletar(1));

            verify(itemMovimentacaoRepository, never()).deleteById(any());
        }
    }

    @Test
    @DisplayName("Deve rejeitar edição de item de cotação")
    void deveRejeitarEdicaoDeItemDeCotacao() {
        ItensNaMovimentacao itemCotado = itemDeCotacaoPendente();
        when(itemMovimentacaoRepository.findById(1)).thenReturn(Optional.of(itemCotado));
        ItensNaMovimentacaoRequestDto request =
                new ItensNaMovimentacaoRequestDto(10, 20, 2, BigDecimal.TEN);

        assertThrows(EntidadeConflitanteException.class, () -> service.editar(request, 1));

        verify(itemMovimentacaoRepository, never()).save(any());
    }

    private ItensNaMovimentacao itemDeCotacaoPendente() {
        Tipo cotacao = new Tipo();
        cotacao.setNome("COTACAO");
        Status pendente = new Status();
        pendente.setNome("PENDENTE");
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setTipo(cotacao);
        movimentacao.setStatus(pendente);
        ItensNaMovimentacao item = new ItensNaMovimentacao();
        item.setMovimentacaoEstoque(movimentacao);
        return item;
    }
}
