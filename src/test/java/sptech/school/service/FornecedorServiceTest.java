package sptech.school.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.dto.fornecedor.FornecedorRequestDto;
import sptech.school.dto.fornecedor.FornecedorResponseDto;
import sptech.school.entity.Categoria;
import sptech.school.entity.Endereco;
import sptech.school.entity.Fornecedor;
import sptech.school.entity.Marca;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.repository.CategoriaRepository;
import sptech.school.repository.EnderecoRepository;
import sptech.school.repository.FornecedorRepository;
import sptech.school.repository.MarcaRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FornecedorService")
class FornecedorServiceTest {

    @Mock
    private FornecedorRepository repository;

    @Mock
    private MarcaRepository marcaRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
    private FornecedorService fornecedorService;


    private Fornecedor criarFornecedor(Integer id, String nomeEmpresa, String nomeContato) {
        Fornecedor f = new Fornecedor();
        f.setId(id);
        f.setNomeEmpresa(nomeEmpresa);
        f.setNomeContato(nomeContato);
        return f;
    }

    private FornecedorRequestDto criarRequest(String nomeEmpresa, String nomeContato) {
        FornecedorRequestDto dto = new FornecedorRequestDto();
        dto.setNomeEmpresa(nomeEmpresa);
        dto.setNomeContato(nomeContato);
        dto.setMarcaId(null);
        dto.setCategoriaId(null);
        dto.setEnderecoId(null);
        return dto;
    }


    @Nested
    @DisplayName("listar()")
    class ListarTest {

        @Test
        @DisplayName("Deve retornar lista com fornecedores quando houver registros")
        void listarComResultados() {
            Fornecedor f1 = criarFornecedor(1, "Empresa A", "João");
            Fornecedor f2 = criarFornecedor(2, "Empresa B", "Maria");

            when(repository.findAll()).thenReturn(List.of(f1, f2));

            List<FornecedorResponseDto> resultado = fornecedorService.listar();

            assertNotNull(resultado);
            assertEquals(2, resultado.size());

            Mockito.verify(repository).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não houver fornecedores")
        void listarVazio() {
            when(repository.findAll()).thenReturn(List.of());

            List<FornecedorResponseDto> resultado = fornecedorService.listar();

            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());

            Mockito.verify(repository).findAll();
        }
    }



    @Nested
    @DisplayName("buscarPorId()")
    class BuscarPorIdTest {

        @Test
        @DisplayName("Deve retornar fornecedor quando ID existir")
        void buscarPorIdComSucesso() {
            Fornecedor fornecedor = criarFornecedor(1, "Empresa A", "João");

            when(repository.findById(1)).thenReturn(Optional.of(fornecedor));

            FornecedorResponseDto resultado = fornecedorService.buscarPorId(1);

            assertNotNull(resultado);

            Mockito.verify(repository).findById(1);
        }

        @Test
        @DisplayName("Deve lançar EntidadeNaoEncontradaException quando ID não existir")
        void buscarPorIdNaoEncontrado() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            assertThrows(EntidadeNaoEncontradaException.class, () ->
                    fornecedorService.buscarPorId(99));

