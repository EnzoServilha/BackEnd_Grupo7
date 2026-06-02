package sptech.school.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.entity.Item;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.exception.ItemSimilarJaAssociadoException;
import sptech.school.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ItensSimilaresServiceTest {

    @Mock
    private ItemRepository itemRepository;
//    @Mock
//    private ItensSimilaresRepository itemSimilaresRepository;
//    @Mock
//    private CodigoAssociadoRepository codigoAssociadoRepository;

    @InjectMocks
    private ItensSimilaresService itensSimilaresService;

    @Nested
    @DisplayName("Cenários do método listarSimilares()")
    class ListarSimilaresTests {

        // Cenário feliz
        @Test
        @DisplayName("Deve retornar lista de itens similares")
        void deveRetornarListaDeItensSimilares() {
            // Given
            Integer itemId = 1;

            Item similar = new Item();
            similar.setId(2);

            Item item = new Item();
            item.setId(itemId);
            item.setItensSimilares(List.of(similar));

            // When
            Mockito.when(itemRepository.findById(itemId))
                    .thenReturn(Optional.of(item));
            List<Item> resultado = itensSimilaresService.listarSimilares(itemId);

            // Then
            Assertions.assertEquals(1, resultado.size());
            Assertions.assertEquals(2, resultado.get(0).getId());
        }

        // Cenário triste
        @Test
        @DisplayName("Deve lançar exception quando item não existe")
        void deveLancarExceptionQuandoItemNaoExiste() {
            // Given
            Integer itemId = 1;

            // when
            Mockito.when(itemRepository.findById(itemId))
                    .thenReturn(Optional.empty());

            // then
            Assertions.assertThrows(
                    EntidadeNaoEncontradaException.class,
                    () -> itensSimilaresService.listarSimilares(itemId)
            );
        }
    }

    @Nested
    @DisplayName("Cenários do método adicionarSimilar()")
    class AdicionarSimilarTests {

        // Cenário feliz
        @Test
        @DisplayName("Deve adicionar item similar")
        void deveAdicionarItemSimilar() {
            // Given
            Integer itemId = 1;
            Integer similarId = 2;

            Item item = new Item();
            item.setId(itemId);
            item.setItensSimilares(new ArrayList<>());

            Item similar = new Item();
            similar.setId(similarId);

            // When
            Mockito.when(itemRepository.findById(itemId))
                    .thenReturn(Optional.of(item));
            Mockito.when(itemRepository.findById(similarId))
                    .thenReturn(Optional.of(similar));
            Mockito.when(itemRepository.save(item))
                    .thenReturn(item);

            Item resultado = itensSimilaresService.adicionarSimilar(itemId, similarId);

            // Then
            Assertions.assertEquals(1,
                    resultado.getItensSimilares().size());
            Mockito.verify(itemRepository).save(item);
        }

        // Cenário triste 1 (não permitir A -> A)
        @Test
        @DisplayName("Deve lançar exception quando tentar associar item a ele mesmo")
        void deveLancarExceptionQuandoAssociarItemAEleMesmo() {
            // Given
            Integer itemId = 1;

            Item item = new Item();
            item.setId(itemId);

            // When
            Mockito.when(itemRepository.findById(itemId))
                    .thenReturn(Optional.of(item));

            // Then
            Assertions.assertThrows(
                    ItemSimilarJaAssociadoException.class,
                    () -> itensSimilaresService.adicionarSimilar(itemId, itemId)
            );
        }

        // Cenário triste 2 (A -> B já existe e tentam criar B -> A)
        @Test
        @DisplayName("Deve lançar ItemSimilarJaAssociadoException")
        void deveLancarItemSimilarJaAssociadoException() {
            // Given
            Integer itemId = 1;
            Integer similarId = 2;

            Item item = new Item();
            item.setId(itemId);

            Item similar = new Item();
            similar.setId(similarId);

            similar.setItensSimilares(List.of(item));

            // When
            Mockito.when(itemRepository.findById(itemId))
                    .thenReturn(Optional.of(item));
            Mockito.when(itemRepository.findById(similarId))
                    .thenReturn(Optional.of(similar));

            // Then
            Assertions.assertThrows(
                    ItemSimilarJaAssociadoException.class,
                    () -> itensSimilaresService.adicionarSimilar(itemId, similarId)
            );
        }
    }

    @Nested
    @DisplayName("Cenários do método removerSimilar()")
    class RemoverSimilarTests {

        // Cenário feliz
        @Test
        @DisplayName("Deve remover item similar")
        void deveRemoverItemSimilar() {
            // Given
            Integer itemId = 1;
            Integer similarId = 2;

            Item similar = new Item();
            similar.setId(similarId);

            Item item = new Item();
            item.setId(itemId);
            item.setItensSimilares(new ArrayList<>(List.of(similar)));

            // When
            Mockito.when(itemRepository.findById(itemId))
                    .thenReturn(Optional.of(item));
            Mockito.when(itemRepository.findById(similarId))
                    .thenReturn(Optional.of(similar));
            Mockito.when(itemRepository.save(item))
                    .thenReturn(item);

            Item resultado = itensSimilaresService.removerSimilar(itemId, similarId);

            // Then
            Assertions.assertTrue(resultado.getItensSimilares().isEmpty());

            Mockito.verify(itemRepository).save(item);
        }

        // Cenário triste 1
        @Test
        @DisplayName("Deve lançar exception quando item não existe")
        void deveLancarExceptionQuandoItemNaoExiste() {
            // Given
            Integer itemId = 1;
            Integer similarId = 2;

            // When
            Mockito.when(itemRepository.findById(itemId))
                    .thenReturn(Optional.empty());

            // Then
            Assertions.assertThrows(
                    EntidadeNaoEncontradaException.class,
                    () -> itensSimilaresService.removerSimilar(itemId, similarId)
            );
        }

        // Cenário triste 2
        @Test
        @DisplayName("Deve remover associação mesmo quando parâmetros vierem invertidos")
        void deveRemoverAssociacaoInvertida() {
            // Given
            Integer itemId = 2;
            Integer similarId = 1;

            Item item1 = new Item();
            item1.setId(1);

            Item item2 = new Item();
            item2.setId(2);

            item1.setItensSimilares(new ArrayList<>(List.of(item2)));
            item2.setItensSimilares(new ArrayList<>());

            // When
            Mockito.when(itemRepository.findById(2))
                    .thenReturn(Optional.of(item2));
            Mockito.when(itemRepository.findById(1))
                    .thenReturn(Optional.of(item1));
            Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            Item resultado = itensSimilaresService.removerSimilar(itemId, similarId);

            // Then
            Assertions.assertTrue(item1.getItensSimilares().isEmpty());
            Mockito.verify(itemRepository).save(item1);
            Mockito.verify(itemRepository).save(item2);
        }
    }
}