package sptech.school.src.mapper;

import sptech.school.src.dto.usuario.*;
import sptech.school.src.entity.Permissao;
import sptech.school.src.entity.Usuario;

import java.util.List;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioRequestDto dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(dto.senha());
        usuario.setDataCadastro(dto.dataCadastro());
        if (dto.permissaoId() != null) {
            Permissao permissao = new Permissao();
            permissao.setId(dto.permissaoId());
            usuario.setPermissao(permissao);
        }
        return usuario;
    }

    public static UsuarioResponseDto toResponseDto(Usuario usuario) {
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getDataCadastro(),
                usuario.getPermissao() != null ? PermissaoMapper.toResponseDto(usuario.getPermissao()) : null
        );
    }

    public static List<UsuarioResponseDto> toResponseDtoList(List<Usuario> usuarios) {
        return usuarios.stream().map(UsuarioMapper::toResponseDto).toList();
    }


    //Metodos para auxilio do Jwt
    public static Usuario of(UsuarioCriacaoDto usuarioCriacaoDto) {
        Usuario usuario = new Usuario();

        usuario.setEmail(usuarioCriacaoDto.getEmail());
        usuario.setNome(usuarioCriacaoDto.getNome());
        usuario.setSenha(usuarioCriacaoDto.getSenha());

        if (usuarioCriacaoDto.getPermissaoId() != null) {
            Permissao permissao = new Permissao();
            permissao.setId(usuarioCriacaoDto.getPermissaoId());
            usuario.setPermissao(permissao);
        }

        return usuario;
    }

    public static Usuario of(UsuarioLoginDto usuarioLoginDto) {
        Usuario usuario = new Usuario();

        usuario.setEmail(usuarioLoginDto.getEmail());
        usuario.setSenha(usuarioLoginDto.getSenha());

        return usuario;
    }

    public static UsuarioTokenDto of(Usuario usuario, String token) {
        UsuarioTokenDto usuarioTokenDto = new UsuarioTokenDto();

        usuarioTokenDto.setUserId(usuario.getId());
        usuarioTokenDto.setEmail(usuario.getEmail());
        usuarioTokenDto.setNome(usuario.getNome());
        usuarioTokenDto.setToken(token);

        return usuarioTokenDto;
    }

    /**
     * Mapeia para o DTO de resposta do login — sem o token.
     *
     * <p>O token não pertence ao body: ele é enviado como cookie HttpOnly
     * via {@code Set-Cookie}. Este DTO carrega apenas os dados necessários
     * para o frontend identificar o usuário na sessão.</p>
     */
    public static UsuarioSessaoDto ofSessao(UsuarioTokenDto tokenDto) {
        UsuarioSessaoDto dto = new UsuarioSessaoDto();

        dto.setUserId(tokenDto.getUserId());
        dto.setEmail(tokenDto.getEmail());
        dto.setNome(tokenDto.getNome());

        return dto;
    }

    public static UsuarioListarDto of(Usuario usuario) {
        UsuarioListarDto usuarioListarDto = new UsuarioListarDto();

        usuarioListarDto.setId(usuario.getId());
        usuarioListarDto.setEmail(usuario.getEmail());
        usuarioListarDto.setNome(usuario.getNome());

        return usuarioListarDto;
    }

}