            Mockito.verify(repository).findById(99);
        }
    }


    @Nested
    @DisplayName("buscarPorNomeContato()")
    class BuscarPorNomeContatoTest {

        @Test
        @DisplayName("Deve retornar lista de fornecedores pelo nome do contato")
        void buscarPorNomeContatoComResultados() {
            Fornecedor f = criarFornecedor(1, "Empresa A", "João");

            when(repository.findByNomeContatoContaining("João")).thenReturn(List.of(f));

            List<FornecedorResponseDto> resultado = fornecedorService.buscarPorNomeContato("João");

            assertNotNull(resultado);
            assertEquals(1, resultado.size());

            Mockito.verify(repository).findByNomeContatoContaining("João");
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando nenhum contato for encontrado")
        void buscarPorNomeContatoVazio() {
            when(repository.findByNomeContatoContaining("Inexistente")).thenReturn(List.of());

            List<FornecedorResponseDto> resultado = fornecedorService.buscarPorNomeContato("Inexistente");

            assertTrue(resultado.isEmpty());

            Mockito.verify(repository).findByNomeContatoContaining("Inexistente");
        }
    }


    @Nested
    @DisplayName("buscarPorNomeEmpresa()")
    class BuscarPorNomeEmpresaTest {

        @Test
        @DisplayName("Deve retornar lista de fornecedores pelo nome da empresa")
        void buscarPorNomeEmpresaComResultados() {
            Fornecedor f = criarFornecedor(1, "Empresa A", "João");

            when(repository.findByNomeEmpresaContaining("Empresa")).thenReturn(List.of(f));

            List<FornecedorResponseDto> resultado = fornecedorService.buscarPorNomeEmpresa("Empresa");

            assertNotNull(resultado);
            assertEquals(1, resultado.size());

            Mockito.verify(repository).findByNomeEmpresaContaining("Empresa");
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando nenhuma empresa for encontrada")
        void buscarPorNomeEmpresaVazio() {
            when(repository.findByNomeEmpresaContaining("XYZ")).thenReturn(List.of());

            List<FornecedorResponseDto> resultado = fornecedorService.buscarPorNomeEmpresa("XYZ");

            assertTrue(resultado.isEmpty());

            Mockito.verify(repository).findByNomeEmpresaContaining("XYZ");
        }
    }

    @Nested
    @DisplayName("listarPorCategoria()")
    class ListarPorCategoriaTest {

        @Test
        @DisplayName("Deve retornar fornecedores da categoria informada")
        void listarPorCategoriaComResultados() {
            Fornecedor f = criarFornecedor(1, "Empresa A", "João");

            when(repository.findByIdCategoria(10)).thenReturn(List.of(f));

            List<FornecedorResponseDto> resultado = fornecedorService.listarPorCategoria(10);

            assertNotNull(resultado);
            assertEquals(1, resultado.size());

            Mockito.verify(repository).findByIdCategoria(10);
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não houver fornecedores na categoria")
        void listarPorCategoriaVazio() {
            when(repository.findByIdCategoria(99)).thenReturn(List.of());

            List<FornecedorResponseDto> resultado = fornecedorService.listarPorCategoria(99);

            assertTrue(resultado.isEmpty());

            Mockito.verify(repository).findByIdCategoria(99);
        }
    }



    @Nested
    @DisplayName("listarPorMarca()")
    class ListarPorMarcaTest {

        @Test
        @DisplayName("Deve retornar fornecedores da marca informada")
        void listarPorMarcaComResultados() {
            Fornecedor f = criarFornecedor(1, "Empresa A", "João");

            when(repository.findByIdMarca(5)).thenReturn(List.of(f));

            List<FornecedorResponseDto> resultado = fornecedorService.listarPorMarca(5);

            assertNotNull(resultado);
            assertEquals(1, resultado.size());

            Mockito.verify(repository).findByIdMarca(5);
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não houver fornecedores da marca")
        void listarPorMarcaVazio() {
            when(repository.findByIdMarca(99)).thenReturn(List.of());

            List<FornecedorResponseDto> resultado = fornecedorService.listarPorMarca(99);

            assertTrue(resultado.isEmpty());

            Mockito.verify(repository).findByIdMarca(99);
        }
    }

    @Nested
    @DisplayName("criar()")
    class CriarTest {

        @Test
        @DisplayName("Deve criar fornecedor sem marcas, categorias e endereço")
        void criarSemRelacionamentos() {
            FornecedorRequestDto request = criarRequest("Empresa Nova", "Pedro");

            Fornecedor salvo = criarFornecedor(1, "Empresa Nova", "Pedro");

            when(repository.save(Mockito.any(Fornecedor.class))).thenReturn(salvo);

            FornecedorResponseDto resultado = fornecedorService.criar(request);

            assertNotNull(resultado);

            Mockito.verify(repository).save(Mockito.any(Fornecedor.class));
        }

        @Test
        @DisplayName("Deve criar fornecedor com marcas válidas")
        void criarComMarcas() {
            FornecedorRequestDto request = criarRequest("Empresa Nova", "Pedro");
            request.setMarcaId(List.of(1, 2));

            Marca marca1 = new Marca();
            marca1.setId(1);
            Marca marca2 = new Marca();
            marca2.setId(2);

            Fornecedor salvo = criarFornecedor(1, "Empresa Nova", "Pedro");

            when(marcaRepository.findAllById(List.of(1, 2))).thenReturn(List.of(marca1, marca2));
            when(repository.save(Mockito.any(Fornecedor.class))).thenReturn(salvo);

            FornecedorResponseDto resultado = fornecedorService.criar(request);

            assertNotNull(resultado);

            Mockito.verify(marcaRepository).findAllById(List.of(1, 2));
            Mockito.verify(repository).save(Mockito.any(Fornecedor.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando nenhuma marca for encontrada pelos IDs informados")
        void criarComMarcasNaoEncontradas() {
            FornecedorRequestDto request = criarRequest("Empresa Nova", "Pedro");
            request.setMarcaId(List.of(99, 100));

            when(marcaRepository.findAllById(List.of(99, 100))).thenReturn(List.of());

            assertThrows(EntidadeNaoEncontradaException.class, () ->
                    fornecedorService.criar(request));

            Mockito.verify(marcaRepository).findAllById(List.of(99, 100));
            Mockito.verify(repository, never()).save(Mockito.any());
        }

        @Test
        @DisplayName("Deve criar fornecedor com categorias válidas")
        void criarComCategorias() {
            FornecedorRequestDto request = criarRequest("Empresa Nova", "Pedro");
            request.setCategoriaId(List.of(3));

            Categoria cat = new Categoria();
            cat.setId(3);

            Fornecedor salvo = criarFornecedor(1, "Empresa Nova", "Pedro");

            when(categoriaRepository.findAllById(List.of(3))).thenReturn(List.of(cat));
            when(repository.save(Mockito.any(Fornecedor.class))).thenReturn(salvo);

            FornecedorResponseDto resultado = fornecedorService.criar(request);

            assertNotNull(resultado);

            Mockito.verify(categoriaRepository).findAllById(List.of(3));
            Mockito.verify(repository).save(Mockito.any(Fornecedor.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando nenhuma categoria for encontrada pelos IDs informados")
        void criarComCategoriasNaoEncontradas() {
            FornecedorRequestDto request = criarRequest("Empresa Nova", "Pedro");
            request.setCategoriaId(List.of(99));

            when(categoriaRepository.findAllById(List.of(99))).thenReturn(List.of());

            assertThrows(EntidadeNaoEncontradaException.class, () ->
                    fornecedorService.criar(request));

            Mockito.verify(categoriaRepository).findAllById(List.of(99));
            Mockito.verify(repository, never()).save(Mockito.any());
        }

        @Test
        @DisplayName("Deve criar fornecedor com endereço válido")
        void criarComEndereco() {
            FornecedorRequestDto request = criarRequest("Empresa Nova", "Pedro");
            request.setEnderecoId(7);

            Endereco endereco = new Endereco();
            endereco.setId(7);

            Fornecedor salvo = criarFornecedor(1, "Empresa Nova", "Pedro");

            when(enderecoRepository.findById(7)).thenReturn(Optional.of(endereco));
            when(repository.save(Mockito.any(Fornecedor.class))).thenReturn(salvo);

            FornecedorResponseDto resultado = fornecedorService.criar(request);

            assertNotNull(resultado);

            Mockito.verify(enderecoRepository).findById(7);
            Mockito.verify(repository).save(Mockito.any(Fornecedor.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando endereço não for encontrado pelo ID informado")
        void criarComEnderecoNaoEncontrado() {
            FornecedorRequestDto request = criarRequest("Empresa Nova", "Pedro");
            request.setEnderecoId(99);

            when(enderecoRepository.findById(99)).thenReturn(Optional.empty());

            assertThrows(EntidadeNaoEncontradaException.class, () ->
                    fornecedorService.criar(request));

            Mockito.verify(enderecoRepository).findById(99);
            Mockito.verify(repository, never()).save(Mockito.any());
        }

        @Test
        @DisplayName("Deve criar fornecedor com marcas, categorias e endereço ao mesmo tempo")
        void criarComTodosRelacionamentos() {
            FornecedorRequestDto request = criarRequest("Empresa Completa", "Ana");
            request.setMarcaId(List.of(1));
            request.setCategoriaId(List.of(2));
            request.setEnderecoId(3);

            Marca marca = new Marca();
            marca.setId(1);

            Categoria cat = new Categoria();
            cat.setId(2);

            Endereco endereco = new Endereco();
            endereco.setId(3);

            Fornecedor salvo = criarFornecedor(10, "Empresa Completa", "Ana");

            when(marcaRepository.findAllById(List.of(1))).thenReturn(List.of(marca));
            when(categoriaRepository.findAllById(List.of(2))).thenReturn(List.of(cat));
            when(enderecoRepository.findById(3)).thenReturn(Optional.of(endereco));
            when(repository.save(Mockito.any(Fornecedor.class))).thenReturn(salvo);

            FornecedorResponseDto resultado = fornecedorService.criar(request);

            assertNotNull(resultado);

            Mockito.verify(marcaRepository).findAllById(List.of(1));
            Mockito.verify(categoriaRepository).findAllById(List.of(2));
            Mockito.verify(enderecoRepository).findById(3);
            Mockito.verify(repository).save(Mockito.any(Fornecedor.class));
        }
    }



    @Nested
    @DisplayName("atualizar()")
    class AtualizarTest {

        @Test
        @DisplayName("Deve atualizar fornecedor com sucesso sem relacionamentos")
        void atualizarComSucesso() {
            FornecedorRequestDto request = criarRequest("Empresa Atualizada", "Carlos");

            Fornecedor salvo = criarFornecedor(1, "Empresa Atualizada", "Carlos");

            when(repository.existsById(1)).thenReturn(true);
            when(repository.save(Mockito.any(Fornecedor.class))).thenReturn(salvo);

            FornecedorResponseDto resultado = fornecedorService.atualizar(request, 1);

            assertNotNull(resultado);

            Mockito.verify(repository).existsById(1);
            Mockito.verify(repository).save(Mockito.any(Fornecedor.class));
        }

        @Test
        @DisplayName("Deve lançar EntidadeNaoEncontradaException quando fornecedor não existir")
        void atualizarNaoEncontrado() {
            FornecedorRequestDto request = criarRequest("Empresa X", "Fulano");

            when(repository.existsById(99)).thenReturn(false);

            assertThrows(EntidadeNaoEncontradaException.class, () ->
                    fornecedorService.atualizar(request, 99));

            Mockito.verify(repository).existsById(99);
            Mockito.verify(repository, never()).save(Mockito.any());
        }

        @Test
        @DisplayName("Deve atualizar fornecedor com marcas válidas")
        void atualizarComMarcas() {
            FornecedorRequestDto request = criarRequest("Empresa B", "Lucas");
            request.setMarcaId(List.of(1));

            Marca marca = new Marca();
            marca.setId(1);

            Fornecedor salvo = criarFornecedor(2, "Empresa B", "Lucas");

            when(repository.existsById(2)).thenReturn(true);
            when(marcaRepository.findAllById(List.of(1))).thenReturn(List.of(marca));
            when(repository.save(Mockito.any(Fornecedor.class))).thenReturn(salvo);

            FornecedorResponseDto resultado = fornecedorService.atualizar(request, 2);

            assertNotNull(resultado);

            Mockito.verify(marcaRepository).findAllById(List.of(1));
            Mockito.verify(repository).save(Mockito.any(Fornecedor.class));
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar com marcas não encontradas")
        void atualizarComMarcasNaoEncontradas() {
            FornecedorRequestDto request = criarRequest("Empresa B", "Lucas");
            request.setMarcaId(List.of(99));

            when(repository.existsById(2)).thenReturn(true);
            when(marcaRepository.findAllById(List.of(99))).thenReturn(List.of());

            assertThrows(EntidadeNaoEncontradaException.class, () ->
                    fornecedorService.atualizar(request, 2));

            Mockito.verify(repository, never()).save(Mockito.any());
        }

        @Test
        @DisplayName("Deve atualizar fornecedor com categorias válidas")
        void atualizarComCategorias() {
            FornecedorRequestDto request = criarRequest("Empresa C", "Bianca");
            request.setCategoriaId(List.of(4));

            Categoria cat = new Categoria();
            cat.setId(4);

            Fornecedor salvo = criarFornecedor(3, "Empresa C", "Bianca");

            when(repository.existsById(3)).thenReturn(true);
            when(categoriaRepository.findAllById(List.of(4))).thenReturn(List.of(cat));
            when(repository.save(Mockito.any(Fornecedor.class))).thenReturn(salvo);

            FornecedorResponseDto resultado = fornecedorService.atualizar(request, 3);

            assertNotNull(resultado);

            Mockito.verify(categoriaRepository).findAllById(List.of(4));
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar com categorias não encontradas")
        void atualizarComCategoriasNaoEncontradas() {
            FornecedorRequestDto request = criarRequest("Empresa C", "Bianca");
            request.setCategoriaId(List.of(99));

            when(repository.existsById(3)).thenReturn(true);
            when(categoriaRepository.findAllById(List.of(99))).thenReturn(List.of());

            assertThrows(EntidadeNaoEncontradaException.class, () ->
                    fornecedorService.atualizar(request, 3));

            Mockito.verify(repository, never()).save(Mockito.any());
        }

        @Test
        @DisplayName("Deve atualizar fornecedor com endereço válido")
        void atualizarComEndereco() {
            FornecedorRequestDto request = criarRequest("Empresa D", "Roberto");
            request.setEnderecoId(5);

            Endereco endereco = new Endereco();
            endereco.setId(5);

            Fornecedor salvo = criarFornecedor(4, "Empresa D", "Roberto");

            when(repository.existsById(4)).thenReturn(true);
            when(enderecoRepository.findById(5)).thenReturn(Optional.of(endereco));
            when(repository.save(Mockito.any(Fornecedor.class))).thenReturn(salvo);

            FornecedorResponseDto resultado = fornecedorService.atualizar(request, 4);

            assertNotNull(resultado);

            Mockito.verify(enderecoRepository).findById(5);
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar com endereço não encontrado")
        void atualizarComEnderecoNaoEncontrado() {
            FornecedorRequestDto request = criarRequest("Empresa D", "Roberto");
            request.setEnderecoId(99);

            when(repository.existsById(4)).thenReturn(true);
            when(enderecoRepository.findById(99)).thenReturn(Optional.empty());

            assertThrows(EntidadeNaoEncontradaException.class, () ->
                    fornecedorService.atualizar(request, 4));

            Mockito.verify(repository, never()).save(Mockito.any());
        }
    }


    @Nested
    @DisplayName("deletar()")
    class DeletarTest {

        @Test
        @DisplayName("Deve deletar fornecedor com sucesso")
        void deletarComSucesso() {
            when(repository.existsById(1)).thenReturn(true);

            fornecedorService.deletar(1);

            Mockito.verify(repository).existsById(1);
            Mockito.verify(repository).deleteById(1);
        }

        @Test
        @DisplayName("Deve lançar EntidadeNaoEncontradaException quando fornecedor não existir")
        void deletarNaoEncontrado() {
            when(repository.existsById(99)).thenReturn(false);

            assertThrows(EntidadeNaoEncontradaException.class, () ->
                    fornecedorService.deletar(99));

            Mockito.verify(repository).existsById(99);
            Mockito.verify(repository, never()).deleteById(Mockito.any());
        }
    }


    @Nested
    @DisplayName("preencher() — listas vazias são ignoradas")
    class PreencherTest {

        @Test
        @DisplayName("Deve ignorar marcaId quando lista estiver vazia")
        void preencherMarcaIdVazio() {
            FornecedorRequestDto request = criarRequest("Empresa Z", "Fernanda");
            request.setMarcaId(List.of());

            Fornecedor salvo = criarFornecedor(1, "Empresa Z", "Fernanda");
            when(repository.save(Mockito.any(Fornecedor.class))).thenReturn(salvo);

            fornecedorService.criar(request);

            Mockito.verify(marcaRepository, never()).findAllById(Mockito.any());
        }

        @Test
        @DisplayName("Deve ignorar categoriaId quando lista estiver vazia")
        void preencherCategoriaIdVazio() {
            FornecedorRequestDto request = criarRequest("Empresa Z", "Fernanda");
            request.setCategoriaId(List.of());

            Fornecedor salvo = criarFornecedor(1, "Empresa Z", "Fernanda");
            when(repository.save(Mockito.any(Fornecedor.class))).thenReturn(salvo);

            fornecedorService.criar(request);

            Mockito.verify(categoriaRepository, never()).findAllById(Mockito.any());
        }

        @Test
        @DisplayName("Deve ignorar enderecoId quando for nulo")
        void preencherEnderecoIdNulo() {
            FornecedorRequestDto request = criarRequest("Empresa Z", "Fernanda");
            request.setEnderecoId(null);

            Fornecedor salvo = criarFornecedor(1, "Empresa Z", "Fernanda");
            when(repository.save(Mockito.any(Fornecedor.class))).thenReturn(salvo);

            fornecedorService.criar(request);

            Mockito.verify(enderecoRepository, never()).findById(Mockito.any());
        }
    }
}