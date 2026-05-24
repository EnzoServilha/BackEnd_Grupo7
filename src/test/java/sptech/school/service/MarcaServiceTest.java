package sptech.school.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.dto.marca.MarcaRequestDto;
import sptech.school.dto.marca.MarcaResponseDto;
import sptech.school.entity.Marca;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.repository.MarcaRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MarcaService")
class MarcaServiceTest {

    @Mock
    private MarcaRepository repository;

    @InjectMocks
    private MarcaService marcaService;

    @Nested
    @DisplayName("buscarPorNome()")
    class BuscarPorNomeTest {

        @Test
        @DisplayName("Deve retornar marca quando nome da empresa for encontrado")
        void buscarPorNomeComSucesso() {
            Marca marca = new Marca();
            marca.setId(1);
            marca.setNomeEmpresa("Nike");

            when(repository.findByNomeEmpresaContaining("Nike")).thenReturn(marca);

            MarcaResponseDto resultado = marcaService.buscarPorNome("Nike");

            assertNotNull(resultado);

            Mockito.verify(repository).findByNomeEmpresaContaining("Nike");
        }

        @Test
        @DisplayName("Deve retornar null quando nenhuma marca for encontrada pelo nome")
        void buscarPorNomeNaoEncontrado() {
            when(repository.findByNomeEmpresaContaining("Inexistente")).thenReturn(null);

            MarcaResponseDto resultado = marcaService.buscarPorNome("Inexistente");

            assertNull(resultado);

            Mockito.verify(repository).findByNomeEmpresaContaining("Inexistente");
        }
    }


    @Nested
    @DisplayName("buscarPorId()")
    class BuscarPorIdTest {

        @Test
        @DisplayName("Deve retornar marca quando ID existir")
        void buscarPorIdComSucesso() {
            Marca marca = new Marca();
            marca.setId(1);
            marca.setNomeEmpresa("Nike");

            when(repository.findById(1)).thenReturn(Optional.of(marca));

            MarcaResponseDto resultado = marcaService.buscarPorId(1);

            assertNotNull(resultado);

            Mockito.verify(repository).findById(1);
        }

        @Test
        @DisplayName("Deve lançar EntidadeNaoEncontradaException quando ID não existir")
        void buscarPorIdNaoEncontrado() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            assertThrows(EntidadeNaoEncontradaException.class, () ->
                    marcaService.buscarPorId(99));

            Mockito.verify(repository).findById(99);
        }
    }


    @Nested
    @DisplayName("criar()")
    class CriarTest {

        @Test
        @DisplayName("Deve criar marca com sucesso")
        void criarComSucesso() {
            MarcaRequestDto request = new MarcaRequestDto();

            Marca salva = new Marca();
            salva.setId(1);
            salva.setNomeEmpresa("Adidas");

            when(repository.save(Mockito.any(Marca.class))).thenReturn(salva);

            MarcaResponseDto resultado = marcaService.criar(request);

            assertNotNull(resultado);

            Mockito.verify(repository).save(Mockito.any(Marca.class));
        }
    }


    @Nested
    @DisplayName("deletar()")
    class DeletarTest {

        @Test
        @DisplayName("Deve deletar marca com sucesso")
        void deletarComSucesso() {
            when(repository.existsById(1)).thenReturn(true);

            marcaService.deletar(1);

            Mockito.verify(repository).existsById(1);
            Mockito.verify(repository).deleteById(1);
        }

        @Test
        @DisplayName("Deve lançar EntidadeNaoEncontradaException quando marca não existir")
        void deletarNaoEncontrado() {
            when(repository.existsById(99)).thenReturn(false);

            assertThrows(EntidadeNaoEncontradaException.class, () ->
                    marcaService.deletar(99));

            Mockito.verify(repository).existsById(99);
            Mockito.verify(repository, never()).deleteById(Mockito.any());
        }
    }
}