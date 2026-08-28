package sptech.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sptech.school.config.GerenciadorTokenJwt;

import sptech.school.dto.usuario.*;

import sptech.school.entity.Permissao;
import sptech.school.exception.AcessoNegadoexception;
import sptech.school.exception.PermissaoNaoEncontradaException;
import sptech.school.exception.EntidadeConflitanteException;
import sptech.school.exception.SenhaInvalidaException;
import sptech.school.exception.UsuarioNaoEncontradoException;
import sptech.school.mapper.UsuarioMapper;

import sptech.school.entity.Usuario;
import sptech.school.repository.PermissaoRepository;
import sptech.school.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioService.class);

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
    novoUsuario.setDataCadastro(LocalDateTime.now());

    this.usuarioRepository.save(novoUsuario);
  }

  public UsuarioTokenDto autenticar(Usuario usuario) {

    final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
          usuario.getEmail(), usuario.getSenha());

    final Authentication authentication = this.authenticationManager.authenticate(credentials);

    Usuario usuarioAutenticado =
          usuarioRepository.findByEmailAndAtivoTrue(usuario.getEmail())
                    .orElseThrow(
                            () -> new ResponseStatusException(404, "Email do usuário não cadastrado", null)
                    );

    SecurityContextHolder.getContext().setAuthentication(authentication);

    final String token = gerenciadorTokenJwt.generateToken(authentication);

    return UsuarioMapper.of(usuarioAutenticado, token);
  }

  public List<UsuarioResponseDto> listarTodos() {

        List<Usuario> usuariosEncontrados = usuarioRepository.findAll().stream()
          .filter(usuario -> Boolean.TRUE.equals(usuario.getAtivo())).toList();
    return UsuarioMapper.toResponseDtoList(usuariosEncontrados);

  }

  public List<UsuarioResponseDto> listarAdministrativo(String ativo) {
    return UsuarioMapper.toResponseDtoList(FiltroAtivacao.filtrar(usuarioRepository.findAll(), ativo));
  }

  public UsuarioResponseDto buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
          .filter(encontrado -> Boolean.TRUE.equals(encontrado.getAtivo()))
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
    return UsuarioMapper.toResponseDto(usuario);
  }

  public Usuario buscarPorIdIncluindoInativo(Long id) {
    return usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
  }

  public Usuario buscarAtivoPorEmail(String email) {
    return usuarioRepository.findByEmailAndAtivoTrue(email)
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
  }

  public void atualizar(Long id, UsuarioCriacaoDto dto) {
        Usuario usuario = usuarioRepository.findById(id)
          .filter(encontrado -> Boolean.TRUE.equals(encontrado.getAtivo()))
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
    usuario.setNome(dto.getNome());
    usuario.setEmail(dto.getEmail());
    usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
    usuarioRepository.save(usuario);
  }

  @Transactional
  public void desativar(Long id, Usuario usuarioExecutor) {
    Usuario usuario = buscarPorIdIncluindoInativo(id);
    if (usuarioExecutor != null && id.equals(usuarioExecutor.getId())) {
      throw new EntidadeConflitanteException("Um administrador não pode desativar a própria conta");
    }
    if (Boolean.TRUE.equals(usuario.getAtivo())
            && usuario.getPermissao() != null
            && "ROLE_ADMIN".equals(usuario.getPermissao().getNome())
            && usuarioRepository.countByPermissaoNomeAndAtivoTrue("ROLE_ADMIN") <= 1) {
      throw new EntidadeConflitanteException("O último administrador ativo não pode ser desativado");
    }
    usuario.desativar(usuarioExecutor);
    usuarioRepository.save(usuario);
  }

  public void deletar(Long id) {
    desativar(id, null);
  }

  @Transactional
  public void reativar(Long id) {
    Usuario usuario = buscarPorIdIncluindoInativo(id);
    usuario.reativar();
    usuarioRepository.save(usuario);
  }

  public void atualizarPermissao(Long id, Integer permissaoId) {
        Usuario usuario = usuarioRepository.findById(id)
          .filter(encontrado -> Boolean.TRUE.equals(encontrado.getAtivo()))
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

    Permissao permissao = permissaoRepository.findById(permissaoId)
            .orElseThrow(() -> new PermissaoNaoEncontradaException("Permissão não encontrada"));

    usuario.setPermissao(permissao);

    usuarioRepository.save(usuario);
  }

  public void alterarSenha(Long id, UsuarioSenhaDto dto) {
        Usuario usuario = usuarioRepository.findById(id)
          .filter(encontrado -> Boolean.TRUE.equals(encontrado.getAtivo()))
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

    if (!passwordEncoder.matches(dto.getSenhaAtual(), usuario.getSenha())) {
      throw new SenhaInvalidaException("Usuario ou senha invalidos"); //no caso senha
    }

    usuario.setSenha(passwordEncoder.encode(dto.getNovaSenha()));
    usuarioRepository.save(usuario);
  }

  public UsuarioResponseDto buscarUsuarioLogado() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    Usuario usuario = usuarioRepository.findByEmailAndAtivoTrue(email)
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
    return UsuarioMapper.toResponseDto(usuario);
  }

  public void verificarAcesso(UsuarioResponseDto logado) {
    if (logado.permissao() == null ||
            !logado.permissao().nome().equals("ROLE_ADMIN")) {
      String permissao = logado.permissao() == null ? "SEM_PERMISSAO" : logado.permissao().nome();
      LOGGER.warn("[SEGURANCA] Acesso administrativo negado: usuario={}, permissao={}",
              logado.email(), permissao);
      throw new AcessoNegadoexception("Você não tem permissão para acessar!");
    }
  }
}