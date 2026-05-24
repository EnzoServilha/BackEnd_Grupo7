package sptech.school.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.dto.categoria.CategoriaRequestDto;
import sptech.school.dto.categoria.CategoriaResponseDto;
import sptech.school.entity.Categoria;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.repository.CategoriaRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoriaService")
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository repository;

    @InjectMocks
    private CategoriaService categoriaService;


    @Nested
    @DisplayName("buscarPorNome()")
    class BuscarPorNomeTest {

        @Test
        @DisplayName("Deve retornar categoria quando nome for encontrado")
        void buscarPorNomeComSucesso() {
            Categoria categoria = new Categoria();
            categoria.setId(1);
            categoria.setNome("Eletrônicos");

            when(repository.findByNomeContaining("Eletrônicos")).thenReturn(categoria);

            CategoriaResponseDto resultado = categoriaService.buscarPorNome("Eletrônicos");

            assertNotNull(resultado);

            Mockito.verify(repository).findByNomeContaining("Eletrônicos");
        }

        @Test
        @DisplayName("Deve retornar null quando nenhuma categoria for encontrada pelo nome")
        void buscarPorNomeNaoEncontrado() {
            when(repository.findByNomeContaining("Inexistente")).thenReturn(null);

            CategoriaResponseDto resultado = categoriaService.buscarPorNome("Inexistente");

            assertNull(resultado);

            Mockito.verify(repository).findByNomeContaining("Inexistente");
        }
    }

    @Nested
    @DisplayName("buscarPorId()")
    class BuscarPorIdTest {

        @Test
        @DisplayName("Deve retornar categoria quando ID existir")
        void buscarPorIdComSucesso() {
            Categoria categoria = new Categoria();
            categoria.setId(1);
            categoria.setNome("Eletrônicos");

            when(repository.findById(1)).thenReturn(Optional.of(categoria));

            CategoriaResponseDto resultado = categoriaService.buscarPorId(1);

            assertNotNull(resultado);

            Mockito.verify(repository).findById(1);
        }

        @Test
        @DisplayName("Deve lançar EntidadeNaoEncontradaException quando ID não existir")
        void buscarPorIdNaoEncontrado() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            assertThrows(EntidadeNaoEncontradaException.class, () ->
                    categoriaService.buscarPorId(99));

            Mockito.verify(repository).findById(99);
        }
    }

    @Nested
    @DisplayName("criar()")
    class CriarTest {

        @Test
        @DisplayName("Deve criar categoria com sucesso")
        void criarComSucesso() {
            CategoriaRequestDto request = new CategoriaRequestDto("Ferramentas");

            Categoria salva = new Categoria();
            salva.setId(1);
            salva.setNome("Ferramentas");

            when(repository.save(Mockito.any(Categoria.class))).thenReturn(salva);

            CategoriaResponseDto resultado = categoriaService.criar(request);

            assertNotNull(resultado);

            Mockito.verify(repository).save(Mockito.any(Categoria.class));
        }

    }

        @Nested
        @DisplayName("deletar()")
        class DeletarTest {

            @Test
            @DisplayName("Deve deletar categoria com sucesso")
            void deletarComSucesso() {
                when(repository.existsById(1)).thenReturn(true);

                categoriaService.deletar(1);

                Mockito.verify(repository).existsById(1);
                Mockito.verify(repository).deleteById(1);
            }

            @Test
            @DisplayName("Deve lançar EntidadeNaoEncontradaException quando categoria não existir")
            void deletarNaoEncontrado() {
                when(repository.existsById(99)).thenReturn(false);

                assertThrows(EntidadeNaoEncontradaException.class, () ->
                        categoriaService.deletar(99));

                Mockito.verify(repository).existsById(99);
                Mockito.verify(repository, never()).deleteById(Mockito.any());
            }
        }




        //Teste
    }
