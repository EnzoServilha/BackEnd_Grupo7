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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import sptech.school.config.GerenciadorTokenJwt;
import sptech.school.dto.usuario.UsuarioResponseDto;
import sptech.school.entity.Permissao;
import sptech.school.entity.Usuario;
import sptech.school.exception.UsuarioNaoEncontradoException;
import sptech.school.mapper.UsuarioMapper;
import sptech.school.repository.PermissaoRepository;
import sptech.school.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PermissaoRepository permissaoRepository;


    @Nested
    @DisplayName("Testes da Função buscarPorId")
    class autenticarTests {

        @Test
        @DisplayName("Autenticar com sucesso")
        void buscarPorIdCorretamente() {
            Long id = 1L;

            Usuario usuario = new Usuario();
            usuario.setId(id);
            usuario.setEmail("fulano@gmail.com");
            usuario.setNome("Fulano de Tal");

            Mockito.when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

            UsuarioResponseDto responseDto = UsuarioMapper.toResponseDto(usuario);

            UsuarioResponseDto retorno = usuarioService.buscarPorId(id);

            Assertions.assertEquals(responseDto,retorno);
        }

        @Test
        @DisplayName("Buscar por ID inexistente")
        void buscarPorIdInexistente() {
            Long id = 1L;

            Mockito.when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

            Assertions.assertThrows(
                    UsuarioNaoEncontradoException.class,
                    () -> usuarioService.buscarPorId(id)
            );
        }
    }

    @Nested
    @DisplayName("Testes da Função listarTodos")
    class listarTodosTests {

        @Test
        @DisplayName("Lista todos corretamente")
        void listarTodosCorretamente (){

            Usuario usuario01 = new Usuario();
            usuario01.setNome("Fulano de Tal");
            usuario01.setEmail("a@gmail.com");

            Usuario usuario02 = new Usuario();
            usuario02.setNome("Beltrano de Tal");
            usuario02.setEmail("b@gmail.com");

            List<Usuario> lista = new ArrayList<>();
            lista.add(usuario01);
            lista.add(usuario02);

            Mockito.when(usuarioRepository.findAll()).thenReturn(lista);

            List<UsuarioResponseDto> responseDtoList = UsuarioMapper.toResponseDtoList(lista);

            List<UsuarioResponseDto> retorno = usuarioService.listarTodos();

            Assertions.assertIterableEquals(responseDtoList,retorno);

        }

    }

    @Nested
    @DisplayName("Testes da Função atualizarPermissao")
    class buscarUsuarioLogadoTests {
        @Test
        @DisplayName("Atualizar usuário corretamente")
        void atualizarPermissaoCorretamente () {

            Long idUsuario = 1L;
            Integer idPermissao = 1;

            Usuario usuario = new Usuario();
            usuario.setId(1L);
            usuario.setNome("Fulano de Tal");
            usuario.setEmail("b@gmail.com");

            Permissao permissao = new Permissao();
            permissao.setId(1);
            permissao.setNome("ROLE_ADMIN");


            Mockito.when(usuarioRepository.findById(idUsuario))
                    .thenReturn(Optional.of(usuario));

            Mockito.when(permissaoRepository.findById(idPermissao))
                    .thenReturn(Optional.of(permissao));

            usuario.setPermissao(permissao);

            Mockito.when(usuarioRepository.save(usuario)).thenReturn(usuario);

            usuarioService.atualizarPermissao(idUsuario, idPermissao);

             UsuarioResponseDto retorno = usuarioService.buscarPorId(idUsuario);

            Assertions.assertEquals(UsuarioMapper.toResponseDto(usuario),retorno);
        }

        @Test
        @DisplayName("Throw erro usuario não encpntrado")
        void atualizarPermissaoComUsuarioNaoExistente () {

            Long idUsuario = 1L;


            Mockito.when(usuarioRepository.findById(idUsuario))
                    .thenReturn(Optional.empty());


            Assertions.assertThrows(
                    UsuarioNaoEncontradoException.class,
                    () -> usuarioService.atualizarPermissao(idUsuario, 1)
            );
        }
    }
}