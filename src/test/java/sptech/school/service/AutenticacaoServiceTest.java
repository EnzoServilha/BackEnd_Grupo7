package sptech.school.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import sptech.school.entity.Usuario;
import sptech.school.repository.UsuarioRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AutenticacaoService autenticacaoService;

    @Test
    void deveCarregarSomenteUsuarioAtivo() {
        Usuario usuario = new Usuario();
        usuario.setEmail("ativo@teste.com");
        usuario.setSenha("senha");
        when(usuarioRepository.findByEmailAndAtivoTrue("ativo@teste.com"))
                .thenReturn(Optional.of(usuario));

        assertTrue(autenticacaoService.loadUserByUsername("ativo@teste.com").isEnabled());
        verify(usuarioRepository).findByEmailAndAtivoTrue("ativo@teste.com");
    }

    @Test
    void deveRejeitarUsuarioInativoOuInexistente() {
        when(usuarioRepository.findByEmailAndAtivoTrue("inativo@teste.com"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> autenticacaoService.loadUserByUsername("inativo@teste.com"));
    }
}