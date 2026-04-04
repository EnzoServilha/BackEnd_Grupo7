package sptech.school.mapper;

import sptech.school.dto.usuario.UsuarioRequestDto;
import sptech.school.dto.usuario.UsuarioResponseDto;
import sptech.school.entity.Permissao;
import sptech.school.entity.Usuario;

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
}

