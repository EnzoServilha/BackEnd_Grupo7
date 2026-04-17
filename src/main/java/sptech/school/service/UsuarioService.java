package sptech.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sptech.school.config.GerenciadorTokenJwt;

import sptech.school.dto.usuario.UsuarioCriacaoDto;
import sptech.school.dto.usuario.UsuarioListarDto;

import sptech.school.exception.UsuarioNaoEncontradoException;
import sptech.school.mapper.UsuarioMapper;

import sptech.school.dto.usuario.UsuarioTokenDto;

import sptech.school.entity.Usuario;
import sptech.school.repository.UsuarioRepository;

import java.util.List;

@Service
public class UsuarioService {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private GerenciadorTokenJwt gerenciadorTokenJwt;

  @Autowired
  private AuthenticationManager authenticationManager;

  public void criar(Usuario novoUsuario) {

    String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
    novoUsuario.setSenha(senhaCriptografada);

    this.usuarioRepository.save(novoUsuario);
  }

  public UsuarioTokenDto autenticar(Usuario usuario) {

    final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
          usuario.getEmail(), usuario.getSenha());

    final Authentication authentication = this.authenticationManager.authenticate(credentials);

    Usuario usuarioAutenticado =
            usuarioRepository.findByEmail(usuario.getEmail())
                    .orElseThrow(
                            () -> new ResponseStatusException(404, "Email do usuário não cadastrado", null)
                    );

    SecurityContextHolder.getContext().setAuthentication(authentication);

    final String token = gerenciadorTokenJwt.generateToken(authentication);

    return UsuarioMapper.of(usuarioAutenticado, token);
  }

  public List<UsuarioListarDto> listarTodos() {

    List<Usuario> usuariosEncontrados = usuarioRepository.findAll();
    return usuariosEncontrados.stream().map(UsuarioMapper::of).toList();

  }

  public UsuarioListarDto buscarPorId(Long id) {
    Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
    return UsuarioMapper.of(usuario);
  }

  public void atualizar(Long id, UsuarioCriacaoDto dto) {
    Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
    usuario.setNome(dto.getNome());
    usuario.setEmail(dto.getEmail());
    usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
    usuarioRepository.save(usuario);
  }

  public void deletar(Long id) {
    if (!usuarioRepository.existsById(id)) {
      throw new UsuarioNaoEncontradoException("Usuário não encontrado");
    }
    usuarioRepository.deleteById(id);
  }

}