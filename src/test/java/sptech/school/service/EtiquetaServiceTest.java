package sptech.school.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.entity.Item;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.exception.PublicacaoEtiquetaException;
import sptech.school.exception.RequisicaoInvalidaException;
import sptech.school.messaging.SolicitacaoEtiquetaMessage;
import sptech.school.messaging.SolicitacaoEtiquetaPublisher;

import java.time.LocalDateTime;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class EtiquetaServiceTest {

    @Mock
    private ItemService itemService;
    @Mock
    private SolicitacaoEtiquetaPublisher publisher;

    @InjectMocks
    private EtiquetaService etiquetaService;

    private Item itemCompleto() {
        Item item = new Item();
        item.setId(7);
        item.setCodigoInterno("ABC-123");
        item.setMarca("Bosch");
        item.setDescricao("Filtro de óleo");
        item.setDataCadastro(LocalDateTime.of(2025, 9, 3, 23, 0, 15));
        return item;
    }

    @Nested
    @DisplayName("Cenários do método solicitarGeracao()")
    class SolicitarGeracaoTests {

        @Test
        @DisplayName("Deve publicar a solicitação com os dados do item e um identificador único")
        void devePublicarSolicitacaoComDadosDoItem() {
            Mockito.when(itemService.buscarPorId(7)).thenReturn(itemCompleto());

            SolicitacaoEtiquetaMessage retorno = etiquetaService.solicitarGeracao(7);

            ArgumentCaptor<SolicitacaoEtiquetaMessage> captor = ArgumentCaptor.forClass(SolicitacaoEtiquetaMessage.class);
            Mockito.verify(publisher).publicar(captor.capture());
            SolicitacaoEtiquetaMessage publicada = captor.getValue();

            Assertions.assertSame(publicada, retorno);
            Assertions.assertDoesNotThrow(() -> UUID.fromString(publicada.solicitacaoId()));
            Assertions.assertEquals(7, publicada.itemId());
            Assertions.assertEquals("ABC-123", publicada.codigoInterno());
            Assertions.assertEquals("Bosch", publicada.marca());
            Assertions.assertEquals("Filtro de óleo", publicada.descricao());
            Assertions.assertEquals("2025-09-03T23:00:15", publicada.dataCadastro());
        }

        @Test
        @DisplayName("Deve gerar um identificador diferente a cada solicitação")
        void deveGerarIdentificadorDiferenteACadaSolicitacao() {
            Mockito.when(itemService.buscarPorId(7)).thenReturn(itemCompleto());

            SolicitacaoEtiquetaMessage primeira = etiquetaService.solicitarGeracao(7);
            SolicitacaoEtiquetaMessage segunda = etiquetaService.solicitarGeracao(7);

            Assertions.assertNotEquals(primeira.solicitacaoId(), segunda.solicitacaoId());
        }

        @Test
        @DisplayName("Deve publicar mesmo quando marca, descrição e data de cadastro não foram informadas")
        void devePublicarComCamposOpcionaisNulos() {
            Item item = new Item();
            item.setId(7);
            item.setCodigoInterno("ABC-123");
            Mockito.when(itemService.buscarPorId(7)).thenReturn(item);

            SolicitacaoEtiquetaMessage retorno = etiquetaService.solicitarGeracao(7);

            Assertions.assertNull(retorno.marca());
            Assertions.assertNull(retorno.descricao());
            Assertions.assertNull(retorno.dataCadastro());
            Mockito.verify(publisher).publicar(retorno);
        }

        @Test
        @DisplayName("Não deve publicar nada quando o item não existe ou está inativo")
        void naoDevePublicarQuandoItemNaoExiste() {
            Mockito.when(itemService.buscarPorId(99)).thenThrow(new EntidadeNaoEncontradaException("Item", 99));

            Assertions.assertThrows(EntidadeNaoEncontradaException.class, () -> etiquetaService.solicitarGeracao(99));

            Mockito.verifyNoInteractions(publisher);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        @DisplayName("Não deve publicar nada quando o item não tem código interno")
        void naoDevePublicarQuandoItemSemCodigoInterno(String codigoInterno) {
            Item item = itemCompleto();
            item.setCodigoInterno(codigoInterno);
            Mockito.when(itemService.buscarPorId(7)).thenReturn(item);

            Assertions.assertThrows(RequisicaoInvalidaException.class, () -> etiquetaService.solicitarGeracao(7));

            Mockito.verifyNoInteractions(publisher);
        }

        @Test
        @DisplayName("Deve propagar a falha de publicação para que a API responda com erro")
        void devePropagarFalhaDePublicacao() {
            Mockito.when(itemService.buscarPorId(7)).thenReturn(itemCompleto());
            Mockito.doThrow(new PublicacaoEtiquetaException("broker fora do ar", null))
                    .when(publisher).publicar(Mockito.any());

            Assertions.assertThrows(PublicacaoEtiquetaException.class, () -> etiquetaService.solicitarGeracao(7));
        }
    }
}
