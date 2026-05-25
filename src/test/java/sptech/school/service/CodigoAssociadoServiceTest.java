package sptech.school.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import sptech.school.repository.ClienteRepository;
import sptech.school.repository.CodigoAssociadoRepository;
import sptech.school.repository.FornecedorRepository;

import static org.junit.jupiter.api.Assertions.*;

class CodigoAssociadoServiceTest {

    @Mock
    private CodigoAssociadoRepository codigoAssociadoRepository;
    @Mock
    private FornecedorRepository fornecedorRepository;
    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private CodigoAssociadoService codigoAssociadoService;

    @Nested
    @DisplayName("Cenários do método listarTodos()")
    class ListarTodosTests {

        @Test
        @DisplayName("Aaaaaa")
        void aaaaaaBbbbbbCccccc() {
        }

    }

    @Nested
    @DisplayName("Cenários do método buscarPorId()")
    class BuscarPorIdTests {

        @Test
        @DisplayName("Aaaaaa")
        void aaaaaaBbbbbbCccccc() {
        }

    }

    @Nested
    @DisplayName("Cenários do método pesquisarPorCodigo()")
    class PesquisaPorCodigoTests {

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
    @DisplayName("Cenários do método preencher()")
    class PreencherTests {

        @Test
        @DisplayName("Aaaaaa")
        void aaaaaaBbbbbbCccccc() {
        }

    }
}