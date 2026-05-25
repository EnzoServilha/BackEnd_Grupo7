package sptech.school.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import sptech.school.repository.ItemRepository;

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

        @Test
        @DisplayName("Aaaaaa")
        void aaaaaaBbbbbbCccccc() {
        }

    }

    @Nested
    @DisplayName("Cenários do método adicionarSimilar()")
    class AdicionarSimilarTests {

        @Test
        @DisplayName("Aaaaaa")
        void aaaaaaBbbbbbCccccc() {
        }

    }

    @Nested
    @DisplayName("Cenários do método removerSimilar()")
    class RemoverSimilarTests {

        @Test
        @DisplayName("Aaaaaa")
        void aaaaaaBbbbbbCccccc() {
        }

    }
}