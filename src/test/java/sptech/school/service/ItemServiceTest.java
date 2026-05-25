package sptech.school.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import sptech.school.entity.Item;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.repository.CodigoAssociadoRepository;
import sptech.school.repository.ItemRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        }

        // Cenário triste
        @DisplayName("Deve retornar lista vazia")
        void deveRetornarListaVazia() {
            // CENÁRIO: Não há itens cadastrados

            // given
            List<Item> itens = Collections.emptyList();

            // when
            Mockito.when(itemRepository.findAll())
                    .thenReturn(itens);

            // then
            Assertions.assertTrue(itens.isEmpty());

        }

    }

    @Nested
    @DisplayName("Cenários do método buscarPorId()")
    class BuscarPorIdTests {

        @Test
        @DisplayName("Aaaaaa")
        void aaaaaaBbbbbbCccccc() {
        }

        // Cenário triste
        @Test
        @DisplayName("Deve lancar exception quando id não encontrado")
        void deveLancarExceptionQuandoIdNaoEncontrado() {
            // given
            Integer id = 1;
            Optional<Item> optional = Optional.empty();

            // when
            Mockito.when(itemRepository.findById(id))
                    .thenReturn(optional);

            // then
            EntidadeNaoEncontradaException exception =
                    Assertions.assertThrows(
                            EntidadeNaoEncontradaException.class,
                            () -> itemService.buscarPorId(id)
                    );

            Assertions.assertEquals("Item 1",
                    exception.getMessage());
        }

    }


    @Nested
    @DisplayName("Cenários do método buscarPorCodigoInterno()")
    class BuscarPorCodigoInternoTests {

        @Test
        @DisplayName("Aaaaaa")
        void aaaaaaBbbbbbCccccc() {
        }

    }


    @Nested
    @DisplayName("Cenários do método listarPorMarca()")
    class ListarPorMarcaTests {

        @Test
        @DisplayName("Aaaaaa")
        void aaaaaaBbbbbbCccccc() {
        }

    }


    @Nested
    @DisplayName("Cenários do método pesquisar()")
    class pesquisarTests {

        @Test
        @DisplayName("Aaaaaa")
        void aaaaaaBbbbbbCccccc() {
        }

    }


    @Nested
    @DisplayName("Cenários do método buscarPorCodigoAssociado()")
    class BuscarPorCodigoAssociadoTests {

        @Test
        @DisplayName("Aaaaaa")
        void aaaaaaBbbbbbCccccc() {
        }

    }

    @Nested
    @DisplayName("Cenários do método cadastrar()")
    class CadastrarTests {

        @Test
        @DisplayName("Aaaaaa")
        void aaaaaaBbbbbbCccccc() {
        }

    }

    @Nested
    @DisplayName("Cenários do método atualizar()")
    class AtualizarTests {

        @Test
        @DisplayName("Aaaaaa")
        void aaaaaaBbbbbbCccccc() {
        }

    }

    @Nested
    @DisplayName("Cenários do método deletar()")
    class DeletarTests {

        @Test
        @DisplayName("Aaaaaa")
        void aaaaaaBbbbbbCccccc() {
        }

    }

    @Nested
    @DisplayName("Cenários do método adicionarCodigoAssociado()")
    class AdicionarCodigoAssociadoTests {

        @Test
        @DisplayName("Aaaaaa")
        void aaaaaaBbbbbbCccccc() {
        }

    }

    @Nested
    @DisplayName("Cenários do método removerCodigoAssociado()")
    class RemoverCodigoAssociadoTests {

        @Test
        @DisplayName("Aaaaaa")
        void aaaaaaBbbbbbCccccc() {
        }

    }

}