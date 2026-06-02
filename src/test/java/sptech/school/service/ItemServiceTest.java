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
import sptech.school.entity.CodigoAssociado;
import sptech.school.entity.Item;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.repository.CodigoAssociadoRepository;
import sptech.school.repository.ItemRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CodigoAssociadoRepository codigoAssociadoRepository;

    @InjectMocks
    private ItemService itemService;

    @Nested
    @DisplayName("Cenários do método listarTodos()")
    class ListarTodosTests {

        // Cenário feliz
        @Test
        @DisplayName("Deve retornar a lista com todos os itens")
        void deveListarCorretamente() {
            // Given
            Item item = new Item();
            item.setId(1);
            List<Item> itens = List.of(item);

            // When
            Mockito.when(itemRepository.findAll())
                    .thenReturn(itens);
            List<Item> resultado = itemService.listarTodos();

            // Then
            Assertions.assertEquals(1, resultado.size());
            Assertions.assertEquals(1, resultado.get(0).getId());
        }

        // Cenário triste
        @Test
        @DisplayName("Deve retornar lista vazia")
        void deveRetornarListaVazia() {
            // CENÁRIO: Não há itens cadastrados
            // Given
            List<Item> itens = Collections.emptyList();

            // When
            Mockito.when(itemRepository.findAll())
                    .thenReturn(itens);
            List<Item> resultado = itemService.listarTodos();

            // Then
            Assertions.assertTrue(resultado.isEmpty());
        }

    }

    @Nested
    @DisplayName("Cenários do método buscarPorId()")
    class BuscarPorIdTests {

        // Cenário feliz
        @Test
        @DisplayName("Deve retornar item encontrado")
        void deveRetornarItemEncontrado() {
            // Given
            Integer id = 1;
            Item item = new Item();
            item.setId(id);
            item.setCodigoInterno("ABC123");

            // When
            Mockito.when(itemRepository.findById(id))
                    .thenReturn(Optional.of(item));
            Item resultado = itemService.buscarPorId(id);

            // Then
            Assertions.assertSame(item, resultado);
        }

        // Cenário triste
        @Test
        @DisplayName("Deve lançar exception quando id não encontrado")
        void deveLancarExceptionQuandoIdNaoEncontrado() {
            // Given
            Integer id = 1;
            Optional<Item> optional = Optional.empty();

            // When
            Mockito.when(itemRepository.findById(id))
                    .thenReturn(optional);

            // Then
            EntidadeNaoEncontradaException exception =
                    Assertions.assertThrows(
                            EntidadeNaoEncontradaException.class,
                            () -> itemService.buscarPorId(id)
                    );
            Assertions.assertEquals("Item com identificador '1' não encontrado(a)",
                    exception.getMessage());
        }

    }


    @Nested
    @DisplayName("Cenários do método buscarPorCodigoInterno()")
    class BuscarPorCodigoInternoTests {

        // Cenário feliz
        @Test
        @DisplayName("Deve retornar item pelo código interno")
        void deveRetornarItemPorCodigoInterno() {
            // Given
            String codigo = "ABC123";
            Item item = new Item();
            item.setCodigoInterno(codigo);

            // When
            Mockito.when(itemRepository.findByCodigoInterno(codigo))
                    .thenReturn(Optional.of(item));
            Item resultado = itemService.buscarPorCodigoInterno(codigo);

            // Then
            Assertions.assertEquals(codigo, resultado.getCodigoInterno());
        }

        // Cenário triste
        @Test
        @DisplayName("Deve lançar exception quando código não encontrado")
        void deveLancarExceptionQuandoCodigoNaoEncontrado() {
            // Given
            String codigo = "ABC123";

            // When
            Mockito.when(itemRepository.findByCodigoInterno(codigo))
                    .thenReturn(Optional.empty());

            // Then
            Assertions.assertThrows(
                    EntidadeNaoEncontradaException.class,
                    () -> itemService.buscarPorCodigoInterno(codigo)
            );
        }
    }


    @Nested
    @DisplayName("Cenários do método listarPorMarca()")
    class ListarPorMarcaTests {

        // Cenário feliz
        @Test
        @DisplayName("Deve retornar itens da marca")
        void deveRetornarItensDaMarca() {
            // Given
            String marca = "Bosch";
            Item item = new Item();
            item.setMarca(marca);

            // When
            Mockito.when(itemRepository.findByMarcaContainingIgnoreCase(marca))
                    .thenReturn(List.of(item));
            List<Item> resultado = itemService.listarPorMarca(marca);

            // Then
            Assertions.assertEquals(1, resultado.size());
        }

        // Cenário triste
        @Test
        @DisplayName("Deve retornar lista vazia quando não encontrar marca")
        void deveRetornarListaVaziaQuandoNaoEncontrarMarca() {
            // Given
            String marca = "Inexistente";

            // When
            Mockito.when(itemRepository.findByMarcaContainingIgnoreCase(marca))
                    .thenReturn(Collections.emptyList());
            List<Item> resultado = itemService.listarPorMarca(marca);

            // Then
            Assertions.assertTrue(resultado.isEmpty());
        }
    }


    @Nested
    @DisplayName("Cenários do método pesquisar()")
    class PesquisarTests {

        // Cenário feliz
        @Test
        @DisplayName("Deve pesquisar itens pelo termo")
        void devePesquisarItens() {
            // Given
            String termo = "parafuso";
            Item item = new Item();
            item.setDescricao("Parafuso sextavado");

            // When
            Mockito.when(itemRepository.pesquisarPorTermo(termo))
                    .thenReturn(List.of(item));
            List<Item> resultado = itemService.pesquisar(termo);

            // Then
            Assertions.assertEquals(1, resultado.size());
        }

        // Cenário triste
        @Test
        @DisplayName("Deve retornar lista vazia na pesquisa")
        void deveRetornarListaVaziaNaPesquisa() {
            // Given
            String termo = "xyz";

            // When
            Mockito.when(itemRepository.pesquisarPorTermo(termo))
                    .thenReturn(Collections.emptyList());
            List<Item> resultado =
                    itemService.pesquisar(termo);

            // Then
            Assertions.assertTrue(resultado.isEmpty());
        }
    }


    @Nested
    @DisplayName("Cenários do método buscarPorCodigoAssociado()")
    class BuscarPorCodigoAssociadoTests {

        // Cenário feliz
        @Test
        @DisplayName("Deve retornar itens pelo código associado")
        void deveRetornarItensPeloCodigoAssociado() {
            // Given
            String codigo = "12345";
            Item item = new Item();

            // When
            Mockito.when(itemRepository.buscarPorCodigoAssociado(codigo))
                    .thenReturn(List.of(item));
            List<Item> resultado = itemService.buscarPorCodigoAssociado(codigo);

            // Then
            Assertions.assertEquals(1, resultado.size());
        }

        // Cenário triste
        @Test
        @DisplayName("Deve retornar lista vazia")
        void deveRetornarListaVazia() {
            // Given
            String codigo = "99999";

            // When
            Mockito.when(itemRepository.buscarPorCodigoAssociado(codigo))
                    .thenReturn(Collections.emptyList());
            List<Item> resultado = itemService.buscarPorCodigoAssociado(codigo);

            // Then
            Assertions.assertTrue(resultado.isEmpty());
        }

    }

    @Nested
    @DisplayName("Cenários do método cadastrar()")
    class CadastrarTests {

        // Cenário feliz
        @Test
        @DisplayName("Deve cadastrar item")
        void deveCadastrarItem() {
            // Given
            Item item = new Item();
            item.setCodigoInterno("ABC123");

            CodigoAssociado codigo = new CodigoAssociado();
            codigo.setId(1);

            Item similar = new Item();
            similar.setId(2);

            List<Integer> codigosIds = List.of(1);
            List<Integer> similaresIds = List.of(2);

            Mockito.when(codigoAssociadoRepository.findAllById(codigosIds))
                    .thenReturn(List.of(codigo));
            Mockito.when(itemRepository.findAllById(similaresIds))
                    .thenReturn(List.of(similar));
            Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                    .thenReturn(item);

            // When
            Item resultado = itemService.cadastrar(item, codigosIds, similaresIds);

            // then
            Assertions.assertNotNull(resultado);
            Mockito.verify(itemRepository).save(item);
        }

        // Cenário triste
        @Test
        @DisplayName("Deve cadastrar item sem códigos e similares")
        void deveCadastrarSemCodigosESimilares() {
            // Given
            Item item = new Item();

            Mockito.when(itemRepository.save(item))
                    .thenReturn(item);

            // When
            Item resultado =
                    itemService.cadastrar(item, null, null);

            // Then
            Assertions.assertNotNull(resultado);

            Mockito.verify(itemRepository).save(item);
        }
    }

    @Nested
    @DisplayName("Cenários do método atualizar()")
    class AtualizarTests {

        // Cenário feliz
        @Test
        @DisplayName("Deve atualizar item corretamente")
        void deveAtualizarCorretamente() {
            // Given
            Integer id = 1;

            Item itemBanco = new Item();
            itemBanco.setId(id);

            Item itemAtualizado = new Item();
            itemAtualizado.setCodigoInterno("NOVO");
            itemAtualizado.setMarca("BOSCH");

            // When
            Mockito.when(itemRepository.findById(id))
                    .thenReturn(Optional.of(itemBanco));
            Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                    .thenReturn(itemBanco);

            Item resultado = itemService.atualizar(id, itemAtualizado);

            // Then
            Assertions.assertEquals("NOVO", resultado.getCodigoInterno());
            Assertions.assertEquals("BOSCH", resultado.getMarca());
            Mockito.verify(itemRepository).save(itemBanco);
        }

        // Cenário triste
        @Test
        @DisplayName("Deve lançar exception quando item não existe")
        void deveLancarExceptionQuandoItemNaoExiste() {
            // Given
            Integer id = 1;
            Item itemAtualizado = new Item();

            // When
            Mockito.when(itemRepository.findById(id))
                    .thenReturn(Optional.empty());

            // Then
            Assertions.assertThrows(
                    EntidadeNaoEncontradaException.class,
                    () -> itemService.atualizar(id, itemAtualizado)
            );
        }
    }

    @Nested
    @DisplayName("Cenários do método deletar()")
    class DeletarTests {

        // Cenário feliz
        @Test
        @DisplayName("Deve deletar item")
        void deveDeletarItem() {
            // Given
            Integer id = 1;

            // When
            Mockito.when(itemRepository.existsById(id))
                    .thenReturn(true);

            // Then
            Assertions.assertDoesNotThrow(
                    () -> itemService.deletar(id)
            );
            Mockito.verify(itemRepository).deleteById(id);
        }

        // Cenário triste
        @Test
        @DisplayName("Deve lançar exception quando item não existe")
        void deveLancarExceptionQuandoItemNaoExiste() {
            // Given
            Integer id = 1;

            // When
            Mockito.when(itemRepository.existsById(id))
                    .thenReturn(false);

            // Then
            Assertions.assertThrows(
                    EntidadeNaoEncontradaException.class,
                    () -> itemService.deletar(id)
            );
            Mockito.verify(itemRepository, Mockito.never())
                    .deleteById(Mockito.anyInt());
        }

    }

    @Nested
    @DisplayName("Cenários do método adicionarCodigoAssociado()")
    class AdicionarCodigoAssociadoTests {

        // Cenário feliz
        @Test
        @DisplayName("Deve adicionar código associado")
        void deveAdicionarCodigoAssociado() {
            // Given
            Integer itemId = 1;
            Integer codigoId = 10;

            Item item = new Item();
            item.setId(itemId);
            item.setCodigosAssociados(new java.util.ArrayList<>());

            CodigoAssociado codigo = new CodigoAssociado();
            codigo.setId(codigoId);

            // When
            Mockito.when(itemRepository.findById(itemId))
                    .thenReturn(Optional.of(item));
            Mockito.when(codigoAssociadoRepository.findById(codigoId))
                    .thenReturn(Optional.of(codigo));
            Mockito.when(itemRepository.save(item))
                    .thenReturn(item);

            Item resultado = itemService.adicionarCodigoAssociado(itemId, codigoId);

            // Then
            Assertions.assertEquals(1, resultado.getCodigosAssociados().size());
            Mockito.verify(itemRepository).save(item);
        }

        // Cenário triste 1
        @Test
        @DisplayName("Deve lançar exception quando item não existir")
        void deveLancarExceptionQuandoItemNaoExistir() {
            // Given
            Integer itemId = 1;
            Integer codigoId = 10;

            // When
            Mockito.when(itemRepository.findById(itemId))
                    .thenReturn(Optional.empty());

            // Then
            Assertions.assertThrows(
                    EntidadeNaoEncontradaException.class,
                    () -> itemService.adicionarCodigoAssociado(itemId, codigoId)
            );
            Mockito.verify(itemRepository, Mockito.never())
                    .save(Mockito.any());
        }

        // Cenário triste 2
        @Test
        @DisplayName("Deve lançar exception quando código associado não existir")
        void deveLancarExceptionQuandoCodigoNaoExistir() {
            // Given
            Integer itemId = 1;
            Integer codigoId = 10;

            Item item = new Item();
            item.setId(itemId);

            // When
            Mockito.when(itemRepository.findById(itemId))
                    .thenReturn(Optional.of(item));
            Mockito.when(codigoAssociadoRepository.findById(codigoId))
                    .thenReturn(Optional.empty());

            // Then
            Assertions.assertThrows(
                    EntidadeNaoEncontradaException.class,
                    () -> itemService.adicionarCodigoAssociado(itemId, codigoId)
            );
            Mockito.verify(itemRepository, Mockito.never())
                    .save(Mockito.any());
        }
    }

    @Nested
    @DisplayName("Cenários do método removerCodigoAssociado()")
    class RemoverCodigoAssociadoTests {

        // Cenário feliz
        @Test
        @DisplayName("Deve remover código associado")
        void deveRemoverCodigoAssociado() {
            // Given
            Integer itemId = 1;
            Integer codigoId = 10;

            CodigoAssociado codigo = new CodigoAssociado();
            codigo.setId(codigoId);

            Item item = new Item();
            item.setId(itemId);
            item.setCodigosAssociados(
                    new ArrayList<>(List.of(codigo))
            );

            // When
            Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
            Mockito.when(itemRepository.save(item)).thenReturn(item);

            Item resultado = itemService.removerCodigoAssociado(itemId, codigoId);

            // Then
            Assertions.assertTrue(resultado.getCodigosAssociados().isEmpty());
            Mockito.verify(itemRepository).save(item);
        }

        // Cenário triste
        @Test
        @DisplayName("Deve lançar exception quando item não existir")
        void deveLancarExceptionQuandoItemNaoExistir() {
            // Given
            Integer itemId = 1;
            Integer codigoId = 10;

            // When
            Mockito.when(itemRepository.findById(itemId))
                    .thenReturn(Optional.empty());

            // Then
            Assertions.assertThrows(
                    EntidadeNaoEncontradaException.class,
                    () -> itemService.removerCodigoAssociado(itemId, codigoId)
            );
        }
    }

}