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

import sptech.school.dto.usuario.*;

import sptech.school.entity.Permissao;
import sptech.school.exception.PermissaoNaoEncontradaException;
import sptech.school.exception.SenhaInvalidaException;
import sptech.school.exception.UsuarioNaoEncontradoException;
import sptech.school.mapper.UsuarioMapper;

import sptech.school.entity.Usuario;
import sptech.school.repository.PermissaoRepository;
import sptech.school.repository.UsuarioRepository;

import java.util.List;

@Service
public class UsuarioService {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private GerenciadorTokenJwt gerenciadorTokenJwt;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private PermissaoRepository permissaoRepository;

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

  public List<UsuarioResponseDto> listarTodos() {

    List<Usuario> usuariosEncontrados = usuarioRepository.findAll();
    return UsuarioMapper.toResponseDtoList(usuariosEncontrados);

  }

  public UsuarioResponseDto buscarPorId(Long id) {
    Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
    return UsuarioMapper.toResponseDto(usuario);
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

  public void atualizarPermissao(Long id, Integer permissaoId) {
    Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

    Permissao permissao = permissaoRepository.findById(permissaoId)
            .orElseThrow(() -> new PermissaoNaoEncontradaException("Permissão não encontrada"));

    usuario.setPermissao(permissao);

    usuarioRepository.save(usuario);
  }

  public void alterarSenha(Long id, UsuarioSenhaDto dto) {
    Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

    if (!passwordEncoder.matches(dto.getSenhaAtual(), usuario.getSenha())) {
      throw new SenhaInvalidaException("Usuario ou senha invalidos"); //no caso senha
    }

    usuario.setSenha(passwordEncoder.encode(dto.getNovaSenha()));
    usuarioRepository.save(usuario);
  }

  public UsuarioResponseDto buscarUsuarioLogado() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
    return UsuarioMapper.toResponseDto(usuario);
  }
}