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
import sptech.school.dto.codigoAssociado.CodigoAssociadoRequestDto;
import sptech.school.dto.codigoAssociado.CodigoAssociadoResponseDto;
import sptech.school.entity.Cliente;
import sptech.school.entity.CodigoAssociado;
import sptech.school.entity.Fornecedor;
import sptech.school.entity.Item;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.repository.ClienteRepository;
import sptech.school.repository.CodigoAssociadoRepository;
import sptech.school.repository.FornecedorRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
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

        // Cenário feliz
        @Test
        @DisplayName("Deve retornar todos os códigos associados")
        void deveRetornarTodosOsCodigosAssociados() {
            // Given
            CodigoAssociado codigo = new CodigoAssociado();
            codigo.setId(1);

            // When
            Mockito.when(codigoAssociadoRepository.findAll())
                    .thenReturn(List.of(codigo));
            List<CodigoAssociado> resultado = codigoAssociadoService.listarTodos();

            // Then
            Assertions.assertEquals(1, resultado.size());
            Assertions.assertEquals(1, resultado.get(0).getId());
        }

        // Cenário triste
        @Test
        @DisplayName("Deve retornar lista vazia")
        void deveRetornarListaVazia() {
            // Given
            Mockito.when(codigoAssociadoRepository.findAll())
                    .thenReturn(Collections.emptyList());

            // When
            List<CodigoAssociado> resultado = codigoAssociadoService.listarTodos();

            // Then
            Assertions.assertTrue(resultado.isEmpty());
        }
    }

    @Nested
    @DisplayName("Cenários do método buscarPorId()")
    class BuscarPorIdTests {

        // Cenário feliz
        @Test
        @DisplayName("Deve retornar código associado encontrado")
        void deveRetornarCodigoAssociadoEncontrado() {
            // Given
            Integer id = 1;

            CodigoAssociado codigo = new CodigoAssociado();
            codigo.setId(id);

            // When
            Mockito.when(codigoAssociadoRepository.findById(id))
                    .thenReturn(Optional.of(codigo));
            CodigoAssociado resultado = codigoAssociadoService.buscarPorId(id);

            // Then
            Assertions.assertEquals(id, resultado.getId());
        }

        // Cenário triste
        @Test
        @DisplayName("Deve lançar exception quando id não encontrado")
        void deveLancarExceptionQuandoIdNaoEncontrado() {
            // Given
            Integer id = 1;
            Optional<CodigoAssociado> optional = Optional.empty();

            // When
            Mockito.when(codigoAssociadoRepository.findById(id))
                    .thenReturn(optional);

            // Then
            EntidadeNaoEncontradaException exception =
                    Assertions.assertThrows(
                            EntidadeNaoEncontradaException.class,
                            () -> codigoAssociadoService.buscarPorId(id)
                    );
            Assertions.assertEquals("Código Associado com identificador '1' não encontrado(a)",
                    exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Cenários do método pesquisarPorCodigo()")
    class PesquisaPorCodigoTests {

        // Cenário feliz
        @Test
        @DisplayName("Deve retornar códigos encontrados")
        void deveRetornarCodigosEncontrados() {
            // Given
            String codigoPesquisado = "ABC";

            CodigoAssociado codigo = new CodigoAssociado();
            codigo.setCodigo("ABC123");

            // When
            Mockito.when(codigoAssociadoRepository.findByCodigoContainingIgnoreCase(codigoPesquisado))
                    .thenReturn(List.of(codigo));
            List<CodigoAssociado> resultado = codigoAssociadoService.pesquisarPorCodigo(codigoPesquisado);

            // Then
            Assertions.assertEquals(1, resultado.size());
        }

        // Cenário triste
        @Test
        @DisplayName("Deve retornar lista vazia")
        void deveRetornarListaVazia() {
            // Given
            String codigo = "XYZ";

            // When
            Mockito.when(codigoAssociadoRepository.findByCodigoContainingIgnoreCase(codigo))
                    .thenReturn(Collections.emptyList());
            List<CodigoAssociado> resultado = codigoAssociadoService.pesquisarPorCodigo(codigo);

            // Then
            Assertions.assertTrue(resultado.isEmpty());
        }
    }

    @Nested
    @DisplayName("Cenários do método cadastrar()")
    class CadastrarTests {

        // Cenário feliz
        @Test
        @DisplayName("Deve cadastrar código associado")
        void deveCadastrarCodigoAssociado() {
            // Given
            CodigoAssociadoRequestDto dto = new CodigoAssociadoRequestDto(
                    "ABC123",
                    null,
                    null
            );

            CodigoAssociado entidadeSalva = new CodigoAssociado();
            entidadeSalva.setId(1);
            entidadeSalva.setCodigo("ABC123");

            // When
            Mockito.when(codigoAssociadoRepository.save(Mockito.any(CodigoAssociado.class)))
                    .thenReturn(entidadeSalva);
            CodigoAssociadoResponseDto resultado = codigoAssociadoService.cadastrar(dto);

            // Then
            Assertions.assertNotNull(resultado);
            Mockito.verify(codigoAssociadoRepository)
                    .save(Mockito.any(CodigoAssociado.class));
        }

        // Cenário triste
        @Test
        @DisplayName("Deve lançar exception quando fornecedor não existe")
        void deveLancarExceptionQuandoFornecedorNaoExiste() {
            // Given
            CodigoAssociadoRequestDto dto = new CodigoAssociadoRequestDto(
                    "ABC123",
                    1,
                    null
            );

            // when
            Mockito.when(fornecedorRepository.findById(1))
                    .thenReturn(Optional.empty());

            // then
            Assertions.assertThrows(
                    EntidadeNaoEncontradaException.class,
                    () -> codigoAssociadoService.cadastrar(dto)
            );
        }
    }

    @Nested
    @DisplayName("Cenários do método atualizar()")
    class AtualizarTests {

        // Cenário feliz
        @Test
        @DisplayName("Deve atualizar código associado")
        void deveAtualizarCodigoAssociado() {
            // Given
            Integer id = 1;

            CodigoAssociadoRequestDto dto = new CodigoAssociadoRequestDto(
                    "NOVO",
                    null,
                    null
            );

            CodigoAssociado salvo = new CodigoAssociado();
            salvo.setId(id);
            salvo.setCodigo("NOVO");

            // When
            Mockito.when(codigoAssociadoRepository.existsById(id))
                    .thenReturn(true);
            Mockito.when(codigoAssociadoRepository.save(Mockito.any(CodigoAssociado.class)))
                    .thenReturn(salvo);
            CodigoAssociadoResponseDto resultado = codigoAssociadoService.atualizar(id, dto);

            // Then
            Assertions.assertNotNull(resultado);
        }

        // Cenário triste
        @Test
        @DisplayName("Deve lançar exception quando id não existe")
        void deveLancarExceptionQuandoIdNaoExiste() {
            // Given
            Integer id = 1;

            CodigoAssociadoRequestDto dto = new CodigoAssociadoRequestDto(
                    "NOVO",
                    null,
                    null
            );

            // When
            Mockito.when(codigoAssociadoRepository.existsById(id))
                    .thenReturn(false);

            // Then
            Assertions.assertThrows(
                    EntidadeNaoEncontradaException.class,
                    () -> codigoAssociadoService.atualizar(id, dto)
            );
        }
    }

    @Nested
    @DisplayName("Cenários do método deletar()")
    class DeletarTests {

        // Cenário feliz
        @Test
        @DisplayName("Deve deletar código associado")
        void deveDeletarCodigoAssociado() {
            // Given
            Integer id = 1;

            // When
            Mockito.when(codigoAssociadoRepository.existsById(id))
                    .thenReturn(true);

            // Then
            Assertions.assertDoesNotThrow(() -> codigoAssociadoService.deletar(id));
            Mockito.verify(codigoAssociadoRepository).deleteById(id);
        }

        // Cenário triste
        @Test
        @DisplayName("Deve lançar exception quando id não existe")
        void deveLancarExceptionQuandoIdNaoExiste() {
            // Given
            Integer id = 1;

            // When
            Mockito.when(codigoAssociadoRepository.existsById(id))
                    .thenReturn(false);

            // Then
            Assertions.assertThrows(
                    EntidadeNaoEncontradaException.class,
                    () -> codigoAssociadoService.deletar(id)
            );
        }
    }

    @Nested
    @DisplayName("Cenários do método preencher()")
    class PreencherTests {

        // Cenário feliz
        @Test
        @DisplayName("Deve preencher fornecedor e cliente")
        void devePreencherFornecedorECliente() {
            // Given
            Fornecedor fornecedor = new Fornecedor();
            fornecedor.setId(1);

            Cliente cliente = new Cliente();
            cliente.setId(2);

            CodigoAssociadoRequestDto dto = new CodigoAssociadoRequestDto(
                    "ABC123",
                    1,
                    2
            );
            CodigoAssociado entity = new CodigoAssociado();

            // when
            Mockito.when(fornecedorRepository.findById(1))
                    .thenReturn(Optional.of(fornecedor));
            Mockito.when(clienteRepository.findById(2))
                    .thenReturn(Optional.of(cliente));
            codigoAssociadoService.preencher(dto, entity);

            // Then
            Assertions.assertEquals(fornecedor, entity.getFornecedor());
            Assertions.assertEquals(cliente, entity.getCliente());
        }

        // Cenário triste
        @Test
        @DisplayName("Deve lançar exception quando fornecedor não existe")
        void deveLancarExceptionQuandoFornecedorNaoExiste() {
            // Given
            CodigoAssociadoRequestDto dto = new CodigoAssociadoRequestDto(
                    "ABC123",
                    1,
                    null
            );
            CodigoAssociado entity = new CodigoAssociado();

            // When
            Mockito.when(fornecedorRepository.findById(1))
                    .thenReturn(Optional.empty());

            // Then
            Assertions.assertThrows(
                    EntidadeNaoEncontradaException.class,
                    () -> codigoAssociadoService.preencher(dto, entity)
            );
        }
    }
}